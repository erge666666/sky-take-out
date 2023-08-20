package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量添加数据
     * @param list
     */
    void insterBatch(List<OrderDetail> list);

    @Select("select * from order_detail where order_id=#{id}")
    List<OrderDetail> getOrderId(Long id);
}
