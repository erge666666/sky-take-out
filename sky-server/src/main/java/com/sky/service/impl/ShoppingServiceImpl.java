package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingMapper;
import com.sky.service.ShoppingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingServiceImpl implements ShoppingService {

    @Autowired
    private ShoppingMapper shoppingMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;


    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //查寻购物车是否已添加
        ShoppingCart shoppingCart=new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId = BaseContext.getCurrentId();

        shoppingCart.setUserId(userId);
        List<ShoppingCart> list=shoppingMapper.select(shoppingCart);

        //如果存在,数量添加1
        if (list!=null && list.size()>0){
            ShoppingCart cart = list.get(0);
            Integer number = cart.getNumber();
            cart.setNumber(number+1);
            shoppingMapper.updateshoping(cart);
        }else {
            //如果不存在，看看是套餐还是菜品
            Long setmealId = shoppingCartDTO.getSetmealId();
            if (setmealId!=null){
                //证明是套餐,id查询套餐
                Setmeal setmeal = setmealMapper.getbyid(setmealId);
                //查询到了再去添加
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());


            }else {
                Long dishId = shoppingCartDTO.getDishId();
                //菜品
                Dish dish=dishMapper.getByid(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }
            //重复写最下面处理
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);
            shoppingMapper.inster(shoppingCart);
        }
    }

    /**
     * 查看购物车
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart s = ShoppingCart.builder()
                .userId(userId)
                .build();
        List<ShoppingCart> list = shoppingMapper.select(s);
        return list;
    }

    /**
     * 清空购物车
     */
    @Override
    public void dels() {
        Long userId = BaseContext.getCurrentId();
        shoppingMapper.dels(userId);
    }
}
