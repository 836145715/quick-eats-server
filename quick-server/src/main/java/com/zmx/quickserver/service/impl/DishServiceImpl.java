package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.common.constants.StatusConstant;
import com.zmx.common.response.PageResult;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.DishAddReqDTO;
import com.zmx.quickpojo.dto.DishFlavorDTO;
import com.zmx.quickpojo.dto.DishPageListReqDTO;
import com.zmx.quickpojo.dto.DishStatusDTO;
import com.zmx.quickpojo.entity.Category;
import com.zmx.quickpojo.entity.Dish;
import com.zmx.quickpojo.entity.DishFlavor;
import com.zmx.quickpojo.entity.Setmeal;
import com.zmx.quickpojo.vo.DishAndSetmealVO;
import com.zmx.quickpojo.vo.DishMobileRspVO;
import com.zmx.quickpojo.vo.DishPageListRspVO;
import com.zmx.quickserver.mapper.DishFlavorMapper;
import com.zmx.quickserver.mapper.DishMapper;
import com.zmx.quickserver.service.CategoryService;
import com.zmx.quickserver.service.DishService;
import com.zmx.quickserver.service.SetmealService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜品服务实现类
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增菜品
     *
     * @param dishDTO 菜品信息
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result<Void> add(DishAddReqDTO dishDTO) {
        // 1. 保存菜品基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 设置状态为启用
        dish.setStatus(StatusConstant.ENABLE);

        // 保存菜品（创建时间、更新时间、创建人、更新人会由MyBatisPlus自动填充）
        boolean success = save(dish);
        if (!success) {
            return Result.error("添加菜品失败");
        }

        // 2. 保存菜品口味
        List<DishFlavorDTO> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            List<DishFlavor> flavorList = flavors.stream().map(flavorDTO -> {
                DishFlavor flavor = new DishFlavor();
                BeanUtils.copyProperties(flavorDTO, flavor);
                flavor.setDishId(dish.getId());
                return flavor;
            }).collect(Collectors.toList());

            // 批量保存口味
            for (DishFlavor flavor : flavorList) {
                dishFlavorMapper.insert(flavor);
            }
        }

        return Result.success();
    }

    /**
     * 菜品分页查询
     *
     * @param dto 查询参数
     * @return 分页结果
     */
    @Override
    public PageResult pageList(DishPageListReqDTO dto) {
        // 构建查询条件
        LambdaQueryChainWrapper<Dish> query = lambdaQuery();
        query.like(StringUtils.isNotBlank(dto.getName()), Dish::getName, dto.getName());
        query.eq(dto.getCategoryId() != null, Dish::getCategoryId, dto.getCategoryId());
        query.eq(dto.getStatus() != null, Dish::getStatus, dto.getStatus());

        // 排序
        query.orderByDesc(Dish::getUpdateTime);

        // 分页查询
        Page<Dish> page = new Page<>(dto.getCurrent(), dto.getSize());
        IPage<Dish> pageResult = query.page(page);

        // 转VO
        List<Dish> records = pageResult.getRecords();
        List<DishPageListRspVO> vos = new ArrayList<>();

        List<Long> dishIds = records.stream().map(Dish::getId).toList();
        List<DishFlavor> flavors = dishFlavorMapper.selectByDishIds(dishIds);

        for (Dish dish : records) {

            List<DishFlavorDTO> dishFlavors = flavors.stream()
                    .filter(flavor -> flavor.getDishId().equals(dish.getId()))
                    .map(item -> DishFlavorDTO.builder().name(item.getName()).value(item.getValue()).build())
                    .toList();

            DishPageListRspVO vo = DishPageListRspVO.builder()
                    .id(dish.getId())
                    .name(dish.getName())
                    .categoryId(dish.getCategoryId())
                    .price(dish.getPrice())
                    .image(dish.getImage())
                    .description(dish.getDescription())
                    .status(dish.getStatus())
                    .flavors(dishFlavors)
                    .build();

            // 查询分类名称
            Category category = categoryService.getById(dish.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }

            vos.add(vo);
        }

        return PageResult.success(pageResult, vos);
    }

    /**
     * 删除菜品
     *
     * @param id 菜品ID
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result deleteById(long id) {
        // 1. 删除菜品
        boolean success = removeById(id);
        if (!success) {
            return Result.error("删除菜品失败");
        }

        // 2. 删除菜品关联的口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        dishFlavorMapper.delete(queryWrapper);

        return Result.success();
    }

    /**
     * 更新菜品状态
     *
     * @param statusDTO 状态信息
     * @return 操作结果
     */
    @Override
    public Result updateStatus(DishStatusDTO statusDTO) {
        // 1. 根据ID查询菜品
        Dish dish = getById(statusDTO.getId());
        if (dish == null) {
            return Result.error("菜品不存在");
        }

        // 2. 更新状态（更新时间和更新人会由MyBatisPlus自动填充）
        dish.setStatus(statusDTO.getStatus());

        // 3. 执行更新
        boolean success = updateById(dish);
        if (success) {
            return Result.success();
        }

        return Result.error("更新菜品状态失败");
    }

    /**
     * 更新菜品信息
     *
     * @param dishDTO 菜品信息
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result update(DishAddReqDTO dishDTO) {
        // 1. 根据ID查询菜品
        Dish dish = getById(dishDTO.getId());
        if (dish == null) {
            return Result.error("菜品不存在");
        }

        // 2. 更新菜品基本信息
        BeanUtils.copyProperties(dishDTO, dish);
        boolean success = updateById(dish);
        if (!success) {
            return Result.error("更新菜品信息失败");
        }

        // 3. 更新菜品口味信息
        // 3.1 先删除原有口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        dishFlavorMapper.delete(queryWrapper);

        // 3.2 再添加新口味
        List<DishFlavorDTO> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            List<DishFlavor> flavorList = flavors.stream().map(flavorDTO -> {
                DishFlavor flavor = new DishFlavor();
                BeanUtils.copyProperties(flavorDTO, flavor);
                flavor.setDishId(dish.getId());
                return flavor;
            }).collect(Collectors.toList());

            // 批量保存口味
            for (DishFlavor flavor : flavorList) {
                dishFlavorMapper.insert(flavor);
            }
        }

        return Result.success();
    }

    /**
     * 根据分类ID查询菜品列表
     *
     * @param categoryId 分类ID
     * @return 菜品列表
     */
    @Override
    public Result<List<Dish>> listByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null, Dish::getCategoryId, categoryId);
        queryWrapper.eq(Dish::getStatus, StatusConstant.ENABLE); // 只查询启用状态的菜品
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        List<Dish> dishes = list(queryWrapper);
        return Result.success(dishes);
    }

    @Override
    public Result<List<DishMobileRspVO>> listMobile() {
        //查询所有启用的分类
        LambdaQueryWrapper<Category> categoryQuery = new LambdaQueryWrapper<>();
        categoryQuery.eq(Category::getStatus, StatusConstant.ENABLE);
        categoryQuery.orderByAsc(Category::getSort);
        List<Category> categories = categoryService.list(categoryQuery);
        Map<Long, DishMobileRspVO> categoryMap = categories.stream()
                .map(category -> DishMobileRspVO.builder()
                        .categoryName(category.getName())
                        .categoryId(category.getId())
                        .type(category.getType())
                        .items(new ArrayList<>())
                        .build())
                .collect(Collectors.toMap(DishMobileRspVO::getCategoryId, vo -> vo));

        // 2. 查询所有启用的菜品并分配
        LambdaQueryWrapper<Dish> dishQuery = new LambdaQueryWrapper<>();
        dishQuery.eq(Dish::getStatus, StatusConstant.ENABLE);
        List<Dish> dishes = list(dishQuery);

        //遍历菜品 填充到对应的分类下
        for (Dish dish : dishes) {
            DishMobileRspVO vo = categoryMap.get(dish.getCategoryId());
            if (vo != null) {
                vo.getItems().add(DishAndSetmealVO.builder()
                        .id(dish.getId())
                        .name(dish.getName())
                        .price(dish.getPrice())
                        .description(dish.getDescription())
                        .image(dish.getImage())
                        .type(1) // 1表示菜品
                        .build());
            }
        }

        // 3. 查询所有启用的套餐并分配
        LambdaQueryWrapper<Setmeal> setmealQuery = new LambdaQueryWrapper<>();
        setmealQuery.eq(Setmeal::getStatus, StatusConstant.ENABLE);
        List<Setmeal> setmeals = setmealService.list(setmealQuery);
        for (Setmeal setmeal : setmeals) {
            DishMobileRspVO vo = categoryMap.get(setmeal.getCategoryId());
            if (vo != null) {
                vo.getItems().add(DishAndSetmealVO.builder()
                        .id(setmeal.getId())
                        .name(setmeal.getName())
                        .price(setmeal.getPrice())
                        .description(setmeal.getDescription())
                        .image(setmeal.getImage())
                        .type(2) // 2表示套餐
                        .build());
            }
        }


        // 按分类顺序来排序
        List<DishMobileRspVO> result = new ArrayList<>();
        for (Category category : categories) {
            DishMobileRspVO vo = categoryMap.get(category.getId());
            if (vo!= null) {
                result.add(vo);
            }
        }

        return Result.success(result);
    }
}