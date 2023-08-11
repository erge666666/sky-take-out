package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmeaDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmeaDishMapper setmeaDishMapper;


    @Override
    //操作多张表事务管理
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //创建一个菜品
        dishMapper.save(dish);
        Long dishId = dish.getId();
        //取出来口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors!=null && flavors.size()!=0){
            //有口味就全部添加上dishid
            flavors.forEach(flavor -> flavor.setDishId(dishId));
            //调用口味mapper
            dishFlavorMapper.save(flavors);
        }
    }


    //分页
    @Override
    public PageResult dishPage(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        //返回dish的菜品
        Page<DishVO> page=dishMapper.dishPage(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //删除
    @Override
    @Transactional
    public void Del(Long[] ids) {
//        判断是不是正在售卖，售卖中不能删除
        for (Long id : ids) {
            Dish d=dishMapper.getByid(id);
            if(d.getStatus() == StatusConstant.ENABLE){
                //起售
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //关联套餐也不能删除
        List<Long> setmealid=setmeaDishMapper.getbyid(ids);
        if (setmealid!=null&&setmealid.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
//删除菜品
        for (Long id : ids) {
            dishMapper.del(id);
            dishFlavorMapper.del(id);
        }
        //删除口味关联的

    }
}
