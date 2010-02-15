package com.blackbox.server.message.publisher;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.Mock;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import org.apache.camel.Exchange;
import com.blackbox.server.social.INetworkDao;
import com.blackbox.server.activity.IActivityStreamDao;
import com.blackbox.message.Message;
import com.blackbox.activity.ActivityFactory;
import com.blackbox.social.NetworkTypeEnum;
import com.blackbox.social.Relationship;
import com.blackbox.EntityReference;

import java.util.List;

/**
 *
 *
 */
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class SaveFollowersMessagesToActivityStreamsUnitTest {
    @Mock
    private INetworkDao mockNetworkDao;
    @Mock
    private IActivityStreamDao mockActivityStreamDao;
    @Mock
    private Exchange mockExchange;
    @Mock
    private org.apache.camel.Message mockMessage;
    @Mock
    private List<Relationship> mockRelationships;

    private SaveFollowersMessagesToActivityStreams publisher;


    @Before
    public void setUp() {
        publisher = new SaveFollowersMessagesToActivityStreams();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSaveWorld() {
        Message testMessage = ActivityFactory.createMessage();
        String testGuid = "mockGuid";
        testMessage.getArtifactMetaData().setArtifactOwner(EntityReference.createEntityReference(testGuid));

        testMessage.setRecipientDepth(NetworkTypeEnum.WORLD);
        when(mockExchange.getIn()).thenReturn(mockMessage);
        when(mockMessage.getBody(Message.class)).thenReturn(testMessage);
        when(mockNetworkDao.loadRelationshipsByFromEntityGuidAndWeightRange(testGuid,
                Relationship.RelationStatus.FOLLOW.getWeight(), Relationship.RelationStatus.FRIEND_PENDING.getWeight()))
                .thenReturn(mockRelationships);
        when(mockRelationships.size()).thenReturn(2);
        Relationship relationship1 = new Relationship();
        Relationship relationship2 = new Relationship();

        when(mockRelationships.get(0)).thenReturn(relationship1);
        when(mockRelationships.get(2)).thenReturn(relationship2);

        doNothing().when(mockActivityStreamDao).saveRecipient(any(Message.class), NetworkTypeEnum.FOLLOWERS);
        publisher.setNetworkDao(mockNetworkDao);
        publisher.setActivityStreamDao(mockActivityStreamDao);
        publisher.save(mockExchange);


    }
}
