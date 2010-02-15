package com.blackbox.server.bookmark.listener;

import com.blackbox.bookmark.Bookmark;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.bookmark.event.CreateBookmarkEvent;
import com.blackbox.server.system.email.EmailDefinition;
import com.blackbox.server.system.email.SimpleEmailDelivery;
import com.blackbox.user.IUserManager;
import com.blackbox.user.User;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;
import org.yestech.event.annotation.AsyncListener;

import javax.annotation.Resource;

/**
 * @author A.J. Wright
 */
@ListenedEvents(CreateBookmarkEvent.class)
@AsyncListener
public class WishEmailListener extends BaseBlackboxListener<CreateBookmarkEvent, Object> {

    @Resource
    SimpleEmailDelivery emailDelivery;

    @Resource
    IUserManager userManager;

    @Override
    public void handle(CreateBookmarkEvent event, ResultReference<Object> objectResultReference) {

        Bookmark bookmark = event.getType();
        if(bookmark.getType() == Bookmark.BookmarkType.WISH) {

            final User target = userManager.loadUserByGuid(bookmark.getTarget().getGuid());
            final User owner = userManager.loadUserByGuid(bookmark.getOwner().getGuid());

            emailDelivery.send(new EmailDefinition() {
                {
                    withRecipient(target.fullName(), target.getEmail());
                    withSubject("OMG! %s, just put a wish on you!", owner.getUsername());
                    withTextTemplate("/velocity/wishEmail-text.vm");
                    withHtmlTemplate("/velocity/wishEmail-html.vm");
                    withTemplateVariable("name",target.getName());
                    withTemplateVariable("wishing_username", owner.getUsername());
                }
            });
        }
    }
}
