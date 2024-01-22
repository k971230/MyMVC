<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
    String ctxPath = request.getContextPath();
    //    /MyMVC
%>

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

<script type="text/javascript">
 $(document).ready(function(){
	 
	 const method = "${requestScope.method}";
	 //console.log("확인용 method : ", method);
	 
	 if(method == "GET"){
	 $("div#div_findResult").hide();
	 }
	 else if(method == "POST"){
	 $("input:text[name='name']").val("${requestScope.name}");
	 $("input:text[name='email']").val("${requestScope.email}");
	 }
	 
	 
	 $("button.btn-success").click(function(){
		 
		 goFind();
		 
	 });
	 
	 $("input:text[name='email']").bind("keyup", function(e){
		 if(e.keyCode === 13){
		 goFind();
		 }
	 });
	 
 });//$(document).ready(function(){
	 
	 //Function Declaration
	 function goFind(){
		 const name = $("input:text[name='name']").val().trim();
			const email = $("input:text[name='email']").val().trim();
			
			if(name == "" || email == "") {
				alert("성명과 이메일 모두 입력하세요!!");
				return; // 종료
			}
			else {
				
				const frm = document.idFindFrm;
				frm.action = "<%= ctxPath%>/login/idFind.up";
				frm.method = "post";
				frm.submit();
			}
	 }// end of function goFind
	
	 
	 // 아이디 찾기 모달창에 입력한 input 태그 value 값 초기화 시키기
	 function func_form_reset_empty(){
		 document.querySelector("form[name='idFindFrm']").reset();
		 $("div#div_findResult").empty();
		 
	 }// end of function func_form_reset_empty()---------
	
	 
	 
	 
</script>

<form name="idFindFrm">

	<ul style="list-style-type: none;">
		<li>
			<label style="display: inline-block; width:90px;">성명</label>
			<input type="text" name="name" size="25" autocomplete="off"/>
		</li>
		<li>
			<label style="display: inline-block; width:90px;">이메일</label>
			<input type="text" name="email" size="25" autocomplete="off"/>
		</li>
		
	</ul>
	
	<div class="my-3 text-center">
		<button type="button" class="btn btn-success">찾기</button>
	</div>

</form>

<div class="my-3 text-center" id="div_findResult">
	ID : <span style="color: red; font-size: 16pt; font-weight: bold;">${requestScope.userid}</span>
</div>


