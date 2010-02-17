package com.blackbox.server.occasion;

import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.server.user.IUserDao;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.Bounds;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.orm.ibatis3.SqlSessionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author colin@blackboxrepublic.com
 */
public class IbatisOccasionDaoIntegrationTest extends BaseIntegrationTest {

    @Resource
    IOccasionDao occasionDao;

    @Resource
    IUserDao userDao;

    @Resource
    SqlSessionTemplate template;

    private ArrayList<String> broken = new ArrayList<String>();

    @Ignore
    @Test
    public void findDuplicates() {
        List<User> users = template.selectList("User.loadAll");
        for (User user : users) {
            for (Occasion occasion : occasionDao.loadOccasionsByAttendee(user.getGuid(), Bounds.boundLess()).getResults()) {
                doLookup(user, occasion);
            }
        }
        System.err.println("broken = " + broken);
    }

    private void doLookup(User user, Occasion occasion) {
        try {
            occasionDao.loadAttendeeByGuidAndOccasionGuid(occasion.getGuid(), user.getGuid());
        } catch (Exception e) {
            broken.add(user.getGuid() + "|" + occasion.getGuid());
        }
    }
}
