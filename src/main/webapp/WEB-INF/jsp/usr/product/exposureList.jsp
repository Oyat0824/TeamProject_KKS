<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="pageTitle" value="Product Exposure List" /> 
<%@ include file="../common/head.jsp" %>

<script>
	const chgForm = function(e) {
		if($(e).data().form == "listOrder") {
			$("#frm").find("[name=listOrder]").val($(e).data().type);
			
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
	})

</script>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3 con">
		<form id="frm" action="">
				<input type="hidden" name="page" value="${param.page == null ? '1' : param.page}" />
				<input type="hidden" name="listOrder" value="${param.listOrder == null ? '' : param.listOrder}" />
				<div class="flex justify-between text-base items-center pb-5 border-b">
					<ul class="flex h-10">
						<li class="mr-2 border"><a class="block p-2 text-blue-500 font-bold" href="#" data-form="listOrder" data-type="" onclick="return chgForm(this);">번호 순</a></li>
						<li class="mr-2 border"><a class="block p-2" href="#" data-form="listOrder" data-type="date" onclick="return chgForm(this);">등록일 순</a></li>
						<li class="mr-2 border"><a class="block p-2" href="#" data-form="listOrder" data-type="reviewMany" onclick="return chgForm(this);">리뷰 많은순</a></li>
						<li class="border"><a class="block p-2" href="#" data-form="listOrder" data-type="purchaseMany" onclick="return chgForm(this);">구매 많은순</a></li>
					</ul>
					
					<div class="flex">
						<div>
							<select class="mr-2 p-2 border h-10" data-form="itemsNum" name="itemsNum" onchange="return chgForm(this);">
								<option value="20">20개씩 보기</option>
								<option value="40" ${param.itemsNum == 40 ? "selected" : "" }>40개씩 보기</option>
							</select>
						</div>
					</div>
				</div>
	
			<div class="gallery">
				<div class="flex flex-wrap">
					<c:forEach var="product" items="${products}" varStatus="status">
						<c:if test="${status.index % 5 == 0 }">
							<div class="flex w-full my-3 pb-5 border-b">
								<c:forEach var="i" begin="${status.index}" end="${status.index + (5 - 1)}" step="1">
									<c:if test="${ products[i] != null }">
										<div class="flex flex-col items-center w-1/5 overflow-hidden">
											<div class="img_area">
												<a href="view?storeId=${products[i].storeId}&id=${products[i].id}">
													<img class="w-40 h-40 border-2 border-gray-400" src="${rq.getImgUri('product', products[i].id, 'productImg')}" alt="" />
												</a>
											</div>
											
											<div class="info_area flex flex-col items-center mt-2 pl-2">
												<div class="productName text-base">
													<a class="font-bold" href="view?storeId=${products[i].storeId}&id=${products[i].id}">
														<c:choose>
															<c:when test="${fn:length(products[i].productName) > 15}">
																<c:out value="${fn:substring(products[i].productName, 0, 15)}.." />
															</c:when>
															<c:otherwise>
																<c:out value="${products[i].productName }" />
													        </c:otherwise>
												        </c:choose>
													</a>
												</div>
												<div class="productEtc text-sm mt-2">
													<a href="">리뷰 <span class="text-indigo-600">${products[i].reviewCnt }</span></a>
													<a class="dot" href="">구매 <span class="text-indigo-600">${products[i].purchaseCnt }</span></a>
												</div>	
											</div>
										</div>
									</c:if>
								</c:forEach>
							</div>
						</c:if>	
					</c:forEach>
				</div>
			</div>
						
			<div class="input-group flex justify-end mt-3">
				<input type="text" placeholder="Search…" class="input input-bordered" name="searchKeyword" value="${param.searchKeyword }" />
				<button class="btn btn-square">
					Go
				</button>
			</div>
			
		</form>
		
		<div class="pageNav flex justify-center my-5">
			<div class="btn-group">
				<c:set var="maxPageNum" value="5" />
				<fmt:parseNumber var="pageBlock" integerOnly="true" value="${page / maxPageNum}" />
				<c:if test="${page % maxPageNum > 0}"><c:set var="pageBlock" value="${pageBlock + 1}" /></c:if>
				<c:set var="endPage" value="${pageBlock * maxPageNum}" />
				<c:set var="startPage" value="${endPage - (maxPageNum - 1)}" />
				<c:set var="endPage" value="${pagesCount < endPage ? pagesCount : endPage }" />

				<c:set var="pageBaseUri" value="&listOrder=${param.listOrder == null ? '' : param.listOrder}&&itemsNum=${param.itemsNum == null ? 20 : param.itemsNum }&searchKeyword=${searchKeyword }" />

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