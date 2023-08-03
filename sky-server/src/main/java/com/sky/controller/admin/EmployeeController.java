package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "管理端api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        System.out.println("登录id是:"+Thread.currentThread().getId());
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<String> logout() {
        return Result.success();
    }


    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增员工")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "employeeDTO",value = "前端返回的员工参数",required = true)
    )
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("前端提交的参数{}",employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }


    /**
     * 分页查询
     * @param pageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO pageQueryDTO){
        log.info("返回的参数{}",pageQueryDTO);
        PageResult pageResult=employeeService.page(pageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 员工的禁用和启用
     * @param status
     * @param id
     * @return
     */
    @PostMapping ("/status/{status}")
    @ApiOperation("员工的禁用和启用")
    public Result startOrstop(@PathVariable Integer status,@RequestParam Long id){
        log.info("返回的状态为{},返回的id为{}",status,id);
        employeeService.statrorstop(status,id);
        return Result.success();
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("id查询员工")
    public Result<Employee> select(@PathVariable Long id){
        log.info("参数{}",id);
        Employee employee=employeeService.select(id);
        return Result.success(employee);
    }

    /**
     * 修改员工
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改员工")
    public Result updateemp(@RequestBody EmployeeDTO employeeDTO){
        log.info("返回的json{}",employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
