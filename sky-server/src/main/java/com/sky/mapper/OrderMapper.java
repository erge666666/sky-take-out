package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param orders
     */
    void inster(Orders orders);


    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 根据订单id查询订单
     * * 取消订单
     * @param id
     * @return
     */
    @Select("select * from orders where id=#{id}")
    OrderVO getById(Long id);

    /**
     * 根据userid查历史订单
     * @param ordersPageQueryDTO
     * @return
     *
     */
    Page<Orders> getUserId(OrdersPageQueryDTO ordersPageQueryDTO);

    //根据status查询有多少条数据
    @Select("select count(status) from orders where status=#{status}")
    Integer countStatus(Integer status);

    @Select("select * from orders where status=#{status} and order_time<#{chaoshitime}")
    List<Orders> getstatusandtime(Integer status, LocalDateTime chaoshitime);


    //获取营业额
    @Select("select sum(amount) from orders where order_time>#{begin} and order_time<#{end} and status=#{status}")
    Double sumamount(Map map);

    Integer getddcount(Map map);

    //统计指定时间的前10
    List<GoodsSalesDTO> getTop10(LocalDateTime begin,LocalDateTime end);
}
