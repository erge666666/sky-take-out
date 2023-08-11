package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

public interface DishService {
    void saveWithFlavor(DishDTO dishDTO);

    PageResult dishPage(DishPageQueryDTO dishPageQueryDTO);

    void Del(Long[] ids);
}
