package com.lianwei.demo05.diy;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 使用注解方式
 */
@Aspect
@Component
public class AnnotationPointCut {

    @Before("execution(* com.lianwei.demo05.service.UserServiceImpl.*(..))")
    public void before(){
        System.out.println("方法执行前");
    }

    @After("execution(* com.lianwei.demo05.service.UserServiceImpl.*(..))")
    public void after(){
        System.out.println("方法执行后");
    }

}
