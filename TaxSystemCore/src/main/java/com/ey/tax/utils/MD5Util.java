package com.ey.tax.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhuji on 2/8/2018.
 */
public final class MD5Util {
    private static final Logger logger = LogManager.getLogger();

    public static String encodeWithCrypt(String text){
        String result = Md5Crypt.md5Crypt(text.getBytes());
        return result;
    }

    public static String encode(String text) {
        return DigestUtils.md5Hex(text.getBytes());
    }

    public static String encode(InputStream data){
        try {
            return DigestUtils.md5Hex(data);
        } catch (IOException e) {
            logger.error("generate md5 value for InputStream failed.",e);
        }
        return null;
    }
}
