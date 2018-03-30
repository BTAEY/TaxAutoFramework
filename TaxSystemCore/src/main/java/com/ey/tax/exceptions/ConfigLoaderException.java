package com.ey.tax.exceptions;

import org.springframework.core.NestedRuntimeException;

/**
 * Created by zhuji on 2/6/2018.
 */
public class ConfigLoaderException extends NestedRuntimeException {
    public ConfigLoaderException(String msg) {
        super(msg);
    }

    public ConfigLoaderException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
