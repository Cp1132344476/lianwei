package com.lianwei.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 授权
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 首页所有人都可以访问 功能页面只有对应权限的人能访问
        http.authorizeRequests().antMatchers("/").permitAll()
                                .antMatchers("/level1/**").hasRole("vip1")
                                .antMatchers("/level2/**").hasRole("vip2")
                                .antMatchers("/level3/**").hasRole("vip3");

        // 没有权限默认会到登陆页面 需要开启登陆的页面
        http.formLogin().loginPage("/toLogin");

        // 注销 开启注销功能
        // 防止网站攻击
        // 关闭csrf
        http.csrf().disable();
        http.logout().logoutUrl("/");

        // 开启记住我功能 cookie 默认保存两周
        http.rememberMe();
    }

    /**
     * 认证
     * 密码编码：PasswordEncoder
     * 在Spring Security 5.0+ 新增了很多加密方法
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 这些数据正常在数据库中读取
//        auth.jdbcAuthentication().dataSource().withDefaultSchema()
//                .withUser(users.username("username").password("password").roles("USER"));

        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("cp").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2","vip3")
                .and()
                .withUser("root").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1","vip2","vip3")
                .and()
                .withUser("guest").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1");
    }
}
