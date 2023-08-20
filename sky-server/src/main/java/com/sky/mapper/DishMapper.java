package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void save(Dish dish);
    /**
     * 分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> dishPage(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 删除菜品
     * @param id
     */
    @Delete("delete from dish where id=#{id}")
    void del(Long id);

    /**
     * 根据主键查找菜品
     * @param id
     * @return
     */
    @Select("select * from dish where id=#{id}")
    Dish getByid(Long id);

    //修改
    @AutoFill(OperationType.UPDATE)
    void dishupdate(Dish dish);

    @Select("select * from dish where category_id=#{categoryId}")
    List<Dish> getcategoryid(Long categoryId);

    @Select("select * from dish where category_id=#{categoryId} and status=#{status}")
    List<Dish> list(Dish dish);


    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
