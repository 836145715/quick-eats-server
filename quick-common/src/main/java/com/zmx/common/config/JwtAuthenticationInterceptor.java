package com.zmx.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zmx.common.enums.ErrorCodeEnum;
import com.zmx.common.response.Result;
import com.zmx.common.utils.BaseContext;
import com.zmx.common.utils.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWT认证拦截器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 获取请求头中的token
        String token = request.getHeader(jwtUtils.getHeader());

        // 如果没有token，返回未认证错误
        if (token == null || token.isEmpty()) {
            responseError(response, "未登录，请先登录");
            return false;
        }

        // 验证token
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            responseError(response, "登录已过期，请重新登录");
            return false;
        }

        log.info("当前线程ID：{}，用户ID：{}", Thread.currentThread().getId(), userId);

        // 将用户ID存入ThreadLocal
        BaseContext.setCurrentId(userId);

        // 将用户ID放入请求属性中，供后续使用
        request.setAttribute("userId", userId);
        return true;
    }

    /**
     * 输出错误信息
     */
    private void responseError(HttpServletResponse response, String message) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            Result<Void> result = Result.error(ErrorCodeEnum.UNAUTHORIZED, message);
            out.write(objectMapper.writeValueAsString(result));
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        // 清理ThreadLocal，防止内存泄漏
        BaseContext.removeCurrentId();
    }
}