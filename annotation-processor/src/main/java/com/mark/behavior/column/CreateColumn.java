package com.mark.behavior.column;

import com.mark.model.ColumnModel;

import javax.lang.model.element.Element;
import java.util.List;

public interface CreateColumn {
    List<ColumnModel> call(Element element);

}
