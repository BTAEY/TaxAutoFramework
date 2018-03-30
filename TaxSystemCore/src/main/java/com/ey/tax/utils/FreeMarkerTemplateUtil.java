package com.ey.tax.utils;

import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.StringWriter;

/**
 * Created by zhuji on 2/28/2018.
 */
public final class FreeMarkerTemplateUtil {
    private static final Logger logger = LogManager.getLogger();

    private static final String FTL_SUFFIX=".ftl";

    private FreeMarkerTemplateUtil(){}

    private static class FreeMarkerTemplateUtilHolder{
        private static final FreeMarkerTemplateUtil INSTANCE = new FreeMarkerTemplateUtil();
    }

    public static FreeMarkerTemplateUtil getInstance(){
        return FreeMarkerTemplateUtilHolder.INSTANCE;
    }

    private static Configuration configuration = new Configuration(Configuration.VERSION_2_3_25);

    static{
        try {
            configuration.setClassLoaderForTemplateLoading(PdfGenerator.class.getClassLoader(),"ftl/");
            configuration.setDefaultEncoding("UTF-8");
            DefaultObjectWrapper objectWrapper = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25).build();
            TemplateHashModel staticModels = objectWrapper.getStaticModels();
            TemplateHashModel dateUtilStatic = (TemplateHashModel) staticModels.get("com.ey.tax.utils.DateUtil");
            configuration.setObjectWrapper(objectWrapper);
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            configuration.setCacheStorage(NullCacheStorage.INSTANCE);
            configuration.setSharedVariable("DateUtil",dateUtilStatic);
        } catch (TemplateModelException e) {
            logger.error("init freemarker configuration occurs error.",e);
        }

    }

    public static String generateContent(Object dataModel, String templateName) throws Exception{
        Template template = configuration.getTemplate(templateName+FTL_SUFFIX);
        StringWriter stringWriter = new StringWriter();
        template.process(dataModel, stringWriter);
        String content = stringWriter.toString();
        stringWriter.flush();
        stringWriter.close();
        return content;
    }
}
