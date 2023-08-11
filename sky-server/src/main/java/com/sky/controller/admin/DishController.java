package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
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

}
