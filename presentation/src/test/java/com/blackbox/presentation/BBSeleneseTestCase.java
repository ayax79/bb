package com.blackbox.presentation;

import com.thoughtworks.selenium.SeleneseTestCase;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.System.getProperty;

/**
 * @author A.J. Wright
 */
public class BBSeleneseTestCase extends SeleneseTestCase {

    private static final Logger logger = LoggerFactory.getLogger(BBSeleneseTestCase.class);

    @Before
    public void setUp() throws Exception {
        String targetHost = getTargetHost();
        String browser = getBrowser();
        logger.info(String.format("Running selenium test case with host: %s browser: %s", targetHost, browser));
        setUp(targetHost, browser);
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
