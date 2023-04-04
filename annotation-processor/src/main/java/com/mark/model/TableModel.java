package com.mark.model;

import javax.persistence.Column;
import javax.persistence.InheritanceType;
import java.util.*;

public class TableModel {

    String tableName;
    String comment;
    InheritanceType inheritanceType = null;

    ColumnModel idColumn;
    Map<String, ColumnModel> columns = new HashMap<>();

    public TableModel(String tableName) {
        this.tableName = tableName;
    }

    public TableModel(String tableName, String comment) {
        this.tableName = tableName;
    }

    public TableModel(InheritanceType inheritanceType, String tableName, String comment) {
        this.inheritanceType = inheritanceType;
        this.tableName = tableName;
        this.comment = comment;
    }

    public ColumnModel getIdColumn(){
        return idColumn;
    }

    public Set<ColumnModel> columns(){
        return new HashSet<>(this.columns.values());
    }

    public String getTableName() {
        return tableName;
    }

    public void setInheritanceType(InheritanceType inheritanceType) {
        this.inheritanceType = inheritanceType;
    }

    public InheritanceType getInheritanceType() {
        return this.inheritanceType;
    }

    public void addColumn(ColumnModel column) {
        if (this.columns.containsKey(column)) {
            return;
        }
        if (column.isId()) {
            this.idColumn = column;
        }
        this.columns.put(column.getColumnName(), column);
    }

    public void addAllColumn(List<ColumnModel> columnModels) {
        for (ColumnModel columnModel : columnModels) {
            addColumn(columnModel);
        }
    }

    public void removeColumn(ColumnModel columnModel) {
        this.columns.remove(columnModel.getColumnName());
    }

    public boolean containsColumn(String columnName) {
        return this.columns.containsKey(columnName);
    }

    public void tableToQuery(StringBuilder builder) {
        if (this.inheritanceType == InheritanceType.TABLE_PER_CLASS) {
            return;
        }
        builder.append("create table ")
                .append(tableName)
                .append("(\n");

        columns.values().forEach(column -> column.makeColumn(builder));

        builder.append(")");

        if (comment != null && !comment.isBlank()) {
            builder.append(" comment ")
                    .append(comment);
        }
        builder.append(";\n");
    }

    public boolean isInheritance() {
        return inheritanceType != null;
    }

    @Override
    public int hashCode() {
        return this.tableName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (((TableModel) (obj)).tableName.equals(this.tableName)) {
            return true;
        }
        return false;
    }
}
