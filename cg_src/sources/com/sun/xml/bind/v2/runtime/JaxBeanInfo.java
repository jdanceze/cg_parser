package com.sun.xml.bind.v2.runtime;

import com.sun.istack.NotNull;
import com.sun.xml.bind.Util;
import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/JaxBeanInfo.class */
public abstract class JaxBeanInfo<BeanT> {
    protected boolean isNilIncluded;
    protected short flag;
    private static final short FLAG_IS_ELEMENT = 1;
    private static final short FLAG_IS_IMMUTABLE = 2;
    private static final short FLAG_HAS_ELEMENT_ONLY_CONTENTMODEL = 4;
    private static final short FLAG_HAS_BEFORE_UNMARSHAL_METHOD = 8;
    private static final short FLAG_HAS_AFTER_UNMARSHAL_METHOD = 16;
    private static final short FLAG_HAS_BEFORE_MARSHAL_METHOD = 32;
    private static final short FLAG_HAS_AFTER_MARSHAL_METHOD = 64;
    private static final short FLAG_HAS_LIFECYCLE_EVENTS = 128;
    private LifecycleMethods lcm;
    public final Class<BeanT> jaxbType;
    private final Object typeName;
    private static final Class[] unmarshalEventParams = {Unmarshaller.class, Object.class};
    private static Class[] marshalEventParams = {Marshaller.class};
    private static final Logger logger = Util.getClassLogger();

    public abstract String getElementNamespaceURI(BeanT beant);

    public abstract String getElementLocalName(BeanT beant);

    public abstract BeanT createInstance(UnmarshallingContext unmarshallingContext) throws IllegalAccessException, InvocationTargetException, InstantiationException, SAXException;

    public abstract boolean reset(BeanT beant, UnmarshallingContext unmarshallingContext) throws SAXException;

    public abstract String getId(BeanT beant, XMLSerializer xMLSerializer) throws SAXException;

    public abstract void serializeBody(BeanT beant, XMLSerializer xMLSerializer) throws SAXException, IOException, XMLStreamException;

    public abstract void serializeAttributes(BeanT beant, XMLSerializer xMLSerializer) throws SAXException, IOException, XMLStreamException;

    public abstract void serializeRoot(BeanT beant, XMLSerializer xMLSerializer) throws SAXException, IOException, XMLStreamException;

    public abstract void serializeURIs(BeanT beant, XMLSerializer xMLSerializer) throws SAXException;

    public abstract Loader getLoader(JAXBContextImpl jAXBContextImpl, boolean z);

    public abstract Transducer<BeanT> getTransducer();

    /* JADX INFO: Access modifiers changed from: protected */
    public JaxBeanInfo(JAXBContextImpl grammar, RuntimeTypeInfo rti, Class<BeanT> jaxbType, QName[] typeNames, boolean isElement, boolean isImmutable, boolean hasLifecycleEvents) {
        this(grammar, rti, (Class) jaxbType, (Object) typeNames, isElement, isImmutable, hasLifecycleEvents);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JaxBeanInfo(JAXBContextImpl grammar, RuntimeTypeInfo rti, Class<BeanT> jaxbType, QName typeName, boolean isElement, boolean isImmutable, boolean hasLifecycleEvents) {
        this(grammar, rti, (Class) jaxbType, (Object) typeName, isElement, isImmutable, hasLifecycleEvents);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JaxBeanInfo(JAXBContextImpl grammar, RuntimeTypeInfo rti, Class<BeanT> jaxbType, boolean isElement, boolean isImmutable, boolean hasLifecycleEvents) {
        this(grammar, rti, jaxbType, (Object) null, isElement, isImmutable, hasLifecycleEvents);
    }

    private JaxBeanInfo(JAXBContextImpl grammar, RuntimeTypeInfo rti, Class<BeanT> jaxbType, Object typeName, boolean isElement, boolean isImmutable, boolean hasLifecycleEvents) {
        this.isNilIncluded = false;
        this.lcm = null;
        grammar.beanInfos.put(rti, this);
        this.jaxbType = jaxbType;
        this.typeName = typeName;
        this.flag = (short) ((isElement ? 1 : 0) | (isImmutable ? 2 : 0) | (hasLifecycleEvents ? 128 : 0));
    }

    public final boolean hasBeforeUnmarshalMethod() {
        return (this.flag & 8) != 0;
    }

    public final boolean hasAfterUnmarshalMethod() {
        return (this.flag & 16) != 0;
    }

    public final boolean hasBeforeMarshalMethod() {
        return (this.flag & 32) != 0;
    }

    public final boolean hasAfterMarshalMethod() {
        return (this.flag & 64) != 0;
    }

    public final boolean isElement() {
        return (this.flag & 1) != 0;
    }

    public final boolean isImmutable() {
        return (this.flag & 2) != 0;
    }

    public final boolean hasElementOnlyContentModel() {
        return (this.flag & 4) != 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void hasElementOnlyContentModel(boolean value) {
        if (value) {
            this.flag = (short) (this.flag | 4);
        } else {
            this.flag = (short) (this.flag & (-5));
        }
    }

    public boolean isNilIncluded() {
        return this.isNilIncluded;
    }

    public boolean lookForLifecycleMethods() {
        return (this.flag & 128) != 0;
    }

    public Collection<QName> getTypeNames() {
        return this.typeName == null ? Collections.emptyList() : this.typeName instanceof QName ? Collections.singletonList((QName) this.typeName) : Arrays.asList((QName[]) this.typeName);
    }

    public QName getTypeName(@NotNull BeanT instance) {
        if (this.typeName == null) {
            return null;
        }
        return this.typeName instanceof QName ? (QName) this.typeName : ((QName[]) this.typeName)[0];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void link(JAXBContextImpl grammar) {
    }

    public void wrapUp() {
    }

    private Method[] getDeclaredMethods(final Class<BeanT> c) {
        return (Method[]) AccessController.doPrivileged(new PrivilegedAction<Method[]>() { // from class: com.sun.xml.bind.v2.runtime.JaxBeanInfo.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Method[] run() {
                return c.getDeclaredMethods();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void setLifecycleFlags() {
        Method[] declaredMethods;
        try {
            if (this.lcm == null) {
                this.lcm = new LifecycleMethods();
            }
            for (Class<? super BeanT> jt = this.jaxbType; jt != null; jt = jt.getSuperclass()) {
                for (Method m : getDeclaredMethods(jt)) {
                    String name = m.getName();
                    if (this.lcm.beforeUnmarshal == null && name.equals("beforeUnmarshal") && match(m, unmarshalEventParams)) {
                        cacheLifecycleMethod(m, (short) 8);
                    }
                    if (this.lcm.afterUnmarshal == null && name.equals("afterUnmarshal") && match(m, unmarshalEventParams)) {
                        cacheLifecycleMethod(m, (short) 16);
                    }
                    if (this.lcm.beforeMarshal == null && name.equals("beforeMarshal") && match(m, marshalEventParams)) {
                        cacheLifecycleMethod(m, (short) 32);
                    }
                    if (this.lcm.afterMarshal == null && name.equals("afterMarshal") && match(m, marshalEventParams)) {
                        cacheLifecycleMethod(m, (short) 64);
                    }
                }
            }
        } catch (SecurityException e) {
            logger.log(Level.WARNING, Messages.UNABLE_TO_DISCOVER_EVENTHANDLER.format(this.jaxbType.getName(), e), (Throwable) e);
        }
    }

    private boolean match(Method m, Class[] params) {
        return Arrays.equals(m.getParameterTypes(), params);
    }

    private void cacheLifecycleMethod(Method m, short lifecycleFlag) {
        if (this.lcm == null) {
            this.lcm = new LifecycleMethods();
        }
        m.setAccessible(true);
        this.flag = (short) (this.flag | lifecycleFlag);
        switch (lifecycleFlag) {
            case 8:
                this.lcm.beforeUnmarshal = m;
                return;
            case 16:
                this.lcm.afterUnmarshal = m;
                return;
            case 32:
                this.lcm.beforeMarshal = m;
                return;
            case 64:
                this.lcm.afterMarshal = m;
                return;
            default:
                return;
        }
    }

    public final LifecycleMethods getLifecycleMethods() {
        return this.lcm;
    }

    public final void invokeBeforeUnmarshalMethod(UnmarshallerImpl unm, Object child, Object parent) throws SAXException {
        Method m = getLifecycleMethods().beforeUnmarshal;
        invokeUnmarshallCallback(m, child, unm, parent);
    }

    public final void invokeAfterUnmarshalMethod(UnmarshallerImpl unm, Object child, Object parent) throws SAXException {
        Method m = getLifecycleMethods().afterUnmarshal;
        invokeUnmarshallCallback(m, child, unm, parent);
    }

    private void invokeUnmarshallCallback(Method m, Object child, UnmarshallerImpl unm, Object parent) throws SAXException {
        try {
            m.invoke(child, unm, parent);
        } catch (IllegalAccessException e) {
            UnmarshallingContext.getInstance().handleError(e, false);
        } catch (InvocationTargetException e2) {
            UnmarshallingContext.getInstance().handleError(e2, false);
        }
    }
}
