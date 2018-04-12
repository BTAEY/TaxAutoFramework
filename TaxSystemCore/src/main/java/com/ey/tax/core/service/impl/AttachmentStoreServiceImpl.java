package com.ey.tax.core.service.impl;

import com.ey.tax.core.repository.AttachmentStoreRepository;
import com.ey.tax.core.service.IAttachmentStoreService;
import com.ey.tax.entity.AttachmentStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhuji on 4/10/2018.
 */
@Component
public class AttachmentStoreServiceImpl implements IAttachmentStoreService {
    @Autowired
    private AttachmentStoreRepository repository;

    @Override
    public AttachmentStore save(AttachmentStore attachment) {
        return repository.saveAndFlush(attachment);
    }
}
