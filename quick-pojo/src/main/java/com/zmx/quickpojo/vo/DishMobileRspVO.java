package com.zmx.quickpojo.vo;

import com.zmx.quickpojo.entity.Dish;
import com.zmx.quickpojo.entity.DishFlavor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "移动端菜品和套餐响应VO")
public class DishMobileRspVO {

    // 分类 - 菜品.
    @Schema(description = "分类名称")
    private String categoryName;
    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "分类类型")
    private Integer type;

    // 菜品
    @Schema(description = "项目列表（菜品或套餐）")
    private List<DishAndSetmealVO> items;

}
