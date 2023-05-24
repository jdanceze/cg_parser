package javax.management.monitor;

import javax.management.Notification;
import javax.management.ObjectName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/monitor/MonitorNotification.class */
public class MonitorNotification extends Notification {
    public static final String OBSERVED_OBJECT_ERROR = new String("jmx.monitor.error.mbean");
    public static final String OBSERVED_ATTRIBUTE_ERROR = new String("jmx.monitor.error.attribute");
    public static final String OBSERVED_ATTRIBUTE_TYPE_ERROR = new String("jmx.monitor.error.type");
    public static final String THRESHOLD_ERROR = new String("jmx.monitor.error.threshold");
    public static final String RUNTIME_ERROR = new String("jmx.monitor.error.runtime");
    public static final String THRESHOLD_VALUE_EXCEEDED = new String("jmx.monitor.counter.threshold");
    public static final String THRESHOLD_HIGH_VALUE_EXCEEDED = new String("jmx.monitor.gauge.high");
    public static final String THRESHOLD_LOW_VALUE_EXCEEDED = new String("jmx.monitor.gauge.low");
    public static final String STRING_TO_COMPARE_VALUE_MATCHED = new String("jmx.monitor.string.matches");
    public static final String STRING_TO_COMPARE_VALUE_DIFFERED = new String("jmx.monitor.string.differs");
    private static final long serialVersionUID = -4608189663661929204L;
    private ObjectName observedObject;
    private String observedAttribute;
    private Object derivedGauge;
    private Object trigger;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MonitorNotification(String str, Object obj, long j, long j2, String str2, ObjectName objectName, String str3, Object obj2, Object obj3) {
        super(str, obj, j, j2, str2);
        this.observedObject = null;
        this.observedAttribute = null;
        this.derivedGauge = null;
        this.trigger = null;
        this.observedObject = objectName;
        this.observedAttribute = str3;
        this.derivedGauge = obj2;
        this.trigger = obj3;
    }

    public ObjectName getObservedObject() {
        return this.observedObject;
    }

    public String getObservedAttribute() {
        return this.observedAttribute;
    }

    public Object getDerivedGauge() {
        return this.derivedGauge;
    }

    public Object getTrigger() {
        return this.trigger;
    }
}
