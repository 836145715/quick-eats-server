package com.zmx.quickpojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "菜品和套餐数据")
public class DashboardDishSetMealVO {
    @Schema(description = "菜品启用数量")
    private Long dishEnableCount;
    @Schema(description = "菜品禁用数量")
    private Long dishDisableCount;
    @Schema(description = "套餐启用数量")
    private Long setMealEnableCount;
    @Schema(description = "套餐禁用数量")
    private Long setMealDisableCount;
}
