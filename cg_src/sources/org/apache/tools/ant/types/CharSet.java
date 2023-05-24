package org.apache.tools.ant.types;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/CharSet.class */
public class CharSet extends EnumeratedAttribute {
    private static final List<String> VALUES = new ArrayList();

    static {
        for (Map.Entry<String, Charset> entry : Charset.availableCharsets().entrySet()) {
            VALUES.add(entry.getKey());
            VALUES.addAll(entry.getValue().aliases());
        }
    }

    public CharSet() {
    }

    public CharSet(String value) {
        setValue(value);
    }

    public static CharSet getDefault() {
        return new CharSet(Charset.defaultCharset().name());
    }

    public static CharSet getAscii() {
        return new CharSet(StandardCharsets.US_ASCII.name());
    }

    public static CharSet getUtf8() {
        return new CharSet(StandardCharsets.UTF_8.name());
    }

    public boolean equivalent(CharSet cs) {
        return getCharset().name().equals(cs.getCharset().name());
    }

    public Charset getCharset() {
        return Charset.forName(getValue());
    }

    @Override // org.apache.tools.ant.types.EnumeratedAttribute
    public String[] getValues() {
        return (String[]) VALUES.toArray(new String[0]);
    }

    @Override // org.apache.tools.ant.types.EnumeratedAttribute
    public final void setValue(String value) {
        String realValue = value;
        if (value != null && !value.isEmpty()) {
            Iterator it = Arrays.asList(value, value.toLowerCase(), value.toUpperCase()).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String v = (String) it.next();
                if (VALUES.contains(v)) {
                    realValue = v;
                    break;
                }
            }
        } else {
            realValue = Charset.defaultCharset().name();
        }
        super.setValue(realValue);
    }
}
