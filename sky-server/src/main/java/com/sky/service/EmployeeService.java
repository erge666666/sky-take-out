package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    public void save(EmployeeDTO employeeDTO);

    PageResult page(EmployeePageQueryDTO pageQueryDTO);

    void statrorstop(Integer status, Long id);

    //id查找
    Employee select(Long id);


    void update(EmployeeDTO employeeDTO);
}
