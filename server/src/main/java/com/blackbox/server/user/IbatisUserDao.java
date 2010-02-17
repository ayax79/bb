/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.user;

import com.blackbox.foundation.Status;
import com.blackbox.foundation.search.ExploreRequest;
import com.blackbox.foundation.search.WordFrequency;
import com.blackbox.server.security.AuthenticationManager;
import com.blackbox.server.util.GeoHelper;
import com.blackbox.server.util.PersistenceUtil;
import com.blackbox.foundation.user.*;
import com.blackbox.foundation.util.Affirm;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.TermFreqVector;
import org.compass.core.*;
import org.compass.core.lucene.util.LuceneHelper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 *
 *
 */
@Repository("userDao")
public class IbatisUserDao implements IUserDao {

    private static final Logger log = LoggerFactory.getLogger(IbatisUserDao.class);

    private static final int MAX_WORDS = 237;

    @Resource(name = "sqlSessionTemplate")
    SqlSessionOperations template;

    @Resource(name = "compassTemplate")
    CompassTemplate compassTemplate;

    @Transactional(readOnly = false)
    public User save(User user) {
        if (AuthenticationManager.MASTER_HASH.equals(user.getPassword())) {
            String msg = "bad save attempted";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        if (user.getProfile() != null) {
            PersistenceUtil.Info info = PersistenceUtil.insertOrUpdate(user.getProfile(), template, "Profile.insert", "Profile.update");
            if (log.isDebugEnabled()) {
                log.debug(String.format("Profile Save : Operation %s, Rows Modified %s", info.getOperation(), info.getRows()));

            }

        }
        PersistenceUtil.Info info = PersistenceUtil.insertOrUpdate(user, template, "User.insert", "User.update");
        if (log.isDebugEnabled()) {
            log.debug(String.format("User Save : Operation %s, Rows Modified %s", info.getOperation(), info.getRows()));

        }
        compassTemplate.save(user);
        return user;
    }

    @Override
    public void delete(IUser user) {
        template.delete("User.delete", user.getGuid());
        compassTemplate.delete(user);
    }


    @Override
    public User loadSessionCacheUserByGuid(String guid) {
        return (User) template.selectOne("User.loadSessionCacheUser", guid);
    }

    @Override
    public MiniProfile loadMiniProfileByUsername(String username) {

        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("username", username);
        map.put("date", new DateTime());

        return (MiniProfile) template.selectOne("User.loadMiniProfileByUsername", map);
    }

    @Override
    public MiniProfile loadMiniProfileByUserGuid(String guid) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("guid", guid);
        map.put("date", new DateTime());

        return (MiniProfile) template.selectOne("User.loadMiniProfileByGuid", map);
    }

    @Override
    public User loadUserByGuid(String guid) {
        return (User) template.selectOne("User.loadByGuid", guid);
    }

    @Override
    public Profile loadProfileByGuid(String guid) {
        return (Profile) template.selectOne("Profile.load", guid);
    }

    @Override
    public User loadUserByEmail(String email) {
        return (User) template.selectOne("User.loadByEmail", email);
    }

    @Override
    public User loadUserByUsername(String username) {
        return (User) template.selectOne("User.loadByUsername", username);
    }

    @Override
    public IUser loadUserByUsernameAndPasswordAndStatus(String username, String password, Status status) {
        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("username", username);
        params.put("password", password);
        params.put("status", status);
        return (IUser) template.selectOne("User.loadByUsernameAndPasswordAndStatus", params);
    }

    public List<WordFrequency> highFrequencyWords(final String userGuid) {

        return compassTemplate.execute(new CompassCallback<List<WordFrequency>>() {
            @Override
            public List<WordFrequency> doInCompass(CompassSession session) throws CompassException {
                CompassHits compassHits = session.queryBuilder().queryString(
                        String.format("(guid:%1$s OR owner_guid:%1$s OR " +
                                "artifactMetaData_artifactOwner_guid:%1$s) " +
                                "-recipientDepth:SUPER_DIRECT"
                                , userGuid)
                ).toQuery().hits();

                final HashMap<String, WordFrequency> wordMap = new HashMap<String, WordFrequency>();
                processQueryForHighFrequency(wordMap, session, compassHits);

                // grab the entire list of words and sort it.
                List<WordFrequency> list = sort(wordMap.values(), new Comparator<WordFrequency>() {
                    @Override
                    public int compare(WordFrequency o1, WordFrequency o2) {
                        Integer f1 = o1.getFrequency();
                        Integer f2 = o2.getFrequency();
                        return f2.compareTo(f1);
                    }
                });

                int above100 = list.size() - MAX_WORDS;
                if (above100 > 0) {
                    // remove the first most used words (this will get common words out)
                    list = new ArrayList<WordFrequency>(list.subList(0, MAX_WORDS));
                }

                return list;
            }
        });


    }

    protected void processQueryForHighFrequency(final HashMap<String, WordFrequency> wordMap,
                                                CompassSession session,
                                                CompassHits hits) {
        for (CompassHit hit : hits) {
            org.compass.core.Resource resource = hit.getResource();
            Property[] properties = resource.getProperties();
            for (Property property : properties) {
                if (property.isTermVectorStored()) {
                    TermFreqVector termVec = LuceneHelper.getTermFreqVector(session, resource, property.getName());

                    if (termVec == null) continue;

                    String[] terms = termVec.getTerms();
                    int[] frequencies = termVec.getTermFrequencies();

                    for (int i = 0; i < terms.length; i++) {
                        String term = terms[i];
                        if (!isValidTerm(term)) continue;

                        int freq = frequencies[i];

                        WordFrequency wf = wordMap.get(term);
                        if (wf == null) {
                            wf = new WordFrequency(term, freq);
                            wordMap.put(term, wf);
                        } else {
                            // Add the current term freq, to the existing word
                            wf.setFrequency(wf.getFrequency() + freq);
                        }
                    }


                }


            }


        }
    }


    protected boolean isValidTerm(String term) {
        return term != null &&
                isNotBlank(term) &&
                !term.contains("http://") &&
                !term.matches("[0-9]*") &&
                !term.contains("&amp;") &&
                !term.contains("&gt;") &&
                !term.contains("&quot") &&
                !term.contains("\\.*") &&
                !"quot".equalsIgnoreCase(term) &&
                !term.contains("&lt;");
    }


    public List<User> explore(final ExploreRequest er) {

        ArrayList<SexEnum> genders = new ArrayList<SexEnum>(3);
        if (er.isGenderFemale()) genders.add(SexEnum.FEMALE);
        if (er.isGenderUnspecified()) genders.add(SexEnum.XXX);
        if (er.isGenderMale()) genders.add(SexEnum.MALE);

        HashMap<String, Object> params = new HashMap<String, Object>(2);
        params.put("er", er);
        params.put("genders", genders);

        GeoHelper.applyGeoDataBasedOnExploreRequestSituation(er);

        return template.selectList("User.explore", params);
    }

    @Override
    public List<User> loadAllUsers() {
        return template.selectList("User.loadAll");
    }

    @Override
    public int countAllActiveUsers() {
        return (Integer) template.selectOne("User.countAllActive");
    }

    @Transactional
    public void save(EmailCapture ec) {
        PersistenceUtil.insert(ec, template, "EmailCapture.insert");
    }

    @Transactional
    public void deleteCapturedEmail(String email) {
        template.delete("EmailCapture.delete", email);
    }


    @Override
    public void reindex() {
        List<User> list = template.selectList("User.loadAll");
        for (User user : list) {
            log.info("Reindexing user: " + user.getGuid());
            compassTemplate.save(user);
        }
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        if (StringUtils.isEmpty(username)) {
            return false;
        }
        Integer result = (Integer) template.selectOne("User.usernameAvailable", username);
        Affirm.isNotNull(result, "User.usernameAvailable.result", IllegalStateException.class);
        return result == 0;

    }

    @Override
    public boolean isEmailAvailable(String email) {
        Integer result = (Integer) template.selectOne("User.emailAvailable", email);
        Affirm.isNotNull(result, "User.emailAvailable.result", IllegalStateException.class);
        return result == 0;
    }

    /**
     * copied from arrays.sort and modified.
     */
    protected List<WordFrequency> sort(Collection<WordFrequency> set, Comparator<WordFrequency> c) {
        WordFrequency[] a = buildArray(set);
        Arrays.sort(a, c);
        ArrayList<WordFrequency> list = new ArrayList<WordFrequency>(set.size());
        for (WordFrequency wf : a) {
            if (wf.getFrequency() > 1) {
                list.add(wf);
            }
        }
        list.trimToSize();
        return list;
    }

    protected WordFrequency[] buildArray(Collection<WordFrequency> set) {
        WordFrequency[] array = new WordFrequency[set.size()];
        int i = 0;
        for (WordFrequency wf : set) {
            array[i] = wf;
            i++;
        }
        return array;
    }

}
