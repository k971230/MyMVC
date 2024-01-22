<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    

    
       
         
         <%-- 유트브 넣기 header1.jsp 에만 있음 --%>
		 <div class="row">
			<div class="col-md-8 offset-md-2 mt-3 embed-responsive embed-responsive-16by9">
		   		<iframe class="embed-responsive-item" src="https://www.youtube.com/embed/E0W5sJZ2d64" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
			</div>
		 </div>
         
         <div style="height: 200px; text-align: left; padding: 20px;">
            
            <%-- === 로그인 처리하기 === --%>
            <%@ include file="/WEB-INF/views/login/login.jsp" %>
            
            <%-- == 쇼핑몰 카테고리목록만을 보여주는 부분 (header1.jsp 에만 있음) == --%>
         <div id="categoryList" style="margin-top: 100px;">
         </div>
         
         </div>
         <div id="sidecontent" style="text-align: left; padding: 20px;"></div>
       