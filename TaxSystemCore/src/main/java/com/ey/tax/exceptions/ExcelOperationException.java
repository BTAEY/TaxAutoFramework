package com.ey.tax.exceptions;

import org.springframework.core.NestedRuntimeException;

/**
 * Created by zhuji on 2/6/2018.
 */
public class ExcelOperationException extends NestedRuntimeException {
    public ExcelOperationException(String msg) {
        super(msg);
    }

    public ExcelOperationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
