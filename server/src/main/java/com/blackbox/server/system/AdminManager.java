package com.blackbox.server.system;

import com.blackbox.message.IMessageManager;
import com.blackbox.server.activity.IActivityStreamDao;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.message.IMessageDao;
import com.blackbox.server.occasion.IOccasionDao;
import com.blackbox.server.user.IUserDao;
import com.blackbox.system.CacheName;
import com.blackbox.system.IAdminManager;
import com.blackbox.user.IUserManager;
import net.sf.ehcache.CacheManager;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class AdminManager implements IAdminManager {

    CacheManager cacheManager;

    @Resource
    IMessageDao messageDao;
    @Resource
    IUserDao userDao;
    @Resource
    IMediaDao mediaDao;
    @Resource
    IOccasionDao occasionDao;
    @Resource
    IActivityStreamDao activityStreamDao;

    public String ping(String text) {
        return text;
    }

    public String ping() {
        return "hello world";
    }

    public void flushCacheWithName(String name) {
        if (cacheManager.cacheExists(name)) {
            cacheManager.getCache(name).removeAll();
            return; //no need to check default cache manager
        }

        CacheManager cacheManager1 = CacheManager.getInstance();
        if (cacheManager1.cacheExists(name)) {
            cacheManager1.removalAll();
        }
    }

    public List<CacheName> getCacheNames() {
        String[] names = CacheManager.getInstance().getCacheNames();
        ArrayList<CacheName> list = new ArrayList<CacheName>(names.length);
        for (String name : names) {
            list.add(new CacheName(name));
        }
        for (String name : cacheManager.getCacheNames()) {
            list.add(new CacheName(name));
        }
        return list;
    }

    public void reindex(final String simpleName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if ("Message".equalsIgnoreCase(simpleName)) {
                    messageDao.reindex();
                }
                else if ("User".equalsIgnoreCase(simpleName)) {
                    userDao.reindex();
                }
                else if ("MediaMetaData".equalsIgnoreCase(simpleName) || "media".equalsIgnoreCase(simpleName)) {
                    mediaDao.reindex();
                }
                else if ("occasion".equalsIgnoreCase(simpleName)) {
                    occasionDao.reindex();
                }
                else if ("activity".equalsIgnoreCase(simpleName)) {
                    activityStreamDao.reindex();
                }
            }
        }).start();
    }


    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
