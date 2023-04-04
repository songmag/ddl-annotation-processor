package com.mark.model;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;


public class EnumColumnModel extends ColumnModel {

    public EnumColumnModel(Element element) {
        super(element);
    }

    @Override
    protected String setType(Element element) {
        Enumerated enumerated = element.getAnnotation(Enumerated.class);
        if (enumerated == null || enumerated.value().equals(EnumType.STRING)) {
            return "varchar(255)";
        }

        List<? extends Element> enclosedElements = ((DeclaredType) element.asType()).asElement()
                .getEnclosedElements();

        return "ENUM()";
    }
}
