package com.ey.tax.service;

import com.ey.tax.entity.AttachmentStore;

import java.util.List;
import java.util.Map;

public class TaskPropertyResolverAdapter implements TaskPropertyResolver {
    @Override
    public Map<String, Object> variables() {
        return null;
    }

    @Override
    public String[] comments() {
        return new String[0];
    }

    @Override
    public List<AttachmentStore> attachments() {
        return null;
    }
}
