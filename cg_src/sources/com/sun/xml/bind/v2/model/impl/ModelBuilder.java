package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.WhiteSpaceProcessor;
import com.sun.xml.bind.util.Which;
import com.sun.xml.bind.v2.model.annotation.AnnotationReader;
import com.sun.xml.bind.v2.model.annotation.ClassLocatable;
import com.sun.xml.bind.v2.model.annotation.Locatable;
import com.sun.xml.bind.v2.model.core.ErrorHandler;
import com.sun.xml.bind.v2.model.core.NonElement;
import com.sun.xml.bind.v2.model.core.PropertyInfo;
import com.sun.xml.bind.v2.model.core.PropertyKind;
import com.sun.xml.bind.v2.model.core.Ref;
import com.sun.xml.bind.v2.model.core.RegistryInfo;
import com.sun.xml.bind.v2.model.core.TypeInfo;
import com.sun.xml.bind.v2.model.core.TypeInfoSet;
import com.sun.xml.bind.v2.model.nav.Navigator;
import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/ModelBuilder.class */
public class ModelBuilder<T, C, F, M> implements ModelBuilderI<T, C, F, M> {
    private static final Logger logger;
    final TypeInfoSetImpl<T, C, F, M> typeInfoSet;
    public final AnnotationReader<T, C, F, M> reader;
    public final Navigator<T, C, F, M> nav;
    public final String defaultNsUri;
    private final Map<C, C> subclassReplacements;
    private ErrorHandler errorHandler;
    private boolean hadError;
    public boolean hasSwaRef;
    private boolean linked;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Map<QName, TypeInfo> typeNames = new HashMap();
    final Map<String, RegistryInfoImpl<T, C, F, M>> registries = new HashMap();
    private final ErrorHandler proxyErrorHandler = new ErrorHandler() { // from class: com.sun.xml.bind.v2.model.impl.ModelBuilder.1
        @Override // com.sun.xml.bind.v2.model.core.ErrorHandler
        public void error(IllegalAnnotationException e) {
            ModelBuilder.this.reportError(e);
        }
    };

    static {
        Messages res;
        $assertionsDisabled = !ModelBuilder.class.desiredAssertionStatus();
        try {
            XmlSchema s = null;
            s.location();
        } catch (NoSuchMethodError e) {
            if (SecureLoader.getClassClassLoader(XmlSchema.class) == null) {
                res = Messages.INCOMPATIBLE_API_VERSION_MUSTANG;
            } else {
                res = Messages.INCOMPATIBLE_API_VERSION;
            }
            throw new LinkageError(res.format(Which.which(XmlSchema.class), Which.which(ModelBuilder.class)));
        } catch (NullPointerException e2) {
        }
        try {
            WhiteSpaceProcessor.isWhiteSpace("xyz");
            logger = Logger.getLogger(ModelBuilder.class.getName());
        } catch (NoSuchMethodError e3) {
            throw new LinkageError(Messages.RUNNING_WITH_1_0_RUNTIME.format(Which.which(WhiteSpaceProcessor.class), Which.which(ModelBuilder.class)));
        }
    }

    public ModelBuilder(AnnotationReader<T, C, F, M> reader, Navigator<T, C, F, M> navigator, Map<C, C> subclassReplacements, String defaultNamespaceRemap) {
        this.reader = reader;
        this.nav = navigator;
        this.subclassReplacements = subclassReplacements;
        this.defaultNsUri = defaultNamespaceRemap == null ? "" : defaultNamespaceRemap;
        reader.setErrorHandler(this.proxyErrorHandler);
        this.typeInfoSet = createTypeInfoSet();
    }

    protected TypeInfoSetImpl<T, C, F, M> createTypeInfoSet() {
        return new TypeInfoSetImpl<>(this.nav, this.reader, BuiltinLeafInfoImpl.createLeaves(this.nav));
    }

    public NonElement<T, C> getClassInfo(C clazz, Locatable upstream) {
        return getClassInfo(clazz, false, upstream);
    }

    public NonElement<T, C> getClassInfo(C clazz, boolean searchForSuperClass, Locatable upstream) {
        NonElement<T, C> r;
        T[] classArrayValue;
        if ($assertionsDisabled || clazz != null) {
            NonElement<T, C> r2 = this.typeInfoSet.getClassInfo(clazz);
            if (r2 != null) {
                return r2;
            }
            if (this.nav.isEnum(clazz)) {
                EnumLeafInfoImpl<T, C, F, M> li = createEnumLeafInfo(clazz, upstream);
                this.typeInfoSet.add(li);
                NonElement<T, C> r3 = li;
                addTypeName(r3);
                r = r3;
            } else {
                boolean isReplaced = this.subclassReplacements.containsKey(clazz);
                if (isReplaced && !searchForSuperClass) {
                    r = getClassInfo(this.subclassReplacements.get(clazz), upstream);
                } else if (this.reader.hasClassAnnotation(clazz, XmlTransient.class) || isReplaced) {
                    r = getClassInfo(this.nav.getSuperClass(clazz), searchForSuperClass, new ClassLocatable(upstream, clazz, this.nav));
                } else {
                    ClassInfoImpl<T, C, F, M> ci = createClassInfo(clazz, upstream);
                    this.typeInfoSet.add(ci);
                    for (PropertyInfo<T, C> p : ci.getProperties()) {
                        if (p.kind() == PropertyKind.REFERENCE) {
                            addToRegistry(clazz, (Locatable) p);
                            Class[] prmzdClasses = getParametrizedTypes(p);
                            if (prmzdClasses != null) {
                                for (Class prmzdClass : prmzdClasses) {
                                    if (prmzdClass != clazz) {
                                        addToRegistry(prmzdClass, (Locatable) p);
                                    }
                                }
                            }
                        }
                        for (TypeInfo<T, C> typeInfo : p.ref()) {
                        }
                    }
                    ci.getBaseClass();
                    NonElement<T, C> r4 = ci;
                    addTypeName(r4);
                    r = r4;
                }
            }
            XmlSeeAlso sa = (XmlSeeAlso) this.reader.getClassAnnotation(XmlSeeAlso.class, clazz, upstream);
            if (sa != null) {
                for (T t : this.reader.getClassArrayValue(sa, "value")) {
                    getTypeInfo(t, (Locatable) sa);
                }
            }
            return r;
        }
        throw new AssertionError();
    }

    private void addToRegistry(C clazz, Locatable p) {
        C c;
        String pkg = this.nav.getPackageName(clazz);
        if (!this.registries.containsKey(pkg) && (c = this.nav.loadObjectFactory(clazz, pkg)) != null) {
            addRegistry(c, p);
        }
    }

    private Class[] getParametrizedTypes(PropertyInfo p) {
        try {
            Type pType = ((RuntimePropertyInfo) p).getIndividualType();
            if (pType instanceof ParameterizedType) {
                ParameterizedType prmzdType = (ParameterizedType) pType;
                if (prmzdType.getRawType() == JAXBElement.class) {
                    Type[] actualTypes = prmzdType.getActualTypeArguments();
                    Class[] result = new Class[actualTypes.length];
                    for (int i = 0; i < actualTypes.length; i++) {
                        result[i] = (Class) actualTypes[i];
                    }
                    return result;
                }
                return null;
            }
            return null;
        } catch (Exception e) {
            logger.log(Level.FINE, "Error in ModelBuilder.getParametrizedTypes. " + e.getMessage());
            return null;
        }
    }

    private void addTypeName(NonElement<T, C> r) {
        TypeInfo old;
        QName t = r.getTypeName();
        if (t != null && (old = this.typeNames.put(t, r)) != null) {
            reportError(new IllegalAnnotationException(Messages.CONFLICTING_XML_TYPE_MAPPING.format(r.getTypeName()), old, r));
        }
    }

    public NonElement<T, C> getTypeInfo(T t, Locatable upstream) {
        NonElement<T, C> r = this.typeInfoSet.getTypeInfo((TypeInfoSetImpl<T, C, F, M>) t);
        if (r != null) {
            return r;
        }
        if (this.nav.isArray(t)) {
            ArrayInfoImpl<T, C, F, M> ai = createArrayInfo(upstream, t);
            addTypeName(ai);
            this.typeInfoSet.add(ai);
            return ai;
        }
        C c = this.nav.asDecl((Navigator<T, C, F, M>) t);
        if ($assertionsDisabled || c != null) {
            return getClassInfo(c, upstream);
        }
        throw new AssertionError(t.toString() + " must be a leaf, but we failed to recognize it.");
    }

    public NonElement<T, C> getTypeInfo(Ref<T, C> ref) {
        if ($assertionsDisabled || !ref.valueList) {
            C c = this.nav.asDecl((Navigator<T, C, F, M>) ref.type);
            if (c != null && this.reader.getClassAnnotation(XmlRegistry.class, c, null) != null) {
                if (!this.registries.containsKey(this.nav.getPackageName(c))) {
                    addRegistry(c, null);
                    return null;
                }
                return null;
            }
            return getTypeInfo(ref.type, null);
        }
        throw new AssertionError();
    }

    protected EnumLeafInfoImpl<T, C, F, M> createEnumLeafInfo(C clazz, Locatable upstream) {
        return new EnumLeafInfoImpl<>(this, upstream, clazz, this.nav.use(clazz));
    }

    protected ClassInfoImpl<T, C, F, M> createClassInfo(C clazz, Locatable upstream) {
        return new ClassInfoImpl<>(this, upstream, clazz);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ElementInfoImpl<T, C, F, M> createElementInfo(RegistryInfoImpl<T, C, F, M> registryInfo, M m) throws IllegalAnnotationException {
        return new ElementInfoImpl<>(this, registryInfo, m);
    }

    protected ArrayInfoImpl<T, C, F, M> createArrayInfo(Locatable upstream, T arrayType) {
        return new ArrayInfoImpl<>(this, upstream, arrayType);
    }

    public RegistryInfo<T, C> addRegistry(C registryClass, Locatable upstream) {
        return new RegistryInfoImpl(this, upstream, registryClass);
    }

    public RegistryInfo<T, C> getRegistry(String packageName) {
        return this.registries.get(packageName);
    }

    public TypeInfoSet<T, C, F, M> link() {
        if ($assertionsDisabled || !this.linked) {
            this.linked = true;
            for (ElementInfoImpl ei : this.typeInfoSet.getAllElements()) {
                ei.link();
            }
            for (ClassInfoImpl ci : this.typeInfoSet.beans().values()) {
                ci.link();
            }
            for (EnumLeafInfoImpl li : this.typeInfoSet.enums().values()) {
                li.link();
            }
            if (this.hadError) {
                return null;
            }
            return this.typeInfoSet;
        }
        throw new AssertionError();
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public final void reportError(IllegalAnnotationException e) {
        this.hadError = true;
        if (this.errorHandler != null) {
            this.errorHandler.error(e);
        }
    }

    public boolean isReplaced(C sc) {
        return this.subclassReplacements.containsKey(sc);
    }

    @Override // com.sun.xml.bind.v2.model.impl.ModelBuilderI
    public Navigator<T, C, F, M> getNavigator() {
        return this.nav;
    }

    @Override // com.sun.xml.bind.v2.model.impl.ModelBuilderI
    public AnnotationReader<T, C, F, M> getReader() {
        return this.reader;
    }
}
