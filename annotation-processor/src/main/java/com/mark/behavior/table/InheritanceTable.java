package com.mark.behavior.table;

import com.mark.model.TableModel;

import javax.lang.model.element.Element;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

public class InheritanceTable extends CreateTable {
    @Override
    protected TableModel createModel(Element element) {
        Inheritance inheritance = element.getAnnotation(Inheritance.class);
        if (inheritance == null) {
            return null;
        }

        TableModel tableModel = super.createModel(element);
        InheritanceType inheritanceType = inheritance.strategy();
        tableModel.setInheritanceType(inheritanceType);
        return tableModel;
    }
}
