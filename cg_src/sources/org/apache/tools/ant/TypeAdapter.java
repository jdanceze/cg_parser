package org.apache.tools.ant;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/TypeAdapter.class */
public interface TypeAdapter {
    void setProject(Project project);

    Project getProject();

    void setProxy(Object obj);

    Object getProxy();

    void checkProxyClass(Class<?> cls);
}
