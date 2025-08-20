package org.example.loginapi.common.config;

import lombok.RequiredArgsConstructor;

import org.example.loginapi.jwt.JwtFilter;
import org.example.loginapi.jwt.JwtUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtUtil jwtUtil;

	@Bean
	public FilterRegistrationBean<JwtFilter> jwtFilter() {
		FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new JwtFilter(jwtUtil));
		registrationBean.addUrlPatterns("/*");
		registrationBean.setOrder(1);

		return registrationBean;
	}
}
