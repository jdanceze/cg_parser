package net.bytebuddy.description.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.FilterableList;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/annotation/AnnotationList.class */
public interface AnnotationList extends FilterableList<AnnotationDescription, AnnotationList> {
    boolean isAnnotationPresent(Class<? extends Annotation> cls);

    boolean isAnnotationPresent(TypeDescription typeDescription);

    <T extends Annotation> AnnotationDescription.Loadable<T> ofType(Class<T> cls);

    AnnotationDescription ofType(TypeDescription typeDescription);

    AnnotationList inherited(Set<? extends TypeDescription> set);

    AnnotationList visibility(ElementMatcher<? super RetentionPolicy> elementMatcher);

    TypeList asTypeList();

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/annotation/AnnotationList$AbstractBase.class */
    public static abstract class AbstractBase extends FilterableList.AbstractBase<AnnotationDescription, AnnotationList> implements AnnotationList {
        @Override // net.bytebuddy.description.annotation.AnnotationList
        public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
            Iterator it = iterator();
            while (it.hasNext()) {
                AnnotationDescription annotation = (AnnotationDescription) it.next();
                if (annotation.getAnnotationType().represents(annotationType)) {
                    return true;
                }
            }
            return false;
        }

        @Override // net.bytebuddy.description.annotation.AnnotationList
        public boolean isAnnotationPresent(TypeDescription annotationType) {
            Iterator it = iterator();
            while (it.hasNext()) {
                AnnotationDescription annotation = (AnnotationDescription) it.next();
                if (annotation.getAnnotationType().equals(annotationType)) {
                    return true;
                }
            }
            return false;
        }

        @Override // net.bytebuddy.description.annotation.AnnotationList
        public <T extends Annotation> AnnotationDescription.Loadable<T> ofType(Class<T> annotationType) {
            Iterator it = iterator();
            while (it.hasNext()) {
                AnnotationDescription annotation = (AnnotationDescription) it.next();
                if (annotation.getAnnotationType().represents(annotationType)) {
                    return annotation.prepare(annotationType);
                }
            }
            return (AnnotationDescription.Loadable<T>) AnnotationDescription.UNDEFINED;
        }

        @Override // net.bytebuddy.description.annotation.AnnotationList
        public AnnotationDescription ofType(TypeDescription annotationType) {
            Iterator it = iterator();
            while (it.hasNext()) {
                AnnotationDescription annotation = (AnnotationDescription) it.next();
                if (annotation.getAnnotationType().equals(annotationType)) {
                    return annotation;
                }
            }
            return AnnotationDescription.UNDEFINED;
        }

        @Override // net.bytebuddy.description.annotation.AnnotationList
        public AnnotationList inherited(Set<? extends TypeDescription> ignoredTypes) {
            List<AnnotationDescription> inherited = new ArrayList<>(size());
            Iterator it = iterator();
            while (it.hasNext()) {
                AnnotationDescription annotation = (AnnotationDescription) it.next();
                if (!ignoredTypes.contains(annotation.getAnnotationType()) && annotation.isInherited()) {
                    inherited.add(annotation);
                }
            }
            return wrap(inherited);
        }

        @Override // net.bytebuddy.description.annotation.AnnotationList
        public AnnotationList visibility(ElementMatcher<? super RetentionPolicy> matcher) {
            List<AnnotationDescription> annotationDescriptions = new ArrayList<>(size());
            Iterator it = iterator();
            while (it.hasNext()) {
                AnnotationDescription annotation = (AnnotationDescription) it.next();
                if (matcher.matches(annotation.getRetention())) {
                    annotationDescriptions.add(annotation);
                }
            }
            return wrap(annotationDescriptions);
        }

        @Override // net.bytebuddy.description.annotation.AnnotationList
        public TypeList asTypeList() {
            List<TypeDescription> annotationTypes = new ArrayList<>(size());
            Iterator it = iterator();
            while (it.hasNext()) {
                AnnotationDescription annotation = (AnnotationDescription) it.next();
                annotationTypes.add(annotation.getAnnotationType());
            }
            return new TypeList.Explicit(annotationTypes);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.matcher.FilterableList.AbstractBase
        public AnnotationList wrap(List<AnnotationDescription> values) {
            return new Explicit(values);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/annotation/AnnotationList$ForLoadedAnnotations.class */
    public static class ForLoadedAnnotations extends AbstractBase {
        private final List<? extends Annotation> annotations;

        public ForLoadedAnnotations(Annotation... annotation) {
            this(Arrays.asList(annotation));
        }

        public ForLoadedAnnotations(List<? extends Annotation> annotations) {
            this.annotations = annotations;
        }

        public static List<AnnotationList> asList(Annotation[][] annotations) {
            List<AnnotationList> result = new ArrayList<>(annotations.length);
            for (Annotation[] annotation : annotations) {
                result.add(new ForLoadedAnnotations(annotation));
            }
            return result;
        }

        @Override // java.util.AbstractList, java.util.List
        public AnnotationDescription get(int index) {
            return AnnotationDescription.ForLoadedAnnotation.of(this.annotations.get(index));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.annotations.size();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/annotation/AnnotationList$Explicit.class */
    public static class Explicit extends AbstractBase {
        private final List<? extends AnnotationDescription> annotationDescriptions;

        public Explicit(AnnotationDescription... annotationDescription) {
            this(Arrays.asList(annotationDescription));
        }

        public Explicit(List<? extends AnnotationDescription> annotationDescriptions) {
            this.annotationDescriptions = annotationDescriptions;
        }

        public static List<AnnotationList> asList(List<? extends List<? extends AnnotationDescription>> annotations) {
            List<AnnotationList> result = new ArrayList<>(annotations.size());
            for (List<? extends AnnotationDescription> annotation : annotations) {
                result.add(new Explicit(annotation));
            }
            return result;
        }

        @Override // java.util.AbstractList, java.util.List
        public AnnotationDescription get(int index) {
            return this.annotationDescriptions.get(index);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.annotationDescriptions.size();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/annotation/AnnotationList$Empty.class */
    public static class Empty extends FilterableList.Empty<AnnotationDescription, AnnotationList> implements AnnotationList {
        public static List<AnnotationList> asList(int length) {
            List<AnnotationList> result = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                result.add(new Empty());
            }
            return result;
        }

        @Override // net.bytebuddy.description.annotation.AnnotationList
        public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
            return false;
        }

        @Override // net.bytebuddy.description.annotation.AnnotationList
        public boolean isAnnotationPresent(TypeDescription annotationType) {
            return false;
        }

        @Override // net.bytebuddy.description.annotation.AnnotationList
        public <T extends Annotation> AnnotationDescription.Loadable<T> ofType(Class<T> annotationType) {
            return (AnnotationDescription.Loadable<T>) AnnotationDescription.UNDEFINED;
        }

        @Override // net.bytebuddy.description.annotation.AnnotationList
        public AnnotationDescription ofType(TypeDescription annotationType) {
            return AnnotationDescription.UNDEFINED;
        }

        @Override // net.bytebuddy.description.annotation.AnnotationList
        public AnnotationList inherited(Set<? extends TypeDescription> ignoredTypes) {
            return this;
        }

        @Override // net.bytebuddy.description.annotation.AnnotationList
        public AnnotationList visibility(ElementMatcher<? super RetentionPolicy> matcher) {
            return this;
        }

        @Override // net.bytebuddy.description.annotation.AnnotationList
        public TypeList asTypeList() {
            return new TypeList.Empty();
        }
    }
}
