<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<%@ taglib prefix="atguigu" tagdir="/WEB-INF/tags" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>销售机会管理</title>
	<script type="text/javascript">
		$(function(){
			$("#new").click(function(){
				window.location.href="${ctp}" + "/chance/";
				return false;
			});
			
			$("img[id^='delete-']").click(function(){
				var custName = $(this).prev(":hidden").val();
				var flag = confirm("确定要删除" + custName + "的信息吗?");
				if(!flag){
					return;
				}
				
				var id = $(this).next(":hidden").val();
				var url = "${ctp}/chance/" + id;
				
				$("#_method").val("DELETE");
				$("#hiddenForm").attr("action",url).submit();
			});
		})
	</script>
</head>

<body class="main">

	<form action="" method="POST" id="hiddenForm">
		<input type="hidden" name="_method" id="_method" value=""/>
	</form>

	<form id="command" action="${ctp}/chance/list" method="post">
		<div class="page_title">
			销售机会管理
		</div>
		<div class="button_bar">
			<button class="common_button" id="new">
				新建
			</button>
			<button class="common_button" onclick="document.forms[1].submit();">
				查询
			</button>
		</div>
		<table class="query_form_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th class="input_title">
					客户名称
				</th>
				<td class="input_content">
					<input type="text" name="search_LIKES_custName" />
				</td>
				<th class="input_title">
					概要
				</th>
				<td class="input_content">
					<input type="text" name="search_LIKES_title" />
				</td>
				<th class="input_title">
					联系人
				</th>
				<td class="input_content">
					<input type="text" name="search_LIKES_contact" />
				</td>
			</tr>
		</table>
		<!-- 列表数据 -->
		<br />
		<c:if test="${!empty requestScope.page.content }">
			<table class="data_list_table" border="0" cellPadding="3" cellSpacing="0">
				<tr>
					<th>
						编号
					</th>
					<th>
						客户名称
					</th>
					<th>
						概要
					</th>
					<th>
						联系人
					</th>
					<th>
						联系人电话
					</th>
					<th>
						创建时间
					</th>
					<th>
						操作
					</th>
				</tr>
				<c:forEach items="${page.content }" var="item">
					<tr>
						<td class="list_data_number">${item.id }</td>
						<td class="list_data_text">${item.custName }</td>
						<td class="list_data_text">${item.title }</td>
						<td class="list_data_text">${item.contact }</td>
						<td class="list_data_text">${item.contactTel }</td>
						<td class="list_data_text">
							<fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd"/>
						</td>
						<td class="list_data_op">
							<img onclick="window.location.href='${ctp}/chance/dispatch/${item.id }'" 
								title="指派" src="${ctp}/static/images/bt_linkman.gif" class="op_button" />
							<img onclick="window.location.href='${ctp}/chance/${item.id }'" 
								title="编辑" src="${ctp}/static/images/bt_edit.gif"
								class="op_button" />
								
							<input type="hidden" value="${item.custName }"/>		
							<img title="删除" src="${ctp}/static/images/bt_del.gif" 
								class="op_button" id="delete-${item.id }" />
							<input type="hidden" value="${item.id }"/>	
						</td>
					</tr>
				</c:forEach>
			</table>

			<atguigu:page page="${page }" queryString="${queryString }"></atguigu:page>
			
		</c:if>
		<c:if test="${empty page.content }">
			没有任何的数据信息.
		</c:if>
	</form>
	
</body>
</html>