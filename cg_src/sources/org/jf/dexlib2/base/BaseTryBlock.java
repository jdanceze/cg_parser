package org.jf.dexlib2.base;

import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.TryBlock;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/BaseTryBlock.class */
public abstract class BaseTryBlock<EH extends ExceptionHandler> implements TryBlock<EH> {
    @Override // org.jf.dexlib2.iface.TryBlock
    public boolean equals(Object o) {
        if (o instanceof TryBlock) {
            TryBlock other = (TryBlock) o;
            return getStartCodeAddress() == other.getStartCodeAddress() && getCodeUnitCount() == other.getCodeUnitCount() && getExceptionHandlers().equals(other.getExceptionHandlers());
        }
        return false;
    }
}
