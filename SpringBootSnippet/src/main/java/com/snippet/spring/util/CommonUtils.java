package com.snippet.spring.util;

import org.apache.commons.lang3.StringUtils;

public class CommonUtils {

    /**
     * sneak to camel
     *
     * @param name sneak field
     * @return camel field
     */
    public static String camelName(String name) {
        StringBuilder result = new StringBuilder();
        if (StringUtils.isBlank(name)) {
            return "";
        } else if (!name.contains("_")) {
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        String[] camels = name.split("_");
        for (String camel : camels) {
            if (camel.isEmpty()) {
                continue;
            }
            if (result.length() == 0) {
                result.append(camel.toLowerCase());
            } else {
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }
}
