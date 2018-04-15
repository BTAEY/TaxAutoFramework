package com.ey.tax.service;

import com.ey.tax.entity.AttachmentStore;
import com.ey.tax.exceptions.UploadException;
import com.ey.tax.utils.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuji on 4/11/2018.
 */
@Service
public class UploadService {
    private Logger logger = LogManager.getLogger();

    public List<AttachmentStore> upload(List<MultipartFile> multipartFiles,IAttachmentStoreDelegate delegate){
        List<AttachmentStore> result = new ArrayList<>();
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
                String originalName = file.getOriginalFilename();
                String extension = StringUtil.getFilenameExtension(originalName);
                if(StringUtil.isEmpty(originalName) || StringUtil.isEmpty(extension)){
                    continue;
                }
                String name = delegate.attachmentName();
                String absolutePath = dir.getAbsolutePath()+File.separator+name+"."+extension;
                File serverFile = new File(absolutePath);
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(serverFile));
                outputStream.write(bytes);
                outputStream.close();
                logger.info("Server File Location = "+serverFile.getAbsolutePath());
                AttachmentStore attachmentStore = delegate.saveAttachmentStore(absolutePath);
                result.add(attachmentStore);
            }
        } catch (IOException e) {
            logger.error("upload file occurs error!",e);
            throw new UploadException("upload file occurs error!",e);
        }
        return result;
    }
}
