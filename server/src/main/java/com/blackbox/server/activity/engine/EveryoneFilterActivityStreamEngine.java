package com.blackbox.server.activity.engine;

import com.blackbox.EntityReference;
import com.blackbox.activity.*;
import com.blackbox.social.NetworkTypeEnum;
import com.blackbox.util.Bounds;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newTreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import static java.util.Collections.sort;

/**
 *
 *
 */
@Component("everyoneFilterActivityStreamEngine")
@SuppressWarnings("unchecked")
public class EveryoneFilterActivityStreamEngine extends BaseActivityStreamEngine {
    final private static Logger logger = LoggerFactory.getLogger(EveryoneFilterActivityStreamEngine.class);

    @Resource(name = "globalActivityStreamEngine")
    private GlobalActivityStreamEngine globalEngine;

    @Resource(name = "privateFollowActivityStreamEngine")
    private PrivateFollowActivityStreamEngine privateFollowEngine;

    @Resource(name = "privateFriendActivityStreamEngine")
    private PrivateFriendActivityStreamEngine privateFriendEngine;

    public GlobalActivityStreamEngine getGlobalEngine() {
        return globalEngine;
    }

    public void setGlobalEngine(GlobalActivityStreamEngine globalEngine) {
        this.globalEngine = globalEngine;
    }

    public PrivateFollowActivityStreamEngine getPrivateFollowEngine() {
        return privateFollowEngine;
    }

    public void setPrivateFollowEngine(PrivateFollowActivityStreamEngine privateFollowEngine) {
        this.privateFollowEngine = privateFollowEngine;
    }

    public PrivateFriendActivityStreamEngine getPrivateFriendEngine() {
        return privateFriendEngine;
    }

    public void setPrivateFriendEngine(PrivateFriendActivityStreamEngine privateFriendEngine) {
        this.privateFriendEngine = privateFriendEngine;
    }

    @Override
    public Collection<IActivityThread> loadThreads(EntityReference entity, Collection<NetworkTypeEnum> types, Bounds bounds) {
        TreeSet<IActivityThread> threads = newTreeSet(DescendingActivityThreadComparator.getDescendingActivityThreadComparator());
        Collection<IActivityThread> globalThreads = globalEngine.loadThreads(entity, types, bounds);
        Collection<IActivityThread> friendThreads = privateFriendEngine.loadThreads(entity, types, bounds);
        Collection<IActivityThread> followThreads = privateFollowEngine.loadThreads(entity, types, bounds);
        threads.addAll(globalThreads);
        threads.addAll(friendThreads);
        threads.addAll(followThreads);
        //remove dups
        int maxStop = bounds.getMaxResults();
        int totalThreads = threads.size();
        int stopIdx = (maxStop < totalThreads) ? maxStop : totalThreads;
        int current = 0;
        List<IActivityThread> result = newArrayList();
        for (IActivityThread thread : threads) {
            if (current < stopIdx) {
                result.add(thread);
                ++current;
            } else {
                break;
            }
        }
        return result;
    }

}