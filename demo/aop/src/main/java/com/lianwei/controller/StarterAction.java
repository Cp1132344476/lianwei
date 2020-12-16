package com.lianwei.controller;

import com.lianwei.log.MyLog;
import com.lianwei.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class StarterAction {
    private TestService testService;

    @Autowired
    public void setTestService(TestService testService) {
        this.testService = testService;
    }

    @RequestMapping("/test")
    @MyLog(desc = "注解1")
    public void test() {
        testService.print();
    }

    @MyLog(desc = "注解2")
    @RequestMapping("/test1")
    public void test1(String name) {
        testService.splitString(name);
    }

}
