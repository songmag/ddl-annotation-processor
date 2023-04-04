package com.mark.model;

public enum DBType {
    Int("int", "int"),
    Integer("java.lang.Integer", "int"),
    LongWrapper("java.lang.Long", "bigint"),
    Long("long", "bigint"),
    BigDecimal("java.math.BigDecimal", "decimal(16,2)"),
    String("java.lang.String", "varchar(255)"),
    ZonedDateTime("java.time.ZonedDateTime", "datetime"),
    ZonedDate("java.time.ZonedDate", "date"),
    LocalDateTime("java.time.LocalDateTime", "datetime"),
    LocalDate("java.time.LocalDate", "date");
    String javaType;
    String dbType;

    DBType(String javaType, String dbType) {
        this.javaType = javaType;
        this.dbType = dbType;
    }
}
