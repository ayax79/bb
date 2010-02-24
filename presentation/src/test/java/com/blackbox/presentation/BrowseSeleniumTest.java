package com.blackbox.presentation;

public class BrowseSeleniumTest extends BBSeleneseTestCase {
    
    public void testBrowseSite() throws Exception {
        login();
        selenium.click("//ul[@id='nav-left']/li[2]/a/span");
        waitForPageLoad();
        selenium.click("link=Pictures");
        selenium.click("link=Activity");
        selenium.click("link=Vouches");
        selenium.click("//ul[@id='nav-left']/li[3]/a/span");
        waitForPageLoad();
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
        waitForPageLoad();
        selenium.click("link=Members");
        selenium.click("//ul[@id='nav-left']/li[5]/a/span");
        waitForPageLoad();
        selenium.click("//div[@id='explore-tabs-top']/ul/li[2]/a");
        selenium.click("link=Members");
        selenium.click("//a[@id='user-settings-button']/span");
        selenium.click("//div[@id='header-settings']/div/ul/li[4]/a/span");
        waitForPageLoad();
        selenium.click("link=Privacy");
        selenium.click("link=Account");
        selenium.click("register");
        waitForPageLoad();
        selenium.click("//ul[@id='nav-left']/li[1]/a/span");
        waitForPageLoad();
        selenium.click("//a[@id='scene-more-link']/span");
        selenium.click("//a[@id='user-settings-button']/span");
        selenium.click("//div[@id='header-settings']/div/ul/li[5]/a/span");
        waitForPageLoad();
    }

}
