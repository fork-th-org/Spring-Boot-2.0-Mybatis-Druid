package com.itunion.springbootstarterdruid.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@MapperScan(basePackages = "com.itunion.springbootstarterdruid.mapper")
@ConfigurationProperties(prefix = "druid")

public class DatasourceConfig {
    private static Logger log = LoggerFactory.getLogger(DatasourceConfig.class);
    @Value("${druid.driver}")
    private String driverClassName;
    @Value("${druid.url}")
    private String url;
    @Value("${druid.username}")
    private String username;
    @Value("${druid.password}")
    private String password;
    @Value("${druid.init-size}")
    private int initSize;
    @Value("${druid.min-idel}")
    private int minIdel;
    @Value("${druid.max-active}")
    private int maxActive;
    @Value("${druid.login.timeout.seconds}")
    private int loginTimeoutSeconds;
    @Value("${druid.query.timeout.seconds}")
    private int queryTimeoutSeconds;
    @Value("${druid.filters}")
    private String filters;

    @Bean
    public DruidDataSource dataSource() throws SQLException {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setInitialSize(initSize);
        ds.setMinIdle(minIdel);
        ds.setMaxActive(maxActive);
        ds.setLoginTimeout(loginTimeoutSeconds);
        ds.setQueryTimeout(queryTimeoutSeconds);
        ds.setFilters(filters);
        return ds;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        final SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource());
        sqlSessionFactory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        sqlSessionFactory.setFailFast(true);
        return sqlSessionFactory.getObject();
    }

    public DataSourceTransactionManager dataSourceTransactionManager() throws SQLException {
        log.debug("> transactionManager");
        return new DataSourceTransactionManager(dataSource());
    }


    @PostConstruct
    public void postConstruct() {
        log.info("jdbc settings={}", this);
    }
}
