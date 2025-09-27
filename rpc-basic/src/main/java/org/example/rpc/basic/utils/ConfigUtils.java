package org.example.rpc.basic.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

public class ConfigUtils {
    public static <T> T getConfigValue(Class<T> clazz, String key) {
        return getConfigValue(clazz, key, null);
    }

    public static <T> T getConfigValue(Class<T> clazz, String prefix, String environment) {
        StringBuilder stringBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            stringBuilder.append("-").append(environment);
        }
        stringBuilder.append(".properties");
        Props props = new Props(stringBuilder.toString());
        return props.toBean(clazz, prefix);
    }
}
