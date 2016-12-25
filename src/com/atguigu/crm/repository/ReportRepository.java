package com.atguigu.crm.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.atguigu.crm.entity.Customer;
import com.atguigu.crm.util.ReflectionUtils;

@Repository
public class ReportRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * 返回 "客户构成分析" 所对应的 Page 对象! Page 的 content 中的类型为 Object[] 类型.
	 * 第一个元素是客户的名称, 第二个元素是对应的数量.
	 * 传入的条件是分组的类别. level OR satify OR credit
	 */
	public Page<Object[]> getConsistPage(int pageNo, int pageSize, String type){
		String jpql = "SELECT count(c.id) "
				+ "FROM Customer c "
				+ "GROUP BY c." + type;
		List<Object> result = entityManager.createQuery(jpql).getResultList();
		long total = result.size();
		System.out.println(total);
		
		jpql = "SELECT count(c.id), c." + type + " "
				+ "FROM Customer c "
				+ "GROUP BY c." + type;
		int firstResult = pageNo * pageSize;
		int maxResults = pageSize;
		List<Object[]> content = entityManager.createQuery(jpql)
				                              .setFirstResult(firstResult)
				                              .setMaxResults(maxResults)
				                              .getResultList();
		
		PageRequest pageable = new PageRequest(pageNo, pageSize);
		PageImpl<Object[]> page = new PageImpl<>(content, pageable, total);
		
		//3. 返回 Page 对象
		return page;
	}
	
	
	/**
	 * 1. 直接使用 SpringDate 的 JpaSpecificationExecutor 的 findPage 方法不能添加 1-n 的集合属性中元素的属性作为
	 * 查询条件!
	 * 2. 需要自己来实现有 1-n 的集合属性中元素的属性作为查询条件的翻页.
	 * 3. 自定义 Repository, 且不需要再实现 Repsotoriy 接口
	 * 4. 使用  @PersistenceContext 注入 EntityManager
	 * 5. 实际在方法中使用 JPQL 来实现具体的分页
	 */
	public Page<Customer> getCustomerPage(int pageNo, int pageSize, Map<String, Object> params){
		//1. 查询总的记录数
		Object name = params.get("name");
		if(name == null){
			name = "";
		}
		name = "%" + name + "%";
		
		String minOrderDateStr = (String) params.get("minOrderDate");
		Date minOrderDate = null;
		try {
			minOrderDate = ReflectionUtils.convertValue(minOrderDateStr, Date.class);
		} catch (Exception e) {
			minOrderDate = ReflectionUtils.convertValue("1970-1-1", Date.class);
		}
		
		String maxOrderDateStr = (String) params.get("maxOrderDate");
		Date maxOrderDate = null;
		try {
			maxOrderDate = ReflectionUtils.convertValue(maxOrderDateStr, Date.class);
		} catch (Exception e) {
			maxOrderDate = ReflectionUtils.convertValue("2099-1-1", Date.class);
		}
		
		String jpql = "SELECT count(DISTINCT c.id) "
				+ "FROM Customer c "
				+ "LEFT OUTER JOIN c.orders o "
				+ "WHERE c.name LIKE :name "
				+ "AND o.date >= :minDate "
				+ "AND o.date <= :maxDate";
		Long count = (Long)entityManager.createQuery(jpql)
				                        .setParameter("name", name)
				                        .setParameter("minDate", minOrderDate)
				                        .setParameter("maxDate", maxOrderDate)
				                        .getSingleResult();
		System.out.println(count);
		
		//2. 查询当前页面需要的 content
		jpql = "SELECT DISTINCT c "
				+ "FROM Customer c "
				+ "LEFT OUTER JOIN FETCH c.orders o "
				+ "LEFT OUTER JOIN FETCH o.items "
				+ "WHERE c.name LIKE :name "
				+ "AND o.date >= :minDate "
				+ "AND o.date <= :maxDate";
		int firstResult = pageNo * pageSize;
		int maxResults = pageSize;
		List<Customer> content = entityManager.createQuery(jpql)
								              .setParameter("name", name)
								              .setParameter("minDate", minOrderDate)
								              .setParameter("maxDate", maxOrderDate)
								              .setFirstResult(firstResult)
								              .setMaxResults(maxResults)
								              .getResultList();
		
		PageRequest pageable = new PageRequest(pageNo, pageSize);
		PageImpl<Customer> page = new PageImpl<>(content, pageable, count);
		
		//3. 返回 Page 对象
		return page;
	}
}
