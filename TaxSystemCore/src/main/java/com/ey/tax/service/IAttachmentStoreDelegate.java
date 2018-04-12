package com.ey.tax.service;

/**
 * Created by zhuji on 4/11/2018.
 */
public interface IAttachmentStoreDelegate {
    void saveAttachmentStore(String filename,String absolutePath);

    String attachmentName();
}
