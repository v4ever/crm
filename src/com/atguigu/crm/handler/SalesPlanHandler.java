package com.atguigu.crm.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.atguigu.crm.entity.SalesChance;
import com.atguigu.crm.entity.SalesPlan;
import com.atguigu.crm.entity.User;
import com.atguigu.crm.orm.Page;
import com.atguigu.crm.service.SalesChanceService;
import com.atguigu.crm.service.SalesPlanService;

@RequestMapping("/plan")
@Controller
public class SalesPlanHandler {
	
	@Autowired
	private SalesChanceService salesChanceService;
	
	@Autowired
	private SalesPlanService salesPlanService;
	
	@ResponseBody
	@RequestMapping(value="/",method=RequestMethod.POST)
	public Long save(SalesPlan plan){
		salesPlanService.save(plan);
		return plan.getId();
	}
	
	@ResponseBody
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT)
	public String update(@PathVariable("id") Long id, @RequestParam("todo") String todo){
		System.out.println("todo:" + todo + ",id:" + id);
		return "1";
	}
	
	@ResponseBody
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id){
		salesPlanService.delete(id);
		return "1";
	}
	
	@RequestMapping(value="/make/{chanceId}",method=RequestMethod.GET)
	public String make(@PathVariable("chanceId") Long chanceId, Map<String, Object> map){
		map.put("chance", salesChanceService.getWithPlans(chanceId));
		return "plan/make";
	}
	
	@RequestMapping("/chance/list")
	public String listChance(@RequestParam(value="pageNo",required=false,defaultValue="1") Integer pageNo,
			HttpServletRequest request, Map<String, Object> map){
		Page<SalesChance> page = new Page<>();
		page.setPageNo(pageNo);
		
		//1. 获取 search_ 开头的请求参数的 Map
		Map<String, Object> params = WebUtils.getParametersStartingWith(request, "search_");
		
		//{LIKES_contact=c, LIKES_custName=a, LIKES_title=b}
		//加入额外的查询条件:  ①. status != 1  ②. 指派人为当前登陆用户的 "销售机会"
		params.put("NOTEQI_status2", 1);
		User user = (User) request.getSession().getAttribute("user");
		params.put("EQL_designeeId", user.getId());
		
		//2. 把 params 再序列化为一个查询字符串
		String queryString = SalesChanceHandler.encodeParamsToQueryString(params);
		
		//3. 把查询字符串传回到页面
		map.put("queryString", queryString);
		
		page = salesChanceService.getPage(page, params);
		map.put("page", page);
		
		return "plan/chance-list";
	}
}
