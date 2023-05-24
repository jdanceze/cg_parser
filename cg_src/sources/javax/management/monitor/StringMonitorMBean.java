package javax.management.monitor;

import javax.management.ObjectName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/monitor/StringMonitorMBean.class */
public interface StringMonitorMBean extends MonitorMBean {
    String getDerivedGauge();

    long getDerivedGaugeTimeStamp();

    String getDerivedGauge(ObjectName objectName);

    long getDerivedGaugeTimeStamp(ObjectName objectName);

    String getStringToCompare();

    void setStringToCompare(String str) throws IllegalArgumentException;

    boolean getNotifyMatch();

    void setNotifyMatch(boolean z);

    boolean getNotifyDiffer();

    void setNotifyDiffer(boolean z);
}
