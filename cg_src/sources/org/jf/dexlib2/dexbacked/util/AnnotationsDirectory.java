package org.jf.dexlib2.dexbacked.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.DexBackedAnnotation;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/util/AnnotationsDirectory.class */
public abstract class AnnotationsDirectory {
    public static final AnnotationsDirectory EMPTY = new AnnotationsDirectory() { // from class: org.jf.dexlib2.dexbacked.util.AnnotationsDirectory.1
        @Override // org.jf.dexlib2.dexbacked.util.AnnotationsDirectory
        public int getFieldAnnotationCount() {
            return 0;
        }

        @Override // org.jf.dexlib2.dexbacked.util.AnnotationsDirectory
        @Nonnull
        public Set<? extends DexBackedAnnotation> getClassAnnotations() {
            return ImmutableSet.of();
        }

        @Override // org.jf.dexlib2.dexbacked.util.AnnotationsDirectory
        @Nonnull
        public AnnotationIterator getFieldAnnotationIterator() {
            return AnnotationIterator.EMPTY;
        }

        @Override // org.jf.dexlib2.dexbacked.util.AnnotationsDirectory
        @Nonnull
        public AnnotationIterator getMethodAnnotationIterator() {
            return AnnotationIterator.EMPTY;
        }

        @Override // org.jf.dexlib2.dexbacked.util.AnnotationsDirectory
        @Nonnull
        public AnnotationIterator getParameterAnnotationIterator() {
            return AnnotationIterator.EMPTY;
        }
    };

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/util/AnnotationsDirectory$AnnotationIterator.class */
    public interface AnnotationIterator {
        public static final AnnotationIterator EMPTY = new AnnotationIterator() { // from class: org.jf.dexlib2.dexbacked.util.AnnotationsDirectory.AnnotationIterator.1
            @Override // org.jf.dexlib2.dexbacked.util.AnnotationsDirectory.AnnotationIterator
            public int seekTo(int key) {
                return 0;
            }

            @Override // org.jf.dexlib2.dexbacked.util.AnnotationsDirectory.AnnotationIterator
            public void reset() {
            }
        };

        int seekTo(int i);

        void reset();
    }

    public abstract int getFieldAnnotationCount();

    @Nonnull
    public abstract Set<? extends DexBackedAnnotation> getClassAnnotations();

    @Nonnull
    public abstract AnnotationIterator getFieldAnnotationIterator();

    @Nonnull
    public abstract AnnotationIterator getMethodAnnotationIterator();

    @Nonnull
    public abstract AnnotationIterator getParameterAnnotationIterator();

    @Nonnull
    public static AnnotationsDirectory newOrEmpty(@Nonnull DexBackedDexFile dexFile, int directoryAnnotationsOffset) {
        if (directoryAnnotationsOffset == 0) {
            return EMPTY;
        }
        return new AnnotationsDirectoryImpl(dexFile, directoryAnnotationsOffset);
    }

    @Nonnull
    public static Set<? extends DexBackedAnnotation> getAnnotations(@Nonnull final DexBackedDexFile dexFile, final int annotationSetOffset) {
        if (annotationSetOffset != 0) {
            final int size = dexFile.getDataBuffer().readSmallUint(annotationSetOffset);
            return new FixedSizeSet<DexBackedAnnotation>() { // from class: org.jf.dexlib2.dexbacked.util.AnnotationsDirectory.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // org.jf.dexlib2.dexbacked.util.FixedSizeSet
                @Nonnull
                public DexBackedAnnotation readItem(int index) {
                    int annotationOffset = DexBackedDexFile.this.getDataBuffer().readSmallUint(annotationSetOffset + 4 + (4 * index));
                    return new DexBackedAnnotation(DexBackedDexFile.this, annotationOffset);
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public int size() {
                    return size;
                }
            };
        }
        return ImmutableSet.of();
    }

    @Nonnull
    public static List<Set<? extends DexBackedAnnotation>> getParameterAnnotations(@Nonnull final DexBackedDexFile dexFile, final int annotationSetListOffset) {
        if (annotationSetListOffset > 0) {
            final int size = dexFile.getDataBuffer().readSmallUint(annotationSetListOffset);
            return new FixedSizeList<Set<? extends DexBackedAnnotation>>() { // from class: org.jf.dexlib2.dexbacked.util.AnnotationsDirectory.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // org.jf.dexlib2.dexbacked.util.FixedSizeList
                @Nonnull
                public Set<? extends DexBackedAnnotation> readItem(int index) {
                    int annotationSetOffset = DexBackedDexFile.this.getDataBuffer().readSmallUint(annotationSetListOffset + 4 + (index * 4));
                    return AnnotationsDirectory.getAnnotations(DexBackedDexFile.this, annotationSetOffset);
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return size;
                }
            };
        }
        return ImmutableList.of();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/util/AnnotationsDirectory$AnnotationsDirectoryImpl.class */
    public static class AnnotationsDirectoryImpl extends AnnotationsDirectory {
        @Nonnull
        public final DexBackedDexFile dexFile;
        private final int directoryOffset;
        private static final int FIELD_COUNT_OFFSET = 4;
        private static final int METHOD_COUNT_OFFSET = 8;
        private static final int PARAMETER_COUNT_OFFSET = 12;
        private static final int ANNOTATIONS_START_OFFSET = 16;
        private static final int FIELD_ANNOTATION_SIZE = 8;
        private static final int METHOD_ANNOTATION_SIZE = 8;

        public AnnotationsDirectoryImpl(@Nonnull DexBackedDexFile dexFile, int directoryOffset) {
            this.dexFile = dexFile;
            this.directoryOffset = directoryOffset;
        }

        @Override // org.jf.dexlib2.dexbacked.util.AnnotationsDirectory
        public int getFieldAnnotationCount() {
            return this.dexFile.getDataBuffer().readSmallUint(this.directoryOffset + 4);
        }

        public int getMethodAnnotationCount() {
            return this.dexFile.getDataBuffer().readSmallUint(this.directoryOffset + 8);
        }

        public int getParameterAnnotationCount() {
            return this.dexFile.getDataBuffer().readSmallUint(this.directoryOffset + 12);
        }

        @Override // org.jf.dexlib2.dexbacked.util.AnnotationsDirectory
        @Nonnull
        public Set<? extends DexBackedAnnotation> getClassAnnotations() {
            return getAnnotations(this.dexFile, this.dexFile.getDataBuffer().readSmallUint(this.directoryOffset));
        }

        @Override // org.jf.dexlib2.dexbacked.util.AnnotationsDirectory
        @Nonnull
        public AnnotationIterator getFieldAnnotationIterator() {
            int fieldAnnotationCount = getFieldAnnotationCount();
            if (fieldAnnotationCount == 0) {
                return AnnotationIterator.EMPTY;
            }
            return new AnnotationIteratorImpl(this.directoryOffset + 16, fieldAnnotationCount);
        }

        @Override // org.jf.dexlib2.dexbacked.util.AnnotationsDirectory
        @Nonnull
        public AnnotationIterator getMethodAnnotationIterator() {
            int methodCount = getMethodAnnotationCount();
            if (methodCount == 0) {
                return AnnotationIterator.EMPTY;
            }
            int fieldCount = getFieldAnnotationCount();
            int methodAnnotationsOffset = this.directoryOffset + 16 + (fieldCount * 8);
            return new AnnotationIteratorImpl(methodAnnotationsOffset, methodCount);
        }

        @Override // org.jf.dexlib2.dexbacked.util.AnnotationsDirectory
        @Nonnull
        public AnnotationIterator getParameterAnnotationIterator() {
            int parameterAnnotationCount = getParameterAnnotationCount();
            if (parameterAnnotationCount == 0) {
                return AnnotationIterator.EMPTY;
            }
            int fieldCount = getFieldAnnotationCount();
            int methodCount = getMethodAnnotationCount();
            int parameterAnnotationsOffset = this.directoryOffset + 16 + (fieldCount * 8) + (methodCount * 8);
            return new AnnotationIteratorImpl(parameterAnnotationsOffset, parameterAnnotationCount);
        }

        /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/util/AnnotationsDirectory$AnnotationsDirectoryImpl$AnnotationIteratorImpl.class */
        private class AnnotationIteratorImpl implements AnnotationIterator {
            private final int startOffset;
            private final int size;
            private int currentIndex = 0;
            private int currentItemIndex;

            public AnnotationIteratorImpl(int startOffset, int size) {
                this.startOffset = startOffset;
                this.size = size;
                this.currentItemIndex = AnnotationsDirectoryImpl.this.dexFile.getDataBuffer().readSmallUint(startOffset);
            }

            @Override // org.jf.dexlib2.dexbacked.util.AnnotationsDirectory.AnnotationIterator
            public int seekTo(int itemIndex) {
                while (this.currentItemIndex < itemIndex && this.currentIndex + 1 < this.size) {
                    this.currentIndex++;
                    this.currentItemIndex = AnnotationsDirectoryImpl.this.dexFile.getDataBuffer().readSmallUint(this.startOffset + (this.currentIndex * 8));
                }
                if (this.currentItemIndex == itemIndex) {
                    return AnnotationsDirectoryImpl.this.dexFile.getDataBuffer().readSmallUint(this.startOffset + (this.currentIndex * 8) + 4);
                }
                return 0;
            }

            @Override // org.jf.dexlib2.dexbacked.util.AnnotationsDirectory.AnnotationIterator
            public void reset() {
                this.currentItemIndex = AnnotationsDirectoryImpl.this.dexFile.getDataBuffer().readSmallUint(this.startOffset);
                this.currentIndex = 0;
            }
        }
    }
}
