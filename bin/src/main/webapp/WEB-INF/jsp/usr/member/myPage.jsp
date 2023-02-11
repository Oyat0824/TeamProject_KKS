<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Member My" />
<%@ include file="../common/head.jsp"%>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1">
			<table class="table table-zebra w-full">
				<colgroup>
					<col width="200" />
				</colgroup>

				<tbody>
					<tr>
						<th>가입일</th>
						<td>${rq.loginedMember.regDate }</td>
					</tr>
					<tr>
						<th>아이디</th>
						<td>${rq.loginedMember.loginId }</td>
					</tr>
					<tr>
						<th>이름</th>
						<td>${rq.loginedMember.name }</td>
					</tr>
					<tr>
						<th>성별</th>
						<td>${rq.loginedMember.gender == "male" ? "남자" : "여자" }</td>
					</tr>
					<tr>
						<th>생년월일</th>
						<td>${rq.loginedMember.birthday }</td>
					</tr>
					<tr>
						<th>이메일</th>
						<td>${rq.loginedMember.email }</td>
					</tr>
					<tr>
						<th>전화번호</th>
						<td>${rq.loginedMember.cellphoneNum }</td>
					</tr>
					<tr>
						<th>프로필 사진</th>
						<td><img class="w-40 h-40 mx-auto object-cover" src="${rq.getImgUri('member', rq.loginedMemberId, 'profileImg')}" onerror="${rq.getRemoveProfileImgIfNotExitOnErrorHtmlAttr() }" alt="" /></td>
					</tr>
					<tr>
						<th>회원 등급</th>
						<td>
							<c:if test="${rq.loginedMember.memberType == 3}">일반 회원</c:if>
							<c:if test="${rq.loginedMember.memberType == 6}">판매자</c:if>
							<c:if test="${rq.loginedMember.memberType == 9}">관리자</c:if>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="btns flex justify-between mt-5">
			<button class="btn btn-primary" onclick="history.back();"><i class="fa-solid fa-right-from-bracket"></i>뒤로가기</button>
			<div>
				<a class="btn btn-secondary" href="chkPassword"><i class="fa-solid fa-user-pen"></i>회원정보 수정</a>
			</div>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jsp"%>