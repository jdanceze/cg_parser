package org.hamcrest.generator.qdox.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/AbstractJavaEntity.class */
public abstract class AbstractJavaEntity extends AbstractBaseJavaEntity implements Comparable {
    private String comment;
    private JavaClass parentClass;
    protected List modifiers = new ArrayList();
    private DocletTag[] tags = new DocletTag[0];

    protected abstract void writeBody(IndentBuffer indentBuffer);

    public String[] getModifiers() {
        return (String[]) this.modifiers.toArray(new String[this.modifiers.size()]);
    }

    public String getComment() {
        return this.comment;
    }

    public DocletTag[] getTags() {
        return this.tags;
    }

    public DocletTag[] getTagsByName(String name) {
        List specifiedTags = new ArrayList();
        for (int i = 0; i < this.tags.length; i++) {
            DocletTag docletTag = this.tags[i];
            if (docletTag.getName().equals(name)) {
                specifiedTags.add(docletTag);
            }
        }
        return (DocletTag[]) specifiedTags.toArray(new DocletTag[specifiedTags.size()]);
    }

    public DocletTag getTagByName(String name) {
        for (int i = 0; i < this.tags.length; i++) {
            DocletTag docletTag = this.tags[i];
            if (docletTag.getName().equals(name)) {
                return docletTag;
            }
        }
        return null;
    }

    public String getNamedParameter(String tagName, String parameterName) {
        DocletTag tag = getTagByName(tagName);
        if (tag != null) {
            return tag.getNamedParameter(parameterName);
        }
        return null;
    }

    void commentHeader(IndentBuffer buffer) {
        if (this.comment == null && (this.tags == null || this.tags.length == 0)) {
            return;
        }
        buffer.write("/**");
        buffer.newline();
        if (this.comment != null && this.comment.length() > 0) {
            buffer.write(" * ");
            buffer.write(this.comment.replaceAll("\n", "\n * "));
            buffer.newline();
        }
        if (this.tags != null && this.tags.length > 0) {
            if (this.comment != null && this.comment.length() > 0) {
                buffer.write(" *");
                buffer.newline();
            }
            for (int i = 0; i < this.tags.length; i++) {
                DocletTag docletTag = this.tags[i];
                buffer.write(" * @");
                buffer.write(docletTag.getName());
                if (docletTag.getValue().length() > 0) {
                    buffer.write(' ');
                    buffer.write(docletTag.getValue());
                }
                buffer.newline();
            }
        }
        buffer.write(" */");
        buffer.newline();
    }

    public String getCodeBlock() {
        IndentBuffer result = new IndentBuffer();
        write(result);
        return result.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void write(IndentBuffer result) {
        commentHeader(result);
        writeBody(result);
    }

    public void setModifiers(String[] modifiers) {
        this.modifiers = Arrays.asList(modifiers);
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTags(List tagList) {
        this.tags = new DocletTag[tagList.size()];
        tagList.toArray(this.tags);
    }

    public boolean isAbstract() {
        return isModifierPresent(Jimple.ABSTRACT);
    }

    public boolean isPublic() {
        return isModifierPresent(Jimple.PUBLIC);
    }

    public boolean isPrivate() {
        return isModifierPresent(Jimple.PRIVATE);
    }

    public boolean isProtected() {
        return isModifierPresent(Jimple.PROTECTED);
    }

    public boolean isStatic() {
        return isModifierPresent(Jimple.STATIC);
    }

    public boolean isFinal() {
        return isModifierPresent(Jimple.FINAL);
    }

    public boolean isSynchronized() {
        return isModifierPresent(Jimple.SYNCHRONIZED);
    }

    public boolean isTransient() {
        return isModifierPresent(Jimple.TRANSIENT);
    }

    public boolean isVolatile() {
        return isModifierPresent(Jimple.VOLATILE);
    }

    public boolean isNative() {
        return isModifierPresent(Jimple.NATIVE);
    }

    public boolean isStrictfp() {
        return isModifierPresent(Jimple.STRICTFP);
    }

    private boolean isModifierPresent(String modifier) {
        return this.modifiers.contains(modifier);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeNonAccessibilityModifiers(IndentBuffer result) {
        for (String modifier : this.modifiers) {
            if (!modifier.startsWith("p")) {
                result.write(modifier);
                result.write(' ');
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeAccessibilityModifier(IndentBuffer result) {
        for (String modifier : this.modifiers) {
            if (modifier.startsWith("p")) {
                result.write(modifier);
                result.write(' ');
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeAllModifiers(IndentBuffer result) {
        for (String modifier : this.modifiers) {
            result.write(modifier);
            result.write(' ');
        }
    }

    public JavaSource getSource() {
        return this.parentClass.getParentSource();
    }

    public void setParentClass(JavaClass parentClass) {
        this.parentClass = parentClass;
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public JavaClass getParentClass() {
        return this.parentClass;
    }
}
