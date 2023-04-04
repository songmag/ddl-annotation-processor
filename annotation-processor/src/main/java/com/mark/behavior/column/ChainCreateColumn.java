package com.mark.behavior.column;

import com.mark.ElementUtil;
import com.mark.model.ColumnModel;
import com.mark.processor.DDLAnnotationProcessor;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

public class ChainCreateColumn implements CreateColumn {

    private static ChainCreateColumn INSTANCE;
    List<CreateColumn> columns = List.of(
            new CreateEmbeddedColumn(),
            new CreateManyToOneColumn(),
            new CreateEnumColumn(),
            new CreatePrimitiveColumn()
    );

    public static ChainCreateColumn getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChainCreateColumn();
        }
        return INSTANCE;
    }

    @Override
    public List<ColumnModel> call(Element element) {
        List<ColumnModel> columnModels = new ArrayList<>();
        for (Element child : ElementUtil.getInstance().getAllElements(element)) {
            if (!child.getKind().isField()) continue;
            if (child.getAnnotation(Transient.class) != null) continue;
            List<ColumnModel> models = makeColumn(child);
            if (models == null) {
                continue;
            }
            columnModels.addAll(models);
        }
        return columnModels;
    }

    public List<ColumnModel> makeColumn(Element element) {
        for (CreateColumn column : columns) {
            List<ColumnModel> model = column.call(element);
            if (model != null) {
                return model;
            }
        }
        return null;
    }
}
