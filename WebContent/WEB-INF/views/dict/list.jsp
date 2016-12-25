<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<%@ taglib prefix="atguigu" tagdir="/WEB-INF/tags" %>
    
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>管理</title>
</head>
<body>
	<div class="page_title">
		基础数据管理
	</div>
	<div class="button_bar">
		<button class="common_button" onclick="window.location.href='${ctp}/dict/create'">
			新建
		</button>
		<button class="common_button" onclick="document.forms[0].submit();">
			查询
		</button>
	</div>
	
	<form action="${ctp}/dict/list" method="POST">
		<table class="query_form_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th>
					类别
				</th>
				<td>
					<input type="text" name="search_LIKES_type" />
				</td>
				<th>
					条目
				</th>
				<td>
					<input type="text" name="search_LIKES_item" />
				</td>
				<th>
					值
				</th>
				<td>
					<input type="text" name="search_LIKES_value" />
				</td>
			</tr>
		</table>
	</form>
	<!-- 列表数据 -->
	<br />
	
	<table class="data_list_table" border="0" cellPadding="3"
		cellSpacing="0">
		<tr>
			<th>
				编号
			</th>
			<th>
				类别
			</th>
			<th>
				条目
			</th>
			<th>
				值
			</th>
			<th>
				操作
			</th>
		</tr>
		<c:forEach items="${page.content }" var="item">
			<tr>
				<td class="list_data_number">
					${item.id }
				</td>
				<td class="list_data_text">
					${item.type }
				</td>
				<td class="list_data_text">
					${item.item }
				</td>
				<td class="list_data_text">
					${item.value }
				</td>

				<td class="list_data_op">
					
				</td>
			</tr>
		</c:forEach>
	</table>
	
	<atguigu:page2 page="${page }" queryString="${queryString }"></atguigu:page2>
	
</body>
</html>