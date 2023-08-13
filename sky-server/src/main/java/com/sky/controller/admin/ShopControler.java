package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Api(tags = "店铺相关接口")
@RequestMapping("/admin/shop")
public class ShopControler {
    @Autowired
    private RedisTemplate redisTemplate;


    //获取营业状态
    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result start(){
        //获取缓存
        Integer shop = (Integer) redisTemplate.opsForValue().get("shop");
        return Result.success(shop);
    }

    //设置营业状态
    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    public Result state(@PathVariable Integer status){
        log.info("营业状态{}",status==1?"营业中":"歇业中");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("shop",status);
        return Result.success();
    }
}
