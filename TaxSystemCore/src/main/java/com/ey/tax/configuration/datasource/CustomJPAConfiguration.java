package com.ey.tax.configuration.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by zhuji on 4/16/2018.
 *
 * 实现多数据源配置
 *
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableConfigurationProperties(JpaProperties.class)
@EnableJpaRepositories(
        entityManagerFactoryRef = "primaryEntityManagerFactory",
        transactionManagerRef = "primaryTransactionManager",
        basePackages = {"com.ey.tax"}
)
@Import({MultipleDataSourceConfiguration.class})
public class CustomJPAConfiguration {
    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    @Qualifier("primaryDataSource")
    private DataSource primaryDataSource;

    @PersistenceContext(unitName = "primaryPersistenceUnit")
    @Bean(name = "primaryEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary(EntityManagerFactoryBuilder builder){
        return builder.dataSource(primaryDataSource)
                .properties(getVendorProperties(primaryDataSource))
                .packages("com.ey.tax.entity")
                .persistenceUnit("primaryPersistenceUnit")
                .build();
    }

    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager transactionManagerPrimary(@Qualifier("primaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }
}
