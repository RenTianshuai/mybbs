package com.yaohan.bbs.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Slf4j
@Component
@Order(Integer.MIN_VALUE + 1000)
public class LogFilterConfig implements Filter {

    private static final ThreadLocal<Long> startTimeThreadLocal =
            new NamedThreadLocal<Long>("ThreadLocal StartTime");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //1、开始时间
        long beginTime = System.currentTimeMillis();
        //线程绑定变量（该数据只有当前请求的线程可见）
        startTimeThreadLocal.set(beginTime);
        log.debug("开始计时: {}  URI: {}", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(beginTime), httpServletRequest.getRequestURI());

        chain.doFilter(httpServletRequest, httpServletResponse);
        //得到线程绑定的局部变量（开始时间）
        beginTime = startTimeThreadLocal.get();
        //2、结束时间
        long endTime = System.currentTimeMillis();
        log.debug("计时结束：{}  耗时：{}ms  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
                new SimpleDateFormat("hh:mm:ss.SSS").format(endTime), (endTime - beginTime),
                httpServletRequest.getRequestURI(), Runtime.getRuntime().maxMemory()/1024/1024, Runtime.getRuntime().totalMemory()/1024/1024, Runtime.getRuntime().freeMemory()/1024/1024,
                (Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory())/1024/1024);
        //删除线程变量中的数据，防止内存泄漏
        startTimeThreadLocal.remove();
    }

    @Override
    public void destroy() {

    }
}
