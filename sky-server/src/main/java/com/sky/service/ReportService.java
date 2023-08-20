package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO yyesum(LocalDate begin, LocalDate end);

    //用户统计
    UserReportVO usercount(LocalDate begin, LocalDate end);

    OrderReportVO ddcount(LocalDate begin, LocalDate end);

    SalesTop10ReportVO top10(LocalDate begin, LocalDate end);

    /**
     * 导出数据
     * @param response
     */
    void export(HttpServletResponse response);
}
