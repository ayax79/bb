package com.blackbox.server.group;

import com.blackbox.group.IGroupManager;
import com.blackbox.group.Group;
import com.blackbox.server.group.event.LoadGroupEvent;
import org.yestech.event.multicaster.BaseServiceContainer;
import org.yestech.event.multicaster.IEventMulticaster;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *
 *
 */
public class GroupManager extends BaseServiceContainer implements IGroupManager
{

    @Resource(name = "eventMulticaster")
    private IEventMulticaster eventMulticaster;

    public void setEventMulticaster(IEventMulticaster eventMulticaster)
    {
        this.eventMulticaster = eventMulticaster;
    }

    @SuppressWarnings({"unchecked"})
    @Transactional(readOnly = true)
    public Group loadGroupByGuid(String guid)
    {
        return (Group) eventMulticaster.process(new LoadGroupEvent(guid));
    }
    
}
