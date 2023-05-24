package polyglot.ext.jl.types;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import polyglot.main.Report;
import polyglot.types.ClassType;
import polyglot.types.CodeInstance;
import polyglot.types.Context;
import polyglot.types.FieldInstance;
import polyglot.types.ImportTable;
import polyglot.types.LocalInstance;
import polyglot.types.MethodInstance;
import polyglot.types.Named;
import polyglot.types.NoMemberException;
import polyglot.types.Package;
import polyglot.types.ParsedClassType;
import polyglot.types.Resolver;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.types.VarInstance;
import polyglot.util.CollectionUtil;
import polyglot.util.Enum;
import polyglot.util.InternalCompilerError;
import soot.coffi.Instruction;
import soot.jimple.infoflow.rifl.RIFLConstants;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/Context_c.class */
public class Context_c implements Context {
    protected TypeSystem ts;
    protected ImportTable it;
    protected ClassType type;
    protected ParsedClassType scope;
    protected CodeInstance code;
    protected Map types;
    protected Map vars;
    protected boolean inCode;
    protected boolean staticContext;
    public static final Kind BLOCK = new Kind("block");
    public static final Kind CLASS = new Kind("class");
    public static final Kind CODE = new Kind("code");
    public static final Kind OUTER = new Kind("outer");
    public static final Kind SOURCE = new Kind(RIFLConstants.SOURCE_TAG);
    private static final Collection TOPICS = CollectionUtil.list(Report.types, Report.context);
    protected Context outer = null;
    protected Kind kind = OUTER;

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/Context_c$Kind.class */
    public static class Kind extends Enum {
        public Kind(String name) {
            super(name);
        }
    }

    public Context_c(TypeSystem ts) {
        this.ts = ts;
    }

    public boolean isBlock() {
        return this.kind == BLOCK;
    }

    public boolean isClass() {
        return this.kind == CLASS;
    }

    public boolean isCode() {
        return this.kind == CODE;
    }

    public boolean isOuter() {
        return this.kind == OUTER;
    }

    public boolean isSource() {
        return this.kind == SOURCE;
    }

    @Override // polyglot.types.Context
    public TypeSystem typeSystem() {
        return this.ts;
    }

    @Override // polyglot.util.Copy
    public Object copy() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalCompilerError("Java clone() weirdness.");
        }
    }

    protected Context_c push() {
        Context_c v = (Context_c) copy();
        v.outer = this;
        v.types = null;
        v.vars = null;
        return v;
    }

    @Override // polyglot.types.Context
    public Resolver outerResolver() {
        if (this.it != null) {
            return this.it;
        }
        return this.ts.systemResolver();
    }

    @Override // polyglot.types.Context
    public ImportTable importTable() {
        return this.it;
    }

    @Override // polyglot.types.Context
    public Package package_() {
        return importTable().package_();
    }

    @Override // polyglot.types.Context
    public boolean isLocal(String name) {
        if (isClass()) {
            return false;
        }
        if ((isBlock() || isCode()) && (findVariableInThisScope(name) != null || findInThisScope(name) != null)) {
            return true;
        }
        if (this.outer == null) {
            return false;
        }
        return this.outer.isLocal(name);
    }

    @Override // polyglot.types.Context
    public MethodInstance findMethod(String name, List argTypes) throws SemanticException {
        if (Report.should_report(TOPICS, 3)) {
            Report.report(3, new StringBuffer().append("find-method ").append(name).append(argTypes).append(" in ").append(this).toString());
        }
        if (currentClass() != null && this.ts.hasMethodNamed(currentClass(), name)) {
            if (Report.should_report(TOPICS, 3)) {
                Report.report(3, new StringBuffer().append("find-method ").append(name).append(argTypes).append(" -> ").append(currentClass()).toString());
            }
            return this.ts.findMethod(currentClass(), name, argTypes, currentClass());
        } else if (this.outer != null) {
            return this.outer.findMethod(name, argTypes);
        } else {
            throw new SemanticException(new StringBuffer().append("Method ").append(name).append(" not found.").toString());
        }
    }

    @Override // polyglot.types.Context
    public LocalInstance findLocal(String name) throws SemanticException {
        VarInstance vi = findVariableSilent(name);
        if (vi instanceof LocalInstance) {
            return (LocalInstance) vi;
        }
        throw new SemanticException(new StringBuffer().append("Local ").append(name).append(" not found.").toString());
    }

    @Override // polyglot.types.Context
    public ClassType findFieldScope(String name) throws SemanticException {
        if (Report.should_report(TOPICS, 3)) {
            Report.report(3, new StringBuffer().append("find-field-scope ").append(name).append(" in ").append(this).toString());
        }
        VarInstance vi = findVariableInThisScope(name);
        if (vi instanceof FieldInstance) {
            if (Report.should_report(TOPICS, 3)) {
                Report.report(3, new StringBuffer().append("find-field-scope ").append(name).append(" in ").append(vi).toString());
            }
            return this.type;
        } else if (vi == null && this.outer != null) {
            return this.outer.findFieldScope(name);
        } else {
            throw new SemanticException(new StringBuffer().append("Field ").append(name).append(" not found.").toString());
        }
    }

    @Override // polyglot.types.Context
    public ClassType findMethodScope(String name) throws SemanticException {
        if (Report.should_report(TOPICS, 3)) {
            Report.report(3, new StringBuffer().append("find-method-scope ").append(name).append(" in ").append(this).toString());
        }
        if (currentClass() != null && this.ts.hasMethodNamed(currentClass(), name)) {
            if (Report.should_report(TOPICS, 3)) {
                Report.report(3, new StringBuffer().append("find-method-scope ").append(name).append(" -> ").append(currentClass()).toString());
            }
            return currentClass();
        } else if (this.outer != null) {
            return this.outer.findMethodScope(name);
        } else {
            throw new SemanticException(new StringBuffer().append("Method ").append(name).append(" not found.").toString());
        }
    }

    @Override // polyglot.types.Context
    public FieldInstance findField(String name) throws SemanticException {
        VarInstance vi = findVariableSilent(name);
        if (vi instanceof FieldInstance) {
            FieldInstance fi = (FieldInstance) vi;
            if (!this.ts.isAccessible(fi, this)) {
                throw new SemanticException(new StringBuffer().append("Field ").append(name).append(" not accessible.").toString());
            }
            if (Report.should_report(TOPICS, 3)) {
                Report.report(3, new StringBuffer().append("find-field ").append(name).append(" -> ").append(fi).toString());
            }
            return fi;
        }
        throw new NoMemberException(3, new StringBuffer().append("Field ").append(name).append(" not found.").toString());
    }

    @Override // polyglot.types.Context
    public VarInstance findVariable(String name) throws SemanticException {
        VarInstance vi = findVariableSilent(name);
        if (vi != null) {
            if (Report.should_report(TOPICS, 3)) {
                Report.report(3, new StringBuffer().append("find-var ").append(name).append(" -> ").append(vi).toString());
            }
            return vi;
        }
        throw new SemanticException(new StringBuffer().append("Variable ").append(name).append(" not found.").toString());
    }

    @Override // polyglot.types.Context
    public VarInstance findVariableSilent(String name) {
        if (Report.should_report(TOPICS, 3)) {
            Report.report(3, new StringBuffer().append("find-var ").append(name).append(" in ").append(this).toString());
        }
        VarInstance vi = findVariableInThisScope(name);
        if (vi != null) {
            if (Report.should_report(TOPICS, 3)) {
                Report.report(3, new StringBuffer().append("find-var ").append(name).append(" -> ").append(vi).toString());
            }
            return vi;
        } else if (this.outer != null) {
            return this.outer.findVariableSilent(name);
        } else {
            return null;
        }
    }

    protected String mapsToString() {
        return new StringBuffer().append("types=").append(this.types).append(" vars=").append(this.vars).toString();
    }

    public String toString() {
        return new StringBuffer().append("(").append(this.kind).append(Instruction.argsep).append(mapsToString()).append(Instruction.argsep).append(this.outer).append(")").toString();
    }

    @Override // polyglot.types.Context
    public Context pop() {
        return this.outer;
    }

    @Override // polyglot.types.Resolver
    public Named find(String name) throws SemanticException {
        if (Report.should_report(TOPICS, 3)) {
            Report.report(3, new StringBuffer().append("find-type ").append(name).append(" in ").append(this).toString());
        }
        if (isOuter()) {
            return outerResolver().find(name);
        }
        if (isSource()) {
            return this.it.find(name);
        }
        Named type = findInThisScope(name);
        if (type != null) {
            if (Report.should_report(TOPICS, 3)) {
                Report.report(3, new StringBuffer().append("find ").append(name).append(" -> ").append(type).toString());
            }
            return type;
        } else if (this.outer != null) {
            return this.outer.find(name);
        } else {
            throw new SemanticException(new StringBuffer().append("Type ").append(name).append(" not found.").toString());
        }
    }

    @Override // polyglot.types.Context
    public Context pushSource(ImportTable it) {
        Context_c v = push();
        v.kind = SOURCE;
        v.it = it;
        v.inCode = false;
        v.staticContext = false;
        return v;
    }

    @Override // polyglot.types.Context
    public Context pushClass(ParsedClassType classScope, ClassType type) {
        if (Report.should_report(TOPICS, 4)) {
            Report.report(4, new StringBuffer().append("push class ").append(classScope).append(Instruction.argsep).append(classScope.position()).toString());
        }
        Context_c v = push();
        v.kind = CLASS;
        v.scope = classScope;
        v.type = type;
        v.inCode = false;
        v.staticContext = false;
        if (!type.isAnonymous()) {
            v.addNamed(type);
        }
        return v;
    }

    @Override // polyglot.types.Context
    public Context pushBlock() {
        if (Report.should_report(TOPICS, 4)) {
            Report.report(4, "push block");
        }
        Context_c v = push();
        v.kind = BLOCK;
        return v;
    }

    @Override // polyglot.types.Context
    public Context pushStatic() {
        if (Report.should_report(TOPICS, 4)) {
            Report.report(4, "push static");
        }
        Context_c v = push();
        v.staticContext = true;
        return v;
    }

    @Override // polyglot.types.Context
    public Context pushCode(CodeInstance ci) {
        if (Report.should_report(TOPICS, 4)) {
            Report.report(4, new StringBuffer().append("push code ").append(ci).append(Instruction.argsep).append(ci.position()).toString());
        }
        Context_c v = push();
        v.kind = CODE;
        v.code = ci;
        v.inCode = true;
        v.staticContext = ci.flags().isStatic();
        return v;
    }

    @Override // polyglot.types.Context
    public CodeInstance currentCode() {
        return this.code;
    }

    @Override // polyglot.types.Context
    public boolean inCode() {
        return this.inCode;
    }

    @Override // polyglot.types.Context
    public boolean inStaticContext() {
        return this.staticContext;
    }

    @Override // polyglot.types.Context
    public ClassType currentClass() {
        return this.type;
    }

    @Override // polyglot.types.Context
    public ParsedClassType currentClassScope() {
        return this.scope;
    }

    @Override // polyglot.types.Context
    public void addVariable(VarInstance vi) {
        if (Report.should_report(TOPICS, 3)) {
            Report.report(3, new StringBuffer().append("Adding ").append(vi).append(" to context.").toString());
        }
        addVariableToThisScope(vi);
    }

    @Override // polyglot.types.Context
    public void addMethod(MethodInstance mi) {
        if (Report.should_report(TOPICS, 3)) {
            Report.report(3, new StringBuffer().append("Adding ").append(mi).append(" to context.").toString());
        }
    }

    @Override // polyglot.types.Context
    public void addNamed(Named t) {
        if (Report.should_report(TOPICS, 3)) {
            Report.report(3, new StringBuffer().append("Adding type ").append(t).append(" to context.").toString());
        }
        addNamedToThisScope(t);
    }

    public Named findInThisScope(String name) {
        Named t = null;
        if (this.types != null) {
            t = (Named) this.types.get(name);
        }
        if (t == null && isClass()) {
            if (!this.type.isAnonymous() && this.type.name().equals(name)) {
                return this.type;
            }
            try {
                return this.ts.findMemberClass(this.type, name, this.type);
            } catch (SemanticException e) {
            }
        }
        return t;
    }

    public void addNamedToThisScope(Named type) {
        if (this.types == null) {
            this.types = new HashMap();
        }
        this.types.put(type.name(), type);
    }

    public ClassType findMethodContainerInThisScope(String name) {
        if (isClass() && this.ts.hasMethodNamed(currentClass(), name)) {
            return this.type;
        }
        return null;
    }

    public VarInstance findVariableInThisScope(String name) {
        VarInstance vi = null;
        if (this.vars != null) {
            vi = (VarInstance) this.vars.get(name);
        }
        if (vi == null && isClass()) {
            try {
                return this.ts.findField(this.type, name, this.type);
            } catch (SemanticException e) {
                return null;
            }
        }
        return vi;
    }

    public void addVariableToThisScope(VarInstance var) {
        if (this.vars == null) {
            this.vars = new HashMap();
        }
        this.vars.put(var.name(), var);
    }
}
