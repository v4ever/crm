package com.atguigu.crm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.crm.dao.SalesChanceMapper;
import com.atguigu.crm.entity.SalesChance;
import com.atguigu.crm.orm.Page;
import com.atguigu.crm.orm.PropertyFilter;
import com.atguigu.crm.orm.PropertyFilter.MatchType;
import com.atguigu.crm.util.ReflectionUtils;

@Service
public class SalesChanceService {

	@Autowired
	private SalesChanceMapper salesChanceMapper;
	
	@Transactional(readOnly=true)
	public SalesChance getWithPlans(Long id){
		return salesChanceMapper.getWithPlans(id);
	}
	
	@Transactional
	public void dispatch(SalesChance chance){
		chance.setStatus(2);
		salesChanceMapper.dispatch(chance);
	}
	
	@Transactional(readOnly=true)
	public Page<SalesChance> getPage(Page<SalesChance> page,
			Map<String, Object> params) {
		//1. 把 params 转为 PropertyFilter 的集合
		List<PropertyFilter> filters = PropertyFilter.parseParamsToPropertyFilters(params);
		
		//2. 再把 PropertyFilter 的集合转为 mybatis 可以用的 Map.
		Map<String, Object> mybatisParams = parsePropertyFiltersToMybatisParams(filters);
		
		//3. 调用 mybatis 的方法. 
		//3.1 获取总的记录数
		long totalElements = salesChanceMapper.getTotalElements2(mybatisParams);
		page.setTotalElements(totalElements);
		
		//3.2 获取 Content
		int firstIndex = (page.getPageNo() - 1) * page.getPageSize() + 1;
		int endIndex = firstIndex  + page.getPageSize();
		mybatisParams.put("firstIndex", firstIndex);
		mybatisParams.put("endIndex", endIndex);
		List<SalesChance> content = salesChanceMapper.getContent2(mybatisParams);
		page.setContent(content);
		
		return page;
	}
	
	private Map<String, Object> parsePropertyFiltersToMybatisParams(
			List<PropertyFilter> filters) {
		//把 PropertyFilter 的 List 转为一个 myBatis 可用的 Map
		Map<String, Object> params = new HashMap<String, Object>();
		
		for(PropertyFilter filter: filters){
			String propertyName = filter.getPropertyName();
			Object propertyVal = filter.getPropertyVal();
			
			if(propertyVal == null || propertyVal.toString().trim().equals("")){
				continue;
			}
			
			//1. 把页面传过来的字符串转为实际的目标类型. 
			Class propertyType = filter.getPropertyType();
			propertyVal = ReflectionUtils.convertValue(propertyVal, propertyType);
			
			
			//2. 若比较方式为LIKE, 则再 value 前后添加 %.
			MatchType matchType = filter.getMatchType();
			if(matchType.equals(MatchType.LIKE)){
				propertyVal = "%" + propertyVal + "%";
			}
			
			params.put(propertyName, propertyVal);
		}
		
		return params;
	}

	@Transactional
	public void update(SalesChance salesChance){
		salesChanceMapper.update(salesChance);
	}
	
	@Transactional(readOnly=true)
	public SalesChance get(Long id){
		return salesChanceMapper.get(id);
	}
	
	@Transactional
	public void delete(Long id){
		salesChanceMapper.delete(id);
	}
	
	@Transactional
	public void save(SalesChance chance){
		chance.setCreateDate(new Date());
		salesChanceMapper.save(chance);
	}
	
	@Transactional(readOnly=true)
	public Page<SalesChance> getPage(Page<SalesChance> page){
		long totalElements = salesChanceMapper.getTotalElements();
		page.setTotalElements(totalElements);
		
		int fromIndex = (page.getPageNo() - 1) * page.getPageSize() + 1;
		int endIndex = fromIndex  + page.getPageSize();
		List<SalesChance> content = salesChanceMapper.getContent(fromIndex, endIndex);
		page.setContent(content);
		
		return page;
	}
}
