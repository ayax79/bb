package com.blackbox.server.user.listener;

import com.blackbox.server.user.IUserDao;
import com.blackbox.server.user.event.CreateUserEvent;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.user.User;
import com.blackbox.user.IUser;
import com.blackbox.Utils;
import org.yestech.event.annotation.ListenedEvents;
import org.yestech.event.ResultReference;

import javax.annotation.Resource;

/**
 *
 *
 */
@ListenedEvents(CreateUserEvent.class)
public class CreateUserListener extends BaseBlackboxListener<CreateUserEvent, IUser> {
    private IUserDao userDao;

    @Resource(name = "userDao")
    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void handle(CreateUserEvent event, ResultReference<IUser> reference) {
        User user = saveUser(event);
        reference.setResult(user);
    }

    public void createUser(CreateUserEvent event) {
        saveUser(event);
    }

    private User saveUser(CreateUserEvent event) {
        User user = event.getType();
        Utils.applyGuid(user);
        userDao.save(user);
        return user;
    }
}
