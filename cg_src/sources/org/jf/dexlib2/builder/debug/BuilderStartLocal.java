package org.jf.dexlib2.builder.debug;

import javax.annotation.Nullable;
import org.jf.dexlib2.builder.BuilderDebugItem;
import org.jf.dexlib2.iface.debug.StartLocal;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/debug/BuilderStartLocal.class */
public class BuilderStartLocal extends BuilderDebugItem implements StartLocal {
    private final int register;
    @Nullable
    private final StringReference name;
    @Nullable
    private final TypeReference type;
    @Nullable
    private final StringReference signature;

    public BuilderStartLocal(int register, @Nullable StringReference name, @Nullable TypeReference type, @Nullable StringReference signature) {
        this.register = register;
        this.name = name;
        this.type = type;
        this.signature = signature;
    }

    @Override // org.jf.dexlib2.iface.debug.StartLocal
    public int getRegister() {
        return this.register;
    }

    @Override // org.jf.dexlib2.iface.debug.StartLocal
    @Nullable
    public StringReference getNameReference() {
        return this.name;
    }

    @Override // org.jf.dexlib2.iface.debug.StartLocal
    @Nullable
    public TypeReference getTypeReference() {
        return this.type;
    }

    @Override // org.jf.dexlib2.iface.debug.StartLocal
    @Nullable
    public StringReference getSignatureReference() {
        return this.signature;
    }

    @Override // org.jf.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getName() {
        if (this.name == null) {
            return null;
        }
        return this.name.getString();
    }

    @Override // org.jf.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getType() {
        if (this.type == null) {
            return null;
        }
        return this.type.getType();
    }

    @Override // org.jf.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getSignature() {
        if (this.signature == null) {
            return null;
        }
        return this.signature.getString();
    }

    @Override // org.jf.dexlib2.iface.debug.DebugItem
    public int getDebugItemType() {
        return 3;
    }
}
