package com.google.protobuf.util;

import com.google.common.base.Splitter;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.protobuf.Descriptors;
import com.google.protobuf.FieldMask;
import com.google.protobuf.Message;
import com.google.protobuf.util.FieldMaskUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;
/* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/FieldMaskTree.class */
final class FieldMaskTree {
    private static final Logger logger = Logger.getLogger(FieldMaskTree.class.getName());
    private static final String FIELD_PATH_SEPARATOR_REGEX = "\\.";
    private final Node root = new Node();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/FieldMaskTree$Node.class */
    public static final class Node {
        final SortedMap<String, Node> children;

        private Node() {
            this.children = new TreeMap();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FieldMaskTree() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FieldMaskTree(FieldMask mask) {
        mergeFromFieldMask(mask);
    }

    public String toString() {
        return FieldMaskUtil.toString(toFieldMask());
    }

    @CanIgnoreReturnValue
    FieldMaskTree addFieldPath(String path) {
        Node node;
        String[] parts = path.split(FIELD_PATH_SEPARATOR_REGEX);
        if (parts.length == 0) {
            return this;
        }
        Node node2 = this.root;
        boolean createNewBranch = false;
        for (String part : parts) {
            if (!createNewBranch && node2 != this.root && node2.children.isEmpty()) {
                return this;
            }
            if (node2.children.containsKey(part)) {
                node = node2.children.get(part);
            } else {
                createNewBranch = true;
                Node tmp = new Node();
                node2.children.put(part, tmp);
                node = tmp;
            }
            node2 = node;
        }
        node2.children.clear();
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @CanIgnoreReturnValue
    public FieldMaskTree mergeFromFieldMask(FieldMask mask) {
        for (String path : mask.getPathsList()) {
            addFieldPath(path);
        }
        return this;
    }

    @CanIgnoreReturnValue
    FieldMaskTree removeFieldPath(String path) {
        List<String> parts = Splitter.onPattern(FIELD_PATH_SEPARATOR_REGEX).splitToList(path);
        if (parts.isEmpty()) {
            return this;
        }
        removeFieldPath(this.root, parts, 0);
        return this;
    }

    @CanIgnoreReturnValue
    private static boolean removeFieldPath(Node node, List<String> parts, int index) {
        String key = parts.get(index);
        if (!node.children.containsKey(key)) {
            return false;
        }
        if (index == parts.size() - 1) {
            node.children.remove(key);
            return node.children.isEmpty();
        }
        if (removeFieldPath(node.children.get(key), parts, index + 1)) {
            node.children.remove(key);
        }
        return node.children.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @CanIgnoreReturnValue
    public FieldMaskTree removeFromFieldMask(FieldMask mask) {
        for (String path : mask.getPathsList()) {
            removeFieldPath(path);
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FieldMask toFieldMask() {
        if (this.root.children.isEmpty()) {
            return FieldMask.getDefaultInstance();
        }
        List<String> paths = new ArrayList<>();
        getFieldPaths(this.root, "", paths);
        return FieldMask.newBuilder().addAllPaths(paths).build();
    }

    private static void getFieldPaths(Node node, String path, List<String> paths) {
        if (node.children.isEmpty()) {
            paths.add(path);
            return;
        }
        for (Map.Entry<String, Node> entry : node.children.entrySet()) {
            String childPath = path.isEmpty() ? entry.getKey() : path + "." + entry.getKey();
            getFieldPaths(entry.getValue(), childPath, paths);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void intersectFieldPath(String path, FieldMaskTree output) {
        if (this.root.children.isEmpty()) {
            return;
        }
        String[] parts = path.split(FIELD_PATH_SEPARATOR_REGEX);
        if (parts.length == 0) {
            return;
        }
        Node node = this.root;
        for (String part : parts) {
            if (node != this.root && node.children.isEmpty()) {
                output.addFieldPath(path);
                return;
            } else if (node.children.containsKey(part)) {
                node = node.children.get(part);
            } else {
                return;
            }
        }
        List<String> paths = new ArrayList<>();
        getFieldPaths(node, path, paths);
        for (String value : paths) {
            output.addFieldPath(value);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void merge(Message source, Message.Builder destination, FieldMaskUtil.MergeOptions options) {
        if (source.getDescriptorForType() != destination.getDescriptorForType()) {
            throw new IllegalArgumentException("Cannot merge messages of different types.");
        }
        if (this.root.children.isEmpty()) {
            return;
        }
        merge(this.root, source, destination, options);
    }

    private static void merge(Node node, Message source, Message.Builder destination, FieldMaskUtil.MergeOptions options) {
        if (source.getDescriptorForType() != destination.getDescriptorForType()) {
            throw new IllegalArgumentException(String.format("source (%s) and destination (%s) descriptor must be equal", source.getDescriptorForType().getFullName(), destination.getDescriptorForType().getFullName()));
        }
        Descriptors.Descriptor descriptor = source.getDescriptorForType();
        for (Map.Entry<String, Node> entry : node.children.entrySet()) {
            Descriptors.FieldDescriptor field = descriptor.findFieldByName(entry.getKey());
            if (field == null) {
                logger.warning("Cannot find field \"" + entry.getKey() + "\" in message type " + descriptor.getFullName());
            } else if (!entry.getValue().children.isEmpty()) {
                if (field.isRepeated() || field.getJavaType() != Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                    logger.warning("Field \"" + field.getFullName() + "\" is not a singular message field and cannot have sub-fields.");
                } else if (source.hasField(field) || destination.hasField(field)) {
                    Message.Builder childBuilder = ((Message) destination.getField(field)).toBuilder();
                    merge(entry.getValue(), (Message) source.getField(field), childBuilder, options);
                    destination.setField(field, childBuilder.buildPartial());
                }
            } else if (field.isRepeated()) {
                if (options.replaceRepeatedFields()) {
                    destination.setField(field, source.getField(field));
                } else {
                    for (Object element : (List) source.getField(field)) {
                        destination.addRepeatedField(field, element);
                    }
                }
            } else if (field.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                if (options.replaceMessageFields()) {
                    if (!source.hasField(field)) {
                        destination.clearField(field);
                    } else {
                        destination.setField(field, source.getField(field));
                    }
                } else if (source.hasField(field)) {
                    destination.setField(field, ((Message) destination.getField(field)).toBuilder().mergeFrom((Message) source.getField(field)).build());
                }
            } else if (source.hasField(field) || !options.replacePrimitiveFields()) {
                destination.setField(field, source.getField(field));
            } else {
                destination.clearField(field);
            }
        }
    }
}
