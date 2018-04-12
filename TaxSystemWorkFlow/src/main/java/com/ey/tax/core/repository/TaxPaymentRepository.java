package com.ey.tax.core.repository;

import com.ey.tax.entity.TaxPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by zhuji on 4/10/2018.
 */
@Repository
public interface TaxPaymentRepository extends JpaRepository<TaxPayment,Long> {

    TaxPayment findByProcessId(String processId);
}
