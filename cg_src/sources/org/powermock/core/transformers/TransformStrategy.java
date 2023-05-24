package org.powermock.core.transformers;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/TransformStrategy.class */
public enum TransformStrategy {
    CLASSLOADER { // from class: org.powermock.core.transformers.TransformStrategy.1
        @Override // org.powermock.core.transformers.TransformStrategy
        public boolean isClassloaderMode() {
            return true;
        }

        @Override // org.powermock.core.transformers.TransformStrategy
        public boolean isAgentMode() {
            return false;
        }
    },
    INST_REDEFINE { // from class: org.powermock.core.transformers.TransformStrategy.2
        @Override // org.powermock.core.transformers.TransformStrategy
        public boolean isClassloaderMode() {
            return false;
        }

        @Override // org.powermock.core.transformers.TransformStrategy
        public boolean isAgentMode() {
            return true;
        }
    };

    public abstract boolean isClassloaderMode();

    public abstract boolean isAgentMode();
}
