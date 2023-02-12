<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="pageTitle" value="Product OrderSheet" />
<%@ include file="../common/head.jsp"%>

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
		    				<fmt:formatNumber value="${product.productPrice * param.productCnt}" pattern="#,###" /><span class="text-base">원</span>
		    			</div>
		    		</td>
		    	</tr>
		    </tbody>
		</table>
	</div>
	
	<div class="container mx-auto mt-10 p-10 border">
		<h3 class="font-bold text-xl select-none mb-5">배송지정보</h3>
		<div class="addressArea form-control">
			<label>
				<span class="inline-block w-28 mr-1">수령인</span>
				<input type="text" placeholder="" class="input input-bordered w-80" />
			</label>
			<label class="mt-2">
				<span class="inline-block w-28 mr-1">연락처 1</span>
				<input type="text" placeholder="" class="input input-bordered w-80" />
			</label>
			<label class="mt-2">
				<span class="inline-block w-28 mr-1">연락처 2</span>
				<input type="text" placeholder="" class="input input-bordered w-80" />
			</label>
			<label class="mt-2">
				<span class="inline-block w-28 mr-1">배송지 주소</span>
				<input type="text" placeholder="" class="input input-bordered w-80" />
			</label>
			<label class="mt-2">
				<span class="inline-block w-28 mr-1">배송메모</span>
				<input type="text" placeholder="" class="input input-bordered w-80" />
			</label>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jsp"%>