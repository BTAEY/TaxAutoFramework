package com.ey.tax.config;

import com.ey.tax.exceptions.ConfigLoaderException;
import org.apache.commons.configuration.MapConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.net.URL;
import java.util.Properties;

/**
 * Created by zhuji on 2/6/2018.
 */
public final class GlobalPropertiesFactory {
    private static final Logger logger = LogManager.getLogger();

    private PropertiesConfiguration configuration;

    private MapConfiguration mapConfiguration;

    private static class GlobalPropertiesFactoryHolder{
        private static GlobalPropertiesFactory instance = new GlobalPropertiesFactory();
    }

    public static GlobalPropertiesFactory getInstance(){
        return GlobalPropertiesFactoryHolder.instance;
    }

    private GlobalPropertiesFactory(){
        loadGlobalPropertiesSettings();
    }

    private void loadGlobalPropertiesSettings() {
        String globalPropertiesFile = GlobalPropertiesConstants.GLOBAL_CONFIG_FILE;
        logger.info("Trying to load global configuration file ["+globalPropertiesFile+"] ");
        try{
            Resource res = new ClassPathResource(globalPropertiesFile);
            URL url = res.getURL();
            configuration = new PropertiesConfiguration(url);
            Properties p = new Properties();
            p.load(res.getInputStream());
            mapConfiguration = new MapConfiguration(p);
            if(logger.isInfoEnabled()){
                logger.info("Load ["+globalPropertiesFile+"] from ["+res.getURL()+"]");
            }
        }catch (Exception ex){
            throw new ConfigLoaderException("Problem loading global properties from URL ["+ globalPropertiesFile + "]",ex);
        }
        logger.info("Finished configuring global properties");
    }

    /**
     * @Function 获取所有配置的资源文件整合到一起
     * @return
     * @author zj
     * @Date 2014年8月7日
     */
    public String[] getPropertyResourcesNames(){
        String[] resourceFiles = configuration.getStringArray(GlobalPropertiesConstants.PROPERTY_RESOURCES);
        return resourceFiles;
    }

    public PropertiesConfiguration getConfiguration(){
        return configuration;
    }

    public MapConfiguration getMapConfiguration(){
        return mapConfiguration;
    }
}
