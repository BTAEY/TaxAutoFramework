package com.ey.tax.configuration;

import com.ey.tax.utils.dozer.StringEnumBiDirectionalConverter;
import org.dozer.CustomConverter;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuji on 3/27/2018.
 */
@Configuration
public class DozerConfig {
    @Bean
    public DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean(@Value("classpath*:dozer/*Mapping.xml") Resource[] resources){
        final DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean = new DozerBeanMapperFactoryBean();
        dozerBeanMapperFactoryBean.setMappingFiles(resources);

        List<CustomConverter> customConverters = new ArrayList<>();
        customConverters.add(new StringEnumBiDirectionalConverter());
        dozerBeanMapperFactoryBean.setCustomConverters(customConverters);
        return dozerBeanMapperFactoryBean;
    }
}
