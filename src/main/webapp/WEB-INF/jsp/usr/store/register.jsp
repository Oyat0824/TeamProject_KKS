<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Store Register" />
<%@ include file="../common/head.jsp"%>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<form action="">
			<div class="table-box-type-1">
				<table class="table table-zebra w-full">
					<colgroup>
						<col width="200" />
					</colgroup>

					<tbody>
						<tr>
							<th>가게 이름</th>
							<td><input class="input input-ghost w-full text-lg border-gray-400" type="text" name="storeName" placeholder="상점 이름을 적어주세요." /></td>
						</tr>
						<tr>
							<th>가게 로고</th>
							<td><input class="file-input file-input-bordered w-full border-gray-400" type="file" name="storeLogo" placeholder="파일 첨부란" /></td>
						</tr>
						<tr>
							<th>가게 이미지</th>
							<td><input class="file-input file-input-bordered w-full border-gray-400" type="file" name="storeImg" placeholder="파일 첨부란" /></td>
						</tr>
						<tr>
							<th>가게 설명</th>
							<td><textarea class="input w-full text-lg border-gray-400" name="storeDesc" placeholder="가게 소개 글" /></textarea></td>
						</tr>
						<tr>
							<td colspan="2"><button class="btn btn-outline btn-accent w-full">가게 등록</button></td>
						</tr>
					</tbody>
				</table>
			</div>
		</form>
		<div class="btns mt-5">
			<button class="btn btn-primary" onclick="history.back();">뒤로가기</button>
		</div>
	</div>
</section>


<%@ include file="../common/foot.jsp"%>