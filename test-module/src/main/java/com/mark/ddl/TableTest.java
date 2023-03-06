package com.mark.ddl;

import com.mark.annotation.Comment;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
public class TableTest {

    @Id
    private int value;

    @Column(name = "table_name", unique = true)
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

        @Embedded
        Money a;
        @Embedded
        Money b;
    }

    @Embeddable
    class Money {
        BigDecimal amount;
        Currency currency;
    }

    @Entity
    class KO {
        @Id
        int co;
        int koo;
    }
}
