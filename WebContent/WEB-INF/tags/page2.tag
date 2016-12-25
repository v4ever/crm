<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="page" type="org.springframework.data.domain.Page" required="true" rtexprvalue="true" %>
<%@ attribute name="queryString" type="java.lang.String" required="false" rtexprvalue="true" %>

<div style="text-align:right; padding:6px 6px 0 0;">

	共 <%= page.getTotalElements() %> 条记录 
	&nbsp;&nbsp;
	
	当前第 <%= page.getNumber() + 1 %> 页/共 <%= page.getTotalPages() %> 页
	&nbsp;&nbsp;
	
	<% 
		if(page.hasPreviousPage()){
	%>
		<a href='?pageNo=1&<%= queryString %>'>首页</a>
		&nbsp;&nbsp;
		<a href='?pageNo=<%= page.getNumber() %>&<%= queryString %>'>上一页</a>
		&nbsp;&nbsp;
	<%		
		}
	%>
	
	<% 
		if(page.hasNextPage()){
	%>
		<a href='?pageNo=<%= page.getNumber() + 1 + 1 %>&<%= queryString %>'>下一页</a>
		&nbsp;&nbsp;
		<a href='?pageNo=<%= page.getTotalPages() %>&<%= queryString %>'>末页</a>
		&nbsp;&nbsp;
	<%		
		}
	%>
	
	转到 <input id="pageNo" size='1'/> 页
	&nbsp;&nbsp;
	</div>

	<script type="text/javascript" src="${ctp}/static/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript">
	
		$(function(){
			
			$("#pageNo").change(function(){
				
				var pageNo = $(this).val();
				var reg = /^\d+$/;
				if(!reg.test(pageNo)){
					$(this).val("");
					alert("输入的页码不合法");
					return;
				}
				
				var pageNo2 = parseInt(pageNo);
				if(pageNo2 < 1 || pageNo2 > parseInt("<%= page.getTotalPages() %>")){
					$(this).val("");
					alert("输入的页码不合法");
					return;
				}
				
				//查询条件需要放入到 class='condition' 的隐藏域中. 
				window.location.href = window.location.pathname + "?pageNo=" + pageNo2 + "&<%= queryString %>";
			});
		})
	</script>