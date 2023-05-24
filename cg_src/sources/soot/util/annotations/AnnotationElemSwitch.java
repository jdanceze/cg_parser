package soot.util.annotations;

import java.util.Iterator;
import soot.tagkit.AbstractAnnotationElemTypeSwitch;
import soot.tagkit.AnnotationAnnotationElem;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationBooleanElem;
import soot.tagkit.AnnotationClassElem;
import soot.tagkit.AnnotationDoubleElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationEnumElem;
import soot.tagkit.AnnotationFloatElem;
import soot.tagkit.AnnotationIntElem;
import soot.tagkit.AnnotationLongElem;
import soot.tagkit.AnnotationStringElem;
/* loaded from: gencallgraphv3.jar:soot/util/annotations/AnnotationElemSwitch.class */
public class AnnotationElemSwitch extends AbstractAnnotationElemTypeSwitch<AnnotationElemResult<?>> {

    /* loaded from: gencallgraphv3.jar:soot/util/annotations/AnnotationElemSwitch$AnnotationElemResult.class */
    public static class AnnotationElemResult<V> {
        private final String name;
        private final V value;

        public AnnotationElemResult(String name, V value) {
            this.name = name;
            this.value = value;
        }

        public String getKey() {
            return this.name;
        }

        public V getValue() {
            return this.value;
        }
    }

    @Override // soot.tagkit.AbstractAnnotationElemTypeSwitch, soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationAnnotationElem(AnnotationAnnotationElem v) {
        AnnotationInstanceCreator aic = new AnnotationInstanceCreator();
        Object result = aic.create(v.getValue());
        setResult(new AnnotationElemResult(v.getName(), result));
    }

    @Override // soot.tagkit.AbstractAnnotationElemTypeSwitch, soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationArrayElem(AnnotationArrayElem v) {
        Object[] result = new Object[v.getNumValues()];
        int i = 0;
        Iterator<AnnotationElem> it = v.getValues().iterator();
        while (it.hasNext()) {
            AnnotationElem elem = it.next();
            AnnotationElemSwitch sw = new AnnotationElemSwitch();
            elem.apply(sw);
            result[i] = sw.getResult().getValue();
            i++;
        }
        setResult(new AnnotationElemResult(v.getName(), result));
    }

    @Override // soot.tagkit.AbstractAnnotationElemTypeSwitch, soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationBooleanElem(AnnotationBooleanElem v) {
        setResult(new AnnotationElemResult(v.getName(), Boolean.valueOf(v.getValue())));
    }

    @Override // soot.tagkit.AbstractAnnotationElemTypeSwitch, soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationClassElem(AnnotationClassElem v) {
        try {
            Class<?> clazz = ClassLoaderUtils.loadClass(v.getDesc().replace('/', '.'));
            setResult(new AnnotationElemResult(v.getName(), clazz));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load class: " + v.getDesc());
        }
    }

    @Override // soot.tagkit.AbstractAnnotationElemTypeSwitch, soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationDoubleElem(AnnotationDoubleElem v) {
        setResult(new AnnotationElemResult(v.getName(), Double.valueOf(v.getValue())));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.tagkit.AbstractAnnotationElemTypeSwitch, soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationEnumElem(AnnotationEnumElem v) {
        try {
            Class<?> clazz = ClassLoaderUtils.loadClass(v.getTypeName().replace('/', '.'));
            Enum<?> result = null;
            Object[] enumConstants = clazz.getEnumConstants();
            int length = enumConstants.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Object o = enumConstants[i];
                try {
                    Enum<?> t = (Enum) o;
                    if (!t.name().equals(v.getConstantName())) {
                        i++;
                    } else {
                        result = t;
                        break;
                    }
                } catch (ClassCastException e) {
                    throw new RuntimeException("Class " + v.getTypeName() + " is no Enum");
                }
            }
            if (result == null) {
                throw new RuntimeException(String.valueOf(v.getConstantName()) + " is not a EnumConstant of " + v.getTypeName());
            }
            setResult(new AnnotationElemResult(v.getName(), result));
        } catch (ClassNotFoundException e2) {
            throw new RuntimeException("Could not load class: " + v.getTypeName());
        }
    }

    @Override // soot.tagkit.AbstractAnnotationElemTypeSwitch, soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationFloatElem(AnnotationFloatElem v) {
        setResult(new AnnotationElemResult(v.getName(), Float.valueOf(v.getValue())));
    }

    @Override // soot.tagkit.AbstractAnnotationElemTypeSwitch, soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationIntElem(AnnotationIntElem v) {
        setResult(new AnnotationElemResult(v.getName(), Integer.valueOf(v.getValue())));
    }

    @Override // soot.tagkit.AbstractAnnotationElemTypeSwitch, soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationLongElem(AnnotationLongElem v) {
        setResult(new AnnotationElemResult(v.getName(), Long.valueOf(v.getValue())));
    }

    @Override // soot.tagkit.AbstractAnnotationElemTypeSwitch, soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationStringElem(AnnotationStringElem v) {
        setResult(new AnnotationElemResult(v.getName(), v.getValue()));
    }

    @Override // soot.tagkit.AbstractAnnotationElemTypeSwitch, soot.tagkit.IAnnotationElemTypeSwitch
    public void defaultCase(Object object) {
        throw new RuntimeException("Unexpected AnnotationElem");
    }
}
