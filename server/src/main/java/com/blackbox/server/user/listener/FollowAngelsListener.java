package com.blackbox.server.user.listener;

import org.yestech.event.annotation.ListenedEvents;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.ResultReference;
import com.blackbox.server.user.event.RegisterUserEvent;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.social.ISocialManager;
import com.blackbox.social.Relationship;
import static com.blackbox.social.Relationship.RelationStatus.FOLLOW;
import com.blackbox.user.User;
import com.blackbox.EntityReference;
import com.blackbox.EntityType;

import javax.annotation.Resource;

@AsyncListener
@ListenedEvents(com.blackbox.server.user.event.RegisterUserEvent.class)
public class FollowAngelsListener extends BaseBlackboxListener<RegisterUserEvent, Object>{

    @Resource ISocialManager socialManager;

    @Override
    public void handle(RegisterUserEvent registerUserEvent, ResultReference<Object> objectResultReference) {
        User user = registerUserEvent.getType();
        socialManager.relate(new Relationship(user.toEntityReference(), new EntityReference(EntityType.USER, "1"), FOLLOW.getWeight()));
    }
}
