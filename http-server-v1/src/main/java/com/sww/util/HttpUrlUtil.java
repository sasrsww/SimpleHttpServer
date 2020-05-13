package com.sww.util;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * HttpUrlUtil
 *
 * @author shenwenwen
 * @date 2020/4/29 21:08
 */
public class HttpUrlUtil {

    public static String fixUri(String uri) {
        StringBuilder builder = new StringBuilder(uri);
        if (builder.indexOf("/") != 0) {
            builder.insert(0, "/");
        }
        if (builder.lastIndexOf("/") == builder.length() - 1) {
            builder.delete(builder.length() - 1, builder.length());
        }
        return builder.toString();
    }

    public static String getReqPath(String uri) {
        if (uri.indexOf("?") < 0) {
            return uri;
        }

        return uri.substring(0, uri.indexOf("?"));
    }

    public static Map<String, String> getReqParams(String uri) {
        Map<String, String> map = Maps.newHashMap();
        if (uri.indexOf("?") < 0) {
            return map;
        }
        String[] params = uri.substring(uri.indexOf("?") + 1).split("&");
        for (String param : params) {
            String[] kv = param.split("=");
            if (kv.length == 2) {
                map.put(kv[0], kv[1]);
            }
        }
        return map;
    }
}
