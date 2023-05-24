package com.google.protobuf;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.AbstractMessage.Builder;
import com.google.protobuf.MessageOrBuilder;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/RepeatedFieldBuilderV3.class */
public class RepeatedFieldBuilderV3<MType extends AbstractMessage, BType extends AbstractMessage.Builder, IType extends MessageOrBuilder> implements AbstractMessage.BuilderParent {
    private AbstractMessage.BuilderParent parent;
    private List<MType> messages;
    private boolean isMessagesListMutable;
    private List<SingleFieldBuilderV3<MType, BType, IType>> builders;
    private boolean isClean;
    private MessageExternalList<MType, BType, IType> externalMessageList;
    private BuilderExternalList<MType, BType, IType> externalBuilderList;
    private MessageOrBuilderExternalList<MType, BType, IType> externalMessageOrBuilderList;

    public RepeatedFieldBuilderV3(List<MType> messages, boolean isMessagesListMutable, AbstractMessage.BuilderParent parent, boolean isClean) {
        this.messages = messages;
        this.isMessagesListMutable = isMessagesListMutable;
        this.parent = parent;
        this.isClean = isClean;
    }

    public void dispose() {
        this.parent = null;
    }

    private void ensureMutableMessageList() {
        if (!this.isMessagesListMutable) {
            this.messages = new ArrayList(this.messages);
            this.isMessagesListMutable = true;
        }
    }

    private void ensureBuilders() {
        if (this.builders == null) {
            this.builders = new ArrayList(this.messages.size());
            for (int i = 0; i < this.messages.size(); i++) {
                this.builders.add(null);
            }
        }
    }

    public int getCount() {
        return this.messages.size();
    }

    public boolean isEmpty() {
        return this.messages.isEmpty();
    }

    public MType getMessage(int index) {
        return getMessage(index, false);
    }

    private MType getMessage(int index, boolean forBuild) {
        if (this.builders == null) {
            return this.messages.get(index);
        }
        SingleFieldBuilderV3<MType, BType, IType> builder = this.builders.get(index);
        if (builder == null) {
            return this.messages.get(index);
        }
        return forBuild ? builder.build() : builder.getMessage();
    }

    public BType getBuilder(int index) {
        ensureBuilders();
        SingleFieldBuilderV3<MType, BType, IType> builder = this.builders.get(index);
        if (builder == null) {
            MType message = this.messages.get(index);
            builder = new SingleFieldBuilderV3<>(message, this, this.isClean);
            this.builders.set(index, builder);
        }
        return builder.getBuilder();
    }

    /* JADX WARN: Type inference failed for: r0v12, types: [com.google.protobuf.MessageOrBuilder, IType extends com.google.protobuf.MessageOrBuilder] */
    /* JADX WARN: Type inference failed for: r0v16, types: [com.google.protobuf.MessageOrBuilder, IType extends com.google.protobuf.MessageOrBuilder] */
    public IType getMessageOrBuilder(int index) {
        if (this.builders == null) {
            return this.messages.get(index);
        }
        SingleFieldBuilderV3<MType, BType, IType> builder = this.builders.get(index);
        if (builder == null) {
            return this.messages.get(index);
        }
        return builder.getMessageOrBuilder();
    }

    public RepeatedFieldBuilderV3<MType, BType, IType> setMessage(int index, MType message) {
        SingleFieldBuilderV3<MType, BType, IType> entry;
        Internal.checkNotNull(message);
        ensureMutableMessageList();
        this.messages.set(index, message);
        if (this.builders != null && (entry = this.builders.set(index, null)) != null) {
            entry.dispose();
        }
        onChanged();
        incrementModCounts();
        return this;
    }

    public RepeatedFieldBuilderV3<MType, BType, IType> addMessage(MType message) {
        Internal.checkNotNull(message);
        ensureMutableMessageList();
        this.messages.add(message);
        if (this.builders != null) {
            this.builders.add(null);
        }
        onChanged();
        incrementModCounts();
        return this;
    }

    public RepeatedFieldBuilderV3<MType, BType, IType> addMessage(int index, MType message) {
        Internal.checkNotNull(message);
        ensureMutableMessageList();
        this.messages.add(index, message);
        if (this.builders != null) {
            this.builders.add(index, null);
        }
        onChanged();
        incrementModCounts();
        return this;
    }

    public RepeatedFieldBuilderV3<MType, BType, IType> addAllMessages(Iterable<? extends MType> values) {
        for (MType value : values) {
            Internal.checkNotNull(value);
        }
        int size = -1;
        if (values instanceof Collection) {
            Collection<?> collection = (Collection) values;
            if (collection.isEmpty()) {
                return this;
            }
            size = collection.size();
        }
        ensureMutableMessageList();
        if (size >= 0 && (this.messages instanceof ArrayList)) {
            ((ArrayList) this.messages).ensureCapacity(this.messages.size() + size);
        }
        for (MType value2 : values) {
            addMessage(value2);
        }
        onChanged();
        incrementModCounts();
        return this;
    }

    public BType addBuilder(MType message) {
        ensureMutableMessageList();
        ensureBuilders();
        SingleFieldBuilderV3<MType, BType, IType> builder = new SingleFieldBuilderV3<>(message, this, this.isClean);
        this.messages.add(null);
        this.builders.add(builder);
        onChanged();
        incrementModCounts();
        return builder.getBuilder();
    }

    public BType addBuilder(int index, MType message) {
        ensureMutableMessageList();
        ensureBuilders();
        SingleFieldBuilderV3<MType, BType, IType> builder = new SingleFieldBuilderV3<>(message, this, this.isClean);
        this.messages.add(index, null);
        this.builders.add(index, builder);
        onChanged();
        incrementModCounts();
        return builder.getBuilder();
    }

    public void remove(int index) {
        SingleFieldBuilderV3<MType, BType, IType> entry;
        ensureMutableMessageList();
        this.messages.remove(index);
        if (this.builders != null && (entry = this.builders.remove(index)) != null) {
            entry.dispose();
        }
        onChanged();
        incrementModCounts();
    }

    public void clear() {
        this.messages = Collections.emptyList();
        this.isMessagesListMutable = false;
        if (this.builders != null) {
            for (SingleFieldBuilderV3<MType, BType, IType> entry : this.builders) {
                if (entry != null) {
                    entry.dispose();
                }
            }
            this.builders = null;
        }
        onChanged();
        incrementModCounts();
    }

    public List<MType> build() {
        this.isClean = true;
        if (!this.isMessagesListMutable && this.builders == null) {
            return this.messages;
        }
        boolean allMessagesInSync = true;
        if (!this.isMessagesListMutable) {
            int i = 0;
            while (true) {
                if (i >= this.messages.size()) {
                    break;
                }
                Message message = this.messages.get(i);
                SingleFieldBuilderV3<MType, BType, IType> builder = this.builders.get(i);
                if (builder == null || builder.build() == message) {
                    i++;
                } else {
                    allMessagesInSync = false;
                    break;
                }
            }
            if (allMessagesInSync) {
                return this.messages;
            }
        }
        ensureMutableMessageList();
        for (int i2 = 0; i2 < this.messages.size(); i2++) {
            this.messages.set(i2, getMessage(i2, true));
        }
        this.messages = Collections.unmodifiableList(this.messages);
        this.isMessagesListMutable = false;
        return this.messages;
    }

    public List<MType> getMessageList() {
        if (this.externalMessageList == null) {
            this.externalMessageList = new MessageExternalList<>(this);
        }
        return this.externalMessageList;
    }

    public List<BType> getBuilderList() {
        if (this.externalBuilderList == null) {
            this.externalBuilderList = new BuilderExternalList<>(this);
        }
        return this.externalBuilderList;
    }

    public List<IType> getMessageOrBuilderList() {
        if (this.externalMessageOrBuilderList == null) {
            this.externalMessageOrBuilderList = new MessageOrBuilderExternalList<>(this);
        }
        return this.externalMessageOrBuilderList;
    }

    private void onChanged() {
        if (this.isClean && this.parent != null) {
            this.parent.markDirty();
            this.isClean = false;
        }
    }

    @Override // com.google.protobuf.AbstractMessage.BuilderParent
    public void markDirty() {
        onChanged();
    }

    private void incrementModCounts() {
        if (this.externalMessageList != null) {
            this.externalMessageList.incrementModCount();
        }
        if (this.externalBuilderList != null) {
            this.externalBuilderList.incrementModCount();
        }
        if (this.externalMessageOrBuilderList != null) {
            this.externalMessageOrBuilderList.incrementModCount();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/RepeatedFieldBuilderV3$MessageExternalList.class */
    public static class MessageExternalList<MType extends AbstractMessage, BType extends AbstractMessage.Builder, IType extends MessageOrBuilder> extends AbstractList<MType> implements List<MType>, RandomAccess {
        RepeatedFieldBuilderV3<MType, BType, IType> builder;

        MessageExternalList(RepeatedFieldBuilderV3<MType, BType, IType> builder) {
            this.builder = builder;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.builder.getCount();
        }

        @Override // java.util.AbstractList, java.util.List
        public MType get(int index) {
            return this.builder.getMessage(index);
        }

        void incrementModCount() {
            this.modCount++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/RepeatedFieldBuilderV3$BuilderExternalList.class */
    public static class BuilderExternalList<MType extends AbstractMessage, BType extends AbstractMessage.Builder, IType extends MessageOrBuilder> extends AbstractList<BType> implements List<BType>, RandomAccess {
        RepeatedFieldBuilderV3<MType, BType, IType> builder;

        BuilderExternalList(RepeatedFieldBuilderV3<MType, BType, IType> builder) {
            this.builder = builder;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.builder.getCount();
        }

        @Override // java.util.AbstractList, java.util.List
        public BType get(int index) {
            return this.builder.getBuilder(index);
        }

        void incrementModCount() {
            this.modCount++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/RepeatedFieldBuilderV3$MessageOrBuilderExternalList.class */
    public static class MessageOrBuilderExternalList<MType extends AbstractMessage, BType extends AbstractMessage.Builder, IType extends MessageOrBuilder> extends AbstractList<IType> implements List<IType>, RandomAccess {
        RepeatedFieldBuilderV3<MType, BType, IType> builder;

        MessageOrBuilderExternalList(RepeatedFieldBuilderV3<MType, BType, IType> builder) {
            this.builder = builder;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.builder.getCount();
        }

        @Override // java.util.AbstractList, java.util.List
        public IType get(int index) {
            return this.builder.getMessageOrBuilder(index);
        }

        void incrementModCount() {
            this.modCount++;
        }
    }
}
