package com.ey.tax;

import com.ey.tax.config.GlobalPropertiesFactory;
import com.ey.tax.utils.PropertiesUtil;
import com.ey.tax.web.configuration.GlobalServletContextListener;
import org.apache.commons.configuration.MapConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jersey.JerseyProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.servlet.ServletContextListener;
import java.util.Map;

/**
 * Created by zhuji on 2/8/2018.
 */
@SpringBootApplication
@EnableJpaAuditing
@MapperScan("com.ey.tax.core.dao")
public class ApplicationBootEntry extends SpringBootServletInitializer {
    private static final String GLOBAL_CONFIG_PROPERTIES = "globalConfig";

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        ConfigurableEnvironment environment = new StandardEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        Map envMap = GlobalPropertiesFactory.getInstance().getMapConfiguration().getMap();
        propertySources.addFirst(new MapPropertySource(GLOBAL_CONFIG_PROPERTIES,envMap));
        return builder.sources(ApplicationBootEntry.class).environment(environment);
    }

    public static void main(String[] args) throws Exception {
        ConfigurableEnvironment environment = new StandardEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        Map envMap = GlobalPropertiesFactory.getInstance().getMapConfiguration().getMap();
        propertySources.addFirst(new MapPropertySource(GLOBAL_CONFIG_PROPERTIES,envMap));
        new SpringApplicationBuilder(ApplicationBootEntry.class).environment(environment).build().run();

    }

    @Bean
    ServletListenerRegistrationBean<ServletContextListener> globalServletListener(){
        ServletListenerRegistrationBean<ServletContextListener> srb = new ServletListenerRegistrationBean<>();
        srb.setListener(new GlobalServletContextListener());
        return srb;
    }
}
