package com.blackbox.server.social;

import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import com.blackbox.social.Ignore;
import com.blackbox.EntityReference;
import static com.blackbox.server.util.PersistenceUtil.insert;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 *
 *
 */
@Repository("ignoreDao")
public class IbatisIgnoreDao implements IIgnoreDao {

    @Resource(name = "sqlSessionTemplate")
    SqlSessionOperations template;

    @Transactional
    public Ignore save(Ignore ignore) {
        insert(ignore, template, "Ignore.insert");
        return ignore;
    }

    public Ignore loadByGuid(String guid) {
        return (Ignore) template.selectOne("Ignore.load", guid);
    }

    @Transactional
    public void delete(Ignore ignore) {
        template.delete("Ignore.delete", ignore.getGuid());
    }

    @SuppressWarnings({"unchecked"})
    public List<EntityReference> loadByIgnorer(String ignorerGuid) {
        return template.selectList("Ignore.loadByIgnorer", ignorerGuid);
    }

    @SuppressWarnings({"unchecked"})
    public List<EntityReference> loadByTarget(String targetGuid) {
        return template.selectList("Ignore.loadByTarget",  targetGuid);
    }

    @SuppressWarnings({"unchecked"})
    public List<EntityReference> loadByTargetandType(String targetGuid, Ignore.IgnoreType type) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("guid", targetGuid);
        params.put("type", type);

        return template.selectList("Ignore.loadByTargetAndType",  params);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<EntityReference> loadByIgnorerAndType(String ignorerGuid, Ignore.IgnoreType type) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("guid", ignorerGuid);
        params.put("type", type);

        return template.selectList("Ignore.loadByIgnorerAndType", params);
    }

    public Ignore loadByIgnorerAndTarget(String ignorerGuid, String targetGuid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ignorerGuid", ignorerGuid);
        params.put("targetGuid", targetGuid);
        return (Ignore) template.selectOne("Ignore.loadByIgnorerAndTarget", params);
    }

}