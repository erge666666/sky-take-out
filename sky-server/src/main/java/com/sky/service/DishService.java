package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    void saveWithFlavor(DishDTO dishDTO);

    PageResult dishPage(DishPageQueryDTO dishPageQueryDTO);

    void Del(Long[] ids);

    //根据id查询菜品和对应的口味
    DishVO getByidwithFlavor(Long id);

    //修改菜品
    void dishupdate(DishDTO dishDTO);

    List<Dish> getcategoryid(Long categoryId);


    //user


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
