package polyglot.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import polyglot.main.Report;
import polyglot.types.PlaceHolder;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/TypeInputStream.class */
public class TypeInputStream extends ObjectInputStream {
    protected TypeSystem ts;
    protected Map cache;

    public TypeInputStream(InputStream in, TypeSystem ts) throws IOException {
        super(in);
        enableResolveObject(true);
        this.ts = ts;
        this.cache = new HashMap();
    }

    public TypeSystem getTypeSystem() {
        return this.ts;
    }

    @Override // java.io.ObjectInputStream
    protected Object resolveObject(Object o) {
        String s = "";
        if (Report.should_report(Report.serialize, 2)) {
            try {
                s = o.toString();
            } catch (NullPointerException e) {
                s = "<NullPointerException thrown>";
            }
        }
        if (o instanceof PlaceHolder) {
            IdentityKey identityKey = new IdentityKey(o);
            TypeObject t = (TypeObject) this.cache.get(identityKey);
            if (t == null) {
                t = ((PlaceHolder) o).resolve(this.ts);
                this.cache.put(identityKey, t);
            }
            if (Report.should_report(Report.serialize, 2)) {
                Report.report(2, new StringBuffer().append("- Resolving ").append(s).append(" : ").append(o.getClass()).append(" to ").append(t).append(" : ").append(t.getClass()).toString());
            }
            return t;
        } else if (o instanceof Enum) {
            if (Report.should_report(Report.serialize, 2)) {
                Report.report(2, new StringBuffer().append("- Interning ").append(s).append(" : ").append(o.getClass()).toString());
            }
            return ((Enum) o).intern();
        } else {
            if (Report.should_report(Report.serialize, 2)) {
                Report.report(2, new StringBuffer().append("- ").append(s).append(" : ").append(o.getClass()).toString());
            }
            return o;
        }
    }
}
