<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="pageTitle" value="Product OrderList" />
<%@ include file="../common/head.jsp"%>

<!-- 주문서 -->
<section class="mt-3">
	<div class="container mx-auto">
		<h1 class="font-bold text-xl select-none mt-5 mb-2">주문내역</h1>
		<c:forEach var="item" items="${purchaseList}">
			<div class="flex flex-col mb-3">
				<div class="flex flex-col justify-center w-full h-48 px-5 border">
					<c:choose> 
						<c:when test="${item.cancel == 1}">
							<h3 class="font-bold text-xl my-3">주문취소</h3>
						</c:when>
						<c:when test="${item.confirm == 1 }">
							<h3 class="font-bold text-xl my-3">구매확정</h3>
						</c:when> 
						<c:when test="${item.ordCheck == 1}">
							<h3 class="font-bold text-xl my-3">주문확인</h3>
						</c:when>
						<c:otherwise>
							<h3 class="font-bold text-xl my-3">결제완료</h3>
						</c:otherwise> 
					</c:choose> 
					
					<div class="flex">
						<div class="thumnail mr-5">
							<a href="view?id=${item.productId}&storeId=${item.storeId}">
								<img class="w-24 h-24 p-1 border rounded-lg" src="${rq.getImgUri('product', item.productId, 'productImg', 1)}" />
							</a>
						</div>
						<div class="desc flex-auto">
							<div>
								<span class="text-sm text-gray-500">${item.regDate.substring(5, 10) } 결제</span>
							</div>
							<div>
								<a href="view?id=${item.productId}&storeId=${item.storeId}">
									<span class="text-lg">${item.productName }</span>
								</a>
							</div>
							<div>
								<span class="font-bold"><fmt:formatNumber value="${item.productPrice}" pattern="#,###" />원</span>
							</div>
							<div>
								<a href="orderStatus?id=${item.id}"><span class="text-indigo-600 flex items-center">주문상세 <span class="ml-1 text-xs font-bold">&gt;</span></span></a>
							</div>
						</div>		
					</div>
				</div>
				
				<div class="flex border border-t-0">
					<c:if test="${item.cancel == 0 && item.confirm == 1 && isReview == 0}">
						<a class="flex-auto block py-3 text-center" href="review?storeId=${item.storeId}&product.id=${item.productId}" onclick="window.open(this.href, '_blank', 'width=480, height=768'); return false;">리뷰쓰기</a>
					</c:if>
					<c:if test="${item.cancel == 0 && item.confirm == 0}">
						<a class="flex-auto block border-r py-3 text-center" href="confirmPurchase?id=${item.id}&memberId=${item.memberId}" onclick="return confirm('구매를 확정하시겠습니까?');">구매확정</a>
					</c:if>
					<c:if test="${item.cancel == 0 && item.confirm == 0 && item.ordCheck == 0}">
						<a class="flex-auto block border-r py-3 text-center" href="cancelPurchase?id=${item.id}&memberId=${item.memberId}" onclick="return confirm('주문을 취소하시겠습니까?');">주문취소</a>
					</c:if>
					<c:if test="${item.cancel == 0 && item.ordCheck == 1}">
						<a class="flex-auto block py-3 text-center" href="">배송조회</a>
					</c:if>
				</div>
			</div>
		</c:forEach>
	</div>
	
	<div class="pageNav flex justify-center mt-5">
		<div class="btn-group">
			<c:set var="maxPageNum" value="5" />
			<fmt:parseNumber var="pageBlock" integerOnly="true" value="${page / maxPageNum}" />
			<c:if test="${page % maxPageNum > 0}"><c:set var="pageBlock" value="${pageBlock + 1}" /></c:if>
			<c:set var="endPage" value="${pageBlock * maxPageNum}" />
			<c:set var="startPage" value="${endPage - (maxPageNum - 1)}" />
			<c:set var="endPage" value="${pagesCount < endPage ? pagesCount : endPage }" />

			<c:set var="pageBaseUri" value="&searchKeyword=${searchKeyword}" />

			<c:if test="${page == 1}">
				<a class="btn btn-sm w-12 btn-disabled">&lt;&lt;</a>
				<a class="btn btn-sm w-12 btn-disabled">&lt;</a>
			</c:if>
			<c:if test="${page > 1}">
				<a class="btn btn-sm w-12" href="?page=1${pageBaseUri}">&lt;&lt;</a>
				<a class="btn btn-sm w-12" href="?page=${page-1}${pageBaseUri}">&lt;</a>
			</c:if>
				
			<c:forEach begin="${startPage }" end="${endPage }" var="i">
				<a class="btn btn-sm w-12 ${page == i ? 'btn-active' : ''}" href="?page=${i}${pageBaseUri}">${i}</a>
			</c:forEach>

			<c:if test="${page == pagesCount}">
				<a class="btn btn-sm w-12 btn-disabled">&gt;</a>
				<a class="btn btn-sm w-12 btn-disabled">&gt;&gt;</a>
			</c:if>
			<c:if test="${page < pagesCount}">
				<a class="btn btn-sm w-12" href="?page=${page+1}${pageBaseUri}">&gt;</a>
				<a class="btn btn-sm w-12" href="?page=${pagesCount}${pageBaseUri}">&gt;&gt;</a>
			</c:if>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jsp"%>