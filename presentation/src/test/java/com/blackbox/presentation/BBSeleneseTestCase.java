package com.blackbox.presentation;

import com.thoughtworks.selenium.SeleneseTestCase;
import org.junit.Before;

import static java.lang.System.getProperty;

/**
 * @author A.J. Wright
 */
public class BBSeleneseTestCase extends SeleneseTestCase {

    @Before
    public void setUp() throws Exception {
        setUp(getTargetHost(), getBrowser());
    }

    protected String getBrowser() {
        return getProperty("selenium.browser");
    }

    protected String getTargetHost() {
        return getProperty("selenium.target.host");
    }

    public String getTestUsername() {
        return getProperty("selenium.testuser.username");
    }

    public String getTestUserPassword() {
        return getProperty("selenium.testuser.password");
    }

    protected void login() {
        selenium.open("/login");
        selenium.type("login_username", getTestUsername());
        selenium.type("login_password", getTestUserPassword());
        selenium.click("//button[@name='login']");
        waitForPageLoad();
    }

    protected void waitForPageLoad() {
        selenium.waitForPageToLoad("30000");
    }
}
