package com.mark.model;

import com.mark.ElementUtil;

import javax.lang.model.element.Element;

public class PrimitiveColumModel extends ColumnModel {
    public PrimitiveColumModel(Element element) {
        super(element);
    }

    @Override
    protected String setType(Element element) {
        for (DBType t : DBType.values()) {
            if (ElementUtil.getInstance().getTypes(element).toString().equals(t.javaType)) {
                return t.dbType;
            }
        }
        throw new RuntimeException("Can`t Create Primitive Type");
    }
}
