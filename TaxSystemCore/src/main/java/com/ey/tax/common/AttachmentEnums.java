package com.ey.tax.common;

/**
 * Created by zhuji on 4/9/2018.
 */
public class AttachmentEnums {
    public enum AttachmentType implements HasCodeEnum{
        PDF{
            @Override
            public Object getCode() {
                return "pdf";
            }
        },
        HTML{
            @Override
            public Object getCode() {
                return "html";
            }
        },
        XLSX{
            @Override
            public Object getCode() {
                return "xlsx";
            }
        }
    }

    public enum StoreType implements HasCodeEnum{
        Local{
            @Override
            public Object getCode() {
                return "local";
            }
        },
        Remote{
            @Override
            public Object getCode() {
                return "remote";
            }
        }
    }
}
