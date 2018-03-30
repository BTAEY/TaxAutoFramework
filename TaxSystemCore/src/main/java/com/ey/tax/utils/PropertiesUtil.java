package com.ey.tax.utils;

import com.ey.tax.config.GlobalPropertiesConstants;
import com.ey.tax.config.GlobalPropertiesFactory;
import com.ey.tax.exceptions.ConfigLoaderException;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by zhuji on 2/6/2018.
 */
public final class PropertiesUtil {
    private static final Logger logger = LogManager.getLogger();
    static CompositeConfiguration cc;

    static{
        loadPropertiesResources();
    }

    private static void loadPropertiesResources() {
        cc = new CompositeConfiguration();
        PropertiesConfiguration globalConfiguration = GlobalPropertiesFactory.getInstance().getConfiguration();
        cc.addConfiguration(globalConfiguration);
        String[] propertyResourcesNames = globalConfiguration.getStringArray(GlobalPropertiesConstants.PROPERTY_RESOURCES);
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            for (String propertiesFile : propertyResourcesNames) {
                Resource[] resources = resourcePatternResolver.getResources(propertiesFile);
                for(Resource resource:resources){
                    Configuration propertiesConfig = new PropertiesConfiguration(resource.getURL());
                    cc.addConfiguration(propertiesConfig);
                }
            }
        }
        catch (IOException | ConfigurationException e ) {
            logger.error(e);
            throw new ConfigLoaderException("Loading property resources occurs error!",e);
        }
    }

    public static String getString(String key){
        return cc.getString(key);
    }

    public static Integer getInt(String key){
        return cc.getInt(key);
    }

    public static BigDecimal getBigDecimal(String key){
        return cc.getBigDecimal(key);
    }

    public static String[] getStringArray(String key){
        return cc.getStringArray(key);
    }

    public static CompositeConfiguration getConfiguration(){
        return cc;
    }
}
