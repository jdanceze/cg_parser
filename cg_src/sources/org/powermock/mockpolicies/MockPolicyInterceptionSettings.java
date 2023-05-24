package org.powermock.mockpolicies;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/mockpolicies/MockPolicyInterceptionSettings.class */
public interface MockPolicyInterceptionSettings {
    void setMethodsToSuppress(Method[] methodArr);

    void addMethodsToSuppress(Method method, Method... methodArr);

    void addMethodsToSuppress(Method[] methodArr);

    void setMethodsToStub(Map<Method, Object> map);

    void stubMethod(Method method, Object obj);

    void proxyMethod(Method method, InvocationHandler invocationHandler);

    Map<Method, InvocationHandler> getProxiedMethods();

    void setMethodsToProxy(Map<Method, InvocationHandler> map);

    @Deprecated
    void setSubtituteReturnValues(Map<Method, Object> map);

    @Deprecated
    void addSubtituteReturnValue(Method method, Object obj);

    void setFieldsSuppress(Field[] fieldArr);

    void addFieldToSuppress(Field field, Field... fieldArr);

    void addFieldToSuppress(Field[] fieldArr);

    void setFieldTypesToSuppress(String[] strArr);

    void addFieldTypesToSuppress(String str, String... strArr);

    void addFieldTypesToSuppress(String[] strArr);

    Method[] getMethodsToSuppress();

    Map<Method, Object> getStubbedMethods();

    @Deprecated
    Map<Method, Object> getSubstituteReturnValues();

    Field[] getFieldsToSuppress();

    String[] getFieldTypesToSuppress();
}
