package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmeaDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmeaDishMapper setmeaDishMapper;


    @Override
    @Transactional
    public void save(SetmealDTO setmealDTO) {
        //封装setmeal里面
        //套餐添加
        Setmeal setmeal= new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.inster(setmeal);
        //获取id和菜品id
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        //循环添加套餐菜品关系
        if (setmealDishes!=null){
            for (SetmealDish setmealDish : setmealDishes) {
                //套餐id
                setmealDish.setSetmealId(setmeal.getId());
                setmeaDishMapper.save(setmealDish);
            }
        }
    }

    //分页
    @Override
    public PageResult getpage(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page=setmeaDishMapper.getpage(setmealPageQueryDTO );
        return new PageResult(page.getTotal(),page.getResult());
    }

    //起售和停售
    @Override
    public void update(int status,Long id) {
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        setmealMapper.update(setmeal);
    }

    //修改套餐
    @Override
    public void allupdate(SetmealDTO setmealDTO) {
        //添加到套餐里面
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
        //全部关联的删除
        Long id = setmealDTO.getId();
        setmeaDishMapper.del(id);
        //判断有没有关联的菜品
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes!=null){
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(id);
                //添加菜品
                setmeaDishMapper.save(setmealDish);
            }
        }
    }

    //根据id查询套餐
    @Override
    public SetmealDTO getbyid(Long id) {
        SetmealDTO setmealDTO=new SetmealDTO();
        Setmeal setmeal=setmealMapper.getbyid(id);
        BeanUtils.copyProperties(setmeal,setmealDTO);
        //查询菜品返回
        List<SetmealDish> setmealDishes=setmeaDishMapper.getsetmealbyid(id);
        setmealDTO.setSetmealDishes(setmealDishes);
        return setmealDTO;
    }

    //删除
    @Override
    @Transactional
    public void del(List<Long> ids) {
        //判断是否起售中
        for (Long id : ids) {
            Setmeal getbyid = setmealMapper.getbyid(id);
            if (getbyid.getStatus()== StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        //删除
        for (Long id : ids) {
            setmealMapper.del(id);
            //如果删除把setmealdish的关联也删除
            setmeaDishMapper.del(id);
        }
    }



//user


    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
