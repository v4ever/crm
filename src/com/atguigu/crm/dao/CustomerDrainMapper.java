package com.atguigu.crm.dao;

import org.apache.ibatis.annotations.Update;

public interface CustomerDrainMapper {
	
	@Update("{call check_drain}")
	void callCheckDrainProcedure();
	
}
