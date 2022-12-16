package com.snippet.spring.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProperiesUtilsTest {

    @Test
    public void properties_test() {
        String propertyName = PropertyUtils.getString("project.name");
        Assertions.assertEquals(propertyName, "spring-snippet");
    }
}
