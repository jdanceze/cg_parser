package org.jf.dexlib2.writer.builder;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseCallSiteReference;
import org.jf.dexlib2.iface.value.StringEncodedValue;
import org.jf.dexlib2.writer.builder.BuilderEncodedValues;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderCallSiteReference.class */
public class BuilderCallSiteReference extends BaseCallSiteReference implements BuilderReference {
    @Nonnull
    final String name;
    @Nonnull
    final BuilderEncodedValues.BuilderArrayEncodedValue encodedCallSite;
    int index = -1;

    public BuilderCallSiteReference(@Nonnull String name, @Nonnull BuilderEncodedValues.BuilderArrayEncodedValue encodedCallSite) {
        this.name = name;
        this.encodedCallSite = encodedCallSite;
    }

    @Override // org.jf.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public String getName() {
        return this.name;
    }

    @Override // org.jf.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public BuilderMethodHandleReference getMethodHandle() {
        return ((BuilderEncodedValues.BuilderMethodHandleEncodedValue) this.encodedCallSite.elements.get(0)).getValue();
    }

    @Override // org.jf.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public String getMethodName() {
        return ((StringEncodedValue) this.encodedCallSite.elements.get(1)).getValue();
    }

    @Override // org.jf.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public BuilderMethodProtoReference getMethodProto() {
        return ((BuilderEncodedValues.BuilderMethodTypeEncodedValue) this.encodedCallSite.elements.get(2)).getValue();
    }

    @Override // org.jf.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public List<? extends BuilderEncodedValues.BuilderEncodedValue> getExtraArguments() {
        if (this.encodedCallSite.elements.size() <= 3) {
            return ImmutableList.of();
        }
        return this.encodedCallSite.elements.subList(3, this.encodedCallSite.elements.size());
    }

    @Override // org.jf.dexlib2.writer.builder.BuilderReference
    public int getIndex() {
        return this.index;
    }

    @Override // org.jf.dexlib2.writer.builder.BuilderReference
    public void setIndex(int index) {
        this.index = index;
    }
}
