package com.atguigu.crm.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import com.atguigu.crm.entity.Dict;
import com.atguigu.crm.service.jpa.DictService;

@RequestMapping("/dict")
@Controller
public class DictHandler {

	@Autowired
	private DictService dictService;
	
	@RequestMapping("/list")
	public String list(@RequestParam(value="pageNo", required=false, defaultValue="1") String pageNoStr,
			Map<String, Object> map, HttpServletRequest request){
		int pageNo = 1;
		try {
			pageNo = Integer.parseInt(pageNoStr);
		} catch (NumberFormatException e) {}
		
		//1. 获取 search_ 开头的请求参数的 Map
		Map<String, Object> params = WebUtils.getParametersStartingWith(request, "search_");
		
		//2. 把 params 再序列化为一个查询字符串
		String queryString = SalesChanceHandler.encodeParamsToQueryString(params);
		
		//3. 把查询字符串传回到页面
		map.put("queryString", queryString);
		
		Page<Dict> page = dictService.getPage(pageNo - 1, 5, params);
		map.put("page", page);
		return "dict/list";
	}
}
