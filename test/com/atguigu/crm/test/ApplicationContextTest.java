package com.atguigu.crm.test;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;

import com.atguigu.crm.entity.Authority;
import com.atguigu.crm.entity.Customer;
import com.atguigu.crm.entity.Dict;
import com.atguigu.crm.entity.SalesChance;
import com.atguigu.crm.entity.SalesPlan;
import com.atguigu.crm.entity.User;
import com.atguigu.crm.repository.ReportRepository;
import com.atguigu.crm.service.SalesChanceService;
import com.atguigu.crm.service.SalesPlanService;
import com.atguigu.crm.service.UserService;
import com.atguigu.crm.service.jpa.CustomerService;
import com.atguigu.crm.service.jpa.DictService;

public class ApplicationContextTest {

	private ApplicationContext ctx = null;
	private UserService userService;
	private SalesChanceService salesChanceService;
	private SalesPlanService salesPlanService;
	private EntityManagerFactory entityManagerFactory;
	private DictService dictService;
	private CustomerService customerService;
	
	private ReportRepository reportRepository;
	
	{
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		userService = ctx.getBean(UserService.class);
		salesChanceService = ctx.getBean(SalesChanceService.class);
		salesPlanService = ctx.getBean(SalesPlanService.class);
		dictService = ctx.getBean(DictService.class);
		customerService = ctx.getBean(CustomerService.class);
		
		reportRepository = ctx.getBean(ReportRepository.class);
	}
	
	@Test
	public void testUserMap(){
		User user = userService.getByName("abcd");
		System.out.println(user.getName());
		System.out.println(user.getRole().getName());
		
		for(Authority authority: user.getRole().getAuthorities()){
			System.out.println("-->" + authority.getName());
		}
	}
	
	@Test
	public void testReportRepository(){
		int pageNo = 0;
		int pageSize = 10;
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("name", "");
//		params.put("minOrderDate", "2016-1-1");
		
//		Page<Customer> page = reportRepository.getCustomerPage(pageNo, pageSize, params);
		
		String type = "level"; //level, credit, satify
		Page<Object[]> page = reportRepository.getConsistPage(pageNo, pageSize, type);
				
		System.out.println(page.getTotalElements());
		System.out.println(page.getTotalPages());
		
		List<Object[]> results = page.getContent();
		for(Object[] objs: results){
			System.out.println(objs[0] + ": " + objs[1]);
		}
	}
	
	@Test
	public void testCollectionProperty(){
		int pageNo = 0;
		int pageSize = 10;
		
		Map<String, Object> params = new HashMap<String, Object>();
		//添加直接属性的条件. OK 
		params.put("LIKES_name", "阿");
		//添加 n-1 关联关系属性的条件
		params.put("LIKES_manager.name", "a");
		//添加 1-n 关联关系集合元素中的属性. NO!!!!!
		params.put("LIKES_orders.address", "a");
		
		Page<Customer> page = customerService.getPage(pageNo, pageSize, params);
		
		System.out.println(page.getTotalElements());
		System.out.println(page.getTotalPages());
		System.out.println(page.getContent());
	}
	
	@Test
	public void testSpringData(){
		Dict dict = new Dict();
		dict.setEditable(false);
		dict.setItem("平西府");
		dict.setType("地区");
		
		dictService.save(dict);
	}
	
	@Test
	public void testEntityManagerFactory(){
		entityManagerFactory = ctx.getBean(EntityManagerFactory.class);
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		System.out.println(entityManager.getClass());
	}
	
	@Test
	public void testSelectKey(){
		SalesPlan plan = new SalesPlan();
		
		SalesChance chance = new SalesChance();
		chance.setId(20020L);
		plan.setChance(chance);
		plan.setDate(new Date());
		plan.setTodo("ABC");
		
		System.out.println(plan.getId());
		salesPlanService.save(plan);
		System.out.println(plan.getId());
	}
	
	@Test
	public void testGetWithPlans(){
		SalesChance salesChance = salesChanceService.getWithPlans(20020L);
		
		System.out.println(salesChance.getCustName());
		System.out.println(salesChance.getCreateBy().getName());
		System.out.println(salesChance.getDesignee().getName());
		
		System.out.println(salesChance.getSalesPlans());
	}
	
	@Test
	public void testGet(){
		SalesChance salesChance = salesChanceService.get(156L);
		
		System.out.println(salesChance.getTitle());
		System.out.println(salesChance.getCreateBy().getName());
		System.out.println(salesChance.getCreateBy().getRole().getName());
	}
	
	@Test
	public void testGetPage(){
		com.atguigu.crm.orm.Page<SalesChance> page = new com.atguigu.crm.orm.Page<>();
		page.setPageNo(1);
		page.setPageSize(3);
		
		page = salesChanceService.getPage(page);
		
		System.out.println(page.getTotalElements());
		System.out.println(page.getContent());
	}

	@Test
	public void testLogin(){
		User user = userService.login("bcde", "4f6ed9e4ab25a6dac05933a8a0c5822ada8177e5");
		System.out.println(user);
	}
	
	@Test
	public void testDataSource() throws SQLException {
		DataSource dataSource = ctx.getBean(DataSource.class);
		System.out.println(dataSource.getConnection());
	}

}
