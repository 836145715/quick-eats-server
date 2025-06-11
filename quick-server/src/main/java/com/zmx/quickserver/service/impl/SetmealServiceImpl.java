package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.common.constants.StatusConstant;
import com.zmx.common.response.PageResult;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.SetmealAddReqDTO;
import com.zmx.quickpojo.dto.SetmealPageListReqDTO;
import com.zmx.quickpojo.dto.SetmealStatusDTO;
import com.zmx.quickpojo.entity.Category;
import com.zmx.quickpojo.entity.Dish;
import com.zmx.quickpojo.entity.Setmeal;
import com.zmx.quickpojo.entity.SetmealDish;
import com.zmx.quickpojo.vo.SetmealDishVO;
import com.zmx.quickpojo.vo.SetmealPageListRspVO;
import com.zmx.quickserver.mapper.DishMapper;
import com.zmx.quickserver.mapper.SetmealDishMapper;
import com.zmx.quickserver.mapper.SetmealMapper;
import com.zmx.quickserver.service.CategoryService;
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
 * 套餐服务实现类
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增套餐
     *
     * @param setmealDTO 套餐信息
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result<Void> add(SetmealAddReqDTO setmealDTO) {
        // 1. 保存套餐基本信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        // 设置状态为启用
        setmeal.setStatus(StatusConstant.ENABLE);

        // 保存套餐（创建时间、更新时间、创建人、更新人会由MyBatisPlus自动填充）
        boolean success = save(setmeal);
        if (!success) {
            return Result.error("添加套餐失败");
        }

        // 2. 保存套餐菜品关联信息
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes().stream().map(item -> {
            SetmealDish setmealDish = new SetmealDish();
            setmealDish.setSetmealId(setmeal.getId());
            setmealDish.setDishId(item.getDishId());
            setmealDish.setName(item.getName());
            setmealDish.setPrice(item.getPrice());
            setmealDish.setCopies(item.getCopies());
            return setmealDish;
        }).collect(Collectors.toList());

        // 批量保存套餐菜品关联信息
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDishMapper.insert(setmealDish);
        }

        return Result.success();
    }

    /**
     * 套餐分页查询
     *
     * @param dto 查询参数
     * @return 分页结果
     */
    @Override
    public PageResult pageList(SetmealPageListReqDTO dto) {
        // 构建查询条件
        LambdaQueryChainWrapper<Setmeal> query = lambdaQuery();
        query.like(StringUtils.isNotBlank(dto.getName()), Setmeal::getName, dto.getName());
        query.eq(dto.getCategoryId() != null, Setmeal::getCategoryId, dto.getCategoryId());
        query.eq(dto.getStatus() != null, Setmeal::getStatus, dto.getStatus());

        // 排序
        query.orderByDesc(Setmeal::getUpdateTime);

        // 分页查询
        Page<Setmeal> page = new Page<>(dto.getCurrent(), dto.getSize());
        IPage<Setmeal> pageResult = query.page(page);

        // 转VO
        List<Setmeal> records = pageResult.getRecords();
        List<SetmealPageListRspVO> vos = new ArrayList<>();

        for (Setmeal setmeal : records) {
            SetmealPageListRspVO vo = new SetmealPageListRspVO();
            BeanUtils.copyProperties(setmeal, vo);

            // 查询分类名称
            Category category = categoryService.getById(setmeal.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }

            // 查询套餐包含的菜品
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
            List<SetmealDish> setmealDishes = setmealDishMapper.selectList(queryWrapper);

            vo.setSetmealDishes(setmealDishes.stream().map(item -> {
                return com.zmx.quickpojo.dto.SetmealDishDTO.builder()
                        .dishId(item.getDishId())
                        .name(item.getName())
                        .price(item.getPrice())
                        .copies(item.getCopies())
                        .build();
            }).collect(Collectors.toList()));

            vos.add(vo);
        }

        return PageResult.success(pageResult, vos);
    }

    /**
     * 删除套餐
     *
     * @param id 套餐ID
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result deleteById(long id) {
        // 1. 查询套餐状态，起售中的套餐不能删除
        Setmeal setmeal = getById(id);
        if (setmeal == null) {
            return Result.error("套餐不存在");
        }

        if (StatusConstant.ENABLE == setmeal.getStatus()) {
            return Result.error("套餐正在售卖中，不能删除");
        }

        // 2. 删除套餐
        boolean success = removeById(id);
        if (!success) {
            return Result.error("删除套餐失败");
        }

        // 3. 删除套餐关联的菜品数据
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        setmealDishMapper.delete(queryWrapper);

        return Result.success();
    }

    /**
     * 更新套餐状态
     *
     * @param statusDTO 状态信息
     * @return 操作结果
     */
    @Override
    public Result updateStatus(SetmealStatusDTO statusDTO) {
        // 1. 根据ID查询套餐
        Setmeal setmeal = getById(statusDTO.getId());
        if (setmeal == null) {
            return Result.error("套餐不存在");
        }

        // 2. 更新状态（更新时间和更新人会由MyBatisPlus自动填充）
        setmeal.setStatus(statusDTO.getStatus());

        // 3. 执行更新
        boolean success = updateById(setmeal);
        if (success) {
            return Result.success();
        }

        return Result.error("更新套餐状态失败");
    }

    /**
     * 更新套餐信息
     *
     * @param setmealDTO 套餐信息
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result update(SetmealAddReqDTO setmealDTO) {
        // 1. 根据ID查询套餐
        Setmeal setmeal = getById(setmealDTO.getId());
        if (setmeal == null) {
            return Result.error("套餐不存在");
        }

        // 2. 更新套餐基本信息
        BeanUtils.copyProperties(setmealDTO, setmeal);
        boolean success = updateById(setmeal);
        if (!success) {
            return Result.error("更新套餐信息失败");
        }

        // 3. 更新套餐菜品关联信息
        // 3.1 先删除原有关联
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
        setmealDishMapper.delete(queryWrapper);

        // 3.2 再添加新关联
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes().stream().map(item -> {
            SetmealDish setmealDish = new SetmealDish();
            setmealDish.setSetmealId(setmeal.getId());
            setmealDish.setDishId(item.getDishId());
            setmealDish.setName(item.getName());
            setmealDish.setPrice(item.getPrice());
            setmealDish.setCopies(item.getCopies());
            return setmealDish;
        }).collect(Collectors.toList());

        // 批量保存套餐菜品关联信息
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDishMapper.insert(setmealDish);
        }

        return Result.success();
    }

    /**
     * 根据分类ID查询套餐列表
     *
     * @param categoryId 分类ID
     * @return 套餐列表
     */
    @Override
    public Result<List<Setmeal>> listByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null, Setmeal::getCategoryId, categoryId);
        queryWrapper.eq(Setmeal::getStatus, StatusConstant.ENABLE); // 只查询启用状态的套餐
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> setmeals = list(queryWrapper);
        return Result.success(setmeals);
    }

    @Override
    public Result<List<SetmealDishVO>> getDish(Long id) {

        // 1. 查询套餐
        Setmeal setmeal = getById(id);
        if (setmeal == null) {
            return Result.error("套餐不存在");
        }
        // 查询套餐菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(queryWrapper);

        // 查询菜品
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.in(Dish::getId,
                setmealDishes.stream().map(SetmealDish::getDishId).collect(Collectors.toList()));

        // 份数map
        Map<Long, Integer> copiesMap = setmealDishes.stream()
                .collect(Collectors.toMap(SetmealDish::getDishId, SetmealDish::getCopies));

        List<Dish> dishes = dishMapper.selectList(dishQueryWrapper);
        return Result.success(dishes.stream().map(item -> {
            return SetmealDishVO.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .price(item.getPrice())
                    .image(item.getImage())
                    .description(item.getDescription())
                    .copies(copiesMap.get(item.getId()))
                    .build();

        }).collect(Collectors.toList()));
    }
}