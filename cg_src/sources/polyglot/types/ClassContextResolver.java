package polyglot.types;

import java.util.Collection;
import polyglot.main.Report;
import polyglot.util.CollectionUtil;
import polyglot.util.InternalCompilerError;
import polyglot.util.StringUtil;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/ClassContextResolver.class */
public class ClassContextResolver extends ClassResolver {
    TypeSystem ts;
    ClassType type;
    private static final Collection TOPICS = CollectionUtil.list(Report.types, Report.resolver);

    public ClassContextResolver(TypeSystem ts, ClassType type) {
        this.ts = ts;
        this.type = type;
    }

    public String toString() {
        return new StringBuffer().append("(class-context ").append(this.type).append(")").toString();
    }

    @Override // polyglot.types.ClassResolver, polyglot.types.Resolver
    public Named find(String name) throws SemanticException {
        if (Report.should_report(TOPICS, 2)) {
            Report.report(2, new StringBuffer().append("Looking for ").append(name).append(" in ").append(this).toString());
        }
        if (!StringUtil.isNameShort(name)) {
            throw new InternalCompilerError(new StringBuffer().append("Cannot lookup qualified name ").append(name).toString());
        }
        ClassType inner = this.ts.findMemberClass(this.type, name);
        if (inner != null) {
            if (Report.should_report(TOPICS, 2)) {
                Report.report(2, new StringBuffer().append("Found member class ").append(inner).toString());
            }
            return inner;
        }
        throw new NoClassException(name, this.type);
    }

    public ClassType classType() {
        return this.type;
    }
}
