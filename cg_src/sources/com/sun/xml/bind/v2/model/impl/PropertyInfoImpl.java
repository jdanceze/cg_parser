package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.v2.TODO;
import com.sun.xml.bind.v2.model.annotation.AnnotationReader;
import com.sun.xml.bind.v2.model.annotation.Locatable;
import com.sun.xml.bind.v2.model.core.Adapter;
import com.sun.xml.bind.v2.model.core.ID;
import com.sun.xml.bind.v2.model.core.PropertyInfo;
import com.sun.xml.bind.v2.model.core.TypeInfo;
import com.sun.xml.bind.v2.model.nav.Navigator;
import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
import com.sun.xml.bind.v2.runtime.Location;
import com.sun.xml.bind.v2.runtime.SwaRefAdapter;
import java.lang.annotation.Annotation;
import java.util.Collection;
import javax.activation.MimeType;
import javax.xml.bind.annotation.XmlAttachmentRef;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlInlineBinaryData;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import javax.xml.namespace.QName;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/PropertyInfoImpl.class */
public abstract class PropertyInfoImpl<T, C, F, M> implements PropertyInfo<T, C>, Locatable, Comparable<PropertyInfoImpl> {
    protected final PropertySeed<T, C, F, M> seed;
    private final boolean isCollection;
    private final ID id;
    private final MimeType expectedMimeType;
    private final boolean inlineBinary;
    private final QName schemaType;
    protected final ClassInfoImpl<T, C, F, M> parent;
    private final Adapter<T, C> adapter;

    /* JADX INFO: Access modifiers changed from: protected */
    public PropertyInfoImpl(ClassInfoImpl<T, C, F, M> parent, PropertySeed<T, C, F, M> spi) {
        this.seed = spi;
        this.parent = parent;
        if (parent == null) {
            throw new AssertionError();
        }
        MimeType mt = Util.calcExpectedMediaType(this.seed, parent.builder);
        if (mt != null && !kind().canHaveXmlMimeType) {
            parent.builder.reportError(new IllegalAnnotationException(Messages.ILLEGAL_ANNOTATION.format(XmlMimeType.class.getName()), this.seed.readAnnotation(XmlMimeType.class)));
            mt = null;
        }
        this.expectedMimeType = mt;
        this.inlineBinary = this.seed.hasAnnotation(XmlInlineBinaryData.class);
        T t = this.seed.getRawType();
        XmlJavaTypeAdapter xjta = getApplicableAdapter(t);
        if (xjta != null) {
            this.isCollection = false;
            this.adapter = new Adapter<>(xjta, reader(), nav());
        } else {
            this.isCollection = nav().isSubClassOf(t, nav().ref(Collection.class)) || nav().isArrayButNotByteArray(t);
            XmlJavaTypeAdapter xjta2 = getApplicableAdapter(getIndividualType());
            if (xjta2 == null) {
                XmlAttachmentRef xsa = (XmlAttachmentRef) this.seed.readAnnotation(XmlAttachmentRef.class);
                if (xsa != null) {
                    parent.builder.hasSwaRef = true;
                    this.adapter = new Adapter<>(nav().asDecl(SwaRefAdapter.class), nav());
                } else {
                    this.adapter = null;
                    XmlJavaTypeAdapter xjta3 = (XmlJavaTypeAdapter) this.seed.readAnnotation(XmlJavaTypeAdapter.class);
                    if (xjta3 != null) {
                        T ad = reader().getClassValue(xjta3, "value");
                        parent.builder.reportError(new IllegalAnnotationException(Messages.UNMATCHABLE_ADAPTER.format(nav().getTypeName(ad), nav().getTypeName(t)), xjta3));
                    }
                }
            } else {
                this.adapter = new Adapter<>(xjta2, reader(), nav());
            }
        }
        this.id = calcId();
        this.schemaType = Util.calcSchemaType(reader(), this.seed, parent.clazz, getIndividualType(), this);
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public ClassInfoImpl<T, C, F, M> parent() {
        return this.parent;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Navigator<T, C, F, M> nav() {
        return this.parent.nav();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final AnnotationReader<T, C, F, M> reader() {
        return this.parent.reader();
    }

    public T getRawType() {
        return this.seed.getRawType();
    }

    public T getIndividualType() {
        if (this.adapter != null) {
            return this.adapter.defaultType;
        }
        T raw = getRawType();
        if (!isCollection()) {
            return raw;
        }
        if (nav().isArrayButNotByteArray(raw)) {
            return nav().getComponentType(raw);
        }
        T bt = nav().getBaseClass(raw, nav().asDecl(Collection.class));
        if (nav().isParameterizedType(bt)) {
            return nav().getTypeArgument(bt, 0);
        }
        return nav().ref(Object.class);
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public final String getName() {
        return this.seed.getName();
    }

    private boolean isApplicable(XmlJavaTypeAdapter jta, T declaredType) {
        if (jta == null) {
            return false;
        }
        T type = reader().getClassValue(jta, "type");
        if (nav().isSameType(declaredType, type)) {
            return true;
        }
        T ad = reader().getClassValue(jta, "value");
        T ba = nav().getBaseClass(ad, nav().asDecl(XmlAdapter.class));
        if (!nav().isParameterizedType(ba)) {
            return true;
        }
        T inMemType = nav().getTypeArgument(ba, 1);
        return nav().isSubClassOf(declaredType, inMemType);
    }

    private XmlJavaTypeAdapter getApplicableAdapter(T type) {
        XmlJavaTypeAdapter jta;
        XmlJavaTypeAdapter[] value;
        XmlJavaTypeAdapter jta2 = (XmlJavaTypeAdapter) this.seed.readAnnotation(XmlJavaTypeAdapter.class);
        if (jta2 != null && isApplicable(jta2, type)) {
            return jta2;
        }
        XmlJavaTypeAdapters jtas = (XmlJavaTypeAdapters) reader().getPackageAnnotation(XmlJavaTypeAdapters.class, this.parent.clazz, this.seed);
        if (jtas != null) {
            for (XmlJavaTypeAdapter xjta : jtas.value()) {
                if (isApplicable(xjta, type)) {
                    return xjta;
                }
            }
        }
        XmlJavaTypeAdapter jta3 = (XmlJavaTypeAdapter) reader().getPackageAnnotation(XmlJavaTypeAdapter.class, this.parent.clazz, this.seed);
        if (isApplicable(jta3, type)) {
            return jta3;
        }
        C refType = nav().asDecl((Navigator<T, C, F, M>) type);
        if (refType != null && (jta = (XmlJavaTypeAdapter) reader().getClassAnnotation(XmlJavaTypeAdapter.class, refType, this.seed)) != null && isApplicable(jta, type)) {
            return jta;
        }
        return null;
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public Adapter<T, C> getAdapter() {
        return this.adapter;
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public final String displayName() {
        return nav().getClassName(this.parent.getClazz()) + '#' + getName();
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public final ID id() {
        return this.id;
    }

    private ID calcId() {
        if (this.seed.hasAnnotation(XmlID.class)) {
            if (!nav().isSameType(getIndividualType(), nav().ref(String.class))) {
                this.parent.builder.reportError(new IllegalAnnotationException(Messages.ID_MUST_BE_STRING.format(getName()), this.seed));
            }
            return ID.ID;
        } else if (this.seed.hasAnnotation(XmlIDREF.class)) {
            return ID.IDREF;
        } else {
            return ID.NONE;
        }
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public final MimeType getExpectedMimeType() {
        return this.expectedMimeType;
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public final boolean inlineBinaryData() {
        return this.inlineBinary;
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public final QName getSchemaType() {
        return this.schemaType;
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public final boolean isCollection() {
        return this.isCollection;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void link() {
        if (this.id == ID.IDREF) {
            for (TypeInfo<T, C> ti : ref()) {
                if (!ti.canBeReferencedByIDREF()) {
                    this.parent.builder.reportError(new IllegalAnnotationException(Messages.INVALID_IDREF.format(this.parent.builder.nav.getTypeName(ti.getType())), this));
                }
            }
        }
    }

    @Override // com.sun.xml.bind.v2.model.annotation.Locatable
    public Locatable getUpstream() {
        return this.parent;
    }

    @Override // com.sun.xml.bind.v2.model.annotation.Locatable
    public Location getLocation() {
        return this.seed.getLocation();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final QName calcXmlName(XmlElement e) {
        if (e != null) {
            return calcXmlName(e.namespace(), e.name());
        }
        return calcXmlName("##default", "##default");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final QName calcXmlName(XmlElementWrapper e) {
        if (e != null) {
            return calcXmlName(e.namespace(), e.name());
        }
        return calcXmlName("##default", "##default");
    }

    private QName calcXmlName(String uri, String local) {
        TODO.checkSpec();
        if (local.length() == 0 || local.equals("##default")) {
            local = this.seed.getName();
        }
        if (uri.equals("##default")) {
            XmlSchema xs = (XmlSchema) reader().getPackageAnnotation(XmlSchema.class, this.parent.getClazz(), this);
            if (xs != null) {
                switch (xs.elementFormDefault()) {
                    case QUALIFIED:
                        QName typeName = this.parent.getTypeName();
                        if (typeName != null) {
                            uri = typeName.getNamespaceURI();
                        } else {
                            uri = xs.namespace();
                        }
                        if (uri.length() == 0) {
                            uri = this.parent.builder.defaultNsUri;
                            break;
                        }
                        break;
                    case UNQUALIFIED:
                    case UNSET:
                        uri = "";
                        break;
                }
            } else {
                uri = "";
            }
        }
        return new QName(uri.intern(), local.intern());
    }

    @Override // java.lang.Comparable
    public int compareTo(PropertyInfoImpl that) {
        return getName().compareTo(that.getName());
    }

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationSource
    public final <A extends Annotation> A readAnnotation(Class<A> annotationType) {
        return (A) this.seed.readAnnotation(annotationType);
    }

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationSource
    public final boolean hasAnnotation(Class<? extends Annotation> annotationType) {
        return this.seed.hasAnnotation(annotationType);
    }
}
