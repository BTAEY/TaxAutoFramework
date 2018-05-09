package com.ey.tax.utils.converter;

import com.ey.tax.utils.DateUtil;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by zhuji on 5/8/2018.
 */
public class SqlTimestampPropertyEditor extends PropertyEditorSupport {
    public static final String DEFAULT_BATCH_PATTERN = "dd/MM/yyyy";

    private final SimpleDateFormat sdf;

    /**
     * uses default pattern yyyy-MM-dd for date parsing.
     */
    public SqlTimestampPropertyEditor() {
        this.sdf = new SimpleDateFormat(SqlTimestampPropertyEditor.DEFAULT_BATCH_PATTERN);
    }

    /**
     * Uses the given pattern for dateparsing, see {@link SimpleDateFormat} for allowed patterns.
     *
     * @param pattern
     *            the pattern describing the date and time format
     * @see SimpleDateFormat#SimpleDateFormat(String)
     */
    public SqlTimestampPropertyEditor(String pattern) {
        this.sdf = new SimpleDateFormat(pattern);
    }

    /**
     * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {

        try {
            setValue(new Timestamp(sdf.parse(text).getTime()));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
        }
    }

    /**
     * Format the Timestamp as String, using the specified DateFormat.
     */
    @Override
    public String getAsText() {
        Timestamp value = (Timestamp) getValue();
        return (value != null ? this.sdf.format(value) : "");
    }
}
