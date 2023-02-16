<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="pageTitle" value="Product OrderSheet" />
<%@ include file="../common/head.jsp"%>
<c:set var="now" value="<%=new java.util.Date().getTime()%>" />

<!-- iamport.payment.js -->
<script src="https://cdn.iamport.kr/v1/iamport.js"></script>
<script>
	//도로명 주소 API
	document.domain = "localhost";
	
	function goPopup() {
	    var pop = window.open("../popup/jusoPopup","pop","width=570,height=420, scrollbars=yes, resizable=yes"); 
	}
	
	function jusoCallBack(zipNo, roadAddrPart1, addrDetail) {
		// 팝업페이지에서 주소입력한 정보를 받아서, 현 페이지에 정보를 등록합니다.
		document.forms[0].zipNo.value = zipNo;
		document.forms[0].roadAddr.value = roadAddrPart1;
		document.forms[0].addrDetail.value = addrDetail;
	}
	
	// 결제 API
  	IMP.init("imp46288786");
	
	function requestPay() {
		const payMethod = $("#pay_method").find("input[type=radio]:checked").val();
		const orderNum = "ORD_${product.id}_${now}";
		
		const ordName = $("input[name=ordName]").val();
		const ordCellphoneNum = $("input[name=ordCellphoneNum]").val();
		const ordEmail = $("input[name=ordEmail]").val();
		const recName = $("input[name=name]").val();
		const recCellphoneNum = $("input[name=cellphoneNum]").val();
		const recZipNo = $("input[name=zipNo]").val();
		const recRoadAddr = $("input[name=roadAddr]").val();
		
		if(ordName.length == 0 || ordCellphoneNum.length == 0 || ordEmail.length == 0) {
			alert("주문자 정보를 적어주세요.");
			return false;
		}
		
		if(recName.length == 0 || recCellphoneNum.length == 0 || recZipNo.length == 0 || recRoadAddr.length == 0) {
			alert("배송지 정보를 적어주세요.");
			return false;
		}
		
		IMP.request_pay({
			pg: "html5_inicis",
			pay_method: payMethod,
			merchant_uid: orderNum,
			name: "${product.productName}",
			amount: ${(product.productPrice * param.productCnt) + product.productDlvyPrice},
			buyer_name: ordName,
			buyer_tel: ordCellphoneNum,
			buyer_email: ordEmail,
		}, function (rsp) { // callback
			if (rsp.success) {
				$("input[name=orderNum]").val(orderNum)
				$("input[name=impUID]").val(rsp.imp_uid)
				
				$("#buyProduct").submit();
			} else {
				alert(rsp.error_msg);
				
				return false;
			}
		});
	}
  	
	$(function(){
		// email 부분 영어, 숫자, 특수문자(@_.-) 만 작성되게
		$("input[name=ordEmail]").on("input", function(event){
			if (!(event.keyCode >=37 && event.keyCode<=40)) { 
				let inputVal = $(this).val();
		
				$(this).val(inputVal.replace(/[^a-z0-9@_.-]/gi, ""));
			} 
		});
		// cellphoneNum 부분 숫자만 입력되게 자동으로 하이픈 삽입
		$("input[name=ordCellphoneNum], input[name=cellphoneNum], input[name=cellphoneNum2]").on("input", function(event){
			if (!(event.keyCode >=37 && event.keyCode<=40)) { 
				let inputVal = $(this).val();
				
				$(this).val(inputVal.replace(/[^0-9]/g, "").replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(\-{1,2})$/g, ""));
			} 
		});
		
		$("#ordToRec").click(function() {
			const ordName = $("input[name=ordName]").val();
			const ordCellphoneNum = $("input[name=ordCellphoneNum]").val();
			
			$("input[name=name]").val(ordName);
			$("input[name=cellphoneNum]").val(ordCellphoneNum);
		});
	})
</script>

<!-- 상단 정보 메뉴 -->
<div class="container mx-auto mt-6 flex items-center justify-between select-none">
	<div>
		<h2 class="text-2xl font-bold">주문/결제</h2>
	</div>
	
	<div class="text-xl breadcrumbs">
		<ul>
			<li><span class="text-gray-500">장바구니</span></li> 
			<li><span class="text-black font-bold">주문/결제</span></li>
			<li><span class="text-gray-500">완료</span></li> 
		</ul>
	</div>
</div>

<!-- 주문서 -->
<section class="mt-3">
	<div class="container mx-auto flex border">
		<table class="w-full">
			<colgroup>
		        <col width="40%">
		        <col width="15%">
		        <col width="15%">
		        <col width="12%">
		        <col>
		    </colgroup>
		    
		    <thead class="bg-gray-100">
		    	<tr>
		    		<th class="p-5">상품정보</th>
		    		<th class="p-5">판매자</th>
		    		<th class="p-5">배송비</th>
		    		<th class="p-5">수량</th>
		    		<th class="p-5">상품금액</th>
		    	</tr>
		    </thead>
		    <tbody>
		    	<tr>
		    		<td>
		    			<div class="productInfo relative">
		    				<a class="absolute top-2/4 left-12 -translate-y-2/4" href="/usr/product/view?id=${product.id }&storeId=${store.id}" target="_blank">
		    					<img class="w-24 h-24 mask mask-circle align-top" src="${rq.getImgUri('product', product.id, 'productImg', 1)}" />
		    				</a>
		    				<div class="productDesc flex flex-col pl-48 py-8">
		    					<div><span class="text-sm text-gray-500">[SB_STORE] ${store.storeName}</span></div>
		    					<div>
		    						<a href="/usr/product/view?id=${product.id }&storeId=${store.id}" target="_blank">
		    							<span class="text-base font-bold">${product.productName }</span>
		    						</a>
		    					</div>
		    				</div>
		    			</div>
		    		</td>
		    		
		    		<td>
		    			<div class="text-center">
		    				<a href="">${store.storeName}</a>
		    			</div>
		    		</td>
		    		
		    		<td>
		    			<div class="text-center">
		    				<span>${product.productDlvy == 0 ? "무료" : "유료" }</span>
		    				<span class="tooltip" data-tip="지역별 추가 배송비&#xa;제주도 3,000원 추가&#xa;제주도 외 도서산간 3,000원 추가"><i class="fa-solid fa-circle-question"></i></span>
			    			<c:if test="${product.productDlvy == 1}">
			    				<div><span class="text-sm"><fmt:formatNumber value="${product.productDlvyPrice}" pattern="#,###" />원</span></div>
			    			</c:if>
		    			</div>
		    		</td>
		    		
		    		<td>
		    			<div class="text-center">
		    				<span>${param.productCnt} 개</span>
		    			</div>
		    		</td>
		    		
		    		<td>
		    			<div class="text-center font-bold">
	    					<c:if test="${product.productDlvy == 0}">
		    					<fmt:formatNumber value="${product.productPrice * param.productCnt}" pattern="#,###" /><span class="text-base">원</span>
	    					</c:if>
	    					
	    					<c:if test="${product.productDlvy == 1}">
	    						<fmt:formatNumber value="${(product.productPrice * param.productCnt) + product.productDlvyPrice}" pattern="#,###" /><span class="text-base">원</span>
	    						<div><span class="text-sm">배송비 +<fmt:formatNumber value="${product.productDlvyPrice}" pattern="#,###" />원</span></div>
	    					</c:if>
		    			</div>
		    		</td>
		    	</tr>
		    </tbody>
		</table>
	</div>
	
	<div class="container mx-auto mt-10 p-10 border">
		<h3 class="font-bold text-xl select-none mb-5">주문자 정보</h3>
		<div class="addressArea form-control">
			<div class="flex items-center">
				<span class="inline-block w-28 mr-1">주문자 이름</span>
				<input type="text" name="ordName" placeholder="주문자 이름" class="input input-ghost text-lg border-gray-400 w-80" value="${rq.loginedMember.name }" />
			</div>
			<div class="flex items-center mt-2">
				<span class="inline-block w-28 mr-1">연락처</span>
				<input type="tel" maxlength="13" name="ordCellphoneNum" placeholder="연락처" class="input input-ghost text-lg border-gray-400 w-80" value="${rq.loginedMember.cellphoneNum }" />
			</div>
			<div class="flex items-center mt-2">
				<span class="inline-block w-28 mr-1">이메일</span>
				<input type="email" name="ordEmail" placeholder="이메일 주소" class="input input-ghost text-lg border-gray-400 w-80" value="${rq.loginedMember.email }" />
			</div>
		</div>
	</div>
	<form id="buyProduct" action="buyProduct">
		<input type="hidden" name="id" value="${product.id }" />
		<input type="hidden" name="productCnt" value="${param.productCnt }" />
		<input type="hidden" name="storeId" value="${store.id }" />
		<input type="hidden" name="memberId" value="${rq.loginedMemberId }" />
		<input type="hidden" name="orderNum" value="" />
		<input type="hidden" name="impUID" value="" />
		
		<div class="container mx-auto mt-1 p-10 border">
			<h3 class="font-bold text-xl select-none mb-5">배송지정보
				<a id="ordToRec" class="cursor-pointer py-2.5 px-5 ml-2 mb-2 text-sm font-medium text-gray-900 focus:outline-none bg-white rounded-lg border border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10 focus:ring-4 focus:ring-gray-200 dark:focus:ring-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:border-gray-600 dark:hover:text-white dark:hover:bg-gray-700">
					주문자 정보 가져오기
				</a>
			</h3>
			<div class="addressArea form-control">
				<div class="flex items-center">
					<span class="inline-block w-28 mr-1">수령자 이름</span>
					<input type="text" name="name" placeholder="수령자 이름" class="input input-ghost text-lg border-gray-400 w-80" value="${rq.loginedMember.name }" />
				</div>
				<div class="flex items-center mt-2">
					<span class="inline-block w-28 mr-1">연락처 1</span>
					<input type="text" maxlength="13" name="cellphoneNum" placeholder="연락처 1" class="input input-ghost text-lg border-gray-400 w-80" value="${rq.loginedMember.cellphoneNum }" />
				</div>
				<div class="flex items-center mt-2">
					<span class="inline-block w-28 mr-1">연락처 2</span>
					<input type="text" maxlength="13" name="cellphoneNum2" placeholder="연락처 2" class="input input-ghost text-lg border-gray-400 w-80" value="" />
				</div>
				<div class="flex items-center mt-2 w-9/12">
					<span class="inline-block w-28 mr-1">배송지 주소</span>
					<div class="flex-1">
						<input type="hidden" id="confmKey" name="confmKey" value=""  >
						<div class="flex items-center">
							<input class="input input-ghost text-lg border-gray-400" type="text" id="zipNo" name="zipNo" placeholder="우편번호" style="width:200px" readonly="readonly" value="${rq.loginedMember.zipNo}">
							<a class="ml-2 btn" onclick="goPopup();">주소검색</a>
						</div>
						
						<div class="flex items-center mt-2">
							<input class="input input-ghost text-lg border-gray-400" type="text" id="roadAddr" name="roadAddr" placeholder="도로명 주소" style="width:60%" readonly="readonly" value="${rq.loginedMember.roadAddr}">	
							<input class="input input-ghost text-lg border-gray-400 ml-2" type="text" id="addrDetail" name="addrDetail" placeholder="상세 주소" style="width:40%"  value="${rq.loginedMember.addrDetail}">
						</div>
					</div>
				</div>
				<div class="flex items-center mt-2 w-9/12">
					<span class="inline-block w-28 mr-1">배송메모</span>
					<input type="text" name="dlvyMemo" placeholder="요청사항을 입력합니다." class="flex-1 input input-ghost text-lg border-gray-400 w-80" />
				</div>
			</div>
		</div>
	</form>
	
	<div class="container mx-auto mt-3 p-10 border">
		<h3 class="font-bold text-xl select-none mb-2">결제 수단</h3>
		<div id="pay_method" class="flex">
			<div class="flex items-center mr-5">
				<input id="pay-radio-1" type="radio" value="card" name="bordered-radio" class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600">
				<label for="pay-radio-1" class="w-full py-4 ml-2 text-sm font-medium text-gray-900 dark:text-gray-300">신용카드</label>
			</div>
			<div class="flex items-center mr-5">
				<input checked id="pay-radio-2" type="radio" value="vbank" name="bordered-radio" class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600">
				<label for="pay-radio-2" class="w-full py-4 ml-2 text-sm font-medium text-gray-900 dark:text-gray-300">무통장 입금</label>
			</div>
			<div class="flex items-center mr-5">
				<input id="pay-radio-3" type="radio" value="trans" name="bordered-radio" class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600">
				<label for="pay-radio-3" class="w-full py-4 ml-2 text-sm font-medium text-gray-900 dark:text-gray-300">실시간 계좌이체</label>
			</div>
			<div class="flex items-center">
				<input id="pay-radio-4" type="radio" value="phone" name="bordered-radio" class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600">
				<label for="pay-radio-4" class="w-full py-4 ml-2 text-sm font-medium text-gray-900 dark:text-gray-300">휴대폰 소액결제</label>
			</div>
		</div>
	</div>
	
	<div class="container mx-auto mt-3 mb-5">
		<div class="mx-auto w-52 h-20">
			<button onclick="return requestPay();" type="button" class="w-full h-full justify-center text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-bold rounded-lg text-xl px-5 py-2.5 text-center inline-flex items-center mr-2 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
				<svg aria-hidden="true" class="w-5 h-5 mr-2 -ml-1" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path d="M3 1a1 1 0 000 2h1.22l.305 1.222a.997.997 0 00.01.042l1.358 5.43-.893.892C3.74 11.846 4.632 14 6.414 14H15a1 1 0 000-2H6.414l1-1H14a1 1 0 00.894-.553l3-6A1 1 0 0017 3H6.28l-.31-1.243A1 1 0 005 1H3zM16 16.5a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0zM6.5 18a1.5 1.5 0 100-3 1.5 1.5 0 000 3z"></path></svg>
				결제하기
			</button>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jsp"%>