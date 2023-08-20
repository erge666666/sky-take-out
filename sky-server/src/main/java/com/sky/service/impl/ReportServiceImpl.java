package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO yyesum(LocalDate begin, LocalDate end) {
        //返回日期列表（集合）
        List<LocalDate> dateList=new ArrayList<>();
        List<Double> turnoverList=new ArrayList<>();
        dateList.add(begin);

        while (!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

        //返回营业额列表（集合）
        //遍历每天时间
        dateList.forEach(d ->{
            LocalDateTime beginTime=LocalDateTime.of(d, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(d, LocalTime.MAX);
            //传入每天最大时间和最小时间
            //查询数据库 select sum(qian) from order where orde_time>当天最小时间 and order_time<当天最大时间
            Map map=new HashMap<>();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);
            Double amount=orderMapper.sumamount(map);
            //如果当天没有营业额代表为0.0元
            amount=amount==null?0.0:amount;
            //添加到营业额列表
            turnoverList.add(amount);
        });

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO usercount(LocalDate begin, LocalDate end) {


        List<LocalDate> dateList=new ArrayList<>();
        List<Integer> newuserlist=new ArrayList<>();
        List<Integer> userlist=new ArrayList<>();

        //日期集合
        dateList.add(begin);
        while (!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

        dateList.forEach(d ->
        {
            LocalDateTime beginTime=LocalDateTime.of(d,LocalTime.MIN);
            LocalDateTime endTime=LocalDateTime.of(d,LocalTime.MAX);
            Map map=new HashMap<>();
            map.put("end",endTime);
            //总量集合  select count(id) from user where user_time<最大时间
            Integer user=userMapper.usercount(map);

            map.put("begin",beginTime);
            //新增人数集合 select count(id) from user where user_time>最小时间 and user_time<最大时间
            Integer newuser=userMapper.usercount(map);


            userlist.add(user);
            newuserlist.add(newuser);
        });

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .totalUserList(StringUtils.join(userlist,","))
                .newUserList(StringUtils.join(newuserlist,","))
                .build();
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO ddcount(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList=new ArrayList<>();
        //日期集合
        dateList.add(begin);
        while (!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

        //订单数列表
        List<Integer> orderCountList=new ArrayList<>();
        //有效订单数列表
        List<Integer> validOrderCountList=new ArrayList<>();
        //循环获取出有效订单
        for (LocalDate date : dateList) {
            LocalDateTime beginTime=LocalDateTime.of(date,LocalTime.MIN);
            LocalDateTime endTime=LocalDateTime.of(date,LocalTime.MAX);
            //求订单数列表
            Integer ddcount = getddcount(beginTime, endTime, null);
            //求有效订单数列表
            Integer statuscount = getddcount(beginTime, endTime, Orders.COMPLETED);

            orderCountList.add(ddcount);
            validOrderCountList.add(statuscount);
        }

        //订单总数，这几天订单加一起
        Integer sum1 = orderCountList.stream().reduce(Integer::sum).get();
        //有效订单数
        Integer sum2 = validOrderCountList.stream().reduce(Integer::sum).get();
        //完成率
        Double orderCompletionRate= 0.0;
        if(sum2!=0){
            orderCompletionRate= Double.valueOf(sum2)/sum1;
        }

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCountList(StringUtils.join(orderCountList,","))
                .validOrderCountList(StringUtils.join(validOrderCountList,","))
                .totalOrderCount(sum1)
                .validOrderCount(sum2)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }


    //方法
    private Integer getddcount(LocalDateTime begin,LocalDateTime end,Integer status){
        Map map=new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);
        map.put("status",status);

        return orderMapper.getddcount(map);
    }


    /**
     * 数据统计相关接口
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {

        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> top10 = orderMapper.getTop10(beginTime, endTime);
        List<String> names = top10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        String namelist = StringUtils.join(names, ",");

        List<Integer> numbers = top10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numberlist = StringUtils.join(numbers, ",");


        return SalesTop10ReportVO.builder()
                .nameList(namelist)
                .numberList(numberlist)
                .build();
    }
}
