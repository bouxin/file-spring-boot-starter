package com.rugoo.cloud.storage.util;

import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

/**
 * Description
 *
 * @author bouxinLou
 * @date 2021-01-02
 */
public class StringUtil {

    public static String concat(String... strings) {
        if (strings == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        for (String s : strings) {
            sb.append(s);
        }

        return sb.toString();
    }

    public static String decode(String source) {
        Charset charset = Charset.defaultCharset();
        int length = source.length();
        if (length == 0) {
            return source;
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(length);
            boolean changed = false;

            for(int i = 0; i < length; ++i) {
                int ch = source.charAt(i);
                if (ch == '%') {
                    if (i + 2 >= length) {
                        throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                    }

                    char hex1 = source.charAt(i + 1);
                    char hex2 = source.charAt(i + 2);
                    int u = Character.digit(hex1, 16);
                    int l = Character.digit(hex2, 16);
                    if (u == -1 || l == -1) {
                        throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                    }

                    baos.write((char)((u << 4) + l));
                    i += 2;
                    changed = true;
                } else {
                    baos.write(ch);
                }
            }

            return changed ? baos.toString() : source;
        }
    }
}