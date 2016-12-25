package com.atguigu.crm.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import com.atguigu.crm.entity.SalesChance;
import com.atguigu.crm.orm.Page;
import com.atguigu.crm.service.SalesChanceService;
import com.atguigu.crm.service.UserService;

@RequestMapping("/chance")
@Controller
public class SalesChanceHandler {

	@Autowired
	private SalesChanceService salesChanceService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/dispatch/{id}",method=RequestMethod.PUT)
	public String dispatch(SalesChance chance){
		salesChanceService.dispatch(chance);
		return "redirect:/chance/list";
	}
	
	@RequestMapping(value="/dispatch/{id}",method=RequestMethod.GET)
	public String dispatch(@PathVariable("id") Long id,Map<String, Object> map){
		map.put("chance", salesChanceService.get(id));
		map.put("users", userService.getAll());
		return "chance/dispatch";
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public String update(SalesChance salesChance){
		salesChanceService.update(salesChance);
		return "redirect:/chance/list";
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public String edit(@PathVariable("id") String idStr, Map<String, Object> map){
		Long id = -1L;
		try {
			id = Long.parseLong(idStr);
			SalesChance chance = salesChanceService.get(id);
			if(chance != null){
				map.put("chance", chance);
				return "chance/input";
			}
		} catch (NumberFormatException e) {}
		
		return "home/error";
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public String delete(@PathVariable("id") String idStr){
		Long id = -1L;
		try {
			id = Long.parseLong(idStr);
			salesChanceService.delete(id);
		} catch (NumberFormatException e) {}
		
		return "redirect:/chance/list";
	}
	
	@RequestMapping(value="/",method=RequestMethod.POST)
	public String create(SalesChance chance){
		salesChanceService.save(chance);
		return "redirect:/chance/list";
	}
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	public String create(Map<String, Object> map){
		map.put("chance", new SalesChance());
		return "chance/input";
	}
	
	@RequestMapping("/list")
	public String list(@RequestParam(value="pageNo", required=false, defaultValue="1") String pageNoStr,
			Map<String, Object> map, HttpServletRequest request){
		Page<SalesChance> page = new Page<>();
		
		int pageNo = 1;
		try {
			pageNo = Integer.parseInt(pageNoStr);
		} catch (NumberFormatException e) {}
		page.setPageNo(pageNo);
		
		//1. 获取 search_ 开头的请求参数的 Map
		Map<String, Object> params = WebUtils.getParametersStartingWith(request, "search_");
		System.out.println("params: " + params);
		
		//2. 把 params 再序列化为一个查询字符串
		String queryString = encodeParamsToQueryString(params);
		
		//3. 把查询字符串传回到页面
		map.put("queryString", queryString);
		
		page = salesChanceService.getPage(page, params);
		map.put("page", page);
		return "chance/list";
	}
	
	public static String encodeParamsToQueryString(Map<String, Object> params) {
		StringBuilder result = new StringBuilder();
		
		for(Map.Entry<String, Object> param: params.entrySet()){
			String key = param.getKey();
			Object val = param.getValue();
			
			if("".equals(val)){
				continue;
			}
			
			result.append("search_").append(key).append("=").append(val).append("&");
		}
		
		if(result.length() > 0){
			result.replace(result.length() - 1, result.length(), "");
		}
		return result.toString();
	}
}
