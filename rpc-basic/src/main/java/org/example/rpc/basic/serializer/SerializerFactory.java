package org.example.rpc.basic.serializer;

import org.example.rpc.basic.spi.SpiLoader;

/**
 * 序列化器工厂（用于获取序列化器对象）
 */
public class SerializerFactory {

    // 加载序列化器接口的所有实现类
    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    public static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取实例
     */
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }

}

