<%@page pageEncoding="utf-8"%>
<span>${cookie.adminCode.value }</span>
<%-- 
	${user} 
 == request.getAttribute("user")
 	${user.name}
 == request.getAttribute("user").getName()
	${pageContext.request.contextPath}
 == pageContext.getRequest().getContextPath()
 --%>
<a href="${pageContext.request.contextPath}/logout.do">[退出]</a>