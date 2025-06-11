package com.zmx.quickpojo.vo;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Schema(description = "套餐菜品VO")
@Builder
public class SetmealDishVO {
    @Schema(description = "菜品ID")
    private Long id;
    @Schema(description = "菜品名称")
    private String name;
    @Schema(description = "菜品价格")
    private BigDecimal price;
    @Schema(description = "菜品数量")
    private Integer copies;
    @Schema(description = "菜品图片")
    private String image;
    @Schema(description = "菜品描述")
    private String description;
}
