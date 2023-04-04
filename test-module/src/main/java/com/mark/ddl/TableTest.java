package com.mark.ddl;

import com.mark.annotation.Comment;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDateTime time1;
    private LocalDate date;
    private tt enums;

    enum tt {

    }

    enum Currency {
        KRW,
        JPY,
        USD;
    }

    @Embeddable
    class TestClass {

        @Embedded
        @AttributeOverrides(
                {
                        @AttributeOverride(name = "amount", column = @Column(name = "multi"))
                }
        )
        Money a;
        @Embedded
        @AttributeOverrides(
                {
                        @AttributeOverride(name = "amount", column = @Column(name = "multi-B"))
                }
        )
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
