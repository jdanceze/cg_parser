package com.sun.xml.bind.v2.runtime.reflect;

import com.sun.istack.SAXException2;
import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.ClassFactory;
import com.sun.xml.bind.v2.TODO;
import com.sun.xml.bind.v2.model.core.Adapter;
import com.sun.xml.bind.v2.model.core.ID;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import com.sun.xml.bind.v2.runtime.unmarshaller.LocatorEx;
import com.sun.xml.bind.v2.runtime.unmarshaller.Patcher;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/Lister.class */
public abstract class Lister<BeanT, PropT, ItemT, PackT> {
    private static final Map<Class, WeakReference<Lister>> arrayListerCache;
    static final Map<Class, Lister> primitiveArrayListers;
    public static final Lister ERROR;
    private static final ListIterator EMPTY_ITERATOR;
    private static final Class[] COLLECTION_IMPL_CLASSES;
    static final /* synthetic */ boolean $assertionsDisabled;

    public abstract ListIterator<ItemT> iterator(PropT propt, XMLSerializer xMLSerializer);

    public abstract PackT startPacking(BeanT beant, Accessor<BeanT, PropT> accessor) throws AccessorException;

    public abstract void addToPack(PackT packt, ItemT itemt) throws AccessorException;

    public abstract void endPacking(PackT packt, BeanT beant, Accessor<BeanT, PropT> accessor) throws AccessorException;

    public abstract void reset(BeanT beant, Accessor<BeanT, PropT> accessor) throws AccessorException;

    static {
        $assertionsDisabled = !Lister.class.desiredAssertionStatus();
        arrayListerCache = Collections.synchronizedMap(new WeakHashMap());
        primitiveArrayListers = new HashMap();
        PrimitiveArrayListerBoolean.register();
        PrimitiveArrayListerByte.register();
        PrimitiveArrayListerCharacter.register();
        PrimitiveArrayListerDouble.register();
        PrimitiveArrayListerFloat.register();
        PrimitiveArrayListerInteger.register();
        PrimitiveArrayListerLong.register();
        PrimitiveArrayListerShort.register();
        ERROR = new Lister() { // from class: com.sun.xml.bind.v2.runtime.reflect.Lister.1
            @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
            public ListIterator iterator(Object o, XMLSerializer context) {
                return Lister.EMPTY_ITERATOR;
            }

            @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
            public Object startPacking(Object o, Accessor accessor) {
                return null;
            }

            @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
            public void addToPack(Object o, Object o1) {
            }

            @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
            public void endPacking(Object o, Object o1, Accessor accessor) {
            }

            @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
            public void reset(Object o, Accessor accessor) {
            }
        };
        EMPTY_ITERATOR = new ListIterator() { // from class: com.sun.xml.bind.v2.runtime.reflect.Lister.2
            @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
            public boolean hasNext() {
                return false;
            }

            @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
            public Object next() {
                throw new IllegalStateException();
            }
        };
        COLLECTION_IMPL_CLASSES = new Class[]{ArrayList.class, LinkedList.class, HashSet.class, TreeSet.class, Stack.class};
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <BeanT, PropT, ItemT, PackT> Lister<BeanT, PropT, ItemT, PackT> create(Type fieldType, ID idness, Adapter<Type, Class> adapter) {
        Class itemType;
        Lister l;
        Class rawType = (Class) Utils.REFLECTION_NAVIGATOR.erasure(fieldType);
        if (rawType.isArray()) {
            itemType = rawType.getComponentType();
            l = getArrayLister(itemType);
        } else if (Collection.class.isAssignableFrom(rawType)) {
            Type bt = Utils.REFLECTION_NAVIGATOR.getBaseClass(fieldType, Collection.class);
            if (bt instanceof ParameterizedType) {
                itemType = (Class) Utils.REFLECTION_NAVIGATOR.erasure(((ParameterizedType) bt).getActualTypeArguments()[0]);
            } else {
                itemType = Object.class;
            }
            l = new CollectionLister(getImplClass(rawType));
        } else {
            return null;
        }
        if (idness == ID.IDREF) {
            l = new IDREFS(l, itemType);
        }
        if (adapter != null) {
            l = new AdaptedLister(l, adapter.adapterType);
        }
        return l;
    }

    private static Class getImplClass(Class<?> fieldType) {
        return ClassFactory.inferImplClass(fieldType, COLLECTION_IMPL_CLASSES);
    }

    private static Lister getArrayLister(Class componentType) {
        Lister l = null;
        if (componentType.isPrimitive()) {
            l = primitiveArrayListers.get(componentType);
        } else {
            WeakReference<Lister> wr = arrayListerCache.get(componentType);
            if (wr != null) {
                l = wr.get();
            }
            if (l == null) {
                l = new ArrayLister(componentType);
                arrayListerCache.put(componentType, new WeakReference<>(l));
            }
        }
        if ($assertionsDisabled || l != null) {
            return l;
        }
        throw new AssertionError();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/Lister$ArrayLister.class */
    public static final class ArrayLister<BeanT, ItemT> extends Lister<BeanT, ItemT[], ItemT, Pack<ItemT>> {
        private final Class<ItemT> itemType;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public /* bridge */ /* synthetic */ void endPacking(Object obj, Object obj2, Accessor accessor) throws AccessorException {
            endPacking((Pack) ((Pack) obj), (Pack<ItemT>) obj2, (Accessor<Pack<ItemT>, ItemT[]>) accessor);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public /* bridge */ /* synthetic */ void addToPack(Object obj, Object obj2) throws AccessorException {
            addToPack((Pack<Pack<ItemT>>) obj, (Pack<ItemT>) obj2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public /* bridge */ /* synthetic */ Object startPacking(Object obj, Accessor accessor) throws AccessorException {
            return startPacking((ArrayLister<BeanT, ItemT>) obj, (Accessor<ArrayLister<BeanT, ItemT>, ItemT[]>) accessor);
        }

        public ArrayLister(Class<ItemT> itemType) {
            this.itemType = itemType;
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public ListIterator<ItemT> iterator(final ItemT[] objects, XMLSerializer context) {
            return new ListIterator<ItemT>() { // from class: com.sun.xml.bind.v2.runtime.reflect.Lister.ArrayLister.1
                int idx = 0;

                @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
                public boolean hasNext() {
                    return this.idx < objects.length;
                }

                @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
                public ItemT next() {
                    Object[] objArr = objects;
                    int i = this.idx;
                    this.idx = i + 1;
                    return (ItemT) objArr[i];
                }
            };
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public Pack startPacking(BeanT current, Accessor<BeanT, ItemT[]> acc) {
            return new Pack(this.itemType);
        }

        public void addToPack(Pack<ItemT> objects, ItemT o) {
            objects.add(o);
        }

        public void endPacking(Pack<ItemT> pack, BeanT bean, Accessor<BeanT, ItemT[]> acc) throws AccessorException {
            acc.set(bean, pack.build());
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public void reset(BeanT o, Accessor<BeanT, ItemT[]> acc) throws AccessorException {
            acc.set(o, (Object[]) Array.newInstance((Class<?>) this.itemType, 0));
        }
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/Lister$Pack.class */
    public static final class Pack<ItemT> extends ArrayList<ItemT> {
        private final Class<ItemT> itemType;

        public Pack(Class<ItemT> itemType) {
            this.itemType = itemType;
        }

        public ItemT[] build() {
            return (ItemT[]) super.toArray((Object[]) Array.newInstance((Class<?>) this.itemType, size()));
        }
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/Lister$CollectionLister.class */
    public static final class CollectionLister<BeanT, T extends Collection> extends Lister<BeanT, T, Object, T> {
        private final Class<? extends T> implClass;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public /* bridge */ /* synthetic */ void endPacking(Object obj, Object obj2, Accessor accessor) throws AccessorException {
            endPacking((CollectionLister<BeanT, T>) ((Collection) obj), (Collection) obj2, (Accessor<Collection, CollectionLister<BeanT, T>>) accessor);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public /* bridge */ /* synthetic */ void addToPack(Object obj, Object obj2) throws AccessorException {
            addToPack((CollectionLister<BeanT, T>) ((Collection) obj), obj2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public /* bridge */ /* synthetic */ Object startPacking(Object obj, Accessor accessor) throws AccessorException {
            return startPacking((CollectionLister<BeanT, T>) obj, (Accessor<CollectionLister<BeanT, T>, T>) accessor);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public /* bridge */ /* synthetic */ ListIterator<Object> iterator(Object obj, XMLSerializer xMLSerializer) {
            return iterator((CollectionLister<BeanT, T>) ((Collection) obj), xMLSerializer);
        }

        public CollectionLister(Class<? extends T> implClass) {
            this.implClass = implClass;
        }

        public ListIterator iterator(T collection, XMLSerializer context) {
            final Iterator itr = collection.iterator();
            return new ListIterator() { // from class: com.sun.xml.bind.v2.runtime.reflect.Lister.CollectionLister.1
                @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
                public boolean hasNext() {
                    return itr.hasNext();
                }

                @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
                public Object next() {
                    return itr.next();
                }
            };
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v9, types: [java.util.Collection] */
        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public T startPacking(BeanT bean, Accessor<BeanT, T> acc) throws AccessorException {
            T collection = acc.get(bean);
            if (collection == null) {
                collection = (Collection) ClassFactory.create(this.implClass);
                if (!acc.isAdapted()) {
                    acc.set(bean, collection);
                }
            }
            collection.clear();
            return collection;
        }

        public void addToPack(T collection, Object o) {
            collection.add(o);
        }

        public void endPacking(T collection, BeanT bean, Accessor<BeanT, T> acc) throws AccessorException {
            try {
                if (acc.isAdapted()) {
                    acc.set(bean, collection);
                }
            } catch (AccessorException ae) {
                if (acc.isAdapted()) {
                    throw ae;
                }
            }
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public void reset(BeanT bean, Accessor<BeanT, T> acc) throws AccessorException {
            T collection = acc.get(bean);
            if (collection == null) {
                return;
            }
            collection.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/Lister$IDREFS.class */
    public static final class IDREFS<BeanT, PropT> extends Lister<BeanT, PropT, String, IDREFS<BeanT, PropT>.Pack> {
        private final Lister<BeanT, PropT, Object, Object> core;
        private final Class itemType;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public /* bridge */ /* synthetic */ void endPacking(Object obj, Object obj2, Accessor accessor) throws AccessorException {
            endPacking((IDREFS<IDREFS<BeanT, PropT>.Pack, PropT>.Pack) obj, (IDREFS<BeanT, PropT>.Pack) obj2, (Accessor<IDREFS<BeanT, PropT>.Pack, PropT>) accessor);
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public /* bridge */ /* synthetic */ void addToPack(Object obj, String str) throws AccessorException {
            addToPack((Pack) ((Pack) obj), str);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public /* bridge */ /* synthetic */ Object startPacking(Object obj, Accessor accessor) throws AccessorException {
            return startPacking((IDREFS<BeanT, PropT>) obj, (Accessor<IDREFS<BeanT, PropT>, PropT>) accessor);
        }

        public IDREFS(Lister core, Class itemType) {
            this.core = core;
            this.itemType = itemType;
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public ListIterator<String> iterator(PropT prop, XMLSerializer context) {
            ListIterator i = this.core.iterator(prop, context);
            return new IDREFSIterator(i, context);
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public IDREFS<BeanT, PropT>.Pack startPacking(BeanT bean, Accessor<BeanT, PropT> acc) {
            return new Pack(bean, acc);
        }

        public void addToPack(IDREFS<BeanT, PropT>.Pack pack, String item) {
            pack.add(item);
        }

        public void endPacking(IDREFS<BeanT, PropT>.Pack pack, BeanT bean, Accessor<BeanT, PropT> acc) {
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
        public void reset(BeanT bean, Accessor<BeanT, PropT> acc) throws AccessorException {
            this.core.reset(bean, acc);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/Lister$IDREFS$Pack.class */
        public class Pack implements Patcher {
            private final BeanT bean;
            private final Accessor<BeanT, PropT> acc;
            private final List<String> idrefs = new ArrayList();
            private final UnmarshallingContext context = UnmarshallingContext.getInstance();
            private final LocatorEx location = new LocatorEx.Snapshot(this.context.getLocator());

            public Pack(BeanT bean, Accessor<BeanT, PropT> acc) {
                this.bean = bean;
                this.acc = acc;
                this.context.addPatcher(this);
            }

            public void add(String item) {
                this.idrefs.add(item);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Patcher
            public void run() throws SAXException {
                Object call;
                try {
                    Object pack = IDREFS.this.core.startPacking(this.bean, this.acc);
                    for (String id : this.idrefs) {
                        Callable callable = this.context.getObjectFromId(id, IDREFS.this.itemType);
                        if (callable != null) {
                            try {
                                call = callable.call();
                            } catch (SAXException e) {
                                throw e;
                            } catch (Exception e2) {
                                throw new SAXException2(e2);
                            }
                        } else {
                            call = null;
                        }
                        Object t = call;
                        if (t == null) {
                            this.context.errorUnresolvedIDREF(this.bean, id, this.location);
                        } else {
                            TODO.prototype();
                            IDREFS.this.core.addToPack(pack, t);
                        }
                    }
                    IDREFS.this.core.endPacking(pack, this.bean, this.acc);
                } catch (AccessorException e3) {
                    this.context.handleError(e3);
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/Lister$IDREFSIterator.class */
    public static final class IDREFSIterator implements ListIterator<String> {
        private final ListIterator i;
        private final XMLSerializer context;
        private Object last;

        private IDREFSIterator(ListIterator i, XMLSerializer context) {
            this.i = i;
            this.context = context;
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
        public boolean hasNext() {
            return this.i.hasNext();
        }

        public Object last() {
            return this.last;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
        public String next() throws SAXException, JAXBException {
            this.last = this.i.next();
            String id = this.context.grammar.getBeanInfo(this.last, true).getId(this.last, this.context);
            if (id == null) {
                this.context.errorMissingId(this.last);
            }
            return id;
        }
    }

    public static <A, B, C, D> Lister<A, B, C, D> getErrorInstance() {
        return ERROR;
    }
}
