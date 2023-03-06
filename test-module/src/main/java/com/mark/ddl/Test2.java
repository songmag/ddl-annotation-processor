package com.mark.ddl;

import com.mark.annotation.DDL;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity()
@DDL(value = "test_table2")
public class Test2 {
    @Id
    int column;


}
