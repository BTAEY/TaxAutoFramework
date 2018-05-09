package com.ey.tax.core.service;

import com.ey.tax.entity.OuterTaxCheckReporter;

/**
 * Created by zhuji on 5/8/2018.
 */
public interface IOuterTaxCheckReporterService {
    OuterTaxCheckReporter saveOrUpdate(OuterTaxCheckReporter outerTaxCheckReporter);
}
