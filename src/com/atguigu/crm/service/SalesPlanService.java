package com.atguigu.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.crm.dao.SalesPlanMapper;
import com.atguigu.crm.entity.SalesPlan;

@Service
public class SalesPlanService {

	@Autowired
	private SalesPlanMapper salesPlanMapper;
	
	@Transactional
	public void save(SalesPlan plan){
		salesPlanMapper.save(plan);
	}
	
	@Transactional
	public void delete(Long id){
		salesPlanMapper.delete(id);
	}
}
