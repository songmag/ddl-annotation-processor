package com.mark.behavior.column;

import com.mark.model.ColumnModel;
import com.mark.model.EnumColumnModel;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import java.util.List;

public class CreateEnumColumn implements CreateColumn {

    @Override
    public List<ColumnModel> call(Element element) {
        if (element.asType().getKind() == TypeKind.DECLARED) {
            return ((DeclaredType) (element.asType())).asElement().getKind() == ElementKind.ENUM ? List.of(new EnumColumnModel(element)) : null;
        }
        return null;
    }
}
