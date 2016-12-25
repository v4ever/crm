package com.atguigu.crm.service.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.atguigu.crm.entity.Dict;
import com.atguigu.crm.orm.PropertyFilter;
import com.atguigu.crm.orm.PropertyFilter.MatchType;
import com.atguigu.crm.repository.DictRepository;
import com.atguigu.crm.util.ReflectionUtils;

@Service
public class DictService {

	@Autowired
	private DictRepository dictRepository;
	
	public Page<Dict> getPage(int pageNo, int pageSize, Map<String, Object> params){
		//1. 把 pageNo 和 pageSize 封装为 PageRequest 对象
		PageRequest pageable = new PageRequest(pageNo, pageSize);
		//2. 把传入的查询条件的 params 转为 PropertyFilter 的集合
		List<PropertyFilter> filters = PropertyFilter.parseParamsToPropertyFilters(params);
		//3. 把 PropertyFilter 的集合转为 Specification 对象
		Specification<Dict> specification = parsePropertyFiltersToSpecification(filters);
		
		return dictRepository.findAll(specification, pageable);
		
		//SpringData: findAll(Predicate, Pageable);
	}
	
	//把 PropertyFilter 的集合转为一个 Predicate 的查询条件
	private Specification<Dict> parsePropertyFiltersToSpecification(
			final List<PropertyFilter> filters) {
		Specification<Dict> specification = new Specification<Dict>() {
			/**
			 * @param root: 查询的根对象. 可以导航到其属性
			 * @param query
			 * @param builder: 可以构建查询条件的工厂类
			 * @return: Predicate: 实际上是 JPA Criteria 查询的一个查询条件
			 */
			@Override
			public Predicate toPredicate(Root<Dict> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<>();
				
				for(PropertyFilter filter: filters){
					String propertyName = filter.getPropertyName(); //可能是级联的. 例如: manager.name
					
					//导航到实际的额 Path. 或 Expression
					String [] names = propertyName.split("\\.");
					Path path = root.get(names[0]);
					for(int i = 1; i < names.length; i++){
						path = path.get(names[i]);
					}
					Expression<Comparable> expression = path;
					
					Object propertyVal = filter.getPropertyVal();
					Class propertyType = filter.getPropertyType();
					//把传入的类型转为实际的类型
					propertyVal = ReflectionUtils.convertValue(propertyVal, propertyType);
					
					MatchType matchType = filter.getMatchType();
					
					Predicate predicate = null;
					switch(matchType){
					case EQ:
						predicate = builder.equal(expression, propertyVal);
						break;
					case GE:
						predicate = builder.greaterThanOrEqualTo(expression, (Comparable)propertyVal);
						break;
					case GT:
						predicate = builder.greaterThan(expression, (Comparable)propertyVal);
						break;
					case LE:
						predicate = builder.lessThanOrEqualTo(expression, (Comparable)propertyVal);
						break;
					case LT:
						predicate = builder.lessThan(expression, (Comparable)propertyVal);
						break;
					case LIKE:
						predicate = builder.like(path, "%" + propertyVal.toString() + "%");
						break;
					case NOTEQ:
						predicate = builder.notEqual(expression, propertyVal);
						break;
					} 
					
					if(predicate != null){
						predicates.add(predicate);
					}
				}
				
				return builder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
		
		return specification;
	}

	public void save(Dict dict){
		dictRepository.saveAndFlush(dict);
	}
}
