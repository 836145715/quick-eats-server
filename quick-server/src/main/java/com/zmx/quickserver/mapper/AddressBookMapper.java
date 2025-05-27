package com.zmx.quickserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmx.quickpojo.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地址簿Mapper接口
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}