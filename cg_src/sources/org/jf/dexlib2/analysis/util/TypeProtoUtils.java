package org.jf.dexlib2.analysis.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.analysis.TypeProto;
import org.jf.dexlib2.analysis.UnresolvedClassException;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/analysis/util/TypeProtoUtils.class */
public class TypeProtoUtils {
    @Nonnull
    public static Iterable<TypeProto> getSuperclassChain(@Nonnull final TypeProto typeProto) {
        return new Iterable<TypeProto>() { // from class: org.jf.dexlib2.analysis.util.TypeProtoUtils.1
            @Override // java.lang.Iterable
            public Iterator<TypeProto> iterator() {
                return new Iterator<TypeProto>() { // from class: org.jf.dexlib2.analysis.util.TypeProtoUtils.1.1
                    @Nullable
                    private TypeProto type;

                    {
                        this.type = TypeProtoUtils.getSuperclassAsTypeProto(TypeProto.this);
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.type != null;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public TypeProto next() {
                        TypeProto type = this.type;
                        if (type == null) {
                            throw new NoSuchElementException();
                        }
                        this.type = TypeProtoUtils.getSuperclassAsTypeProto(type);
                        return type;
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    @Nullable
    public static TypeProto getSuperclassAsTypeProto(@Nonnull TypeProto type) {
        try {
            String next = type.getSuperclass();
            if (next != null) {
                return type.getClassPath().getClass(next);
            }
            return null;
        } catch (UnresolvedClassException e) {
            return type.getClassPath().getUnknownClass();
        }
    }

    public static boolean extendsFrom(@Nonnull TypeProto candidate, @Nonnull String possibleSuper) {
        if (candidate.getType().equals(possibleSuper)) {
            return true;
        }
        for (TypeProto superProto : getSuperclassChain(candidate)) {
            if (superProto.getType().equals(possibleSuper)) {
                return true;
            }
        }
        return false;
    }
}
