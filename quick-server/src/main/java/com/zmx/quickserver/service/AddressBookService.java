package com.zmx.quickserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.AddressBookAddReqDTO;
import com.zmx.quickpojo.entity.AddressBook;
import com.zmx.quickpojo.vo.AddressBookVO;

import java.util.List;

/**
 * 地址簿Service接口
 */
public interface AddressBookService extends IService<AddressBook> {

    /**
     * 新增地址
     *
     * @param addressBookDTO 地址信息
     * @return 操作结果
     */
    Result<Void> addAddress(AddressBookAddReqDTO addressBookDTO);

    /**
     * 更新地址
     *
     * @param addressBookDTO 地址信息
     * @return 操作结果
     */
    Result<Void> updateAddress(AddressBookAddReqDTO addressBookDTO);

    /**
     * 删除地址
     *
     * @param id 地址ID
     * @return 操作结果
     */
    Result<Void> deleteAddressById(Long id);

    /**
     * 查询用户地址列表
     *
     * @return 地址列表
     */
    Result<List<AddressBookVO>> listByUser();

    /**
     * 根据ID查询地址详情
     *
     * @param id 地址ID
     * @return 地址详情
     */
    Result<AddressBookVO> getAddressById(Long id);

    /**
     * 设置默认地址
     *
     * @param id 地址ID
     * @return 操作结果
     */
    Result<Void> setDefault(Long id);

    /**
     * 获取默认地址
     *
     * @return 默认地址
     */
    Result<AddressBookVO> getDefault();
}