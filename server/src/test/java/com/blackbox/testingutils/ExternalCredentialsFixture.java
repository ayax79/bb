package com.blackbox.testingutils;

import com.blackbox.foundation.user.FacebookCredentials;
import com.blackbox.foundation.user.User;

/**
 * @author colin@blackboxrepublic.com
 */
public class ExternalCredentialsFixture {

    public static final UserParts blackboxTweeter = new UserParts("johnieblackbox", "bla5ph3my");
    public static final UserParts facebookLogin = new UserParts("johnnie.blackbox", "bla5ph3my");

//    public static final FacebookCredentials matthews-facebookPoster =
//            new FacebookCredentials("445ade94d79c830c302a5cd7370b88a1", "89b58ceee15527591b8d87fb5bf6fd57", "cba55eaddfcb4b5d70d1c3b9-1661240846", true);
    public static final FacebookCredentials facebookPoster =
            new FacebookCredentials("445ade94d79c830c302a5cd7370b88a1", "4aa9e20831da36d473d48998744ab915", "b4f23addee3430324e91a65f-100000720294855", true);

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