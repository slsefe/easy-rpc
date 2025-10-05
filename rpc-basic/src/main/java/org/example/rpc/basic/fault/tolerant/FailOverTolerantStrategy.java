package org.example.rpc.basic.fault.tolerant;

import lombok.extern.slf4j.Slf4j;
import org.example.rpc.basic.model.RpcResponse;

import java.util.Map;


/**
 * 容错策略：服务转移
 * 遇到异常之后，调用其他可用的服务节点
 */
@Slf4j
public class FailOverTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception exception) {
        // TODO
        return null;
    }
}
