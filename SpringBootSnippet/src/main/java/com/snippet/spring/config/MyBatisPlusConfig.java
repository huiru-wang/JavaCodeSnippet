package com.snippet.spring.config;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.Date;


@Configuration
@MapperScan("com.snippet.spring.dao.mapper")
public class MyBatisPlusConfig {

    @Bean
    public EasySqlInjector easySqlInjector() {
        return new EasySqlInjector();
    }

    @Bean
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(@Autowired HikariDataSource hikariDataSource, @Autowired GlobalConfig globalConfig) throws IOException {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(hikariDataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        sqlSessionFactoryBean.setGlobalConfig(globalConfig);
        return sqlSessionFactoryBean;
    }

    @Bean
    public GlobalConfig globalConfig() {
        // db config
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setLogicDeleteValue("1");
        dbConfig.setLogicNotDeleteValue("0");
        dbConfig.setTablePrefix("t_");

        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setBanner(false);
        globalConfig.setDbConfig(dbConfig);
        globalConfig.setMetaObjectHandler(new CustomMetaObjectHandler());
        globalConfig.setIdentifierGenerator(new CustomIdGenerator());
        return globalConfig;
    }


    /**
     * 自动填充配置
     * 需要在对应字段添加：@TableField(fill = FieldFill.INSERT_UPDATE)
     */
    public class CustomMetaObjectHandler implements MetaObjectHandler {

        @Override
        public void insertFill(MetaObject metaObject) {
            this.setFieldValByName("createTime", new Date(), metaObject);
            this.setFieldValByName("updateTime", new Date(), metaObject);
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            this.setFieldValByName("updateTime", new Date(), metaObject);
        }
    }

    /**
     * 注入自定义的主键生成策略(自定义SQL无效)
     * 主键字段上添加：@TableId(type = IdType.ASSIGN_ID)
     */
    class CustomIdGenerator implements IdentifierGenerator {
        @Override
        public Number nextId(Object entity) {
            // 自定义生成策略,并在@TableId()
            return IdUtil.getSnowflakeNextId();
        }
    }

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
