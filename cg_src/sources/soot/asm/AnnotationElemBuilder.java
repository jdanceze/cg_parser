package soot.asm;

import java.util.ArrayList;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import soot.tagkit.AnnotationAnnotationElem;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationClassElem;
import soot.tagkit.AnnotationDoubleElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationEnumElem;
import soot.tagkit.AnnotationFloatElem;
import soot.tagkit.AnnotationIntElem;
import soot.tagkit.AnnotationLongElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationTag;
/* loaded from: gencallgraphv3.jar:soot/asm/AnnotationElemBuilder.class */
abstract class AnnotationElemBuilder extends AnnotationVisitor {
    protected final ArrayList<AnnotationElem> elems;

    @Override // org.objectweb.asm.AnnotationVisitor
    public abstract void visitEnd();

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnnotationElemBuilder(int expected) {
        super(327680);
        this.elems = new ArrayList<>(expected);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnnotationElemBuilder() {
        this(4);
    }

    public AnnotationElem getAnnotationElement(String name, Object value) {
        Object[] objArr;
        Object[] objArr2;
        AnnotationElem elem;
        if (value instanceof Byte) {
            elem = new AnnotationIntElem(((Byte) value).byteValue(), 'B', name);
        } else if (value instanceof Boolean) {
            elem = new AnnotationIntElem(((Boolean) value).booleanValue() ? 1 : 0, 'Z', name);
        } else if (value instanceof Character) {
            elem = new AnnotationIntElem(((Character) value).charValue(), 'C', name);
        } else if (value instanceof Short) {
            elem = new AnnotationIntElem(((Short) value).shortValue(), 'S', name);
        } else if (value instanceof Integer) {
            elem = new AnnotationIntElem(((Integer) value).intValue(), 'I', name);
        } else if (value instanceof Long) {
            elem = new AnnotationLongElem(((Long) value).longValue(), 'J', name);
        } else if (value instanceof Float) {
            elem = new AnnotationFloatElem(((Float) value).floatValue(), 'F', name);
        } else if (value instanceof Double) {
            elem = new AnnotationDoubleElem(((Double) value).doubleValue(), 'D', name);
        } else if (value instanceof String) {
            elem = new AnnotationStringElem(value.toString(), 's', name);
        } else if (value instanceof Type) {
            Type t = (Type) value;
            elem = new AnnotationClassElem(t.getDescriptor(), 'c', name);
        } else if (value.getClass().isArray()) {
            ArrayList<AnnotationElem> annotationArray = new ArrayList<>();
            if (value instanceof byte[]) {
                for (byte b : (byte[]) value) {
                    Object element = Byte.valueOf(b);
                    annotationArray.add(getAnnotationElement(name, element));
                }
            } else if (value instanceof boolean[]) {
                for (boolean z : (boolean[]) value) {
                    Object element2 = Boolean.valueOf(z);
                    annotationArray.add(getAnnotationElement(name, element2));
                }
            } else if (value instanceof char[]) {
                for (char c : (char[]) value) {
                    Object element3 = Character.valueOf(c);
                    annotationArray.add(getAnnotationElement(name, element3));
                }
            } else if (value instanceof short[]) {
                for (short s : (short[]) value) {
                    Object element4 = Short.valueOf(s);
                    annotationArray.add(getAnnotationElement(name, element4));
                }
            } else if (value instanceof int[]) {
                for (int i : (int[]) value) {
                    Object element5 = Integer.valueOf(i);
                    annotationArray.add(getAnnotationElement(name, element5));
                }
            } else if (value instanceof long[]) {
                for (long j : (long[]) value) {
                    Object element6 = Long.valueOf(j);
                    annotationArray.add(getAnnotationElement(name, element6));
                }
            } else if (value instanceof float[]) {
                for (float f : (float[]) value) {
                    Object element7 = Float.valueOf(f);
                    annotationArray.add(getAnnotationElement(name, element7));
                }
            } else if (value instanceof double[]) {
                for (double d : (double[]) value) {
                    Object element8 = Double.valueOf(d);
                    annotationArray.add(getAnnotationElement(name, element8));
                }
            } else if (value instanceof String[]) {
                for (Object element9 : (String[]) value) {
                    annotationArray.add(getAnnotationElement(name, element9));
                }
            } else if (value instanceof Type[]) {
                for (Object element10 : (Type[]) value) {
                    annotationArray.add(getAnnotationElement(name, element10));
                }
            } else {
                throw new UnsupportedOperationException("Unsupported array value type: " + value.getClass());
            }
            elem = new AnnotationArrayElem(annotationArray, '[', name);
        } else {
            throw new UnsupportedOperationException("Unsupported value type: " + value.getClass());
        }
        return elem;
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public void visit(String name, Object value) {
        AnnotationElem elem = getAnnotationElement(name, value);
        this.elems.add(elem);
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public void visitEnum(String name, String desc, String value) {
        this.elems.add(new AnnotationEnumElem(desc, value, 'e', name));
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(final String name) {
        return new AnnotationElemBuilder() { // from class: soot.asm.AnnotationElemBuilder.1
            @Override // soot.asm.AnnotationElemBuilder, org.objectweb.asm.AnnotationVisitor
            public void visitEnd() {
                String ename = name;
                if (ename == null) {
                    ename = "default";
                }
                AnnotationElemBuilder.this.elems.add(new AnnotationArrayElem(this.elems, '[', ename));
            }
        };
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(final String name, final String desc) {
        return new AnnotationElemBuilder() { // from class: soot.asm.AnnotationElemBuilder.2
            @Override // soot.asm.AnnotationElemBuilder, org.objectweb.asm.AnnotationVisitor
            public void visitEnd() {
                AnnotationTag tag = new AnnotationTag(desc, this.elems);
                AnnotationElemBuilder.this.elems.add(new AnnotationAnnotationElem(tag, '@', name));
            }
        };
    }
}
