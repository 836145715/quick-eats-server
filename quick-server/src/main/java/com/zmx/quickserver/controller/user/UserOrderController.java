package com.zmx.quickserver.controller.user;

import com.zmx.common.annotation.ApiLog;
import com.zmx.common.response.PageResult;
import com.zmx.common.response.Result;
import com.zmx.common.utils.BaseContext;
import com.zmx.quickpojo.dto.OrderPageListReqDTO;
import com.zmx.quickpojo.dto.OrderSubmitReqDTO;
import com.zmx.quickpojo.vo.OrderPageListRspVO;
import com.zmx.quickpojo.vo.OrderSubmitRspVO;
import com.zmx.quickserver.service.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户端订单控制器
 */
@RestController
@RequestMapping("/user/order")
@Slf4j
@Tag(name = "用户端订单接口", description = "用户端订单相关接口")
public class UserOrderController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 用户下单
     *
     * @param orderSubmitDTO 订单提交信息
     * @return 订单提交结果
     */
    @PostMapping("/submit")
    @ApiLog
    @Operation(summary = "用户下单", description = "用户提交订单接口")
    public Result<OrderSubmitRspVO> submit(@RequestBody @Valid OrderSubmitReqDTO orderSubmitDTO) {
        log.info("用户下单：{}", orderSubmitDTO);
        return ordersService.submitOrder(orderSubmitDTO);
    }

    /**
     * 历史订单查询
     *
     * @param dto 查询参数
     * @return 分页结果
     */
    @PostMapping("/historyOrders")
    @ApiLog
    @Operation(summary = "历史订单查询", description = "用户查询历史订单接口")
    public PageResult<OrderPageListRspVO> historyOrders(@RequestBody OrderPageListReqDTO dto) {
        log.info("历史订单查询：{}", dto);

        // 设置当前用户ID
        dto.setUserId(BaseContext.getCurrentId());

        return ordersService.pageList(dto);
    }

    /**
     * 查询订单详情
     *
     * @param id 订单ID
     * @return 订单详情
     */
    @GetMapping("/orderDetail/{id}")
    @ApiLog
    @Operation(summary = "查询订单详情", description = "根据订单ID查询订单详情接口")
    public Result<OrderPageListRspVO> orderDetail(@PathVariable Long id) {
        log.info("查询订单详情：{}", id);
        return ordersService.getById(id);
    }

    /**
     * 取消订单
     *
     * @param id 订单ID
     * @return 操作结果
     */
    @GetMapping("/cancel/{id}")
    @ApiLog
    @Operation(summary = "取消订单", description = "用户取消订单接口")
    public Result<Void> cancel(@PathVariable Long id) {
        log.info("用户取消订单：{}", id);
        return ordersService.cancelOrder(id, "用户取消订单");
    }

    /**
     * 再来一单
     *
     * @param id 订单ID
     * @return 操作结果
     */
    @PostMapping("/repetition/{id}")
    @ApiLog
    @Operation(summary = "再来一单", description = "根据订单ID再来一单接口")
    public Result<Void> repetition(@PathVariable Long id) {
        log.info("再来一单：{}", id);
        return ordersService.repetitionOrder(id);
    }

    /**
     * 催单
     *
     * @param id 订单ID
     * @return 操作结果
     */
    @GetMapping("/reminder/{id}")
    @ApiLog
    @Operation(summary = "催单", description = "用户催单接口")
    public Result<Void> reminder(@PathVariable Long id) {
        log.info("用户催单：{}", id);
        return ordersService.reminderOrder(id);
    }

    /**
     * 模拟支付
     *
     * @param id
     * @return
     */
    @GetMapping("/pay/{id}")
    @ApiLog
    @Operation(summary = "支付订单", description = "用户支付订单接口")
    public Result<Void> pay(@PathVariable Long id) {
        log.info("用户支付订单：{}", id);
        return ordersService.payOrder(id);
    }
}
