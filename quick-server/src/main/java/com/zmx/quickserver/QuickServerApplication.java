package com.zmx.quickserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 应用程序启动类
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching  //开启缓存注解
@ComponentScan(basePackages = { "com.zmx.common", "com.zmx.quickserver", "com.zmx.quickpojo" })
@MapperScan("com.zmx.*.mapper")
@EnableAspectJAutoProxy
@EnableScheduling
public class QuickServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickServerApplication.class, args);
    }

}
