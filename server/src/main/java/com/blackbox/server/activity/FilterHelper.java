package com.blackbox.server.activity;

import com.blackbox.social.NetworkTypeEnum;

import java.util.Collection;
import java.util.Collections;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author colin@blackboxrepublic.com
 */
public final class FilterHelper {

    private static final Collection<NetworkTypeEnum> instance =
            Collections.unmodifiableCollection(newArrayList(NetworkTypeEnum.FRIENDS, NetworkTypeEnum.FOLLOWING, NetworkTypeEnum.ALL_MEMBERS, NetworkTypeEnum.WORLD));

    public static Collection<NetworkTypeEnum> everyoneFilter() {
        return instance;
    }

    public static boolean isEveryOneFilter(Collection<NetworkTypeEnum> networkTypeEnums) {
        return networkTypeEnums.containsAll(everyoneFilter());
    }

    public static boolean isFriendFilter(Collection<NetworkTypeEnum> types) {
        return types.size() == 1 && types.contains(NetworkTypeEnum.FRIENDS);
    }

    public static boolean isFollowingFilter(Collection<NetworkTypeEnum> types) {
        return types.size() == 1 && types.contains(NetworkTypeEnum.FOLLOWING);
    }


}
