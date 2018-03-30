package com.ey.tax.types;

/**
 * Created by zhuji on 2/6/2018.
 */
public enum ColumnType {
    VARCHAR{
        @Override
        public Object getNullableValue() {
            return "";
        }
    },DECIMAL{
        @Override
        public Object getNullableValue() {
            return null;
        }
    },DOUBLE{
        @Override
        public Object getNullableValue() {
            return 0.0d;
        }
    },DATETIME{
        @Override
        public Object getNullableValue() {
            return null;
        }
    },INT{
        @Override
        public Object getNullableValue() {
            return 0;
        }
    },DATE{
        @Override
        public Object getNullableValue() {
            return null;
        }
    };

    public abstract Object getNullableValue();
}
