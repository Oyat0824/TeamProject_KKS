<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
	$(function(){
		let num = $("section").height();
		
		$("#sideMenu").css("height", num);
	})
</script>


<div id="sideMenu" class="flex flex-col w-80 h-screen-c border-r">
	<div>
		<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b ${menuName == 'noticeWrite' ? 'text-indigo-600 font-bold' : '' }">
			<a href="/admin/notice/write">공지 등록</a>
		</div>
		<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b ${menuName == 'noticeList' ? 'text-indigo-600 font-bold' : '' }">
			<a href="/admin/notice/list">공지 사항</a>
		</div>
		<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b ${menuName == 'memberList' ? 'text-indigo-600 font-bold' : '' }">
			<a href="/admin/member/list">멤버 관리</a>
		</div>
		<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b ${menuName == 'storeList' ? 'text-indigo-600 font-bold' : '' }">
			<a href="/admin/store/list">스토어 관리</a>
		</div>
		<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b ${menuName == 'productList' ? 'text-indigo-600 font-bold' : '' }">
			<a href="/admin/product/list">상품 관리</a>
		</div>
		<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b">
			<a class="block" href="/usr/home/main">돌아가기</a>
		</div>
	</div>
</div>