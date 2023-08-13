package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

//菜品管理
@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品接口")
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("新增菜品方法")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("返回的菜品和口味{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }


    @GetMapping("/page")
    @ApiOperation("分页操作")
    public Result<PageResult> dishPage(DishPageQueryDTO dishPageQueryDTO){
        log.info("返回的所有参数{}",dishPageQueryDTO.toString());
        PageResult pageResult=dishService.dishPage(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    //删除菜品
    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result delect(@RequestParam Long[] ids){
        log.info("删除的菜品id{}", Arrays.toString(ids));
        //给service层操作
        dishService.Del(ids);
        return Result.success();
    }

    //根据id查询菜品
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品接口")
    public Result<DishVO> select(@PathVariable Long id){
        log.info("根据id查询的菜品{}",id);
        DishVO dishVO=dishService.getByidwithFlavor(id);
        return Result.success(dishVO);
    }

    //修改菜品
    @PutMapping
    @ApiOperation("修改菜品接口")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品返回的数据{}",dishDTO);
        dishService.dishupdate(dishDTO);
        return Result.success();
    }

    //根据分类查询菜品
    @GetMapping("/list")
    @ApiOperation("根据分类查询菜品")
    public Result<List<Dish>> getcategoryid(@RequestParam Long categoryId){
        log.info("{}",categoryId);
        List<Dish> list=dishService.getcategoryid(categoryId);
        return Result.success(list);
    }
}
