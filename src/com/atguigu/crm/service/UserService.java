package com.atguigu.crm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.crm.dao.UserMapper;
import com.atguigu.crm.entity.User;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
	
	@Transactional(readOnly=true)
	public User getByName(String name){
		return userMapper.getByName(name);
	}
	
	@Transactional(readOnly=true)
	public List<User> getAll(){
		return userMapper.getAll();
	}
	
	@Transactional(readOnly=true)
	public User login(String name, String password){
		User user = userMapper.getByName(name);
		
		if(user != null
				&& user.getPassword().equals(password)
				&& user.getEnabled() == 1){
			return user;
		}
		
		return null;
	}
}
