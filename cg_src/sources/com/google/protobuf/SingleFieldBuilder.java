package com.google.protobuf;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.GeneratedMessage.Builder;
import com.google.protobuf.MessageOrBuilder;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/SingleFieldBuilder.class */
public class SingleFieldBuilder<MType extends GeneratedMessage, BType extends GeneratedMessage.Builder, IType extends MessageOrBuilder> implements GeneratedMessage.BuilderParent {
    private GeneratedMessage.BuilderParent parent;
    private BType builder;
    private MType message;
    private boolean isClean;

    public SingleFieldBuilder(MType message, GeneratedMessage.BuilderParent parent, boolean isClean) {
        this.message = (MType) Internal.checkNotNull(message);
        this.parent = parent;
        this.isClean = isClean;
    }

    public void dispose() {
        this.parent = null;
    }

    public MType getMessage() {
        if (this.message == null) {
            this.message = (MType) this.builder.buildPartial();
        }
        return this.message;
    }

    public MType build() {
        this.isClean = true;
        return getMessage();
    }

    public BType getBuilder() {
        if (this.builder == null) {
            this.builder = (BType) this.message.newBuilderForType(this);
            this.builder.mergeFrom(this.message);
            this.builder.markClean();
        }
        return this.builder;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [MType extends com.google.protobuf.GeneratedMessage, IType extends com.google.protobuf.MessageOrBuilder] */
    /* JADX WARN: Type inference failed for: r0v5, types: [BType extends com.google.protobuf.GeneratedMessage$Builder, IType extends com.google.protobuf.MessageOrBuilder] */
    public IType getMessageOrBuilder() {
        if (this.builder != null) {
            return this.builder;
        }
        return this.message;
    }

    public SingleFieldBuilder<MType, BType, IType> setMessage(MType message) {
        this.message = (MType) Internal.checkNotNull(message);
        if (this.builder != null) {
            this.builder.dispose();
            this.builder = null;
        }
        onChanged();
        return this;
    }

    public SingleFieldBuilder<MType, BType, IType> mergeFrom(MType value) {
        if (this.builder == null && this.message == this.message.getDefaultInstanceForType()) {
            this.message = value;
        } else {
            getBuilder().mergeFrom(value);
        }
        onChanged();
        return this;
    }

    public SingleFieldBuilder<MType, BType, IType> clear() {
        MessageLite defaultInstanceForType;
        if (this.message != null) {
            defaultInstanceForType = this.message.getDefaultInstanceForType();
        } else {
            defaultInstanceForType = this.builder.getDefaultInstanceForType();
        }
        this.message = (MType) ((GeneratedMessage) defaultInstanceForType);
        if (this.builder != null) {
            this.builder.dispose();
            this.builder = null;
        }
        onChanged();
        return this;
    }

    private void onChanged() {
        if (this.builder != null) {
            this.message = null;
        }
        if (this.isClean && this.parent != null) {
            this.parent.markDirty();
            this.isClean = false;
        }
    }

    @Override // com.google.protobuf.AbstractMessage.BuilderParent
    public void markDirty() {
        onChanged();
    }
}
