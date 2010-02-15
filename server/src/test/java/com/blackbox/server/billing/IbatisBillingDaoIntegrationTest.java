package com.blackbox.server.billing;

import com.blackbox.billing.BillingInfo;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.server.user.IUserDao;
import com.blackbox.user.User;
import org.joda.time.DateTime;
import org.junit.Test;
import org.yestech.lib.i18n.CountryEnum;

import javax.annotation.Resource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.yestech.lib.crypto.PasswordGenerator.createPassword;

public class IbatisBillingDaoIntegrationTest extends BaseIntegrationTest {

    @Resource
    IUserDao userDao;

    @Resource
    IBillingDao billingDao;


    @Test
    public void testCrud() {
        User testUser = createTestUser();
        BillingInfo bi = new BillingInfo();
        bi.getAddress().setAddress1("sdlkjskdlfj");
        bi.getAddress().setAddress2("sdlkjskdlfj");
        bi.getAddress().setCity("sdlkjskdlfj");
        bi.getAddress().setCountry(CountryEnum.ALBANIA);
        bi.setBillingPhone("lsxdkfjslkdfj");
        bi.setProvider(BillingInfo.BillingProvider.EPX);
        bi.setLastBilled(new DateTime().minusDays(13));
        bi.setUser(testUser);
        bi.setFirstName("slkdfsfdlk");
        bi.setLastName("sdlkjsdfljk");
        billingDao.save(bi);
        assertNotNull(bi.getVersion());
        assertNotNull(bi.getCreated());
        assertNotNull(bi.getModified());

        bi = billingDao.loadByUser(testUser.getGuid());
        assertNotNull(bi);

        bi.setBillingPhone("sldkafalsdfkjasdflkj");
        billingDao.save(bi);
        BillingInfo bi2 = billingDao.loadByUser(testUser.getGuid());
        assertEquals(bi.getBillingPhone(), bi2.getBillingPhone());

    }


    public User createTestUser() {
        User user = User.createUser();
        user.setEmail(createPassword(8)+"@"+ createPassword(4)+".com");
        user.setUsername(createPassword(8));
        user.setPassword(createPassword(8));
        return userDao.save(user);
    }


}
