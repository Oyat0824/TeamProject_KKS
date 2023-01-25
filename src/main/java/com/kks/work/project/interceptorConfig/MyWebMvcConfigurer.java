package com.kks.work.project.interceptorConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kks.work.project.interceptor.BeforeActionInterceptor;
import com.kks.work.project.interceptor.NeedLoginInterceptor;
import com.kks.work.project.interceptor.NeedLogoutInterceptor;
import com.kks.work.project.interceptor.NeedSellerInterceptor;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
	private BeforeActionInterceptor beforeActionInterceptor;
	private NeedLoginInterceptor needLoginInterceptor;
	private NeedLogoutInterceptor needLogoutInterceptor;
	private NeedSellerInterceptor needSellerInterceptor;
	
	// 파일 생성 경로
	@Value("${custom.genFileDirPath}")
	private String genFileDirPath;

	// [의존성] 생성자 주입
	@Autowired
	public MyWebMvcConfigurer(BeforeActionInterceptor beforeActionInterceptor,
			NeedLoginInterceptor needLoginInterceptor, NeedLogoutInterceptor needLogoutInterceptor,
			NeedSellerInterceptor needSellerInterceptor) {
		this.beforeActionInterceptor = beforeActionInterceptor;
		this.needLoginInterceptor = needLoginInterceptor;
		this.needLogoutInterceptor = needLogoutInterceptor;
		this.needSellerInterceptor = needSellerInterceptor;
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/gen/**")
				.addResourceLocations("file:///" + genFileDirPath + "/")
				.setCachePeriod(20);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration ir;

		ir = registry.addInterceptor(beforeActionInterceptor);
		ir.addPathPatterns("/**");
		ir.addPathPatterns("/favicon.ico");
		ir.excludePathPatterns("/resource/**");
		ir.excludePathPatterns("/error");

		// 로그인 후 이용
		ir = registry.addInterceptor(needLoginInterceptor);
		// 스토어
		ir.addPathPatterns("/usr/store/**");
		// 로그아웃
		ir.addPathPatterns("/usr/member/doLogout");
		// 회원정보 관련
		ir.addPathPatterns("/usr/member/myPage");
		ir.addPathPatterns("/usr/member/chkPassword");
		ir.addPathPatterns("/usr/member/doChkPassword");
		ir.addPathPatterns("/usr/member/modify");
		ir.addPathPatterns("/usr/member/doModify");
		ir.addPathPatterns("/usr/member/passwordModify");
		ir.addPathPatterns("/usr/member/doPasswordModify");

		// 로그아웃 후 이용
		ir = registry.addInterceptor(needLogoutInterceptor);
		// 회원가입
		ir.addPathPatterns("/usr/member/join");
		ir.addPathPatterns("/usr/member/doJoin");
		ir.addPathPatterns("/usr/member/getLoginIdDup");
		// 로그인
		ir.addPathPatterns("/usr/member/login");
		ir.addPathPatterns("/usr/member/doLogin");
		// 아이디/비밀번호 찾기
		ir.addPathPatterns("/usr/member/findLoginId");
		ir.addPathPatterns("/usr/member/doFindLoginId");
		ir.addPathPatterns("/usr/member/findLoginPw");
		ir.addPathPatterns("/usr/member/doFindLoginPw");
		
		// 판매자만 이용
		ir = registry.addInterceptor(needSellerInterceptor);
		// 스토어 관련
		ir.addPathPatterns("/usr/store/**");
		ir.excludePathPatterns("/usr/store/view");
	}

}
