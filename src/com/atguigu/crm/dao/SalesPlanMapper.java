package com.atguigu.crm.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;

import com.atguigu.crm.entity.SalesPlan;

public interface SalesPlanMapper {
 
	@SelectKey(before=true, keyColumn="id", keyProperty="id", resultType=Long.class, 
			statement="SELECT crm_seq.nextval FROM dual")
	@Insert("INSERT INTO sales_plan(id, todo, plan_date, chance_id) "
			+ "VALUES(#{id}, #{todo}, #{date}, #{chance.id})")
	void save(SalesPlan plan);
	
	@Delete("DELETE FROM sales_plan WHERE id = #{id}")
	void delete(@Param("id") Long id);

}
