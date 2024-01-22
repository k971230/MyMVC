<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%-- === main tiles 를 사용하는 레이아웃 페이지 만들기 === --%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%
	String ctxPath = request.getContextPath();
%>      
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyMVC 타일즈1</title>

<%-- Required meta tags --%>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<%-- Bootstrap CSS --%>
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css" > 

<%-- Font Awesome 6 Icons --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

<%-- 직접 만든 CSS --%>
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/template/template.css" />

<%-- Optional JavaScript --%>
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" ></script> 

<%-- jQueryUI CSS 및 JS --%>
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.css" />
<script type="text/javascript" src="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.js"></script>

<%-- 직접 만든 JS --%>
<script type="text/javascript" src="<%= ctxPath%>/js/template/template.js"></script>

</head>
<body>
	<div id="mycontainer">
	
		<div id="myheader">
			<tiles:insertAttribute name="header" />
		</div>
		
		<hr style="background-color: gold; height: 1.2px; position: relative; top:85px; margin: 0 1.7%;"> 
		
		<div class="container-fluid" id="container" style="position: relative; top:90px; padding: 0.1% 2.5%;">
      		<div class="row">
     	
				<div id="mysideinfo" class="col-md-3" >
		         	<tiles:insertAttribute name="sideinfo" />
		      	</div>
		      	
		      	<div class="col-md-9" id="maininfo" align="center">
			        <div id="mycontent"  >
						<tiles:insertAttribute name="content" />
					</div>
				</div>
		
			</div>
		
		<div id="myfooter">
			<tiles:insertAttribute name="footer" />
		</div>
		</div>
	</div>
</body>
</html>