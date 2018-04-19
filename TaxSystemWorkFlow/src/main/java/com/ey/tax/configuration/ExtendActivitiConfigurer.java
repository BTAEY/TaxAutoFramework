package com.ey.tax.configuration;

import com.ey.tax.utils.StringUtil;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhuji on 4/13/2018.
 *
 * 扩展 ProcessEngineConfiguration
 */

@Configuration
@EnableConfigurationProperties(
        ExtendActivitiConfigurer.ExtendActivitiProperties.class
)
public class ExtendActivitiConfigurer implements ProcessEngineConfigurationConfigurer
{
    @Autowired
    private ExtendActivitiProperties extendActivitiProperties;
    /**
     * @param processEngineConfiguration
     */
    @Override
    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
        //解决前台显示流程图片中文乱码问题
        processEngineConfiguration.setActivityFontName(extendActivitiProperties.getActivityFontName());
        processEngineConfiguration.setLabelFontName(extendActivitiProperties.getLabelFontName());
        processEngineConfiguration.setAnnotationFontName(extendActivitiProperties.getAnnotationFontName());

        processEngineConfiguration.setDatabaseType(extendActivitiProperties.getDatabaseType());
        /**
         * 流程部署形式， default, single-resource,resource-parent-folder,none-auto-deploy
         */
        if(StringUtil.isNotEmpty(extendActivitiProperties.getDeploymentMode())){
            if("none-auto-deploy".equals(extendActivitiProperties.getDeploymentMode())){
                processEngineConfiguration.setDeploymentResources(null);
            }else{
                processEngineConfiguration.setDeploymentMode(extendActivitiProperties.getDeploymentMode());
            }
        }
    }

    @ConfigurationProperties("spring.activiti.extend")
    public static class ExtendActivitiProperties {
        private String databaseType;

        private String activityFontName;

        private String labelFontName;

        private String annotationFontName;

        private String deploymentMode;

        public String getDatabaseType() {
            return databaseType;
        }

        public void setDatabaseType(String databaseType) {
            this.databaseType = databaseType;
        }

        public String getActivityFontName() {
            return activityFontName;
        }

        public void setActivityFontName(String activityFontName) {
            this.activityFontName = activityFontName;
        }

        public String getLabelFontName() {
            return labelFontName;
        }

        public void setLabelFontName(String labelFontName) {
            this.labelFontName = labelFontName;
        }

        public String getAnnotationFontName() {
            return annotationFontName;
        }

        public void setAnnotationFontName(String annotationFontName) {
            this.annotationFontName = annotationFontName;
        }

        public String getDeploymentMode() {
            return deploymentMode;
        }

        public void setDeploymentMode(String deploymentMode) {
            this.deploymentMode = deploymentMode;
        }
    }
}
