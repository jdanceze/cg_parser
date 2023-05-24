package org.mockito.internal.util.reflection;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/FieldInitializationReport.class */
public class FieldInitializationReport {
    private final Object fieldInstance;
    private final boolean wasInitialized;
    private final boolean wasInitializedUsingConstructorArgs;

    public FieldInitializationReport(Object fieldInstance, boolean wasInitialized, boolean wasInitializedUsingConstructorArgs) {
        this.fieldInstance = fieldInstance;
        this.wasInitialized = wasInitialized;
        this.wasInitializedUsingConstructorArgs = wasInitializedUsingConstructorArgs;
    }

    public Object fieldInstance() {
        return this.fieldInstance;
    }

    public boolean fieldWasInitialized() {
        return this.wasInitialized;
    }

    public boolean fieldWasInitializedUsingContructorArgs() {
        return this.wasInitializedUsingConstructorArgs;
    }

    public Class<?> fieldClass() {
        if (this.fieldInstance != null) {
            return this.fieldInstance.getClass();
        }
        return null;
    }
}
