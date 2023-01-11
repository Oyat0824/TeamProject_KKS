<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Member Login" />
<%@ include file="../common/head.jsp"%>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<form action="doLogin">
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
						<tr>
							<td colspan="2"><button class="btn btn-outline btn-accent w-full">로그인</button></td>
						</tr>
						<tr>
							<td colspan="2">
								<a class="btn btn btn-info w-6/12" href="findLoginId">아이디 찾기</a>
								<a class="btn btn-success w-6/12" href="findLoginPw">비밀번호 찾기</a>
							</td>
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