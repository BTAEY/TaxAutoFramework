package com.ey.tax.common;

/**
 * Created by zhuji on 3/27/2018.
 */
public final class CommonEnums {
    public enum LoginStatus implements HasCodeEnum{
        ONLINE{
            @Override
            public Object getCode() {
                return "online";
            }
        },
        OFFLINE{
            @Override
            public Object getCode() {
                return "offline";
            }
        }
    }

    public enum EffectiveStatus{
        INEFFECTIVE,EFFECTIVE;
    }
}
