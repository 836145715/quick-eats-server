package com.zmx.quickpojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商品销量Top10响应数据")
public class SalesTop10RspVO {

    @Schema(description = "商品名称")
    private List<String> names;

    @Schema(description = "商品销量")
    private List<Long> sales;

}
