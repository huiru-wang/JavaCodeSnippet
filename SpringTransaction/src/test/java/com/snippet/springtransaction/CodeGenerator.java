package com.snippet.springtransaction;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

/**
 * create by whr on 2023/3/3
 */
public class CodeGenerator {
    String url = "jdbc:mysql://ubuntu.wsl:3306/snippet?useUnicode=true&characterEncoding=utf-8&userSSL=false";

    String username = "will";

    String password = "will";

    List<String> tableNames = Lists.newArrayList("t_order");

    @Test
    public void run() {
        FastAutoGenerator.create(url, username, password).globalConfig(globalConfigBuilder -> {
                    globalConfigBuilder.author(System.getenv().get("USERNAME"))
                            .outputDir(System.getProperty("user.dir") + "/src/main/java")
                            .commentDate("yyyy-MM-dd hh:mm:ss")
                            .dateType(DateType.TIME_PACK)
                            .enableSwagger()
                            .disableOpenDir()
                            .fileOverride(); // 覆盖原有文件
                }).packageConfig(packageConfigBuilder -> {
                    // =============== package ===============
                    packageConfigBuilder.parent("com.snippet.springtransaction") // 父目录
                            .moduleName("") // module
                            .entity("dao.entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .mapper("dao.mapper")
                            .xml("mapper.xml")
                            //.controller("controller")
                            .other("config")
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    System.getProperty("user.dir") + "/src/main/resources/mapper"));
                }).strategyConfig(strategyConfigBuilder -> {
                    strategyConfigBuilder
                            // =============== strategy ===============
                            .enableCapitalMode()
                            // .enableSchema()
                            .addInclude(tableNames)
                            .addTablePrefix("t_")

                            // =============== Entity ===============
                            .entityBuilder()
                            // .enableChainModel() // 链式
                            .disableSerialVersionUID()
                            .enableTableFieldAnnotation()
                            .enableLombok()
                            // 乐观锁字段
                            .versionColumnName("version")
                            .versionPropertyName("version")
                            // 逻辑删除字段
                            .logicDeleteColumnName("deleted")
                            .logicDeletePropertyName("deleted")
                            .naming(NamingStrategy.underline_to_camel)
                            .columnNaming(NamingStrategy.underline_to_camel)
                            // 自动填充列的策略
                            .addTableFills(new Column("create_time", FieldFill.INSERT),
                                    new Column("update_time", FieldFill.INSERT_UPDATE))
                            // 主键自动分配雪花算法Id
                            .idType(IdType.ASSIGN_ID)
                            .fileOverride()

                            // =============== Controller ===============
                            .controllerBuilder()
                            //.enableHyphenStyle()
                            //.formatFileName("%sController") // %s 匹配表名
                            .enableRestStyle() // 添加 @RestController
                            //.fileOverride()

                            // =============== Service ===============
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .fileOverride()

                            // =============== Mapper ===============
                            .mapperBuilder()
                            .superClass(BaseMapper.class)
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .formatMapperFileName("%sMapper")
                            .enableMapperAnnotation()
                            .formatXmlFileName("%sXml");
                }).templateEngine(new VelocityTemplateEngine())    // 引擎选择：FreemarkerTemplateEngine也可
                .templateConfig(builder -> builder.controller("")) // 不生成controller
                //.templateConfig(builder -> builder.service("")) // 不生成service接口
                .execute();
    }
}