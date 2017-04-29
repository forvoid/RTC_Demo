package com.service;

import com.dao.SetmealDao;
import com.vo.Setmeal;

import java.util.List;

/**
 * Created by forvoid on 4/28/2017.
 */
public class SetmealService {
    private SetmealDao setMealDao;
    public void add(Setmeal setmeal){
        setMealDao.addUser(setmeal);
    }
    public void update(Setmeal setmeal){
        setMealDao.update(setmeal);
    }
    public List findAll(){
        return setMealDao.findByAll();
    }
    public void delete(){

    }

    public void setSetmealDao(SetmealDao setMealDao) {
        this.setMealDao = setMealDao;
    }
}
