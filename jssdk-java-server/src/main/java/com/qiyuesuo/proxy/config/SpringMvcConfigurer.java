package com.qiyuesuo.proxy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfigurer implements WebMvcConfigurer {

	/**
	 * 跨域支持
	 */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET","POST","DELETE","OPTIONS","PUT");
        registry.addMapping("/**/**").allowedMethods("GET","POST","DELETE","OPTIONS","PUT");
    }

}
