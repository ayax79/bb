package com.blackbox;

import com.thoughtworks.selenium.SeleneseTestCase;

public class BrowseSeleniumTest extends SeleneseTestCase {
    
    public void setUp() throws Exception {
        setUp("http://localhost:8080/", "*chrome");
    }
    
    public void testBrowseSite() throws Exception {
        selenium.open("/community/login");
        selenium.type("login_username", "aj");
        selenium.click("//button[@name='login']");
        selenium.waitForPageToLoad("30000");
        selenium.click("//ul[@id='nav-left']/li[2]/a/span");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Pictures");
        selenium.click("link=Activity");
        selenium.click("link=Vouches");
        selenium.click("//ul[@id='nav-left']/li[3]/a/span");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Sent");
        selenium.click("link=Archive");
        selenium.click("//div[@id='archive_container']/div[1]/ul/li[2]/a/span");
        selenium.click("//div[@id='archive_container']/div[1]/ul/li[3]/a/span");
        selenium.click("//div[@id='archive_container']/div[1]/ul/li[4]/a/span");
        selenium.click("//div[@id='archive_container']/div[1]/ul/li[5]/a/span");
        selenium.click("link=Inbox");
        selenium.click("link=exact:RE: wordcloud");
        selenium.click("//li[@id='event-nav']/a/span");
        selenium.click("link=Member Events");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Members");
        selenium.click("//ul[@id='nav-left']/li[5]/a/span");
        selenium.waitForPageToLoad("30000");
        selenium.click("//div[@id='explore-tabs-top']/ul/li[2]/a");
        selenium.click("link=Members");
        selenium.click("//a[@id='user-settings-button']/span");
        selenium.click("//div[@id='header-settings']/div/ul/li[4]/a/span");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Privacy");
        selenium.click("link=Account");
        selenium.click("register");
        selenium.waitForPageToLoad("30000");
        selenium.click("//ul[@id='nav-left']/li[1]/a/span");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@id='scene-more-link']/span");
        selenium.click("//a[@id='user-settings-button']/span");
        selenium.click("//div[@id='header-settings']/div/ul/li[5]/a/span");
        selenium.waitForPageToLoad("30000");
    }
    
}
