package com.lianwei;

import com.lianwei.demo05.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private UserService userService;


    @Test
    void contextLoads() {
    }

    @Test
    void MyTest() {
        userService.add();
    }

}
