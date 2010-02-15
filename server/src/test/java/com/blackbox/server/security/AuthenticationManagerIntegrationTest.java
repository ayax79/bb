package com.blackbox.server.security;

import com.blackbox.security.*;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.user.User;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import static org.yestech.lib.crypto.MessageDigestUtils.sha1Hash;

import javax.annotation.Resource;

/**
 * @author A.J. Wright
 */
public class AuthenticationManagerIntegrationTest extends BaseIntegrationTest {

    @Resource(name = "authenticationManager")
    IAuthenticationManager authenticationManager;

    @Test
    public void testLoad() {
        assertNotNull(authenticationManager);
        BlackBoxAuthenticationToken token = new BlackBoxAuthenticationToken("aj", sha1Hash("testing"));
        token.setRememberMe(true);
        token.setRealm(SecurityRealmEnum.WEB);
        IBlackBoxAuthenticationInfo authInfo = authenticationManager.load(token);
        assertNotNull(authInfo);
        User user = (User) authInfo.getPrincipals().getPrimaryPrincipal();
        assertNotNull(user);
    }

}
