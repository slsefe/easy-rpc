package org.example.rpc.basic.fault.retrying;

import org.junit.Test;

public class RetryStrategyTest {

    NoRetryStrategy noRetryStrategy = new NoRetryStrategy();

    FixedIntervalRetryStrategy fixedIntervalRetryStrategy = new FixedIntervalRetryStrategy();

    @Test
    public void retryWithNoRetryStrategy() {
        try {
            noRetryStrategy.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("模拟重试失败");
            });
        } catch (Exception e) {
            System.out.println("重试多次失败");
            e.printStackTrace();
        }
    }

    @Test
    public void retryWithFixedIntervalRetryStrategy() {
        try {
            fixedIntervalRetryStrategy.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("模拟重试失败");
            });
        } catch (Exception e) {
            System.out.println("重试多次失败");
            e.printStackTrace();
        }
    }

}