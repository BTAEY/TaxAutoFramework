package com.ey.tax.core.service.impl;

import com.ey.tax.core.repository.OuterTaxCheckReporterRepository;
import com.ey.tax.core.service.IOuterTaxCheckReporterService;
import com.ey.tax.entity.OuterTaxCheckReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhuji on 5/8/2018.
 */
@Component
public class OuterTaxCheckReporterServiceImpl implements IOuterTaxCheckReporterService {
    @Autowired
    private OuterTaxCheckReporterRepository outerTaxCheckReporterRepository;

    @Override
    public OuterTaxCheckReporter saveOrUpdate(OuterTaxCheckReporter outerTaxCheckReporter) {
        return outerTaxCheckReporterRepository.saveAndFlush(outerTaxCheckReporter);
    }
}
