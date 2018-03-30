package com.ey.tax.core.repository;

import com.ey.tax.entity.AccountLoginLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zhuji on 3/22/2018.
 */
@Repository
public interface AccountLoginLogRepository extends JpaRepository<AccountLoginLogEntity,Long> {
    AccountLoginLogEntity findTop1ByUserIdOrderByOnlineTimeDesc(Long userId);
}
