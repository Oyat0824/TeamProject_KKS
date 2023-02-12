<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Member Login" />
<%@ include file="../common/head.jsp"%>

<script>
	//Submit 전 검증
	const MemberLogin__submit = function(form) {
		// 아이디 검증
		form.loginId.value = form.loginId.value.trim();
		if(form.loginId.value.length == 0) {
			alert("아이디를 입력해주세요.");
			form.loginId.focus();
			
			return false;
		}
	
		// 비밀번호 검증
		form.loginPw.value = form.loginPw.value.trim();
		if(form.loginPw.value.length == 0) {
			alert("비밀번호를 입력해주세요.");
			form.loginPw.focus();
			
			return false;
		}
	}
</script>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<h1 class="font-bold text-xl select-none mb-5">로그인</h1>
		<form action="doLogin" method="POST" onsubmit="return MemberLogin__submit(this);">
			<div class="table-box-type-1">
				<table class="table table-zebra w-full">
					<colgroup>
						<col width="200" />
					</colgroup>

					<tbody>
						<tr>
							<th>로그인 아이디</th>
							<td><input class="input input-ghost w-full text-lg border-gray-400" type="text" name="loginId" placeholder="아이디를 입력해주세요." /></td>
						</tr>
						<tr>
							<th>로그인 비밀번호</th>
							<td><input class="input input-ghost w-full text-lg border-gray-400" type="password" name="loginPw" placeholder="비밀번호를 입력해주세요." /></td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="flex flex-col mt-5">
				<div>
					<button class="btn btn-active btn-accent w-full text-white text-base">로그인</button>
				</div>
				
				<div class="flex mt-2">
					<a class="flex-auto mr-2 btn btn-primary" href="join">회원가입</a>
					<a class="flex-auto mr-2 btn btn-info" href="findLoginId">아이디 찾기</a>
					<a class="flex-auto btn btn-success" href="findLoginPw">비밀번호 찾기</a>
				</div>
			</div>
		</form>
		<div class="flex justify-end mt-5">
			<button class="btn" onclick="history.back();"><i class="fa-solid fa-right-from-bracket mr-2"></i> 뒤로가기</button>
		</div>
	</div>
</section>


<%@ include file="../common/foot.jsp"%>