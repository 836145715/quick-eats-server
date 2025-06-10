package com.zmx.quickpojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "菜品和套餐的统一视图对象")
public class DishAndSetmealVO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "类型，1表示菜品，2表示套餐")
    private Integer type;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "图片")
    private String image;
}