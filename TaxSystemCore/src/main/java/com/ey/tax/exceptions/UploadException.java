package com.ey.tax.exceptions;

import org.springframework.core.NestedRuntimeException;

/**
 * Created by zhuji on 4/11/2018.
 */
public class UploadException extends NestedRuntimeException {
    public UploadException(String msg) {
        super(msg);
    }

    public UploadException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
