package com.ey.tax.service;

import com.ey.tax.entity.AttachmentStore;

/**
 * Created by zhuji on 4/11/2018.
 */
public interface IAttachmentStoreDelegate {
    AttachmentStore saveAttachmentStore(String absolutePath);

    String attachmentName();
}
