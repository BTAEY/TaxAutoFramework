package com.ey.tax.configuration.datasource;

import cn.hutool.db.dialect.impl.MysqlDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by zhuji on 4/16/2018.
 */
@Configuration
@EnableConfigurationProperties({
        MultipleDataSourceConfiguration.MySqlDSProperties.class
})
public class MultipleDataSourceConfiguration {

    @Autowired
    private MySqlDSProperties primaryDSProperties;

    @Primary
    @Bean(name="primaryDataSource")
    public DataSource primaryDataSource(){
        DataSourceBuilder builder = DataSourceBuilder.create()
                .driverClassName(primaryDSProperties.getDriverClassName())
                .url(primaryDSProperties.getUrl())
                .username(primaryDSProperties.getUsername())
                .password(primaryDSProperties.getPassword());
        return builder.build();
    }

    @Bean(name="primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("primaryDataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @ConfigurationProperties(prefix = "spring.datasource.primary")
    static class MySqlDSProperties extends BaseDataSourceProperties{}

    static class BaseDataSourceProperties{
        private String url;

        private String driverClassName;

        private String username;

        private String password;

        private String dbname;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDbname() {
            return dbname;
        }

        public void setDbname(String dbname) {
            this.dbname = dbname;
        }
    }
}
