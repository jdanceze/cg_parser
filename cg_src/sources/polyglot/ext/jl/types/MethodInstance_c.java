package polyglot.ext.jl.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import polyglot.main.Report;
import polyglot.types.Flags;
import polyglot.types.MethodInstance;
import polyglot.types.ReferenceType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/MethodInstance_c.class */
public class MethodInstance_c extends ProcedureInstance_c implements MethodInstance {
    protected String name;
    protected Type returnType;

    protected MethodInstance_c() {
    }

    public MethodInstance_c(TypeSystem ts, Position pos, ReferenceType container, Flags flags, Type returnType, String name, List formalTypes, List excTypes) {
        super(ts, pos, container, flags, formalTypes, excTypes);
        this.returnType = returnType;
        this.name = name;
    }

    @Override // polyglot.types.MethodInstance
    public MethodInstance flags(Flags flags) {
        if (!flags.equals(this.flags)) {
            MethodInstance_c n = (MethodInstance_c) copy();
            n.flags = flags;
            return n;
        }
        return this;
    }

    @Override // polyglot.types.MethodInstance
    public String name() {
        return this.name;
    }

    @Override // polyglot.types.MethodInstance
    public MethodInstance name(String name) {
        if ((name != null && !name.equals(this.name)) || (name == null && name != this.name)) {
            MethodInstance_c n = (MethodInstance_c) copy();
            n.name = name;
            return n;
        }
        return this;
    }

    @Override // polyglot.types.MethodInstance
    public Type returnType() {
        return this.returnType;
    }

    @Override // polyglot.types.MethodInstance
    public MethodInstance returnType(Type returnType) {
        if (this.returnType != returnType) {
            MethodInstance_c n = (MethodInstance_c) copy();
            n.returnType = returnType;
            return n;
        }
        return this;
    }

    @Override // polyglot.types.MethodInstance
    public MethodInstance formalTypes(List l) {
        if (!CollectionUtil.equals(this.formalTypes, l)) {
            MethodInstance_c n = (MethodInstance_c) copy();
            n.formalTypes = new ArrayList(l);
            return n;
        }
        return this;
    }

    @Override // polyglot.types.MethodInstance
    public MethodInstance throwTypes(List l) {
        if (!CollectionUtil.equals(this.excTypes, l)) {
            MethodInstance_c n = (MethodInstance_c) copy();
            n.excTypes = new ArrayList(l);
            return n;
        }
        return this;
    }

    @Override // polyglot.types.MethodInstance
    public MethodInstance container(ReferenceType container) {
        if (this.container != container) {
            MethodInstance_c n = (MethodInstance_c) copy();
            n.container = container;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.types.ProcedureInstance_c, polyglot.ext.jl.types.TypeObject_c
    public int hashCode() {
        return this.flags.hashCode() + this.name.hashCode();
    }

    @Override // polyglot.ext.jl.types.ProcedureInstance_c, polyglot.ext.jl.types.TypeObject_c, polyglot.types.TypeObject
    public boolean equalsImpl(TypeObject o) {
        if (o instanceof MethodInstance) {
            MethodInstance i = (MethodInstance) o;
            return this.ts.equals(this.returnType, i.returnType()) && this.name.equals(i.name()) && super.equalsImpl((TypeObject) i);
        }
        return false;
    }

    public String toString() {
        String s = new StringBuffer().append(designator()).append(Instruction.argsep).append(this.flags.translate()).append(this.returnType).append(Instruction.argsep).append(signature()).toString();
        if (!this.excTypes.isEmpty()) {
            s = new StringBuffer().append(s).append(" throws ").append(TypeSystem_c.listToString(this.excTypes)).toString();
        }
        return s;
    }

    @Override // polyglot.types.ProcedureInstance
    public String signature() {
        return new StringBuffer().append(this.name).append("(").append(TypeSystem_c.listToString(this.formalTypes)).append(")").toString();
    }

    @Override // polyglot.types.ProcedureInstance
    public String designator() {
        return "method";
    }

    @Override // polyglot.types.MethodInstance
    public final boolean isSameMethod(MethodInstance m) {
        return this.ts.isSameMethod(this, m);
    }

    @Override // polyglot.types.MethodInstance
    public boolean isSameMethodImpl(MethodInstance m) {
        return name().equals(m.name()) && hasFormals(m.formalTypes());
    }

    @Override // polyglot.types.TypeObject
    public boolean isCanonical() {
        return this.container.isCanonical() && this.returnType.isCanonical() && listIsCanonical(this.formalTypes) && listIsCanonical(this.excTypes);
    }

    @Override // polyglot.types.MethodInstance
    public final boolean methodCallValid(String name, List argTypes) {
        return this.ts.methodCallValid(this, name, argTypes);
    }

    @Override // polyglot.types.MethodInstance
    public boolean methodCallValidImpl(String name, List argTypes) {
        return name().equals(name) && this.ts.callValid(this, argTypes);
    }

    @Override // polyglot.types.MethodInstance
    public List overrides() {
        return this.ts.overrides(this);
    }

    @Override // polyglot.types.MethodInstance
    public List overridesImpl() {
        List l = new LinkedList();
        ReferenceType container = container();
        while (true) {
            ReferenceType rt = container;
            if (rt != null) {
                l.addAll(rt.methods(this.name, this.formalTypes));
                ReferenceType sup = null;
                if (rt.superType() != null && rt.superType().isReference()) {
                    sup = (ReferenceType) rt.superType();
                }
                container = sup;
            } else {
                return l;
            }
        }
    }

    @Override // polyglot.types.MethodInstance
    public final boolean canOverride(MethodInstance mj) {
        return this.ts.canOverride(this, mj);
    }

    @Override // polyglot.types.MethodInstance
    public final void checkOverride(MethodInstance mj) throws SemanticException {
        this.ts.checkOverride(this, mj);
    }

    public final boolean canOverrideImpl(MethodInstance mj) throws SemanticException {
        throw new RuntimeException("canOverrideImpl(MethodInstance mj) should not be called.");
    }

    @Override // polyglot.types.MethodInstance
    public boolean canOverrideImpl(MethodInstance mj, boolean quiet) throws SemanticException {
        if (name().equals(mj.name()) && hasFormals(mj.formalTypes())) {
            if (this.ts.equals(returnType(), mj.returnType())) {
                if (!this.ts.throwsSubset(this, mj)) {
                    if (Report.should_report(Report.types, 3)) {
                        Report.report(3, new StringBuffer().append(throwTypes()).append(" not subset of ").append(mj.throwTypes()).toString());
                    }
                    if (quiet) {
                        return false;
                    }
                    throw new SemanticException(new StringBuffer().append(signature()).append(" in ").append(container()).append(" cannot override ").append(mj.signature()).append(" in ").append(mj.container()).append("; the throw set is not a subset of the ").append("overridden method's throw set").toString(), position());
                } else if (flags().moreRestrictiveThan(mj.flags())) {
                    if (Report.should_report(Report.types, 3)) {
                        Report.report(3, new StringBuffer().append(flags()).append(" more restrictive than ").append(mj.flags()).toString());
                    }
                    if (quiet) {
                        return false;
                    }
                    throw new SemanticException(new StringBuffer().append(signature()).append(" in ").append(container()).append(" cannot override ").append(mj.signature()).append(" in ").append(mj.container()).append("; attempting to assign weaker ").append("access privileges").toString(), position());
                } else if (flags().isStatic() != mj.flags().isStatic()) {
                    if (Report.should_report(Report.types, 3)) {
                        Report.report(3, new StringBuffer().append(signature()).append(" is ").append(flags().isStatic() ? "" : "not").append(" static but ").append(mj.signature()).append(" is ").append(mj.flags().isStatic() ? "" : "not").append(" static").toString());
                    }
                    if (quiet) {
                        return false;
                    }
                    throw new SemanticException(new StringBuffer().append(signature()).append(" in ").append(container()).append(" cannot override ").append(mj.signature()).append(" in ").append(mj.container()).append("; overridden method is ").append(mj.flags().isStatic() ? "" : "not").append(Jimple.STATIC).toString(), position());
                } else if (this != mj && !equals(mj) && mj.flags().isFinal()) {
                    if (Report.should_report(Report.types, 3)) {
                        Report.report(3, new StringBuffer().append(mj.flags()).append(" final").toString());
                    }
                    if (quiet) {
                        return false;
                    }
                    throw new SemanticException(new StringBuffer().append(signature()).append(" in ").append(container()).append(" cannot override ").append(mj.signature()).append(" in ").append(mj.container()).append("; overridden method is final").toString(), position());
                } else {
                    return true;
                }
            }
            if (Report.should_report(Report.types, 3)) {
                Report.report(3, new StringBuffer().append("return type ").append(returnType()).append(" != ").append(mj.returnType()).toString());
            }
            if (quiet) {
                return false;
            }
            throw new SemanticException(new StringBuffer().append(signature()).append(" in ").append(container()).append(" cannot override ").append(mj.signature()).append(" in ").append(mj.container()).append("; attempting to use incompatible ").append("return type\n").append("found: ").append(returnType()).append("\n").append("required: ").append(mj.returnType()).toString(), position());
        } else if (quiet) {
            return false;
        } else {
            throw new SemanticException("Arguments are different", position());
        }
    }

    @Override // polyglot.types.MethodInstance
    public List implemented() {
        return this.ts.implemented(this);
    }

    @Override // polyglot.types.MethodInstance
    public List implementedImpl(ReferenceType rt) {
        if (rt == null) {
            return Collections.EMPTY_LIST;
        }
        List l = new LinkedList();
        l.addAll(rt.methods(this.name, this.formalTypes));
        Type superType = rt.superType();
        if (superType != null) {
            l.addAll(implementedImpl(superType.toReference()));
        }
        List<ReferenceType> ints = rt.interfaces();
        for (ReferenceType rt2 : ints) {
            l.addAll(implementedImpl(rt2));
        }
        return l;
    }
}
