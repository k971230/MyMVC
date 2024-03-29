<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<%
   String ctxPath = request.getContextPath();
    //     /MyMVC     
%>


<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" /> 
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script>

<style type="text/css">
          
   li {margin-bottom: 10px;} 
   
   div#viewComments {width: 80%;
                      margin: 1% 0 0 0; 
                     text-align: left;
                     max-height: 300px;
                     overflow: auto;
                     /* border: solid 1px red; */
   }
   
   span.markColor {color: #ff0000; }
   
   div.customDisplay {display: inline-block;
                      margin: 1% 3% 0 0;
   }
                   
   div.spacediv {margin-bottom: 5%;}
   
   div.commentDel {font-size: 8pt;
                   font-style: italic;
                   cursor: pointer; }
   
   div.commentDel:hover {background-color: navy;
                         color: white;   }
   
   
   /* ~~~~ 일반적으로 태블릿 PC 가로 및 일반적으로 데스크탑 PC 에서만 CSS transform 을 사용하여 3D 효과를 주는 flip-card 를 만들어 보기 시작 ~~~~ */
   @media screen and (min-width:1024px){
      
      .flip-card {
            background-color: transparent; /* 투명 */
            perspective: 2000px;  /* perspective는 3D 환경을 만들때 사용하는 것으로서 원근감을 주는 것이다. 
                               이 값이 작으면 작을 수록 보고있는 사람의 위치를 더 가까이에서 보는 것으로 처리하므로 엘리먼트(요소)가 커 보이게 된다.  
                               이 값이 크면 클수록 보고있는 사람의 위치를 더 멀리 떨어져서 보는 것으로 처리하므로 엘리먼트(요소)가 작게 보이게 된다. */ 
      }
      
      .flip-card-inner {
            position: relative;
            width: 100%;  
            height: 100%; 
            text-align: center;
         /* transition: transform 2.6s; */ /* 요소(엘리먼트)를 transform(변형) 시키는데 걸리는 시간(단위는 초) 2.6초 */
            transition: transform 0.6s;    /* 요소(엘리먼트)를 transform(변형) 시키는데 걸리는 시간(단위는 초) 0.6초 */
            transform-style: preserve-3d;  /* 요소(엘리먼트)의 자식요소들(엘리먼트들)을 3D 공간에 배치 시킨다. */
         /* box-shadow: 0 4px 8px 0 rgba(0,0,0,1.0); */ /* rgba(빨강, 초록, 파랑, 투명도) */
            box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
      }
      
      .flip-card:hover .flip-card-inner {
        /* transform: rotateX(540deg); */  /* transform 은 요소(엘리먼트)를 변형시키는 것이다.
                                                                                 요소를 회전(rotate), 확대 또는 축소(scale), 기울이기(skew), 이동(translate) 효과를 부여할 수 있다. 
                                                                                 이를 통해 CSS 시각적 서식 모델의 좌표 공간을 변경한다.
                                              transform 이 지원되는 웹브라우저는 IE는 버전 10 이상부터 지원한다.
                                              
                                              rotateX는 x축을 기준으로 요소(엘리먼트)를 회전시키는 것이다. 
                                              1회전이 360deg 이므로 540deg 는 1바퀴 반을 회전시키는 것이다.*/
                                              
            transform: rotateY(180deg);    /* rotateY는 y축을 기준으로 회전한다. 180deg 반바퀴를 도는 것이다.*/
      }
      
      .flip-card-front, .flip-card-back {
        /* position: static; */
        /* position: relative;*/
           position: absolute;  /* .flip-card-front 엘리먼트(앞면)와  .flip-card-back 엘리먼트(뒷면)가 서로 겹쳐야 하므로 
                                   position 을 반드시 absolute; 로  주어야 한다. */
           width: 100%;  
           height: 100%; 
        /* backface-visibility: visible; */ /* backface-visibility 을 생략하더라도 기본값은 visible 이다. */
           backface-visibility: hidden;     /* 3D Transform에서 요소의 뒷면을 숨기는 역할을 한다.
                                                                                   이것을 hidden 처리하지 않으면 앞면/뒷면이 함께 보이기 때문에 이상하게 나오게 된다. */
      }
      
      .flip-card-front {
           background-color: #bbb;
           color: black;
           z-index: 2; /* position 속성을 이용하다 보면 엘리먼트(요소)를 겹치게 놓게될 수 있다. 
                                         이때 엘리먼트(요소)들의 수직 위치(쌓이는 순서)를 z-index 속성으로 정한다. 
                                         값은 정수이며, 숫자가 클 수록 위로 올라오고, 숫자가 작을 수록 아래로 내려간다. */
      }
      
      .flip-card-back {
        /* background-color: #2980b9; 파랑 */
           background-color: #ff8080; /* 빨강 */
           color: white;
        /* transform: rotateX(540deg); */   /* transform 은 IE는 버전 10 이상부터 지원한다.
                                               rotateX는 x축을 기준으로 요소(엘리먼트)를 회전시킨다. 
                                               1회전이 360deg 이므로 540deg 는 1바퀴 반을 회전시키는 것이다.*/
           transform: rotateY(180deg);      /* rotateY는 y축을 기준으로 요소(엘리먼트)를 회전시킨다. 
                                               180deg 반바퀴를 회전시키는 것이다.*/
           z-index: 1; /* position 속성을 이용하다 보면 엘리먼트(요소)를 겹치게 놓게될 수 있다. 
                                           이때 엘리먼트(요소)들의 수직 위치(쌓이는 순서)를 z-index 속성으로 정한다. 
                                           값은 정수이며, 숫자가 클 수록 위로 올라오고, 숫자가 작을 수록 아래로 내려간다. */
      }

   }   
   /* ~~~~ 일반적으로 태블릿 PC 가로 및 일반적으로 데스크탑 PC 에서만 CSS transform 을 사용하여 3D 효과를 주는 flip-card 를 만들어 보기 끝 ~~~~ */         
	
   /* ==== 추가이미지 캐러젤로 보여주기 시작 ==== */
      .carousel-inner .carousel-item.active,
      .carousel-inner .carousel-item-next,
      .carousel-inner .carousel-item-prev {
        display: flex;
      }
      
      .carousel-inner .carousel-item-right.active,
      .carousel-inner .carousel-item-next {
      /* transform: translate(25%, 0); */
      /* 또는 */ 
         transform: translateX(25%);
          
      /* transform: translate(0, 25%);
         또는 
         transform: translateY(25%); */ 
      }
      
      .carousel-inner .carousel-item-left.active, 
      .carousel-inner .carousel-item-prev {
         transform: translateX(-25%); 
      }
        
      .carousel-inner .carousel-item-right,
      .carousel-inner .carousel-item-left{ 
         transform: translateX(0); 
      }
   /* ==== 추가이미지 캐러젤로 보여주기 끝 ==== */   
	
	/* -- CSS 로딩화면 구현 시작(bootstrap 에서 가져옴) 시작 -- */   
   .loader {
  /* border: 16px solid #f3f3f3; */
     border: 12px solid #f3f3f3;
     border-radius: 50%;
  /* border-top: 16px solid #3498db; */
     border-top: 12px dotted blue;
     border-right: 12px dotted green;
     border-bottom: 12px dotted red;
     border-left: 12px dotted pink;
      
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
   
</style>

<script type="text/javascript" src="<%= ctxPath%>/js/myshop/categoryListJSON.js"></script>

<script type="text/javascript">

   let check = '0';
   let isOrderOK = false;
   // 로그인한 사용자가 해당 제품을 구매한 상태인지 구매하지 않은 상태인지 알아오는 용도.
   // isOrderOK 값이 true 이면   로그인한 사용자가 해당 제품을 구매한 상태이고,
   // isOrderOK 값이 false 이면  로그인한 사용자가 해당 제품을 구매하지 않은 상태로 할 것임.

   $(document).ready(function() {
	   
	   // ======= 추가이미지 캐러젤로 보여주기(Bootstrap Carousel 4개 표시 하되 1번에 1개 진행) 시작 ======= //
       $('div#recipeCarousel').carousel({
           interval : 2000  <%-- 2000 밀리초(== 2초) 마다 자동으로 넘어가도록 함(2초마다 캐러젤을 클릭한다는 말이다.) --%>
       });

       $('div.carousel div.carousel-item').each(function(index, elmt){
          <%--
               console.log($(elmt).html());
          --%>    
          <%--      
              <img class="d-block col-3 img-fluid" src="/MyMVC/images/berkelekle단가라포인트033.jpg">
              <img class="d-block col-3 img-fluid" src="/MyMVC/images/berkelekle덩크043.jpg">
              <img class="d-block col-3 img-fluid" src="/MyMVC/images/berkelekle트랜디053.jpg">
              <img class="d-block col-3 img-fluid" src="/MyMVC/images/berkelekle디스트리뷰트063.jpg">
          --%>
          
           let next = $(elmt).next();      <%--  다음엘리먼트    --%>
       <%-- console.log(next.length); --%>  <%--  다음엘리먼트개수 --%>
       <%--  1  1  1  0   --%>
       
       <%-- console.log("다음엘리먼트 내용 : " + next.html()); --%>
       <%--     
           다음엘리먼트 내용 : <img class="d-block col-3 img-fluid" src="/MyMVC/images/berkelekle덩크043.jpg">
           다음엘리먼트 내용 : <img class="d-block col-3 img-fluid" src="/MyMVC/images/berkelekle트랜디053.jpg">
           다음엘리먼트 내용 : <img class="d-block col-3 img-fluid" src="/MyMVC/images/berkelekle디스트리뷰트063.jpg">
           다음엘리먼트 내용 : undefined
       --%>    
             if (next.length == 0) { <%-- 다음엘리먼트가 없다라면 --%>
               <%--           
                 console.log("다음엘리먼트가 없는 엘리먼트 내용 : " + $(elmt).html());
                --%>  
                <%-- 
                     다음엘리먼트가 없는 엘리먼트 내용 : <img class="d-block col-3 img-fluid" src="/MyMVC/images/berkelekle디스트리뷰트063.jpg">
                --%>
             
             //  next = $('div.carousel div.carousel-item').eq(0);
             //  또는   
             //   next = $(elmt).siblings(':first'); <%-- 해당엘리먼트의 형제요소중 해당엘리먼트를 제외한 모든 형제엘리먼트중 제일 첫번째 엘리먼트 --%>
             //  또는 
                 next = $(elmt).siblings().first(); <%-- 해당엘리먼트의 형제요소중 해당엘리먼트를 제외한 모든 형제엘리먼트중 제일 첫번째 엘리먼트 --%>
                 <%-- 
                      선택자.siblings() 는 선택자의 형제요소(형제태그)중 선택자(자기자신)을 제외한 나머지 모든 형제요소(형제태그)를 가리키는 것이다.
                      :first   는 선택된 요소 중 첫번째 요소를 가리키는 것이다.
                      :last   는 선택된 요소 중 마지막 요소를 가리키는 것이다. 
                      참조사이트 : https://stalker5217.netlify.app/javascript/jquery/
                      
                      .first()   선택한 요소 중에서 첫 번째 요소를 선택함.
                      .last()   선택한 요소 중에서 마지막 요소를 선택함.
                      참조사이트 : https://www.devkuma.com/docs/jquery/%ED%95%84%ED%84%B0%EB%A7%81-%EB%A9%94%EC%86%8C%EB%93%9C-first--last--eq--filter--not--is-/ 
                 --%> 
             }
             
             $(elmt).append(next.children(':first-child').clone());
             <%-- next.children(':first-child') 은 결국엔 img 태그가 되어진다. --%>
             <%-- 선택자.clone() 은 선택자 엘리먼트를 복사본을 만드는 것이다 --%>
             <%-- 즉, 다음번 클래스가 carousel-item 인 div 의 자식태그인 img 태그를 복사한 img 태그를 만들어서 
                  $(elmt) 태그속에 있는 기존 img 태그 뒤에 붙여준다. --%>
             
             for(let i=0; i<2; i++) { // 남은 나머지 2개를 위처럼 동일하게 만든다.
                 next = next.next(); 
                 
                 if (next.length == 0) {
                 //   next = $(elmt).siblings(':first');
                 //  또는
                    next = $(elmt).siblings().first();
                  }
                 
                 $(elmt).append(next.children(':first-child').clone());
             }// end of for--------------------------
           
        //   console.log(index+" => "+$(elmt).html()); 
         
         }); // end of $('div.carousel div.carousel-item').each(function(index, elmt)----
        // ======= 추가이미지 캐러젤로 보여주기(Bootstrap Carousel 4개 표시 하되 1번에 1개 진행) 끝 ======= //

        
     $("p#order_error_msg").css('display','none'); // 코인잔액 부족시 주문이 안된다는 표시해주는 곳. 
   
     $("div.loader").hide(); // CSS 로딩화면 감추기
     
     goLikeDislikeCount(); // 좋아요, 싫어요 갯수를 보여주도록 하는 것이다. (페이지가 로딩되자마자 표시가 되게 하기 위해 호출)

     goReviewListView();  // 제품 구매후기를 보여주는 것. (페이지가 로딩되자마자 표시가 되게 하기 위해 호출)
     
      /////////////////////////////////////////////////////////////////////////
      // === 로그인한 사용자가 해당 제품을 구매한 상태인지 구매하지 않은 상태인지 먼저 알아온다 === // 
     
      
      if ('${sessionScope.loginuser.userid}' != ''){
	      $.ajax({
	           url:"<%= ctxPath%>/shop/isOrder.up",
	            type:"GET",
	            data:{"fk_pnum":"${requestScope.pvo.pnum}"
	               ,"fk_userid":"${sessionScope.loginuser.userid}"},
	            dataType:"JSON",
	         
	         // async:false,   // 동기처리
	         // async:true,    // 비동기처리(기본값임) 
	         
	            success:function(json){
	            //	console.log("~~ 확인용 : ", JSON.stringify(json) );
	            	// ~~ 확인용 : {"isOrder":true}   해당 제품을 구매한 경우
	            	// ~~ 확인용 : {"isOrder":false}  해당 제품을 구매하지 않은 경우
	            	
	               isOrderOK = json.isOrder; 
	               // json.isOrder 값이 true  이면 로그인한 사용자가 해당 제품을 구매한 경우이고
	               // json.isOrder 값이 false 이면 로그인한 사용자가 해당 제품을 구매하지 않은 경우이다.
	            },
	            error: function(request, status, error){
	               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	            }
	     });
      }
     //////////////////////////////////////////////////////////////////////////
     
     
     $("input#spinner").spinner( {
         spin: function(event, ui) {
            if(ui.value > 100) {
               $(this).spinner("value", 100);
               return false;
            }
            else if(ui.value < 1) {
               $(this).spinner("value", 1);
               return false;
            }
         }
      } );// end of $("input#spinner").spinner({});----------------    
      
      
   // === 주문개수가 변경되어지면 총주문액 변경해주기 시작 === //
      let jumun_cnt = Number($("input#spinner").val());
      let totalSaleprice = Number("${requestScope.pvo.saleprice}") * jumun_cnt;
      $("span#totalSaleprice").html(totalSaleprice.toLocaleString('en')+"원");
      
      $(document).on("spinstop", $("input#spinner"), function(){ // 이벤트, 선택자, 함수
         jumun_cnt = Number($("input#spinner").val());
         totalSaleprice = Number("${requestScope.pvo.saleprice}") * jumun_cnt;
         $("span#totalSaleprice").html(totalSaleprice.toLocaleString('en')+"원"); 
      });
      // === 주문개수가 변경되어지면 총주문액 변경해주기 끝 === //
      
      
      
   // **** 제품후기 쓰기(로그인하여 실제 해당 제품을 구매했을 때만 딱 1번만 작성할 수 있는 것. 제품후기를 삭제했을 경우에는 다시 작성할 수 있는 것임.) ****// 
	$("button#btnCommentOK").click(function(){
		
		if(${empty sessionScope.loginuser}) {
			alert("제품사용 후기를 작성하시려면 먼저 로그인하셔야 합니다.");
			return; // 종료
		}
		
		if(!isOrderOK) { // 해당 제품을 구매하지 않은 경우라면
			alert("${requestScope.pvo.pname} 제품을 구매하셔야만 후기작성이 가능합니다.");
		}
		else {
			const review_contents = $("textarea[name='contents']").val().trim();
			
			if(review_contents == "") {
				alert("제품후기 내용을 입력하세요!");
				$("textarea[name='contents']").val(""); // 공백제거
				return; // 종료
			}

			// >>> 보내야할 데이터를 선정하는 첫번째 방법 <<<
		<%--	
			const queryString = {"fk_userid":"${sessionScope.loginuser.userid}"
							   , "fk_pnum":"${requestScope.pvo.pnum}"
							   , "contents":$("textarea[name='contents']").val()};
		--%>	
			// >>> 보내야할 데이터를 선정하는 첫번째 방법 <<<
			// jQuery 에서 사용하는 것으로써,
			// form태그의 선택자.serialize(); 을 해주면 form 태그내의 모든 값들을 name 속성값을 키값으로 만들어서 보내준다.
			const queryString = $("form[name='commentFrm']").serialize();
			/*
	            queryString 은 아래와 같이 되어진다.
	            
	            {"contents":$("textarea[name='contents']").val(),
	            "fk_userid":$("input[name='fk_userid']").val(),
	            "fk_pnum":$(input[name='fk_pnum']).val()}
        	*/
			
        	
        	// HTML 코드에서 요소를 선택합니다.
        	var elements = document.getElementsByClassName('customDisplay namecheck');

        	// 선택된 요소의 갯수를 확인합니다.
        	var numberOfElements = elements.length;

        	// 반복문을 통해 각 요소에 대해 작업을 수행합니다.
        	for (var i = 0; i < numberOfElements; i++) {
        	    // 현재 반복 중인 요소에 대한 작업을 수행합니다.
        	    var currentElement = elements[i];
        	    
        	    // 현재 요소의 텍스트를 가져옵니다.
        	    var elementText = currentElement.textContent || currentElement.innerText;
				
				// 만약 현재 요소의 텍스트가 '${sessionScope.loginuser.userid}'와 같다면
        	    if (elementText.trim() == '${sessionScope.loginuser.name}') {
        	    	check = '1';
        	    }
        	    else{
        	    	
        	    }
        	
        	}
        	if(check != '1'){
        		
        		$.ajax({
 	               url:"<%= ctxPath%>/shop/reviewRegister.up",
 	               type:"POST",
 	               data:queryString,
 	               dataType:"JSON",
 	               success:function(json){ 
 	                  console.log(JSON.stringify(json));
 	                  /*
 	                     {"n":1} 또는 {"n":-1} 또는 {"n":0}
 	                  */
 	                  
 	                  if(json.i == 1) {
 	                     // 제품후기 등록(insert)이 성공했으므로 제품후기글을 새로이 보여줘야(select) 한다.
 						 goReviewListView(); // 제품후기글을 보여주는 함수 호출하기 
 	                  }
 	                  else if(json.i == -1)  {
 	                  // 동일한 제품에 대하여 동일한 회원이 제품후기를 2번 쓰려고 경우 unique 제약에 위배됨 
 	                  // alert("이미 후기를 작성하셨습니다.\n작성하시려면 기존의 제품후기를\n삭제하시고 다시 쓰세요.");
 	                     swal("이미 후기를 작성하셨습니다.\n작성하시려면 기존의 제품후기를\n삭제하시고 다시 쓰세요.");
 	                  }
 	                  else  {
 	                     // 제품후기 등록(insert)이 실패한 경우 
 	                     alert("제품후기 글쓰기가 실패했습니다.");
 	                  }
 	                  
 	                  $("textarea[name='contents']").val("").focus();
 	               },
 	               error: function(request, status, error){
 	                  alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
 	               }
 	            });
        		
        	}
        	else{
        		
        		alert("이미 후기등록을 하셨네요");
        		
        	}
        	
        	
        	
        
        	
		}// end of if ~ else
		
	});// end of $("button#btnCommentOK").click(function(){})--------- 
      
   }); // end of $(document).ready();------------------------------
   
   
   // Function Declaration 
   
   // **** 특정제품에 대한 좋아요 등록하기 **** // 
   function golikeAdd(pnum) {
   
	 if(${empty sessionScope.loginuser}) {
		 alert("좋아요 하시려면 먼저 로그인하셔야 합니다.");
		 return; // 종료
	 }
      
	if(!isOrderOK) { // 해당 제품을 구매하지 않은 경우라면
		alert("${requestScope.pvo.pname} 제품을 구매하셔야만 좋아요 투표가 가능합니다.");
	}
	else { // 해당 제품을 구매한 경우라면
		$.ajax({
            url:"<%= ctxPath%>/shop/likeAdd.up",
            type:"post",
            data:{"pnum":pnum,
                 "userid":"${sessionScope.loginuser.userid}"},
            dataType:"json", 
            success:function(json) {
         //    console.log(JSON.stringify(json));
                // {"msg":"해당제품에\n 좋아요를 클릭하셨습니다."}
                // 또는
                // {"msg":"이미 좋아요를 클릭하셨기에\n 두번 이상 좋아요는 불가합니다."}
                  
             //  alert(json.msg);
             
             if(json.n == 0){
            	 alert("안댐");
             }else{
            	 alert("성공");
             }
                 
                 goLikeDislikeCount();
            },
            error: function(request, status, error){
               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
         });
	}
		
   }// end of golikeAdd(pnum)---------------------------
   
   
   // **** 특정제품에 대한 싫어요 등록하기 **** //
   function godisLikeAdd(pnum) {
 
	 if(${empty sessionScope.loginuser}) {
		 alert("싫어요 하시려면 먼저 로그인하셔야 합니다.");
		 return; // 종료
	 }
    
	 if(!isOrderOK) { // 해당 제품을 구매하지 않은 경우라면
			alert("${requestScope.pvo.pname} 제품을 구매하셔야만 싫어요 투표가 가능합니다.");
		}
		else { // 해당 제품을 구매한 경우라면
			$.ajax({
	            url:"<%= ctxPath%>/shop/dislikeAdd.up",
	            type:"post",
	            data:{"pnum":pnum,
	                 "userid":"${sessionScope.loginuser.userid}"},
	            dataType:"json", 
	            success:function(json) {
	          	
	            	if(json.n == 0){
	               	 alert("안댐");
	                }else{
	               	 alert("성공");
	                }
	                   goLikeDislikeCount();
	            },
	            error: function(request, status, error){
	               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	            }
	         });
		}
	 
   }// end of golikeAdd(pnum)---------------
   
   
   // **** 특정 제품에 대한 좋아요, 싫어요 갯수를 보여주기 **** //
   function goLikeDislikeCount() {

	   $.ajax({
	         url:"<%= ctxPath%>/shop/likeDislikeCount.up",
	      // type:"GET",
	         data:{"pnum":"${requestScope.pvo.pnum}"},
	         dataType:"JSON", 
	         success:function(json) {
	         // console.log(JSON.stringify(json));
	            // {"likecnt":1, "dislikecnt":0}
	           
	            $("span#likeCnt").html(json.likecnt);
	            $("span#dislikeCnt").html(json.dislikecnt);
	         },
	         error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	         }
	   });    
      
   }// end of function goLikeDislikeCount()-------------------
   
   
   // 특정 제품의 제품후기글들을 보여주는 함수
   function goReviewListView() {

      $.ajax({
    	url:"<%= ctxPath%>/shop/reviewList.up",
    	type: "get",
    	data:{"fk_pnum":"${requestScope.pvo.pnum}"},
    	dataType: "json",
    	success: function(json){
    	//	console.log(JSON.stringify(json));
    	/*
    		[{"contents":"정말로 좋은 옷이네요","name":"엄정화","writeDate":"2023-11-06 09:06:20","review_seq":5,"userid":"eomjh"},{"contents":"좋습니당","name":"김진영","writeDate":"2023-11-03 15:26:38","review_seq":1,"userid":"kimjy"}]
    	
    		또는
    		
    		[]
    	*/
    	
			let v_html = "";
            
            if (json.length > 0) {    
               $.each(json, function(index, item){ 
                  let writeuserid = item.userid;
                  let loginuserid = "${sessionScope.loginuser.userid}";
                                 
                  v_html += "<div id='review"+index+"'><span class='markColor'>▶</span>&nbsp;"+item.contents+"</div>" // div마다 고유한 값을 주었다. 수정, 삭제를 위해
                          + "<div class='customDisplay namecheck'>"+item.name+"</div>"      
                          + "<div class='customDisplay'>"+item.writeDate+"</div>";
                   
                   if( loginuserid == "") { 
                      // 로그인을 안한 경우 
                      v_html += "<div class='customDisplay spacediv'>&nbsp;</div>";
                   }      
                   else if( loginuserid != "" && writeuserid != loginuserid ) { 
                      // 로그인을 했으나 후기글이 로그인한 사용자 쓴 글이 아니라 다른 사용자 쓴 후기글 이라면  
                      v_html += "<div class='customDisplay spacediv'>&nbsp;</div>";
                   }    
                   else if( loginuserid != "" && writeuserid == loginuserid ) {
                      // 로그인을 했고 후기글이 로그인한 사용자 쓴 글 이라면
                      v_html += "<div class='customDisplay spacediv commentDel' onclick='delMyReview("+item.review_seq+")'>후기삭제</div>"; 
                      v_html += "<div class='customDisplay spacediv commentDel commentUpdate' onclick='updateMyReview("+index+","+item.review_seq+")'>후기수정</div>"; 
                   }
               } ); 
            }// end of if -----------------------
            
            else {
            	v_html += "<div>등록된 상품후기가 없습니다.</div>";
            }// end of else ---------------------
            
            $("div#viewComments").html(v_html);
    	
    	
    	},
    	error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
      });
      
   }// end of function goReviewListView()--------------
   
   
   // 특정 제품의 제품후기를 삭제하는 함수
   function delMyReview(review_seq) {
      
      if(confirm("정말로 제품후기를 삭제하시겠습니까?")) {
    	  $.ajax({
    		  url:"<%= ctxPath%>/shop/reviewDel.up",
    		  type:"post",
    		  data:{"review_seq":review_seq},
    		  dataType:"json",
    		  success:function(json){
    			//	console.log(JSON.stringify(json));
    			//  {"n":1} 또는 {"n":0}
    			
    				if(json.n == 1) {
    					alert("제품후기를 삭제했습니다.");
    					goReviewListView(); // 특정 제품의 제품후기글들을 보여주는 함수 호출하기
    				}
    				else {
    					alert("제품후기 삭제에 실패했습니다.");
    				}
    				
    		  },
    		  error: function(request, status, error){
	   	          alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	   	      }
    	  });
      }
      
   }// end of function delMyReview(review_seq) {}--------------------------  
   
   
   // 특정 제품의 제품후기를 수정하는 함수
   function updateMyReview(index, review_seq) {
	   
	   const origin_elmt = $("div#review"+index).html(); // 원래의 제품후기 엘리먼트
	// alert(origin_elmt);
	// <span class="markColor">▶</span>&nbsp;~(ㅇㅅㅇ~) 다시 후기 써용 옷 좋습니다 (~ㅇㅅㅇ)~
	   
	// alert($("div#review"+index).text());
	// ▶ ~(ㅇㅅㅇ~) 다시 후기 써용 옷 좋습니다 (~ㅇㅅㅇ)~
	   
	   const review_contents = $("div#review"+index).text().substring(2); // 원래의 제품후기 내용
	// alert(review_contents);
	// ~(ㅇㅅㅇ~) 다시 후기 써용 옷 좋습니다 (~ㅇㅅㅇ)~
	
	   $("div.commentUpdate").hide(); // "후기수정" 글자 감추기
	   
	   // "후기수정" 을 위한 엘리먼트 만들기
	   let v_html = "<textarea id='edit_textarea' style='font-size: 12pt; width: 40%; height: 50px;'>"+review_contents+"</textarea>";
       v_html += "<div style='display: inline-block; position: relative; top: -20px; left: 10px;'><button type='button' class='btn btn-sm btn-outline-secondary' id='btnReviewUpdate_OK'>수정완료</button></div>"; 
       v_html += "<div style='display: inline-block; position: relative; top: -20px; left: 20px;'><button type='button' class='btn btn-sm btn-outline-secondary' id='btnReviewUpdate_NO'>수정취소</button></div>";
       
       // 원래의 제품후기 엘리먼트에 위에서 만든 "후기수정" 을 위한 엘리먼트로 교체하기
       $("div#review"+index).html(v_html);
       
       // 수정취소 버튼 클릭시 
       $(document).on("click", "button#btnReviewUpdate_NO", function(){
    	   $("div#review"+index).html(origin_elmt);  // 원래의 제품후기 엘리먼트로 복원하기
    	   $("div.commentUpdate").show(); // "후기수정" 글자 보여주기
       });
       
       // 수정완료 버튼 클릭시 
       $(document).on("click", "button#btnReviewUpdate_OK", function(){
    	   
    	// alert(review_seq); // 수정할 제품후기 번호
    	// alert($("textarea#edit_textarea").val()); // 수정할 제품후기 내용
    	   
    	   $.ajax({
    		  url:"<%= ctxPath%>/shop/reviewUpdate.up",
     		  type:"post",
     		  data:{"review_seq":review_seq
     			   ,"contents":$("textarea#edit_textarea").val()},
     		  dataType:"json",
     		  success:function(json){
     			//	console.log(JSON.stringify(json));
     			//  {"n":1} 또는 {"n":0}
     			
     				if(json.n == 1) {
     					goReviewListView(); // 특정 제품의 제품후기글들을 보여주는 함수 호출하기
     				}
     				else {
     					alert("제품후기 수정에 실패했습니다.");
     					goReviewListView(); // 특정 제품의 제품후기글들을 보여주는 함수 호출하기
     				}
     				
     		  },
     		  error: function(request, status, error){
 	   	          alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
 	   	      }
    	   });
    	   
       });
       
       
   }// end of function updateMyReview(index, review_seq)------------------- 
   
   
   
   
   
   // *** 장바구니 담기 ***//
   function goCart() {
   
      // === 주문량에 대한 유효성 검사하기 === //
      const frm = document.cartOrderFrm;
      
      const regExp = /^[0-9]+$/;  // 숫자만 체크하는 정규표현식
      let oqty = frm.oqty.value;
      const bool = regExp.test(oqty);
      
      if(!bool) {
         // 숫자 이외의 값이 들어온 경우 
         alert("주문갯수는 1개 이상이어야 합니다.");
         frm.oqty.value = "1";
         frm.oqty.focus();
         return; // 종료 
      }
      
      // 문자형태로 숫자로만 들어온 경우
      oqty = parseInt(oqty);
      if(oqty < 1) {
         alert("주문갯수는 1개 이상이어야 합니다.");
         frm.oqty.value = "1";
         frm.oqty.focus();
         return; // 종료 
      }
      
      if(oqty > Number(${requestScope.pvo.pqty})) {
          // 주문개수가 잔고개수 보다 클 경우
          alert("주문개수가 잔고개수 보다 더 커서 진행할 수 없습니다.");
          frm.oqty.value = "1";
          frm.oqty.focus();
          return; // 종료 
       }
      
      // 주문개수가 1개 이상인 경우
      frm.method = "POST";
      frm.action = "<%= ctxPath%>/shop/cartAdd.up";
      frm.submit();
   
   }// end of function goCart()-------------------------
   
   
	// *** 바로주문하기 (form 태그를 사용하지 않고 Ajax 를 사용하여 처리해 보겠습니다.) *** // 
	function goOrder() {
    	  
		if( ${not empty sessionScope.loginuser} ) {
			const currentCoin = Number("${sessionScope.loginuser.coin}"); // 현재코인액
			const totalSaleprice = Number("${requestScope.pvo.saleprice}") * Number($("input#spinner").val()); // 제품총판매가
			
			if(currentCoin < totalSaleprice) {
				$("p#order_error_msg").html("코인잔액이 부족하므로 주문이 불가합니다.<br>총주문액: " +totalSaleprice.toLocaleString('en')+"원, 현재코인액: " + currentCoin.toLocaleString('en')+"원" ).css('display','');
				return; // 종료
			}
			
			else {
				$("p#order_error_msg").css('diplay', 'none');
				
				if(confirm("총주문액 : " + totalSaleprice.toLocaleString('en') + "원 주문하시겠습니까?")); {
					
					$("div.loader").show(); // CSS 로딩화면 보여주기
					
					$.ajax({
						url:"<%= ctxPath%>/order/orderAdd.up",
						type:"post",
						data:{"n_sum_totalPrice":Number("${requestScope.pvo.saleprice}")*Number($("input#spinner").val()),
	        				  "n_sum_totalPoint":Number("${requestScope.pvo.point}")*Number($("input#spinner").val()),
	        				  "str_pnum_join":"${requestScope.pvo.pnum}",
	        				  "str_oqty_join":$("input#spinner").val(),
	        				  "str_totalPrice_join":Number("${requestScope.pvo.saleprice}")*Number($("input#spinner").val())
						},
						dataType:"json",
						success:function(json){ // json ==> {"isSuccess":1} 또는 {"isSuccess":0}
	        				if(json.isSuccess == 1) {
	                            location.href="<%= ctxPath%>/shop/orderList.up";
	                        }
	                        else {
	                           location.href="<%= ctxPath%>/shop/orderError.up";
	                        }
						},
						error: function(request, status, error){
		 		               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		 		        }
					});
				}
			}
		}
		else {
			alert("주문을 하시려면 먼저 로그인하세요!");
			// 만약에 로그인을 해주는 페이지가 따로 존재한다면 로그인을 해주는 페이지로 이동시켜줘야한다.
		}
		
	} // end of function goOrder()----------------------
   
   
   let popup; // 추가이미지 파일을 클릭했을때 기존에 띄어진 팝업창에 추가이미지가 또 추가되지 않도록 하기 위해 밖으로 뺌.   
   
   function openPopup(src) {
	   
	// alert(src);
	
	if(popup != undefined) { // 기존에 띄워진 팝업창이 있을 경우
		popup.close(); // 팝업창을 먼저 닫도록 한다.
	}
	
	// 너비 800, 높이 480 인 팝업창을 화면 가운데 위치시키기
		 const width = 800;
		 const height = 480;
		 const left = Math.ceil( (window.screen.width - width)/2 );  // 정수로 만듬
		 const top = Math.ceil( (window.screen.height - height)/2 );	// 정수로 만듬
		 
		 popup = window.open("", "imgInfo",
		 		 	         `left=\${left}, top=\${top}, width=\${width}, height=\${height}`);
	    
	     popup.document.writeln("<html>");
	     popup.document.writeln("<head><title>제품이미지 확대보기</title></head>");
	     popup.document.writeln("<body align='center'>");
	     popup.document.writeln("<img src='"+src+"' />");
	     popup.document.writeln("<br><br><br>");
	     popup.document.writeln("<button type='button' onclick='window.close()'>팝업창닫기</button>");
	     popup.document.writeln("</body>");
	     popup.document.writeln("</html>");
	 
   }// end of function openPopup(src)------------------
   
   
   function modal_content(img) {
	  // alert(img.src);
	  $("div#add_image_modal-body").html("<img class='d-blockimg img-fluid' src='"+img.src+"' />")
   }// end of function modal_content(img) 
   
</script>

<div style="width: 95%;">
   <div class="my-3">
      <p class="h4 text-center">&raquo;&nbsp;&nbsp;제품 상세 정보&nbsp;&nbsp;&laquo;</p>
   </div>
   
   <div class="row my-3 text-left">
      <div class="col-md-6">
          <div class="flip-card">
              <div class="flip-card-inner">         
              <div class="flip-card-front">
                   <img src="<%= ctxPath%>/images/${requestScope.pvo.pimage1}" class="img-fluid" style="width:100%;" />
               </div>
               <div class="flip-card-back">
                   <img src="<%= ctxPath%>/images/${requestScope.pvo.pimage2}" class="img-fluid" style="width: 100%;" />
               </div>
            </div>
         </div>
      </div>
      
      <div class="col-md-6 pl-5">
          <ul class="list-unstyled">
            <li><span style="color: red; font-size: 12pt; font-weight: bold;">${requestScope.pvo.spvo.sname}</span></li>
            <li>제품번호: ${requestScope.pvo.pnum}</li>
            <li>제품이름: ${requestScope.pvo.pname}</li>
            <li>제조회사: ${requestScope.pvo.pcompany}</li>
            <li>제품설명서: 
              <c:if test="${requestScope.pvo.prdmanual_orginFileName ne '없음'}">
                 <a href="<%= ctxPath%>/shop/fileDownload.up?pnum=${pvo.pnum}">${pvo.prdmanual_orginFileName}</a>
              </c:if>
              <c:if test="${requestScope.pvo.prdmanual_orginFileName eq '없음'}">
                 첨부파일없음
              </c:if>
              </li>
              <li>제품정가: <span style="text-decoration: line-through;"><fmt:formatNumber value="${requestScope.pvo.price}" pattern="###,###" />원</span></li>
              <li>제품판매가: <span style="color: blue; font-weight: bold;"><fmt:formatNumber value="${requestScope.pvo.saleprice}" pattern="###,###" />원</span></li>
              <li>할인율: <span style="color: maroon; font-weight: bold;">[${requestScope.pvo.discountPercent}% 할인]</span></li>
              <li>포인트: <span style="color: green; font-weight: bold;">${requestScope.pvo.point} POINT</span></li>
              <li>잔고갯수: <span style="color: maroon; font-weight: bold;">${requestScope.pvo.pqty} 개</span></li>          
              <li>총주문액: <span id="totalSaleprice" style="color: maroon; font-weight: bold;"></span></li>          
          </ul>
          <%-- ==== 장바구니 담기 또는 바로주문하기 폼 ==== --%>
          <form name="cartOrderFrm">       
             <ul class="list-unstyled mt-3">
                <li>
                    <label for="spinner">주문개수&nbsp;</label>
                    <input id="spinner" name="oqty" value="1" style="width: 110px;">
               </li>
               <li>
                  <button type="button" class="btn btn-secondary btn-sm mr-3" onclick="goCart()">장바구니담기</button>
                  <button type="button" class="btn btn-danger btn-sm" onclick="goOrder()">바로주문하기</button>
               </li>
            </ul>
            <input type="hidden" name="pnum" value="${requestScope.pvo.pnum}" />
         </form>   
              
      </div>
   </div>
   
   <%-- CSS 로딩화면 구현한것--%>
    <div style="display: flex; position: absolute; top: 30%; left: 37%; border: solid 0px blue;">
        <div class="loader" style="margin: auto"></div>
    </div>
   
   <%-- === 추가이미지 보여주기 시작 === --%>
   <c:if test="${not empty requestScope.imgList}">
      <%-- === 그냥 이미지로 보여주는 것 시작 === --%>
      <%--
      <div class="row">
        <c:forEach var="imgfilename" items="${requestScope.imgList}">
          <div class="col-md-6 my-3">
             <img src="<%= ctxPath%>/images/${imgfilename}" class="img-fluid" style="width:100%;" />
          </div>
        </c:forEach>
      </div>
      --%>
      <%-- === 그냥 이미지로 보여주는 것 끝 === --%>
      
      <%-- /////// 추가이미지 캐러젤로 보여주는 것 시작 /////// --%>
      <div class="row mx-auto my-auto">
           <div id="recipeCarousel" class="carousel slide w-100" data-ride="carousel">
               <div class="carousel-inner w-100" role="listbox">
                   <c:forEach var="imgfilename" items="${requestScope.imgList}" varStatus="status">
                      <c:if test="${status.index == 0}">
                         <div class="carousel-item active">
                       <%-- <img class="d-block col-3 img-fluid" src="<%= ctxPath%>/images/${imgfilename}" style="cursor: pointer;" onclick="openPopup('<%= ctxPath%>/images/${imgfilename}')" /> --%>
                            <img class="d-block col-3 img-fluid" src="<%= ctxPath%>/images/${imgfilename}" style="cursor: pointer;" data-toggle="modal" data-target="#add_image_modal_view" data-dismiss="modal" onclick="modal_content(this)" />
                         </div>
                      </c:if>
                      <c:if test="${status.index > 0}">
                         <div class="carousel-item">
                       <%-- <img class="d-block col-3 img-fluid" src="<%= ctxPath%>/images/${imgfilename}" style="cursor: pointer;" onclick="openPopup('<%= ctxPath%>/images/${imgfilename}')" /> --%>
                            <img class="d-block col-3 img-fluid" src="<%= ctxPath%>/images/${imgfilename}" style="cursor: pointer;" data-toggle="modal" data-target="#add_image_modal_view" data-dismiss="modal" onclick="modal_content(this)" />
                         </div>
                      </c:if>
                   </c:forEach>
               </div>
               <a class="carousel-control-prev" href="#recipeCarousel" role="button" data-slide="prev">
                   <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                   <span class="sr-only">Previous</span>
               </a>
               <a class="carousel-control-next" href="#recipeCarousel" role="button" data-slide="next">
                   <span class="carousel-control-next-icon" aria-hidden="true"></span>
                   <span class="sr-only">Next</span>
               </a>
           </div>
       </div>
      <%-- /////// 추가이미지 캐러젤로 보여주는 것 끝 /////// --%>
   </c:if>
   <%-- === 추가이미지 보여주기 끝 === --%>
   
   <div>
        <p id="order_error_msg" class="text-center text-danger font-weight-bold h4"></p>
    </div>
      
   <div class="jumbotron mt-5">
        
        <div class="text-left" style="margin-top: -4.5%;">
	        <p class="h4 bg-secondary text-white w-50">${requestScope.pvo.pname} 제품의 특징</p>
	        <p>${requestScope.pvo.pcontent}</p>   
		</div>
   
      <div class="row">
         <div class="col" style="display: flex">
               <h3 style="margin: auto">
                  <i class="fas fa-thumbs-up fa-2x" style="cursor: pointer;" onclick="golikeAdd('${requestScope.pvo.pnum}')"></i>
                  <span id="likeCnt" class="badge badge-primary"></span>
                </h3>
         </div>
         
         <div class="col" style="display: flex">
             <h3 style="margin: auto">
                <i class="fas fa-thumbs-down fa-2x" style="cursor: pointer;" onclick="godisLikeAdd('${requestScope.pvo.pnum}')"></i>
                <span id="dislikeCnt" class="badge badge-danger"></span>
             </h3>       
         </div>
      </div>
   
   </div>
   
   
   <%-- 내가 할 부분 --%>
   
   <div class="text-left">
	    <p class="h4 text-muted">${requestScope.pvo.pname} 제품 사용후기</p>
	    
	    <div id="viewComments">
	       <%-- 여기가 제품사용 후기 내용이 들어오는 곳이다. --%>
	    </div>
   </div>
     
    <div class="row">
        <div class="col-lg-10">
          <form name="commentFrm">
              <textarea name="contents" style="font-size: 12pt; width: 100%; height: 150px;"></textarea>
              <input type="hidden" name="fk_userid" value="${sessionScope.loginuser.userid}" />
              <input type="hidden" name="fk_pnum" value="${requestScope.pvo.pnum}" />
          </form>
       </div>
       <div class="col-lg-2" style="display: flex;">
          <button type="button" class="btn btn-outline-secondary w-100 h-100" id="btnCommentOK" style="margin: auto;"><span class="h5">후기등록</span></button>
       </div>
    </div>
   
   <%-- 내가 할 부분 --%>
   
</div>


<%-- ****** 추가이미지 보여주기 Modal 시작 ****** --%>
  <div class="modal fade" id="add_image_modal_view"> <%-- 만약에 모달이 안보이거나 뒤로 가버릴 경우에는 모달의 class 에서 fade 를 뺀 class="modal" 로 하고서 해당 모달의 css 에서 zindex 값을 1050; 으로 주면 된다. --%> 
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
      
        <!-- Modal header -->
        <div class="modal-header">
          <h4 class="modal-title">추가 이미지 원래크기 보기</h4>
          <button type="button" class="close idFindClose" data-dismiss="modal">&times;</button>
        </div>
        
        <!-- Modal body -->
        <div class="modal-body" id="add_image_modal-body">
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
          <button type="button" class="btn btn-danger idFindClose" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
<%-- ****** 추가이미지 보여주기 Modal 끝 ****** --%>

