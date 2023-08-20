package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);


    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    OrderVO ddxg(Long id);

    /**
     * 订单查询
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult ddpage(OrdersPageQueryDTO ordersPageQueryDTO);

    void qxdd(Long id);

    void zlyd(Long id);

    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 统计各个订单数量
     * @return
     */
    OrderStatisticsVO countdd();

    /**
     * 接单
     * @param id
     */
    void jiedan(OrdersConfirmDTO ordersConfirmDTO);

    void judan(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 取消订单
     * @param ordersCancelDTO
     */
    void adminqxdd(OrdersCancelDTO ordersCancelDTO);

    /**
     * 派送订单
     */
    void psdd(Long id);

    /**
     * 完成订单
     * @param id
     */
    void wcdd(Long id);
}
