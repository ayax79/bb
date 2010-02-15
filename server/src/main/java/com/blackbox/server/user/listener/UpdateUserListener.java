package com.blackbox.server.user.listener;

import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;
import com.blackbox.user.User;
import com.blackbox.user.IUser;
import com.blackbox.server.user.event.UpdateUserEvent;
import com.blackbox.server.user.IUserDao;
import com.blackbox.server.BaseBlackboxListener;

import javax.annotation.Resource;

@ListenedEvents(UpdateUserEvent.class)
public class UpdateUserListener extends BaseBlackboxListener<UpdateUserEvent, IUser>
{

    private IUserDao userDao;

    @Resource(name = "userDao")
    public void setUserDao(IUserDao userDao)
    {
        this.userDao = userDao;
    }

    
    @Override
    public void handle(UpdateUserEvent updateUserEvent, ResultReference<IUser> result)
    {

        User user = updateUserEvent.getType();
        userDao.save(user);
        result.setResult(user);
    }
}
