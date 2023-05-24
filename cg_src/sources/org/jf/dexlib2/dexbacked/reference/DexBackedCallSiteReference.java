package org.jf.dexlib2.dexbacked.reference;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseCallSiteReference;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.util.EncodedArrayItemIterator;
import org.jf.dexlib2.iface.reference.MethodHandleReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.MethodHandleEncodedValue;
import org.jf.dexlib2.iface.value.MethodTypeEncodedValue;
import org.jf.dexlib2.iface.value.StringEncodedValue;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/reference/DexBackedCallSiteReference.class */
public class DexBackedCallSiteReference extends BaseCallSiteReference {
    @Nonnull
    public final DexBackedDexFile dexFile;
    public final int callSiteIndex;
    public final int callSiteIdOffset;
    private int callSiteOffset = -1;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DexBackedCallSiteReference.class.desiredAssertionStatus();
    }

    public DexBackedCallSiteReference(DexBackedDexFile dexFile, int callSiteIndex) {
        this.dexFile = dexFile;
        this.callSiteIndex = callSiteIndex;
        this.callSiteIdOffset = dexFile.getCallSiteSection().getOffset(callSiteIndex);
    }

    @Override // org.jf.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public String getName() {
        return String.format("call_site_%d", Integer.valueOf(this.callSiteIndex));
    }

    @Override // org.jf.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public MethodHandleReference getMethodHandle() {
        EncodedArrayItemIterator iter = getCallSiteIterator();
        if (iter.getItemCount() < 3) {
            throw new ExceptionWithContext("Invalid call site item: must contain at least 3 entries.", new Object[0]);
        }
        EncodedValue encodedValue = getCallSiteIterator().getNextOrNull();
        if ($assertionsDisabled || encodedValue != null) {
            if (encodedValue.getValueType() != 22) {
                throw new ExceptionWithContext("Invalid encoded value type (%d) for the first item in call site %d", Integer.valueOf(encodedValue.getValueType()), Integer.valueOf(this.callSiteIndex));
            }
            return ((MethodHandleEncodedValue) encodedValue).getValue();
        }
        throw new AssertionError();
    }

    @Override // org.jf.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public String getMethodName() {
        EncodedArrayItemIterator iter = getCallSiteIterator();
        if (iter.getItemCount() < 3) {
            throw new ExceptionWithContext("Invalid call site item: must contain at least 3 entries.", new Object[0]);
        }
        iter.skipNext();
        EncodedValue encodedValue = iter.getNextOrNull();
        if ($assertionsDisabled || encodedValue != null) {
            if (encodedValue.getValueType() != 23) {
                throw new ExceptionWithContext("Invalid encoded value type (%d) for the second item in call site %d", Integer.valueOf(encodedValue.getValueType()), Integer.valueOf(this.callSiteIndex));
            }
            return ((StringEncodedValue) encodedValue).getValue();
        }
        throw new AssertionError();
    }

    @Override // org.jf.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public MethodProtoReference getMethodProto() {
        EncodedArrayItemIterator iter = getCallSiteIterator();
        if (iter.getItemCount() < 3) {
            throw new ExceptionWithContext("Invalid call site item: must contain at least 3 entries.", new Object[0]);
        }
        iter.skipNext();
        iter.skipNext();
        EncodedValue encodedValue = iter.getNextOrNull();
        if ($assertionsDisabled || encodedValue != null) {
            if (encodedValue.getValueType() != 21) {
                throw new ExceptionWithContext("Invalid encoded value type (%d) for the second item in call site %d", Integer.valueOf(encodedValue.getValueType()), Integer.valueOf(this.callSiteIndex));
            }
            return ((MethodTypeEncodedValue) encodedValue).getValue();
        }
        throw new AssertionError();
    }

    @Override // org.jf.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public List<? extends EncodedValue> getExtraArguments() {
        List<EncodedValue> values = Lists.newArrayList();
        EncodedArrayItemIterator iter = getCallSiteIterator();
        if (iter.getItemCount() < 3) {
            throw new ExceptionWithContext("Invalid call site item: must contain at least 3 entries.", new Object[0]);
        }
        if (iter.getItemCount() == 3) {
            return values;
        }
        iter.skipNext();
        iter.skipNext();
        iter.skipNext();
        EncodedValue nextOrNull = iter.getNextOrNull();
        while (true) {
            EncodedValue item = nextOrNull;
            if (item != null) {
                values.add(item);
                nextOrNull = iter.getNextOrNull();
            } else {
                return values;
            }
        }
    }

    private EncodedArrayItemIterator getCallSiteIterator() {
        return EncodedArrayItemIterator.newOrEmpty(this.dexFile, getCallSiteOffset());
    }

    private int getCallSiteOffset() {
        if (this.callSiteOffset < 0) {
            this.callSiteOffset = this.dexFile.getBuffer().readSmallUint(this.callSiteIdOffset);
        }
        return this.callSiteOffset;
    }

    @Override // org.jf.dexlib2.base.reference.BaseReference, org.jf.dexlib2.iface.reference.Reference
    public void validateReference() throws Reference.InvalidReferenceException {
        if (this.callSiteIndex < 0 || this.callSiteIndex >= this.dexFile.getCallSiteSection().size()) {
            throw new Reference.InvalidReferenceException("callsite@" + this.callSiteIndex);
        }
    }
}
