package com.yqn.op.log.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author huayuanlin
 * @date 2021/08/05 17:42
 * @desc the class desc
 */
@Configuration
public class DatasourceAutoConfiguration {


    @Bean
    public DefaultDatasourcePrimarySetterBean defaultDatasourcePrimarySetterBean() {
        return new DefaultDatasourcePrimarySetterBean();
    }

    @Bean("opLogDatasourceProperties")
    @ConfigurationProperties("op.log.datasource")
    public DataSourceProperties opLogDatasourceProperties() {
        return new DataSourceProperties();
    }


    @Bean("opLogJdbcTemplate")
    public JdbcTemplate opLogJdbcTemplate(@Qualifier("opLogDatasourceProperties") DataSourceProperties properties) {
        return new JdbcTemplate(properties.initializeDataSourceBuilder().build());
    }

}
