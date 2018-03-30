package com.ey.tax.utils.dozer;

import com.ey.tax.common.CommonEnums;
import org.dozer.CustomConverter;
import org.dozer.DozerConverter;

/**
 * Created by zhuji on 3/27/2018.
 */
public class StringBooleanConverter implements CustomConverter{

    @Override
    public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
        if(sourceFieldValue == null ){
            return null;
        }
        if(Boolean.class.isAssignableFrom(sourceClass)){
            if(Boolean.valueOf(sourceFieldValue.toString())){
                return CommonEnums.EffectiveStatus.EFFECTIVE.name();
            }else{
                return CommonEnums.EffectiveStatus.INEFFECTIVE.name();
            }
        }
        if(Boolean.class.isAssignableFrom(destinationClass)){
            if(CommonEnums.EffectiveStatus.EFFECTIVE.name().equals(sourceFieldValue)){
                return true;
            }else{
                return false;
            }
        }
        return null;
    }
}
