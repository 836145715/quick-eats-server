package com.zmx.quickpojo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeePageListReqDTO {

    long current;
    long size;

    private String name;

    private String username;

    private String phone;

    private Integer sex;
}
