package org.hamcrest.generator.qdox.model;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/DefaultDocletTagFactory.class */
public class DefaultDocletTagFactory implements DocletTagFactory {
    @Override // org.hamcrest.generator.qdox.model.DocletTagFactory
    public DocletTag createDocletTag(String tag, String text, AbstractBaseJavaEntity context, int lineNumber) {
        return new DefaultDocletTag(tag, text, context, lineNumber);
    }

    @Override // org.hamcrest.generator.qdox.model.DocletTagFactory
    public DocletTag createDocletTag(String tag, String text) {
        return createDocletTag(tag, text, null, 0);
    }
}
