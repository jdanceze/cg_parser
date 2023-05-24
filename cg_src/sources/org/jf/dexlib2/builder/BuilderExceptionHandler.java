package org.jf.dexlib2.builder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseExceptionHandler;
import org.jf.dexlib2.iface.reference.TypeReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/BuilderExceptionHandler.class */
public abstract class BuilderExceptionHandler extends BaseExceptionHandler {
    @Nonnull
    protected final Label handler;

    private BuilderExceptionHandler(@Nonnull Label handler) {
        this.handler = handler;
    }

    @Nonnull
    public Label getHandler() {
        return this.handler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BuilderExceptionHandler newExceptionHandler(@Nullable final TypeReference exceptionType, @Nonnull Label handler) {
        if (exceptionType == null) {
            return newExceptionHandler(handler);
        }
        return new BuilderExceptionHandler(handler) { // from class: org.jf.dexlib2.builder.BuilderExceptionHandler.1
            @Override // org.jf.dexlib2.iface.ExceptionHandler
            @Nullable
            public String getExceptionType() {
                return exceptionType.getType();
            }

            @Override // org.jf.dexlib2.iface.ExceptionHandler
            public int getHandlerCodeAddress() {
                return this.handler.getCodeAddress();
            }

            @Override // org.jf.dexlib2.base.BaseExceptionHandler, org.jf.dexlib2.iface.ExceptionHandler
            @Nullable
            public TypeReference getExceptionTypeReference() {
                return exceptionType;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BuilderExceptionHandler newExceptionHandler(@Nonnull Label handler) {
        return new BuilderExceptionHandler(handler) { // from class: org.jf.dexlib2.builder.BuilderExceptionHandler.2
            @Override // org.jf.dexlib2.iface.ExceptionHandler
            @Nullable
            public String getExceptionType() {
                return null;
            }

            @Override // org.jf.dexlib2.iface.ExceptionHandler
            public int getHandlerCodeAddress() {
                return this.handler.getCodeAddress();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BuilderExceptionHandler newExceptionHandler(@Nullable final String exceptionType, @Nonnull Label handler) {
        if (exceptionType == null) {
            return newExceptionHandler(handler);
        }
        return new BuilderExceptionHandler(handler) { // from class: org.jf.dexlib2.builder.BuilderExceptionHandler.3
            @Override // org.jf.dexlib2.iface.ExceptionHandler
            @Nullable
            public String getExceptionType() {
                return exceptionType;
            }

            @Override // org.jf.dexlib2.iface.ExceptionHandler
            public int getHandlerCodeAddress() {
                return this.handler.getCodeAddress();
            }
        };
    }
}
