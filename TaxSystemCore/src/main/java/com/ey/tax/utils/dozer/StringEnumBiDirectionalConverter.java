package com.ey.tax.utils.dozer;

import org.dozer.CustomConverter;

/**
 * Created by zhuji on 3/27/2018.
 */
public class StringEnumBiDirectionalConverter implements CustomConverter {
    @Override
    public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
        if(sourceFieldValue == null){
            return null;
        }
        if(String.class.isAssignableFrom(sourceClass)
                && Enum.class.isAssignableFrom(destinationClass)){
            return Enum.valueOf((Class<Enum>)destinationClass,(String)sourceFieldValue);
        }
        if(Enum.class.isAssignableFrom(sourceClass)
                && String.class.isAssignableFrom(destinationClass)){
            return sourceFieldValue.toString();
        }
        return null;
    }
}
