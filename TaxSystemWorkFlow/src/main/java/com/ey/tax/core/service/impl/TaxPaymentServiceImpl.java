package com.ey.tax.core.service.impl;

import com.ey.tax.core.repository.TaxPaymentRepository;
import com.ey.tax.core.service.ITaxPaymentService;
import com.ey.tax.entity.TaxPayment;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhuji on 4/10/2018.
 */
@Component("taxPaymentService")
public class TaxPaymentServiceImpl implements ITaxPaymentService {
    @Autowired
    private TaxPaymentRepository repository;

    @Override
    public TaxPayment save(TaxPayment taxPayment) {
        return repository.saveAndFlush(taxPayment);
    }

    @Override
    public TaxPayment findTaxPaymentByProcessId(String processId) {
        return repository.findByProcessId(processId);
    }

    public TaxPayment insertProcessId(DelegateExecution execution, Long taxPaymentId){
        TaxPayment dbObj = repository.findOne(taxPaymentId);
        dbObj.setProcessId(execution.getProcessInstanceId());
        return repository.saveAndFlush(dbObj);
    }
}
