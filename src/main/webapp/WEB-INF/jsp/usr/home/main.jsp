<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="MAIN" />
<%@ include file="../common/head.jsp"%>

<style>
/*** 메인 비주얼 시작 ***/
.mainVisual {
    width: 100%;
    min-width: 1640px;
    margin: 0 auto 120px;
}

.mainVisual > .visual-wrap {
    width: 100%;
    overflow: hidden;
}

/* 메인 비주얼 > 슬라이드 페이지 */
.mainVisual > .visual-wrap > .slides {
    position: relative;
}

.mainVisual > .visual-wrap > .slides > .slide-page .box {
    position: relative;
}

.mainVisual > .visual-wrap > .slides > .slide-page .box > a {
    display: block;
}

.mainVisual > .visual-wrap > .slides > .slide-page .box > a > .img-box {
    background-repeat: no-repeat;
    background-position: center;
    background-size: cover;
    object-fit: cover;
    height: 100vh;
}

.mainVisual > .visual-wrap > .slides > .slide-page .box > a > .txt-box {
    position: absolute;
    left: calc((100% - 1640px)/2);
    bottom: 140px;
    padding-bottom: 45px;
    color: #fff;
    font-size: 48px;
    line-height: 64px;
}

/* 메인 비주얼 > 페이지 넘버 */
.mainVisual > .visual-wrap > .slides > .slide-num {
    display: flex;
    align-items: center;
    
    position: absolute;
    left: calc((100% - 1640px)/2);
    bottom: 126px;

    width: 300px;
    font-size: 14px;
    color: #fff;
}

.mainVisual > .visual-wrap > .slides > .slide-num > .page-num {
    margin-right: 10px;
}

.mainVisual > .visual-wrap > .slides > .slide-num > .page-total {
    opacity: .5;
    margin-left: 10px;
}

.mainVisual > .visual-wrap > .slides > .slide-num > .page-track {
    width: 100%;
    height: 6px;
    background-color: rgba(255, 255, 255, 0.3);
}

.mainVisual > .visual-wrap > .slides > .slide-num > .page-track > .track {
    width: 0%;
    height: 100%;
    background-color: #fff;
}

/* 메인 비주얼 > 페이지 버튼 */
.mainVisual > .visual-wrap > .slides > .slide-btn {
    display: flex;
    position: absolute;
    bottom: 112px;
    left: calc((100% - 1640px)/2 + 338px );
}

.mainVisual > .visual-wrap > .slides > .slide-btn > div {
    position: relative;
    width: 56px;
    height: 57px;
    cursor: pointer;
}

.mainVisual > .visual-wrap > .slides > .slide-btn > .prev {
    left: auto;
    background: url(https://cdn.jsdelivr.net/gh/oyat0824/Prospecs_copy/img/main_visual/btn_arrow_prev.svg) center no-repeat;
}

.mainVisual > .visual-wrap > .slides > .slide-btn > .next {
    right: -4px;
    background: url(https://cdn.jsdelivr.net/gh/oyat0824/Prospecs_copy/img/main_visual/btn_arrow_next.svg) center no-repeat;
}

/*** 메인 비주얼 끝 ***/

</style>

<script>
$(function(){
	//메인 비주얼 시작
	// 슬라이드 초기화
	    // 페이지 초기 넘버 설정
	    // 페이지 초기 트랙 설정
	    // 첫화면 비디오 대기 시간 설정
	$('.mainVisual > .visual-wrap > .slides > .slide-page').on('init', function(event, slick){
	    $(".mainVisual > .visual-wrap > .slides > .slide-num > .page-num").text("01")
		$(".mainVisual > .visual-wrap > .slides > .slide-num > .page-total").text("0" + slick.slideCount)

	    $(".mainVisual > .visual-wrap > .slides > .slide-num > .page-track > .track").css({
	        "width" : (100 / slick.slideCount) + "%"
	    })
	})

	// 슬라이더 기능
	$(".mainVisual > .visual-wrap > .slides > .slide-page").slick({
		autoplay: true,
		autoplaySpeed: 5000,
		arrows: false,
	    pauseOnHover: false,
	})

	// 넘어가기 전
	    // 페이지 넘버 관련
	    // 페이지 트랙 관련
	$('.mainVisual > .visual-wrap > .slides > .slide-page').on('beforeChange', function(event, slick, currentSlide, nextSlide){
	    $(".mainVisual > .visual-wrap > .slides > .slide-num > .page-num").text("0" + (nextSlide+1))

	    $(".mainVisual > .visual-wrap > .slides > .slide-num > .page-track > .track").css({
	        "width" : (100 / slick.slideCount) * (nextSlide+1) + "%"
	    })
	})


	// 넘어간 후
	    // 비디오 체크
	$('.mainVisual > .visual-wrap > .slides > .slide-page').on('afterChange', function(event, slick, currentSlide){
		let chk_video = slick.$slides[currentSlide].dataset.slideType

	    if(chk_video == "video") {
	        $('#ps-video')[0].currentTime = 0;
	        $('.mainVisual > .visual-wrap > .slides > .slide-page').slick("slickPause")
	        setTimeout(function() {
	            $('.mainVisual > .visual-wrap > .slides > .slide-page').slick("slickPlay")
	        }, 52600)
	    }
	})

	// 슬라이드 이전/다음 버튼
	// 만약에 슬라이드가 멈춰있다면 버튼을 누를 경우 재생
	$(".mainVisual > .visual-wrap > .slides > .slide-btn > .prev").click(function(){
		$(".mainVisual > .visual-wrap > .slides > .slide-page").slick("slickPrev")

	    if($('.mainVisual > .visual-wrap > .slides > .slide-page').slick("getSlick").paused) {
	        $('.mainVisual > .visual-wrap > .slides > .slide-page').slick("slickPlay")
	    }
	})
	$(".mainVisual > .visual-wrap > .slides > .slide-btn > .next").click(function(){
		$(".mainVisual > .visual-wrap > .slides > .slide-page").slick("slickNext")

	    if($('.mainVisual > .visual-wrap > .slides > .slide-page').slick("getSlick").paused) {
	        $('.mainVisual > .visual-wrap > .slides > .slide-page').slick("slickPlay")
	    }
	})
	//메인 비주얼 끝
});

</script>

<section class="mainVisual">
	<div class="visual-wrap">
		<div class="slides">
			<div class="slide-page eng-font">
				<div class="box" data-slide-type="img">
					<a href="javascript:void(0)">
						<div class="img-box"
							style="background-image: url(https://cdn.jsdelivr.net/gh/oyat0824/Prospecs_copy/img/main_visual/banner02.png);"></div>
						<div class="txt-box">
							<p>프로스펙스닷컴</p>
							<p>리뉴얼 오픈 이벤트</p>
						</div>
					</a>
				</div>
				<div class="box" data-slide-type="img">
					<a href="javascript:void(0)">
						<div class="img-box"
							style="background-image: url(https://cdn.jsdelivr.net/gh/oyat0824/Prospecs_copy/img/main_visual/banner03.png);"></div>
						<div class="txt-box">
							<p>PRO-SPECS</p>
							<p>SPONSORSHIP</p>
						</div>
					</a>
				</div>
				<div class="box" data-slide-type="img">
					<a href="javascript:void(0)">
						<div class="img-box"
							style="background-image: url(https://cdn.jsdelivr.net/gh/oyat0824/Prospecs_copy/img/main_visual/banner04.png);"></div>
						<div class="txt-box">
							<p>22SS NEW COLLECTION</p>
							<p>PERFORMANCE</p>
						</div>
					</a>
				</div>
				<div class="box" data-slide-type="img">
					<a href="javascript:void(0)">
						<div class="img-box"
							style="background-image: url(https://cdn.jsdelivr.net/gh/oyat0824/Prospecs_copy/img/main_visual/banner05.png);"></div>
						<div class="txt-box">
							<p>22SS NEW COLLECTION</p>
							<p>ORIGINAL</p>
						</div>
					</a>
				</div>
			</div>
			<div class="slide-num eng-font">
				<div class="page-num"></div>
				<div class="page-track">
					<div class="track"></div>
				</div>
				<div class="page-total"></div>
			</div>
			<div class="slide-btn">
				<div class="prev"></div>
				<div class="next"></div>
			</div>
		</div>
	</div>
</section>

<section class="mt-8">
	<div class="container mx-auto">
		<h1>안녕하세요</h1>
	</div>
</section>

<%@ include file="../common/foot.jsp"%>