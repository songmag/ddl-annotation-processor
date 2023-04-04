package com.mark;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.*;

public class ElementUtil {

    private static Object locks = new Object();
    private static ElementUtil INSTANCE;
    private Elements elements;
    private Types types;

    public ElementUtil(Elements elements, Types types) {
        this.elements = elements;
        this.types = types;
    }

    public static ElementUtil getInstance() {
        synchronized (locks) {
            if (INSTANCE == null) {
                throw new RuntimeException("init first annotation processor");
            }
            return INSTANCE;
        }
    }

    public static void init(Elements elements, Types types) {
        synchronized (locks) {
            INSTANCE = new ElementUtil(elements, types);
        }
    }

    public Element getSuperClassElement(Element element) {
        return this.types.directSupertypes(element.asType())
                .stream()
                .filter(i -> ((DeclaredType) i).asElement().getKind() == ElementKind.CLASS)
                .map(i -> ((DeclaredType) i).asElement())
                .findFirst()
                .get();
    }


    public List<Element> getAllElements(Element element) {
        Map<String, Element> elements = new HashMap<String, Element>();
        Element superElement = getSuperClassElement(element);
        List<? extends Element> superElements = this.elements.getAllMembers((TypeElement) superElement);
        List<? extends Element> originElements = this.elements.getAllMembers((TypeElement) element);

        for (Element se : superElements) {
            elements.put(se.getSimpleName().toString(), se);
        }
        for (Element oe : originElements) {
            elements.put(oe.getSimpleName().toString(), oe);
        }
        return new LinkedList<>(elements.values());
    }
}
