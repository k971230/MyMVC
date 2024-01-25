<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
		String ctxPath = request.getContextPath();
		//    /MyMVC
%>


<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/myshop/mallHomeMore.css" />
<script type="text/javascript" src="<%= ctxPath%>/js/myshop/mallHomeMore.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/js/myshop/categoryListJSON.js"></script>

	<%-- === HIT 상품을 모두 가져와서 디스플레이(더보기 방식으로 페이징 처리한 것) === --%>
	<div>
		<p class="h3 my-3 text-center">- HIT 상품 -</p>
		
		<div class="row" id="displayHIT" style="text-align: left;"></div>
		
		<div>
			<p class="text-center">
				<span id="end" style="display:block; margin:20px; font-size: 14pt; font-weight: bold; color: red;"></span> 
				<button type="button" class="btn btn-secondary btn-lg" id="btnMoreHIT" value="">더보기</button>
				<span id="totalHITCount">${requestScope.totalHITCount}</span>
				<span id="countHIT">0</span>
			</p>
		</div>
		
	</div>



