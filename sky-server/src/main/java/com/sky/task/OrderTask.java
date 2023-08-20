package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    //设置两个常量
    private static final LocalDateTime chaoshitime=LocalDateTime.now().plusMinutes(-15);
    private static final LocalDateTime zdwc=LocalDateTime.now().plusHours(-1);


    @Autowired
    private OrderMapper orderMapper;

    /**
     * 订单超过15分钟
     */
    @Scheduled(cron = "0 * * * * ?")
    //@Scheduled(cron = "0/20 * * * * ? ")
    public void chaoshi(){
        List<Orders> list=orderMapper.getstatusandtime(Orders.PENDING_PAYMENT,chaoshitime);

        if (list!=null && list.size()>0){

            list.forEach(l ->{
                l.setStatus(Orders.CANCELLED);
                l.setCancelReason("订单超时");
                l.setCancelTime(LocalDateTime.now());
                orderMapper.update(l);
            });
        }

    }


    @Scheduled(cron = "0 0 1 * * ?")
    //@Scheduled(cron = "0/10 * * * * ? ")
    public void zdwc(){
        List<Orders> list=orderMapper.getstatusandtime(Orders.DELIVERY_IN_PROGRESS,zdwc);

        if (list!=null && list.size()>0){

            list.forEach(l ->{
                l.setStatus(Orders.COMPLETED);
                orderMapper.update(l);
            });
        }
    }
}
