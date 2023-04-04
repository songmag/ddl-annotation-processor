package com.mark.ddl;

import javax.persistence.*;

@Entity
public class EmbeddableTest2 {
    @Id
    long id;

    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "money.amount", column = @Column(name = "amount1")),
                    @AttributeOverride(name = "money2.amount", column = @Column(name = "amount2")),
                    @AttributeOverride(name = "money2.currency", column = @Column(name = "update-currency")),
            }
    )
    MoneyEmbeddable2 embeddable2;

    @Embedded
    MoneyEmbeddable2 embeddable3;

    @Embeddable
    public static class MoneyEmbeddable2 {
        @Embedded
        @AttributeOverrides(
                {
                        @AttributeOverride(name = "amount", column = @Column(name = "test_amount")),
                        @AttributeOverride(name = "currency", column = @Column(insertable = false))
                }
        )
        private TableTest.Money money;

        @Embedded
        @AttributeOverrides(
                {@AttributeOverride(name = "amount", column = @Column(name = "test_amount_2"))}
        )
        private TableTest.Money money2;
    }
}
