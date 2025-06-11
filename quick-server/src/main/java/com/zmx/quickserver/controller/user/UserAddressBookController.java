package com.zmx.quickserver.controller.user;

import com.zmx.common.annotation.ApiLog;
import com.zmx.common.response.Result;
import com.zmx.common.utils.AddressUtils;
import com.zmx.quickpojo.dto.AddressBookAddReqDTO;
import com.zmx.quickpojo.dto.AddressValidateReqDTO;
import com.zmx.quickpojo.vo.AddressBookVO;
import com.zmx.quickserver.service.AddressBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户端地址簿控制器
 */
@RestController
@RequestMapping("/user/addressBook")
@Slf4j
@Tag(name = "用户端地址簿接口", description = "用户端地址簿相关接口")
public class UserAddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     *
     * @param addressBookDTO 地址信息
     * @return 操作结果
     */
    @PostMapping
    @ApiLog
    @Operation(summary = "新增地址", description = "用户新增收货地址接口")
    public Result<Void> add(@RequestBody @Valid AddressBookAddReqDTO addressBookDTO) {
        log.info("新增地址：{}", addressBookDTO);
        return addressBookService.addAddress(addressBookDTO);
    }

    /**
     * 查询地址列表
     *
     * @return 地址列表
     */
    @GetMapping("/list")
    @ApiLog
    @Operation(summary = "查询地址列表", description = "查询当前用户的地址列表接口")
    public Result<List<AddressBookVO>> list() {
        log.info("查询地址列表");
        return addressBookService.listByUser();
    }

    /**
     * 查询默认地址
     *
     * @return 默认地址
     */
    @GetMapping("/default")
    @ApiLog
    @Operation(summary = "查询默认地址", description = "查询当前用户的默认地址接口")
    public Result<AddressBookVO> getDefault() {
        log.info("查询默认地址");
        return addressBookService.getDefault();
    }

    /**
     * 根据ID查询地址
     *
     * @param id 地址ID
     * @return 地址详情
     */
    @GetMapping("/{id}")
    @ApiLog
    @Operation(summary = "根据ID查询地址", description = "根据地址ID查询地址详情接口")
    public Result<AddressBookVO> getById(@PathVariable Long id) {
        log.info("根据ID查询地址：{}", id);
        return addressBookService.getAddressById(id);
    }

    /**
     * 修改地址
     *
     * @param addressBookDTO 地址信息
     * @return 操作结果
     */
    @PutMapping
    @ApiLog
    @Operation(summary = "修改地址", description = "用户修改收货地址接口")
    public Result<Void> update(@RequestBody @Valid AddressBookAddReqDTO addressBookDTO) {
        log.info("修改地址：{}", addressBookDTO);
        return addressBookService.updateAddress(addressBookDTO);
    }

    /**
     * 删除地址
     *
     * @param id 地址ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @ApiLog
    @Operation(summary = "删除地址", description = "用户删除收货地址接口")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("删除地址：{}", id);
        return addressBookService.deleteAddressById(id);
    }

    /**
     * 设置默认地址
     *
     * @param id 地址ID
     * @return 操作结果
     */
    @PostMapping("/default/{id}")
    @ApiLog
    @Operation(summary = "设置默认地址", description = "设置默认收货地址接口")
    public Result<Void> setDefault(@PathVariable Long id) {
        log.info("设置默认地址：{}", id);
        return addressBookService.setDefault(id);
    }

    /**
     * 验证地址完整性
     *
     * @param validateDTO 地址验证信息
     * @return 验证结果
     */
    @PostMapping("/validate")
    @ApiLog
    @Operation(summary = "验证地址完整性", description = "验证地址信息是否完整接口")
    public Result<String> validateAddress(@RequestBody @Valid AddressValidateReqDTO validateDTO) {
        log.info("验证地址完整性：{}", validateDTO);

        boolean isComplete = AddressUtils.isCompleteAddress(
                validateDTO.getProvinceName(),
                validateDTO.getCityName(),
                validateDTO.getDistrictName(),
                validateDTO.getDetail());

        if (!isComplete) {
            return Result.error("地址信息不完整");
        }

        String fullAddress = AddressUtils.buildFullAddress(
                validateDTO.getProvinceName(),
                validateDTO.getCityName(),
                validateDTO.getDistrictName(),
                validateDTO.getDetail());

        return Result.success(fullAddress);
    }
}
