package com.zmx.quickpojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String openid;

    private String name;

    private String phone;

    private String sex;

    private String idNumber;

    private String avatar;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}