package com.ey.tax.core.service;

import com.ey.tax.entity.OuterTaxCheck;

import java.util.List;

/**
 * Created by zhuji on 5/8/2018.
 */
public interface IOuterTaxCheckService {
    List<OuterTaxCheck> getAll();

    List<OuterTaxCheck> findByUser(String name);

    OuterTaxCheck saveOrUpdate(OuterTaxCheck outerTaxCheck);

    OuterTaxCheck findById(Long id);


}
