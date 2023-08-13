package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;

import java.util.List;

public interface SetmealService {
    void save(SetmealDTO setmealDTO);

    PageResult getpage(SetmealPageQueryDTO setmealPageQueryDTO);

    //起售
    void update(int status,Long id);

    //修改套餐
    void allupdate(SetmealDTO setmealDTO);

    //id查询套餐
    SetmealDTO getbyid(Long id);

    void del(List<Long> ids);


//user
    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}
