package soot.toolkits.astmetrics;

import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:soot/toolkits/astmetrics/ClassData.class */
public class ClassData {
    String className;
    ArrayList<MetricData> metricData = new ArrayList<>();

    public ClassData(String name) {
        this.className = name;
    }

    public String getClassName() {
        return this.className;
    }

    public boolean classNameEquals(String className) {
        return this.className.equals(className);
    }

    public void addMetric(MetricData data) {
        Iterator<MetricData> it = this.metricData.iterator();
        while (it.hasNext()) {
            MetricData temp = it.next();
            if (temp.metricName.equals(data.metricName)) {
                return;
            }
        }
        this.metricData.add(data);
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("<Class>\n");
        b.append("<ClassName>" + this.className + "</ClassName>\n");
        Iterator<MetricData> it = this.metricData.iterator();
        while (it.hasNext()) {
            b.append(it.next().toString());
        }
        b.append("</Class>");
        return b.toString();
    }
}
