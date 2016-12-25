<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
    
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>制定计划</title>
	<script type="text/javascript">
		$(function(){
			$("#save").click(function(){
				var url = "${ctp}/plan/";
				
				var chanceId = $("#chanceId").val();
				var todo = $("#todo").val();
				var date = $("#date").val();
				
				var args = {"chance.id":chanceId,"todo":todo,"date":date,"time":new Date()};
				
				$.post(url, args, function(data){
					if(data > 0){
						alert("创建成功!");
						
						//使用 javascript 把新创建的 plan 添加到上面的 table 中.
						/*
						<tr id="plan-20024">
							<td class="list_data_text">
								1990-12-12
								&nbsp;
							</td>
							<td class="list_data_ltext">
								<input type="text" size="50" value="XXXX" />
								<button class="common_button" id="save-20024">
									保存
								</button>
								<input type="hidden" value="20024"/>
								<button class="common_button" id="delete-20024">
									删除
								</button>
							
							</td>
						</tr>
						*/
						var $tr = $("<tr id='plan-" + data + "'></tr>");
						var $td1 = $("<td class='list_data_text'>" + date + "&nbsp;</td>");
						var $td2 = $("<td class='list_data_ltext'></td>");
						$td2.append("<input type='text' size='50' value='" + todo + "' />")
						    .append("<button class='common_button' id='save-" + data + "'>保存</button>")
						    .append("<input type='hidden' value='" + data + "'/>")
						    .append("<button class='common_button' id='delete-" + data + "'>删除</button>");
						$td2.find("button[id^='save-']").click(function(){
							save(this);
						});
						$td2.find("button[id^='delete-']").click(function(){
							delete2(this);
						});
						
						$tr.append($td1).append($td2).appendTo($(".data_list_table"));
					}else{
						alert("创建失败!");
					}
				});
			});
			
			function save(btn){
				var id = $(btn).next(":hidden").val();
				var url = "${ctp}/plan/update/" + id;
				var todo = $(btn).prev(":text").val();
				todo = $.trim(todo);
				
				var args = {"todo":todo, "_method":"PUT", "time":new Date()};
				$.post(url, args, function(data){
					if(data == "1"){
						alert("修改成功!");
					}else{
						alert("修改失败!");
					}
				});
				
				return false;
			}
			
			$("button[id^='save-']").click(function(){
				save(this);
			});
			
			function delete2(btn){
				var id = $(btn).prev(":hidden").val();
				var url = "${ctp}/plan/delete/" + id;
				var args = {"_method":"DELETE", "time":new Date()};
				$.post(url, args, function(data){
					if(data == "1"){
						alert("删除成功!");
						$("#plan-" + id).remove();
					}else{
						alert("删除失败!");
					}
				});
				
				return false;
			}
			
			$("button[id^='delete-']").click(function(){
				delete2(this);
			});			
		})
	</script>
</head>

<body class="main">
	<span class="page_title">制定计划</span>
	<div class="button_bar">
		<button class="common_button" id="execute">
			执行计划
		</button>
		<button class="common_button" onclick="javascript:history.go(-1);">
			返回
		</button>
	</div>
		<input type="hidden" id="chanceId" value="${chance.id }"/>
		
		<form action="${ctp}/plan/make" method="post">
		<table class="query_form_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th>
					编号
				</th>
				<td>
					${chance.id }
				</td>
				<th>
					机会来源
				</th>
				<td>
					${chance.source }
				</td>
			</tr>
			<tr>
				<th>
					客户名称
				</th>
				<td>
					${chance.custName }
				</td>
				<th>
					成功机率（%）
				</th>
				<td>
					${chance.rate }
				</td>
			</tr>
			<tr>
				<th>
					概要
				</th>
				<td colspan="3">
					${chance.title }
				</td>
			</tr>
			<tr>
				<th>
					联系人
				</th>
				<td>
					${chance.contact }
				</td>
				<th>
					联系人电话
				</th>
				<td>
					${chance.contactTel }
				</td>
			</tr>
			<tr>
				<th>
					机会描述
				</th>
				<td colspan="3">
					${chance.description }
				</td>
			</tr>
			<tr>
				<th>
					创建人
				</th>
				<td>
					${chance.createBy.name }
				</td>
				<th>
					创建时间
				</th>
				<td>
					<fmt:formatDate value="${chance.createDate }" pattern="yyyy-MM-dd"/>
				</td>
			</tr>
			<tr>
				<th>
					指派给
				</th>
				<td>
					${chance.designee.name }
				</td>

			</tr>
		</table>

		<br />
		
		<table class="data_list_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th width="200px">
					日期
				</th>
				<th>
					计划项
				</th>
			</tr>
			<c:forEach items="${chance.salesPlans }" var="item">
				<tr id="plan-${item.id }">
					<td class="list_data_text">
						<fmt:formatDate value="${item.date }" pattern="yyyy-MM-dd"/>
						&nbsp;
					</td>
					<td class="list_data_ltext">
					<c:if test="${item.result != null }">
						<input type="text" size="50"
							value="${item.todo }" readonly="readonly"/>
						<input type="text" size="50"
							value="${item.result }" readonly="readonly"/>
					</c:if>
					<c:if test="${item.result == null }">
						<input type="text" size="50" value="${item.todo }" />
						<button class="common_button" id="save-${item.id }">
							保存
						</button>
						<input type="hidden" value="${item.id }"/>
						<button class="common_button" id="delete-${item.id }">
							删除
						</button>
					</c:if>
					</td>
				</tr>
			</c:forEach>
				
				
			
		</table>
		<div class="button_bar">
			<button class="common_button" id="save">
				新建
			</button>
		</div>
		<input type="hidden" name="chance.id" value="20020" />
		<table class="query_form_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th>
					日期
					<br />
					(格式: yyyy-mm-dd)
				</th>
				<td>
					<input type="text" name="date" id="date" />
					&nbsp;
				</td>
				<th>
					计划项
				</th>
				<td>
					<input type="text" name="todo" size="50" id="todo" />
					&nbsp;
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
