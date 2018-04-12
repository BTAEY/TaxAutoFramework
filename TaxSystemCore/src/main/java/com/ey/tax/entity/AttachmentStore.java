package com.ey.tax.entity;

import com.ey.tax.common.AttachmentEnums;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Created by zhuji on 4/9/2018.
 */
@Entity
@Table(name = "t_attachment_store")
public class AttachmentStore extends BaseEntity {
    @Column(name="attachment_name")
    private String attachmentName;

    @Enumerated(EnumType.STRING)
    @Column(name="attachment_type")
    private AttachmentEnums.AttachmentType attachmentType;

    @Column(name="physical_path")
    private String physicalPath;

    @Column(name="remote_path")
    private String remotePath;

    @Enumerated(EnumType.STRING)
    @Column(name="store_type")
    private AttachmentEnums.StoreType storeType;

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public AttachmentEnums.AttachmentType getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(AttachmentEnums.AttachmentType attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getPhysicalPath() {
        return physicalPath;
    }

    public void setPhysicalPath(String physicalPath) {
        this.physicalPath = physicalPath;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public AttachmentEnums.StoreType getStoreType() {
        return storeType;
    }

    public void setStoreType(AttachmentEnums.StoreType storeType) {
        this.storeType = storeType;
    }
}
