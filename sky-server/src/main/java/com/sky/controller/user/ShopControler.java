package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("UserShopController")
@Slf4j
@Api(tags = "用户店铺相关接口")
@RequestMapping("/user/shop")
public class ShopControler {
    @Autowired
    private RedisTemplate redisTemplate;

    //获取营业状态
    @GetMapping("/status")
    @ApiOperation("用户端获取营业状态")
    public Result start(){
        //获取缓存
        Integer shop = (Integer) redisTemplate.opsForValue().get("shop");
        return Result.success(shop);
    }
}
