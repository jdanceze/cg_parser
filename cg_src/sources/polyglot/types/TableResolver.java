package polyglot.types;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import polyglot.main.Report;
import polyglot.util.CollectionUtil;
import polyglot.util.InternalCompilerError;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/TableResolver.class */
public class TableResolver extends ClassResolver implements TopLevelResolver {
    protected Map table = new HashMap();
    private static final Collection TOPICS = CollectionUtil.list(Report.types, Report.resolver);

    public void addNamed(Named type) {
        addNamed(type.name(), type);
    }

    public void addNamed(String name, Named type) {
        if (name == null || type == null) {
            throw new InternalCompilerError("Bad insertion into TableResolver");
        }
        if (Report.should_report(TOPICS, 3)) {
            Report.report(3, new StringBuffer().append("TableCR.addNamed(").append(name).append(", ").append(type).append(")").toString());
        }
        this.table.put(name, type);
    }

    @Override // polyglot.types.TopLevelResolver
    public boolean packageExists(String name) {
        for (Map.Entry e : this.table.entrySet()) {
            Named type = (Named) e.getValue();
            if (type instanceof Importable) {
                Importable im = (Importable) type;
                if (im.package_() != null && im.package_().fullName().startsWith(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // polyglot.types.ClassResolver, polyglot.types.Resolver
    public Named find(String name) throws SemanticException {
        if (Report.should_report(TOPICS, 3)) {
            Report.report(3, new StringBuffer().append("TableCR.find(").append(name).append(")").toString());
        }
        Named n = (Named) this.table.get(name);
        if (n != null) {
            return n;
        }
        throw new NoClassException(name);
    }

    public String toString() {
        return new StringBuffer().append("(table ").append(this.table).append(")").toString();
    }
}
