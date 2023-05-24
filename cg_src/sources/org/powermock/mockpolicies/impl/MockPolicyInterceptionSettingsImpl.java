package org.powermock.mockpolicies.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.powermock.mockpolicies.MockPolicyInterceptionSettings;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/mockpolicies/impl/MockPolicyInterceptionSettingsImpl.class */
public class MockPolicyInterceptionSettingsImpl implements MockPolicyInterceptionSettings {
    private Set<Field> fieldsToSuppress = new LinkedHashSet();
    private Set<Method> methodsToSuppress = new LinkedHashSet();
    private Map<Method, Object> substituteReturnValues = new HashMap();
    private Map<Method, InvocationHandler> proxies = new HashMap();
    private Set<String> fieldsTypesToSuppress = new LinkedHashSet();

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public void addFieldTypesToSuppress(String firstType, String... additionalFieldTypes) {
        this.fieldsTypesToSuppress.add(firstType);
        addFieldTypesToSuppress(additionalFieldTypes);
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public void addFieldTypesToSuppress(String[] fieldTypes) {
        Collections.addAll(this.fieldsTypesToSuppress, fieldTypes);
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public void setFieldTypesToSuppress(String[] fieldTypes) {
        this.fieldsTypesToSuppress.clear();
        addFieldTypesToSuppress(fieldTypes);
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public Field[] getFieldsToSuppress() {
        return (Field[]) this.fieldsToSuppress.toArray(new Field[this.fieldsToSuppress.size()]);
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public Method[] getMethodsToSuppress() {
        return (Method[]) this.methodsToSuppress.toArray(new Method[this.methodsToSuppress.size()]);
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public Map<Method, Object> getStubbedMethods() {
        return Collections.unmodifiableMap(this.substituteReturnValues);
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public void addFieldToSuppress(Field firstField, Field... fields) {
        this.fieldsToSuppress.add(firstField);
        addFieldToSuppress(fields);
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public void addFieldToSuppress(Field[] fields) {
        Collections.addAll(this.fieldsToSuppress, fields);
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public void addMethodsToSuppress(Method methodToSuppress, Method... additionalMethodsToSuppress) {
        this.methodsToSuppress.add(methodToSuppress);
        addMethodsToSuppress(additionalMethodsToSuppress);
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public void addMethodsToSuppress(Method[] methods) {
        Collections.addAll(this.methodsToSuppress, methods);
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public void stubMethod(Method method, Object returnObject) {
        this.substituteReturnValues.put(method, returnObject);
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public void setFieldsSuppress(Field[] fields) {
        this.fieldsToSuppress.clear();
        addFieldToSuppress(fields);
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public void setMethodsToSuppress(Method[] methods) {
        this.methodsToSuppress.clear();
        addMethodsToSuppress(methods);
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public void setMethodsToStub(Map<Method, Object> substituteReturnValues) {
        this.substituteReturnValues = substituteReturnValues;
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public String[] getFieldTypesToSuppress() {
        return (String[]) this.fieldsTypesToSuppress.toArray(new String[this.fieldsTypesToSuppress.size()]);
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public void addSubtituteReturnValue(Method method, Object returnObject) {
        this.substituteReturnValues.put(method, returnObject);
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public void setSubtituteReturnValues(Map<Method, Object> substituteReturnValues) {
        this.substituteReturnValues = substituteReturnValues;
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public Map<Method, Object> getSubstituteReturnValues() {
        return getStubbedMethods();
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public Map<Method, InvocationHandler> getProxiedMethods() {
        return this.proxies;
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public void proxyMethod(Method method, InvocationHandler invocationHandler) {
        this.proxies.put(method, invocationHandler);
    }

    @Override // org.powermock.mockpolicies.MockPolicyInterceptionSettings
    public void setMethodsToProxy(Map<Method, InvocationHandler> proxies) {
        this.proxies = proxies;
    }
}
