package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import java.util.SortedSet;
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/SortedMultisetBridge.class */
interface SortedMultisetBridge<E> extends Multiset<E> {
    @Override // com.google.common.collect.Multiset
    SortedSet<E> elementSet();
}
