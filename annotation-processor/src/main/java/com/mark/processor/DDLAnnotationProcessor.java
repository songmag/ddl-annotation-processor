package com.mark.processor;

import com.mark.annotation.Comment;
import com.mark.annotation.DDL;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.persistence.Column;
import java.util.Set;

@SupportedAnnotationTypes("com.mark.annotation")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class DDLAnnotationProcessor extends AbstractProcessor {

    private enum Type {
        Integer("int", " int "),
        Long("long", "bigint"),
        BigDecimal("java.Math.BigDecimal", "decimal(16,2)"),
        String("java.lang.String", "varchar(255)"),
        Enum("enum", "varchar");

        String javaType;
        String dbType;


        Type(String javaType, String dbType) {
            this.javaType = javaType;
            this.dbType = dbType;
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(DDL.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("EEEEE");
        for (Element element : roundEnv.getElementsAnnotatedWith(DDL.class)) {
            DDL tableValue = element.getAnnotation(DDL.class);
            String tableName;
            if (tableValue.value().isBlank()) {
                tableName = element.getSimpleName().toString();
            } else {
                tableName = tableValue.value();
            }

            TypeElement typeElement = (TypeElement) element;
            StringBuilder sb = new StringBuilder();
            sb.append("create table ")
                    .append(tableName)
                    .append("(");
            for (Element fieldElement : typeElement.getEnclosedElements()) {
                if (fieldElement.getKind().isField()) {
                    // Get the Column annotation on the field
                    Comment commentAnnotation = fieldElement.getAnnotation(Comment.class);
                    Column columnAnnotation = fieldElement.getAnnotation(Column.class);
                    if (columnAnnotation == null) {
                        continue;
                    }

                    if (fieldElement.getKind().isField()) {
                        VariableElement variableElement = (VariableElement) fieldElement;
                        variableElement.asType();
                    }
                    // Add the column definition to the SQL script
//                    sb.append(columnAnnotation.name()).append(" ")
//                            .append(columnAnnotation.type()).append("(").append(columnAnnotation.length()).append(") ")
//                            .append(columnAnnotation.nullable() ? "NULL" : "NOT NULL").append(" ")
//                            .append(columnAnnotation.comment()).append(",\n");
                }
            }
            sb.append(")");

            System.out.println(sb);
        }

        System.out.println("EEEEE");
        return true;
    }
}
