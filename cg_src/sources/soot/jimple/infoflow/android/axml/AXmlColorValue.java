package soot.jimple.infoflow.android.axml;

import javax.resource.spi.work.WorkException;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/axml/AXmlColorValue.class */
public class AXmlColorValue {
    private final int a;
    private final int r;
    private final int g;
    private final int b;

    public AXmlColorValue(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public AXmlColorValue(int r, int g, int b) {
        this.a = -1;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getA() {
        return this.a;
    }

    public int getR() {
        return this.r;
    }

    public int getG() {
        return this.g;
    }

    public int getB() {
        return this.b;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('#');
        if (this.a >= 0) {
            sb.append(toHexComponent(this.a));
        }
        sb.append(toHexComponent(this.r));
        sb.append(toHexComponent(this.g));
        sb.append(toHexComponent(this.b));
        return sb.toString();
    }

    public String toRGBString() {
        return '#' + toHexComponent(this.r) + toHexComponent(this.g) + toHexComponent(this.b);
    }

    private static String toHexComponent(int val) {
        String s = Integer.toHexString(val);
        if (s.length() < 2) {
            s = WorkException.UNDEFINED + s;
        }
        return s;
    }

    public int hashCode() {
        int result = (31 * 1) + this.a;
        return (31 * ((31 * ((31 * result) + this.b)) + this.g)) + this.r;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AXmlColorValue other = (AXmlColorValue) obj;
        if (this.a != other.a || this.b != other.b || this.g != other.g || this.r != other.r) {
            return false;
        }
        return true;
    }
}
