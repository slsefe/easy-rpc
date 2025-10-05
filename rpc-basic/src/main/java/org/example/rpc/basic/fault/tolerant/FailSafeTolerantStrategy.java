package org.example.rpc.basic.fault.tolerant;

import lombok.extern.slf4j.Slf4j;
import org.example.rpc.basic.model.RpcResponse;

import java.util.Map;


/**
 * 容错策略：静默
 * 遇到异常之后，记录日志，正常返回一个响应对象
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception exception) {
        log.info("FailSafeTolerantStrategy doTolerant");
        return new RpcResponse();
    }
}
