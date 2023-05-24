package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.model.annotation.FieldLocatable;
import com.sun.xml.bind.v2.model.annotation.Locatable;
import com.sun.xml.bind.v2.model.runtime.RuntimeEnumLeafInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeNonElement;
import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
import com.sun.xml.bind.v2.runtime.Name;
import com.sun.xml.bind.v2.runtime.Transducer;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import java.io.IOException;
import java.lang.Enum;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/RuntimeEnumLeafInfoImpl.class */
public final class RuntimeEnumLeafInfoImpl<T extends Enum<T>, B> extends EnumLeafInfoImpl<Type, Class, Field, Method> implements RuntimeEnumLeafInfo, Transducer<T> {
    private final Transducer<B> baseXducer;
    private final Map<B, T> parseMap;
    private final Map<T, B> printMap;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.runtime.Transducer
    public /* bridge */ /* synthetic */ QName getTypeName(Object obj) {
        return getTypeName((RuntimeEnumLeafInfoImpl<T, B>) ((Enum) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.runtime.Transducer
    public /* bridge */ /* synthetic */ void writeLeafElement(XMLSerializer xMLSerializer, Name name, Object obj, String str) throws IOException, SAXException, XMLStreamException, AccessorException {
        writeLeafElement(xMLSerializer, name, (Name) ((Enum) obj), str);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.runtime.Transducer
    public /* bridge */ /* synthetic */ void writeText(XMLSerializer xMLSerializer, Object obj, String str) throws IOException, SAXException, XMLStreamException, AccessorException {
        writeText(xMLSerializer, (XMLSerializer) ((Enum) obj), str);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.runtime.Transducer
    public /* bridge */ /* synthetic */ CharSequence print(Object obj) throws AccessorException {
        return print((RuntimeEnumLeafInfoImpl<T, B>) ((Enum) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.runtime.Transducer
    public /* bridge */ /* synthetic */ void declareNamespace(Object obj, XMLSerializer xMLSerializer) throws AccessorException {
        declareNamespace((RuntimeEnumLeafInfoImpl<T, B>) ((Enum) obj), xMLSerializer);
    }

    @Override // com.sun.xml.bind.v2.model.runtime.RuntimeLeafInfo, com.sun.xml.bind.v2.model.runtime.RuntimeNonElement
    public Transducer<T> getTransducer() {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RuntimeEnumLeafInfoImpl(RuntimeModelBuilder builder, Locatable upstream, Class<T> enumType) {
        super(builder, upstream, enumType, enumType);
        this.parseMap = new HashMap();
        this.printMap = new EnumMap(enumType);
        this.baseXducer = ((RuntimeNonElement) this.baseType).getTransducer();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.model.impl.EnumLeafInfoImpl
    public RuntimeEnumConstantImpl createEnumConstant(String name, String literal, Field constant, EnumConstantImpl<Type, Class, Field, Method> last) {
        try {
            try {
                constant.setAccessible(true);
            } catch (SecurityException e) {
            }
            Enum r0 = (Enum) constant.get(null);
            B b = null;
            try {
                b = this.baseXducer.parse(literal);
            } catch (Exception e2) {
                this.builder.reportError(new IllegalAnnotationException(Messages.INVALID_XML_ENUM_VALUE.format(literal, ((Type) this.baseType.getType()).toString()), e2, new FieldLocatable(this, constant, nav())));
            }
            this.parseMap.put(b, r0);
            this.printMap.put(r0, b);
            return new RuntimeEnumConstantImpl(this, name, literal, last);
        } catch (IllegalAccessException e3) {
            throw new IllegalAccessError(e3.getMessage());
        }
    }

    @Override // com.sun.xml.bind.v2.model.runtime.RuntimeLeafInfo
    public QName[] getTypeNames() {
        return new QName[]{getTypeName()};
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.bind.v2.model.impl.EnumLeafInfoImpl, com.sun.xml.bind.v2.model.core.EnumLeafInfo
    public Class getClazz() {
        return (Class) this.clazz;
    }

    @Override // com.sun.xml.bind.v2.runtime.Transducer
    public boolean useNamespace() {
        return this.baseXducer.useNamespace();
    }

    public void declareNamespace(T t, XMLSerializer w) throws AccessorException {
        this.baseXducer.declareNamespace(this.printMap.get(t), w);
    }

    public CharSequence print(T t) throws AccessorException {
        return this.baseXducer.print(this.printMap.get(t));
    }

    @Override // com.sun.xml.bind.v2.runtime.Transducer
    public T parse(CharSequence lexical) throws AccessorException, SAXException {
        String b = this.baseXducer.parse(lexical);
        if (this.tokenStringType) {
            b = ((String) b).trim();
        }
        return this.parseMap.get(b);
    }

    public void writeText(XMLSerializer w, T t, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
        this.baseXducer.writeText(w, this.printMap.get(t), fieldName);
    }

    public void writeLeafElement(XMLSerializer w, Name tagName, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
        this.baseXducer.writeLeafElement(w, tagName, this.printMap.get(o), fieldName);
    }

    public QName getTypeName(T instance) {
        return null;
    }
}
