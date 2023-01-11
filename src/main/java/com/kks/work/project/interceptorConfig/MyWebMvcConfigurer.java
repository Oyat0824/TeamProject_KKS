package com.kks.work.project.interceptorConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kks.work.project.interceptor.BeforeActionInterceptor;
import com.kks.work.project.interceptor.NeedLoginInterceptor;
import com.kks.work.project.interceptor.NeedLogoutInterceptor;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
	private BeforeActionInterceptor beforeActionInterceptor;
	private NeedLoginInterceptor needLoginInterceptor;
	private NeedLogoutInterceptor needLogoutInterceptor;

	// [의존성] 생성자 주입
	@Autowired
	public MyWebMvcConfigurer(BeforeActionInterceptor beforeActionInterceptor, NeedLoginInterceptor needLoginInterceptor, NeedLogoutInterceptor needLogoutInterceptor) {
		this.beforeActionInterceptor = beforeActionInterceptor;
		this.needLoginInterceptor = needLoginInterceptor;
		this.needLogoutInterceptor = needLogoutInterceptor;
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
		// 로그아웃
		ir.addPathPatterns("/usr/member/doLogout");
		
		// 로그아웃 후 이용
		ir = registry.addInterceptor(needLogoutInterceptor);
		// 로그인
		ir.addPathPatterns("/usr/member/login");
		ir.addPathPatterns("/usr/member/doLogin");
		// 회원가입
		ir.addPathPatterns("/usr/member/join");
		ir.addPathPatterns("/usr/member/doJoin");
	}

}
