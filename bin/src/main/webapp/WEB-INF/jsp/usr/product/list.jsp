<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="pageTitle" value="Product List" /> 
<c:set var="menuName" value="productList" />
<%@ include file="../common/head.jsp" %>

<script>
	const chgForm = function(e) {
		if($(e).data().form == "listOrder") {
			$("#frm").find("[name=listOrder]").val($(e).data().type);
			
			$("#frm").submit();
			
			return false;
		}
		
		if($(e).data().form == "listStyle") {
			$("#frm").find("[name=listStyle]").val($(e).data().type);
			
			$("#frm").submit();
			
			return false;
		}
		
		if($(e).data().form == "itemsNum") {
			$("#frm").submit();
			
			return false;
		}
	}
	
	$(function(){
		if($("#frm").find("[name=listOrder]").val() == "${param.listOrder}") {
			$("[data-form='listOrder']").removeClass("text-blue-500 font-bold");
			$("[data-type='${param.listOrder}']").addClass("text-blue-500 font-bold");
		}
		
		if($("#frm").find("[name=listStyle]").val() == "${param.listStyle}") {
			$("[data-form='listStyle']").removeClass("text-blue-500");
			$("[data-type='${param.listStyle}']").addClass("text-blue-500");
		}
	})
</script>

<section>
	<div class="flex container mx-auto">
		<%@ include file="../common/sideMenu.jsp"%>
	
		<div class="w-full my-10 mx-5">
			<form id="frm" action="">
				<div class="flex justify-end text-base items-center pb-5 mb-5 border-b">
					<div>
						<select class="mr-2 p-2 border h-10" data-form="itemsNum" name="itemsNum" onchange="return chgForm(this);">
							<option value="20">20개씩 보기</option>
							<option value="40" ${param.itemsNum == 40 ? "selected" : "" }>40개씩 보기</option>
						</select>
					</div>
				</div>
			</form>
			
			<div>
				<table class="table w-11/12 mx-auto">
					<thead>
						<tr>
							<th class="text-sm">번호</th>
							<th class="text-sm">상품 분류</th>
							<th class="text-sm">상품 이름</th>
							<th class="text-sm">상품 가격</th>
							<th class="text-sm">상품 재고</th>
							<th class="text-sm">등록일</th>
							<th class="text-sm">수정</th>
							<th class="text-sm">삭제</th>
						</tr>
					</thead>
	
					<tbody>
						<c:forEach var="product" items="${products}">
							<tr class="hover">
								<td><div class="badge badge-lg border-transparent font-bold text-white">${product.ROWNUM}</div></td>
								<td>${product.productCategory == "" ? "없음" : product.categoryName}</td>
								<td><a class="hover:text-indigo-700" href="view?storeId=${product.storeId}&id=${product.id}">${product.productName}</a></td>
								<td>${product.productPrice}</td>
								<td>${product.productStock}</td>
								<td>${product.regDate.substring(0, 16) }</td>
								<td><a class="hover:text-indigo-700" href="modify?storeId=${product.storeId}&id=${product.id}">수정</a></td>
								<td>삭제</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			
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