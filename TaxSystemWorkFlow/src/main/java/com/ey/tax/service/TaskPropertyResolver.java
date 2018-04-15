package com.ey.tax.service;

import com.ey.tax.entity.AttachmentStore;

import java.util.List;
import java.util.Map;

public interface TaskPropertyResolver {
    Map<String, Object> variables();

    String[] comments();

    List<AttachmentStore> attachments();
}
