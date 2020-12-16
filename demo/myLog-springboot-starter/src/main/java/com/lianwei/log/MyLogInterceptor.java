package com.lianwei.log;


import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author chenpeng
 */
public class MyLogInterceptor implements HandlerInterceptor {
    private final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handler1 = (HandlerMethod) handler;
        Method method = handler1.getMethod();
        MyLog myLog = method.getAnnotation(MyLog.class);
        if (myLog != null) {
            threadLocal.set(System.currentTimeMillis());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerMethod handler1 = (HandlerMethod) handler;
        Method method = handler1.getMethod();
        MyLog myLog = method.getAnnotation(MyLog.class);
        if (myLog != null) {
            long time = System.currentTimeMillis() - threadLocal.get();
            StringBuffer requestURL = request.getRequestURL();
            String desc = myLog.desc();
            String s = method.getDeclaringClass().getName() + "    " + method.getName();
            System.out.println("本方法执行耗时：" + time);
            System.out.println("本方法的请求路径：" + requestURL);
            System.out.println("本方法的备注是：" + desc);
            System.out.println("本方法所在位置以及方法名称是：" + s);
        }
    }
}
