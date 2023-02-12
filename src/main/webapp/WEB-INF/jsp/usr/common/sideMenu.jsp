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
		<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b ${menuName == 'modify' ? 'text-indigo-600 font-bold' : '' }">
			<a class="block" href="/usr/store/modify?id=${param.id }&storeModifyAuthKey=${param.storeModifyAuthKey}">스토어 수정</a>
		</div>
		<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b ${menuName == 'category' ? 'text-indigo-600 font-bold' : '' }">
			<a class="block" href="/usr/store/category?id=${param.id }&storeModifyAuthKey=${param.storeModifyAuthKey}">카테고리</a>
		</div>
		<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b ${menuName == 'banner' ? 'text-indigo-600 font-bold' : '' }">
			<a class="block" href="/usr/store/banner?id=${param.id }&storeModifyAuthKey=${param.storeModifyAuthKey}">배너 등록</a>
		</div>
		<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b ${menuName == 'productReg' ? 'text-indigo-600 font-bold' : '' }">
			<a class="block" href="/usr/product/register?id=${param.id }&storeModifyAuthKey=${param.storeModifyAuthKey}">상품 등록</a>
		</div>
		<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b ${menuName == 'productList' ? 'text-indigo-600 font-bold' : '' }">
			<a class="block" href="/usr/product/list?id=${param.id }&storeModifyAuthKey=${param.storeModifyAuthKey}">상품 목록</a>
		</div>
		<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b">
			<a class="block" href="#">스토어 삭제</a>
		</div>
		<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b">
			<a class="block" href="/usr/store/view?id=${param.id }">내 스토어로</a>
		</div>
		<div class="box-content text-center text-lg leading-7 h-7 py-3 border-b">
			<a class="block" href="/usr/home/main">메인으로</a>
		</div>
	</div>
</div>