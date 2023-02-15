<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="pageTitle" value="ADMIN PAGE - PRODUCT LIST" /> 
<c:set var="menuName" value="productList" />
<%@ include file="../../usr/common/head.jsp" %>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<div class="mb-2 flex justify-between items-center">
			<div>
				<span>상품 수 : ${productsCount }</span>
			</div>
			<form>
				<select data-value="${searchKeywordTypeCode }" class="select select-bordered" name="searchKeywordTypeCode">
					<option value="storeName,productName">전체</option>
					<option value="productName">상품 이름</option>
					<option value="storeName">스토어 이름</option>
				</select>

				<input class="ml-2 w-84 input input-bordered" type="text" name="searchKeyword" placeholder="검색어를 입력해주세요" maxlength="20" value="${searchKeyword }" />

				<button class="ml-2 btn btn-active btn-ghost">검색</button>
			</form>
		</div>
		<c:choose>
			<c:when test="${productsCount == 0}">
				<div class="text-center py-2">조건에 일치하는 검색결과가 없습니다</div>
			</c:when>
			<c:otherwise>
				<div class="table-box-type-1">
					<table class="table w-full">
						<thead>
							<tr>
								<th><input type="checkbox" class="checkbox-all-product-id" /></th>
								<th>번호</th>
								<th>등록날짜</th>
								<th>수정날짜</th>
								<th>상품이 등록된 스토어</th>
								<th>상품이름</th>
								<th>상품설명</th>
								<th>삭제여부</th>
								<th>삭제날짜</th>
							</tr>
						</thead>

						<tbody>
							<c:forEach var="product" items="${products}">
								<c:if test="${member.memberType != 9 }">
									<tr class="hover">
										<c:choose>
											<c:when test="${product.delProductStatus != true }">
												<td><input type="checkbox" class="checkbox-product-id" value="${product.id }" /></td>
											</c:when>
											<c:otherwise>
												<td><input type="checkbox" class="checkbox-product-id" value="${product.id }" disabled/></td>
											</c:otherwise>
										</c:choose>
										<td>${product.id}</td>
										<td>${product.regDate.substring(2,16)}</td>
										<td>${product.updateDate.substring(2,16)}</td>
										<td>${product.storeName}</td>
										<td>${product.productName}</td>
										<td>${product.productBody}</td>
										<td>${product.delStatusStr()}</td>
										<td>${product.delDateStr()}</td>
									</tr>
								</c:if>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:otherwise>
		</c:choose>

		<script>
			$('.checkbox-all-product-id').change(function() {
				const allCheck = $(this);
				const allChecked = allCheck.prop('checked');
				$('.checkbox-product-id').prop('checked', allChecked);
				$('.checkbox-product-id:is(:disabled)').prop('checked', false)
			})
			
			$('.checkbox-product-id').change(function() {
				const checkboxMemberIdCount = $('.checkbox-product-id').length;
				const checkboxMemberIdCheckedCount = $('.checkbox-product-id:checked').length;
				const checkboxDisabledCount = $('.checkbox-product-id:is(:disabled)').length;
				const allChecked = (checkboxMemberIdCount - checkboxDisabledCount) == checkboxStoreIdCheckedCount;
				$('.checkbox-all-product-id').prop('checked', allChecked);
			})
		</script>
		
		<div class="mt-2 flex justify-between ">
			<button class="btn btn-primary" onclick="history.back();"><i class="fa-solid fa-right-from-bracket"></i>뒤로가기</button>
			<button class="btn-text-link btn btn-active btn-ghost btn-delete-selected-products">상품 삭제</button>
		</div>

		<form method="POST" name="do-delete-products-form" action="doDeleteProducts">
			<input type="hidden" name="ids" value="" />
		</form>
		
		<script>
			$('.btn-delete-selected-products').click(function() {
				const values = $('.checkbox-product-id:checked').map((index, el) => el.value).toArray();
				if (values.length == 0) {
					alert('선택한 상품이 없습니다');
					return;
				}
				if (confirm('선택한 상품을 삭제하시겠습니까?') == false) {
					return;
				}
				$('input[name=ids]').val(values.join(','));
				$('form[name=do-delete-products-form]').submit();
			})
		</script>
			
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
</section>

<%@ include file="../../usr/common/foot.jsp"%>	