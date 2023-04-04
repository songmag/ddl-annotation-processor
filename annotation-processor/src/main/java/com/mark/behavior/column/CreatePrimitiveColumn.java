package com.mark.behavior.column;

import com.mark.model.ColumnModel;
import com.mark.model.PrimitiveColumModel;

import javax.lang.model.element.Element;
import java.util.List;

public class CreatePrimitiveColumn implements CreateColumn {

    @Override
    public List<ColumnModel> call(Element element) {
        try {
            return List.of(new PrimitiveColumModel(element));
        } catch (RuntimeException e) {
            return null;
        }
    }
}
