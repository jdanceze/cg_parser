package com.sun.xml.bind.v2.runtime.reflect;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/PrimitiveArrayListerBoolean.class */
final class PrimitiveArrayListerBoolean<BeanT> extends Lister<BeanT, boolean[], Boolean, BooleanArrayPack> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ void endPacking(BooleanArrayPack booleanArrayPack, Object obj, Accessor accessor) throws AccessorException {
        endPacking2(booleanArrayPack, (BooleanArrayPack) obj, (Accessor<BooleanArrayPack, boolean[]>) accessor);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ BooleanArrayPack startPacking(Object obj, Accessor accessor) throws AccessorException {
        return startPacking((PrimitiveArrayListerBoolean<BeanT>) obj, (Accessor<PrimitiveArrayListerBoolean<BeanT>, boolean[]>) accessor);
    }

    private PrimitiveArrayListerBoolean() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void register() {
        Lister.primitiveArrayListers.put(Boolean.TYPE, new PrimitiveArrayListerBoolean());
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public ListIterator<Boolean> iterator(final boolean[] objects, XMLSerializer context) {
        return new ListIterator<Boolean>() { // from class: com.sun.xml.bind.v2.runtime.reflect.PrimitiveArrayListerBoolean.1
            int idx = 0;

            @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
            public boolean hasNext() {
                return this.idx < objects.length;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
            public Boolean next() {
                boolean[] zArr = objects;
                int i = this.idx;
                this.idx = i + 1;
                return Boolean.valueOf(zArr[i]);
            }
        };
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public BooleanArrayPack startPacking(BeanT current, Accessor<BeanT, boolean[]> acc) {
        return new BooleanArrayPack();
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public void addToPack(BooleanArrayPack objects, Boolean o) {
        objects.add(o);
    }

    /* renamed from: endPacking  reason: avoid collision after fix types in other method */
    public void endPacking2(BooleanArrayPack pack, BeanT bean, Accessor<BeanT, boolean[]> acc) throws AccessorException {
        acc.set(bean, pack.build());
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public void reset(BeanT o, Accessor<BeanT, boolean[]> acc) throws AccessorException {
        acc.set(o, new boolean[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/PrimitiveArrayListerBoolean$BooleanArrayPack.class */
    public static final class BooleanArrayPack {
        boolean[] buf = new boolean[16];
        int size;

        BooleanArrayPack() {
        }

        void add(Boolean b) {
            if (this.buf.length == this.size) {
                boolean[] nb = new boolean[this.buf.length * 2];
                System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
                this.buf = nb;
            }
            if (b != null) {
                boolean[] zArr = this.buf;
                int i = this.size;
                this.size = i + 1;
                zArr[i] = b.booleanValue();
            }
        }

        boolean[] build() {
            if (this.buf.length == this.size) {
                return this.buf;
            }
            boolean[] r = new boolean[this.size];
            System.arraycopy(this.buf, 0, r, 0, this.size);
            return r;
        }
    }
}
