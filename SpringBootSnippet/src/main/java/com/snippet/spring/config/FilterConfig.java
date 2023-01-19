//package com.snippet.spring.config;
//
//import com.snippet.spring.filter.AuthFilter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//
///**
// * 顺序：<br/>
// * Filter --> Servlet --> Interceptor --> Controller --> Interceptor<br/>
// * Filter：基于函数回调，根据配置的多个Filter遍历执行(@WebFilter, @ServletComponentScan)<br/>
// * Interceptor：基于AOP，针对Controller设置前后通知(需要实现HandlerInterceptor ，配置InterceptorConfig)<br/>
// * <p>
// * 注意：@WebFilter的Bean会和Interceptor的配置冲突，使得Interceptor注册不生效,使用：FilterRegistrationBean
// */
////@Configuration
//public class FilterConfig {
//
//    //@Bean
//    public FilterRegistrationBean<AuthFilter> authFilterFilterRegistrationBean() {
//        FilterRegistrationBean<AuthFilter> authFilter = new FilterRegistrationBean<>();
//        authFilter.setFilter(new AuthFilter());
//        authFilter.addUrlPatterns("/api/user/*");
//        authFilter.setName("authFilter");
//        authFilter.setOrder(1);
//        return authFilter;
//    }
//}
