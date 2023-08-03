package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = DigestUtils.md5DigestAsHex(employeeLoginDTO.getPassword().getBytes());

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // 已加密
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     * @return
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        //向数据库提交数据
        Employee employee=new Employee();
        //前端返回的属性添加到employee数据中
        BeanUtils.copyProperties(employeeDTO,employee);
        //配置启用禁用
        employee.setStatus(StatusConstant.ENABLE);
        //配置密码加密md5
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        //设置时间
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateTime(LocalDateTime.now());
        //Todo TODO创建人和修改人
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());
        //数据库
        employeeMapper.save(employee);
    }

    //分页
    @Override
    public PageResult page(EmployeePageQueryDTO pageQueryDTO) {
        //select * from emp limit 0,10
        //开始分页查询
        PageHelper.startPage(pageQueryDTO.getPage(),pageQueryDTO.getPageSize());
        Page<Employee> page=employeeMapper.page(pageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //启用禁用
    @Override
    public void statrorstop(Integer status, Long id) {
        /*Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);*/
        //使用builder
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        //直接调用mapper
        employeeMapper.update(employee);
    }

    //id查找
    @Override
    public Employee select(Long id) {
        Employee employee = employeeMapper.select(id);
        //把密码给隐藏了
        employee.setPassword("****");
        return employee;
    }

    //修改员工
    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();
        //拷贝过来
        BeanUtils.copyProperties(employeeDTO,employee);
        //设置修改时间和修改人
        employee.setUpdateUser(BaseContext.getCurrentId());
        employee.setUpdateTime(LocalDateTime.now());

        employeeMapper.update(employee);
    }
}
