package com.mark.ddl;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
@DiscriminatorColumn(name = "DP_TYPE")
public abstract class SuperClassTest {
    @Id
    private Long id;
}

@Entity
@DiscriminatorValue("EX_PER_CLASS")
class ExtendsClass extends SuperClassTest implements Serializable {
    private String value1;
    private String value2;
}

@Entity
@DiscriminatorValue("EX_T")
class ExtendsClass2 extends SuperClassTest {
    private LocalDate createdAt;
    private LocalDate updatedAt;
}

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@DiscriminatorColumn()
abstract class JoinedClassTest {
    @Id
    @Column(name = "joined_class_id", nullable = false)
    private Long id;

    private LocalDate create;
    private LocalDate end;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

@Entity
@DiscriminatorValue("TE")
class JoinedClass1 extends JoinedClassTest {
    private String t;
}

@Entity
@DiscriminatorValue("TE_2")
class JoinedClass2 extends JoinedClassTest {
    private String p;
}
