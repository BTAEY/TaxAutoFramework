package com.ey.tax.core.repository;

import com.ey.tax.entity.OuterTaxCheckReporter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zhuji on 5/8/2018.
 */
@Repository
public interface OuterTaxCheckReporterRepository extends JpaRepository<OuterTaxCheckReporter,Long> {
}
