package org.powermock.api.extension.agent;

import org.powermock.api.mockito.internal.mockcreation.MockCreator;
import org.powermock.core.agent.JavaAgentClassRegister;
import org.powermock.core.agent.JavaAgentFrameworkRegister;
import org.powermock.reflect.Whitebox;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/extension/agent/JavaAgentFrameworkRegisterImpl.class */
public class JavaAgentFrameworkRegisterImpl implements JavaAgentFrameworkRegister {
    public static final String MOCK_CREATOR_IMPLEMENTATION_CLASS = "org.powermock.api.mockito.internal.mockcreation.DefaultMockCreator";
    private MockCreator mockCreator;

    @Override // org.powermock.core.agent.JavaAgentFrameworkRegister
    public void set(JavaAgentClassRegister javaAgentClassRegister) {
        setToPowerMockito(javaAgentClassRegister);
    }

    private void setToPowerMockito(JavaAgentClassRegister javaAgentClassRegister) {
        this.mockCreator = getPowerMockCoreForCurrentClassLoader();
        Whitebox.setInternalState(this.mockCreator, "agentClassRegister", javaAgentClassRegister);
    }

    private MockCreator getPowerMockCoreForCurrentClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            return (MockCreator) Whitebox.getInternalState(classLoader.loadClass(MOCK_CREATOR_IMPLEMENTATION_CLASS), "MOCK_CREATOR");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // org.powermock.core.agent.JavaAgentFrameworkRegister
    public void clear() {
        if (this.mockCreator == null) {
            throw new IllegalStateException("Cannot clear JavaAgentClassRegister. Set method has not been called.");
        }
        Whitebox.setInternalState(this.mockCreator, "agentClassRegister", (Object) null);
    }
}
