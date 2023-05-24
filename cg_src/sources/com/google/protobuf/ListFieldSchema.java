package com.google.protobuf;

import com.google.protobuf.Internal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@CheckReturnValue
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/ListFieldSchema.class */
abstract class ListFieldSchema {
    private static final ListFieldSchema FULL_INSTANCE = new ListFieldSchemaFull();
    private static final ListFieldSchema LITE_INSTANCE = new ListFieldSchemaLite();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract <L> List<L> mutableListAt(Object obj, long j);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void makeImmutableListAt(Object obj, long j);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract <L> void mergeListsAt(Object obj, Object obj2, long j);

    private ListFieldSchema() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ListFieldSchema full() {
        return FULL_INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ListFieldSchema lite() {
        return LITE_INSTANCE;
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/ListFieldSchema$ListFieldSchemaFull.class */
    private static final class ListFieldSchemaFull extends ListFieldSchema {
        private static final Class<?> UNMODIFIABLE_LIST_CLASS = Collections.unmodifiableList(Collections.emptyList()).getClass();

        private ListFieldSchemaFull() {
            super();
        }

        @Override // com.google.protobuf.ListFieldSchema
        <L> List<L> mutableListAt(Object message, long offset) {
            return mutableListAt(message, offset, 10);
        }

        @Override // com.google.protobuf.ListFieldSchema
        void makeImmutableListAt(Object message, long offset) {
            Object immutable;
            List<?> list = (List) UnsafeUtil.getObject(message, offset);
            if (list instanceof LazyStringList) {
                immutable = ((LazyStringList) list).getUnmodifiableView();
            } else if (UNMODIFIABLE_LIST_CLASS.isAssignableFrom(list.getClass())) {
                return;
            } else {
                if ((list instanceof PrimitiveNonBoxingCollection) && (list instanceof Internal.ProtobufList)) {
                    if (((Internal.ProtobufList) list).isModifiable()) {
                        ((Internal.ProtobufList) list).makeImmutable();
                        return;
                    }
                    return;
                }
                immutable = Collections.unmodifiableList(list);
            }
            UnsafeUtil.putObject(message, offset, immutable);
        }

        private static <L> List<L> mutableListAt(Object message, long offset, int additionalCapacity) {
            List<L> list = getList(message, offset);
            if (list.isEmpty()) {
                if (list instanceof LazyStringList) {
                    list = new LazyStringArrayList(additionalCapacity);
                } else if ((list instanceof PrimitiveNonBoxingCollection) && (list instanceof Internal.ProtobufList)) {
                    list = ((Internal.ProtobufList) list).mutableCopyWithCapacity(additionalCapacity);
                } else {
                    list = new ArrayList<>(additionalCapacity);
                }
                UnsafeUtil.putObject(message, offset, list);
            } else if (UNMODIFIABLE_LIST_CLASS.isAssignableFrom(list.getClass())) {
                ArrayList<L> newList = new ArrayList<>(list.size() + additionalCapacity);
                newList.addAll(list);
                list = newList;
                UnsafeUtil.putObject(message, offset, list);
            } else if (list instanceof UnmodifiableLazyStringList) {
                List<L> newList2 = new LazyStringArrayList(list.size() + additionalCapacity);
                newList2.addAll((UnmodifiableLazyStringList) list);
                list = newList2;
                UnsafeUtil.putObject(message, offset, list);
            } else if ((list instanceof PrimitiveNonBoxingCollection) && (list instanceof Internal.ProtobufList) && !((Internal.ProtobufList) list).isModifiable()) {
                list = ((Internal.ProtobufList) list).mutableCopyWithCapacity(list.size() + additionalCapacity);
                UnsafeUtil.putObject(message, offset, list);
            }
            return list;
        }

        @Override // com.google.protobuf.ListFieldSchema
        <E> void mergeListsAt(Object msg, Object otherMsg, long offset) {
            List<E> other = getList(otherMsg, offset);
            List<E> mine = mutableListAt(msg, offset, other.size());
            int size = mine.size();
            int otherSize = other.size();
            if (size > 0 && otherSize > 0) {
                mine.addAll(other);
            }
            List<E> merged = size > 0 ? mine : other;
            UnsafeUtil.putObject(msg, offset, merged);
        }

        static <E> List<E> getList(Object message, long offset) {
            return (List) UnsafeUtil.getObject(message, offset);
        }
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/ListFieldSchema$ListFieldSchemaLite.class */
    private static final class ListFieldSchemaLite extends ListFieldSchema {
        private ListFieldSchemaLite() {
            super();
        }

        @Override // com.google.protobuf.ListFieldSchema
        <L> List<L> mutableListAt(Object message, long offset) {
            Internal.ProtobufList<L> list = getProtobufList(message, offset);
            if (!list.isModifiable()) {
                int size = list.size();
                list = list.mutableCopyWithCapacity(size == 0 ? 10 : size * 2);
                UnsafeUtil.putObject(message, offset, list);
            }
            return list;
        }

        @Override // com.google.protobuf.ListFieldSchema
        void makeImmutableListAt(Object message, long offset) {
            Internal.ProtobufList<?> list = getProtobufList(message, offset);
            list.makeImmutable();
        }

        @Override // com.google.protobuf.ListFieldSchema
        <E> void mergeListsAt(Object msg, Object otherMsg, long offset) {
            Internal.ProtobufList<E> mine = getProtobufList(msg, offset);
            Internal.ProtobufList<E> other = getProtobufList(otherMsg, offset);
            int size = mine.size();
            int otherSize = other.size();
            if (size > 0 && otherSize > 0) {
                if (!mine.isModifiable()) {
                    mine = mine.mutableCopyWithCapacity(size + otherSize);
                }
                mine.addAll(other);
            }
            Internal.ProtobufList<E> merged = size > 0 ? mine : other;
            UnsafeUtil.putObject(msg, offset, merged);
        }

        static <E> Internal.ProtobufList<E> getProtobufList(Object message, long offset) {
            return (Internal.ProtobufList) UnsafeUtil.getObject(message, offset);
        }
    }
}
