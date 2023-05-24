package com.sun.xml.bind.v2.model.impl;

import com.sun.istack.Nullable;
import com.sun.xml.bind.WhiteSpaceProcessor;
import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.model.annotation.Locatable;
import com.sun.xml.bind.v2.model.annotation.RuntimeAnnotationReader;
import com.sun.xml.bind.v2.model.core.ID;
import com.sun.xml.bind.v2.model.core.PropertyInfo;
import com.sun.xml.bind.v2.model.core.TypeInfoSet;
import com.sun.xml.bind.v2.model.runtime.RuntimeNonElement;
import com.sun.xml.bind.v2.model.runtime.RuntimeNonElementRef;
import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfoSet;
import com.sun.xml.bind.v2.runtime.FilterTransducer;
import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
import com.sun.xml.bind.v2.runtime.InlineBinaryTransducer;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.bind.v2.runtime.MimeTypedTransducer;
import com.sun.xml.bind.v2.runtime.SchemaTypeTransducer;
import com.sun.xml.bind.v2.runtime.Transducer;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import javax.activation.MimeType;
import javax.xml.namespace.QName;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/RuntimeModelBuilder.class */
public class RuntimeModelBuilder extends ModelBuilder<Type, Class, Field, Method> {
    @Nullable
    public final JAXBContextImpl context;

    public RuntimeModelBuilder(JAXBContextImpl context, RuntimeAnnotationReader annotationReader, Map<Class, Class> subclassReplacements, String defaultNamespaceRemap) {
        super(annotationReader, Utils.REFLECTION_NAVIGATOR, subclassReplacements, defaultNamespaceRemap);
        this.context = context;
    }

    @Override // com.sun.xml.bind.v2.model.impl.ModelBuilder
    public RuntimeNonElement getClassInfo(Class clazz, Locatable upstream) {
        return (RuntimeNonElement) super.getClassInfo((RuntimeModelBuilder) clazz, upstream);
    }

    @Override // com.sun.xml.bind.v2.model.impl.ModelBuilder
    public RuntimeNonElement getClassInfo(Class clazz, boolean searchForSuperClass, Locatable upstream) {
        return (RuntimeNonElement) super.getClassInfo((RuntimeModelBuilder) clazz, searchForSuperClass, upstream);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.bind.v2.model.impl.ModelBuilder
    public RuntimeEnumLeafInfoImpl createEnumLeafInfo(Class clazz, Locatable upstream) {
        return new RuntimeEnumLeafInfoImpl(this, upstream, clazz);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.bind.v2.model.impl.ModelBuilder
    public RuntimeClassInfoImpl createClassInfo(Class clazz, Locatable upstream) {
        return new RuntimeClassInfoImpl(this, upstream, clazz);
    }

    @Override // com.sun.xml.bind.v2.model.impl.ModelBuilder
    public RuntimeElementInfoImpl createElementInfo(RegistryInfoImpl<Type, Class, Field, Method> registryInfo, Method method) throws IllegalAnnotationException {
        return new RuntimeElementInfoImpl(this, registryInfo, method);
    }

    @Override // com.sun.xml.bind.v2.model.impl.ModelBuilder
    public RuntimeArrayInfoImpl createArrayInfo(Locatable upstream, Type arrayType) {
        return new RuntimeArrayInfoImpl(this, upstream, (Class) arrayType);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.bind.v2.model.impl.ModelBuilder
    /* renamed from: createTypeInfoSet */
    public TypeInfoSetImpl<Type, Class, Field, Method> createTypeInfoSet2() {
        return new RuntimeTypeInfoSetImpl(this.reader);
    }

    @Override // com.sun.xml.bind.v2.model.impl.ModelBuilder
    /* renamed from: link */
    public TypeInfoSet<Type, Class, Field, Method> link2() {
        return (RuntimeTypeInfoSet) super.link();
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.sun.xml.bind.v2.model.runtime.RuntimeNonElement] */
    public static Transducer createTransducer(RuntimeNonElementRef ref) {
        Transducer t = ref.getTarget().getTransducer();
        PropertyInfo<Type, Class> source = ref.getSource();
        ID id = source.id();
        if (id == ID.IDREF) {
            return RuntimeBuiltinLeafInfoImpl.STRING;
        }
        Transducer t2 = t;
        if (id == ID.ID) {
            t2 = new IDTransducerImpl(t);
        }
        MimeType emt = source.getExpectedMimeType();
        Transducer t3 = t2;
        if (emt != null) {
            t3 = new MimeTypedTransducer(t2, emt);
        }
        Transducer t4 = t3;
        if (source.inlineBinaryData()) {
            t4 = new InlineBinaryTransducer(t3);
        }
        Transducer t5 = t4;
        if (source.getSchemaType() != null) {
            if (source.getSchemaType().equals(createXSSimpleType())) {
                return RuntimeBuiltinLeafInfoImpl.STRING;
            }
            t5 = new SchemaTypeTransducer(t4, source.getSchemaType());
        }
        return t5;
    }

    private static QName createXSSimpleType() {
        return new QName("http://www.w3.org/2001/XMLSchema", "anySimpleType");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/RuntimeModelBuilder$IDTransducerImpl.class */
    public static final class IDTransducerImpl<ValueT> extends FilterTransducer<ValueT> {
        public IDTransducerImpl(Transducer<ValueT> core) {
            super(core);
        }

        @Override // com.sun.xml.bind.v2.runtime.FilterTransducer, com.sun.xml.bind.v2.runtime.Transducer
        public ValueT parse(CharSequence lexical) throws AccessorException, SAXException {
            String value = WhiteSpaceProcessor.trim(lexical).toString();
            UnmarshallingContext.getInstance().addToIdTable(value);
            return (ValueT) this.core.parse(value);
        }
    }
}
