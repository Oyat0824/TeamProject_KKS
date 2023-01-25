<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="flex flex-col w-80 h-screen-c text-white" style="background-color: #6b90dc;">
	<div class="box-content text-center text-lg leading-7 h-7 py-3 border-t border-b ${pageTitle == 'Store Modify' ? 'bg-indigo-500' : '' }">
		<a class="block" href="modify?id=${param.id }&storeModifyAuthKey=${param.storeModifyAuthKey}">스토어 수정</a>
	</div>
	<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b ${pageTitle == 'Store Category' ? 'bg-indigo-500' : '' }">
		<a class="block" href="category?id=${param.id }&storeModifyAuthKey=${param.storeModifyAuthKey}">카테고리</a>
	</div>
	<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b">
		<a class="block" href="#">상품 목록</a>
	</div>
	<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b">
		<a class="block" href="#">스토어 삭제</a>
	</div>
	<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b">
		<a class="block" href="/usr/home/main">돌아가기</a>
	</div>
</div>