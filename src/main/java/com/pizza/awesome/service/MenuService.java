package com.pizza.awesome.service;

import com.pizza.awesome.model.entity.MenuEntity;
import com.pizza.awesome.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public List<MenuEntity> getMenu(){
        return menuRepository.findAll();
    }

}
