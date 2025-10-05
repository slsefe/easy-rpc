package org.example.rpc.basic.fault.tolerant;

import org.example.rpc.basic.model.RpcResponse;

import java.util.Map;


/**
 * 容错策略：快速失败
 * 遇到异常之后，将异常抛出，交由外层处理
 */
public class FailFastTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception exception) {
        throw new RuntimeException(exception);
    }
}
