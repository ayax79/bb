package com.blackbox.testingutils;

import com.blackbox.foundation.user.ExternalCredentials;
import com.blackbox.foundation.user.User;

/**
 * @author colin@blackboxrepublic.com
 */
public class ExternalCredentialsFixture {

    public static final UserParts blackboxTweeter = new UserParts("johnieblackbox", "bla5ph3my");
    public static final UserParts facebookPoster = new UserParts("johnnie.blackbox", "bla5ph3my");

    public static class UserParts {
        private String userName;
        private String password;

        public UserParts(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }

        public User toUser() {
            return new User(userName, null, null, null);
        }

    }


}