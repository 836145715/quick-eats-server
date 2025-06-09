package com.zmx.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * HTTP工具类
 */
@Slf4j
@Component
public class HttpUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 发送GET请求
     *
     * @param urlString 请求URL
     * @return 响应内容
     */
    public static String doGet(String urlString) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            log.info("HTTP GET请求: {}, 响应码: {}", urlString, responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } else {
                log.error("HTTP GET请求失败, URL: {}, 响应码: {}", urlString, responseCode);
                return null;
            }

        } catch (IOException e) {
            log.error("HTTP GET请求异常, URL: {}", urlString, e);
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("关闭BufferedReader异常", e);
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        String result = response.toString();
        log.info("HTTP GET响应: {}", result);
        return result;
    }

    /**
     * 发送GET请求并解析为指定类型的对象
     *
     * @param urlString 请求URL
     * @param clazz     目标类型
     * @param <T>       泛型类型
     * @return 解析后的对象
     */
    public static <T> T doGet(String urlString, Class<T> clazz) {
        String response = doGet(urlString);
        if (response == null) {
            return null;
        }

        try {
            return objectMapper.readValue(response, clazz);
        } catch (Exception e) {
            log.error("解析HTTP响应异常, URL: {}, 响应: {}", urlString, response, e);
            return null;
        }
    }
}
