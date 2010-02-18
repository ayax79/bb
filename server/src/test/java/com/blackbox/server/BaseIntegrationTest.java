/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Base clase for integration testing that sets up the hibernate session correctly.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/baseconfig.xml",
        "classpath:/spring/cache-config.xml",
        "classpath:/spring/activity-config.xml",
        "classpath:/spring/media-config.xml",
        "classpath:/spring/message-config.xml",
        "classpath:/spring/notification-config.xml",
        "classpath:/spring/commerce-config.xml",
        "classpath:/spring/diskstore-config.xml",
        "classpath:/spring/occasion-config.xml",
        "classpath:/spring/producer-messagebus-config.xml",
        "classpath:/spring/security-config.xml",
        "classpath:/spring/user-config.xml",
        "classpath:/spring/generic-listener-config.xml",
        "classpath:/camel/base-camel.xml",
        "classpath:/camel/multimediaContentPublish-camel.xml",
        "classpath:/camel/notification-camel.xml",
        "classpath:/camel/textMessagePublish-camel.xml"})
public abstract class BaseIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

    private static final Logger logger = LoggerFactory.getLogger(BaseIntegrationTest.class);

    @Before
    public void possiblyWarnOfProductionUsage() {
        DataSource dataSource = (DataSource) super.applicationContext.getBean("targetDataSource");
        if (isPossiblyProductionOrSqlException(dataSource)) {
            logger.warn("Your application is accessing the PRODUCTION DATABASE!!!");
        }
    }

    private boolean isPossiblyProductionOrSqlException(DataSource dataSource) {
        try {
            return !dataSource.getConnection().getMetaData().getURL().contains("localhost") &&
                    !dataSource.getConnection().getMetaData().getURL().contains("127.0.0.1");
        } catch (SQLException e) {
            logger.warn("Unable to determine data source url!", e);
            return true;
        }
    }


}
