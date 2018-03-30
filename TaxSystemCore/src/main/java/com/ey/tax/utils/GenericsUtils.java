package com.ey.tax.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuji on 2/8/2018.
 */
public final class GenericsUtils {
    /**
     * 通过反射,获得指定类的父类的泛型参数的实际类型
     * @param clazz
     * @param index
     * @return
     */
    public static Class getSuperClassGenericType(Class clazz,int index){
        Type genericType = clazz.getGenericSuperclass();
        //如果没有实现ParameterizedType接口，即不支持泛型，直接返回Object.class
        if (!(genericType instanceof ParameterizedType)) {
            return Object.class;
        }
		/*
		 * 返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class,
		 * 如BuyerServiceBean extends DaoSupport<Buyer,Contact>就返回Buyer和Contact类型
		 */
        Type[] params = ((ParameterizedType) genericType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new RuntimeException("Index "+ (index<0 ? "cannot be -1" : "out of array bounds."));
        }
        if(!(params[index] instanceof Class)){
            return Object.class;
        }
        return (Class)params[index];
    }

    public static Class getSuperClassGenericType(Class clazz) {
        return getSuperClassGenericType(clazz,0);
    }

    /**
     * 通过反射,获得方法返回值泛型参数的实际类型. 如：public Map<String, Buyer> getNames(){}
     * @param md
     * @param index
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回<code>Object.class</code>
     */
    public static Class getMethodGenericReturnType(Method md, int index){
        Type returnType = md.getGenericReturnType();
        Class methodArgType = getGenericType(index, returnType);
        if(methodArgType != null){
            return methodArgType;
        }
        return Object.class;
    }

    public static Class getMethodGenericReturnType(Method md){
        return getMethodGenericReturnType(md,0);
    }

    /**
     * 通过反射,获得Field泛型参数的实际类型. 如: public Map<String, Buyer> names;
     * @param field
     * @param index
     * @return
     */
    public static Class getFieldGenericType(Field field, int index) {
        Type genericFieldType = field.getGenericType();
        Class fieldArgType = getGenericType(index, genericFieldType);
        if (fieldArgType != null){
            return fieldArgType;
        }
        return Object.class;
    }

    private static Class getGenericType(int index, Type genericFieldType) {
        if(genericFieldType instanceof ParameterizedType){
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            if (index >= fieldArgTypes.length || index < 0) {
                throw new RuntimeException("Index "+ (index<0 ? "cannot be -1" : "out of array bounds."));
            }
            return (Class)fieldArgTypes[index];
        }
        return null;
    }

    public static Class getFieldGenericType(Field field) {
        return getFieldGenericType(field, 0);
    }

    /**
     * 通过反射,获得方法输入参数第index个输入参数的所有泛型参数的实际类型.
     * 如: public void add(Map<String, Buyer> maps, List<String> names){}
     * @param method
     * @param index
     * @return
     */
    public static List<Class> getMethodGenericParameterTypes(Method method, int index) {
        List<Class> results = new ArrayList<Class>();
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        if (index >= genericParameterTypes.length ||index < 0) {
            throw new RuntimeException("Index "+ (index<0 ? "cannot be -1" : "out of array bounds."));
        }
        Type genericParameterType = genericParameterTypes[index];
        if(genericParameterType instanceof ParameterizedType){
            ParameterizedType aType = (ParameterizedType) genericParameterType;
            Type[] parameterArgTypes = aType.getActualTypeArguments();
            for(Type parameterArgType : parameterArgTypes){
                Class parameterArgClass = (Class) parameterArgType;
                results.add(parameterArgClass);
            }
            return results;
        }
        return results;
    }
}
