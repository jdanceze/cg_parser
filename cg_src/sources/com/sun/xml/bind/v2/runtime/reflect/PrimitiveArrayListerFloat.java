package com.sun.xml.bind.v2.runtime.reflect;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/PrimitiveArrayListerFloat.class */
final class PrimitiveArrayListerFloat<BeanT> extends Lister<BeanT, float[], Float, FloatArrayPack> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ void endPacking(FloatArrayPack floatArrayPack, Object obj, Accessor accessor) throws AccessorException {
        endPacking2(floatArrayPack, (FloatArrayPack) obj, (Accessor<FloatArrayPack, float[]>) accessor);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ FloatArrayPack startPacking(Object obj, Accessor accessor) throws AccessorException {
        return startPacking((PrimitiveArrayListerFloat<BeanT>) obj, (Accessor<PrimitiveArrayListerFloat<BeanT>, float[]>) accessor);
    }

    private PrimitiveArrayListerFloat() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void register() {
        Lister.primitiveArrayListers.put(Float.TYPE, new PrimitiveArrayListerFloat());
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public ListIterator<Float> iterator(final float[] objects, XMLSerializer context) {
        return new ListIterator<Float>() { // from class: com.sun.xml.bind.v2.runtime.reflect.PrimitiveArrayListerFloat.1
            int idx = 0;

            @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
            public boolean hasNext() {
                return this.idx < objects.length;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
            public Float next() {
                float[] fArr = objects;
                int i = this.idx;
                this.idx = i + 1;
                return Float.valueOf(fArr[i]);
            }
        };
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public FloatArrayPack startPacking(BeanT current, Accessor<BeanT, float[]> acc) {
        return new FloatArrayPack();
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public void addToPack(FloatArrayPack objects, Float o) {
        objects.add(o);
    }

    /* renamed from: endPacking  reason: avoid collision after fix types in other method */
    public void endPacking2(FloatArrayPack pack, BeanT bean, Accessor<BeanT, float[]> acc) throws AccessorException {
        acc.set(bean, pack.build());
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public void reset(BeanT o, Accessor<BeanT, float[]> acc) throws AccessorException {
        acc.set(o, new float[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/PrimitiveArrayListerFloat$FloatArrayPack.class */
    public static final class FloatArrayPack {
        float[] buf = new float[16];
        int size;

        FloatArrayPack() {
        }

        void add(Float b) {
            if (this.buf.length == this.size) {
                float[] nb = new float[this.buf.length * 2];
                System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
                this.buf = nb;
            }
            if (b != null) {
                float[] fArr = this.buf;
                int i = this.size;
                this.size = i + 1;
                fArr[i] = b.floatValue();
            }
        }

        float[] build() {
            if (this.buf.length == this.size) {
                return this.buf;
            }
            float[] r = new float[this.size];
            System.arraycopy(this.buf, 0, r, 0, this.size);
            return r;
        }
    }
}
