package com.sun.xml.bind.v2.runtime.reflect;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/PrimitiveArrayListerDouble.class */
final class PrimitiveArrayListerDouble<BeanT> extends Lister<BeanT, double[], Double, DoubleArrayPack> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ void endPacking(DoubleArrayPack doubleArrayPack, Object obj, Accessor accessor) throws AccessorException {
        endPacking2(doubleArrayPack, (DoubleArrayPack) obj, (Accessor<DoubleArrayPack, double[]>) accessor);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ DoubleArrayPack startPacking(Object obj, Accessor accessor) throws AccessorException {
        return startPacking((PrimitiveArrayListerDouble<BeanT>) obj, (Accessor<PrimitiveArrayListerDouble<BeanT>, double[]>) accessor);
    }

    private PrimitiveArrayListerDouble() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void register() {
        Lister.primitiveArrayListers.put(Double.TYPE, new PrimitiveArrayListerDouble());
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public ListIterator<Double> iterator(final double[] objects, XMLSerializer context) {
        return new ListIterator<Double>() { // from class: com.sun.xml.bind.v2.runtime.reflect.PrimitiveArrayListerDouble.1
            int idx = 0;

            @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
            public boolean hasNext() {
                return this.idx < objects.length;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
            public Double next() {
                double[] dArr = objects;
                int i = this.idx;
                this.idx = i + 1;
                return Double.valueOf(dArr[i]);
            }
        };
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public DoubleArrayPack startPacking(BeanT current, Accessor<BeanT, double[]> acc) {
        return new DoubleArrayPack();
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public void addToPack(DoubleArrayPack objects, Double o) {
        objects.add(o);
    }

    /* renamed from: endPacking  reason: avoid collision after fix types in other method */
    public void endPacking2(DoubleArrayPack pack, BeanT bean, Accessor<BeanT, double[]> acc) throws AccessorException {
        acc.set(bean, pack.build());
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public void reset(BeanT o, Accessor<BeanT, double[]> acc) throws AccessorException {
        acc.set(o, new double[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/PrimitiveArrayListerDouble$DoubleArrayPack.class */
    public static final class DoubleArrayPack {
        double[] buf = new double[16];
        int size;

        DoubleArrayPack() {
        }

        void add(Double b) {
            if (this.buf.length == this.size) {
                double[] nb = new double[this.buf.length * 2];
                System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
                this.buf = nb;
            }
            if (b != null) {
                double[] dArr = this.buf;
                int i = this.size;
                this.size = i + 1;
                dArr[i] = b.doubleValue();
            }
        }

        double[] build() {
            if (this.buf.length == this.size) {
                return this.buf;
            }
            double[] r = new double[this.size];
            System.arraycopy(this.buf, 0, r, 0, this.size);
            return r;
        }
    }
}
