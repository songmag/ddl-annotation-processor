package com.mark.ddl;

import com.mark.model.TableModel;

import javax.persistence.*;

@Entity
public class JoinTableTest {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;


    @ManyToOne()
    @JoinColumn(name = "join_table_column_1")
    public TableTest tableTest;

    @ManyToOne()
    @JoinColumn(referencedColumnName = "decimal")
    public TableTest tableTest2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
