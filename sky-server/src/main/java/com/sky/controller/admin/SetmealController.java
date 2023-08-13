package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @PostMapping
    @ApiOperation("新增套餐接口")
    @CacheEvict(cacheNames = "setmealCache",key = "#setmealDTO.categoryId")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐参数{}",setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }

    //分页查询
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> setmealpage(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("返回的分页参数{}",setmealPageQueryDTO);
        PageResult pageResult=setmealService.getpage(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    //起售停售

    @PostMapping("/status/{status}")
    @ApiOperation("起售停售")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result status(@PathVariable int status,Long id){
        log.info("起售停售：{},{}",status==1?"起售中":"停售中",id);
        setmealService.update(status,id);
        return Result.success();
    }


    //修改套餐
    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result Update(@RequestBody SetmealDTO setmealDTO){
        log.info("参数{}",setmealDTO);
        setmealService.allupdate(setmealDTO);
        return Result.success();
    }

    //根据id查询套餐
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealDTO> getbyid(@PathVariable Long id){
        SetmealDTO setmealDTO=setmealService.getbyid(id);
        return Result.success(setmealDTO);
    }

    //批量删除套餐
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result del(@RequestParam List<Long> ids){
        setmealService.del(ids);
        return Result.success();
    }
}
