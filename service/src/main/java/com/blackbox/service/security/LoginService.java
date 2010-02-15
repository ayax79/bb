/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.service.security;

import com.blackbox.security.BlackBoxAuthenticationToken;
import com.blackbox.security.SecurityRealmEnum;
import com.blackbox.user.IUser;
import com.blackbox.user.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import static org.yestech.lib.crypto.MessageDigestUtils.sha1Hash;

import javax.ws.rs.PathParam;

/**
 * Login service
 */
@Service
public class LoginService implements ILoginService {
    final private static Logger logger = LoggerFactory.getLogger(LoginService.class);


    @Override
    public IUser login(@PathParam("username") String username,
                       @PathParam("password") String password, @PathParam("guid") String guid) {
        String hashedPassword = sha1Hash(password);
        IUser user = new User();
        user.setStatus(null);
        BlackBoxAuthenticationToken token = new BlackBoxAuthenticationToken(username, hashedPassword);
        token.setGuid(guid);
        token.setRealm(SecurityRealmEnum.MOBILE);
        try {
            Subject subject = SecurityUtils.getSubject();

            subject.login(token);
            user = (User) SecurityUtils.getSubject().getPrincipal();
        } catch (IncorrectCredentialsException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            token.clear();
        }
        return user;
    }

}
