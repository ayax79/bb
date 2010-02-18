package com.blackbox.server.config;

import com.blackbox.foundation.exception.BlackBoxException;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.orm.ibatis3.SqlSessionTemplate;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import java.io.InputStreamReader;
import java.sql.Connection;

/**
 * @author A.J. Wright
 */
@Configuration
public class DatabaseConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Value("${jdbc.driverClassName}")
    String jdbcDriverClassName;

    @Value("${jdbc.url}")
    String jdbcUrl;

    @Value("${jdbc.username}")
    String jdbcUsername;

    @Value("${jdbc.password}")
    String jdbcPassword;

    @Value("${jdbc.jndiName}")
    String jndiName;

    @Bean
    public DataSource targetDataSource() {
        if (jndiName != null && !"NONE".equals(jndiName)) {
            try {
                InitialContext ctx = new InitialContext();
                return (DataSource) ctx.lookup(jndiName);
            } catch (NamingException e) {
                logger.error("Failed to configure database with jndi connection " + jndiName);
                throw new BlackBoxException(e.getMessage(), e);
            }
        } else {
            BasicDataSource basic = new BasicDataSource();
            basic.setUrl(jdbcUrl);
            basic.setDriverClassName(jdbcDriverClassName);
            basic.setUsername(jdbcUsername);
            basic.setPassword(jdbcPassword);
            basic.setDefaultAutoCommit(true);
            basic.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            basic.setValidationQuery("SELECT 1");
            basic.setMaxActive(100);
            basic.setMaxWait(1000L);
            return basic;
        }
    }

    @Bean
    public DataSource dataSource() {
        return new TransactionAwareDataSourceProxy(targetDataSource());
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() {
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(
                new InputStreamReader(getClass().getResourceAsStream("/ibatis/sqlmap-config.xml")));
        org.apache.ibatis.session.Configuration configuration = factory.getConfiguration();
        TransactionFactory transactionFactory = new ManagedTransactionFactory();
        Environment environment = new Environment("SqlSessionFactory", transactionFactory, dataSource());
        configuration.setEnvironment(environment);
        return factory;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate() {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

}
