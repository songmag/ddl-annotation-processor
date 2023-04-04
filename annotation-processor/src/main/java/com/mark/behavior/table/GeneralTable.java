package com.mark.behavior.table;

import com.mark.model.TableModel;

import javax.lang.model.element.Element;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Inheritance;

public class GeneralTable extends CreateTable {
    @Override
    protected TableModel createModel(Element element) {
        if (element.getAnnotation(Inheritance.class) != null) return null;
        if (element.getAnnotation(DiscriminatorValue.class) != null) return null;

        return super.createModel(element);
    }
}
