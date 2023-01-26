<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="pageTitle" value="Store List" />
<%@ include file="../common/head.jsp"%>

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

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<form id="frm" action="">
			<input type="hidden" name="listOrder" value="${param.listOrder == null ? 'rank' : param.listOrder}" />
			<input type="hidden" name="listStyle" value="${param.listStyle == null ? 'list' : param.listStyle}" />
			<div class="flex justify-between text-base items-center pb-5 mb-5 border-b">
				<ul class="flex h-10">
					<li class="mr-2 border"><a class="block p-2 text-blue-500 font-bold" href="#" data-form="listOrder" data-type="rank" onclick="return chgForm(this);">랭킹 순</a></li>
					<li class="mr-2 border"><a class="block p-2" href="#" data-form="listOrder" data-type="reviewMany" onclick="return chgForm(this);">리뷰 많은순</a></li>
					<li class="mr-2 border"><a class="block p-2" href="#" data-form="listOrder" data-type="reviewSmall" onclick="return chgForm(this);">리뷰 좋은순</a></li>
					<li class="border"><a class="block p-2" href="#" data-form="listOrder" data-type="date" onclick="return chgForm(this);">등록일 순</a></li>
				</ul>
				
				<div class="flex">
					<div>
						<select class="mr-2 p-2 border h-10" data-form="itemsNum" name="itemsNum" onchange="return chgForm(this);">
							<option value="20">20개씩 보기</option>
							<option value="40" ${param.itemsNum == 40 ? "selected" : "" }>40개씩 보기</option>
						</select>
					</div>
					<div class="flex h-10">
						<a class="block mr-1 p-2 border w-10 text-center text-blue-500" href="#" data-form="listStyle" data-type="list" onclick="return chgForm(this);"><i class="fa-sharp fa-solid fa-list"></i></a>
						<a class="block p-2 border w-10 text-center" href="#" data-form="listStyle" data-type="gallery" onclick="return chgForm(this);"><i class="fa-solid fa-border-all"></i></a>
					</div>
				</div>
			</div>
		</form>
		
		<div class="list">
			<c:forEach var="store" items="${stores}">
				<div class="flex items-center p-5 hover:bg-gray-50 border-b">
					<div class="img_area mr-10">
						<a href="view?id=${store.id}">
							<img class="w-36 h-36 border-2 border-gray-400" src="${rq.getImgUri('store', store.id, 'storeImg')}" alt="" />
						</a>
					</div>
					
					<div class="info_area">
						<div class="storeName text-base">
							<a class="font-bold" href="view?id=${store.id}">${store.storeName}</a>
						</div>
						<div class="storeDesc text-sm my-3 h-20 overflow-hidden">
							<p>${store.getForPrintDesc() }</p>
						</div>
						<div class="storeEtc text-sm">
							<a href="">리뷰 수 <span class="text-indigo-600">12</span></a>
							<a class="dot" href="">구매건수 <span class="text-indigo-600">567</span></a>
							<span class="dot">등록일 ${store.regDate.substring(0, 8).replace("-", ".") }</span>
							<a class="dot" href="">찜하기 <span class="text-indigo-600">567</span></a>
						</div>
					</div>
					
					<div></div>
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