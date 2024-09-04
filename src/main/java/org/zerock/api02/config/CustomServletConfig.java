package org.zerock.api02.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CustomServletConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/files/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //.allowedOrigins("http://localhost:8082", "http://localhost:8081") // 허용할 출처
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST") // 허용할 HTTP method
                .allowedHeaders("*")
                .allowCredentials(true) // 쿠키 인증 요청 허용
        //        .maxAge(3000)
        ; // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱
    }
}
