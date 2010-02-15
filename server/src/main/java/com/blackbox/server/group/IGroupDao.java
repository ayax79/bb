package com.blackbox.server.group;

import com.blackbox.group.Group;

/**
 *
 *
 */
public interface IGroupDao
{
    Group save(Group group);

    void delete(Group group);

    Group loadGroupByGuid(String guid);

}
