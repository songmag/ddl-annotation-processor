package com.mark.behavior.column;

import com.mark.annotation.Comment;
import com.mark.model.ColumnModel;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

public class CreateManyToOneColumn implements CreateColumn {


    @Override
    public List<ColumnModel> call(Element element) {
        ManyToOne manyToOne = element.getAnnotation(ManyToOne.class);
        if (manyToOne == null) {
            return null;
        }
        JoinColumn joinColumn = element.getAnnotation(JoinColumn.class);
        ColumnModel model = null;
        ChainCreateColumn createColumns = ChainCreateColumn.getInstance();
        if (joinColumn == null) {
            Element childElem = ((DeclaredType) (element.asType())).asElement().getEnclosedElements()
                    .stream()
                    .filter(i -> i.getKind().isField())
                    .filter(i -> i.getAnnotation(Id.class) != null)
                    .findFirst()
                    .get();
            List<ColumnModel> columns = createColumns.makeColumn(childElem);
            model = columns.get(0);
        } else {
            Element childElem;
            if (!joinColumn.referencedColumnName().isBlank()) {
                childElem = ((DeclaredType) (element.asType())).asElement().getEnclosedElements()
                        .stream()
                        .filter(i -> i.getKind().isField())
                        .filter(i -> i.getSimpleName().toString().equals(joinColumn.referencedColumnName()))
                        .findFirst()
                        .get();
            } else {
                childElem = ((DeclaredType) (element.asType())).asElement().getEnclosedElements()
                        .stream()
                        .filter(i -> i.getKind().isField())
                        .filter(i -> i.getAnnotation(Id.class) != null)
                        .findFirst()
                        .get();
            }
            model = createColumns.makeColumn(childElem).get(0);
            if (!joinColumn.name().isBlank()) {
                model.setName(joinColumn.name());
            }
        }
        if (model == null) {
            return null;
        }
        Comment comment = element.getAnnotation(Comment.class);
        if (comment != null) {
            model.setComment(comment.value());
        } else {
            String connectElementName = ((DeclaredType) (element.asType())).asElement().getSimpleName().toString();
            model.setComment(connectElementName + " Entity join key");
        }
        model.setId(false);
        return List.of(model);
    }
}
