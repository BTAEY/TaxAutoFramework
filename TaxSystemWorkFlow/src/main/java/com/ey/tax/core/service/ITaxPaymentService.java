package com.ey.tax.core.service;

import com.ey.tax.entity.TaxPayment;

/**
 * Created by zhuji on 4/10/2018.
 */
public interface ITaxPaymentService {
    TaxPayment save(TaxPayment taxPayment);

    TaxPayment findTaxPaymentByProcessId(String processId);
}
