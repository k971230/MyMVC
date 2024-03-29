<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
   String ctxPath = request.getContextPath();
    //     /MyMVC
%>


<script type="text/javascript" src="<%= ctxPath%>/js/myshop/categoryListJSON.js"></script>    
<style type="text/css" >
   table#tblCartList {width: 90%;
                      border: solid gray 1px;
                      margin-top: 20px;
                      margin-left: 10px;
                      margin-bottom: 20px;}
                      
   table#tblCartList th {border: solid gray 1px;}
   table#tblCartList td {border: dotted gray 1px;} 
   
/* -- CSS 로딩화면 구현 시작(bootstrap 에서 가져옴) 시작 -- */    
  div.loader {
     /* border: 16px solid #f3f3f3; */
     border: 12px solid #f3f3f3;
     border-radius: 50%;
     /* border-top: 16px solid #3498db; */
     border-top: 12px dotted blue;
     border-right: 12px dotted green;
     border-bottom: 12px dotted red;
     border-left: 12px dotted orange;
     
     width: 120px;
     height: 120px;
     -webkit-animation: spin 2s linear infinite; /* Safari */
     animation: spin 2s linear infinite;
   }

/* Safari */
   @-webkit-keyframes spin {
     0% { -webkit-transform: rotate(0deg); }
     100% { -webkit-transform: rotate(360deg); }
   }
   
   @keyframes spin {
     0% { transform: rotate(0deg); }
     100% { transform: rotate(360deg); }
   }
/* -- CSS 로딩화면 구현 끝(bootstrap 에서 가져옴) 끝 -- */
    
</style>

<script type="text/javascript">

   $(document).ready(function(){
	   
	   
         $("div.loader").hide();// CSS 로딩화면 감추기
         
      	$("p#order_error_msg").css({'display':'none'});// 코인잔액 부족시 주문이 안된다는 표시를 해주는 곳.
                        
      
      $(".spinner").spinner({
         spin: function(event, ui) {
            if(ui.value > 100) {
               $(this).spinner("value", 100);
               return false;
            }
            else if(ui.value < 0) {
               $(this).spinner("value", 0);
               return false;
            }
         }
      });// end of $(".spinner").spinner({});-----------------
      
            
      // 제품번호의 모든 체크박스가 체크가 되었다가 그 중 하나만 이라도 체크를 해제하면 전체선택 체크박스에도 체크를 해제하도록 한다.
      $(".chkboxpnum").click(function(){
         
         let bFlag = false;
         $(".chkboxpnum").each(function(){
            const bChecked = $(this).prop("checked");
            if(!bChecked) {
               $("#allCheckOrNone").prop("checked",false);
               bFlag = true;
               return false;
            }
         });
         
         if(!bFlag) {
            $("#allCheckOrNone").prop("checked",true);
         }
         
      });
      
   }); // end of $(document).ready()--------------------------
   
   
   // Function declaration
   
   function allCheckBox() {
   
      const bool = $("#allCheckOrNone").is(":checked");
      /*
         $("#allCheckOrNone").is(":checked"); 은
           선택자 $("#allCheckOrNone") 이 체크되어지면 true를 나타내고,
           선택자 $("#allCheckOrNone") 이 체크가 해제되어지면 false를 나타내어주는 것이다.
      */
      
      $(".chkboxpnum").prop("checked", bool);
   }// end of function allCheckBox()-------------------------
   
   
   // === 장바구니 현재주문수량 수정하기 === // 
   function goOqtyEdit(obj) {
	   
	   
	   
	   const index = $("button.updateBtn").index(obj);
	   //alert(index);
	   
	   const cartno = $("input.cartno").eq(index).val(); // 장바구니번호
	   // 배열중에서 몇번째 요소를 꺼내오는것과 같은느낌
	   
	   const oqty = $("input.oqty").eq(index).val(); // 수정개수
	   
	   //console.log(`장바구니번호 : \${cartno}, 수정개수 : \${oqty}`);
	   
	   const regExp = /^[0-9]+$/g;// 숫자만 체크하는 정규표현식 
	   
	   const bool = regExp.test(oqty);
	   
	   if(!bool) {
		   alert("수정하시려는 수량은 0개 이상이어야 합니다.");
		   location.href="javascript:history.go(0)";
		   return; // 함수 종료
	   }
	   const pqty = $("input.pqty").eq(index).val();  // 잔고개수
	     // alert("주문량 : "+ oqty + ", 잔고량 : " + pqty);
	   	 // alert(typeof oqty+","+typeof pqty);
	  
	  //   !!! 조심할 것 !!! //
	  //   if(oqty > pqty) { 으로 하면 꽝됨!! 왜냐하면 string 타입으로 비교하기에
	  //   if("2" > "19") {  참이됨
	  //   if(2 > 19) {  거짓이됨     
	      if(Number(oqty) > Number(pqty)) {
	         alert("주문개수가 잔고개수 보다 더 커서 진행할 수 없습니다.");
	         location.href="javascript:history.go(0)";
	         return; // goOqtyEdit 함수 종료
	      }
	  
	   
	   if(oqty == "0") {
		   goDel(cartno); // 해당 장바구니 번호 비우기 
	   }
	   else {
		   $.ajax({
			   url:"<%= ctxPath%>/shop/cartEdit.up",
			   type:"post",
			   data:{"cartno":cartno, 
				     "oqty":oqty},
			   dataType:"json",
			   success:function(json){
				   console.log("~~ 확인용 ", JSON.stringify(json));
				   // ~~~~ 확인용 {"n":1}
				   
				   if(json.n == 1) {
					   alert("주문수량이 변경되었습니다.")
					   location.href = "cartList.up"; // 장바구니 보기 페이지로 간다.
				   }
				   else if(json.n == 100){
					   alert("비정상적인 경로로 들어왔습니다.");
					   location.href = "javascript:history.back()";
				   }
			   },
			   error: function(request, status, error){
	               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	            }
			   
		   });
	   }
	   
	   
	   
     
   
   }// end of function goOqtyEdit(obj)-----------------
   
   
   // === 장바구니에서 특정 제품을 비우기 === //  
   function goDel(cartno) {
      
	   const pname = $(event.target).parent().parent().find("span.cart_pname").text();
	   /*
	   if(confirm(pname+"을(를) 장바구니에서 제거하시는 것이 맞습니까?")){
		   
		   
	   }
	   else {
		   alert("장바구니에서 " +pname+ " 제품 삭제를 취소하셨습니다.")
	   }
	   */
	   // 또는
	   if(confirm(`\${pname}을(를) 장바구니에서 제거하시는 것이 맞습니까?`)){
		   
		   $.ajax({
			   url:"<%=ctxPath%>/shop/cartDel.up",
			   type:"post",
			   data:{"cartno":cartno},
			   dataType: "json",
			   success:function(json){
				   console.log("~~ 확인용 ", JSON.stringify(json));
				   // ~~~~ 확인용 {"n":1}
				   
				   if(json.n == 1) {
					   //장바구니가 변경되었으므로 다시 새로고침을 한다.
					   location.href = "cartList.up"; // 장바구니 보기 페이지로 간다.
				   }
				   else if(json.n == 100){
					   alert("비정상적인 경로로 들어왔습니다.");
					   location.href = "javascript:history.back()";
				   }
				   
				  
			   },
			   error: function(request, status, error){
	               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	            }
		   });
		   
	   }
	   else {
		   alert(`장바구니에서 \${pname} 제품 삭제를 취소하셨습니다.`)
	   }
	   
	   
      
      
   }// end of function goDel(cartno)---------------------------
   
   
   // === 장바구니에서 제품 주문하기 === // 
   function goOrder() {
	   
	   
	   //// === 체크박스의 체크된 개수(checked 속성이용) == ////
	   
	 const checkCnt = $("input:checkbox[name='pnum']:checked").length;
	 
	   if(checkCnt < 1) {
		   alert("주문하실 제품을 선택하세요!!");
		   return; //종료
		}
	   
	   else {
			//// === 체크박스의 체크된 개수(checked 속성이용) == //// 
			//// === 체크가 된 것만 읽어와서 배열에 넣어준다. === ////
			const allCnt = $("input:checkbox[name='pnum']").length;
			
			const pnumArr = new Array();        // 또는 const pnumArr = [];
            const oqtyArr = new Array();        // 또는 const oqtyArr = [];
            const pqtyArr = new Array();        // 또는 const pqtyArr = [];
            const cartnoArr = new Array();      // 또는 const cartnoArr = [];
            const totalPriceArr = new Array();  // 또는 const totalPriceArr = [];
            const totalPointArr = new Array();  // 또는 const totalPointArr = [];
            
            for(let i=0; i<allCnt; i++){
            	
            	if( $("input:checkbox[name='pnum']").eq(i).prop("checked") ) {
            	    
            		/* console.log("제품번호 : " , $("input:checkbox[name='pnum']").eq(i).val() ); 
            	    console.log("주문량 : " , $("input.oqty").eq(i).val() );
            	    console.log("삭제해야할 장바구니 번호 : " , $("input.cartno").eq(i).val() ); 
            	    console.log("주문한 제품의 개수에 따른 가격합계 : " , $("input.totalPrice").eq(i).val() );
            	    console.log("주문한 제품의 개수에 따른 포인트합계 : " , $("input.totalPoint").eq(i).val() );
            	    console.log("============================") */
            	    
            	    pnumArr.push( $("input:checkbox[name='pnum']").eq(i).val() );
                    oqtyArr.push( $("input.oqty").eq(i).val() );
                    cartnoArr.push( $("input.cartno").eq(i).val() );
                    totalPriceArr.push( $("input.totalPrice").eq(i).val() );
                    totalPointArr.push( $("input.totalPoint").eq(i).val() );
                    pqtyArr.push( $("input.pqty").eq(i).val() );
            	}
            
            }//end of for
            <%-- 
            for(let i=0; i<checkCnt; i++) {
              console.log("확인용 제품번호: " + pnumArr[i] + ", 주문량: " + oqtyArr[i] + ", 잔고량: " + pqtyArr[i] + ", 장바구니번호 : " + cartnoArr[i] + ", 주문금액: " + totalPriceArr[i] + ", 포인트: " + totalPointArr[i]);
            /*
                    확인용 제품번호: 5, 주문량: 1, 잔고량: 50, 장바구니번호 : 11, 주문금액: 33000, 포인트: 20
                        확인용 제품번호: 4, 주문량: 2, 잔고량: 48, 장바구니번호 : 8, 주문금액: 26000, 포인트: 20
                        확인용 제품번호: 61, 주문량: 3, 잔고량: 15,장바구니번호 : 7, 주문금액: 57000, 포인트: 300    
            */
            }// end of for---------------------------
         --%>
            
         for(let i=0; i<checkCnt; i++) {
             if(Number(pqtyArr[i]) < Number(oqtyArr[i])) {
                // 주문할 제품중 아무거나 하나가 잔고량이 주문량 보다 적을 경우
                
                alert("제품번호 "+ pnumArr[i] +" 의 주문개수가 잔고개수 보다 더 커서 진행할 수 없습니다.");
             location.href="javascript:history.go(0)";
             return; // goOrder 함수 종료
             } 
          }// end of for---------------------------
          
         const str_pnum = pnumArr.join();
         const str_oqty = oqtyArr.join();
         const str_cartno = cartnoArr.join();
         const str_totalPrice = totalPriceArr.join();
         const str_totalPoint = totalPointArr.join();
         
         let n_sum_totalPrice = 0;
         for(let i=0; i<totalPriceArr.length; i++ ){
        	 n_sum_totalPrice += Number(totalPriceArr[i]);
        	 
         }//end of for 
         
         let n_sum_totalPoint = 0;
         for(let i=0; i<totalPointArr.length; i++ ){
        	 n_sum_totalPoint += Number(totalPointArr[i]);
        	 
         }//end of for 
         
         <%--
         console.log("확인용 str_pnum : ", str_pnum); 					//확인용 str_pnum :  34,36,63
         console.log("확인용 str_oqty : ", str_oqty); 					//확인용 str_oqty :  4,3,3
         console.log("확인용 str_cartno : ", str_cartno); 				//확인용 str_cartno :  6,5,4
         console.log("확인용 str_totalPrice : ", str_totalPrice); 		//확인용 str_totalPrice :  4000000,3000000,57000
         console.log("확인용 str_totalPoint : ", str_totalPoint); 		//확인용 str_totalPoint :  240,180,300
         console.log("확인용 n_sum_totalPrice : ", n_sum_totalPrice); //확인용 n_sum_totalPrice :  7057000
         console.log("확인용 n_sum_totalPoint : ", n_sum_totalPoint); //확인용 n_sum_totalPoint :  720
         --%>
         
         	const current_coin = ${sessionScope.loginuser.coin};
         	
         	if(current_coin < n_sum_totalPrice){
         		$("p#order_error_msg").html("코인잔액이 부족하므로 주문이 불가합니다.<br>주문총액 : "+ n_sum_totalPrice.toLocaleString('en') +"원 / 코인잔액 : "+ current_coin.toLocaleString('en') +"원").css({'display':''});
         		// 숫자.toLocaleString('en') 자바스크립트에서 숫자 3자리마다 콤마 찍어주기
         		return; // 종료
         	}
         	
         	else{
         		$("p#order_error_msg").css({'display':'none'});
         		
         		if( confirm("총주문액 " + n_sum_totalPrice.toLocaleString('en') + "원을 결제하시겠습니까?")){
         			
         			$("div.loader").show();// CSS 로딩화면 보여주기
         			
         			$.ajax({
         				url:"<%=ctxPath%>/order/orderAdd.up",
         				type:"post",
         				data:{"n_sum_totalPrice":n_sum_totalPrice,
         					  "n_sum_totalPoint":n_sum_totalPoint,
         					  "str_pnum_join":str_pnum,
         					  "str_oqty_join":str_oqty,
         					  "str_totalPrice_join":str_totalPrice,
         					  "str_cartno_join":str_cartno 
         					},
         				dataType:"json",
         				success:function(json){
         					if(json.isSuccess == 1) {
                                location.href="<%=ctxPath%>/shop/orderList.up";
                             }
                             else {
                                location.href="<%=ctxPath%>/shop/orderError.up";
                             }
         					
         				},
         				error: function(request, status, error){
         	               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
         	            }
         				
         			});
         		}
         		
         		
         	}
         	
      		
	   }
	   
     
      
   }// end of function goOrder()----------------------
   
</script>

<div class="container-fluid" style="border: solid 0px red">
   <div class="my-3">
      <p class="h4 text-center">&raquo;&nbsp;&nbsp;${(sessionScope.loginuser).name} [${(sessionScope.loginuser).userid}]님 장바구니 목록&nbsp;&nbsp;&laquo;</p>
   </div>
   <div>
       <table id="tblCartList" class="mx-auto" style="width: 90%">
         <thead>
            <tr>
             <th style="border-right-style: none;">
                 <input type="checkbox" id="allCheckOrNone" onClick="allCheckBox();" />
                 <span style="font-size: 10pt;"><label for="allCheckOrNone">전체선택</label></span>
             </th>
             <th colspan="5" style="border-left-style: none; font-size: 12pt; text-align: center;">
                 <marquee>주문을 하시려면 먼저 제품번호를 선택하신 후 [주문하기] 를 클릭하세요</marquee>
             </th>
            </tr>
         
            <tr style="background-color: #cfcfcf;">
              <th style="width:10%; text-align: center; height: 30px;">제품번호</th>
              <th style="width:23%; text-align: center;">제품명</th>
                 <th style="width:17%; text-align: center;">현재주문수량</th>
                 <th style="width:20%; text-align: center;">판매가/포인트(개당)</th>
                 <th style="width:20%; text-align: center;">주문총액/총포인트</th>
                 <th style="width:10%; text-align: center;">비우기</th>
            </tr>   
         </thead>
       
         <tbody>
            <c:if test="${empty requestScope.cartList}">
               <tr>
                    <td colspan="6" align="center">
                      <span style="color: red; font-weight: bold;">
                         장바구니에 담긴 상품이 없습니다.
                      </span>
                    </td>   
               </tr>
            </c:if>   
            
            <c:if test="${not empty requestScope.cartList}">
               <c:forEach var="cartvo" items="${requestScope.cartList}" varStatus="status"> 
                   <tr>
                        <td> <%-- 체크박스 및 제품번호 --%>
                             <%-- c:forEach 에서 선택자 id를 고유하게 사용하는 방법  
                                  varStatus="status" 을 이용하면 된다.
                                  status.index 은 0 부터 시작하고,
                                  status.count 는 1 부터 시작한다. 
                             --%> 
                             <input type="checkbox" name="pnum" class="chkboxpnum" id="pnum${status.index}" value="${cartvo.fk_pnum}" /> &nbsp;<label for="pnum${status.index}">${cartvo.fk_pnum}</label>   
                        </td>
                        <td align="center"> <%-- 제품이미지1 및 제품명 --%> 
                           <a href="<%=ctxPath %>/shop/prodView.up?pnum=${cartvo.fk_pnum}">
                              <img src="<%=ctxPath %>/images/${cartvo.prod.pimage1}" class="img-thumbnail" width="130px" height="100px" />
                           </a> 
                           <br/><span class="cart_pname">${cartvo.prod.pname}</span> 
                        </td>
                        <td align="center"> 
                            <%-- 현재주문수량 --%>
                              <input class="spinner oqty" name="oqty" value="${cartvo.oqty}" style="width: 30px; height: 20px;">개
                              <button type="button" class="btn btn-outline-secondary btn-sm updateBtn" onclick="goOqtyEdit(this)">수정</button>
                              
                              <%-- 장바구니 테이블에서 특정제품의 현재주문수량을 변경하여 적용하려면 먼저 장바구니번호(시퀀스)를 알아야 한다 --%>
                              <input type="hidden" class="cartno" value="${cartvo.cartno}" /> 
                              <%-- 잔고량 --%>
                              <input type="hidden" class="pqty" value="${cartvo.prod.pqty}" />
                        </td>
                        	 
                              
                        <td align="right"> <%-- 판매가/포인트(개당) --%> 
                            <fmt:formatNumber value="${cartvo.prod.saleprice}" pattern="###,###" /> 원<br>
                            <fmt:formatNumber value="${cartvo.prod.point}" pattern="###,###" /> POINT
                        </td>
                        <td align="right"> <%-- 주문총액/총포인트 --%> 
                            <fmt:formatNumber value="${cartvo.prod.totalPrice}" pattern="###,###" /> 원<br>
                            <fmt:formatNumber value="${cartvo.prod.totalPoint}" pattern="###,###" /> POINT
                            <input type="hidden" class="totalPrice" value="${cartvo.prod.totalPrice}" />
                            <input type="hidden" class="totalPoint" value="${cartvo.prod.totalPoint}" />
                        </td>
                        <td align="center"> <%-- 장바구니에서 해당 특정 제품 비우기 --%> 
                            <button type="button" class="btn btn-outline-danger btn-sm" onclick="goDel('${cartvo.cartno}')">삭제</button>  
                        </td>
                    </tr>
                 </c:forEach>
            </c:if>   
            
            <tr>
                 <td colspan="3" align="right">
                     <span style="font-weight: bold;">장바구니 총액 :</span>
                  <span style="color: red; font-weight: bold;"><fmt:formatNumber value="${requestScope.sumMap.SUMTOTALPRICE}" pattern="###,###" /></span> 원  
                     <br>
                     <span style="font-weight: bold;">총 포인트 :</span> 
                  <span style="color: red; font-weight: bold;"><fmt:formatNumber value="${requestScope.sumMap.SUMTOTALPOINT}" pattern="###,###" /></span> POINT 
                 </td>
                 <td colspan="3" align="center">
                  <button type="button" class="btn btn-outline-dark btn-sm mr-3" onclick="goOrder()">주문하기</button>
                    <a class="btn btn-outline-dark btn-sm" href="<%=ctxPath %>/shop/mallHomeMore.up" role="button">계속쇼핑</a>
                 </td>
            </tr>
            
         </tbody>
      </table> 
   </div>
   
    <div>
        <p id="order_error_msg" class="text-center text-danger font-weight-bold h4"></p>
    </div>
    
    <%-- CSS 로딩화면 구현한것--%>
    <div style="display: flex; position: absolute; top: 30%; left: 37%; border: solid 0px blue;">
      <div class="loader" style="margin: auto"></div>
   </div>
    
 </div>
    
 
  