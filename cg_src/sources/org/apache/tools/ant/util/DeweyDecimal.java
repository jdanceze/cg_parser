package org.apache.tools.ant.util;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/DeweyDecimal.class */
public class DeweyDecimal implements Comparable<DeweyDecimal> {
    private final int[] components;

    public DeweyDecimal(int[] components) {
        this.components = new int[components.length];
        System.arraycopy(components, 0, this.components, 0, components.length);
    }

    public DeweyDecimal(String string) throws NumberFormatException {
        java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(string, ".", true);
        int size = tokenizer.countTokens();
        this.components = new int[(size + 1) / 2];
        for (int i = 0; i < this.components.length; i++) {
            String component = tokenizer.nextToken();
            if (component.isEmpty()) {
                throw new NumberFormatException("Empty component in string");
            }
            this.components[i] = Integer.parseInt(component);
            if (tokenizer.hasMoreTokens()) {
                tokenizer.nextToken();
                if (!tokenizer.hasMoreTokens()) {
                    throw new NumberFormatException("DeweyDecimal ended in a '.'");
                }
            }
        }
    }

    public int getSize() {
        return this.components.length;
    }

    public int get(int index) {
        return this.components[index];
    }

    public boolean isEqual(DeweyDecimal other) {
        int max = Math.max(other.components.length, this.components.length);
        int i = 0;
        while (i < max) {
            int component1 = i < this.components.length ? this.components[i] : 0;
            int component2 = i < other.components.length ? other.components[i] : 0;
            if (component2 == component1) {
                i++;
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean isLessThan(DeweyDecimal other) {
        return !isGreaterThanOrEqual(other);
    }

    public boolean isLessThanOrEqual(DeweyDecimal other) {
        return !isGreaterThan(other);
    }

    public boolean isGreaterThan(DeweyDecimal other) {
        int max = Math.max(other.components.length, this.components.length);
        int i = 0;
        while (i < max) {
            int component1 = i < this.components.length ? this.components[i] : 0;
            int component2 = i < other.components.length ? other.components[i] : 0;
            if (component2 > component1) {
                return false;
            }
            if (component2 >= component1) {
                i++;
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean isGreaterThanOrEqual(DeweyDecimal other) {
        int max = Math.max(other.components.length, this.components.length);
        int i = 0;
        while (i < max) {
            int component1 = i < this.components.length ? this.components[i] : 0;
            int component2 = i < other.components.length ? other.components[i] : 0;
            if (component2 > component1) {
                return false;
            }
            if (component2 >= component1) {
                i++;
            } else {
                return true;
            }
        }
        return true;
    }

    public String toString() {
        return (String) IntStream.of(this.components).mapToObj(Integer::toString).collect(Collectors.joining("."));
    }

    @Override // java.lang.Comparable
    public int compareTo(DeweyDecimal other) {
        int max = Math.max(other.components.length, this.components.length);
        int i = 0;
        while (i < max) {
            int component1 = i < this.components.length ? this.components[i] : 0;
            int component2 = i < other.components.length ? other.components[i] : 0;
            if (component1 == component2) {
                i++;
            } else {
                return component1 - component2;
            }
        }
        return 0;
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public boolean equals(Object o) {
        return (o instanceof DeweyDecimal) && isEqual((DeweyDecimal) o);
    }
}
