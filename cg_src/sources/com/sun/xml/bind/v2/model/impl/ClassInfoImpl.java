package com.sun.xml.bind.v2.model.impl;

import android.content.ContentResolver;
import com.sun.istack.FinalArrayList;
import com.sun.xml.bind.annotation.OverrideAnnotationOf;
import com.sun.xml.bind.v2.model.annotation.Locatable;
import com.sun.xml.bind.v2.model.annotation.MethodLocatable;
import com.sun.xml.bind.v2.model.core.ClassInfo;
import com.sun.xml.bind.v2.model.core.Element;
import com.sun.xml.bind.v2.model.core.ID;
import com.sun.xml.bind.v2.model.core.NonElement;
import com.sun.xml.bind.v2.model.core.PropertyInfo;
import com.sun.xml.bind.v2.model.core.PropertyKind;
import com.sun.xml.bind.v2.model.core.ValuePropertyInfo;
import com.sun.xml.bind.v2.model.nav.Navigator;
import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
import com.sun.xml.bind.v2.runtime.Location;
import com.sun.xml.bind.v2.util.EditDistance;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttachmentRef;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlInlineBinaryData;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/ClassInfoImpl.class */
public class ClassInfoImpl<T, C, F, M> extends TypeInfoImpl<T, C, F, M> implements ClassInfo<T, C>, Element<T, C> {
    protected final C clazz;
    private final QName elementName;
    private final QName typeName;
    private FinalArrayList<PropertyInfoImpl<T, C, F, M>> properties;
    private String[] propOrder;
    private ClassInfoImpl<T, C, F, M> baseClass;
    private boolean baseClassComputed;
    private boolean hasSubClasses;
    protected PropertySeed<T, C, F, M> attributeWildcard;
    private M factoryMethod;
    private static final SecondaryAnnotation[] SECONDARY_ANNOTATIONS;
    private static final Annotation[] EMPTY_ANNOTATIONS;
    private static final HashMap<Class, Integer> ANNOTATION_NUMBER_MAP;
    private static final String[] DEFAULT_ORDER;
    static final /* synthetic */ boolean $assertionsDisabled;

    @Override // com.sun.xml.bind.v2.model.impl.TypeInfoImpl, com.sun.xml.bind.v2.model.annotation.Locatable
    public /* bridge */ /* synthetic */ Locatable getUpstream() {
        return super.getUpstream();
    }

    static {
        SecondaryAnnotation[] secondaryAnnotationArr;
        Class[] clsArr;
        $assertionsDisabled = !ClassInfoImpl.class.desiredAssertionStatus();
        SECONDARY_ANNOTATIONS = SecondaryAnnotation.values();
        EMPTY_ANNOTATIONS = new Annotation[0];
        ANNOTATION_NUMBER_MAP = new HashMap<>();
        Class[] annotations = {XmlTransient.class, XmlAnyAttribute.class, XmlAttribute.class, XmlValue.class, XmlElement.class, XmlElements.class, XmlElementRef.class, XmlElementRefs.class, XmlAnyElement.class, XmlMixed.class, OverrideAnnotationOf.class};
        HashMap<Class, Integer> m = ANNOTATION_NUMBER_MAP;
        for (Class c : annotations) {
            m.put(c, Integer.valueOf(m.size()));
        }
        int index = 20;
        for (SecondaryAnnotation sa : SECONDARY_ANNOTATIONS) {
            for (Class member : sa.members) {
                m.put(member, Integer.valueOf(index));
            }
            index++;
        }
        DEFAULT_ORDER = new String[0];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ClassInfoImpl(ModelBuilder<T, C, F, M> builder, Locatable upstream, C clazz) {
        super(builder, upstream);
        this.baseClassComputed = false;
        this.hasSubClasses = false;
        this.factoryMethod = null;
        this.clazz = clazz;
        if (!$assertionsDisabled && clazz == null) {
            throw new AssertionError();
        }
        this.elementName = parseElementName(clazz);
        XmlType t = (XmlType) reader().getClassAnnotation(XmlType.class, clazz, this);
        this.typeName = parseTypeName(clazz, t);
        if (t != null) {
            String[] propOrder = t.propOrder();
            if (propOrder.length == 0) {
                this.propOrder = null;
            } else if (propOrder[0].length() == 0) {
                this.propOrder = DEFAULT_ORDER;
            } else {
                this.propOrder = propOrder;
            }
        } else {
            this.propOrder = DEFAULT_ORDER;
        }
        XmlAccessorOrder xao = (XmlAccessorOrder) reader().getPackageAnnotation(XmlAccessorOrder.class, clazz, this);
        if (xao != null && xao.value() == XmlAccessOrder.UNDEFINED) {
            this.propOrder = null;
        }
        XmlAccessorOrder xao2 = (XmlAccessorOrder) reader().getClassAnnotation(XmlAccessorOrder.class, clazz, this);
        if (xao2 != null && xao2.value() == XmlAccessOrder.UNDEFINED) {
            this.propOrder = null;
        }
        if (nav().isInterface(clazz)) {
            builder.reportError(new IllegalAnnotationException(Messages.CANT_HANDLE_INTERFACE.format(nav().getClassName(clazz)), this));
        }
        if (!hasFactoryConstructor(t) && !nav().hasDefaultConstructor(clazz)) {
            if (nav().isInnerClass(clazz)) {
                builder.reportError(new IllegalAnnotationException(Messages.CANT_HANDLE_INNER_CLASS.format(nav().getClassName(clazz)), this));
            } else if (this.elementName != null) {
                builder.reportError(new IllegalAnnotationException(Messages.NO_DEFAULT_CONSTRUCTOR.format(nav().getClassName(clazz)), this));
            }
        }
    }

    @Override // com.sun.xml.bind.v2.model.core.ClassInfo
    public ClassInfoImpl<T, C, F, M> getBaseClass() {
        if (!this.baseClassComputed) {
            C s = nav().getSuperClass(this.clazz);
            if (s == null || s == nav().asDecl(Object.class)) {
                this.baseClass = null;
            } else {
                NonElement classInfo = this.builder.getClassInfo(s, true, this);
                if (classInfo instanceof ClassInfoImpl) {
                    this.baseClass = (ClassInfoImpl) classInfo;
                    this.baseClass.hasSubClasses = true;
                } else {
                    this.baseClass = null;
                }
            }
            this.baseClassComputed = true;
        }
        return this.baseClass;
    }

    @Override // com.sun.xml.bind.v2.model.core.Element
    public final Element<T, C> getSubstitutionHead() {
        ClassInfoImpl<T, C, F, M> c;
        ClassInfoImpl<T, C, F, M> baseClass = getBaseClass();
        while (true) {
            c = baseClass;
            if (c == null || c.isElement()) {
                break;
            }
            baseClass = c.getBaseClass();
        }
        return c;
    }

    @Override // com.sun.xml.bind.v2.model.core.ClassInfo
    public final C getClazz() {
        return this.clazz;
    }

    @Override // com.sun.xml.bind.v2.model.core.Element
    public ClassInfoImpl<T, C, F, M> getScope() {
        return null;
    }

    @Override // com.sun.xml.bind.v2.model.core.TypeInfo
    public final T getType() {
        return nav().use(this.clazz);
    }

    @Override // com.sun.xml.bind.v2.model.core.TypeInfo
    public boolean canBeReferencedByIDREF() {
        for (PropertyInfo<T, C> p : getProperties()) {
            if (p.id() == ID.ID) {
                return true;
            }
        }
        ClassInfoImpl<T, C, F, M> base = getBaseClass();
        if (base != null) {
            return base.canBeReferencedByIDREF();
        }
        return false;
    }

    @Override // com.sun.xml.bind.v2.model.core.ClassInfo
    public final String getName() {
        return nav().getClassName(this.clazz);
    }

    public <A extends Annotation> A readAnnotation(Class<A> a) {
        return (A) reader().getClassAnnotation(a, this.clazz, this);
    }

    @Override // com.sun.xml.bind.v2.model.core.MaybeElement
    public Element<T, C> asElement() {
        if (isElement()) {
            return this;
        }
        return null;
    }

    @Override // com.sun.xml.bind.v2.model.core.ClassInfo
    public List<? extends PropertyInfo<T, C>> getProperties() {
        if (this.properties != null) {
            return this.properties;
        }
        XmlAccessType at = getAccessType();
        this.properties = new FinalArrayList<>();
        findFieldProperties(this.clazz, at);
        findGetterSetterProperties(at);
        if (this.propOrder == DEFAULT_ORDER || this.propOrder == null) {
            XmlAccessOrder ao = getAccessorOrder();
            if (ao == XmlAccessOrder.ALPHABETICAL) {
                Collections.sort(this.properties);
            }
        } else {
            ClassInfoImpl<T, C, F, M>.PropertySorter sorter = new PropertySorter();
            Iterator<PropertyInfoImpl<T, C, F, M>> it = this.properties.iterator();
            while (it.hasNext()) {
                sorter.checkedGet(it.next());
            }
            Collections.sort(this.properties, sorter);
            sorter.checkUnusedProperties();
        }
        PropertyInfoImpl vp = null;
        PropertyInfoImpl ep = null;
        Iterator<PropertyInfoImpl<T, C, F, M>> it2 = this.properties.iterator();
        while (it2.hasNext()) {
            PropertyInfoImpl p = it2.next();
            switch (p.kind()) {
                case ELEMENT:
                case REFERENCE:
                case MAP:
                    ep = p;
                    break;
                case VALUE:
                    if (vp != null) {
                        this.builder.reportError(new IllegalAnnotationException(Messages.MULTIPLE_VALUE_PROPERTY.format(new Object[0]), vp, p));
                    }
                    if (getBaseClass() != null) {
                        this.builder.reportError(new IllegalAnnotationException(Messages.XMLVALUE_IN_DERIVED_TYPE.format(new Object[0]), p));
                    }
                    vp = p;
                    break;
                case ATTRIBUTE:
                    break;
                default:
                    if ($assertionsDisabled) {
                        break;
                    } else {
                        throw new AssertionError();
                    }
            }
        }
        if (ep != null && vp != null) {
            this.builder.reportError(new IllegalAnnotationException(Messages.ELEMENT_AND_VALUE_PROPERTY.format(new Object[0]), vp, ep));
        }
        return this.properties;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void findFieldProperties(C c, XmlAccessType at) {
        ClassInfo<T, C> top;
        Object superClass = nav().getSuperClass(c);
        if (shouldRecurseSuperClass(superClass)) {
            findFieldProperties(superClass, at);
        }
        for (F f : nav().getDeclaredFields(c)) {
            Annotation[] annotations = reader().getAllFieldAnnotations(f, this);
            boolean isDummy = reader().hasFieldAnnotation(OverrideAnnotationOf.class, f);
            if (nav().isTransient(f)) {
                if (hasJAXBAnnotation(annotations)) {
                    this.builder.reportError(new IllegalAnnotationException(Messages.TRANSIENT_FIELD_NOT_BINDABLE.format(nav().getFieldName(f)), getSomeJAXBAnnotation(annotations)));
                }
            } else if (nav().isStaticField(f)) {
                if (hasJAXBAnnotation(annotations)) {
                    addProperty(createFieldSeed(f), annotations, false);
                }
            } else {
                if (at == XmlAccessType.FIELD || ((at == XmlAccessType.PUBLIC_MEMBER && nav().isPublicField(f)) || hasJAXBAnnotation(annotations))) {
                    if (isDummy) {
                        ClassInfo<T, C> classInfo = getBaseClass();
                        while (true) {
                            top = classInfo;
                            if (top == null || top.getProperty(ContentResolver.SCHEME_CONTENT) != null) {
                                break;
                            }
                            classInfo = top.getBaseClass();
                        }
                        DummyPropertyInfo prop = (DummyPropertyInfo) top.getProperty(ContentResolver.SCHEME_CONTENT);
                        PropertySeed seed = createFieldSeed(f);
                        prop.addType(createReferenceProperty(seed));
                    } else {
                        addProperty(createFieldSeed(f), annotations, false);
                    }
                }
                checkFieldXmlLocation(f);
            }
        }
    }

    @Override // com.sun.xml.bind.v2.model.core.ClassInfo
    public final boolean hasValueProperty() {
        ClassInfoImpl<T, C, F, M> bc = getBaseClass();
        if (bc != null && bc.hasValueProperty()) {
            return true;
        }
        for (PropertyInfo p : getProperties()) {
            if (p instanceof ValuePropertyInfo) {
                return true;
            }
        }
        return false;
    }

    @Override // com.sun.xml.bind.v2.model.core.ClassInfo
    public PropertyInfo<T, C> getProperty(String name) {
        for (PropertyInfo<T, C> p : getProperties()) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    protected void checkFieldXmlLocation(F f) {
    }

    /* JADX WARN: Incorrect return type in method signature: <T::Ljava/lang/annotation/Annotation;>(Ljava/lang/Class<TT;>;)TT; */
    private Annotation getClassOrPackageAnnotation(Class cls) {
        Annotation classAnnotation = reader().getClassAnnotation(cls, this.clazz, this);
        if (classAnnotation != null) {
            return classAnnotation;
        }
        return reader().getPackageAnnotation(cls, this.clazz, this);
    }

    private XmlAccessType getAccessType() {
        XmlAccessorType xat = (XmlAccessorType) getClassOrPackageAnnotation(XmlAccessorType.class);
        if (xat != null) {
            return xat.value();
        }
        return XmlAccessType.PUBLIC_MEMBER;
    }

    private XmlAccessOrder getAccessorOrder() {
        XmlAccessorOrder xao = (XmlAccessorOrder) getClassOrPackageAnnotation(XmlAccessorOrder.class);
        if (xao != null) {
            return xao.value();
        }
        return XmlAccessOrder.UNDEFINED;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/ClassInfoImpl$PropertySorter.class */
    public final class PropertySorter extends HashMap<String, Integer> implements Comparator<PropertyInfoImpl> {
        PropertyInfoImpl[] used;
        private Set<String> collidedNames;

        PropertySorter() {
            super(ClassInfoImpl.this.propOrder.length);
            String[] strArr;
            this.used = new PropertyInfoImpl[ClassInfoImpl.this.propOrder.length];
            for (String name : ClassInfoImpl.this.propOrder) {
                if (put(name, Integer.valueOf(size())) != null) {
                    ClassInfoImpl.this.builder.reportError(new IllegalAnnotationException(Messages.DUPLICATE_ENTRY_IN_PROP_ORDER.format(name), ClassInfoImpl.this));
                }
            }
        }

        @Override // java.util.Comparator
        public int compare(PropertyInfoImpl o1, PropertyInfoImpl o2) {
            int lhs = checkedGet(o1);
            int rhs = checkedGet(o2);
            return lhs - rhs;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int checkedGet(PropertyInfoImpl p) {
            Integer i = get(p.getName());
            if (i == null) {
                if (p.kind().isOrdered) {
                    ClassInfoImpl.this.builder.reportError(new IllegalAnnotationException(Messages.PROPERTY_MISSING_FROM_ORDER.format(p.getName()), p));
                }
                i = Integer.valueOf(size());
                put(p.getName(), i);
            }
            int ii = i.intValue();
            if (ii < this.used.length) {
                if (this.used[ii] != null && this.used[ii] != p) {
                    if (this.collidedNames == null) {
                        this.collidedNames = new HashSet();
                    }
                    if (this.collidedNames.add(p.getName())) {
                        ClassInfoImpl.this.builder.reportError(new IllegalAnnotationException(Messages.DUPLICATE_PROPERTIES.format(p.getName()), p, this.used[ii]));
                    }
                }
                this.used[ii] = p;
            }
            return i.intValue();
        }

        public void checkUnusedProperties() {
            int i = 0;
            while (i < this.used.length) {
                if (this.used[i] == null) {
                    String unusedName = ClassInfoImpl.this.propOrder[i];
                    String nearest = EditDistance.findNearest(unusedName, new AbstractList<String>() { // from class: com.sun.xml.bind.v2.model.impl.ClassInfoImpl.PropertySorter.1
                        @Override // java.util.AbstractList, java.util.List
                        public String get(int index) {
                            return ((PropertyInfoImpl) ClassInfoImpl.this.properties.get(index)).getName();
                        }

                        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                        public int size() {
                            return ClassInfoImpl.this.properties.size();
                        }
                    });
                    boolean isOverriding = i > ClassInfoImpl.this.properties.size() - 1 ? false : ((PropertyInfoImpl) ClassInfoImpl.this.properties.get(i)).hasAnnotation(OverrideAnnotationOf.class);
                    if (!isOverriding) {
                        ClassInfoImpl.this.builder.reportError(new IllegalAnnotationException(Messages.PROPERTY_ORDER_CONTAINS_UNUSED_ENTRY.format(unusedName, nearest), ClassInfoImpl.this));
                    }
                }
                i++;
            }
        }
    }

    @Override // com.sun.xml.bind.v2.model.core.ClassInfo
    public boolean hasProperties() {
        return !this.properties.isEmpty();
    }

    private static <T> T pickOne(T... args) {
        for (T arg : args) {
            if (arg != null) {
                return arg;
            }
        }
        return null;
    }

    private static <T> List<T> makeSet(T... args) {
        List<T> l = new FinalArrayList<>();
        for (T arg : args) {
            if (arg != null) {
                l.add(arg);
            }
        }
        return l;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/ClassInfoImpl$ConflictException.class */
    public static final class ConflictException extends Exception {
        final List<Annotation> annotations;

        public ConflictException(List<Annotation> one) {
            this.annotations = one;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/ClassInfoImpl$DuplicateException.class */
    public static final class DuplicateException extends Exception {
        final Annotation a1;
        final Annotation a2;

        public DuplicateException(Annotation a1, Annotation a2) {
            this.a1 = a1;
            this.a2 = a2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/ClassInfoImpl$SecondaryAnnotation.class */
    public enum SecondaryAnnotation {
        JAVA_TYPE(1, XmlJavaTypeAdapter.class),
        ID_IDREF(2, XmlID.class, XmlIDREF.class),
        BINARY(4, XmlInlineBinaryData.class, XmlMimeType.class, XmlAttachmentRef.class),
        ELEMENT_WRAPPER(8, XmlElementWrapper.class),
        LIST(16, XmlList.class),
        SCHEMA_TYPE(32, XmlSchemaType.class);
        
        final int bitMask;
        final Class<? extends Annotation>[] members;

        SecondaryAnnotation(int bitMask, Class... clsArr) {
            this.bitMask = bitMask;
            this.members = clsArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/ClassInfoImpl$PropertyGroup.class */
    public enum PropertyGroup {
        TRANSIENT(false, false, false, false, false, false),
        ANY_ATTRIBUTE(true, false, false, false, false, false),
        ATTRIBUTE(true, true, true, false, true, true),
        VALUE(true, true, true, false, true, true),
        ELEMENT(true, true, true, true, true, true),
        ELEMENT_REF(true, false, false, true, false, false),
        MAP(false, false, false, true, false, false);
        
        final int allowedsecondaryAnnotations;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ClassInfoImpl.class.desiredAssertionStatus();
        }

        PropertyGroup(boolean... bits) {
            int mask = 0;
            if (!$assertionsDisabled && bits.length != ClassInfoImpl.SECONDARY_ANNOTATIONS.length) {
                throw new AssertionError();
            }
            for (int i = 0; i < bits.length; i++) {
                if (bits[i]) {
                    mask |= ClassInfoImpl.SECONDARY_ANNOTATIONS[i].bitMask;
                }
            }
            this.allowedsecondaryAnnotations = mask ^ (-1);
        }

        boolean allows(SecondaryAnnotation a) {
            return (this.allowedsecondaryAnnotations & a.bitMask) == 0;
        }
    }

    private void checkConflict(Annotation a, Annotation b) throws DuplicateException {
        if (!$assertionsDisabled && b == null) {
            throw new AssertionError();
        }
        if (a != null) {
            throw new DuplicateException(a, b);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void addProperty(PropertySeed<T, C, F, M> seed, Annotation[] annotations, boolean dummy) {
        SecondaryAnnotation[] secondaryAnnotationArr;
        Class<? extends Annotation>[] clsArr;
        XmlTransient t = null;
        XmlAnyAttribute aa = null;
        XmlAttribute a = null;
        XmlValue v = null;
        XmlElement e1 = null;
        XmlElements e2 = null;
        XmlElementRef r1 = null;
        XmlElementRefs r2 = null;
        XmlAnyElement xae = null;
        XmlMixed mx = null;
        OverrideAnnotationOf ov = null;
        int secondaryAnnotations = 0;
        try {
            for (Annotation ann : annotations) {
                Integer index = ANNOTATION_NUMBER_MAP.get(ann.annotationType());
                if (index != null) {
                    switch (index.intValue()) {
                        case 0:
                            checkConflict(t, ann);
                            t = (XmlTransient) ann;
                            continue;
                        case 1:
                            checkConflict(aa, ann);
                            aa = (XmlAnyAttribute) ann;
                            continue;
                        case 2:
                            checkConflict(a, ann);
                            a = (XmlAttribute) ann;
                            continue;
                        case 3:
                            checkConflict(v, ann);
                            v = (XmlValue) ann;
                            continue;
                        case 4:
                            checkConflict(e1, ann);
                            e1 = (XmlElement) ann;
                            continue;
                        case 5:
                            checkConflict(e2, ann);
                            e2 = (XmlElements) ann;
                            continue;
                        case 6:
                            checkConflict(r1, ann);
                            r1 = (XmlElementRef) ann;
                            continue;
                        case 7:
                            checkConflict(r2, ann);
                            r2 = (XmlElementRefs) ann;
                            continue;
                        case 8:
                            checkConflict(xae, ann);
                            xae = (XmlAnyElement) ann;
                            continue;
                        case 9:
                            checkConflict(mx, ann);
                            mx = (XmlMixed) ann;
                            continue;
                        case 10:
                            checkConflict(ov, ann);
                            ov = (OverrideAnnotationOf) ann;
                            continue;
                        default:
                            secondaryAnnotations |= 1 << (index.intValue() - 20);
                            continue;
                    }
                }
            }
            PropertyGroup group = null;
            int groupCount = 0;
            if (t != null) {
                group = PropertyGroup.TRANSIENT;
                groupCount = 0 + 1;
            }
            if (aa != null) {
                group = PropertyGroup.ANY_ATTRIBUTE;
                groupCount++;
            }
            if (a != null) {
                group = PropertyGroup.ATTRIBUTE;
                groupCount++;
            }
            if (v != null) {
                group = PropertyGroup.VALUE;
                groupCount++;
            }
            if (e1 != null || e2 != null) {
                group = PropertyGroup.ELEMENT;
                groupCount++;
            }
            if (r1 != null || r2 != null || xae != null || mx != null || ov != null) {
                group = PropertyGroup.ELEMENT_REF;
                groupCount++;
            }
            if (groupCount > 1) {
                throw new ConflictException(makeSet(t, aa, a, v, (Annotation) pickOne(e1, e2), (Annotation) pickOne(r1, r2, xae)));
            }
            if (group == null) {
                if (!$assertionsDisabled && groupCount != 0) {
                    throw new AssertionError();
                }
                if (nav().isSubClassOf(seed.getRawType(), nav().ref(Map.class)) && !seed.hasAnnotation(XmlJavaTypeAdapter.class)) {
                    group = PropertyGroup.MAP;
                } else {
                    group = PropertyGroup.ELEMENT;
                }
            } else if (group.equals(PropertyGroup.ELEMENT) && nav().isSubClassOf(seed.getRawType(), nav().ref(Map.class)) && !seed.hasAnnotation(XmlJavaTypeAdapter.class)) {
                group = PropertyGroup.MAP;
            }
            if ((secondaryAnnotations & group.allowedsecondaryAnnotations) != 0) {
                for (SecondaryAnnotation sa : SECONDARY_ANNOTATIONS) {
                    if (!group.allows(sa)) {
                        for (Class<A> cls : sa.members) {
                            Annotation offender = seed.readAnnotation(cls);
                            if (offender != null) {
                                this.builder.reportError(new IllegalAnnotationException(Messages.ANNOTATION_NOT_ALLOWED.format(cls.getSimpleName()), offender));
                                return;
                            }
                        }
                        continue;
                    }
                }
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
            }
            switch (group) {
                case TRANSIENT:
                    return;
                case ANY_ATTRIBUTE:
                    if (this.attributeWildcard != null) {
                        this.builder.reportError(new IllegalAnnotationException(Messages.TWO_ATTRIBUTE_WILDCARDS.format(nav().getClassName(getClazz())), aa, this.attributeWildcard));
                        return;
                    }
                    this.attributeWildcard = seed;
                    if (inheritsAttributeWildcard()) {
                        this.builder.reportError(new IllegalAnnotationException(Messages.SUPER_CLASS_HAS_WILDCARD.format(new Object[0]), aa, getInheritedAttributeWildcard()));
                        return;
                    } else if (!nav().isSubClassOf(seed.getRawType(), nav().ref(Map.class))) {
                        this.builder.reportError(new IllegalAnnotationException(Messages.INVALID_ATTRIBUTE_WILDCARD_TYPE.format(nav().getTypeName(seed.getRawType())), aa, getInheritedAttributeWildcard()));
                        return;
                    } else {
                        return;
                    }
                case ATTRIBUTE:
                    this.properties.add(createAttributeProperty(seed));
                    return;
                case VALUE:
                    this.properties.add(createValueProperty(seed));
                    return;
                case ELEMENT:
                    this.properties.add(createElementProperty(seed));
                    return;
                case ELEMENT_REF:
                    this.properties.add(createReferenceProperty(seed));
                    return;
                case MAP:
                    this.properties.add(createMapProperty(seed));
                    return;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    return;
            }
        } catch (ConflictException x) {
            List<Annotation> err = x.annotations;
            this.builder.reportError(new IllegalAnnotationException(Messages.MUTUALLY_EXCLUSIVE_ANNOTATIONS.format(nav().getClassName(getClazz()) + '#' + seed.getName(), err.get(0).annotationType().getName(), err.get(1).annotationType().getName()), err.get(0), err.get(1)));
        } catch (DuplicateException e) {
            this.builder.reportError(new IllegalAnnotationException(Messages.DUPLICATE_ANNOTATIONS.format(e.a1.annotationType().getName()), e.a1, e.a2));
        }
    }

    protected ReferencePropertyInfoImpl<T, C, F, M> createReferenceProperty(PropertySeed<T, C, F, M> seed) {
        return new ReferencePropertyInfoImpl<>(this, seed);
    }

    protected AttributePropertyInfoImpl<T, C, F, M> createAttributeProperty(PropertySeed<T, C, F, M> seed) {
        return new AttributePropertyInfoImpl<>(this, seed);
    }

    protected ValuePropertyInfoImpl<T, C, F, M> createValueProperty(PropertySeed<T, C, F, M> seed) {
        return new ValuePropertyInfoImpl<>(this, seed);
    }

    protected ElementPropertyInfoImpl<T, C, F, M> createElementProperty(PropertySeed<T, C, F, M> seed) {
        return new ElementPropertyInfoImpl<>(this, seed);
    }

    protected MapPropertyInfoImpl<T, C, F, M> createMapProperty(PropertySeed<T, C, F, M> seed) {
        return new MapPropertyInfoImpl<>(this, seed);
    }

    private void findGetterSetterProperties(XmlAccessType at) {
        Annotation[] r;
        Map<String, M> getters = new LinkedHashMap<>();
        Map<String, M> setters = new LinkedHashMap<>();
        C c = this.clazz;
        do {
            collectGetterSetters(this.clazz, getters, setters);
            c = nav().getSuperClass(c);
        } while (shouldRecurseSuperClass(c));
        Set<String> complete = new TreeSet<>(getters.keySet());
        complete.retainAll(setters.keySet());
        resurrect(getters, complete);
        resurrect(setters, complete);
        for (String name : complete) {
            M getter = getters.get(name);
            M setter = setters.get(name);
            Annotation[] ga = getter != null ? reader().getAllMethodAnnotations(getter, new MethodLocatable(this, getter, nav())) : EMPTY_ANNOTATIONS;
            Annotation[] sa = setter != null ? reader().getAllMethodAnnotations(setter, new MethodLocatable(this, setter, nav())) : EMPTY_ANNOTATIONS;
            boolean hasAnnotation = hasJAXBAnnotation(ga) || hasJAXBAnnotation(sa);
            boolean isOverriding = false;
            if (!hasAnnotation) {
                isOverriding = getter != null && nav().isOverriding(getter, c) && setter != null && nav().isOverriding(setter, c);
            }
            if ((at == XmlAccessType.PROPERTY && !isOverriding) || ((at == XmlAccessType.PUBLIC_MEMBER && isConsideredPublic(getter) && isConsideredPublic(setter) && !isOverriding) || hasAnnotation)) {
                if (getter != null && setter != null && !nav().isSameType(nav().getReturnType(getter), nav().getMethodParameters(setter)[0])) {
                    this.builder.reportError(new IllegalAnnotationException(Messages.GETTER_SETTER_INCOMPATIBLE_TYPE.format(nav().getTypeName(nav().getReturnType(getter)), nav().getTypeName(nav().getMethodParameters(setter)[0])), new MethodLocatable(this, getter, nav()), new MethodLocatable(this, setter, nav())));
                } else {
                    if (ga.length == 0) {
                        r = sa;
                    } else if (sa.length == 0) {
                        r = ga;
                    } else {
                        r = new Annotation[ga.length + sa.length];
                        System.arraycopy(ga, 0, r, 0, ga.length);
                        System.arraycopy(sa, 0, r, ga.length, sa.length);
                    }
                    addProperty(createAccessorSeed(getter, setter), r, false);
                }
            }
        }
        getters.keySet().removeAll(complete);
        setters.keySet().removeAll(complete);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void collectGetterSetters(C c, Map<String, M> getters, Map<String, M> setters) {
        Object superClass = nav().getSuperClass(c);
        if (shouldRecurseSuperClass(superClass)) {
            collectGetterSetters(superClass, getters, setters);
        }
        Collection<? extends M> methods = nav().getDeclaredMethods(c);
        Map<String, List<M>> allSetters = new LinkedHashMap<>();
        for (M method : methods) {
            boolean used = false;
            if (!nav().isBridgeMethod(method)) {
                String name = nav().getMethodName(method);
                int arity = nav().getMethodParameters(method).length;
                if (nav().isStaticMethod(method)) {
                    ensureNoAnnotation(method);
                } else {
                    String propName = getPropertyNameFromGetMethod(name);
                    if (propName != null && arity == 0) {
                        getters.put(propName, method);
                        used = true;
                    }
                    String propName2 = getPropertyNameFromSetMethod(name);
                    if (propName2 != null && arity == 1) {
                        List<M> propSetters = allSetters.get(propName2);
                        List<M> propSetters2 = propSetters;
                        if (null == propSetters) {
                            List<M> propSetters3 = new ArrayList<>();
                            allSetters.put(propName2, propSetters3);
                            propSetters2 = propSetters3;
                        }
                        propSetters2.add(method);
                        used = true;
                    }
                    if (!used) {
                        ensureNoAnnotation(method);
                    }
                }
            }
        }
        for (Map.Entry<String, M> entry : getters.entrySet()) {
            String propName3 = entry.getKey();
            M getter = entry.getValue();
            List<M> propSetters4 = allSetters.remove(propName3);
            if (null != propSetters4) {
                Object returnType = nav().getReturnType(getter);
                Iterator<M> it = propSetters4.iterator();
                while (true) {
                    if (it.hasNext()) {
                        M setter = it.next();
                        if (nav().isSameType(nav().getMethodParameters(setter)[0], returnType)) {
                            setters.put(propName3, setter);
                            break;
                        }
                    }
                }
            }
        }
        for (Map.Entry<String, List<M>> e : allSetters.entrySet()) {
            setters.put(e.getKey(), e.getValue().get(0));
        }
    }

    private boolean shouldRecurseSuperClass(C sc) {
        return sc != null && (this.builder.isReplaced(sc) || reader().hasClassAnnotation(sc, XmlTransient.class));
    }

    private boolean isConsideredPublic(M m) {
        return m == null || nav().isPublicMethod(m);
    }

    private void resurrect(Map<String, M> methods, Set<String> complete) {
        for (Map.Entry<String, M> e : methods.entrySet()) {
            if (!complete.contains(e.getKey()) && hasJAXBAnnotation(reader().getAllMethodAnnotations(e.getValue(), this))) {
                complete.add(e.getKey());
            }
        }
    }

    private void ensureNoAnnotation(M method) {
        Annotation[] annotations = reader().getAllMethodAnnotations(method, this);
        for (Annotation a : annotations) {
            if (isJAXBAnnotation(a)) {
                this.builder.reportError(new IllegalAnnotationException(Messages.ANNOTATION_ON_WRONG_METHOD.format(new Object[0]), a));
                return;
            }
        }
    }

    private static boolean isJAXBAnnotation(Annotation a) {
        return ANNOTATION_NUMBER_MAP.containsKey(a.annotationType());
    }

    private static boolean hasJAXBAnnotation(Annotation[] annotations) {
        return getSomeJAXBAnnotation(annotations) != null;
    }

    private static Annotation getSomeJAXBAnnotation(Annotation[] annotations) {
        for (Annotation a : annotations) {
            if (isJAXBAnnotation(a)) {
                return a;
            }
        }
        return null;
    }

    private static String getPropertyNameFromGetMethod(String name) {
        if (name.startsWith("get") && name.length() > 3) {
            return name.substring(3);
        }
        if (name.startsWith("is") && name.length() > 2) {
            return name.substring(2);
        }
        return null;
    }

    private static String getPropertyNameFromSetMethod(String name) {
        if (name.startsWith("set") && name.length() > 3) {
            return name.substring(3);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PropertySeed<T, C, F, M> createFieldSeed(F f) {
        return new FieldPropertySeed(this, f);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PropertySeed<T, C, F, M> createAccessorSeed(M getter, M setter) {
        return new GetterSetterPropertySeed(this, getter, setter);
    }

    @Override // com.sun.xml.bind.v2.model.core.MaybeElement
    public final boolean isElement() {
        return this.elementName != null;
    }

    @Override // com.sun.xml.bind.v2.model.core.ClassInfo
    public boolean isAbstract() {
        return nav().isAbstract(this.clazz);
    }

    @Override // com.sun.xml.bind.v2.model.core.ClassInfo
    public boolean isOrdered() {
        return this.propOrder != null;
    }

    @Override // com.sun.xml.bind.v2.model.core.ClassInfo
    public final boolean isFinal() {
        return nav().isFinal(this.clazz);
    }

    @Override // com.sun.xml.bind.v2.model.core.ClassInfo
    public final boolean hasSubClasses() {
        return this.hasSubClasses;
    }

    @Override // com.sun.xml.bind.v2.model.core.ClassInfo
    public final boolean hasAttributeWildcard() {
        return declaresAttributeWildcard() || inheritsAttributeWildcard();
    }

    @Override // com.sun.xml.bind.v2.model.core.ClassInfo
    public final boolean inheritsAttributeWildcard() {
        return getInheritedAttributeWildcard() != null;
    }

    @Override // com.sun.xml.bind.v2.model.core.ClassInfo
    public final boolean declaresAttributeWildcard() {
        return this.attributeWildcard != null;
    }

    private PropertySeed<T, C, F, M> getInheritedAttributeWildcard() {
        ClassInfoImpl<T, C, F, M> baseClass = getBaseClass();
        while (true) {
            ClassInfoImpl<T, C, F, M> c = baseClass;
            if (c != null) {
                if (c.attributeWildcard == null) {
                    baseClass = c.getBaseClass();
                } else {
                    return c.attributeWildcard;
                }
            } else {
                return null;
            }
        }
    }

    @Override // com.sun.xml.bind.v2.model.core.MaybeElement
    public final QName getElementName() {
        return this.elementName;
    }

    @Override // com.sun.xml.bind.v2.model.core.NonElement
    public final QName getTypeName() {
        return this.typeName;
    }

    @Override // com.sun.xml.bind.v2.model.core.NonElement
    public final boolean isSimpleType() {
        List<? extends PropertyInfo> props = getProperties();
        return props.size() == 1 && props.get(0).kind() == PropertyKind.VALUE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.xml.bind.v2.model.impl.TypeInfoImpl
    public void link() {
        getProperties();
        Map<String, PropertyInfoImpl> names = new HashMap<>();
        Iterator<PropertyInfoImpl<T, C, F, M>> it = this.properties.iterator();
        while (it.hasNext()) {
            PropertyInfoImpl<T, C, F, M> p = it.next();
            p.link();
            PropertyInfoImpl old = names.put(p.getName(), p);
            if (old != null) {
                this.builder.reportError(new IllegalAnnotationException(Messages.PROPERTY_COLLISION.format(p.getName()), p, old));
            }
        }
        super.link();
    }

    @Override // com.sun.xml.bind.v2.model.annotation.Locatable
    public Location getLocation() {
        return nav().getClassLocation(this.clazz);
    }

    private boolean hasFactoryConstructor(XmlType t) {
        if (t == null) {
            return false;
        }
        String method = t.factoryMethod();
        T fClass = reader().getClassValue(t, "factoryClass");
        if (method.length() > 0) {
            if (nav().isSameType(fClass, nav().ref(XmlType.DEFAULT.class))) {
                fClass = nav().use(this.clazz);
            }
            Iterator<? extends M> it = nav().getDeclaredMethods(nav().asDecl((Navigator<T, C, F, M>) fClass)).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                M m = (M) it.next();
                if (nav().getMethodName(m).equals(method) && nav().isSameType(nav().getReturnType(m), nav().use(this.clazz)) && nav().getMethodParameters(m).length == 0 && nav().isStaticMethod(m)) {
                    this.factoryMethod = m;
                    break;
                }
            }
            if (this.factoryMethod == null) {
                this.builder.reportError(new IllegalAnnotationException(Messages.NO_FACTORY_METHOD.format(nav().getClassName(nav().asDecl((Navigator<T, C, F, M>) fClass)), method), this));
            }
        } else if (!nav().isSameType(fClass, nav().ref(XmlType.DEFAULT.class))) {
            this.builder.reportError(new IllegalAnnotationException(Messages.FACTORY_CLASS_NEEDS_FACTORY_METHOD.format(nav().getClassName(nav().asDecl((Navigator<T, C, F, M>) fClass))), this));
        }
        return this.factoryMethod != null;
    }

    public Method getFactoryMethod() {
        return (Method) this.factoryMethod;
    }

    public String toString() {
        return "ClassInfo(" + this.clazz + ')';
    }
}
