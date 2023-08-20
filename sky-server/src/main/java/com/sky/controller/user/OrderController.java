package com.sky.controller.user;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "C端下单相关接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 提交订单接口
     *
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("提交订单接口")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("下单返回的json数据{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO=orderService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }



    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * 查询订单详情
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> ddxq(@PathVariable Long id){
        log.info("{}",id);
        OrderVO orderVO=orderService.ddxg(id);
        return Result.success(orderVO);
    }

    /**
     * /user/order/historyOrders
     * 历史订单
     * @return
     */
    @GetMapping("/historyOrders")
    @ApiOperation("历史订单")
    public Result<PageResult> lsdd(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("历史订单返回的参数{}",ordersPageQueryDTO);
        PageResult pageResult=orderService.ddpage(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    //取消订单
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result qxdd(@PathVariable Long id){
        orderService.qxdd(id);
        return Result.success();
    }


    /**
     * 再来一单
     */
    @PostMapping("/repetition/{id}")
    public Result zlyd(@PathVariable Long id){
        log.info("返回的订单id为:{}",id);
        orderService.zlyd(id);
        return Result.success();
    }
}
