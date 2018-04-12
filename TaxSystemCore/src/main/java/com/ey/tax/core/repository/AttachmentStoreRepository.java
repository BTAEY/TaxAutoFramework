package com.ey.tax.core.repository;

import com.ey.tax.entity.AttachmentStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zhuji on 4/10/2018.
 */
@Repository
public interface AttachmentStoreRepository extends JpaRepository<AttachmentStore,Long> {
}
