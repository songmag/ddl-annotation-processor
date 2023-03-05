package com.mark.ddl;

import com.mark.annotation.Comment;
import com.mark.annotation.DDL;

import javax.persistence.Column;
import java.math.BigDecimal;

@DDL
public class TableTest {

    @Column(name = "test column name")
    @Comment(value = "test comment")
    private String testValue;

    @Column(name = "t")
    @Comment(value = "testPP")
    private String test;

    @Comment(value = "value test")
    private long value1;
    @Comment(value = "value-test-comment")
    private int value2;
    private BigDecimal decimal;
    private Long longValue;
    private Integer integerValue;
    private tt enums;
    private TestClass testClass;

    enum tt {

    }

    class TestClass {
        int p;
        long t;
        KO clazz;
    }

    class KO{
        int co;
        int koo;
    };
}
