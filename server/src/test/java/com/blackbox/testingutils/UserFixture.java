package com.blackbox.testingutils;

import com.blackbox.foundation.user.User;

/**
 * @author colin@blackboxrepublic.com
 */
public class UserFixture {

    public static final UserParts sam = new UserParts("sam", "412566b7f3e257e22bab9c8f697056edc9779481");
    public static final UserParts april = new UserParts("april", "105433d2148545d371fb1c71ef4851efb8c04392");
    public static final UserParts aj = new UserParts("aj", "35c68cff468635c4308390d25143d4451fc44de7");

    public static class UserParts {
        private String userName;
        private String guid;

        public UserParts(String userName, String guid) {
            this.userName = userName;
            this.guid = guid;
        }

        public String getUserName() {
            return userName;
        }

        public String getGuid() {
            return guid;
        }

        public User toUser() {
            return new User(userName, guid, null, null);
        }

    }
}
