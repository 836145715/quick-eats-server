package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.quickpojo.entity.AddressBook;
import com.zmx.quickserver.mapper.AddressBookMapper;
import com.zmx.quickserver.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * 地址簿Service实现类
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}