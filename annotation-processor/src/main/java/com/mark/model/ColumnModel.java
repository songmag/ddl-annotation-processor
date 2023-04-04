package com.mark.model;


import com.mark.annotation.Comment;

import javax.lang.model.element.Element;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public abstract class ColumnModel {

    private String elementName;
    private String columnName;
    private String type;
    private String comment;

    private String columnDefined;
    private boolean unique;
    private boolean nullable;
    private boolean autoIncrement;
    private boolean id;

    private boolean insertable = true;

    public ColumnModel() {
    }

    public ColumnModel(Element element) {
        this.columnName = element.getSimpleName().toString();
        this.elementName = columnName;
        if (element.getAnnotation(Id.class) != null) {
            this.id = true;
        }
        Column column = element.getAnnotation(Column.class);
        Comment comment = element.getAnnotation(Comment.class);

        if (column == null) {
            this.nullable = false;
            this.unique = false;
        } else {
            this.initColumnAnnotation(column);
        }

        if (comment != null) {
            this.comment = comment.value();
        }
        String type = setType(element);
        this.type = type;
        if (type.equals("bigInt") || type.equals("int")) {
            GeneratedValue value = element.getAnnotation(GeneratedValue.class);
            if (value == null) {
                this.autoIncrement = false;
            } else {
                if (value.strategy().equals(GenerationType.IDENTITY)) {
                    this.autoIncrement = true;
                }
            }
        }
    }

    public String getElementName() {
        return this.elementName;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public void setName(String name) {
        this.columnName = name;
    }

    protected String setType(Element element) {
        for (DBType t : DBType.values()) {
            if (element.asType().toString().equals(t.javaType)) {
                return t.dbType;
            }
        }
        return "";
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void initColumnAnnotation(Column column) {
        if (!column.name().isBlank()) {
            this.columnName = column.name();
        }
        this.nullable = column.nullable();
        this.unique = column.unique();
        if (!column.columnDefinition().isBlank()) {
            this.columnDefined = column.columnDefinition();
        }
        insertable = column.insertable();
    }

    protected ColumnModel getColumnModel() {
        return this;
    }

    public void makeColumn(StringBuilder builder) {
        ColumnModel model = getColumnModel();
        if (!model.insertable) {
            return;
        }
        builder.append("\t`")
                .append(model.columnName)
                .append("` ");

        if (model.columnDefined != null && !model.columnDefined.isBlank()) {
            builder.append(model.columnDefined);
            builder.append("\n");
            return;
        }

        builder.append(model.type);

        if (model.autoIncrement) {
            builder.append(" AUTO_INCREMENT");
        }

        if (model.nullable) {
            builder.append(" NULL");
        } else {
            builder.append(" NOT NULL");
        }

        if (model.unique) {
            builder.append(" UNIQUE");
        }

        if (model.comment != null) {
            builder.append(" comment '")
                    .append(model.comment)
                    .append("'");
        }

        builder.append("\n");
    }

    public String getColumnName() {
        return columnName;
    }
}
