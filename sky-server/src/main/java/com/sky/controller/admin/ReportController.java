package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@RestController("Recontroller")
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "数据统计相关文档")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 营业额统计接口
     */
    @ApiOperation("营业额统计接口")
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> yyesum(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                           @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("开始时间：{}，结束时间：{}",begin,end);
        TurnoverReportVO turnoverReportVO=reportService.yyesum(begin,end);
        return Result.success(turnoverReportVO);
    }


    /**
     * 用户统计
     * @return
     */
    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO> usercount(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                          @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("开始时间：{}，结束时间：{}",begin,end);
        UserReportVO userReportVO=reportService.usercount(begin,end);
        return Result.success(userReportVO);
    }

    /**
     * 订单统计
     */
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计")
    public Result<OrderReportVO> ddcount(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                         @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("开始时间：{}，结束时间：{}",begin,end);

        return Result.success(reportService.ddcount(begin,end));
    }


    /**
     *
     *数据统计相关接口
     */

    @GetMapping("/top10")
    @ApiOperation("数据统计相关接口")
    public Result<SalesTop10ReportVO> top10(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end)
    {
        log.info("开始时间：{}，结束时间：{}",begin,end);

        return Result.success(reportService.top10(begin,end));

    }


    /**
     * 导出数据
     */
    @GetMapping("/export")
    @ApiOperation("导出数据")
    public void export(HttpServletResponse response){
        reportService.export(response);
    }
}
