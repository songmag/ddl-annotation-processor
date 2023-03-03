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

    private long value1;
    private int value2;
    private BigDecimal decimal;
    private Long longValue;
    private Integer integerValue;
    private tt enums;

    enum tt {

    }

}
