package com.mark.processor;

import com.mark.annotation.Comment;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("com.mark.annotation")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class DDLAnnotationProcessor extends AbstractProcessor {

    private enum Type {
        Int("int", "int"),
        Integer("java.lang.Integer", "int"),
        LongWrapper("java.lang.Long", "bigint"),
        Long("long", "bigint"),
        BigDecimal("java.math.BigDecimal", "decimal(16,2)"),
        String("java.lang.String", "varchar(255)");

        String javaType;
        String dbType;

        public static String getDBType(Element element) {
            for (Type t : values()) {
                if (element.asType().toString().equals(t.javaType)) {
                    return t.dbType;
                }
            }

            if (isEnum(element)) {
                return "varchar(255)";
            }

            return null;
        }

        private static boolean isEnum(Element element) {
            return ((DeclaredType) (element.asType())).asElement().getKind() == ElementKind.ENUM;
        }

        Type(String javaType, String dbType) {
            this.javaType = javaType;
            this.dbType = dbType;
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(Entity.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        StringBuilder sb = new StringBuilder();
        for (Element element : roundEnv.getElementsAnnotatedWith(Entity.class)) {
            DiscriminatorValue discriminatorValue = element.getAnnotation(DiscriminatorValue.class);
            if (discriminatorValue != null) {
                continue;
            }
            Table tableValue = element.getAnnotation(Table.class);
            String tableName;
            if (tableValue == null) {
                tableName = element.getSimpleName().toString();
            } else if (tableValue.name().isBlank()) {
                tableName = element.getSimpleName().toString();
            } else {
                tableName = tableValue.name();
            }
            TypeElement typeElement = (TypeElement) element;
            sb.append("create table ")
                    .append(tableName)
                    .append("(\n\t");

            for (Element fieldElement : typeElement.getEnclosedElements()) {
                generateFields(fieldElement, sb);
            }
            sb.replace(sb.length() - 3, sb.length(), "");
            sb.append("\n);\n");
        }
        if (sb.length() > 0) {
            try {
                File file = new File("./project-ddl-query.sql");
                file.createNewFile();
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
                writer.write(sb.toString());
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    private void generateFields(Element element, StringBuilder sb) {
        if (element.getKind().isField()) {
            // Get the Column annotation on the field
            String columnName = element.getSimpleName().toString();
            Transient fieldTransient = element.getAnnotation(Transient.class);

            if (fieldTransient != null) {
                return;
            }

            Column columnAnnotation = element.getAnnotation(Column.class);
            boolean nullable = true;
            boolean unique = false;
            if (columnAnnotation != null) {
                if (!columnAnnotation.insertable()) {
                    return;
                }
                if (!columnAnnotation.name().isBlank()) {
                    columnName = columnAnnotation.name();
                }
                nullable = columnAnnotation.nullable();
                unique = columnAnnotation.unique();
                if (!columnAnnotation.columnDefinition().isBlank()) {
                    sb.append(columnName).append(" ");
                    sb.append(columnAnnotation.columnDefinition()).append(",\n\t");
                    return;
                }
            }

            Comment commentAnnotation = element.getAnnotation(Comment.class);
            String type = Type.getDBType(element);
            if (type == null) {
                Queue<Element> fields = new LinkedList<>();

                Embedded embeddedAnnotation = element.getAnnotation(Embedded.class);

                if (embeddedAnnotation != null) {
                    Element subElement = ((DeclaredType) element.asType()).asElement();
                    fields.addAll(subElement.getEnclosedElements()
                            .stream()
                            .filter(i -> i.getKind().isField())
                            .collect(Collectors.toList()));
                }

                ManyToOne manyToOneAnnotation = element.getAnnotation(ManyToOne.class);

                if (manyToOneAnnotation == null && fields.isEmpty()) {
                    return;
                }
                if (manyToOneAnnotation != null) {
                    JoinColumn joinColumnAnnotation = element.getAnnotation(JoinColumn.class);
                    if (joinColumnAnnotation == null) {
                        Element subElement = ((DeclaredType) element.asType()).asElement();
                        fields.addAll(subElement.getEnclosedElements()
                                .stream()
                                .filter(i -> {
                                    Id idAnnotation = i.getAnnotation(Id.class);
                                    return idAnnotation != null;
                                })
                                .collect(Collectors.toList())
                        );
                    } else {
                        //id annotation override
                    }
                }

                while (!fields.isEmpty()) {
                    generateFields(fields.poll(), sb);
                }
                return;
            }

            sb.append(columnName)
                    .append(" ")
                    .append(type)
                    .append(" ")
                    .append(nullable ? "NULL" : "NOT NULL")
                    .append(" ")
                    .append(unique ? "unique" : "");

            if (commentAnnotation != null && !commentAnnotation.value().isBlank()) {
                sb
                        .append(" comment '")
                        .append(commentAnnotation.value())
                        .append("'");
            }
            sb.append(",\n\t");
        }
    }
}
