package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("返回查询页的参数")
public class EmployeePageQueryDTO implements Serializable {

    //员工姓名
    @ApiModelProperty(value = "搜索的姓名",required = false)
    private String name;

    //页码
    @ApiModelProperty(value = "页数",required = true)
    private int page;

    //每页显示记录数
    @ApiModelProperty(value = "条数",required = true)
    private int pageSize;

}
