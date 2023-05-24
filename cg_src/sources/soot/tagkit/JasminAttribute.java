package soot.tagkit;

import java.util.Hashtable;
import java.util.Map;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/JasminAttribute.class */
public abstract class JasminAttribute implements Attribute {
    public abstract byte[] decode(String str, Hashtable<String, Integer> hashtable);

    public abstract String getJasminValue(Map<Unit, String> map);
}
