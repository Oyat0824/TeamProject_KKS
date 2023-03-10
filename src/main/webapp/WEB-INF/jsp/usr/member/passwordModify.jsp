<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Member Password Modify" />
<%@ include file="../common/head.jsp"%>

<script>
	const MemberModify__submit = function(form) {
		form.loginPw.value = form.loginPw.value.trim();
		form.loginPwChk.value = form.loginPwChk.value.trim();
		
		if (form.loginPw.value.length == 0) {
			alert('새 비밀번호를 입력해주세요');
			form.loginPw.focus();
		
			return false;
		}
		
		if (form.loginPwChk.value.length == 0) {
			alert('새 비밀번호 확인을 입력해주세요');
			form.loginPwChk.focus();
		
			return false;
		}
		
		if (form.loginPw.value != form.loginPwChk.value) {
			alert('비밀번호가 일치하지 않습니다');
			form.loginPw.focus();
		
			return false;
		}
		
		form.submit();
	}
</script>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<h1 class="font-bold text-xl select-none mb-5">비밀번호 변경</h1>
		<form action="doPasswordModify" method="POST" onsubmit="return MemberModify__submit(this);">
			<input type="hidden" name="memberModifyAuthKey" value="${param.memberModifyAuthKey}" />
			<div class="table-box-type-1">
				<table class="table table-zebra w-full">
					<colgroup>
						<col width="200" />
					</colgroup>

					<tbody>
						<tr>
							<th>새 비밀번호</th>
							<td><input class="bg-white input input-ghost w-full text-lg border-gray-400" type="password" name="loginPw"
								placeholder="새 비밀번호를 입력해주세요." value="" /></td>
						</tr>
						<tr>
							<th>새 비밀번호 확인</th>
							<td><input class="bg-white input input-ghost w-full text-lg border-gray-400" type="password" name="loginPwChk"
								placeholder="비밀번호 확인을 위해 입력해주세요." value="" /></td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="mt-5">
				<button class="btn btn-active btn-accent w-full text-white text-base">로그인</button>
			</div>
		</form>
		<div class="flex justify-end mt-5">
			<button class="btn" onclick="history.back();"><i class="fa-solid fa-right-from-bracket mr-2"></i> 뒤로가기</button>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jsp"%>