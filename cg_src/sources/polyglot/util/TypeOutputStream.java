package polyglot.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import polyglot.main.Report;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/TypeOutputStream.class */
public class TypeOutputStream extends ObjectOutputStream {
    protected TypeSystem ts;
    protected Set roots;
    protected Map placeHolders;

    public TypeOutputStream(OutputStream out, TypeSystem ts, Type root) throws IOException {
        super(out);
        this.ts = ts;
        this.roots = ts.getTypeEncoderRootSet(root);
        this.placeHolders = new HashMap();
        if (Report.should_report(Report.serialize, 2)) {
            Report.report(2, new StringBuffer().append("Began TypeOutputStream with roots: ").append(this.roots).toString());
        }
        enableReplaceObject(true);
    }

    protected Object placeHolder(TypeObject o) {
        IdentityKey identityKey = new IdentityKey(o);
        Object p = this.placeHolders.get(identityKey);
        if (p == null) {
            p = this.ts.placeHolder(o, this.roots);
            this.placeHolders.put(identityKey, p);
        }
        return p;
    }

    @Override // java.io.ObjectOutputStream
    protected Object replaceObject(Object o) throws IOException {
        if (this.roots.contains(o)) {
            if (Report.should_report(Report.serialize, 2)) {
                Report.report(2, new StringBuffer().append("+ In roots: ").append(o).append(" : ").append(o.getClass()).toString());
            }
            return o;
        } else if (o instanceof TypeObject) {
            Object r = placeHolder((TypeObject) o);
            if (Report.should_report(Report.serialize, 2)) {
                if (r != o) {
                    Report.report(2, new StringBuffer().append("+ Replacing: ").append(o).append(" : ").append(o.getClass()).append(" with ").append(r).toString());
                } else {
                    Report.report(2, new StringBuffer().append("+ ").append(o).append(" : ").append(o.getClass()).toString());
                }
            }
            return r;
        } else {
            if (Report.should_report(Report.serialize, 2)) {
                Report.report(2, new StringBuffer().append("+ ").append(o).append(" : ").append(o.getClass()).toString());
            }
            return o;
        }
    }
}
