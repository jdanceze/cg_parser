package javax.management.monitor;

import javax.management.ObjectName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/monitor/GaugeMonitorMBean.class */
public interface GaugeMonitorMBean extends MonitorMBean {
    Number getDerivedGauge();

    long getDerivedGaugeTimeStamp();

    Number getDerivedGauge(ObjectName objectName);

    long getDerivedGaugeTimeStamp(ObjectName objectName);

    Number getHighThreshold();

    Number getLowThreshold();

    void setThresholds(Number number, Number number2) throws IllegalArgumentException;

    boolean getNotifyHigh();

    void setNotifyHigh(boolean z);

    boolean getNotifyLow();

    void setNotifyLow(boolean z);

    boolean getDifferenceMode();

    void setDifferenceMode(boolean z);
}
