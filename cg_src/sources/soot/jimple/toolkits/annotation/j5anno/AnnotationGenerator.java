package soot.jimple.toolkits.annotation.j5anno;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import soot.G;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Host;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/j5anno/AnnotationGenerator.class */
public class AnnotationGenerator {
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$java$lang$annotation$RetentionPolicy;

    static /* synthetic */ int[] $SWITCH_TABLE$java$lang$annotation$RetentionPolicy() {
        int[] iArr = $SWITCH_TABLE$java$lang$annotation$RetentionPolicy;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[RetentionPolicy.values().length];
        try {
            iArr2[RetentionPolicy.CLASS.ordinal()] = 2;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[RetentionPolicy.RUNTIME.ordinal()] = 3;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[RetentionPolicy.SOURCE.ordinal()] = 1;
        } catch (NoSuchFieldError unused3) {
        }
        $SWITCH_TABLE$java$lang$annotation$RetentionPolicy = iArr2;
        return iArr2;
    }

    public AnnotationGenerator(Singletons.Global g) {
    }

    public static AnnotationGenerator v() {
        return G.v().soot_jimple_toolkits_annotation_j5anno_AnnotationGenerator();
    }

    public void annotate(Host h, Class<? extends Annotation> klass, AnnotationElem... elems) {
        annotate(h, klass, Arrays.asList(elems));
    }

    public void annotate(Host h, Class<? extends Annotation> klass, List<AnnotationElem> elems) {
        Target t = (Target) klass.getAnnotation(Target.class);
        Collection<ElementType> elementTypes = Arrays.asList(t.value());
        String ERR = "Annotation class " + klass + " not applicable to host of type " + h.getClass() + ".";
        if (h instanceof SootClass) {
            if (!elementTypes.contains(ElementType.TYPE)) {
                throw new RuntimeException(ERR);
            }
        } else if (h instanceof SootMethod) {
            if (!elementTypes.contains(ElementType.METHOD)) {
                throw new RuntimeException(ERR);
            }
        } else if (h instanceof SootField) {
            if (!elementTypes.contains(ElementType.FIELD)) {
                throw new RuntimeException(ERR);
            }
        } else {
            throw new RuntimeException("Tried to attach annotation to host of type " + h.getClass() + ".");
        }
        Retention r = (Retention) klass.getAnnotation(Retention.class);
        int retPolicy = 1;
        if (r != null) {
            switch ($SWITCH_TABLE$java$lang$annotation$RetentionPolicy()[r.value().ordinal()]) {
                case 2:
                    retPolicy = 1;
                    break;
                case 3:
                    retPolicy = 0;
                    break;
                default:
                    throw new RuntimeException("Unexpected retention policy: 1");
            }
        }
        annotate(h, klass.getCanonicalName(), retPolicy, elems);
    }

    public void annotate(Host h, String annotationName, int visibility, List<AnnotationElem> elems) {
        String annotationName2 = annotationName.replace('.', '/');
        if (!annotationName2.endsWith(";")) {
            annotationName2 = "L" + annotationName2 + ';';
        }
        VisibilityAnnotationTag tagToAdd = findOrAdd(h, visibility);
        AnnotationTag at = new AnnotationTag(annotationName2, elems);
        tagToAdd.addAnnotation(at);
    }

    private VisibilityAnnotationTag findOrAdd(Host h, int visibility) {
        ArrayList<VisibilityAnnotationTag> va_tags = new ArrayList<>();
        for (Tag t : h.getTags()) {
            if (t instanceof VisibilityAnnotationTag) {
                VisibilityAnnotationTag vat = (VisibilityAnnotationTag) t;
                if (vat.getVisibility() == visibility) {
                    va_tags.add(vat);
                }
            }
        }
        if (va_tags.isEmpty()) {
            VisibilityAnnotationTag vat2 = new VisibilityAnnotationTag(visibility);
            h.addTag(vat2);
            return vat2;
        }
        return va_tags.get(0);
    }
}
