package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.v2.model.annotation.AnnotationReader;
import com.sun.xml.bind.v2.model.core.ClassInfo;
import com.sun.xml.bind.v2.model.core.Element;
import com.sun.xml.bind.v2.model.core.ElementInfo;
import com.sun.xml.bind.v2.model.core.NonElement;
import com.sun.xml.bind.v2.model.core.PropertyKind;
import com.sun.xml.bind.v2.model.core.ReferencePropertyInfo;
import com.sun.xml.bind.v2.model.core.TypeInfoSet;
import com.sun.xml.bind.v2.model.core.WildcardMode;
import com.sun.xml.bind.v2.model.nav.Navigator;
import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.namespace.QName;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/ReferencePropertyInfoImpl.class */
public class ReferencePropertyInfoImpl<T, C, F, M> extends ERPropertyInfoImpl<T, C, F, M> implements ReferencePropertyInfo<T, C>, DummyPropertyInfo<T, C, F, M> {
    private Set<Element<T, C>> types;
    private Set<ReferencePropertyInfoImpl<T, C, F, M>> subTypes;
    private final boolean isMixed;
    private final WildcardMode wildcard;
    private final C domHandler;
    private Boolean isRequired;
    private static boolean is2_2;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ReferencePropertyInfoImpl.class.desiredAssertionStatus();
        is2_2 = true;
    }

    public ReferencePropertyInfoImpl(ClassInfoImpl<T, C, F, M> classInfo, PropertySeed<T, C, F, M> seed) {
        super(classInfo, seed);
        this.subTypes = new LinkedHashSet();
        this.isMixed = seed.readAnnotation(XmlMixed.class) != null;
        XmlAnyElement xae = (XmlAnyElement) seed.readAnnotation(XmlAnyElement.class);
        if (xae == null) {
            this.wildcard = null;
            this.domHandler = null;
            return;
        }
        this.wildcard = xae.lax() ? WildcardMode.LAX : WildcardMode.SKIP;
        this.domHandler = nav().asDecl((Navigator<T, C, F, M>) reader().getClassValue(xae, "value"));
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public Set<? extends Element<T, C>> ref() {
        return getElements();
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public PropertyKind kind() {
        return PropertyKind.REFERENCE;
    }

    @Override // com.sun.xml.bind.v2.model.core.ReferencePropertyInfo
    public Set<? extends Element<T, C>> getElements() {
        if (this.types == null) {
            calcTypes(false);
        }
        if ($assertionsDisabled || this.types != null) {
            return this.types;
        }
        throw new AssertionError();
    }

    private void calcTypes(boolean last) {
        XmlElementRef[] ann;
        XmlElementRef[] ann2;
        XmlElementRef[] xmlElementRefArr;
        boolean yield;
        XmlElementRef[] xmlElementRefArr2;
        boolean yield2;
        this.types = new LinkedHashSet();
        XmlElementRefs refs = (XmlElementRefs) this.seed.readAnnotation(XmlElementRefs.class);
        XmlElementRef ref = (XmlElementRef) this.seed.readAnnotation(XmlElementRef.class);
        if (refs != null && ref != null) {
            this.parent.builder.reportError(new IllegalAnnotationException(Messages.MUTUALLY_EXCLUSIVE_ANNOTATIONS.format(nav().getClassName(this.parent.getClazz()) + '#' + this.seed.getName(), ref.annotationType().getName(), refs.annotationType().getName()), ref, refs));
        }
        if (refs != null) {
            ann = refs.value();
        } else {
            ann = ref != null ? new XmlElementRef[]{ref} : null;
        }
        this.isRequired = Boolean.valueOf(!isCollection());
        if (ann != null) {
            Navigator<T, C, F, M> nav = nav();
            AnnotationReader<T, C, F, M> reader = reader();
            T defaultType = nav.ref(XmlElementRef.DEFAULT.class);
            C je = nav.asDecl(JAXBElement.class);
            for (XmlElementRef r : ann) {
                T type = reader.getClassValue(r, "type");
                if (nav().isSameType(type, defaultType)) {
                    type = nav.erasure(getIndividualType());
                }
                if (nav.getBaseClass(type, je) != null) {
                    yield2 = addGenericElement(r);
                } else {
                    yield2 = addAllSubtypes(type);
                }
                if (this.isRequired.booleanValue() && !isRequired(r)) {
                    this.isRequired = false;
                }
                if (last && !yield2) {
                    if (nav().isSameType(type, nav.ref(JAXBElement.class))) {
                        this.parent.builder.reportError(new IllegalAnnotationException(Messages.NO_XML_ELEMENT_DECL.format(getEffectiveNamespaceFor(r), r.name()), this));
                        return;
                    } else {
                        this.parent.builder.reportError(new IllegalAnnotationException(Messages.INVALID_XML_ELEMENT_REF.format(type), this));
                        return;
                    }
                }
            }
        }
        for (ReferencePropertyInfoImpl<T, C, F, M> info : this.subTypes) {
            PropertySeed sd = info.seed;
            XmlElementRefs refs2 = (XmlElementRefs) sd.readAnnotation(XmlElementRefs.class);
            XmlElementRef ref2 = (XmlElementRef) sd.readAnnotation(XmlElementRef.class);
            if (refs2 != null && ref2 != null) {
                this.parent.builder.reportError(new IllegalAnnotationException(Messages.MUTUALLY_EXCLUSIVE_ANNOTATIONS.format(nav().getClassName(this.parent.getClazz()) + '#' + this.seed.getName(), ref2.annotationType().getName(), refs2.annotationType().getName()), ref2, refs2));
            }
            if (refs2 != null) {
                ann2 = refs2.value();
            } else {
                ann2 = ref2 != null ? new XmlElementRef[]{ref2} : null;
            }
            if (ann2 != null) {
                Navigator<T, C, F, M> nav2 = nav();
                AnnotationReader<T, C, F, M> reader2 = reader();
                T defaultType2 = nav2.ref(XmlElementRef.DEFAULT.class);
                C je2 = nav2.asDecl(JAXBElement.class);
                for (XmlElementRef r2 : ann2) {
                    T type2 = reader2.getClassValue(r2, "type");
                    if (nav().isSameType(type2, defaultType2)) {
                        type2 = nav2.erasure(getIndividualType());
                    }
                    if (nav2.getBaseClass(type2, je2) != null) {
                        yield = addGenericElement(r2, info);
                    } else {
                        yield = addAllSubtypes(type2);
                    }
                    if (last && !yield) {
                        if (nav().isSameType(type2, nav2.ref(JAXBElement.class))) {
                            this.parent.builder.reportError(new IllegalAnnotationException(Messages.NO_XML_ELEMENT_DECL.format(getEffectiveNamespaceFor(r2), r2.name()), this));
                            return;
                        } else {
                            this.parent.builder.reportError(new IllegalAnnotationException(Messages.INVALID_XML_ELEMENT_REF.format(new Object[0]), this));
                            return;
                        }
                    }
                }
                continue;
            }
        }
        this.types = Collections.unmodifiableSet(this.types);
    }

    @Override // com.sun.xml.bind.v2.model.core.ReferencePropertyInfo
    public boolean isRequired() {
        if (this.isRequired == null) {
            calcTypes(false);
        }
        return this.isRequired.booleanValue();
    }

    private boolean isRequired(XmlElementRef ref) {
        if (is2_2) {
            try {
                return ref.required();
            } catch (LinkageError e) {
                is2_2 = false;
                return true;
            }
        }
        return true;
    }

    private boolean addGenericElement(XmlElementRef r) {
        String nsUri = getEffectiveNamespaceFor(r);
        return addGenericElement(this.parent.owner.getElementInfo((TypeInfoSet) this.parent.getClazz(), new QName(nsUri, r.name())));
    }

    private boolean addGenericElement(XmlElementRef r, ReferencePropertyInfoImpl<T, C, F, M> info) {
        String nsUri = info.getEffectiveNamespaceFor(r);
        ElementInfo ei = this.parent.owner.getElementInfo((TypeInfoSet) info.parent.getClazz(), new QName(nsUri, r.name()));
        this.types.add(ei);
        return true;
    }

    private String getEffectiveNamespaceFor(XmlElementRef r) {
        String nsUri = r.namespace();
        XmlSchema xs = (XmlSchema) reader().getPackageAnnotation(XmlSchema.class, this.parent.getClazz(), this);
        if (xs != null && xs.attributeFormDefault() == XmlNsForm.QUALIFIED && nsUri.length() == 0) {
            nsUri = this.parent.builder.defaultNsUri;
        }
        return nsUri;
    }

    private boolean addGenericElement(ElementInfo<T, C> ei) {
        if (ei == null) {
            return false;
        }
        this.types.add(ei);
        for (ElementInfo<T, C> subst : ei.getSubstitutionMembers()) {
            addGenericElement(subst);
        }
        return true;
    }

    private boolean addAllSubtypes(T type) {
        Navigator<T, C, F, M> nav = nav();
        NonElement classInfo = this.parent.builder.getClassInfo(nav.asDecl((Navigator<T, C, F, M>) type), this);
        if (!(classInfo instanceof ClassInfo)) {
            return false;
        }
        boolean result = false;
        ClassInfo<T, C> c = (ClassInfo) classInfo;
        if (c.isElement()) {
            this.types.add(c.asElement());
            result = true;
        }
        for (ClassInfo<T, C> ci : this.parent.owner.beans().values()) {
            if (ci.isElement() && nav.isSubClassOf(ci.getType(), type)) {
                this.types.add(ci.asElement());
                result = true;
            }
        }
        for (ElementInfo<T, C> ei : this.parent.owner.getElementMappings(null).values()) {
            if (nav.isSubClassOf(ei.getType(), type)) {
                this.types.add(ei);
                result = true;
            }
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.bind.v2.model.impl.PropertyInfoImpl
    public void link() {
        super.link();
        calcTypes(true);
    }

    @Override // com.sun.xml.bind.v2.model.impl.DummyPropertyInfo
    public final void addType(PropertyInfoImpl<T, C, F, M> info) {
        this.subTypes.add((ReferencePropertyInfoImpl) info);
    }

    @Override // com.sun.xml.bind.v2.model.core.ReferencePropertyInfo
    public final boolean isMixed() {
        return this.isMixed;
    }

    @Override // com.sun.xml.bind.v2.model.core.ReferencePropertyInfo
    public final WildcardMode getWildcard() {
        return this.wildcard;
    }

    @Override // com.sun.xml.bind.v2.model.core.ReferencePropertyInfo
    public final C getDOMHandler() {
        return this.domHandler;
    }
}
