package javax.management.monitor;

import javax.management.ObjectName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/monitor/CounterMonitorMBean.class */
public interface CounterMonitorMBean extends MonitorMBean {
    Number getDerivedGauge();

    long getDerivedGaugeTimeStamp();

    Number getThreshold();

    void setThreshold(Number number) throws IllegalArgumentException;

    Number getDerivedGauge(ObjectName objectName);

    long getDerivedGaugeTimeStamp(ObjectName objectName);

    Number getThreshold(ObjectName objectName);

    Number getInitThreshold();

    void setInitThreshold(Number number) throws IllegalArgumentException;

    Number getOffset();

    void setOffset(Number number) throws IllegalArgumentException;

    Number getModulus();

    void setModulus(Number number) throws IllegalArgumentException;

    boolean getNotify();

    void setNotify(boolean z);

    boolean getDifferenceMode();

    void setDifferenceMode(boolean z);
}
