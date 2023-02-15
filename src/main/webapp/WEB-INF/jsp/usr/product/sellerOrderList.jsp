<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="pageTitle" value="Seller Order List" /> 
<c:set var="menuName" value="sellerOrderList" />
<%@ include file="../common/head.jsp" %>

<script>
	$(function(){
		$("#ordNum").click(function(){
			$(this).prev().attr("type", "text");
			$(this).prev().select();
			
			var copy = document.execCommand("copy");
			$(this).prev().attr("type", "hidden");
			
			if(copy) {
				alert("주문번호가 복사됐습니다.");
			}
		});
	});
</script>

<section>
	<div class="flex container mx-auto">
		<%@ include file="../common/sideMenu.jsp"%>
	
		<div class="w-full my-10 mx-5">
			<h1 class="w-11/12 mx-auto font-bold text-xl select-none mb-5">주문 목록</h1>	
			<table class="w-11/12 mx-auto table">
				<thead>
					<tr>
						<th class="text-sm">번호</th>
						<th class="text-sm">주문 번호</th>
						<th class="text-sm">상품 이름</th>
						<th class="text-sm">주문일</th>
						<th class="text-sm">배송 메모</th>
						<th class="text-sm">운송장 번호</th>
						<th class="text-sm">주문 취소</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach var="order" items="${orderList}">
						<tr class="hover">
							<td>
								<div class="badge badge-lg border-transparent font-bold text-white">${order.ROWNUM}</div>
							</td>
							<td>
								<input type="hidden" value="${order.orderNum}" />
								<span id="ordNum" class="badge tooltip cursor-pointer" data-tip="${order.orderNum}">주문번호</span>
							</td>
							<td><a class="hover:text-indigo-700" href="view?id=${order.productId}&storeId=${order.storeId}">
									<c:choose> 
										<c:when test="${fn:length(order.productName) > 15}">
											${order.productName.substring(0, 15)}...
										</c:when> 
										<c:otherwise>
											${order.productName}
										</c:otherwise> 
									</c:choose>
								</a>
							</td>
							<td><span>${order.regDate.substring(0, 10) }</span></td>
							<td><span>${order.dlvyMemo == "" ? "-" : order.dlvyMemo}</span></td>
							<td>
								<form class="relative" action="checkPurchase" ${order.cancel == 1 ? "onsubmit='return false;'" : '' }>
									<input type="hidden" name="storeId" value="${param.id }" />
									<input type="hidden" name="storeModifyAuthKey" value="${param.storeModifyAuthKey }" />
									<input type="hidden" name="orderId" value="${order.id }" />
									<input type="hidden" name="productId" value="${order.productId }" />
									<input type="hidden" name="ordCheck" value="${order.ordCheck }" />
									<input type="hidden" name="ordPCnt" value="${order.productCnt }"/>
									<div class="form-control">
										<div class="input-group">
											<input type="text" placeholder="운송장 번호" class="input input-bordered" name="waybill" value="${order.waybill}" ${order.cancel == 1 ? "disabled" : "" } />
											<button class="btn btn-square" ${order.cancel == 1 ? "disabled" : "" }>
												Go
											</button>
										</div>
									</div>
								</form>
							</td>
							<td>
								<c:if test="${order.confirm == 1 }">
									<div>구매확정</div>
								</c:if>
								<c:if test="${order.cancel == 1 }">
									<div>취소됨</div>
									<div>${order.updateDate.substring(0, 10) }</div>
								</c:if>
								<c:if test="${order.cancel == 0 && order.confirm == 0 }">
									<a class="hover:text-indigo-700" onclick="if(confirm('정말 취소하시겠습니까?') == false) return false;" href="sellerCancelPurchase?storeId=${order.storeId}&storeModifyAuthKey=${param.storeModifyAuthKey}&orderId=${order.id}&productId=${order.productId}&ordPCnt=${order.productCnt}">
										주문 취소
									</a>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<div class="pageNav flex justify-center mt-5">
				<div class="btn-group">
					<c:set var="maxPageNum" value="5" />
					<fmt:parseNumber var="pageBlock" integerOnly="true" value="${page / maxPageNum}" />
					<c:if test="${page % maxPageNum > 0}"><c:set var="pageBlock" value="${pageBlock + 1}" /></c:if>
					<c:set var="endPage" value="${pageBlock * maxPageNum}" />
					<c:set var="startPage" value="${endPage - (maxPageNum - 1)}" />
					<c:set var="endPage" value="${pagesCount < endPage ? pagesCount : endPage }" />
	
					<c:set var="pageBaseUri" value="&id=${param.id }&storeModifyAuthKey=${param.storeModifyAuthKey }&searchKeyword=${searchKeyword }" />
	
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
		</div>
	</div>
</section>

<%@ include file="../common/foot.jsp"%>	