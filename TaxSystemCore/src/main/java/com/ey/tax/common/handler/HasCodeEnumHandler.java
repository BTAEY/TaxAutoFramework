package com.ey.tax.common.handler;

import com.ey.tax.common.HasCodeEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zhuji on 3/27/2018.
 */
public class HasCodeEnumHandler<E extends Enum<E> & HasCodeEnum> extends BaseTypeHandler<E> {
    private Class<E> type;

    private final E[] enums;

    public HasCodeEnumHandler(Class<E> type){
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        this.enums = type.getEnumConstants();
        if(this.enums == null){
            throw new IllegalArgumentException(type.getSimpleName()
                    + " does not represent an enum type.");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, String.valueOf(parameter.getCode()));
        } else {
            ps.setObject(i, parameter.getCode(), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object value = rs.getObject(columnName);
        if(rs.wasNull()){
            return null;
        }
        for(E e : enums){
            if(value.equals(e.getCode())){
                return e;
            }
        }
        throw new IllegalArgumentException("Cannot convert "
                + value + " to " + type.getSimpleName());
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object value = rs.getObject(columnIndex);
        if(rs.wasNull()){
            return null;
        }
        for(E e : enums){
            if(value.equals(e.getCode())){
                return e;
            }
        }
        throw new IllegalArgumentException("Cannot convert "
                + value + " to " + type.getSimpleName());
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object value = cs.getObject(columnIndex);
        if(cs.wasNull()){
            return null;
        }
        for(E e : enums){
            if(value.equals(e.getCode())){
                return e;
            }
        }
        throw new IllegalArgumentException("Cannot convert "
                + value + " to " + type.getSimpleName());
    }
}
