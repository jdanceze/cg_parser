package soot.jimple.infoflow.android.axml.flags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/axml/flags/BitwiseFlagSystem.class */
public class BitwiseFlagSystem<T> {
    private List<T> keys = new ArrayList();
    private List<Integer> values = new ArrayList();

    public void register(T key, int setBits) {
        this.keys.add(key);
        this.values.add(Integer.valueOf(setBits));
    }

    public final Collection<T> getFlags(int value) {
        List<T> matchedResults = new ArrayList<>(4);
        List<Integer> matchedValues = new ArrayList<>(4);
        for (int i = 0; i < this.keys.size(); i++) {
            int v = this.values.get(i).intValue();
            if ((v & value) == v && !hadAnyMatch(v, matchedValues)) {
                matchedResults.add(this.keys.get(i));
                matchedValues.add(Integer.valueOf(v));
            }
        }
        return matchedResults;
    }

    private static boolean hadAnyMatch(int value, List<Integer> matchedValues) {
        for (Integer num : matchedValues) {
            int c = num.intValue();
            if ((c & value) == value) {
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    public final boolean isSet(int inputValue, T... tArr) {
        List<T> flagsLeft = new ArrayList<>(tArr.length);
        for (T i : tArr) {
            flagsLeft.add(i);
        }
        for (Object obj : getFlags(inputValue)) {
            Iterator<T> it = flagsLeft.iterator();
            while (it.hasNext()) {
                if (it.next().equals(obj)) {
                    it.remove();
                    if (flagsLeft.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
