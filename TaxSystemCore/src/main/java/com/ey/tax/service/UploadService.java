package com.ey.tax.service;

import com.ey.tax.common.AttachmentEnums;
import com.ey.tax.core.service.IAttachmentStoreService;
import com.ey.tax.entity.AttachmentStore;
import com.ey.tax.exceptions.UploadException;
import com.ey.tax.utils.FileNameFormatUtil;
import com.ey.tax.utils.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by zhuji on 4/11/2018.
 */
@Service
public class UploadService {
    private Logger logger = LogManager.getLogger();

    public void upload(List<MultipartFile> multipartFiles,IAttachmentStoreDelegate delegate){
        try {
            for(MultipartFile file: multipartFiles){
                byte[] bytes = file.getBytes();
                //creating the directory to store file
                String rootPath = System.getProperty("user.home");
                File dir = new File(rootPath+File.separator+"tempFiles");
                if(!dir.exists()){
                    dir.mkdirs();
                }
                // create the file on server
//                String originalFileName = file.getOriginalFilename();
//                String fileExtension = StringUtil.getFilenameExtension(originalFileName);
//                String name = FileNameFormatUtil.format()+"."+fileExtension;

                String name = delegate.attachmentName();
                File serverFile = new File(dir.getAbsolutePath()+File.separator+name);
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(serverFile));
                outputStream.write(bytes);
                outputStream.close();
                logger.info("Server File Location = "+serverFile.getAbsolutePath());
                delegate.saveAttachmentStore(name,serverFile.getAbsolutePath());
            }
        } catch (IOException e) {
            logger.error("upload file occurs error!",e);
            throw new UploadException("upload file occurs error!",e);
        }
    }
}
