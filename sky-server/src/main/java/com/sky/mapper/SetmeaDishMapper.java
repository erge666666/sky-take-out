package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.SetmealDish;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmeaDishMapper {

    //用dishid查
    List<Long> getbyid(Long[] ids);

    void save(SetmealDish setmealDish);

    Page<SetmealVO> getpage(SetmealPageQueryDTO setmealPageQueryDTO);

    @Delete("delete from setmeal_dish where setmeal_id=#{id}")
    void del(Long id);


    @Select("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> getsetmealbyid(Long id);
}
