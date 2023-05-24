package org.hamcrest.generator.qdox.model.annotation;

import java.util.StringTokenizer;
import org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity;
import org.hamcrest.generator.qdox.model.JavaClass;
import org.hamcrest.generator.qdox.model.JavaClassParent;
import org.hamcrest.generator.qdox.model.JavaField;
import org.hamcrest.generator.qdox.model.Type;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/annotation/AnnotationFieldRef.class */
public class AnnotationFieldRef implements AnnotationValue {
    private final int[] parts;
    private final String name;
    private AbstractBaseJavaEntity context;
    private JavaField field;
    String[] myArray = {"unchecked"};
    private int fieldIndex = -1;

    public AnnotationFieldRef(String name) {
        this.name = name;
        int length = new StringTokenizer(name, ".").countTokens();
        this.parts = new int[length + 1];
        this.parts[0] = -1;
        for (int i = 1; i < length; i++) {
            this.parts[i] = name.indexOf(46, this.parts[i - 1] + 1);
        }
        this.parts[length] = name.length();
    }

    public String getName() {
        return this.name;
    }

    public String getNamePrefix(int end) {
        return this.name.substring(0, this.parts[end + 1]);
    }

    public String getNamePart(int index) {
        return this.name.substring(this.parts[index] + 1, this.parts[index + 1]);
    }

    public int getPartCount() {
        return this.parts.length - 1;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object accept(AnnotationVisitor visitor) {
        return visitor.visitAnnotationFieldRef(this);
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object getParameterValue() {
        return getName();
    }

    public String toString() {
        return getName();
    }

    public AbstractBaseJavaEntity getContext() {
        return this.context;
    }

    public void setContext(AbstractBaseJavaEntity context) {
        this.context = context;
    }

    public String getClassPart() {
        String result = null;
        if (getField() != null) {
            result = this.name.substring(0, this.parts[this.fieldIndex]);
        }
        return result;
    }

    public String getFieldPart() {
        String result = null;
        if (getField() != null) {
            result = this.name.substring(this.parts[this.fieldIndex] + 1);
        }
        return result;
    }

    protected JavaField resolveField(JavaClass javaClass, int start, int end) {
        JavaField field = null;
        for (int i = start; i < end; i++) {
            field = javaClass.getFieldByName(getNamePart(i));
            if (field == null) {
                break;
            }
            javaClass = field.getType().getJavaClass();
        }
        return field;
    }

    public JavaField getField() {
        if (this.fieldIndex < 0) {
            if (this.context.getParentClass() != null) {
                this.field = resolveField(this.context.getParentClass(), 0, this.parts.length - 1);
                this.fieldIndex = 0;
            }
            if (this.field == null) {
                JavaClassParent classParent = this.context.getParentClass();
                if (classParent == null) {
                    classParent = (JavaClass) this.context;
                }
                int i = 0;
                while (true) {
                    if (i >= this.parts.length - 1) {
                        break;
                    }
                    String className = getNamePrefix(i);
                    String typeName = classParent.resolveType(className);
                    if (typeName != null) {
                        Type type = Type.createUnresolved(typeName, 0, classParent);
                        JavaClass javaClass = type.getJavaClass();
                        if (javaClass != null) {
                            this.fieldIndex = i + 1;
                            this.field = resolveField(javaClass, i + 1, this.parts.length - 1);
                            break;
                        }
                    }
                    i++;
                }
            }
        }
        return this.field;
    }
}
