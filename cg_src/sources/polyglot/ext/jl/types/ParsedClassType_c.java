package polyglot.ext.jl.types;

import java.util.LinkedList;
import java.util.List;
import polyglot.frontend.Source;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.LazyClassInitializer;
import polyglot.types.MethodInstance;
import polyglot.types.Package;
import polyglot.types.ParsedClassType;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.TypedList;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/ParsedClassType_c.class */
public class ParsedClassType_c extends ClassType_c implements ParsedClassType {
    protected transient LazyClassInitializer init;
    protected transient Source fromSource;
    protected Type superType;
    protected List interfaces;
    protected List methods;
    protected List fields;
    protected List constructors;
    protected List memberClasses;
    protected Package package_;
    protected Flags flags;
    protected ClassType.Kind kind;
    protected String name;
    protected ClassType outer;
    protected boolean inStaticContext;
    static Class class$polyglot$types$ConstructorInstance;
    static Class class$polyglot$types$Type;
    static Class class$polyglot$types$MethodInstance;
    static Class class$polyglot$types$FieldInstance;

    protected ParsedClassType_c() {
        this.inStaticContext = false;
    }

    public ParsedClassType_c(TypeSystem ts, LazyClassInitializer init, Source fromSource) {
        super(ts);
        this.inStaticContext = false;
        this.init = init;
        this.fromSource = fromSource;
        if (init == null) {
            throw new InternalCompilerError("Null lazy class initializer");
        }
    }

    @Override // polyglot.types.ParsedClassType
    public Source fromSource() {
        return this.fromSource;
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.types.ClassType
    public ClassType.Kind kind() {
        return this.kind;
    }

    @Override // polyglot.types.ParsedClassType
    public void inStaticContext(boolean inStaticContext) {
        this.inStaticContext = inStaticContext;
    }

    @Override // polyglot.types.ClassType
    public boolean inStaticContext() {
        return this.inStaticContext;
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.types.ClassType
    public ClassType outer() {
        if (isTopLevel()) {
            return null;
        }
        if (this.outer == null) {
            throw new InternalCompilerError("Nested classes must have outer classes.");
        }
        return this.outer;
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.types.Named
    public String name() {
        if (isAnonymous()) {
            throw new InternalCompilerError("Anonymous classes cannot have names.");
        }
        if (this.name == null) {
            throw new InternalCompilerError("Non-anonymous classes must have names.");
        }
        return this.name;
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.ext.jl.types.ReferenceType_c, polyglot.types.ReferenceType
    public Type superType() {
        return this.superType;
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.types.Importable
    public Package package_() {
        return this.package_;
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.types.MemberInstance
    public Flags flags() {
        if (isAnonymous()) {
            return Flags.NONE;
        }
        return this.flags;
    }

    protected boolean initialized() {
        return (this.methods == null || this.constructors == null || this.fields == null || this.memberClasses == null || this.interfaces == null) ? false : true;
    }

    protected void freeInit() {
        if (initialized()) {
            this.init = null;
        } else if (this.init == null) {
            throw new InternalCompilerError("Null lazy class initializer");
        }
    }

    @Override // polyglot.types.ParsedClassType
    public void flags(Flags flags) {
        this.flags = flags;
    }

    @Override // polyglot.types.ParsedClassType
    public void kind(ClassType.Kind kind) {
        this.kind = kind;
    }

    @Override // polyglot.types.ParsedClassType
    public void outer(ClassType outer) {
        if (isTopLevel()) {
            throw new InternalCompilerError("Top-level classes cannot have outer classes.");
        }
        this.outer = outer;
    }

    @Override // polyglot.types.ParsedClassType
    public void name(String name) {
        if (isAnonymous()) {
            throw new InternalCompilerError("Anonymous classes cannot have names.");
        }
        this.name = name;
    }

    @Override // polyglot.types.ParsedClassType
    public void position(Position pos) {
        this.position = pos;
    }

    @Override // polyglot.types.ParsedClassType
    public void package_(Package p) {
        this.package_ = p;
    }

    @Override // polyglot.types.ParsedClassType
    public void superType(Type t) {
        this.superType = t;
    }

    @Override // polyglot.types.ParsedClassType
    public void addInterface(Type t) {
        interfaces().add(t);
    }

    @Override // polyglot.types.ParsedClassType
    public void addMethod(MethodInstance mi) {
        methods().add(mi);
    }

    @Override // polyglot.types.ParsedClassType
    public void addConstructor(ConstructorInstance ci) {
        constructors().add(ci);
    }

    @Override // polyglot.types.ParsedClassType
    public void addField(FieldInstance fi) {
        fields().add(fi);
    }

    @Override // polyglot.types.ParsedClassType
    public void addMemberClass(ClassType t) {
        memberClasses().add(t);
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.types.ClassType
    public List constructors() {
        Class cls;
        if (this.constructors == null) {
            LinkedList linkedList = new LinkedList();
            if (class$polyglot$types$ConstructorInstance == null) {
                cls = class$("polyglot.types.ConstructorInstance");
                class$polyglot$types$ConstructorInstance = cls;
            } else {
                cls = class$polyglot$types$ConstructorInstance;
            }
            this.constructors = new TypedList(linkedList, cls, false);
            this.init.initConstructors(this);
            freeInit();
        }
        return this.constructors;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.types.ClassType
    public List memberClasses() {
        Class cls;
        if (this.memberClasses == null) {
            LinkedList linkedList = new LinkedList();
            if (class$polyglot$types$Type == null) {
                cls = class$("polyglot.types.Type");
                class$polyglot$types$Type = cls;
            } else {
                cls = class$polyglot$types$Type;
            }
            this.memberClasses = new TypedList(linkedList, cls, false);
            this.init.initMemberClasses(this);
            freeInit();
        }
        return this.memberClasses;
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.ext.jl.types.ReferenceType_c, polyglot.types.ReferenceType
    public List methods() {
        Class cls;
        if (this.methods == null) {
            LinkedList linkedList = new LinkedList();
            if (class$polyglot$types$MethodInstance == null) {
                cls = class$("polyglot.types.MethodInstance");
                class$polyglot$types$MethodInstance = cls;
            } else {
                cls = class$polyglot$types$MethodInstance;
            }
            this.methods = new TypedList(linkedList, cls, false);
            this.init.initMethods(this);
            freeInit();
        }
        return this.methods;
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.ext.jl.types.ReferenceType_c, polyglot.types.ReferenceType
    public List fields() {
        Class cls;
        if (this.fields == null) {
            LinkedList linkedList = new LinkedList();
            if (class$polyglot$types$FieldInstance == null) {
                cls = class$("polyglot.types.FieldInstance");
                class$polyglot$types$FieldInstance = cls;
            } else {
                cls = class$polyglot$types$FieldInstance;
            }
            this.fields = new TypedList(linkedList, cls, false);
            this.init.initFields(this);
            freeInit();
        }
        return this.fields;
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.ext.jl.types.ReferenceType_c, polyglot.types.ReferenceType
    public List interfaces() {
        Class cls;
        if (this.interfaces == null) {
            LinkedList linkedList = new LinkedList();
            if (class$polyglot$types$Type == null) {
                cls = class$("polyglot.types.Type");
                class$polyglot$types$Type = cls;
            } else {
                cls = class$polyglot$types$Type;
            }
            this.interfaces = new TypedList(linkedList, cls, false);
            this.init.initInterfaces(this);
            freeInit();
        }
        return this.interfaces;
    }
}
