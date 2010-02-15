package com.blackbox.presentation.action.persona;

import com.blackbox.presentation.extension.BlackBoxContext;
import com.blackbox.presentation.extension.MockBlackBoxContext;
import com.blackbox.social.ISocialManager;
import com.blackbox.social.Relationship;
import com.blackbox.user.User;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletResponse;

import static com.blackbox.social.Relationship.RelationStatus;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EditRelationshipsActionBeanUnitTest {

    @Mock
    ISocialManager socialManager;

    @Mock
    PersonaHelper personaHelper;

    @Mock
    HttpServletResponse response;


    EditRelationshipsActionBean actionBean;
    BlackBoxContext context;


    @Before
    public void setup() {
        actionBean = new EditRelationshipsActionBean();
        actionBean.socialManager = socialManager;
        actionBean.personaHelper = personaHelper;
        context = spy(new MockBlackBoxContext());
        actionBean.setContext(context);
        when(context.getResponse()).thenReturn(response);
    }

    @Test
    public void testDelete() throws Exception {
        User currentUser = User.createUser();
        User user2 = User.createUser();
        when(context.getUser()).thenReturn(currentUser);
        actionBean.setGuid(user2.getGuid());
        Relationship r = Relationship.createRelationship(currentUser, user2, RelationStatus.IN_RELATIONSHIP);
        r.setDescription("lsdjfsldkfj");
        when(socialManager.loadRelationshipByEntities(currentUser.getGuid(), user2.getGuid())).thenReturn(r);

        Resolution resolution = actionBean.delete();
        assertNotNull(resolution);
        assertTrue(resolution instanceof StreamingResolution);
        assertNull(r.getDescription());
        assertEquals(RelationStatus.FRIEND.getWeight(), r.getWeight());

        verify(socialManager).loadRelationshipByEntities(currentUser.getGuid(), user2.getGuid());
        verify(socialManager).relate(r);
    }

}
