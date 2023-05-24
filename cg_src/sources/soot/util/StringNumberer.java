package soot.util;

import heros.ThreadSafe;
import java.util.HashMap;
import java.util.Map;
@ThreadSafe
/* loaded from: gencallgraphv3.jar:soot/util/StringNumberer.class */
public class StringNumberer extends ArrayNumberer<NumberedString> {
    private final Map<String, NumberedString> stringToNumbered = new HashMap(1024);

    public synchronized NumberedString findOrAdd(String s) {
        NumberedString ret = this.stringToNumbered.get(s);
        if (ret == null) {
            ret = new NumberedString(s);
            this.stringToNumbered.put(s, ret);
            add((StringNumberer) ret);
        }
        return ret;
    }

    public NumberedString find(String s) {
        return this.stringToNumbered.get(s);
    }
}
