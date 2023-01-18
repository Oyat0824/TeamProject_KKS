<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Product List" /> 
<%@ include file="../common/head.jsp" %>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<div class="mb-2 flex justify-between ">
			<div><span>등록된 상품${productCount } 개</span></div>
			
			<form>

				<select data-value="${searchKeywordTypeCode }" class="select select-bordered" name="searchKeywordTypeCode">
					<option value="productName">상품 이름</option>
					<option value="productCetegory">상품 카테고리</option>
					<option value="productName,productCetegory">상품 이름 + 상품 카테고리</option>
				</select>

				<input class="ml-2 w-84 input input-bordered" type="text" name="searchKeyword" placeholder="검색어를 입력해주세요" maxlength="20" value="${searchKeyword }" />

				<button class="ml-2 btn btn-active btn-ghost">검색</button>
			</form>
			
		</div>
		
		<div class="table-box-type-1">
			<table class="table w-full">
			
				<colgroup>
					<col width="60"/>
					<col width="200"/>
					<col />
					<col width="120"/>
					<col width="50"/>
					<col width="50"/>
				</colgroup>
				
				<thead>
					<tr>
						<th>번호</th>
						<th>날짜</th>
						<th>상품이름</th>
						<th>상품로고</th>
						<th>상품설명</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach var="product" items="${products}">
						<tr class="hover">
							<td>${product.id}</td>
							<td>${product.regDate.substring(2, 16)}</td>
							<td>${product.productName}</td>
							<td>${product.productLogo}</td>
							<td>${product.productDesc}</td>
						</tr>
					</c:forEach>
					
				</tbody>
				
			</table>	
			
		</div>
		
		<div class="page-menu mt-2 flex justify-center">
		
			<div class="btn-group">
				<!-- «  -->
				<!-- »  -->
							<!-- 지정한 만큼 페이징 수 보이기  -->
				<c:set var="pageMenuLen" value="10" />
				<c:set var="startPage" value="${page - pageMenuLen >= 1 ? page - pageMenuLen : 1}" />
				<c:set var="endPage" value="${page + pageMenuLen <= pagesCount ? page + pageMenuLen : pagesCount}" />
				
				<c:set var="pageBaseUri" value="?boardId=${boardId }&searchKeywordTypeCode=${searchKeywordTypeCode }&searchKeyword=${searchKeyword }" />
				
				<!-- 페이지 처음과 끝으로 이동 -->
				<c:if test="${page == 1 }">
					<a class="btn btn-sm btn-disabled">«</a>
					<a class="btn btn-sm btn-disabled">&lt;</a>
				</c:if>
				<c:if test="${page > 1 }">
					<a class="btn btn-sm" href="${pageBaseUri }&page=1">«</a>
					<a class="btn btn-sm" href="${pageBaseUri }&page=${page - 1 }">&lt;</a>
				</c:if>
				
				<c:forEach begin="${startPage }" end="${endPage }" var="i">  <!-- 페이징 카운트 -->
					<a class="btn btn-sm ${page == i ? 'btn-active' : ''}" href="${pageBaseUri }&page=${i }">${i }</a>
				</c:forEach>
				
				<c:if test="${page < pagesCount }">
					<a class="btn btn-sm" href="${pageBaseUri }&page=${page + 1 }">&gt;</a>
					<a class="btn btn-sm" href="${pageBaseUri }&page=${pagesCount }">»</a>
				</c:if>
				<c:if test="${page == pagesCount }">
					<a class="btn btn-sm btn-disabled">&gt;</a>
					<a class="btn btn-sm btn-disabled">»</a>
				</c:if>
				
			</div>
			
		</div>
		
		<button class="btn btn-primary" onclick="history.back();"><i class="fa-solid fa-right-from-bracket"></i>뒤로가기</button>
		
	</div>
	
</section>

<%@ include file="../common/foot.jsp"%>	