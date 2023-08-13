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

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 根据id查询菜品和对应的口味
     * @param id
     * @return
     */
    @Override
    @Transactional
    public DishVO getByidwithFlavor(Long id) {
        //查询处理菜品
        Dish dish = dishMapper.getByid(id);
        //查询放集合里面口味
        List<DishFlavor> list=dishFlavorMapper.getBydishid(id);
//        返回vo
        DishVO dishVO=new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(list);
        return dishVO;
    }

    /**
     * 修改菜品
     * @param dishDTO
     */
    @Override
    @Transactional
    public void dishupdate(DishDTO dishDTO) {
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //修改菜品
        dishMapper.dishupdate(dish);
        //删除全部口味再添加
        dishFlavorMapper.del(dishDTO.getId());
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors!=null && flavors.size()!=0){
            //有口味就全部添加上dishid
            flavors.forEach(flavor -> flavor.setDishId(dishDTO.getId()));
            //调用口味mapper
            dishFlavorMapper.save(flavors);
        }

    }

    //根据分类查询菜品
    @Override
    public List<Dish> getcategoryid(Long categoryId) {
        List<Dish>dish=dishMapper.getcategoryid(categoryId);
        return dish;
    }


//user

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getBydishid(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
