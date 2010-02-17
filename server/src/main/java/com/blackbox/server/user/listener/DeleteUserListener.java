package com.blackbox.server.user.listener;

import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;
import com.blackbox.foundation.user.IUser;
import com.blackbox.server.user.event.DeleteUserEvent;
import com.blackbox.server.user.IUserDao;
import com.blackbox.server.BaseBlackboxListener;

import javax.annotation.Resource;

/**
 *
 *
 */
@ListenedEvents(DeleteUserEvent.class)
public class DeleteUserListener extends BaseBlackboxListener<DeleteUserEvent, IUser>
{
    private IUserDao userDao;

    @Resource(name = "userDao")
    public void setUserDao(IUserDao userDao) {

        this.userDao = userDao;
    }


    @Override
    public void handle(DeleteUserEvent deleteUserEvent, ResultReference<IUser> result)
    {
        userDao.delete(deleteUserEvent.getType());
    }
}
