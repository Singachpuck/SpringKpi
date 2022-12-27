package com.kpi.tendersystem.service;

import com.kpi.tendersystem.dao.UserDao;
import com.kpi.tendersystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public Optional<User> getByUsername(final String username){
        return userDao.getByUsername(username);
    }

    public Optional<User> getById(final int userId) {
        return userDao.getById(userId);
    }
}
