package org.jf.dexlib2.immutable.debug;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseStringReference;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.iface.debug.StartLocal;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/debug/ImmutableStartLocal.class */
public class ImmutableStartLocal extends ImmutableDebugItem implements StartLocal {
    protected final int register;
    @Nullable
    protected final String name;
    @Nullable
    protected final String type;
    @Nullable
    protected final String signature;

    public ImmutableStartLocal(int codeAddress, int register, @Nullable String name, @Nullable String type, @Nullable String signature) {
        super(codeAddress);
        this.register = register;
        this.name = name;
        this.type = type;
        this.signature = signature;
    }

    @Nonnull
    public static ImmutableStartLocal of(@Nonnull StartLocal startLocal) {
        if (startLocal instanceof ImmutableStartLocal) {
            return (ImmutableStartLocal) startLocal;
        }
        return new ImmutableStartLocal(startLocal.getCodeAddress(), startLocal.getRegister(), startLocal.getName(), startLocal.getType(), startLocal.getSignature());
    }

    @Override // org.jf.dexlib2.iface.debug.StartLocal
    public int getRegister() {
        return this.register;
    }

    @Override // org.jf.dexlib2.iface.debug.StartLocal
    @Nullable
    public StringReference getNameReference() {
        if (this.name == null) {
            return null;
        }
        return new BaseStringReference() { // from class: org.jf.dexlib2.immutable.debug.ImmutableStartLocal.1
            @Override // org.jf.dexlib2.iface.reference.StringReference
            @Nonnull
            public String getString() {
                return ImmutableStartLocal.this.name;
            }
        };
    }

    @Override // org.jf.dexlib2.iface.debug.StartLocal
    @Nullable
    public TypeReference getTypeReference() {
        if (this.type == null) {
            return null;
        }
        return new BaseTypeReference() { // from class: org.jf.dexlib2.immutable.debug.ImmutableStartLocal.2
            @Override // org.jf.dexlib2.iface.reference.TypeReference, org.jf.dexlib2.iface.ClassDef
            @Nonnull
            public String getType() {
                return ImmutableStartLocal.this.type;
            }
        };
    }

    @Override // org.jf.dexlib2.iface.debug.StartLocal
    @Nullable
    public StringReference getSignatureReference() {
        if (this.signature == null) {
            return null;
        }
        return new BaseStringReference() { // from class: org.jf.dexlib2.immutable.debug.ImmutableStartLocal.3
            @Override // org.jf.dexlib2.iface.reference.StringReference
            @Nonnull
            public String getString() {
                return ImmutableStartLocal.this.signature;
            }
        };
    }

    @Override // org.jf.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getName() {
        return this.name;
    }

    @Override // org.jf.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getType() {
        return this.type;
    }

    @Override // org.jf.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getSignature() {
        return this.signature;
    }

    @Override // org.jf.dexlib2.iface.debug.DebugItem
    public int getDebugItemType() {
        return 3;
    }
}
