package com.atguigu.crm.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("filterChainDefinitionMapBuilder")
public class FilterChainDefinitionMapBuilder {
	
	public Map<String, String> buildFilterChainDefinitionMap(){
		Map<String, String> result = new HashMap<String, String>();
		
		//从数据库中读取受保护的资源信息
		System.out.println("<<buildFilterChainDefinitionMap>>");
		result.put("/chance/**", "roles[chance]");
		
		return result;
	}
	
}
