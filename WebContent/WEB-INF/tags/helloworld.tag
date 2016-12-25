<%@ tag import="java.util.Date"%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag body-content="scriptless" %>

<!-- rtexprvalue rt: runtime, expr: expression, value. 即是否可以接受 EL 的值. 通常需要设置为 true -->
<%@ attribute name="count" type="java.lang.Integer" required="true" rtexprvalue="true" %>

<!-- 直接输出内容 -->
HelloWorld, time is: <%= new Date() %>

<br><br>
<!-- 输出标签体的内容 -->
<jsp:doBody></jsp:doBody>

<br><br>
<!-- 使用标签的属性 -->
<%
	for(int i = 0; i < count; i++){
		out.print(i);
		out.print("<br>");
	}
%>

<%
	getj
%>