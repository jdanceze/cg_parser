package com.sun.xml.bind.v2.runtime.reflect;

import com.sun.istack.Nullable;
import com.sun.xml.bind.Util;
import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.model.core.Adapter;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.bind.v2.runtime.unmarshaller.Receiver;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.awt.Image;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.Source;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/Accessor.class */
public abstract class Accessor<BeanT, ValueT> implements Receiver {
    public final Class<ValueT> valueType;
    private static List<Class> nonAbstractableClasses = Arrays.asList(Object.class, Calendar.class, Duration.class, XMLGregorianCalendar.class, Image.class, DataHandler.class, Source.class, Date.class, File.class, URI.class, URL.class, Class.class, String.class, Source.class);
    private static boolean accessWarned = false;
    private static final Accessor ERROR = new Accessor<Object, Object>(Object.class) { // from class: com.sun.xml.bind.v2.runtime.reflect.Accessor.1
        @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
        public Object get(Object o) {
            return null;
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
        public void set(Object o, Object o1) {
        }
    };
    public static final Accessor<JAXBElement, Object> JAXB_ELEMENT_VALUE = new Accessor<JAXBElement, Object>(Object.class) { // from class: com.sun.xml.bind.v2.runtime.reflect.Accessor.2
        @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
        public Object get(JAXBElement jaxbElement) {
            return jaxbElement.getValue();
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
        public void set(JAXBElement jaxbElement, Object o) {
            jaxbElement.setValue(o);
        }
    };
    private static final Map<Class, Object> uninitializedValues = new HashMap();

    public abstract ValueT get(BeanT beant) throws AccessorException;

    public abstract void set(BeanT beant, ValueT valuet) throws AccessorException;

    public Class<ValueT> getValueType() {
        return this.valueType;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Accessor(Class<ValueT> valueType) {
        this.valueType = valueType;
    }

    public Accessor<BeanT, ValueT> optimize(@Nullable JAXBContextImpl context) {
        return this;
    }

    public Object getUnadapted(BeanT bean) throws AccessorException {
        return get(bean);
    }

    public boolean isAdapted() {
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setUnadapted(BeanT bean, Object value) throws AccessorException {
        set(bean, value);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Receiver
    public void receive(UnmarshallingContext.State state, Object o) throws SAXException {
        try {
            set(state.getTarget(), o);
        } catch (AccessorException e) {
            Loader.handleGenericException(e, true);
        } catch (IllegalAccessError iae) {
            Loader.handleGenericError(iae);
        }
    }

    static {
        uninitializedValues.put(Byte.TYPE, (byte) 0);
        uninitializedValues.put(Boolean.TYPE, false);
        uninitializedValues.put(Character.TYPE, (char) 0);
        uninitializedValues.put(Float.TYPE, Float.valueOf(0.0f));
        uninitializedValues.put(Double.TYPE, Double.valueOf((double) Const.default_value_double));
        uninitializedValues.put(Integer.TYPE, 0);
        uninitializedValues.put(Long.TYPE, 0L);
        uninitializedValues.put(Short.TYPE, (short) 0);
    }

    public boolean isValueTypeAbstractable() {
        return !nonAbstractableClasses.contains(getValueType());
    }

    public boolean isAbstractable(Class clazz) {
        return !nonAbstractableClasses.contains(clazz);
    }

    public final <T> Accessor<BeanT, T> adapt(Class<T> targetType, Class<? extends XmlAdapter<T, ValueT>> adapter) {
        return new AdaptedAccessor(targetType, this, adapter);
    }

    public final <T> Accessor<BeanT, T> adapt(Adapter<Type, Class> adapter) {
        return new AdaptedAccessor((Class) Utils.REFLECTION_NAVIGATOR.erasure(adapter.defaultType), this, adapter.adapterType);
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/Accessor$FieldReflection.class */
    public static class FieldReflection<BeanT, ValueT> extends Accessor<BeanT, ValueT> {
        public final Field f;
        private static final Logger logger = Util.getClassLogger();

        public FieldReflection(Field f) {
            this(f, false);
        }

        public FieldReflection(Field f, boolean supressAccessorWarnings) {
            super(f.getType());
            this.f = f;
            int mod = f.getModifiers();
            if (!Modifier.isPublic(mod) || Modifier.isFinal(mod) || !Modifier.isPublic(f.getDeclaringClass().getModifiers())) {
                try {
                    f.setAccessible(true);
                } catch (SecurityException e) {
                    if (!Accessor.accessWarned && !supressAccessorWarnings) {
                        logger.log(Level.WARNING, Messages.UNABLE_TO_ACCESS_NON_PUBLIC_FIELD.format(f.getDeclaringClass().getName(), f.getName()), (Throwable) e);
                    }
                    boolean unused = Accessor.accessWarned = true;
                }
            }
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
        public ValueT get(BeanT bean) {
            try {
                return (ValueT) this.f.get(bean);
            } catch (IllegalAccessException e) {
                throw new IllegalAccessError(e.getMessage());
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
        public void set(BeanT bean, ValueT value) {
            if (value == null) {
                try {
                    value = Accessor.uninitializedValues.get(this.valueType);
                } catch (IllegalAccessException e) {
                    throw new IllegalAccessError(e.getMessage());
                }
            }
            this.f.set(bean, value);
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
        public Accessor<BeanT, ValueT> optimize(JAXBContextImpl context) {
            return this;
        }
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/Accessor$ReadOnlyFieldReflection.class */
    public static final class ReadOnlyFieldReflection<BeanT, ValueT> extends FieldReflection<BeanT, ValueT> {
        public ReadOnlyFieldReflection(Field f, boolean supressAccessorWarnings) {
            super(f, supressAccessorWarnings);
        }

        public ReadOnlyFieldReflection(Field f) {
            super(f);
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor.FieldReflection, com.sun.xml.bind.v2.runtime.reflect.Accessor
        public void set(BeanT bean, ValueT value) {
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor.FieldReflection, com.sun.xml.bind.v2.runtime.reflect.Accessor
        public Accessor<BeanT, ValueT> optimize(JAXBContextImpl context) {
            return this;
        }
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/Accessor$GetterSetterReflection.class */
    public static class GetterSetterReflection<BeanT, ValueT> extends Accessor<BeanT, ValueT> {
        public final Method getter;
        public final Method setter;
        private static final Logger logger = Util.getClassLogger();

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public GetterSetterReflection(java.lang.reflect.Method r5, java.lang.reflect.Method r6) {
            /*
                r4 = this;
                r0 = r4
                r1 = r5
                if (r1 == 0) goto Lc
                r1 = r5
                java.lang.Class r1 = r1.getReturnType()
                goto L12
            Lc:
                r1 = r6
                java.lang.Class[] r1 = r1.getParameterTypes()
                r2 = 0
                r1 = r1[r2]
            L12:
                r0.<init>(r1)
                r0 = r4
                r1 = r5
                r0.getter = r1
                r0 = r4
                r1 = r6
                r0.setter = r1
                r0 = r5
                if (r0 == 0) goto L28
                r0 = r4
                r1 = r5
                r0.makeAccessible(r1)
            L28:
                r0 = r6
                if (r0 == 0) goto L31
                r0 = r4
                r1 = r6
                r0.makeAccessible(r1)
            L31:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.bind.v2.runtime.reflect.Accessor.GetterSetterReflection.<init>(java.lang.reflect.Method, java.lang.reflect.Method):void");
        }

        private void makeAccessible(Method m) {
            if (!Modifier.isPublic(m.getModifiers()) || !Modifier.isPublic(m.getDeclaringClass().getModifiers())) {
                try {
                    m.setAccessible(true);
                } catch (SecurityException e) {
                    if (!Accessor.accessWarned) {
                        logger.log(Level.WARNING, Messages.UNABLE_TO_ACCESS_NON_PUBLIC_FIELD.format(m.getDeclaringClass().getName(), m.getName()), (Throwable) e);
                    }
                    boolean unused = Accessor.accessWarned = true;
                }
            }
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
        public ValueT get(BeanT bean) throws AccessorException {
            try {
                return (ValueT) this.getter.invoke(bean, new Object[0]);
            } catch (IllegalAccessException e) {
                throw new IllegalAccessError(e.getMessage());
            } catch (InvocationTargetException e2) {
                throw handleInvocationTargetException(e2);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
        public void set(BeanT bean, ValueT value) throws AccessorException {
            if (value == null) {
                try {
                    value = Accessor.uninitializedValues.get(this.valueType);
                } catch (IllegalAccessException e) {
                    throw new IllegalAccessError(e.getMessage());
                } catch (InvocationTargetException e2) {
                    throw handleInvocationTargetException(e2);
                }
            }
            this.setter.invoke(bean, value);
        }

        private AccessorException handleInvocationTargetException(InvocationTargetException e) {
            Throwable t = e.getTargetException();
            if (t instanceof RuntimeException) {
                throw ((RuntimeException) t);
            }
            if (t instanceof Error) {
                throw ((Error) t);
            }
            return new AccessorException(t);
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
        public Accessor<BeanT, ValueT> optimize(JAXBContextImpl context) {
            return this;
        }
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/Accessor$GetterOnlyReflection.class */
    public static class GetterOnlyReflection<BeanT, ValueT> extends GetterSetterReflection<BeanT, ValueT> {
        public GetterOnlyReflection(Method getter) {
            super(getter, null);
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor.GetterSetterReflection, com.sun.xml.bind.v2.runtime.reflect.Accessor
        public void set(BeanT bean, ValueT value) throws AccessorException {
            throw new AccessorException(Messages.NO_SETTER.format(this.getter.toString()));
        }
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/Accessor$SetterOnlyReflection.class */
    public static class SetterOnlyReflection<BeanT, ValueT> extends GetterSetterReflection<BeanT, ValueT> {
        public SetterOnlyReflection(Method setter) {
            super(null, setter);
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor.GetterSetterReflection, com.sun.xml.bind.v2.runtime.reflect.Accessor
        public ValueT get(BeanT bean) throws AccessorException {
            throw new AccessorException(Messages.NO_GETTER.format(this.setter.toString()));
        }
    }

    public static <A, B> Accessor<A, B> getErrorInstance() {
        return ERROR;
    }
}
