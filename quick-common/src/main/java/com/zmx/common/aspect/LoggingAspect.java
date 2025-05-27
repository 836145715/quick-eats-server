package com.zmx.common.aspect;

import com.zmx.common.annotation.ApiLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    //定义切入点
    @Pointcut("@annotation(com.zmx.common.annotation.ApiLog)")
    public void apiLogPointcut() {}


    //方法执行前
    @Before("apiLogPointcut()")
    public void before(JoinPoint joinPoint){
        MethodSignature signature =(MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ApiLog apiLog = method.getAnnotation(ApiLog.class);
        String methodName = method.getName();
        Object[] args = joinPoint.getArgs();
        log.info("方法执行：{}，描述：{}，参数：{}",methodName,apiLog.value(),args);
    }


    //方法执行后
    @AfterReturning(pointcut = "apiLogPointcut()",returning = "result")
    public  void afterReturning(JoinPoint joinPoint, Object result){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        log.info("方法 {} 执行完毕，返回结果: {}", methodName, result);
    }


}
