package com.ey.tax.core.repository;

import com.ey.tax.entity.OuterTaxCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhuji on 5/8/2018.
 */
@Repository
public interface OuterTaxCheckRepository extends JpaRepository<OuterTaxCheck,Long> {
    List<OuterTaxCheck> findByCreateBy(String createMan);
}
