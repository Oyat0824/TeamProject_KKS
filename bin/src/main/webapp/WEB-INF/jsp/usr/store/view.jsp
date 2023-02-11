<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Store View" />
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
						<td><div class="badge badge-lg bg-purple-600 border-transparent font-bold text-white">${store.id}</div></td>
					</tr>
					<tr>
						<th>스토어 시작 일</th>
						<td>${store.regDate}</td>
					</tr>
					<tr>
						<th>스토어 수정 일</th>
						<td>${store.updateDate}</td>
					</tr>
					<tr>
						<th>스토어 이름</th>
						<td>${store.storeName}</td>
					</tr>
					<tr>
						<th>스토어 로고</th>
						<td><img class="object-cover mx-auto" style="width: 160px; height: 50px" src="${rq.getImgUri('store', store.id, 'storeLogo')}" alt="" /></td>
					</tr>
					<tr>
						<th>스토어 이미지</th>
						<td><img class="object-cover mx-auto" style="width: 100px; height: 100px" src="${rq.getImgUri('store', store.id, 'storeImg')}" alt="" /></td>
					</tr>
					<tr>
						<th>스토어 소개</th>
						<td>${store.getForPrintDesc()}</td>
					</tr>
					<tr>
						<th>판매자 이름</th>
						<td>${store.sellerName}</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="btns flex justify-between mt-5">
			<button class="btn btn-primary" onclick="history.back();">뒤로가기</button>
			<c:if test="${store.actorCanChangeData}">
				<div>
					<a class="btn btn-secondary" href="chkPassword?id=${store.id}">수정</a>
				</div>
			</c:if>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jsp"%>