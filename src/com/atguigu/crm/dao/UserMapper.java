package com.atguigu.crm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.atguigu.crm.entity.User;

public interface UserMapper {

	@Select("SELECT u.id, u.name FROM users u")
	List<User> getAll();
	
	User getByName(@Param("name") String name);
	
}
