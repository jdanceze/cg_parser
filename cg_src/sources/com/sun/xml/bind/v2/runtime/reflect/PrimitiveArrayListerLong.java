package com.sun.xml.bind.v2.runtime.reflect;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/PrimitiveArrayListerLong.class */
final class PrimitiveArrayListerLong<BeanT> extends Lister<BeanT, long[], Long, LongArrayPack> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ void endPacking(LongArrayPack longArrayPack, Object obj, Accessor accessor) throws AccessorException {
        endPacking2(longArrayPack, (LongArrayPack) obj, (Accessor<LongArrayPack, long[]>) accessor);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ LongArrayPack startPacking(Object obj, Accessor accessor) throws AccessorException {
        return startPacking((PrimitiveArrayListerLong<BeanT>) obj, (Accessor<PrimitiveArrayListerLong<BeanT>, long[]>) accessor);
    }

    private PrimitiveArrayListerLong() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void register() {
        Lister.primitiveArrayListers.put(Long.TYPE, new PrimitiveArrayListerLong());
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public ListIterator<Long> iterator(final long[] objects, XMLSerializer context) {
        return new ListIterator<Long>() { // from class: com.sun.xml.bind.v2.runtime.reflect.PrimitiveArrayListerLong.1
            int idx = 0;

            @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
            public boolean hasNext() {
                return this.idx < objects.length;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
            public Long next() {
                long[] jArr = objects;
                int i = this.idx;
                this.idx = i + 1;
                return Long.valueOf(jArr[i]);
            }
        };
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public LongArrayPack startPacking(BeanT current, Accessor<BeanT, long[]> acc) {
        return new LongArrayPack();
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public void addToPack(LongArrayPack objects, Long o) {
        objects.add(o);
    }

    /* renamed from: endPacking  reason: avoid collision after fix types in other method */
    public void endPacking2(LongArrayPack pack, BeanT bean, Accessor<BeanT, long[]> acc) throws AccessorException {
        acc.set(bean, pack.build());
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public void reset(BeanT o, Accessor<BeanT, long[]> acc) throws AccessorException {
        acc.set(o, new long[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/PrimitiveArrayListerLong$LongArrayPack.class */
    public static final class LongArrayPack {
        long[] buf = new long[16];
        int size;

        LongArrayPack() {
        }

        void add(Long b) {
            if (this.buf.length == this.size) {
                long[] nb = new long[this.buf.length * 2];
                System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
                this.buf = nb;
            }
            if (b != null) {
                long[] jArr = this.buf;
                int i = this.size;
                this.size = i + 1;
                jArr[i] = b.longValue();
            }
        }

        long[] build() {
            if (this.buf.length == this.size) {
                return this.buf;
            }
            long[] r = new long[this.size];
            System.arraycopy(this.buf, 0, r, 0, this.size);
            return r;
        }
    }
}
