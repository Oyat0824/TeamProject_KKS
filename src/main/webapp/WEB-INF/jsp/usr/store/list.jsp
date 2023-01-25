<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="pageTitle" value="Store List" />
<%@ include file="../common/head.jsp"%>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1">
			<div class="headTab flex justify-between items-center mb-3">
				<div>
					스토어 총 개수 : <span class="font-bold">${storesCount}</span>
				</div>

				<form action="" class="form-control">
					<input type="hidden" name="page" value="1" />
					<div class="input-group">
						<input class="input input-bordered focus:outline-none" type="text" name="searchKeyword" placeholder="Search…" value="${searchKeyword }" />
						<button class="btn btn-square focus:outline-none">GO</button>
					</div>
				</form>
			</div>
			<table class="table table-zebra w-full">
				<thead>
					<tr>
						<th class="text-sm">번호</th>
						<th class="text-sm title">제목</th>
						<th class="text-sm">작성자</th>
						<th class="text-sm">날짜</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach var="store" items="${stores}">
						<tr class="hover">
							<td><div class="badge badge-lg bg-purple-600 border-transparent font-bold text-white">${store.id}</div></td>
							<td><a class="hover:text-yellow-500" href="view?id=${store.id}">${store.storeName}</a></td>
							<td>${store.sellerName}</td>
							<td>${store.regDate.substring(2, 16)}</td>
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

				<c:set var="pageBaseUri" value="&searchKeyword=${searchKeyword }" />

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
</section>
<%@ include file="../common/foot.jsp"%>