package org.example.rpc.basic.utils;

import org.example.rpc.basic.config.RpcConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigUtilsTest {

    @Test
    void given_noEnv_when_getConfigValue_then_getDefaultConfigValue() {
        RpcConfig rpc = ConfigUtils.getConfigValue(RpcConfig.class, "rpc");
        assertEquals("easy-rpc", rpc.getName());
        assertEquals("1.0", rpc.getVersion());
        assertEquals("localhost", rpc.getServerHost());
        assertEquals(8081, rpc.getServerPort());
    }

    @Test
    void given_testEnv_when_getConfigValue_then_getTestConfigValue() {
        RpcConfig rpc = ConfigUtils.getConfigValue(RpcConfig.class, "rpc", "test");
        assertEquals("easy-rpc-test", rpc.getName());
        assertEquals("2.0", rpc.getVersion());
        assertEquals("127.0.0.1", rpc.getServerHost());
        assertEquals(8888, rpc.getServerPort());
    }


}