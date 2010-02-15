package com.blackbox.server.group;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.yestech.event.multicaster.IEventMulticaster;
import com.blackbox.group.Group;
import com.blackbox.server.group.event.LoadGroupEvent;

/**
 *
 *
 */
@SuppressWarnings({"unchecked"})
@RunWith(JMock.class)
public class GroupManagerUnitTest
{

    Mockery mockery = new JUnit4Mockery();


    @Test
    public void testLoadGroupById()
    {

        final IEventMulticaster eventMulticaster = mockery.mock(IEventMulticaster.class, "eventMulticaster");

        final Group g = Group.createGroup();
        g.setDescription("lskdffjdls");
        g.setName("sdlkjfldfks");

        mockery.checking(new Expectations() {
            {
                oneOf(eventMulticaster).process(new LoadGroupEvent(g.getGuid()));
                will(returnValue(g));
            }
        });

        GroupManager groupManager = new GroupManager();
        groupManager.setEventMulticaster(eventMulticaster);

        Group result = groupManager.loadGroupByGuid(g.getGuid());
        assertEquals(g, result);
    }
}
