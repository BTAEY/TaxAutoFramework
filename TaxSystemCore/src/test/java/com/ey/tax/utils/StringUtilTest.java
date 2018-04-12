package com.ey.tax.utils;

import org.junit.Test;

/**
 * Created by zhuji on 4/12/2018.
 */
public class StringUtilTest {
    @Test
    public void testGetFileName(){
        String filepath = "C:\\WorkBench\\idea_workspace\\TaxAutoFramework\\TaxSystemWorkFlow\\target\\classes\\processes\\Taxpayment.TaxPayment.png";
        String fileName = StringUtil.getFilename(filepath);
        System.out.println(fileName);
    }
}
