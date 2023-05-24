package javax.management.monitor;

import javax.management.ObjectName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/monitor/MonitorMBean.class */
public interface MonitorMBean {
    void start();

    void stop();

    void addObservedObject(ObjectName objectName) throws IllegalArgumentException;

    void removeObservedObject(ObjectName objectName);

    boolean containsObservedObject(ObjectName objectName);

    ObjectName[] getObservedObjects();

    ObjectName getObservedObject();

    void setObservedObject(ObjectName objectName);

    String getObservedAttribute();

    void setObservedAttribute(String str);

    long getGranularityPeriod();

    void setGranularityPeriod(long j) throws IllegalArgumentException;

    boolean isActive();
}
