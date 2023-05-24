package org.jf.dexlib2.writer.util;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseArrayEncodedValue;
import org.jf.dexlib2.base.value.BaseMethodHandleEncodedValue;
import org.jf.dexlib2.base.value.BaseMethodTypeEncodedValue;
import org.jf.dexlib2.iface.reference.CallSiteReference;
import org.jf.dexlib2.iface.reference.MethodHandleReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableStringEncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/util/CallSiteUtil.class */
public class CallSiteUtil {
    public static ArrayEncodedValue getEncodedCallSite(final CallSiteReference callSiteReference) {
        return new BaseArrayEncodedValue() { // from class: org.jf.dexlib2.writer.util.CallSiteUtil.1
            @Override // org.jf.dexlib2.iface.value.ArrayEncodedValue
            @Nonnull
            public List<? extends EncodedValue> getValue() {
                List<EncodedValue> encodedCallSite = Lists.newArrayList();
                encodedCallSite.add(new BaseMethodHandleEncodedValue() { // from class: org.jf.dexlib2.writer.util.CallSiteUtil.1.1
                    @Override // org.jf.dexlib2.iface.value.MethodHandleEncodedValue
                    @Nonnull
                    public MethodHandleReference getValue() {
                        return CallSiteReference.this.getMethodHandle();
                    }
                });
                encodedCallSite.add(new ImmutableStringEncodedValue(CallSiteReference.this.getMethodName()));
                encodedCallSite.add(new BaseMethodTypeEncodedValue() { // from class: org.jf.dexlib2.writer.util.CallSiteUtil.1.2
                    @Override // org.jf.dexlib2.iface.value.MethodTypeEncodedValue
                    @Nonnull
                    public MethodProtoReference getValue() {
                        return CallSiteReference.this.getMethodProto();
                    }
                });
                encodedCallSite.addAll(CallSiteReference.this.getExtraArguments());
                return encodedCallSite;
            }
        };
    }
}
