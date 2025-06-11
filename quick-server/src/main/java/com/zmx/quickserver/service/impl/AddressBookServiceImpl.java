package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.common.response.Result;
import com.zmx.common.utils.AddressUtils;
import com.zmx.common.utils.BaseContext;
import com.zmx.quickpojo.dto.AddressBookAddReqDTO;
import com.zmx.quickpojo.entity.AddressBook;
import com.zmx.quickpojo.vo.AddressBookVO;
import com.zmx.quickserver.mapper.AddressBookMapper;
import com.zmx.quickserver.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 地址簿Service实现类
 */
@Service
@Slf4j
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    /**
     * 新增地址
     *
     * @param addressBookDTO 地址信息
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result<Void> addAddress(AddressBookAddReqDTO addressBookDTO) {
        log.info("新增地址：{}", addressBookDTO);

        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return Result.error("用户未登录");
        }

        // 如果设置为默认地址，先取消其他默认地址
        if (Boolean.TRUE.equals(addressBookDTO.getIsDefault())) {
            clearDefaultAddress(userId);
        }

        // 构建地址实体
        AddressBook addressBook = new AddressBook();
        BeanUtils.copyProperties(addressBookDTO, addressBook);
        addressBook.setUserId(userId);

        // 如果是第一个地址，自动设为默认
        LambdaQueryWrapper<AddressBook> query = new LambdaQueryWrapper<>();
        query.eq(AddressBook::getUserId, userId);
        long count = count(query);
        if (count == 0) {
            addressBook.setIsDefault(true);
        }

        save(addressBook);
        return Result.success();
    }

    /**
     * 更新地址
     *
     * @param addressBookDTO 地址信息
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result<Void> updateAddress(AddressBookAddReqDTO addressBookDTO) {
        log.info("更新地址：{}", addressBookDTO);

        if (addressBookDTO.getId() == null) {
            return Result.error("地址ID不能为空");
        }

        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return Result.error("用户未登录");
        }

        // 验证地址是否属于当前用户
        AddressBook existAddress = getById(addressBookDTO.getId());
        if (existAddress == null) {
            return Result.error("地址不存在");
        }
        if (!existAddress.getUserId().equals(userId)) {
            return Result.error("无权限操作此地址");
        }

        // 如果设置为默认地址，先取消其他默认地址
        if (Boolean.TRUE.equals(addressBookDTO.getIsDefault())) {
            clearDefaultAddress(userId);
        }

        // 更新地址信息
        AddressBook addressBook = new AddressBook();
        BeanUtils.copyProperties(addressBookDTO, addressBook);
        addressBook.setUserId(userId);

        updateById(addressBook);
        return Result.success();
    }

    /**
     * 删除地址
     *
     * @param id 地址ID
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result<Void> deleteAddressById(Long id) {
        log.info("删除地址：{}", id);

        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return Result.error("用户未登录");
        }

        // 验证地址是否属于当前用户
        AddressBook existAddress = getById(id);
        if (existAddress == null) {
            return Result.error("地址不存在");
        }
        if (!existAddress.getUserId().equals(userId)) {
            return Result.error("无权限操作此地址");
        }

        // 删除地址
        removeById(id);

        // 如果删除的是默认地址，需要重新设置默认地址
        if (Boolean.TRUE.equals(existAddress.getIsDefault())) {
            setFirstAddressAsDefault(userId);
        }

        return Result.success();
    }

    /**
     * 查询用户地址列表
     *
     * @return 地址列表
     */
    @Override
    public Result<List<AddressBookVO>> listByUser() {
        log.info("查询用户地址列表");

        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return Result.error("用户未登录");
        }

        // 查询用户地址列表
        LambdaQueryWrapper<AddressBook> query = new LambdaQueryWrapper<>();
        query.eq(AddressBook::getUserId, userId);
        query.orderByDesc(AddressBook::getIsDefault);
        query.orderByDesc(AddressBook::getId);

        List<AddressBook> addressBooks = list(query);

        // 转换为VO
        List<AddressBookVO> addressBookVOs = addressBooks.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return Result.success(addressBookVOs);
    }

    /**
     * 根据ID查询地址详情
     *
     * @param id 地址ID
     * @return 地址详情
     */
    @Override
    public Result<AddressBookVO> getAddressById(Long id) {
        log.info("查询地址详情：{}", id);

        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return Result.error("用户未登录");
        }

        AddressBook addressBook = getById(id);
        if (addressBook == null) {
            return Result.error("地址不存在");
        }

        // 验证地址是否属于当前用户
        if (!addressBook.getUserId().equals(userId)) {
            return Result.error("无权限访问此地址");
        }

        AddressBookVO addressBookVO = convertToVO(addressBook);
        return Result.success(addressBookVO);
    }

    /**
     * 设置默认地址
     *
     * @param id 地址ID
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result<Void> setDefault(Long id) {
        log.info("设置默认地址：{}", id);

        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return Result.error("用户未登录");
        }

        // 验证地址是否属于当前用户
        AddressBook addressBook = getById(id);
        if (addressBook == null) {
            return Result.error("地址不存在");
        }
        if (!addressBook.getUserId().equals(userId)) {
            return Result.error("无权限操作此地址");
        }

        // 先取消其他默认地址
        clearDefaultAddress(userId);

        // 设置当前地址为默认
        AddressBook updateAddress = new AddressBook();
        updateAddress.setId(id);
        updateAddress.setIsDefault(true);
        updateById(updateAddress);

        return Result.success();
    }

    /**
     * 获取默认地址
     *
     * @return 默认地址
     */
    @Override
    public Result<AddressBookVO> getDefault() {
        log.info("获取默认地址");

        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return Result.error("用户未登录");
        }

        // 查询默认地址
        LambdaQueryWrapper<AddressBook> query = new LambdaQueryWrapper<>();
        query.eq(AddressBook::getUserId, userId);
        query.eq(AddressBook::getIsDefault, true);

        AddressBook addressBook = getOne(query);
        if (addressBook == null) {
            return Result.error("未设置默认地址");
        }

        AddressBookVO addressBookVO = convertToVO(addressBook);
        return Result.success(addressBookVO);
    }

    /**
     * 清除用户的默认地址
     *
     * @param userId 用户ID
     */
    private void clearDefaultAddress(Long userId) {
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId, userId);
        updateWrapper.eq(AddressBook::getIsDefault, true);
        updateWrapper.set(AddressBook::getIsDefault, false);
        update(updateWrapper);
    }

    /**
     * 设置第一个地址为默认地址
     *
     * @param userId 用户ID
     */
    private void setFirstAddressAsDefault(Long userId) {
        LambdaQueryWrapper<AddressBook> query = new LambdaQueryWrapper<>();
        query.eq(AddressBook::getUserId, userId);
        query.orderByAsc(AddressBook::getId);
        query.last("LIMIT 1");

        AddressBook firstAddress = getOne(query);
        if (firstAddress != null) {
            AddressBook updateAddress = new AddressBook();
            updateAddress.setId(firstAddress.getId());
            updateAddress.setIsDefault(true);
            updateById(updateAddress);
        }
    }

    /**
     * 转换为VO对象
     *
     * @param addressBook 地址实体
     * @return 地址VO
     */
    private AddressBookVO convertToVO(AddressBook addressBook) {
        AddressBookVO vo = new AddressBookVO();
        BeanUtils.copyProperties(addressBook, vo);

        // 设置性别描述
        vo.setSexText(AddressUtils.getSexText(addressBook.getSex()));

        // 构建完整地址
        String fullAddress = AddressUtils.buildFullAddress(
                addressBook.getProvinceName(),
                addressBook.getCityName(),
                addressBook.getDistrictName(),
                addressBook.getDetail());
        vo.setFullAddress(fullAddress);

        return vo;
    }
}