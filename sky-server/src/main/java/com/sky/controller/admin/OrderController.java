package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "管理端订单相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;



    /**
     * 搜索订单
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("搜索订单")
    public Result<PageResult> page(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("搜索订单的参数{}",ordersPageQueryDTO);
        PageResult pageResult=orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 统计各个订单数量
     */
    @GetMapping("/statistics")
    @ApiOperation("统计各个订单数量")
    public Result<OrderStatisticsVO> count(){
        OrderStatisticsVO orderStatisticsVO=orderService.countdd();
        return Result.success(orderStatisticsVO);
    }


    /**
     * 查询订单详情
     */
    @GetMapping("/details/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> select(@PathVariable Long id){
        log.info("查寻订单id为:{}",id);
        OrderVO orderVO= orderService.ddxg(id);
        return Result.success(orderVO);
    }

    /**
     * 接单
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result jd(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        log.info("接单的参数id为{}",ordersConfirmDTO);
        orderService.jiedan(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 拒单
     */
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result judan(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        log.info("拒单参数{}",ordersRejectionDTO);
        orderService.judan(ordersRejectionDTO);
        return Result.success();
    }


    /**
     * 取消订单
     */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result qxdd(@RequestBody OrdersCancelDTO ordersCancelDTO){
        log.info("取消订单参数：{}",ordersCancelDTO);
        orderService.adminqxdd(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 派送订单
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result psdd(@PathVariable Long id){
        log.info("派送订单参数{}",id);
        orderService.psdd(id);
        return Result.success();
    }


    /**
     * 完成订单
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result wcdd(@PathVariable Long id){
        log.info("派送订单参数{}",id);
        orderService.wcdd(id);
        return Result.success();
    }


}
