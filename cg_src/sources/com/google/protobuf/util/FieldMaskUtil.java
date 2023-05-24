package com.google.protobuf.util;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.protobuf.Descriptors;
import com.google.protobuf.FieldMask;
import com.google.protobuf.Internal;
import com.google.protobuf.Message;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
/* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/FieldMaskUtil.class */
public final class FieldMaskUtil {
    private static final String FIELD_PATH_SEPARATOR = ",";
    private static final String FIELD_PATH_SEPARATOR_REGEX = ",";
    private static final String FIELD_SEPARATOR_REGEX = "\\.";

    private FieldMaskUtil() {
    }

    public static String toString(FieldMask fieldMask) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (String value : fieldMask.getPathsList()) {
            if (!value.isEmpty()) {
                if (first) {
                    first = false;
                } else {
                    result.append(",");
                }
                result.append(value);
            }
        }
        return result.toString();
    }

    public static FieldMask fromString(String value) {
        return fromStringList(Arrays.asList(value.split(",")));
    }

    public static FieldMask fromString(Class<? extends Message> type, String value) {
        return fromStringList(type, Arrays.asList(value.split(",")));
    }

    public static FieldMask fromStringList(Class<? extends Message> type, Iterable<String> paths) {
        return fromStringList(((Message) Internal.getDefaultInstance(type)).getDescriptorForType(), paths);
    }

    public static FieldMask fromStringList(Descriptors.Descriptor descriptor, Iterable<String> paths) {
        return fromStringList(Optional.of(descriptor), paths);
    }

    public static FieldMask fromStringList(Iterable<String> paths) {
        return fromStringList(Optional.absent(), paths);
    }

    private static FieldMask fromStringList(Optional<Descriptors.Descriptor> descriptor, Iterable<String> paths) {
        FieldMask.Builder builder = FieldMask.newBuilder();
        for (String path : paths) {
            if (!path.isEmpty()) {
                if (descriptor.isPresent() && !isValid(descriptor.get(), path)) {
                    throw new IllegalArgumentException(path + " is not a valid path for " + descriptor.get().getFullName());
                }
                builder.addPaths(path);
            }
        }
        return builder.build();
    }

    public static FieldMask fromFieldNumbers(Class<? extends Message> type, int... fieldNumbers) {
        return fromFieldNumbers(type, Ints.asList(fieldNumbers));
    }

    public static FieldMask fromFieldNumbers(Class<? extends Message> type, Iterable<Integer> fieldNumbers) {
        Descriptors.Descriptor descriptor = ((Message) Internal.getDefaultInstance(type)).getDescriptorForType();
        FieldMask.Builder builder = FieldMask.newBuilder();
        for (Integer fieldNumber : fieldNumbers) {
            Descriptors.FieldDescriptor field = descriptor.findFieldByNumber(fieldNumber.intValue());
            Preconditions.checkArgument(field != null, String.format("%s is not a valid field number for %s.", fieldNumber, type));
            builder.addPaths(field.getName());
        }
        return builder.build();
    }

    public static String toJsonString(FieldMask fieldMask) {
        List<String> paths = new ArrayList<>(fieldMask.getPathsCount());
        for (String path : fieldMask.getPathsList()) {
            if (!path.isEmpty()) {
                paths.add(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, path));
            }
        }
        return Joiner.on(",").join(paths);
    }

    public static FieldMask fromJsonString(String value) {
        Iterable<String> paths = Splitter.on(",").split(value);
        FieldMask.Builder builder = FieldMask.newBuilder();
        for (String path : paths) {
            if (!path.isEmpty()) {
                builder.addPaths(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, path));
            }
        }
        return builder.build();
    }

    public static boolean isValid(Class<? extends Message> type, FieldMask fieldMask) {
        Descriptors.Descriptor descriptor = ((Message) Internal.getDefaultInstance(type)).getDescriptorForType();
        return isValid(descriptor, fieldMask);
    }

    public static boolean isValid(Descriptors.Descriptor descriptor, FieldMask fieldMask) {
        for (String path : fieldMask.getPathsList()) {
            if (!isValid(descriptor, path)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValid(Class<? extends Message> type, String path) {
        Descriptors.Descriptor descriptor = ((Message) Internal.getDefaultInstance(type)).getDescriptorForType();
        return isValid(descriptor, path);
    }

    public static boolean isValid(@Nullable Descriptors.Descriptor descriptor, String path) {
        Descriptors.FieldDescriptor field;
        Descriptors.Descriptor descriptor2;
        String[] parts = path.split(FIELD_SEPARATOR_REGEX);
        if (parts.length == 0) {
            return false;
        }
        for (String name : parts) {
            if (descriptor == null || (field = descriptor.findFieldByName(name)) == null) {
                return false;
            }
            if (!field.isRepeated() && field.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                descriptor2 = field.getMessageType();
            } else {
                descriptor2 = null;
            }
            descriptor = descriptor2;
        }
        return true;
    }

    public static FieldMask normalize(FieldMask mask) {
        return new FieldMaskTree(mask).toFieldMask();
    }

    public static FieldMask union(FieldMask firstMask, FieldMask secondMask, FieldMask... otherMasks) {
        FieldMaskTree maskTree = new FieldMaskTree(firstMask).mergeFromFieldMask(secondMask);
        for (FieldMask mask : otherMasks) {
            maskTree.mergeFromFieldMask(mask);
        }
        return maskTree.toFieldMask();
    }

    public static FieldMask subtract(FieldMask firstMask, FieldMask secondMask, FieldMask... otherMasks) {
        FieldMaskTree maskTree = new FieldMaskTree(firstMask).removeFromFieldMask(secondMask);
        for (FieldMask mask : otherMasks) {
            maskTree.removeFromFieldMask(mask);
        }
        return maskTree.toFieldMask();
    }

    public static FieldMask intersection(FieldMask mask1, FieldMask mask2) {
        FieldMaskTree tree = new FieldMaskTree(mask1);
        FieldMaskTree result = new FieldMaskTree();
        for (String path : mask2.getPathsList()) {
            tree.intersectFieldPath(path, result);
        }
        return result.toFieldMask();
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/FieldMaskUtil$MergeOptions.class */
    public static final class MergeOptions {
        private boolean replaceMessageFields = false;
        private boolean replaceRepeatedFields = false;
        private boolean replacePrimitiveFields = false;

        public boolean replaceMessageFields() {
            return this.replaceMessageFields;
        }

        public boolean replaceRepeatedFields() {
            return this.replaceRepeatedFields;
        }

        public boolean replacePrimitiveFields() {
            return this.replacePrimitiveFields;
        }

        @CanIgnoreReturnValue
        public MergeOptions setReplaceMessageFields(boolean value) {
            this.replaceMessageFields = value;
            return this;
        }

        @CanIgnoreReturnValue
        public MergeOptions setReplaceRepeatedFields(boolean value) {
            this.replaceRepeatedFields = value;
            return this;
        }

        @CanIgnoreReturnValue
        public MergeOptions setReplacePrimitiveFields(boolean value) {
            this.replacePrimitiveFields = value;
            return this;
        }
    }

    public static void merge(FieldMask mask, Message source, Message.Builder destination, MergeOptions options) {
        new FieldMaskTree(mask).merge(source, destination, options);
    }

    public static void merge(FieldMask mask, Message source, Message.Builder destination) {
        merge(mask, source, destination, new MergeOptions());
    }

    public static <P extends Message> P trim(FieldMask mask, P source) {
        Message.Builder destination = source.newBuilderForType();
        merge(mask, source, destination);
        return (P) destination.build();
    }
}
