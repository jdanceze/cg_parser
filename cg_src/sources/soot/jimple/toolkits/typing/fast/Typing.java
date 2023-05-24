package soot.jimple.toolkits.typing.fast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import soot.Local;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/Typing.class */
public class Typing {
    protected HashMap<Local, Type> map;

    public Typing(Collection<Local> vs) {
        this.map = new HashMap<>(vs.size());
    }

    public Typing(Typing tg) {
        this.map = new HashMap<>(tg.map);
    }

    public Map<Local, Type> getMap() {
        return this.map;
    }

    public Type get(Local v) {
        Type t = this.map.get(v);
        return t == null ? BottomType.v() : t;
    }

    public Type set(Local v, Type t) {
        if (t instanceof BottomType) {
            return null;
        }
        return this.map.put(v, t);
    }

    public Collection<Local> getAllLocals() {
        return this.map.keySet();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (Map.Entry<Local, Type> e : this.map.entrySet()) {
            sb.append(e.getKey());
            sb.append(':');
            sb.append(e.getValue());
            sb.append(',');
        }
        sb.append('}');
        return sb.toString();
    }
}
