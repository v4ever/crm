<%@page import="com.atguigu.crm.entity.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="atguigu" tagdir="/WEB-INF/tags" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	<atguigu:helloworld count="10">
		welcome: ${param.username }
	</atguigu:helloworld>

	<br><br>
	
	<!--  
	1. 使用 form:form 标签, 必须注意其 modelAttribute 属性. 
	2. modelAttribute 属性的默认值为: command
	3. 若 form:form 中有 form 标签的其他元素. 则必须在 request 中有一个 key 为 modelAttribute 属性值对应的 bean.
	若没有, 则错误信息为: 
	Neither BindingResult nor plain target object for bean name 'command' available as request attribute
	4. 若有, 且 request 中属性值对应的 bean 必须有和表单元素的字段对应的属性!
	若没有, 则错误消息为:
	Invalid property 'name' of bean class [java.lang.String]: 
	Bean property 'name' is not readable or has an invalid getter method: Does the return type of the getter match the parameter type of the setter?
	-->

	<% 
		request.setAttribute("command", new User());
	%>
	
	<h4>Test ModelAttribute</h4>
	
	<form:form action="" method="POST" modelAttribute="command">
		_method: <input type="text" name="_method" value="PUT"/>
		<br><br>
		
		name: <form:input path="name"/>
	</form:form>
	
</body>
</html>