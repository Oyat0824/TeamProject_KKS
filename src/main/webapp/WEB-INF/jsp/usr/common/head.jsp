<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${pageTitle}</title>
<!-- 파비콘 불러오기 -->
<link rel="shortcut icon" href="/favicon.ico" />
<!-- 노말라이즈, 라이브러리 -->
<!-- 제이쿼리, 제이쿼리UI 불러오기 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.13.2/jquery-ui.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.13.2/themes/base/jquery-ui.min.css" />
<!-- 테일윈드, 데이지 UI 불러오기 -->
<script src="https://cdn.tailwindcss.com"></script>
<link href="https://cdn.jsdelivr.net/npm/daisyui@2.45.0/dist/full.css" rel="stylesheet" type="text/css" />
<!-- 폰트어썸 불러오기 -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css" />
<!-- 커스텀 공통 CSS -->
<link rel="stylesheet" href="/resource/common.css" />
<!-- 공통 JS -->
<script src="/resource/common.js" defer="defer"></script>
</head>
<body>
	<header>
		<div class="container mx-auto mt-2 navbar bg-base-300 rounded-box text-xl relative z-50">
			<div class="navbar-start">
				<a class="btn btn-ghost normal-case text-xl" href="/">Logo</a>
			</div>
			<div class="navbar-center hidden lg:flex">
				<ul class="menu menu-horizontal px-1">
					<li><a href="#">Item 1</a></li>
					<li><a href="#">Item 2</a></li>
					<li><a href="#">Item 3</a></li>
				</ul>
			</div>
			<div class="navbar-end">
				<c:if test="${rq.getLoginedMemberId() == 0}">
					<a class="btn btn-outline mr-2" href="/usr/member/join">JOIN</a>
					<a class="btn" href="/usr/member/login">LOGIN</a>
				</c:if>
				<c:if test="${rq.getLoginedMemberId() != 0}">
					<c:if test="${rq.getLoginedMember().getMemberType() == 6}">
						<a class="btn btn-outline mr-2 " href="/usr/store/register">가게 등록</a>
					</c:if>
					<a class="btn btn-outline mr-2 " href="/usr/member/myPage">${rq.loginedMember.name}</a>
					<a class="btn" href="/usr/member/doLogout">LOGOUT</a>
				</c:if>
			</div>
		</div>
	</header>


	<section class="my-3 text-2xl">
		<div class="container mx-auto px-3">
			<h1>${pageTitle}&nbsp;Page</h1>
		</div>
	</section>
	<main>