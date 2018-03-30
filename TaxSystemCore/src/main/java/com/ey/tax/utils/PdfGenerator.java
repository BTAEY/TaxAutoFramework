package com.ey.tax.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.OutputStream;
import java.util.Map;

/**
 * Created by zhuji on 2/28/2018.
 */
public class PdfGenerator {
    private static final Logger logger = LogManager.getLogger();

    public static void generatePdf(Map<String,Object> dataModel, String templateName, OutputStream os) throws Exception{
        if(dataModel != null && StringUtils.isNotEmpty(templateName)) {
            logger.info("Begin to generate pdf file, template name: " + templateName);
            ITextRenderer renderer = new ITextRenderer();
            String content = FreeMarkerTemplateUtil.getInstance().generateContent(dataModel,templateName);
            renderer.getSharedContext().setMedia("pdf");
            renderer.setDocumentFromString(content);
            renderer.layout();
            renderer.createPDF(os);
        }
    }
}
