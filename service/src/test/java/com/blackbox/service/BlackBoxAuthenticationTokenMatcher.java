/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.service;

import org.apache.commons.lang.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import com.blackbox.security.BlackBoxAuthenticationToken;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class BlackBoxAuthenticationTokenMatcher extends TypeSafeMatcher<BlackBoxAuthenticationToken> {
    private BlackBoxAuthenticationToken param;

    public BlackBoxAuthenticationTokenMatcher(BlackBoxAuthenticationToken param) {
        this.param = param;
    }

    @Override
    public boolean matchesSafely(BlackBoxAuthenticationToken blackBoxAuthenticationToken) {
        boolean valid = false;
        if (blackBoxAuthenticationToken != null &&
                param.getUsername().equals(blackBoxAuthenticationToken.getUsername())
                && blackBoxAuthenticationToken.getPassword() != null
                && param.getRealm().equals(blackBoxAuthenticationToken.getRealm())) {
            String paramPassword = new String(param.getPassword());
            String password = new String(blackBoxAuthenticationToken.getPassword());
            valid = StringUtils.equals(paramPassword, password);
        }
        return valid;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("usernamepassword token ").appendValue(param);
    }

    @Factory
    public static Matcher<BlackBoxAuthenticationToken> loginToken( BlackBoxAuthenticationToken param ) {
        return new BlackBoxAuthenticationTokenMatcher(param);
    }
}