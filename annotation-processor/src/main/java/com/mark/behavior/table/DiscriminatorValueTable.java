package com.mark.behavior.table;

import com.mark.ElementUtil;
import com.mark.model.ColumnModel;
import com.mark.model.TableModel;
import com.mark.processor.DDLAnnotationProcessor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.persistence.DiscriminatorValue;
import javax.persistence.InheritanceType;
import java.util.List;
import java.util.Set;

public class DiscriminatorValueTable extends CreateTable {
    @Override
    protected TableModel createModel(Element element) {
        DiscriminatorValue value = element.getAnnotation(DiscriminatorValue.class);
        if (value == null) {
            return null;
        }

        Element superClass = ElementUtil.getInstance().getSuperClassElement(element);
        String superClassName = superClass.getSimpleName().toString();
        TableModel model = DDLAnnotationProcessor.MODELS.get(superClassName);
        if (model == null) {
            return null;
        }

        if (model.getInheritanceType() == InheritanceType.JOINED) {
             TableModel tableModel = super.createModel(element);
             ColumnModel idColumn = model.getIdColumn();
             Set<ColumnModel> columns = model.columns();
            for (ColumnModel column : columns) {
                if(column.equals(idColumn)){
                    continue;
                }
                tableModel.removeColumn(column);
            }
        }

        if (model.getInheritanceType() == InheritanceType.TABLE_PER_CLASS) {
            return super.createModel(element);
        }

        return model;
    }
}
