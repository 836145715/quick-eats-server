package com.zmx.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 地址工具类
 */
public class AddressUtils {

    /**
     * 获取性别描述
     *
     * @param sex 性别代码
     * @return 性别描述
     */
    public static String getSexText(String sex) {
        if (StringUtils.isEmpty(sex)) {
            return "未知";
        }
        
        switch (sex) {
            case "0":
                return "女";
            case "1":
                return "男";
            default:
                return "未知";
        }
    }

    /**
     * 构建完整地址
     *
     * @param provinceName 省份名称
     * @param cityName 城市名称
     * @param districtName 区县名称
     * @param detail 详细地址
     * @return 完整地址
     */
    public static String buildFullAddress(String provinceName, String cityName, String districtName, String detail) {
        StringBuilder fullAddress = new StringBuilder();
        
        if (StringUtils.isNotEmpty(provinceName)) {
            fullAddress.append(provinceName);
        }
        
        if (StringUtils.isNotEmpty(cityName) && !cityName.equals(provinceName)) {
            fullAddress.append(cityName);
        }
        
        if (StringUtils.isNotEmpty(districtName)) {
            fullAddress.append(districtName);
        }
        
        if (StringUtils.isNotEmpty(detail)) {
            fullAddress.append(detail);
        }
        
        return fullAddress.toString();
    }

    /**
     * 验证手机号格式
     *
     * @param phone 手机号
     * @return 是否有效
     */
    public static boolean isValidPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        
        // 中国大陆手机号正则表达式
        String phoneRegex = "^1[3-9]\\d{9}$";
        return phone.matches(phoneRegex);
    }

    /**
     * 脱敏手机号
     *
     * @param phone 手机号
     * @return 脱敏后的手机号
     */
    public static String maskPhone(String phone) {
        if (StringUtils.isEmpty(phone) || phone.length() != 11) {
            return phone;
        }
        
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 脱敏地址
     *
     * @param address 地址
     * @return 脱敏后的地址
     */
    public static String maskAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            return address;
        }
        
        if (address.length() <= 6) {
            return address;
        }
        
        // 保留前3个字符和后3个字符，中间用*号代替
        int maskLength = address.length() - 6;
        StringBuilder masked = new StringBuilder();
        masked.append(address.substring(0, 3));
        for (int i = 0; i < maskLength; i++) {
            masked.append("*");
        }
        masked.append(address.substring(address.length() - 3));
        
        return masked.toString();
    }

    /**
     * 验证地址是否完整
     *
     * @param provinceName 省份名称
     * @param cityName 城市名称
     * @param districtName 区县名称
     * @param detail 详细地址
     * @return 是否完整
     */
    public static boolean isCompleteAddress(String provinceName, String cityName, String districtName, String detail) {
        return StringUtils.isNotEmpty(provinceName) 
                && StringUtils.isNotEmpty(cityName) 
                && StringUtils.isNotEmpty(districtName) 
                && StringUtils.isNotEmpty(detail);
    }
}
