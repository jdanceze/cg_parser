package org.hamcrest.generator;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.hamcrest.generator.FactoryMethod;
import org.hamcrest.generator.qdox.model.DocletTag;
import org.hamcrest.generator.qdox.model.JavaClass;
import org.hamcrest.generator.qdox.model.JavaMethod;
import org.hamcrest.generator.qdox.model.JavaParameter;
import org.hamcrest.generator.qdox.model.Type;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/QDoxFactoryReader.class */
public class QDoxFactoryReader implements Iterable<FactoryMethod> {
    private final Iterable<FactoryMethod> wrapped;
    private final JavaClass classSource;
    private static final Pattern GENERIC_REGEX = Pattern.compile("<.*>");
    private static final Pattern VARARGS_REGEX = Pattern.compile("...", 16);

    public QDoxFactoryReader(Iterable<FactoryMethod> wrapped, QDox qdox, String className) {
        this.wrapped = wrapped;
        this.classSource = qdox.getClassByName(className);
    }

    @Override // java.lang.Iterable
    public Iterator<FactoryMethod> iterator() {
        final Iterator<FactoryMethod> iterator = this.wrapped.iterator();
        return new Iterator<FactoryMethod>() { // from class: org.hamcrest.generator.QDoxFactoryReader.1
            @Override // java.util.Iterator
            public boolean hasNext() {
                return iterator.hasNext();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public FactoryMethod next() {
                return QDoxFactoryReader.this.enhance((FactoryMethod) iterator.next());
            }

            @Override // java.util.Iterator
            public void remove() {
                iterator.remove();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public FactoryMethod enhance(FactoryMethod factoryMethod) {
        JavaMethod methodSource = findMethodInSource(factoryMethod);
        if (methodSource != null) {
            factoryMethod.setJavaDoc(createJavaDocComment(methodSource));
            JavaParameter[] parametersFromSource = methodSource.getParameters();
            List<FactoryMethod.Parameter> parametersFromReflection = factoryMethod.getParameters();
            if (parametersFromReflection.size() == parametersFromSource.length) {
                for (int i = 0; i < parametersFromSource.length; i++) {
                    parametersFromReflection.get(i).setName(parametersFromSource[i].getName());
                }
            }
        }
        return factoryMethod;
    }

    private JavaMethod findMethodInSource(FactoryMethod factoryMethod) {
        List<FactoryMethod.Parameter> params = factoryMethod.getParameters();
        Type[] types = new Type[params.size()];
        boolean varArgs = false;
        for (int i = 0; i < types.length; i++) {
            String type = params.get(i).getType();
            varArgs = VARARGS_REGEX.matcher(type).find();
            types[i] = new Type(VARARGS_REGEX.matcher(GENERIC_REGEX.matcher(type).replaceAll("")).replaceAll(""));
        }
        JavaMethod[] methods = this.classSource.getMethodsBySignature(factoryMethod.getName(), types, false, varArgs);
        if (methods.length == 1) {
            return methods[0];
        }
        return null;
    }

    private static String createJavaDocComment(JavaMethod methodSource) {
        String comment = methodSource.getComment();
        DocletTag[] tags = methodSource.getTags();
        if ((comment == null || comment.trim().length() == 0) && tags.length == 0) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        result.append(comment);
        result.append("\n\n");
        for (DocletTag tag : tags) {
            result.append('@').append(tag.getName()).append(' ').append(tag.getValue()).append('\n');
        }
        return result.toString();
    }
}
