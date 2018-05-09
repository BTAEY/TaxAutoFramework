package com.ey.tax.core.service.impl;

import com.ey.tax.core.repository.OuterTaxCheckRepository;
import com.ey.tax.core.service.IOuterTaxCheckService;
import com.ey.tax.entity.OuterTaxCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhuji on 5/8/2018.
 */
@Service
public class OuterTaxCheckServiceImpl implements IOuterTaxCheckService {
    @Autowired
    private OuterTaxCheckRepository outerTaxCheckRepository;

    @Override
    public List<OuterTaxCheck> getAll() {
        return outerTaxCheckRepository.findAll();
    }

    @Override
    public List<OuterTaxCheck> findByUser(String name) {
        return outerTaxCheckRepository.findByCreateBy(name);
    }

    @Override
    public OuterTaxCheck saveOrUpdate(OuterTaxCheck outerTaxCheck) {
        return outerTaxCheckRepository.saveAndFlush(outerTaxCheck);
    }

    @Override
    public OuterTaxCheck findById(Long id) {
        return outerTaxCheckRepository.findOne(id);
    }
}
