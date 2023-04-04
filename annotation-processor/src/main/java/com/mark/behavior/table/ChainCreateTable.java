package com.mark.behavior.table;

import com.mark.model.TableModel;

import javax.lang.model.element.Element;
import java.util.Arrays;
import java.util.List;

public class ChainCreateTable extends CreateTable {
    List<CreateTable> createTables;
    public ChainCreateTable(CreateTable... createTables) {
        this.createTables = Arrays.asList(createTables);
    }

    public ChainCreateTable(List<CreateTable> createTables) {
        this.createTables = createTables;
    }

    @Override
    public TableModel create(Element element) {
        for (CreateTable createTable : createTables) {
            TableModel model = createTable.create(element);
            if (model != null) {
                return model;
            }
        }
        return null;
    }
}
