package com.ey.tax.utils;

import com.ey.tax.types.ColumnType;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by zhuji on 2/6/2018.
 */
public final class DataSourceUtil {
    private static final Logger logger = LogManager.getLogger();

    public static boolean testConnection(DataSource ds){
        boolean hasConnection = false;
        try(Connection conn = DataSourceUtils.getConnection(ds);) {
            hasConnection = true;
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return hasConnection;
    }

    private static final Cache<String,Map<String,String>> COLUMN_TYPE_CACHE = CacheBuilder.newBuilder().softValues().build();

    public static ColumnType getColumnType(final DataSource ds, final String tableName, String columnName) throws Exception{
        Map<String,String> columnNameAndTypeMapping = new HashMap<>();
        try{
            columnNameAndTypeMapping = COLUMN_TYPE_CACHE.get(tableName, new Callable<Map<String, String>>() {
                @Override
                public Map<String, String> call() throws Exception {
                    try(Connection connection = ds.getConnection()){
                        final DatabaseMetaData meta = connection.getMetaData();
                        Map<String,String> result = new HashMap<>();
                        ResultSet rsColumns = meta.getColumns(null, null, tableName, null);
                        while (rsColumns.next()) {
                            String columnType = rsColumns.getString("TYPE_NAME");
                            String columnName = rsColumns.getString("COLUMN_NAME");
                            logger.debug("First load columnType["+columnType+"] of columnName["+columnName+"] in table["+tableName+"].");
                            result.put(columnName,columnType);
                        }
                        return result;
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return ColumnType.valueOf(columnNameAndTypeMapping.get(columnName));
    }
}
