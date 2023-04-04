package com.mark.processor;

import com.mark.ElementUtil;
import com.mark.behavior.table.*;
import com.mark.model.TableModel;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.persistence.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("com.mark.annotation")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class DDLAnnotationProcessor extends AbstractProcessor {

    public static final Map<String, TableModel> MODELS = new HashMap<>();
    public static Queue<ElementItem> ENTITIES_ELEMENTS;
    private CreateTable createTable;
    private class ElementItem {
        private Element element;
        private boolean visit;

        public ElementItem(Element element) {
            this.element = element;
            this.visit = false;
        }

        public void updateVisit() {
            this.visit = true;
        }

        public boolean isVisit() {
            return this.visit;
        }
    }

    public DDLAnnotationProcessor() {
        this.createTable = new ChainCreateTable(
                new InheritanceTable(),
                new DiscriminatorValueTable(),
                new GeneralTable()
        );
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(Entity.class.getCanonicalName());
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        ElementUtil.init(processingEnv.getElementUtils(), processingEnv.getTypeUtils());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            Set<? extends Element> entityElements = roundEnv.getElementsAnnotatedWith(Entity.class);

            ENTITIES_ELEMENTS = new LinkedList<>(entityElements.stream().map(ElementItem::new).collect(Collectors.toList()));
            while (!ENTITIES_ELEMENTS.isEmpty()) {
                ElementItem item = ENTITIES_ELEMENTS.poll();
                TableModel model = createTable.create(item.element);
                if (model == null) {
                    if (item.isVisit()) {
                        System.out.println("ERROR. HAVE_NOT_CREATE_TABLE_ELEMENT");
                        return true;
                    }
                    item.updateVisit();
                    ENTITIES_ELEMENTS.offer(item);
                    continue;
                }

                MODELS.put(model.getTableName(), model);
            }

            File file = new File("./project-ddl-query.sql");
            if(file.exists()){
                LocalDateTime dateTime = LocalDateTime.from(new Date(file.lastModified()).toInstant());
                if(dateTime.isBefore(LocalDateTime.now().minusDays(1))) {
                    file.delete();
                    file.createNewFile();
                }
                file.setWritable(true);
            } else {
                file.createNewFile();
            }
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file))) {
                for (TableModel model : MODELS.values()) {
                    StringBuilder builder = new StringBuilder();
                    model.tableToQuery(builder);
                    writer.write(builder.toString());
                    writer.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ANNOTATION_PROCESSOR_CREATE_SQL_ERROR");
        }
        return true;
    }
}
