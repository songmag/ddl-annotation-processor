package com.mark.model;


import javax.lang.model.element.Element;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;

public class EmbeddedColumnModel extends ColumnModel {
    Element rootElement;
    ColumnModel column;

    public EmbeddedColumnModel(
            Element element,
            ColumnModel column
    ) {
        this.rootElement = element;
        this.column = column;
    }

    @Override
    public String getColumnName() {
        return column.getColumnName();
    }

    @Override
    public String getElementName() {
        return rootElement.getSimpleName() + "." + column.getElementName();
    }

    public void overrideAttribute(AttributeOverride attributeOverride) {
        String name = attributeOverride.name();
        if (this.getElementName().equals(name)) {
            initColumnAnnotation(attributeOverride.column());
            return;
        }
        if (this.column instanceof EmbeddedColumnModel) {
            ((EmbeddedColumnModel) this.column).overrideAttribute(attributeOverride);
            return;
        }
        if (this.column.getElementName().equals(name)) {
            this.column.initColumnAnnotation(attributeOverride.column());
        }
    }

    @Override
    public void initColumnAnnotation(Column column) {
        this.column.initColumnAnnotation(column);
    }

    @Override
    protected ColumnModel getColumnModel() {
        ColumnModel model = this.column;
        while (model instanceof EmbeddedColumnModel) {
            model = model.getColumnModel();
        }
        return model;
    }
}
