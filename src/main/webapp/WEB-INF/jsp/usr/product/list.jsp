<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="pageTitle" value="Product List" /> 
<c:set var="menuName" value="productList" />
<%@ include file="../common/head.jsp" %>

<section>
	<div class="flex container mx-auto">
		<%@ include file="../common/sideMenu.jsp"%>
	
		<div class="w-full my-10 mx-5">
			<h1 class="w-11/12 mx-auto font-bold text-xl select-none mb-5">상품 목록</h1>	
			<table class="w-11/12 mx-auto table">
				<thead>
					<tr>
						<th class="text-sm">번호</th>
						<th class="text-sm">상품 분류</th>
						<th class="text-sm">상품 이름</th>
						<th class="text-sm">상품 가격</th>
						<th class="text-sm">상품 재고</th>
						<th class="text-sm">배송비</th>
						<th class="text-sm">등록일</th>
						<th class="text-sm">수정</th>
						<th class="text-sm">삭제</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach var="product" items="${products}">
						<tr class="hover">
							<td><div class="badge badge-lg border-transparent font-bold text-white">${product.ROWNUM}</div></td>
							<td><span>${product.productCategory == "" ? "없음" : product.categoryName}</span></td>
							<td><a class="hover:text-indigo-700" href="view?storeId=${product.storeId}&id=${product.id}">
									<c:choose> 
										<c:when test="${fn:length(product.productName) > 20}">
											${product.productName.substring(0, 20)}...
										</c:when> 
										<c:otherwise>
											${product.productName}
										</c:otherwise> 
									</c:choose> 
								</a>
							</td>
							<td><span><fmt:formatNumber value="${product.productPrice}" pattern="#,###" />원</span></td>
							<td><span><fmt:formatNumber value="${product.productStock }" pattern="#,###" />개</span></td>
							<td>
								<c:if test="${product.productDlvy == 0}">
									<span>무료</span>
		    					</c:if>
		    					
		    					<c:if test="${product.productDlvy == 1}">
		    						<span><fmt:formatNumber value="${product.productDlvyPrice}" pattern="#,###" />원</span>
		    					</c:if>
							</td>
							<td><span>${product.regDate.substring(0, 10) }</span></td>
							<td><a class="hover:text-indigo-700" href="modify?storeId=${product.storeId}&id=${product.id}&storeModifyAuthKey=${param.storeModifyAuthKey}">수정</a></td>
							<td><a class="hover:text-indigo-700" onclick="return confirm('정말 삭제하시겠습니까?');" href="doDelete?storeId=${product.storeId}&id=${product.id}&storeModifyAuthKey=${param.storeModifyAuthKey}">삭제</a></td>
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