package com.ey.tax.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Created by zhuji on 4/16/2018.
 *
 * 加入DependsOn 使Flyway的配置在JPA自动建表之后生效
 * 同时要在application.properties 文件中开启flyway.baseline-on-migrate=true
 * 脚本从V2开始执行，V1 不再生效
 */
@Configuration
public class MigrationConfiguration extends FlywayAutoConfiguration {

    @Bean
    FlywayMigrationInitializer flywayInitializer(Flyway flyway){
        return new FlywayMigrationInitializer(flyway, (f) ->{} );
    }

    @Bean
    @DependsOn("primaryEntityManagerFactory")
    FlywayMigrationInitializer delayedFlywayInitializer(Flyway flyway) {
        return new FlywayMigrationInitializer(flyway, null);
    }
}
