package com.zmx.quickpojo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeePageListRspVO {

    private Long id;

    private String name;

    private String username;

    private String phone;

    private String sex;

    private Integer status;

    private String idNumber;
}
