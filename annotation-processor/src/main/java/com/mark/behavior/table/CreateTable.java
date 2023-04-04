package com.mark.behavior.table;

import com.mark.annotation.Comment;
import com.mark.behavior.column.ChainCreateColumn;
import com.mark.behavior.column.CreateColumn;
import com.mark.model.TableModel;

import javax.lang.model.element.Element;
import javax.persistence.Table;

public abstract class CreateTable {

    protected CreateColumn createColumn = ChainCreateColumn.getInstance();

    public TableModel create(Element element) {
        TableModel model = createModel(element);
        if (model == null) {
            return null;
        }
        return createColumn(element, model);
    }

    protected TableModel createModel(Element element) {
        Table tableValue = element.getAnnotation(Table.class);

        String tableName;
        if (tableValue == null) {
            tableName = element.getSimpleName().toString();
        } else if (tableValue.name().isBlank()) {
            tableName = element.getSimpleName().toString();
        } else {
            tableName = tableValue.name();
        }

        Comment comment = element.getAnnotation(Comment.class);
        TableModel model;
        if (comment == null) {
            model = new TableModel(tableName);
        } else {
            model = new TableModel(tableName, comment.value());
        }

        return model;
    }

    protected TableModel createColumn(Element element, TableModel model) {
        model.addAllColumn(createColumn.call(element));
        return model;
    }
}
