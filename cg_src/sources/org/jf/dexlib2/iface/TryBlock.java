package org.jf.dexlib2.iface;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.ExceptionHandler;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/TryBlock.class */
public interface TryBlock<EH extends ExceptionHandler> {
    int getStartCodeAddress();

    int getCodeUnitCount();

    @Nonnull
    List<? extends EH> getExceptionHandlers();

    boolean equals(@Nullable Object obj);
}
