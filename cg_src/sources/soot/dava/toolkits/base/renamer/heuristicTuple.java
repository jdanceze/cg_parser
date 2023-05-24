package soot.dava.toolkits.base.renamer;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.resource.spi.work.WorkException;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/renamer/heuristicTuple.class */
public class heuristicTuple {
    BitSet heuristics;
    int bitSetSize;
    Vector<String> methodName = new Vector<>();
    Vector<String> objectClassName = new Vector<>();
    Vector<String> fieldName = new Vector<>();
    Vector<String> castStrings = new Vector<>();

    public heuristicTuple(int bits) {
        this.heuristics = new BitSet(bits);
        this.bitSetSize = bits;
    }

    public void addCastString(String castString) {
        this.castStrings.add(castString);
        setHeuristic(10);
    }

    public List<String> getCastStrings() {
        return this.castStrings;
    }

    public void setFieldName(String fieldName) {
        this.fieldName.add(fieldName);
        setHeuristic(8);
    }

    public List<String> getFieldName() {
        return this.fieldName;
    }

    public void setObjectClassName(String objectClassName) {
        this.objectClassName.add(objectClassName);
        setHeuristic(0);
    }

    public List<String> getObjectClassName() {
        return this.objectClassName;
    }

    public void setMethodName(String methodName) {
        this.methodName.add(methodName);
        setHeuristic(1);
        if (methodName.startsWith("get") || methodName.startsWith("set")) {
            setHeuristic(2);
        }
    }

    public List<String> getMethodName() {
        return this.methodName;
    }

    public void setHeuristic(int bitIndex) {
        this.heuristics.set(bitIndex);
    }

    public boolean getHeuristic(int bitIndex) {
        return this.heuristics.get(bitIndex);
    }

    public boolean isAnyHeuristicSet() {
        return !this.heuristics.isEmpty();
    }

    public String getPrint() {
        String concat;
        String temp = "BitSet: ";
        for (int i = 0; i < this.bitSetSize; i++) {
            if (getHeuristic(i)) {
                concat = temp.concat(WorkException.START_TIMED_OUT);
            } else {
                concat = temp.concat(WorkException.UNDEFINED);
            }
            temp = concat;
        }
        String temp2 = temp.concat("  Field: " + this.fieldName.toString()).concat("  Method: ");
        Iterator<String> it = getMethodName().iterator();
        while (it.hasNext()) {
            temp2 = temp2.concat(String.valueOf(it.next()) + " , ");
        }
        return temp2.concat("  Class: " + this.objectClassName.toString());
    }
}
