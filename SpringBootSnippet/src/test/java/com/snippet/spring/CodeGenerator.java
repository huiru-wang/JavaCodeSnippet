public class CodeGenerator {
    String url = "jdbc:mysql://ubuntu.wsl:3306/hello?useUnicode=true&characterEncoding=utf-8&userSSL=false";

    String username = "will";

    String password = "will";

    String author = "will";

    String parent = "com";

    String module = "example.demo";

    List<String> tableNames = Lists.newArrayList("t_user", "t_award_check_record");

    @Test
    public void run() {
        FastAutoGenerator.create(url, username, password).globalConfig(globalConfigBuilder -> {
            globalConfigBuilder.author(author)
                .outputDir(System.getProperty("user.dir") + "/src/main/java")
                .commentDate("yyyy-MM-dd hh:mm:ss")
                .dateType(DateType.TIME_PACK)
                .enableSwagger()
                .disableOpenDir();
        }).packageConfig(packageConfigBuilder -> {
            // =============== package ===============
            packageConfigBuilder.parent(parent)
                .moduleName(module)
                .entity("dao.entity")
                .service("service")
                .serviceImpl("service.impl")
                .mapper("dao")
                .xml("mapper.xml")
                .controller("controller")
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

                // =============== Controller ===============
                .controllerBuilder()
                // .enableHyphenStyle()
                .formatFileName("%sController") // %s 匹配表名
                .enableRestStyle() // 添加 @RestController
                .fileOverride()

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
        }).templateEngine(new VelocityTemplateEngine()).execute();
    }
}
