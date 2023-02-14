<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="pageTitle" value="Product OrderStatus" />
<%@ include file="../common/head.jsp"%>

<!-- 주문서 -->
<section class="mt-3">
	<div class="container mx-auto">
		<div class="font-bold text-xl text-center py-2 border">주문 상세 정보</div>
		
		<div class="mt-2 p-2 border">
			<div class="font-bold"><span>주문번호 : </span> <span>${purchase.orderNum }</span></div>
			<div class="text-gray-500"><span>결제일 : </span> <span>${purchase.regDate.substring(0, 10) }</span></div>
		</div>
		
		<div class="mt-2 p-4 border">
			<div class="flex justify-between">
				<div class="flex items-center text-lg font-bold">
					<i class="fa-solid fa-house mr-1"></i><span>${purchase.storeName}</span>
				</div>
				
				<div class="flex items-center">
					<c:choose> 
						<c:when test="${purchase.productDlvy == 0}">
							<i class="fa-solid fa-truck mr-1"></i><span class="text-gray-500">무료배송</span>
						</c:when> 
						<c:otherwise>
							<i class="fa-solid fa-truck mr-1"></i><span class="text-gray-500"><fmt:formatNumber value="${purchase.productDlvyPrice}" pattern="#,###" />원 선결제</span>
						</c:otherwise> 
					</c:choose> 
				</div>
			</div>
			
			<div class="flex flex-col mt-2 pb-5 border-b border-gray-700">
				<div class="flex items-center">
					<i class="fa-solid fa-phone mr-1"></i> <span>판매자 번호 : ${purchase.sellerTel }</span>
				</div>
				<div class="text-sm text-gray-500 select-none">
					<ul>
						<li><span class="mr-2">-</span>판매자에게 직접 연락하시면 빠른 확인이 가능합니다.</li>
						<li><span class="mr-2">-</span>구매확정 이후 환불 문의는 판매자에게 문의해 주세요.</li>
					</ul>
				</div>
			</div>
			
			<div class="flex mt-3">
			<!-- 구매 확정인 경우, 상품 이름 옆 구매확정 아이콘 // 구매확정일 주문 수량 아래 추가 -->
				<div class="thumnail mr-3">
					<a href="view?id=${purchase.productId}&storeId=${purchase.storeId}">
						<img class="w-24 h-24 p-1 border rounded-lg" src="${rq.getImgUri('product', purchase.productId, 'productImg', 1)}" />
					</a>
				</div>
				<div class="desc flex-auto">
					<div><span class="text-sm text-gray-500">[SB_STORE] ${purchase.storeName}</span></div>
					<div class="flex items-center">
						<c:if test="${purchase.confirm == 1 }"><span class="px-1 text-sm border select-none mr-2">구매확정</span></c:if>
						<c:if test="${purchase.cancel == 1 }"><span class="px-1 text-sm border select-none mr-2">주문취소</span></c:if>
						<a href="view?id=${purchase.productId}&storeId=${purchase.storeId}">
							<span class="text-lg">${purchase.productName }</span>
						</a>
					</div>
					<div>
						<span class="font-bold"><fmt:formatNumber value="${purchase.productPrice}" pattern="#,###" />원</span>
					</div>
					<div>
						<span class="text-sm text-gray-500">주문수량 : ${purchase.productCnt}</span>
					</div>
					<c:if test="${purchase.confirm == 1 }">
						<div>
							<span class="text-sm text-gray-500 text-indigo-600">구매확정일 : ${purchase.updateDate.substring(0, 10)}</span>
						</div>
					</c:if>
					<c:if test="${purchase.cancel == 1 }">
						<div>
							<span class="text-sm text-gray-500 text-indigo-600">주문취소일 : ${purchase.updateDate.substring(0, 10)}</span>
						</div>
					</c:if>
					<div class="flex mt-2">
						<c:if test="${purchase.cancel == 0 && purchase.confirm == 1 && isReview == 0}">
							<div class="flex-auto mx-1 p-2 border text-center">
								<a class="block" href="review?storeId=${purchase.storeId}&productId=${purchase.productId}" onclick="window.open(this.href, '_blank', 'width=480, height=768'); return false;">리뷰쓰기</a>
							</div>
						</c:if>
						<c:if test="${purchase.cancel == 0 && purchase.ordCheck == 1}">
							<div class="flex-auto mx-1 p-2 border text-center">
								<a class="block" href="">배송조회</a>
							</div>
						</c:if>
						<c:if test="${purchase.cancel == 0 && purchase.confirm == 0}">
							<div class="flex-auto mx-1 p-2 border text-center">
								<a class="block" href="confirmPurchase?id=${purchase.id}&memberId=${purchase.memberId}" onclick="return confirm('구매를 확정하시겠습니까?');">구매확정</a>
							</div>
						</c:if>
						<c:if test="${purchase.cancel == 0 && purchase.confirm == 0 && purchase.ordCheck == 0}">
							<div class="flex-auto mx-1 p-2 border text-center">
								<a class="block" href="cancelPurchase?id=${purchase.id}&memberId=${purchase.memberId}" onclick="return confirm('주문을 취소하시겠습니까?');">주문취소</a>
							</div>
						</c:if>
					</div>
					
				</div>		
			</div>
		</div>
		
		<div class="mt-2 p-4 border">
			<h3 class="font-bold text-xl select-none mb-5 pb-5 border-b">배송지정보</h3>
			<div class="flex mb-2">
				<div class="mr-10 w-20 text-gray-500">수령인</div>
				<div>${purchase.name }</div>
			</div>
			<div class="flex mb-2">
				<div class="mr-10 w-20 text-gray-500">연락처</div>
				<div>${purchase.cellphoneNum }</div>
			</div>
			<div class="flex mb-2">
				<div class="mr-10 w-20 text-gray-500">배송지</div>
				<div>
					${purchase.zipNo} <br />
					${purchase.roadAddr } <br />
					${purchase.addrDetail }
				</div>
			</div>
			<div class="flex">
				<div class="mr-10 w-20 text-gray-500">배송메모</div>
				<div>${purchase.dlvyMemo == "" ? "-" : purchase.dlvyMemo }</div>
			</div>
		</div>
		
		<div class="mt-2 p-4 border">
			<div class="flex justify-between mb-5 pb-5 border-b">
				<h3 class="font-bold text-xl select-none">주문금액</h3>
				<h3 class="font-bold text-xl select-none"><fmt:formatNumber value="${(purchase.productPrice * purchase.productCnt) + purchase.productDlvyPrice}" pattern="#,###" />원</h3>
			</div>
			<c:choose> 
				<c:when test="${purchase.productCnt > 1}">
					<div class="flex mb-2">
						<div class="mr-10 w-20 text-gray-500">상품금액</div>
						<div class="font-bold"><fmt:formatNumber value="${purchase.productPrice}" pattern="#,###" />원</div>
					</div>
					<div class="flex mb-2">
						<div class="mr-10 w-20 text-gray-500">구매수량</div>
						<div class="font-bold"><fmt:formatNumber value="${purchase.productCnt}" pattern="#,###" />개</div>
					</div>
				</c:when> 
				<c:otherwise>
					<div class="flex mb-2">
						<div class="mr-10 w-20 text-gray-500">상품금액</div>
						<div class="font-bold"><fmt:formatNumber value="${purchase.productPrice}" pattern="#,###" />원</div>
					</div>
				</c:otherwise> 
			</c:choose> 
			<c:if test="${purchase.productDlvy == 1 }">
				<div class="flex mb-2">
					<div class="mr-10 w-20 text-gray-500">배송비</div>
					<div class="font-bold">+ <fmt:formatNumber value="${purchase.productDlvyPrice}" pattern="#,###" />원</div>
				</div>
			</c:if>
		</div>
		
		<div class="mt-2 p-4 border">
			<h3 class="font-bold text-xl select-none mb-5 pb-5 border-b">결제 상세</h3>
			<div class="flex mb-2">
				<div class="mr-10 w-20 text-gray-500">
					<c:if test="${payMethod == 'card'}">신용카드</c:if>
					<c:if test="${payMethod == 'vbank'}">무통장 입금</c:if>
					<c:if test="${payMethod == 'trans'}">실시간 계좌이체</c:if>
					<c:if test="${payMethod == 'phone'}">휴대폰 소액결제</c:if>
				</div>
				<div class="font-bold text-indigo-600"><fmt:formatNumber value="${(purchase.productPrice * purchase.productCnt) + purchase.productDlvyPrice}" pattern="#,###" />원</div>
			</div>
			<c:if test="${payMethod == 'vbank'}">
				<div class="flex mb-2">
					<div class="mr-10 w-20 text-gray-500">입금은행</div>
					<div class="font-bold">${vbank_name}</div>
				</div>
				<div class="flex mb-2">
					<div class="mr-10 w-20 text-gray-500">예금주</div>
					<div class="font-bold">${vbank_holder}</div>
				</div>
				<div class="flex mb-2">
					<div class="mr-10 w-20 text-gray-500">계좌번호</div>
					<div class="font-bold">${vbank_num}</div>
				</div>
			</c:if>
		</div>
		
		<div class="mt-5">
			<button class="btn w-full" onclick="history.back();">뒤로가기</button>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jsp"%>