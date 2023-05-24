package org.jf.dexlib2.writer.util;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.value.BaseArrayEncodedValue;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableEncodedValueFactory;
import org.jf.dexlib2.util.EncodedValueUtils;
import org.jf.util.AbstractForwardSequentialList;
import org.jf.util.CollectionUtils;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/util/StaticInitializerUtil.class */
public class StaticInitializerUtil {
    private static final Predicate<Field> HAS_INITIALIZER = new Predicate<Field>() { // from class: org.jf.dexlib2.writer.util.StaticInitializerUtil.2
        @Override // com.google.common.base.Predicate
        public boolean apply(Field input) {
            EncodedValue encodedValue = input.getInitialValue();
            return (encodedValue == null || EncodedValueUtils.isDefaultValue(encodedValue)) ? false : true;
        }
    };
    private static final Function<Field, EncodedValue> GET_INITIAL_VALUE = new Function<Field, EncodedValue>() { // from class: org.jf.dexlib2.writer.util.StaticInitializerUtil.3
        @Override // com.google.common.base.Function
        public EncodedValue apply(Field input) {
            EncodedValue initialValue = input.getInitialValue();
            if (initialValue == null) {
                return ImmutableEncodedValueFactory.defaultValueForType(input.getType());
            }
            return initialValue;
        }
    };

    @Nullable
    public static ArrayEncodedValue getStaticInitializers(@Nonnull final SortedSet<? extends Field> sortedStaticFields) {
        final int lastIndex = CollectionUtils.lastIndexOf(sortedStaticFields, HAS_INITIALIZER);
        if (lastIndex > -1) {
            return new BaseArrayEncodedValue() { // from class: org.jf.dexlib2.writer.util.StaticInitializerUtil.1
                @Override // org.jf.dexlib2.iface.value.ArrayEncodedValue
                @Nonnull
                public List<? extends EncodedValue> getValue() {
                    return new AbstractForwardSequentialList<EncodedValue>() { // from class: org.jf.dexlib2.writer.util.StaticInitializerUtil.1.1
                        @Override // org.jf.util.AbstractForwardSequentialList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
                        @Nonnull
                        public Iterator<EncodedValue> iterator() {
                            return FluentIterable.from(sortedStaticFields).limit(lastIndex + 1).transform(StaticInitializerUtil.GET_INITIAL_VALUE).iterator();
                        }

                        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                        public int size() {
                            return lastIndex + 1;
                        }
                    };
                }
            };
        }
        return null;
    }
}
