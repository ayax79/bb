package com.blackbox.server.activity.engine;

import com.blackbox.foundation.activity.IActivity;
import com.blackbox.server.activity.FilterHelper;
import com.blackbox.foundation.social.NetworkTypeEnum;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 *
 *
 */
public class EngineFactory {
    private static Map<EngineType, IActivityStreamEngine> engines = newHashMap();

    /**
     * @return engine based on activity mapping based on the activity's recipientDepth
     */
    public static IActivityStreamEngine create(IActivity activity) {
        if (NetworkTypeEnum.FOLLOWERS.equals(activity.getRecipientDepth())) {
            //following
            return engines.get(EngineType.FOLLOW);
        } else if (NetworkTypeEnum.FRIENDS.equals(activity.getRecipientDepth())) {
            //friends
            return engines.get(EngineType.FRIEND);
        } else if (isPublicView(activity)) {
            //all public
            return engines.get(EngineType.GLOBAL);
        } else {
            return engines.get(EngineType.PERSONAL);
        }
    }

    @Required
    public void setEngines(Map<EngineType, IActivityStreamEngine> tempEngines) {
        if (tempEngines == null) {
            throw new IllegalArgumentException("tempEngines cant be null");
        }
        engines.putAll(tempEngines);
    }

    public static IActivityStreamEngine create(EngineType engineType) {
        return engines.get(engineType);
    }

    public static IActivityStreamEngine create(Collection<NetworkTypeEnum> networkTypeEnums) {
        if (FilterHelper.isFollowingFilter(networkTypeEnums)) {
            return engines.get(EngineType.FOLLOW);
        } else if (FilterHelper.isFriendFilter(networkTypeEnums)) {
            return engines.get(EngineType.FRIEND);
        } else if (FilterHelper.isEveryOneFilter(networkTypeEnums)) {
            return engines.get(EngineType.EVERYONE_FILTER);
        } else {
            return engines.get(EngineType.GLOBAL);
        }
    }

    public static boolean isPublicView(IActivity activityToSave) {
        return NetworkTypeEnum.ALL_MEMBERS.equals(activityToSave.getRecipientDepth()) ||
                NetworkTypeEnum.WORLD.equals(activityToSave.getRecipientDepth());
    }

    public static IActivityStreamEngine create(NetworkTypeEnum type) {
        IActivityStreamEngine engine = null;
        if (NetworkTypeEnum.FOLLOWERS.equals(type)) {
            engine = engines.get(EngineType.FOLLOW);
        } else if (NetworkTypeEnum.FRIENDS.equals(type)) {
            engine = engines.get(EngineType.FRIEND);
        }
        return engine;
    }
}
