package org.powermock.core.agent;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/agent/JavaAgentClassRegister.class */
public interface JavaAgentClassRegister {
    boolean isModifiedByAgent(ClassLoader classLoader, String str);

    void registerClass(ClassLoader classLoader, String str);

    void clear();
}
