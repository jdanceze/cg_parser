package org.powermock.api.mockito.mockpolicies;

import java.lang.reflect.Method;
import org.mockito.Mockito;
import org.powermock.core.spi.PowerMockPolicy;
import org.powermock.mockpolicies.MockPolicyClassLoadingSettings;
import org.powermock.mockpolicies.MockPolicyInterceptionSettings;
import org.powermock.mockpolicies.support.LogPolicySupport;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/mockpolicies/Slf4jMockPolicy.class */
public class Slf4jMockPolicy implements PowerMockPolicy {
    private static final String LOGGER_FACTORY_CLASS_NAME = "org.slf4j.LoggerFactory";
    private static final String LOGGER_FACTORY_METHOD_NAME = "getLogger";
    private static final String FRAMEWORK_NAME = "sfl4j";
    private static final String LOGGER_CLASS_NAME = "org.slf4j.Logger";
    private static ThreadLocal<Object> threadLogger = new ThreadLocal<>();

    @Override // org.powermock.core.spi.PowerMockPolicy
    public void applyClassLoadingPolicy(MockPolicyClassLoadingSettings mockPolicyClassLoadingSettings) {
        mockPolicyClassLoadingSettings.addFullyQualifiedNamesOfClassesToLoadByMockClassloader(LOGGER_FACTORY_CLASS_NAME, "org.apache.log4j.Appender", "org.apache.log4j.xml.DOMConfigurator");
    }

    @Override // org.powermock.core.spi.PowerMockPolicy
    public void applyInterceptionPolicy(MockPolicyInterceptionSettings mockPolicyInterceptionSettings) {
        LogPolicySupport logPolicySupport = new LogPolicySupport();
        Method[] loggerFactoryMethods = logPolicySupport.getLoggerMethods(LOGGER_FACTORY_CLASS_NAME, LOGGER_FACTORY_METHOD_NAME, FRAMEWORK_NAME);
        initializeMockForThread(logPolicySupport);
        for (Method loggerFactoryMethod : loggerFactoryMethods) {
            mockPolicyInterceptionSettings.stubMethod(loggerFactoryMethod, threadLogger.get());
        }
    }

    private void initializeMockForThread(LogPolicySupport logPolicySupport) {
        Class<?> loggerClass = getLoggerClass(logPolicySupport);
        if (threadLogger.get() == null) {
            ClassLoader originalCl = Thread.currentThread().getContextClassLoader();
            ClassLoader classLoader = Mockito.class.getClassLoader();
            Thread.currentThread().setContextClassLoader(classLoader);
            try {
                Object mock = Mockito.mock(loggerClass);
                Thread.currentThread().setContextClassLoader(originalCl);
                threadLogger.set(mock);
            } catch (Throwable th) {
                Thread.currentThread().setContextClassLoader(originalCl);
                throw th;
            }
        }
    }

    private Class<?> getLoggerClass(LogPolicySupport logPolicySupport) {
        try {
            Class<?> loggerType = logPolicySupport.getType(LOGGER_CLASS_NAME, FRAMEWORK_NAME);
            return loggerType;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }
}
