package polyglot.visit;

import java.util.Stack;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.CachingResolver;
import polyglot.types.ClassType;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.ImportTable;
import polyglot.types.Named;
import polyglot.types.Package;
import polyglot.types.ParsedClassType;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorQueue;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/TypeBuilder.class */
public class TypeBuilder extends HaltingVisitor {
    protected ImportTable importTable;
    protected Job job;
    protected TypeSystem ts;
    protected NodeFactory nf;
    protected TypeBuilder outer = null;
    protected boolean inCode;
    protected boolean global;
    protected ParsedClassType type;

    public TypeBuilder(Job job, TypeSystem ts, NodeFactory nf) {
        this.job = job;
        this.ts = ts;
        this.nf = nf;
    }

    public TypeBuilder push() {
        TypeBuilder tb = (TypeBuilder) copy();
        tb.outer = this;
        return tb;
    }

    public TypeBuilder pop() {
        return this.outer;
    }

    public Job job() {
        return this.job;
    }

    public ErrorQueue errorQueue() {
        return this.job.compiler().errorQueue();
    }

    public NodeFactory nodeFactory() {
        return this.nf;
    }

    public TypeSystem typeSystem() {
        return this.ts;
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor begin() {
        Context context = this.job.context();
        if (context == null) {
            return this;
        }
        Stack s = new Stack();
        ParsedClassType currentClassScope = context.currentClassScope();
        while (true) {
            ParsedClassType ct = currentClassScope;
            if (ct == null) {
                break;
            }
            s.push(ct);
            if (ct.isNested()) {
                currentClassScope = (ParsedClassType) ct.outer();
            } else {
                currentClassScope = null;
            }
        }
        if (context.importTable() != null) {
            setImportTable(context.importTable());
        }
        TypeBuilder tb = this;
        while (!s.isEmpty()) {
            ParsedClassType ct2 = (ParsedClassType) s.pop();
            try {
                tb = tb.pushClass(ct2);
                if (ct2.isLocal() || ct2.isAnonymous()) {
                    tb = tb.pushCode();
                }
            } catch (SemanticException e) {
                errorQueue().enqueue(5, e.getMessage(), ct2.position());
                return null;
            }
        }
        return tb;
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node n) {
        try {
            return n.del().buildTypesEnter(this);
        } catch (SemanticException e) {
            Position position = e.position();
            if (position == null) {
                position = n.position();
            }
            if (e.getMessage() != null) {
                errorQueue().enqueue(5, e.getMessage(), position);
            }
            return this;
        }
    }

    @Override // polyglot.visit.NodeVisitor
    public Node leave(Node old, Node n, NodeVisitor v) {
        try {
            return n.del().buildTypes((TypeBuilder) v);
        } catch (SemanticException e) {
            Position position = e.position();
            if (position == null) {
                position = n.position();
            }
            if (e.getMessage() != null) {
                errorQueue().enqueue(5, e.getMessage(), position);
            }
            return n;
        }
    }

    public TypeBuilder pushCode() {
        if (Report.should_report(Report.visit, 4)) {
            Report.report(4, new StringBuffer().append("TB pushing code: ").append(this).toString());
        }
        TypeBuilder tb = push();
        tb.inCode = true;
        tb.global = false;
        return tb;
    }

    protected TypeBuilder pushClass(ParsedClassType type) throws SemanticException {
        if (Report.should_report(Report.visit, 4)) {
            Report.report(4, new StringBuffer().append("TB pushing class ").append(type).append(": ").append(this).toString());
        }
        TypeBuilder tb = push();
        tb.type = type;
        tb.inCode = false;
        if (importTable() != null && type.isTopLevel()) {
            tb.importTable().addClassImport(type.fullName());
        }
        return tb;
    }

    protected ParsedClassType newClass(Position pos, Flags flags, String name) throws SemanticException {
        boolean allMembers;
        TypeSystem ts = typeSystem();
        ParsedClassType ct = ts.createClassType(this.job.source());
        if (this.inCode) {
            ct.kind(ClassType.LOCAL);
            ct.outer(currentClass());
            ct.flags(flags);
            ct.name(name);
            ct.position(pos);
            if (currentPackage() != null) {
                ct.package_(currentPackage());
            }
            return ct;
        } else if (currentClass() != null) {
            ct.kind(ClassType.MEMBER);
            ct.outer(currentClass());
            ct.flags(flags);
            ct.name(name);
            ct.position(pos);
            currentClass().addMemberClass(ct);
            if (currentPackage() != null) {
                ct.package_(currentPackage());
            }
            ClassType container = ct.outer();
            boolean z = container.isMember() || container.isTopLevel();
            while (true) {
                allMembers = z;
                if (!container.isMember()) {
                    break;
                }
                container = container.outer();
                z = allMembers && (container.isMember() || container.isTopLevel());
            }
            if (allMembers) {
                typeSystem().parsedResolver().addNamed(typeSystem().getTransformedClassName(ct), ct);
            }
            return ct;
        } else {
            ct.kind(ClassType.TOP_LEVEL);
            ct.flags(flags);
            ct.name(name);
            ct.position(pos);
            if (currentPackage() != null) {
                ct.package_(currentPackage());
            }
            Named dup = ((CachingResolver) typeSystem().systemResolver()).check(ct.fullName());
            if (dup != null && dup.fullName().equals(ct.fullName())) {
                throw new SemanticException(new StringBuffer().append("Duplicate class \"").append(ct.fullName()).append("\".").toString(), pos);
            }
            typeSystem().parsedResolver().addNamed(ct.fullName(), ct);
            ((CachingResolver) typeSystem().systemResolver()).addNamed(ct.fullName(), ct);
            return ct;
        }
    }

    public TypeBuilder pushAnonClass(Position pos) throws SemanticException {
        if (Report.should_report(Report.visit, 4)) {
            Report.report(4, new StringBuffer().append("TB pushing anon class: ").append(this).toString());
        }
        if (!this.inCode) {
            throw new InternalCompilerError("Can only push an anonymous class within code.");
        }
        TypeSystem ts = typeSystem();
        ParsedClassType ct = ts.createClassType(job().source());
        ct.kind(ClassType.ANONYMOUS);
        ct.outer(currentClass());
        ct.position(pos);
        if (currentPackage() != null) {
            ct.package_(currentPackage());
        }
        return pushClass(ct);
    }

    public TypeBuilder pushClass(Position pos, Flags flags, String name) throws SemanticException {
        ParsedClassType t = newClass(pos, flags, name);
        return pushClass(t);
    }

    public ParsedClassType currentClass() {
        return this.type;
    }

    public Package currentPackage() {
        if (importTable() == null) {
            return null;
        }
        return this.importTable.package_();
    }

    public ImportTable importTable() {
        return this.importTable;
    }

    public void setImportTable(ImportTable it) {
        this.importTable = it;
    }

    @Override // polyglot.visit.NodeVisitor
    public String toString() {
        return new StringBuffer().append("(TB ").append(this.type).append(this.inCode ? " inCode" : "").append(this.global ? " global" : "").append(this.outer == null ? ")" : new StringBuffer().append(Instruction.argsep).append(this.outer.toString()).append(")").toString()).toString();
    }
}
