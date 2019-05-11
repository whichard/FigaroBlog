
package com.whichard.spring.boot.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全配置类.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年5月30日
 * */


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 启用方法安全设置

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String KEY = "www.whichard.cn";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder md5PasswordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();   // 使用 MD5 加密
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(md5PasswordEncoder); // 设置密码加密方式
        return authenticationProvider;
    }

/**
     * 自定义配置
  */

@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers("/css/**", "/js/**", "/fonts/**", "/index").permitAll() // 都可以访问
            .antMatchers("/h2-console/**").permitAll() // 都可以访问
            .antMatchers("/admins/**").hasRole("ADMIN") // 需要相应的角色才能访问
            .and()
            .formLogin()   //基于 Form 表单登录验证
            .loginPage("/login").failureUrl("/login-error") // 自定义登录界面
            .and().rememberMe().key(KEY) // 启用 remember me
            .and().exceptionHandling().accessDeniedPage("/403");  // 处理异常，拒绝访问就重定向到 403 页面
    http.csrf().ignoringAntMatchers("/h2-console/**"); // 禁用 H2 控制台的 CSRF 防护
    http.headers().frameOptions().sameOrigin();// 允许来自同一来源的H2 控制台的请求
}

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers("/css/**", "/js/**", "/fonts/**", "/index").permitAll() // 都可以访问
//                .antMatchers("/h2-console/**").permitAll() // 都可以访问
//                .antMatchers("/admins/**").hasRole("ADMIN") // 需要相应的角色才能访问
//                .and()
//                .formLogin()   //基于 Form 表单登录验证
//                .loginPage("/login").failureUrl("/login-error") // 自定义登录界面
//                .and().rememberMe().key(KEY) // 启用 remember me
//                .and().exceptionHandling().accessDeniedPage("/403");  // 处理异常，拒绝访问就重定向到 403 页面
//        http.csrf().ignoringAntMatchers("/h2-console/**"); // 禁用 H2 控制台的 CSRF 防护
//        http.headers().frameOptions().sameOrigin();// 允许来自同一来源的H2 控制台的请求
//        // .and().headers().defaultsDisabled().cacheControl();  原生的开启cacheControl
//    }

/**
     * 认证信息管理
     *
     * @param auth
     * @throws Exception
     */

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }
}
