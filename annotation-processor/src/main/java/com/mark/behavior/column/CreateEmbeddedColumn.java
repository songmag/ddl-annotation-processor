package com.mark.behavior.column;

import com.mark.model.ColumnModel;
import com.mark.model.EmbeddedColumnModel;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Embedded;
import java.util.*;
import java.util.stream.Collectors;

public class CreateEmbeddedColumn implements CreateColumn {

    @Override
    public List<ColumnModel> call(Element element) {
        Embedded embedded = element.getAnnotation(Embedded.class);
        if (embedded == null) {
            return null;
        }

        List<ColumnModel> result = new ArrayList<>();

        for (Element child : ((DeclaredType) (element.asType())).asElement().getEnclosedElements()) {
            List<ColumnModel> models = ChainCreateColumn.getInstance().makeColumn(child);
            if (models == null) {
                continue;
            }
            result.addAll(
                    models.stream()
                            .map(i -> new EmbeddedColumnModel(element, i))
                            .collect(Collectors.toList())
            );
        }


        AttributeOverrides attributeOverrides = element.getAnnotation(AttributeOverrides.class);
        if (attributeOverrides != null) {
            for (AttributeOverride attributeOverride : attributeOverrides.value()) {
                result.stream()
                        .map(i -> (EmbeddedColumnModel) i)
                        .forEach(i -> i.overrideAttribute(attributeOverride));
            }
        }

        return result;
    }
}
