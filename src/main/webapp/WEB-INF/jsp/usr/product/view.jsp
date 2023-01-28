<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Product View" />
<%@ include file="../common/head.jsp"%>
<script>

</script>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1">
			<table class="table table-zebra w-full">
				<colgroup>
					<col width="200" />
				</colgroup>

				<tbody>
					<tr>
						<th>번호</th>
						<td><div class="badge badge-lg bg-purple-600 border-transparent font-bold text-white">${product.id}</div></td>
					</tr>
					<tr>
						<th>상품 등록 일</th>
						<td>${product.regDate}</td>
					</tr>
					<tr>
						<th>상품 정보 업데이트 일</th>
						<td>${product.updateDate}</td>
					</tr>
					<tr>
						<th>상품 이름</th>
						<td>${product.productName}</td>
					</tr>
					<tr>
						<th>상품 가격</th>
						<td>${product.productPrice}</td>
					</tr>
					<tr>
						<th>상품 카테고리</th>
						<td>${product.productCetegory}</td>
					</tr>
					<tr>
						<th>상품 재고</th>
						<td>${product.productStock}</td>
					</tr>
					<tr>
						<th>상품 이미지</th>
						<td><img class="object-cover mx-auto" style="width: 100px; height: 100px" src="${rq.getImgUri('product', product.id, 'productImg')}" alt="" /></td>
					</tr>
					<tr>
						<th>상품 소개</th>
						<td>${product.getForPrintDesc()}</td>
					</tr>
					<tr>
						<th>상점 이름</th>
						<td>${product.storeName}</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="btns flex justify-between mt-5">
			<button class="btn btn-primary" onclick="history.back();"><i class="fa-solid fa-right-from-bracket"></i>뒤로가기</button>
			<div>
				<a class="btn btn-secondary" href="chkPassword?id=${product.id}">수정</a>
			</div>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jsp"%>