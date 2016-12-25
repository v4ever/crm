package com.atguigu.crm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.atguigu.crm.entity.SalesChance;

public interface SalesChanceMapper {

	SalesChance getWithPlans(@Param("id") Long id);
	
	@Update("UPDATE sales_chances SET status = #{status}, designee_id = #{designee.id}, designee_date = #{designeeDate} "
			+ "WHERE id = #{id}")
	void dispatch(SalesChance chance);
	
	@Update("UPDATE sales_chances SET title = #{title}, contact = #{contact}, contact_tel = #{contactTel}, cust_name = #{custName}, description = #{description}, rate = #{rate}, source = #{source} "
			+ "WHERE id = #{id}")
	void update(SalesChance chance);
	
	@Select("SELECT s.id, title, contact, contact_tel, cust_name, create_date, s.description, rate, source, status, u.name as \"createBy.name\", r.name as \"createBy.role.name\" "
			+ "FROM sales_chances s "
			+ "LEFT OUTER JOIN users u "
			+ "ON s.created_user_id = u.id "
			+ "LEFT OUTER JOIN roles r "
			+ "ON u.role_id = r.id "
			+ "WHERE s.id = #{id}")
	SalesChance get(@Param("id") Long id);
	
	@Delete("DELETE FROM sales_chances WHERE id = #{id}")
	void delete(@Param("id") Long id);
	
	@Insert("INSERT INTO sales_chances(id, title, contact, contact_tel, cust_name, create_date, description, rate, source, status, created_user_id) "
			+ "VALUES(crm_seq.nextval,#{title},#{contact},#{contactTel},#{custName},#{createDate}, #{description}, #{rate}, #{source}, 1, #{createBy.id})")
	void save(SalesChance chance);
	
	long getTotalElements2(Map<String, Object> params);
	List<SalesChance> getContent2(Map<String, Object> params);
	
	@Select("SELECT count(id) FROM sales_chances")
	long getTotalElements(); //获取总的记录数
	
	@Select("SELECT * "
		  + "FROM (SELECT rownum rn, id, contact, contact_tel, create_date, title, cust_name "
		  + "      FROM sales_chances ) t "
		  + "WHERE t.rn >= #{firstIndex} AND rn < #{endIndex}")
	List<SalesChance> getContent(@Param("firstIndex") int fromIndex, @Param("endIndex") int endIndex);  //获取当前页面的 Content
	
}
