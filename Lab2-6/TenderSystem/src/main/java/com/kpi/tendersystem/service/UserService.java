package com.kpi.tendersystem.service;

import com.kpi.tendersystem.dao.UserDao;
import com.kpi.tendersystem.model.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getByUsername(final String username){
        return userDao.getByUsername(username);
    }
}
