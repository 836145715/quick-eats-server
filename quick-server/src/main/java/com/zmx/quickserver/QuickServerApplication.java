package com.zmx.quickserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Base64;

/**
 * 应用程序启动类
 */
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.zmx.common", "com.zmx.quickserver","com.zmx.quickpojo"})
@MapperScan("com.zmx.quickpojo.mapper")
@EnableAspectJAutoProxy
public class QuickServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickServerApplication.class, args);
    }

}
