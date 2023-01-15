package com.snippet.spring.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.validation.Validation;
import javax.validation.Validator;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * 应用程序通用配置:
 * 数据校验
 * 跨域
 * 序列化
 */
@Configuration
public class GlobalConfig {

    /**
     * 数据校验快速失败
     * 当出现一个校验失败，则返回
     * 默认是出现校验失败，继续校验直到结束
     *
     * @return Validator
     */
    @Bean
    public Validator validator() {
        return Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory()
                .getValidator();
    }

    /**
     * 配置Spring序列化响应时策略
     *
     * @param builder Jackson2ObjectMapperBuilder
     * @return ObjectMapper
     */
    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder.serializationInclusion(JsonInclude.Include.NON_NULL) // null字段不参与序列化
                .dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) // 时间格式
                .timeZone(TimeZone.getTimeZone("GMT+8:00")) // 时区
                .build();
    }

    /**
     * 后端允许跨域配置
     *
     * @return CorsFilter
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);   //带上cookie信息
        config.addAllowedOrigin(CorsConfiguration.ALL);  //允许跨域的域名， *表示允许任何域名使用
        config.addAllowedOriginPattern(CorsConfiguration.ALL);  //使用addAllowedOriginPattern 避免出现 When allowCredentials is true, allowedOrigins cannot contain the special value "*" since that cannot be set on the "Access-Control-Allow-Origin" response header. To allow credentials to a set of origins, list them explicitly or consider using "allowedOriginPatterns" instead.
        config.addAllowedHeader(CorsConfiguration.ALL);   //允许任何请求头
        config.addAllowedMethod(CorsConfiguration.ALL);   //允许任何方法（post、get等）

        source.registerCorsConfiguration("/**", config); // CORS 配置对所有接口都有效
        return new CorsFilter(source);
    }
}
