<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Store Category" />
<c:set var="menuName" value="category" />
<%@ include file="../common/head.jsp"%>
<script>
	const CategoryWrite__submitForm = function(form) {
		form.name.value = form.name.value.trim();
		form.orderNo.value = form.orderNo.value.trim();
		
		if(form.name.value.length < 2) {
			alert("2글자 이상 작성해주세요.");
			form.name.focus();
			
			return false;
		}
		
		if(form.orderNo.value == "") {
			alert("순서를 입력해주세요.");
			form.orderNo.focus();
			
			return false;
		}
		
		form.submit();
	}
	
	let originalIndex = null;
	let originalForm = null;
	
	const CategoryModify__cancel = function(idx) {
		const CategoryContent = $("#category_" + idx);
		CategoryContent.html(originalForm);
		
		originalIndex = null;
		originalForm = null;
	}
	
	const CategoryModify__getForm = function(idx, categoryId, storeModifyAuthKey) {
		if(originalIndex != null && originalForm != null) {
			CategoryModify__cancel(originalIndex);
		}
		
		$.get('../store/getCategory', {
			id : categoryId,
			ajaxMode : 'Y'
		}, function(data){
			const categoryContent = $("#category_" + idx);
			originalIndex = idx;
			originalForm = categoryContent.html();
			
			const modifyForm = `
			
				<input type="hidden" name="id" value="\${categoryId}" />
				<input type="hidden" name="storeModifyAuthKey" value="\${storeModifyAuthKey}" />
				<td><input type="number" min="1" max="10" class="input input-bordered" name="orderNo" value="\${data.data1.orderNo}" required></td>
				<td><input type="text" minlength="2" class="input input-bordered w-full" name="name" value="\${data.data1.name}" required></td>
				<td><button type="submit" class="hover:underline">수정</button></td>
				<td><a href='javascript:void(0);' onclick="CategoryModify__cancel(\${idx});" class="hover:underline">취소</a></td>
			
			`;
			
			categoryContent.html(modifyForm);
		}, 'json');
	}
</script>

<section>
	<div class="flex container mx-auto">
		<%@ include file="sideMenu.jsp"%>
		
		<div class="w-full my-10">
		
			<form action="addCategory" method="POST" onsubmit="return CategoryWrite__submitForm(this);">
				<input type="hidden" name="id" value="${param.id}" />
				<input type="hidden" name="storeModifyAuthKey" value="${param.storeModifyAuthKey}" />
				<div class="table-box-type-2">
					<table class="table w-11/12 mx-auto">
						<colgroup>
							<col width="200" />
						</colgroup>
						
						<thead>
							<tr>
								<th colspan="2" class="cHead">입력</th>
							</tr>
						</thead>

						<tbody>
							<tr>
								<th>이름</th>
								<td><input type="text" minlength="2" class="input input-bordered w-full" name="name" placeholder="카테고리 이름을 입력해주세요." required></td>
							</tr>
							<tr>
								<th>순서</th>
								<td><input type="number" min="1" max="10" class="input input-bordered w-full" name="orderNo" value="${categorys.size() + 1}" ${categorys.size() == 10 ? 'disabled' : '' } placeholder="순서를 입력해주세요." required></td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="w-11/12 mx-auto mt-5">
					<button class="btn btn-success w-full">
						등록
					</button>
				</div>
			</form>
			
			<form action="doCategoryModify" method="POST" onsubmit="return CategoryWrite__submitForm(this);">
				<div class="mt-10 w-11/12 mx-auto flex items-center bg-blue-500 text-white text-sm font-bold px-4 py-3" role="alert">
					<svg class="fill-current w-4 h-4 mr-2" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path d="M12.432 0c1.34 0 2.01.912 2.01 1.957 0 1.305-1.164 2.512-2.679 2.512-1.269 0-2.009-.75-1.974-1.99C9.789 1.436 10.67 0 12.432 0zM8.309 20c-1.058 0-1.833-.652-1.093-3.524l1.214-5.092c.211-.814.246-1.141 0-1.141-.317 0-1.689.562-2.502 1.117l-.528-.88c2.572-2.186 5.531-3.467 6.801-3.467 1.057 0 1.233 1.273.705 3.23l-1.391 5.352c-.246.945-.141 1.271.106 1.271.317 0 1.357-.392 2.379-1.207l.6.814C12.098 19.02 9.365 20 8.309 20z"/></svg>
					<p>카테고리 순서 및 갯수는 &lt; 10 &gt; 까지 작성이 가능합니다.</p>
				</div>
				<div class="mt-5 table-box-type-2">
					<table class="table w-11/12 mx-auto">
						<colgroup>
							<col width="100" />
							<col width="100%" />
							<col width="100" />
							<col width="100" />
						</colgroup>
						
						<thead>
							<tr>
								<th colspan="4" class="cHead">목록</th>
							</tr>
							
							<tr>
								<th>순서</th>
								<th>이름</th>
								<th>수정</th>
								<th>삭제</th>
							</tr>
						</thead>
							
						<tbody>
							<c:forEach var="category" items="${categorys}" varStatus="status">
								<tr id="category_${status.index}">
									<td><div class="badge badge-lg font-bold text-white">${category.orderNo}</div></td>
									<td>${category.name}</td>
									<td><a href='javascript:void(0);' onclick="CategoryModify__getForm(${status.index}, ${category.id}, '${param.storeModifyAuthKey}');" class="hover:underline">수정</a></td>
									<td><a href="doCategoryDelete?id=${category.id}&storeModifyAuthKey=${param.storeModifyAuthKey}" class="hover:underline" onclick="return confirm('삭제하시겠습니까?')">삭제</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</form>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jsp"%>