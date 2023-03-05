package com.mark.ddl;

import com.mark.annotation.Comment;
import com.mark.annotation.DDL;

import javax.persistence.*;
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
    @Embedded
    private TestClass testClass;

    enum tt {

    }

    @Embeddable
    class TestClass {
        int p;
        long t;
        @ManyToOne
        KO clazz;
    }

    @Entity
    class KO {
        @Id
        int co;
        int koo;
    }
}
