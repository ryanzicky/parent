package com.zr.algorithm.ratelimiting.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhourui
 * @Date 2022/3/24 10:07
 */
@RestController
public class TestSentinel {

    @GetMapping("/test-sentinel-api")
    public String testSentinelApi() {
        String resourceName = "/order/create";
        Entry entry = null;
        try {
            entry = SphU.entry(resourceName);
            Thread.sleep(100);
            return "success";
        } catch (BlockException e) {
            return "资源被限流或降级了";
        } catch (InterruptedException e) {
            Tracer.trace(e);
            return "发生业务异常";
        } finally {

        }
    }

    @GetMapping("/test-sentinel-resource")
    @SentinelResource(
            value = "/order/create",
            blockHandler = "blockHandlerMethod",
            fallback = "fallbackMethod"
    )
    public String testSentinelResource(@RequestParam(required = false) String a) throws InterruptedException {
        // 模拟执行被保护的业务逻辑耗时
        Thread.sleep(100);
        return a;
    }
}
