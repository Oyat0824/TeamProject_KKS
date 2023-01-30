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
		<div class="xl:container mx-auto">
			<div class="h-10 mx-5 flex items-center justify-between text-white">
				<div>
					<a class="font-bold" href="/">SB_Store</a>
				</div>
				<div class="gnb relative">
					<c:if test="${rq.getLoginedMemberId() == 0}">
						<div class="w-14 h-6 bg-white bg-opacity-5 border border-black border-opacity-10 text-center text-sm">
							<a href="/usr/member/login">로그인</a>
						</div>
					</c:if>
					<c:if test="${rq.getLoginedMemberId() != 0}">
						<script>
							const lyrOpen = function() {
								$(".my_lyr").toggleClass("open_lyr");
							}
							
							$(function(){
								$("html").click(function(e) {   
									if($(e.target).parents(".open_lyr").length < 1 && !$(e.target).hasClass('lyr')){   
										$(".my_lyr").removeClass("open_lyr");
									}
								});
							});
						</script>
						<div class="text-sm">
							<a class="lyr hover:underline" onclick="lyrOpen(); return false;" href="javascript:void();" >${rq.loginedMember.name}<i class="lyr fa-solid fa-caret-down ml-1"></i></a>
						</div>
						
						<div class="my_lyr">
							<div class="flex items-center py-4 px-2 text-gray-600">
								<div class="img_area">
									<img class="w-20 object-cover rounded-full border-2" style="aspect-ratio: 1/1" src="${rq.getImgUri('member', rq.loginedMemberId, 'profileImg')}" alt="" />
								</div>
								<div class="ml-3 text-area">
									<p class="account">
										<span class="align-middle"><a class="font-bold text-black hover:underline" href="/usr/member/myPage">${rq.loginedMember.name}</a>님</span>
										<span class="inline-block w-16 h-6 bg-white bg-opacity-5 border border-black border-opacity-10 text-center text-sm">
											<a href="/usr/member/doLogout">로그아웃</a>
										</span>
									</p>
									<p class="text-sm">${rq.loginedMember.email}</p>
								</div>
							</div>
							
							<div class="flex flex-1 text-sm font-bold text-black">
								<div class="flex items-center justify-center flex-1 bg-gray-100 border-t border-gray-300 text-center font-bold">
									<a href="/usr/member/myPage">마이 페이지</a>
								</div>
								<c:if test="${rq.getLoginedMember().getMemberType() == 6 && rq.getLoginedMember().getStoreState() == 0}">
									<div class="border-l flex items-center justify-center flex-1 bg-gray-100 border-t border-gray-300 text-center font-bold">
										<a href="/usr/store/register">스토어 등록</a>
									</div>
								</c:if>
								<c:if test="${rq.getLoginedMember().getMemberType() == 6 && rq.getLoginedMember().getStoreState() == 1}">
									<div class="border-l flex items-center justify-center flex-1 bg-gray-100 border-t border-gray-300 text-center font-bold">
										<a href="/usr/store/view?id=${rq.getLoginedMember().getStoreId() }">내 스토어</a>
									</div>
									<div class="border-l flex items-center justify-center flex-1 bg-gray-100 border-t border-gray-300 text-center font-bold">
										<a href="/usr/store/chkPassword?id=${rq.getLoginedMember().getStoreId() }">스토어 관리</a>
									</div>
								</c:if>
							</div>
						</div>
					</c:if>
				</div>
			</div>
			<div class="h-16 mx-5 flex items-center justify-between text-white">
				<div><a href="/usr/store/list">스토어 목록</a></div>
			</div>
		</div>
	</header>
	
	<main>