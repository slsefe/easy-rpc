package org.example.rpc.basic.serializer;

import java.io.IOException;

public interface Serializer {

    <T> byte[] serialize(T object) throws IOException;

    <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException;
}
