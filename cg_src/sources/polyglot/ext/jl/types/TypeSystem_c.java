package polyglot.ext.jl.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.Source;
import polyglot.main.Report;
import polyglot.types.ArrayType;
import polyglot.types.CachingResolver;
import polyglot.types.ClassContextResolver;
import polyglot.types.ClassType;
import polyglot.types.CompoundResolver;
import polyglot.types.ConstructorInstance;
import polyglot.types.Context;
import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.ImportTable;
import polyglot.types.InitializerInstance;
import polyglot.types.LazyClassInitializer;
import polyglot.types.LoadedClassResolver;
import polyglot.types.LocalInstance;
import polyglot.types.MemberInstance;
import polyglot.types.MethodInstance;
import polyglot.types.Named;
import polyglot.types.NoClassException;
import polyglot.types.NoMemberException;
import polyglot.types.NullType;
import polyglot.types.Package;
import polyglot.types.PackageContextResolver;
import polyglot.types.ParsedClassType;
import polyglot.types.PrimitiveType;
import polyglot.types.ProcedureInstance;
import polyglot.types.ReferenceType;
import polyglot.types.Resolver;
import polyglot.types.SemanticException;
import polyglot.types.TableResolver;
import polyglot.types.TopLevelResolver;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.types.UnknownPackage;
import polyglot.types.UnknownQualifier;
import polyglot.types.UnknownType;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.StringUtil;
import soot.JavaBasicTypes;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/TypeSystem_c.class */
public class TypeSystem_c implements TypeSystem {
    protected TopLevelResolver systemResolver;
    protected TableResolver parsedResolver;
    protected LoadedClassResolver loadedResolver;
    protected Map flagsForName;
    protected ClassType OBJECT_;
    protected ClassType CLASS_;
    protected ClassType STRING_;
    protected ClassType THROWABLE_;
    protected LazyClassInitializer defaultClassInit;
    protected final NullType NULL_ = createNull();
    protected final PrimitiveType VOID_ = createPrimitive(PrimitiveType.VOID);
    protected final PrimitiveType BOOLEAN_ = createPrimitive(PrimitiveType.BOOLEAN);
    protected final PrimitiveType CHAR_ = createPrimitive(PrimitiveType.CHAR);
    protected final PrimitiveType BYTE_ = createPrimitive(PrimitiveType.BYTE);
    protected final PrimitiveType SHORT_ = createPrimitive(PrimitiveType.SHORT);
    protected final PrimitiveType INT_ = createPrimitive(PrimitiveType.INT);
    protected final PrimitiveType LONG_ = createPrimitive(PrimitiveType.LONG);
    protected final PrimitiveType FLOAT_ = createPrimitive(PrimitiveType.FLOAT);
    protected final PrimitiveType DOUBLE_ = createPrimitive(PrimitiveType.DOUBLE);
    protected UnknownType unknownType = new UnknownType_c(this);
    protected UnknownPackage unknownPackage = new UnknownPackage_c(this);
    protected UnknownQualifier unknownQualifier = new UnknownQualifier_c(this);
    protected final Flags ACCESS_FLAGS = Public().Protected().Private();
    protected final Flags LOCAL_FLAGS = Final();
    protected final Flags FIELD_FLAGS = this.ACCESS_FLAGS.Static().Final().Transient().Volatile();
    protected final Flags CONSTRUCTOR_FLAGS = this.ACCESS_FLAGS.Synchronized().Native();
    protected final Flags INITIALIZER_FLAGS = Static();
    protected final Flags METHOD_FLAGS = this.ACCESS_FLAGS.Abstract().Static().Final().Native().Synchronized().StrictFP();
    protected final Flags TOP_LEVEL_CLASS_FLAGS = this.ACCESS_FLAGS.clear(Private()).Abstract().Final().StrictFP().Interface();
    protected final Flags MEMBER_CLASS_FLAGS = this.ACCESS_FLAGS.Static().Abstract().Final().StrictFP().Interface();
    protected final Flags LOCAL_CLASS_FLAGS = this.TOP_LEVEL_CLASS_FLAGS.clear(this.ACCESS_FLAGS);

    @Override // polyglot.types.TypeSystem
    public void initialize(LoadedClassResolver loadedResolver, ExtensionInfo extInfo) throws SemanticException {
        if (Report.should_report(Report.types, 1)) {
            Report.report(1, new StringBuffer().append("Initializing ").append(getClass().getName()).toString());
        }
        this.parsedResolver = new TableResolver();
        this.loadedResolver = loadedResolver;
        CompoundResolver compoundResolver = new CompoundResolver(this.parsedResolver, loadedResolver);
        this.systemResolver = new CachingResolver(compoundResolver, extInfo);
        initFlags();
        initTypes();
    }

    protected void initTypes() throws SemanticException {
    }

    @Override // polyglot.types.TypeSystem
    public TopLevelResolver systemResolver() {
        return this.systemResolver;
    }

    @Override // polyglot.types.TypeSystem
    public TableResolver parsedResolver() {
        return this.parsedResolver;
    }

    @Override // polyglot.types.TypeSystem
    public LoadedClassResolver loadedResolver() {
        return this.loadedResolver;
    }

    @Override // polyglot.types.TypeSystem
    public ImportTable importTable(String sourceName, Package pkg) {
        assert_(pkg);
        return new ImportTable(this, this.systemResolver, pkg, sourceName);
    }

    @Override // polyglot.types.TypeSystem
    public ImportTable importTable(Package pkg) {
        assert_(pkg);
        return new ImportTable(this, this.systemResolver, pkg);
    }

    @Override // polyglot.types.TypeSystem
    public boolean packageExists(String name) {
        return this.systemResolver.packageExists(name);
    }

    protected void assert_(Collection l) {
        Iterator i = l.iterator();
        while (i.hasNext()) {
            TypeObject o = (TypeObject) i.next();
            assert_(o);
        }
    }

    protected void assert_(TypeObject o) {
        if (o != null && o.typeSystem() != this) {
            throw new InternalCompilerError(new StringBuffer().append("we are ").append(this).append(" but ").append(o).append(" is from ").append(o.typeSystem()).toString());
        }
    }

    @Override // polyglot.types.TypeSystem
    public String wrapperTypeString(PrimitiveType t) {
        assert_(t);
        if (t.kind() == PrimitiveType.BOOLEAN) {
            return JavaBasicTypes.JAVA_LANG_BOOLEAN;
        }
        if (t.kind() == PrimitiveType.CHAR) {
            return JavaBasicTypes.JAVA_LANG_CHARACTER;
        }
        if (t.kind() == PrimitiveType.BYTE) {
            return JavaBasicTypes.JAVA_LANG_BYTE;
        }
        if (t.kind() == PrimitiveType.SHORT) {
            return JavaBasicTypes.JAVA_LANG_SHORT;
        }
        if (t.kind() == PrimitiveType.INT) {
            return JavaBasicTypes.JAVA_LANG_INTEGER;
        }
        if (t.kind() == PrimitiveType.LONG) {
            return JavaBasicTypes.JAVA_LANG_LONG;
        }
        if (t.kind() == PrimitiveType.FLOAT) {
            return JavaBasicTypes.JAVA_LANG_FLOAT;
        }
        if (t.kind() == PrimitiveType.DOUBLE) {
            return JavaBasicTypes.JAVA_LANG_DOUBLE;
        }
        if (t.kind() == PrimitiveType.VOID) {
            return "java.lang.Void";
        }
        throw new InternalCompilerError("Unrecognized primitive type.");
    }

    @Override // polyglot.types.TypeSystem
    public Context createContext() {
        return new Context_c(this);
    }

    @Override // polyglot.types.TypeSystem
    public Resolver packageContextResolver(Resolver cr, Package p) {
        assert_(p);
        return new PackageContextResolver(this, p, cr);
    }

    @Override // polyglot.types.TypeSystem
    public Resolver classContextResolver(ClassType type) {
        assert_(type);
        return new ClassContextResolver(this, type);
    }

    @Override // polyglot.types.TypeSystem
    public FieldInstance fieldInstance(Position pos, ReferenceType container, Flags flags, Type type, String name) {
        assert_(container);
        assert_(type);
        return new FieldInstance_c(this, pos, container, flags, type, name);
    }

    @Override // polyglot.types.TypeSystem
    public LocalInstance localInstance(Position pos, Flags flags, Type type, String name) {
        assert_(type);
        return new LocalInstance_c(this, pos, flags, type, name);
    }

    @Override // polyglot.types.TypeSystem
    public ConstructorInstance defaultConstructor(Position pos, ClassType container) {
        assert_(container);
        Flags access = Flags.NONE;
        if (container.flags().isPrivate()) {
            access = access.Private();
        }
        if (container.flags().isProtected()) {
            access = access.Protected();
        }
        if (container.flags().isPublic()) {
            access = access.Public();
        }
        return constructorInstance(pos, container, access, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    }

    @Override // polyglot.types.TypeSystem
    public ConstructorInstance constructorInstance(Position pos, ClassType container, Flags flags, List argTypes, List excTypes) {
        assert_(container);
        assert_(argTypes);
        assert_(excTypes);
        return new ConstructorInstance_c(this, pos, container, flags, argTypes, excTypes);
    }

    @Override // polyglot.types.TypeSystem
    public InitializerInstance initializerInstance(Position pos, ClassType container, Flags flags) {
        assert_(container);
        return new InitializerInstance_c(this, pos, container, flags);
    }

    @Override // polyglot.types.TypeSystem
    public MethodInstance methodInstance(Position pos, ReferenceType container, Flags flags, Type returnType, String name, List argTypes, List excTypes) {
        assert_(container);
        assert_(returnType);
        assert_(argTypes);
        assert_(excTypes);
        return new MethodInstance_c(this, pos, container, flags, returnType, name, argTypes, excTypes);
    }

    @Override // polyglot.types.TypeSystem
    public boolean descendsFrom(Type child, Type ancestor) {
        assert_(child);
        assert_(ancestor);
        return child.descendsFromImpl(ancestor);
    }

    @Override // polyglot.types.TypeSystem
    public boolean isCastValid(Type fromType, Type toType) {
        assert_(fromType);
        assert_(toType);
        return fromType.isCastValidImpl(toType);
    }

    @Override // polyglot.types.TypeSystem
    public boolean isImplicitCastValid(Type fromType, Type toType) {
        assert_(fromType);
        assert_(toType);
        return fromType.isImplicitCastValidImpl(toType);
    }

    @Override // polyglot.types.TypeSystem
    public boolean equals(TypeObject type1, TypeObject type2) {
        assert_(type1);
        assert_(type2);
        if (type1 instanceof TypeObject_c) {
            return ((TypeObject_c) type1).equalsImpl(type2);
        }
        throw new InternalCompilerError("Unknown implementation of TypeObject", type1.position());
    }

    @Override // polyglot.types.TypeSystem
    public boolean numericConversionValid(Type t, Object value) {
        assert_(t);
        return t.numericConversionValidImpl(value);
    }

    @Override // polyglot.types.TypeSystem
    public boolean numericConversionValid(Type t, long value) {
        assert_(t);
        return t.numericConversionValidImpl(value);
    }

    @Override // polyglot.types.TypeSystem
    public boolean isCanonical(Type type) {
        assert_(type);
        return type.isCanonical();
    }

    @Override // polyglot.types.TypeSystem
    public boolean isAccessible(MemberInstance mi, Context context) {
        return isAccessible(mi, context.currentClass());
    }

    protected boolean isAccessible(MemberInstance mi, ClassType contextClass) {
        assert_(mi);
        ReferenceType target = mi.container();
        Flags flags = mi.flags();
        if (!target.isClass()) {
            return flags.isPublic();
        }
        ClassType targetClass = target.toClass();
        if (!classAccessible(targetClass, contextClass)) {
            return false;
        }
        if (equals(targetClass, contextClass) || isEnclosed(contextClass, targetClass) || isEnclosed(targetClass, contextClass)) {
            return true;
        }
        ClassType ct = contextClass;
        while (!ct.isTopLevel()) {
            ct = ct.outer();
            if (isEnclosed(targetClass, ct)) {
                return true;
            }
        }
        if (flags.isProtected()) {
            if (descendsFrom(contextClass, targetClass)) {
                return true;
            }
            ClassType ct2 = contextClass;
            while (!ct2.isTopLevel()) {
                ct2 = ct2.outer();
                if (descendsFrom(ct2, targetClass)) {
                    return true;
                }
            }
        }
        return accessibleFromPackage(flags, targetClass.package_(), contextClass.package_());
    }

    @Override // polyglot.types.TypeSystem
    public boolean classAccessible(ClassType targetClass, Context context) {
        if (context.currentClass() == null) {
            return classAccessibleFromPackage(targetClass, context.importTable().package_());
        }
        return classAccessible(targetClass, context.currentClass());
    }

    protected boolean classAccessible(ClassType targetClass, ClassType contextClass) {
        assert_(targetClass);
        if (targetClass.isMember()) {
            return isAccessible(targetClass, contextClass);
        }
        if (!targetClass.isTopLevel() || equals(targetClass, contextClass) || isEnclosed(contextClass, targetClass)) {
            return true;
        }
        return accessibleFromPackage(targetClass.flags(), targetClass.package_(), contextClass.package_());
    }

    @Override // polyglot.types.TypeSystem
    public boolean classAccessibleFromPackage(ClassType targetClass, Package pkg) {
        assert_(targetClass);
        if (!targetClass.isTopLevel() && !targetClass.isMember()) {
            return false;
        }
        Flags flags = targetClass.flags();
        if (targetClass.isMember()) {
            if (!targetClass.container().isClass()) {
                return flags.isPublic();
            }
            if (!classAccessibleFromPackage(targetClass.container().toClass(), pkg)) {
                return false;
            }
        }
        return accessibleFromPackage(flags, targetClass.package_(), pkg);
    }

    protected boolean accessibleFromPackage(Flags flags, Package pkg1, Package pkg2) {
        if (flags.isPublic()) {
            return true;
        }
        if (flags.isPackage() || flags.isProtected()) {
            if (pkg1 == null && pkg2 == null) {
                return true;
            }
            if (pkg1 != null && pkg1.equals(pkg2)) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // polyglot.types.TypeSystem
    public boolean isEnclosed(ClassType inner, ClassType outer) {
        return inner.isEnclosedImpl(outer);
    }

    @Override // polyglot.types.TypeSystem
    public boolean hasEnclosingInstance(ClassType inner, ClassType encl) {
        return inner.hasEnclosingInstanceImpl(encl);
    }

    @Override // polyglot.types.TypeSystem
    public void checkCycles(ReferenceType goal) throws SemanticException {
        checkCycles(goal, goal);
    }

    protected void checkCycles(ReferenceType curr, ReferenceType goal) throws SemanticException {
        assert_(curr);
        assert_(goal);
        if (curr == null) {
            return;
        }
        ReferenceType superType = null;
        if (curr.superType() != null) {
            superType = curr.superType().toReference();
        }
        if (goal == superType) {
            throw new SemanticException(new StringBuffer().append("Circular inheritance involving ").append(goal).toString(), curr.position());
        }
        checkCycles(superType, goal);
        for (Type si : curr.interfaces()) {
            if (si == goal) {
                throw new SemanticException(new StringBuffer().append("Circular inheritance involving ").append(goal).toString(), curr.position());
            }
            checkCycles(si.toReference(), goal);
        }
        if (curr.isClass()) {
            checkCycles(curr.toClass().outer(), goal);
        }
    }

    @Override // polyglot.types.TypeSystem
    public boolean canCoerceToString(Type t, Context c) {
        return !t.isVoid();
    }

    @Override // polyglot.types.TypeSystem
    public boolean isThrowable(Type type) {
        assert_(type);
        return type.isThrowable();
    }

    @Override // polyglot.types.TypeSystem
    public boolean isUncheckedException(Type type) {
        assert_(type);
        return type.isUncheckedException();
    }

    @Override // polyglot.types.TypeSystem
    public Collection uncheckedExceptions() {
        List l = new ArrayList(2);
        l.add(Error());
        l.add(RuntimeException());
        return l;
    }

    @Override // polyglot.types.TypeSystem
    public boolean isSubtype(Type t1, Type t2) {
        assert_(t1);
        assert_(t2);
        return t1.isSubtypeImpl(t2);
    }

    @Override // polyglot.types.TypeSystem
    public FieldInstance findField(ReferenceType container, String name, Context c) throws SemanticException {
        ClassType ct = null;
        if (c != null) {
            ct = c.currentClass();
        }
        return findField(container, name, ct);
    }

    @Override // polyglot.types.TypeSystem
    public FieldInstance findField(ReferenceType container, String name, ClassType currClass) throws SemanticException {
        Collection fields = findFields(container, name);
        if (fields.size() == 0) {
            throw new NoMemberException(3, new StringBuffer().append("Field \"").append(name).append("\" not found in type \"").append(container).append("\".").toString());
        }
        Iterator i = fields.iterator();
        FieldInstance fi = (FieldInstance) i.next();
        if (i.hasNext()) {
            FieldInstance fi2 = (FieldInstance) i.next();
            throw new SemanticException(new StringBuffer().append("Field \"").append(name).append("\" is ambiguous; it is defined in both ").append(fi.container()).append(" and ").append(fi2.container()).append(".").toString());
        } else if (currClass != null && !isAccessible(fi, currClass)) {
            throw new SemanticException(new StringBuffer().append("Cannot access ").append(fi).append(".").toString());
        } else {
            return fi;
        }
    }

    @Override // polyglot.types.TypeSystem
    public FieldInstance findField(ReferenceType container, String name) throws SemanticException {
        return findField(container, name, (ClassType) null);
    }

    protected Set findFields(ReferenceType container, String name) {
        assert_(container);
        if (container == null) {
            throw new InternalCompilerError(new StringBuffer().append("Cannot access field \"").append(name).append("\" within a null container type.").toString());
        }
        FieldInstance fi = container.fieldNamed(name);
        if (fi != null) {
            return Collections.singleton(fi);
        }
        Set fields = new HashSet();
        if (container.superType() != null && container.superType().isReference()) {
            Set superFields = findFields(container.superType().toReference(), name);
            fields.addAll(superFields);
        }
        if (container.isClass()) {
            ClassType ct = container.toClass();
            for (Type it : ct.interfaces()) {
                Set superFields2 = findFields(it.toReference(), name);
                fields.addAll(superFields2);
            }
        }
        return fields;
    }

    @Override // polyglot.types.TypeSystem
    public ClassType findMemberClass(ClassType container, String name, Context c) throws SemanticException {
        return findMemberClass(container, name, c.currentClass());
    }

    @Override // polyglot.types.TypeSystem
    public ClassType findMemberClass(ClassType container, String name, ClassType currClass) throws SemanticException {
        assert_(container);
        Set s = findMemberClasses(container, name);
        if (s.size() == 0) {
            throw new NoClassException(name, container);
        }
        Iterator i = s.iterator();
        ClassType t = (ClassType) i.next();
        if (i.hasNext()) {
            ClassType t2 = (ClassType) i.next();
            throw new SemanticException(new StringBuffer().append("Member type \"").append(name).append("\" is ambiguous; it is defined in both ").append(t.container()).append(" and ").append(t2.container()).append(".").toString());
        } else if (currClass != null && !isAccessible(t, currClass)) {
            throw new SemanticException(new StringBuffer().append("Cannot access member type \"").append(t).append("\".").toString());
        } else {
            return t;
        }
    }

    public Set findMemberClasses(ClassType container, String name) throws SemanticException {
        assert_(container);
        ClassType mt = container.memberClassNamed(name);
        if (mt != null) {
            if (!mt.isMember()) {
                throw new InternalCompilerError(new StringBuffer().append("Class ").append(mt).append(" is not a member class, ").append(" but is in ").append(container).append("'s list of members.").toString());
            }
            if (mt.outer() != container) {
                throw new InternalCompilerError(new StringBuffer().append("Class ").append(mt).append(" has outer class ").append(mt.outer()).append(" but is a member of ").append(container).toString());
            }
            return Collections.singleton(mt);
        }
        Set memberClasses = new HashSet();
        if (container.superType() != null) {
            Set s = findMemberClasses(container.superType().toClass(), name);
            memberClasses.addAll(s);
        }
        for (Type it : container.interfaces()) {
            Set s2 = findMemberClasses(it.toClass(), name);
            memberClasses.addAll(s2);
        }
        return memberClasses;
    }

    @Override // polyglot.types.TypeSystem
    public ClassType findMemberClass(ClassType container, String name) throws SemanticException {
        return findMemberClass(container, name, (ClassType) null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String listToString(List l) {
        StringBuffer sb = new StringBuffer();
        Iterator i = l.iterator();
        while (i.hasNext()) {
            Object o = i.next();
            sb.append(o.toString());
            if (i.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override // polyglot.types.TypeSystem
    public MethodInstance findMethod(ReferenceType container, String name, List argTypes, Context c) throws SemanticException {
        return findMethod(container, name, argTypes, c.currentClass());
    }

    @Override // polyglot.types.TypeSystem
    public boolean hasMethodNamed(ReferenceType container, String name) {
        assert_(container);
        if (container == null) {
            throw new InternalCompilerError(new StringBuffer().append("Cannot access method \"").append(name).append("\" within a null container type.").toString());
        }
        if (!container.methodsNamed(name).isEmpty()) {
            return true;
        }
        if (container.superType() != null && container.superType().isReference() && hasMethodNamed(container.superType().toReference(), name)) {
            return true;
        }
        if (container.isClass()) {
            ClassType ct = container.toClass();
            for (Type it : ct.interfaces()) {
                if (hasMethodNamed(it.toReference(), name)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // polyglot.types.TypeSystem
    public MethodInstance findMethod(ReferenceType container, String name, List argTypes, ClassType currClass) throws SemanticException {
        assert_(container);
        assert_(argTypes);
        List acceptable = findAcceptableMethods(container, name, argTypes, currClass);
        if (acceptable.size() == 0) {
            throw new NoMemberException(1, new StringBuffer().append("No valid method call found for ").append(name).append("(").append(listToString(argTypes)).append(")").append(" in ").append(container).append(".").toString());
        }
        MethodInstance mi = (MethodInstance) findProcedure(acceptable, container, argTypes, currClass);
        if (mi == null) {
            throw new SemanticException(new StringBuffer().append("Reference to ").append(name).append(" is ambiguous, multiple methods match: ").append(acceptable).toString());
        }
        return mi;
    }

    @Override // polyglot.types.TypeSystem
    public ConstructorInstance findConstructor(ClassType container, List argTypes, Context c) throws SemanticException {
        return findConstructor(container, argTypes, c.currentClass());
    }

    @Override // polyglot.types.TypeSystem
    public ConstructorInstance findConstructor(ClassType container, List argTypes, ClassType currClass) throws SemanticException {
        assert_(container);
        assert_(argTypes);
        List acceptable = findAcceptableConstructors(container, argTypes, currClass);
        if (acceptable.size() == 0) {
            throw new NoMemberException(2, new StringBuffer().append("No valid constructor found for ").append(container).append("(").append(listToString(argTypes)).append(").").toString());
        }
        ConstructorInstance ci = (ConstructorInstance) findProcedure(acceptable, container, argTypes, currClass);
        if (ci == null) {
            throw new NoMemberException(2, new StringBuffer().append("Reference to ").append(container).append(" is ambiguous, multiple ").append("constructors match: ").append(acceptable).toString());
        }
        return ci;
    }

    protected ProcedureInstance findProcedure(List acceptable, ReferenceType container, List argTypes, ClassType currClass) throws SemanticException {
        Collection maximal = findMostSpecificProcedures(acceptable, container, argTypes, currClass);
        if (maximal.size() == 1) {
            return (ProcedureInstance) maximal.iterator().next();
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected Collection findMostSpecificProcedures(List acceptable, ReferenceType container, List argTypes, ClassType currClass) throws SemanticException {
        assert_(container);
        assert_(argTypes);
        MostSpecificComparator msc = new MostSpecificComparator(this);
        Collections.sort(acceptable, msc);
        List<ProcedureInstance> maximal = new ArrayList(acceptable.size());
        Iterator i = acceptable.iterator();
        ProcedureInstance first = (ProcedureInstance) i.next();
        maximal.add(first);
        while (i.hasNext()) {
            ProcedureInstance p = (ProcedureInstance) i.next();
            if (msc.compare(first, p) >= 0) {
                maximal.add(p);
            }
        }
        if (maximal.size() > 1) {
            List notAbstract = new ArrayList(maximal.size());
            for (ProcedureInstance p2 : maximal) {
                if (!p2.flags().isAbstract()) {
                    notAbstract.add(p2);
                }
            }
            if (notAbstract.size() == 1) {
                maximal = notAbstract;
            } else if (notAbstract.size() == 0) {
                Iterator j = maximal.iterator();
                ProcedureInstance first2 = (ProcedureInstance) j.next();
                while (j.hasNext()) {
                    if (!first2.hasFormals(((ProcedureInstance) j.next()).formalTypes())) {
                        return maximal;
                    }
                }
                maximal = Collections.singletonList(first2);
            }
        }
        return maximal;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/TypeSystem_c$MostSpecificComparator.class */
    public class MostSpecificComparator implements Comparator {
        private final TypeSystem_c this$0;

        protected MostSpecificComparator(TypeSystem_c this$0) {
            this.this$0 = this$0;
        }

        @Override // java.util.Comparator
        public int compare(Object o1, Object o2) {
            ProcedureInstance p1 = (ProcedureInstance) o1;
            ProcedureInstance p2 = (ProcedureInstance) o2;
            if (this.this$0.moreSpecific(p1, p2)) {
                return -1;
            }
            if (this.this$0.moreSpecific(p2, p1)) {
                return 1;
            }
            if (p1.flags().isAbstract() == p2.flags().isAbstract()) {
                return 0;
            }
            if (p1.flags().isAbstract()) {
                return 1;
            }
            return -1;
        }
    }

    protected List findAcceptableMethods(ReferenceType container, String name, List argTypes, ClassType currClass) throws SemanticException {
        assert_(container);
        assert_(argTypes);
        List acceptable = new ArrayList();
        List<MethodInstance> unacceptable = new ArrayList();
        Set visitedTypes = new HashSet();
        LinkedList typeQueue = new LinkedList();
        typeQueue.addLast(container);
        while (!typeQueue.isEmpty()) {
            Type type = (Type) typeQueue.removeFirst();
            if (!visitedTypes.contains(type)) {
                visitedTypes.add(type);
                if (Report.should_report(Report.types, 2)) {
                    Report.report(2, new StringBuffer().append("Searching type ").append(type).append(" for method ").append(name).append("(").append(listToString(argTypes)).append(")").toString());
                }
                if (!type.isReference()) {
                    throw new SemanticException(new StringBuffer().append("Cannot call method in  non-reference type ").append(type).append(".").toString());
                }
                for (MethodInstance mi : type.toReference().methods()) {
                    if (Report.should_report(Report.types, 3)) {
                        Report.report(3, new StringBuffer().append("Trying ").append(mi).toString());
                    }
                    if (methodCallValid(mi, name, argTypes)) {
                        if (isAccessible(mi, currClass)) {
                            if (Report.should_report(Report.types, 3)) {
                                Report.report(3, new StringBuffer().append("->acceptable: ").append(mi).append(" in ").append(mi.container()).toString());
                            }
                            acceptable.add(mi);
                        } else {
                            unacceptable.add(mi);
                        }
                    }
                }
                if (type.toReference().superType() != null) {
                    typeQueue.addLast(type.toReference().superType());
                }
                typeQueue.addAll(type.toReference().interfaces());
            }
        }
        for (MethodInstance mi2 : unacceptable) {
            acceptable.removeAll(mi2.overrides());
        }
        return acceptable;
    }

    protected List findAcceptableConstructors(ClassType container, List argTypes, ClassType currClass) throws SemanticException {
        assert_(container);
        assert_(argTypes);
        List acceptable = new ArrayList();
        if (Report.should_report(Report.types, 2)) {
            Report.report(2, new StringBuffer().append("Searching type ").append(container).append(" for constructor ").append(container).append("(").append(listToString(argTypes)).append(")").toString());
        }
        for (ConstructorInstance ci : container.constructors()) {
            if (Report.should_report(Report.types, 3)) {
                Report.report(3, new StringBuffer().append("Trying ").append(ci).toString());
            }
            if (callValid(ci, argTypes) && isAccessible(ci, currClass)) {
                if (Report.should_report(Report.types, 3)) {
                    Report.report(3, new StringBuffer().append("->acceptable: ").append(ci).toString());
                }
                acceptable.add(ci);
            }
        }
        return acceptable;
    }

    @Override // polyglot.types.TypeSystem
    public boolean moreSpecific(ProcedureInstance p1, ProcedureInstance p2) {
        return p1.moreSpecificImpl(p2);
    }

    @Override // polyglot.types.TypeSystem
    public Type superType(ReferenceType type) {
        assert_(type);
        return type.superType();
    }

    @Override // polyglot.types.TypeSystem
    public List interfaces(ReferenceType type) {
        assert_(type);
        return type.interfaces();
    }

    @Override // polyglot.types.TypeSystem
    public Type leastCommonAncestor(Type type1, Type type2) throws SemanticException {
        assert_(type1);
        assert_(type2);
        if (equals(type1, type2)) {
            return type1;
        }
        if (type1.isNumeric() && type2.isNumeric()) {
            if (isImplicitCastValid(type1, type2)) {
                return type2;
            }
            if (isImplicitCastValid(type2, type1)) {
                return type1;
            }
            if ((type1.isChar() && type2.isByte()) || (type1.isByte() && type2.isChar())) {
                return Int();
            }
            if ((type1.isChar() && type2.isShort()) || (type1.isShort() && type2.isChar())) {
                return Int();
            }
        }
        if (type1.isArray() && type2.isArray()) {
            return arrayOf(leastCommonAncestor(type1.toArray().base(), type2.toArray().base()));
        }
        if (type1.isReference() && type2.isNull()) {
            return type1;
        }
        if (type2.isReference() && type1.isNull()) {
            return type2;
        }
        if (type1.isReference() && type2.isReference()) {
            if (type1.isClass() && type1.toClass().flags().isInterface()) {
                return Object();
            }
            if (type2.isClass() && type2.toClass().flags().isInterface()) {
                return Object();
            }
            if (equals(type1, Object())) {
                return type1;
            }
            if (!equals(type2, Object()) && !isSubtype(type1, type2)) {
                if (isSubtype(type2, type1)) {
                    return type1;
                }
                Type t1 = leastCommonAncestor(type1.toReference().superType(), type2);
                Type t2 = leastCommonAncestor(type2.toReference().superType(), type1);
                return equals(t1, t2) ? t1 : Object();
            }
            return type2;
        }
        throw new SemanticException(new StringBuffer().append("No least common ancestor found for types \"").append(type1).append("\" and \"").append(type2).append("\".").toString());
    }

    @Override // polyglot.types.TypeSystem
    public boolean throwsSubset(ProcedureInstance p1, ProcedureInstance p2) {
        assert_(p1);
        assert_(p2);
        return p1.throwsSubsetImpl(p2);
    }

    @Override // polyglot.types.TypeSystem
    public boolean hasFormals(ProcedureInstance pi, List formalTypes) {
        assert_(pi);
        assert_(formalTypes);
        return pi.hasFormalsImpl(formalTypes);
    }

    @Override // polyglot.types.TypeSystem
    public boolean hasMethod(ReferenceType t, MethodInstance mi) {
        assert_(t);
        assert_(mi);
        return t.hasMethodImpl(mi);
    }

    @Override // polyglot.types.TypeSystem
    public List overrides(MethodInstance mi) {
        return mi.overridesImpl();
    }

    @Override // polyglot.types.TypeSystem
    public List implemented(MethodInstance mi) {
        return mi.implementedImpl(mi.container());
    }

    @Override // polyglot.types.TypeSystem
    public boolean canOverride(MethodInstance mi, MethodInstance mj) {
        try {
            return mi.canOverrideImpl(mj, true);
        } catch (SemanticException e) {
            throw new InternalCompilerError(e);
        }
    }

    @Override // polyglot.types.TypeSystem
    public void checkOverride(MethodInstance mi, MethodInstance mj) throws SemanticException {
        mi.canOverrideImpl(mj, false);
    }

    @Override // polyglot.types.TypeSystem
    public boolean isSameMethod(MethodInstance m1, MethodInstance m2) {
        assert_(m1);
        assert_(m2);
        return m1.isSameMethodImpl(m2);
    }

    @Override // polyglot.types.TypeSystem
    public boolean methodCallValid(MethodInstance prototype, String name, List argTypes) {
        assert_(prototype);
        assert_(argTypes);
        return prototype.methodCallValidImpl(name, argTypes);
    }

    @Override // polyglot.types.TypeSystem
    public boolean callValid(ProcedureInstance prototype, List argTypes) {
        assert_(prototype);
        assert_(argTypes);
        return prototype.callValidImpl(argTypes);
    }

    @Override // polyglot.types.TypeSystem
    public NullType Null() {
        return this.NULL_;
    }

    @Override // polyglot.types.TypeSystem
    public PrimitiveType Void() {
        return this.VOID_;
    }

    @Override // polyglot.types.TypeSystem
    public PrimitiveType Boolean() {
        return this.BOOLEAN_;
    }

    @Override // polyglot.types.TypeSystem
    public PrimitiveType Char() {
        return this.CHAR_;
    }

    @Override // polyglot.types.TypeSystem
    public PrimitiveType Byte() {
        return this.BYTE_;
    }

    @Override // polyglot.types.TypeSystem
    public PrimitiveType Short() {
        return this.SHORT_;
    }

    @Override // polyglot.types.TypeSystem
    public PrimitiveType Int() {
        return this.INT_;
    }

    @Override // polyglot.types.TypeSystem
    public PrimitiveType Long() {
        return this.LONG_;
    }

    @Override // polyglot.types.TypeSystem
    public PrimitiveType Float() {
        return this.FLOAT_;
    }

    @Override // polyglot.types.TypeSystem
    public PrimitiveType Double() {
        return this.DOUBLE_;
    }

    protected ClassType load(String name) {
        try {
            return (ClassType) typeForName(name);
        } catch (SemanticException e) {
            throw new InternalCompilerError(new StringBuffer().append("Cannot find class \"").append(name).append("\"; ").append(e.getMessage()).toString(), e);
        }
    }

    @Override // polyglot.types.TypeSystem
    public Named forName(String name) throws SemanticException {
        try {
            return this.systemResolver.find(name);
        } catch (SemanticException e) {
            if (!StringUtil.isNameShort(name)) {
                String containerName = StringUtil.getPackageComponent(name);
                String shortName = StringUtil.getShortNameComponent(name);
                try {
                    Named container = forName(containerName);
                    if (container instanceof ClassType) {
                        return classContextResolver((ClassType) container).find(shortName);
                    }
                } catch (SemanticException e2) {
                }
            }
            throw e;
        }
    }

    @Override // polyglot.types.TypeSystem
    public Type typeForName(String name) throws SemanticException {
        return (Type) forName(name);
    }

    @Override // polyglot.types.TypeSystem
    public ClassType Object() {
        if (this.OBJECT_ != null) {
            return this.OBJECT_;
        }
        ClassType load = load(JavaBasicTypes.JAVA_LANG_OBJECT);
        this.OBJECT_ = load;
        return load;
    }

    @Override // polyglot.types.TypeSystem
    public ClassType Class() {
        if (this.CLASS_ != null) {
            return this.CLASS_;
        }
        ClassType load = load("java.lang.Class");
        this.CLASS_ = load;
        return load;
    }

    @Override // polyglot.types.TypeSystem
    public ClassType String() {
        if (this.STRING_ != null) {
            return this.STRING_;
        }
        ClassType load = load("java.lang.String");
        this.STRING_ = load;
        return load;
    }

    @Override // polyglot.types.TypeSystem
    public ClassType Throwable() {
        if (this.THROWABLE_ != null) {
            return this.THROWABLE_;
        }
        ClassType load = load("java.lang.Throwable");
        this.THROWABLE_ = load;
        return load;
    }

    @Override // polyglot.types.TypeSystem
    public ClassType Error() {
        return load("java.lang.Error");
    }

    @Override // polyglot.types.TypeSystem
    public ClassType Exception() {
        return load("java.lang.Exception");
    }

    @Override // polyglot.types.TypeSystem
    public ClassType RuntimeException() {
        return load("java.lang.RuntimeException");
    }

    @Override // polyglot.types.TypeSystem
    public ClassType Cloneable() {
        return load("java.lang.Cloneable");
    }

    @Override // polyglot.types.TypeSystem
    public ClassType Serializable() {
        return load(JavaBasicTypes.JAVA_IO_SERIALIZABLE);
    }

    @Override // polyglot.types.TypeSystem
    public ClassType NullPointerException() {
        return load("java.lang.NullPointerException");
    }

    @Override // polyglot.types.TypeSystem
    public ClassType ClassCastException() {
        return load("java.lang.ClassCastException");
    }

    @Override // polyglot.types.TypeSystem
    public ClassType OutOfBoundsException() {
        return load("java.lang.ArrayIndexOutOfBoundsException");
    }

    @Override // polyglot.types.TypeSystem
    public ClassType ArrayStoreException() {
        return load("java.lang.ArrayStoreException");
    }

    @Override // polyglot.types.TypeSystem
    public ClassType ArithmeticException() {
        return load("java.lang.ArithmeticException");
    }

    protected NullType createNull() {
        return new NullType_c(this);
    }

    protected PrimitiveType createPrimitive(PrimitiveType.Kind kind) {
        return new PrimitiveType_c(this, kind);
    }

    @Override // polyglot.types.TypeSystem
    public Object placeHolder(TypeObject o) {
        assert_(o);
        return placeHolder(o, new HashSet());
    }

    @Override // polyglot.types.TypeSystem
    public Object placeHolder(TypeObject o, Set roots) {
        assert_(o);
        if (o instanceof ClassType) {
            ClassType ct = (ClassType) o;
            if (ct.isLocal() || ct.isAnonymous()) {
                throw new InternalCompilerError(new StringBuffer().append("Cannot serialize ").append(o).append(".").toString());
            }
            return new PlaceHolder_c(ct);
        }
        return o;
    }

    @Override // polyglot.types.TypeSystem
    public UnknownType unknownType(Position pos) {
        return this.unknownType;
    }

    @Override // polyglot.types.TypeSystem
    public UnknownPackage unknownPackage(Position pos) {
        return this.unknownPackage;
    }

    @Override // polyglot.types.TypeSystem
    public UnknownQualifier unknownQualifier(Position pos) {
        return this.unknownQualifier;
    }

    @Override // polyglot.types.TypeSystem
    public Package packageForName(Package prefix, String name) throws SemanticException {
        return createPackage(prefix, name);
    }

    @Override // polyglot.types.TypeSystem
    public Package packageForName(String name) throws SemanticException {
        if (name == null || name.equals("")) {
            return null;
        }
        String s = StringUtil.getShortNameComponent(name);
        String p = StringUtil.getPackageComponent(name);
        return packageForName(packageForName(p), s);
    }

    @Override // polyglot.types.TypeSystem
    public Package createPackage(Package prefix, String name) {
        assert_(prefix);
        return new Package_c(this, prefix, name);
    }

    @Override // polyglot.types.TypeSystem
    public Package createPackage(String name) {
        if (name == null || name.equals("")) {
            return null;
        }
        String s = StringUtil.getShortNameComponent(name);
        String p = StringUtil.getPackageComponent(name);
        return createPackage(createPackage(p), s);
    }

    @Override // polyglot.types.TypeSystem
    public ArrayType arrayOf(Type type) {
        assert_(type);
        return arrayOf(type.position(), type);
    }

    @Override // polyglot.types.TypeSystem
    public ArrayType arrayOf(Position pos, Type type) {
        assert_(type);
        return arrayType(pos, type);
    }

    protected ArrayType arrayType(Position pos, Type type) {
        return new ArrayType_c(this, pos, type);
    }

    @Override // polyglot.types.TypeSystem
    public ArrayType arrayOf(Type type, int dims) {
        return arrayOf(null, type, dims);
    }

    @Override // polyglot.types.TypeSystem
    public ArrayType arrayOf(Position pos, Type type, int dims) {
        if (dims > 1) {
            return arrayOf(pos, arrayOf(pos, type, dims - 1));
        }
        if (dims == 1) {
            return arrayOf(pos, type);
        }
        throw new InternalCompilerError("Must call arrayOf(type, dims) with dims > 0");
    }

    public Type typeForClass(Class clazz) throws SemanticException {
        if (clazz == Void.TYPE) {
            return this.VOID_;
        }
        if (clazz == Boolean.TYPE) {
            return this.BOOLEAN_;
        }
        if (clazz == Byte.TYPE) {
            return this.BYTE_;
        }
        if (clazz == Character.TYPE) {
            return this.CHAR_;
        }
        if (clazz == Short.TYPE) {
            return this.SHORT_;
        }
        if (clazz == Integer.TYPE) {
            return this.INT_;
        }
        if (clazz == Long.TYPE) {
            return this.LONG_;
        }
        if (clazz == Float.TYPE) {
            return this.FLOAT_;
        }
        if (clazz == Double.TYPE) {
            return this.DOUBLE_;
        }
        if (clazz.isArray()) {
            return arrayOf(typeForClass(clazz.getComponentType()));
        }
        return (Type) this.systemResolver.find(clazz.getName());
    }

    @Override // polyglot.types.TypeSystem
    public Set getTypeEncoderRootSet(Type t) {
        return Collections.singleton(t);
    }

    @Override // polyglot.types.TypeSystem
    public String getTransformedClassName(ClassType ct) {
        StringBuffer sb = new StringBuffer(ct.fullName().length());
        if (!ct.isMember() && !ct.isTopLevel()) {
            return null;
        }
        while (ct.isMember()) {
            sb.insert(0, ct.name());
            sb.insert(0, '$');
            ct = ct.outer();
            if (!ct.isMember() && !ct.isTopLevel()) {
                return null;
            }
        }
        sb.insert(0, ct.fullName());
        return sb.toString();
    }

    @Override // polyglot.types.TypeSystem
    public String translatePackage(Resolver c, Package p) {
        return p.translate(c);
    }

    @Override // polyglot.types.TypeSystem
    public String translateArray(Resolver c, ArrayType t) {
        return t.translate(c);
    }

    @Override // polyglot.types.TypeSystem
    public String translateClass(Resolver c, ClassType t) {
        return t.translate(c);
    }

    @Override // polyglot.types.TypeSystem
    public String translatePrimitive(Resolver c, PrimitiveType t) {
        return t.translate(c);
    }

    @Override // polyglot.types.TypeSystem
    public PrimitiveType primitiveForName(String name) throws SemanticException {
        if (name.equals(Jimple.VOID)) {
            return Void();
        }
        if (name.equals("boolean")) {
            return Boolean();
        }
        if (name.equals("char")) {
            return Char();
        }
        if (name.equals("byte")) {
            return Byte();
        }
        if (name.equals("short")) {
            return Short();
        }
        if (name.equals("int")) {
            return Int();
        }
        if (name.equals("long")) {
            return Long();
        }
        if (name.equals(Jimple.FLOAT)) {
            return Float();
        }
        if (name.equals("double")) {
            return Double();
        }
        throw new SemanticException(new StringBuffer().append("Unrecognized primitive type \"").append(name).append("\".").toString());
    }

    @Override // polyglot.types.TypeSystem
    public LazyClassInitializer defaultClassInitializer() {
        if (this.defaultClassInit == null) {
            this.defaultClassInit = new LazyClassInitializer_c(this);
        }
        return this.defaultClassInit;
    }

    @Override // polyglot.types.TypeSystem
    public final ParsedClassType createClassType() {
        return createClassType(defaultClassInitializer(), null);
    }

    @Override // polyglot.types.TypeSystem
    public final ParsedClassType createClassType(Source fromSource) {
        return createClassType(defaultClassInitializer(), fromSource);
    }

    @Override // polyglot.types.TypeSystem
    public final ParsedClassType createClassType(LazyClassInitializer init) {
        return createClassType(init, null);
    }

    @Override // polyglot.types.TypeSystem
    public ParsedClassType createClassType(LazyClassInitializer init, Source fromSource) {
        return new ParsedClassType_c(this, init, fromSource);
    }

    @Override // polyglot.types.TypeSystem
    public List defaultPackageImports() {
        List l = new ArrayList(1);
        l.add("java.lang");
        return l;
    }

    @Override // polyglot.types.TypeSystem
    public PrimitiveType promote(Type t1, Type t2) throws SemanticException {
        if (!t1.isNumeric()) {
            throw new SemanticException(new StringBuffer().append("Cannot promote non-numeric type ").append(t1).toString());
        }
        if (!t2.isNumeric()) {
            throw new SemanticException(new StringBuffer().append("Cannot promote non-numeric type ").append(t2).toString());
        }
        return promoteNumeric(t1.toPrimitive(), t2.toPrimitive());
    }

    protected PrimitiveType promoteNumeric(PrimitiveType t1, PrimitiveType t2) {
        if (t1.isDouble() || t2.isDouble()) {
            return Double();
        }
        if (t1.isFloat() || t2.isFloat()) {
            return Float();
        }
        if (t1.isLong() || t2.isLong()) {
            return Long();
        }
        return Int();
    }

    @Override // polyglot.types.TypeSystem
    public PrimitiveType promote(Type t) throws SemanticException {
        if (!t.isNumeric()) {
            throw new SemanticException(new StringBuffer().append("Cannot promote non-numeric type ").append(t).toString());
        }
        return promoteNumeric(t.toPrimitive());
    }

    protected PrimitiveType promoteNumeric(PrimitiveType t) {
        if (t.isByte() || t.isShort() || t.isChar()) {
            return Int();
        }
        return t.toPrimitive();
    }

    @Override // polyglot.types.TypeSystem
    public void checkMethodFlags(Flags f) throws SemanticException {
        if (!f.clear(this.METHOD_FLAGS).equals(Flags.NONE)) {
            throw new SemanticException(new StringBuffer().append("Cannot declare method with flags ").append(f.clear(this.METHOD_FLAGS)).append(".").toString());
        }
        if (f.isAbstract() && f.isPrivate()) {
            throw new SemanticException("Cannot declare method that is both abstract and private.");
        }
        if (f.isAbstract() && f.isStatic()) {
            throw new SemanticException("Cannot declare method that is both abstract and static.");
        }
        if (f.isAbstract() && f.isFinal()) {
            throw new SemanticException("Cannot declare method that is both abstract and final.");
        }
        if (f.isAbstract() && f.isNative()) {
            throw new SemanticException("Cannot declare method that is both abstract and native.");
        }
        if (f.isAbstract() && f.isSynchronized()) {
            throw new SemanticException("Cannot declare method that is both abstract and synchronized.");
        }
        if (f.isAbstract() && f.isStrictFP()) {
            throw new SemanticException("Cannot declare method that is both abstract and strictfp.");
        }
        checkAccessFlags(f);
    }

    @Override // polyglot.types.TypeSystem
    public void checkLocalFlags(Flags f) throws SemanticException {
        if (!f.clear(this.LOCAL_FLAGS).equals(Flags.NONE)) {
            throw new SemanticException(new StringBuffer().append("Cannot declare local variable with flags ").append(f.clear(this.LOCAL_FLAGS)).append(".").toString());
        }
    }

    @Override // polyglot.types.TypeSystem
    public void checkFieldFlags(Flags f) throws SemanticException {
        if (!f.clear(this.FIELD_FLAGS).equals(Flags.NONE)) {
            throw new SemanticException(new StringBuffer().append("Cannot declare field with flags ").append(f.clear(this.FIELD_FLAGS)).append(".").toString());
        }
        checkAccessFlags(f);
    }

    @Override // polyglot.types.TypeSystem
    public void checkConstructorFlags(Flags f) throws SemanticException {
        if (!f.clear(this.CONSTRUCTOR_FLAGS).equals(Flags.NONE)) {
            throw new SemanticException(new StringBuffer().append("Cannot declare constructor with flags ").append(f.clear(this.CONSTRUCTOR_FLAGS)).append(".").toString());
        }
        checkAccessFlags(f);
    }

    @Override // polyglot.types.TypeSystem
    public void checkInitializerFlags(Flags f) throws SemanticException {
        if (!f.clear(this.INITIALIZER_FLAGS).equals(Flags.NONE)) {
            throw new SemanticException(new StringBuffer().append("Cannot declare initializer with flags ").append(f.clear(this.INITIALIZER_FLAGS)).append(".").toString());
        }
    }

    @Override // polyglot.types.TypeSystem
    public void checkTopLevelClassFlags(Flags f) throws SemanticException {
        if (!f.clear(this.TOP_LEVEL_CLASS_FLAGS).equals(Flags.NONE)) {
            throw new SemanticException(new StringBuffer().append("Cannot declare a top-level class with flag(s) ").append(f.clear(this.TOP_LEVEL_CLASS_FLAGS)).append(".").toString());
        }
        if (f.isFinal() && f.isInterface()) {
            throw new SemanticException("Cannot declare a final interface.");
        }
        checkAccessFlags(f);
    }

    @Override // polyglot.types.TypeSystem
    public void checkMemberClassFlags(Flags f) throws SemanticException {
        if (!f.clear(this.MEMBER_CLASS_FLAGS).equals(Flags.NONE)) {
            throw new SemanticException(new StringBuffer().append("Cannot declare a member class with flag(s) ").append(f.clear(this.MEMBER_CLASS_FLAGS)).append(".").toString());
        }
        if (f.isStrictFP() && f.isInterface()) {
            throw new SemanticException("Cannot declare a strictfp interface.");
        }
        if (f.isFinal() && f.isInterface()) {
            throw new SemanticException("Cannot declare a final interface.");
        }
        checkAccessFlags(f);
    }

    @Override // polyglot.types.TypeSystem
    public void checkLocalClassFlags(Flags f) throws SemanticException {
        if (f.isInterface()) {
            throw new SemanticException("Cannot declare a local interface.");
        }
        if (!f.clear(this.LOCAL_CLASS_FLAGS).equals(Flags.NONE)) {
            throw new SemanticException(new StringBuffer().append("Cannot declare a local class with flag(s) ").append(f.clear(this.LOCAL_CLASS_FLAGS)).append(".").toString());
        }
        checkAccessFlags(f);
    }

    @Override // polyglot.types.TypeSystem
    public void checkAccessFlags(Flags f) throws SemanticException {
        int count = 0;
        if (f.isPublic()) {
            count = 0 + 1;
        }
        if (f.isProtected()) {
            count++;
        }
        if (f.isPrivate()) {
            count++;
        }
        if (count > 1) {
            throw new SemanticException(new StringBuffer().append("Invalid access flags: ").append(f.retain(this.ACCESS_FLAGS)).append(".").toString());
        }
    }

    protected List abstractSuperInterfaces(ReferenceType rt) {
        List superInterfaces = new LinkedList();
        superInterfaces.add(rt);
        for (ClassType interf : rt.interfaces()) {
            superInterfaces.addAll(abstractSuperInterfaces(interf));
        }
        if (rt.superType() != null) {
            ClassType c = rt.superType().toClass();
            if (c.flags().isAbstract()) {
                superInterfaces.addAll(abstractSuperInterfaces(c));
            }
        }
        return superInterfaces;
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x0134  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0153 A[EDGE_INSN: B:69:0x0153->B:48:0x0153 ?: BREAK  , SYNTHETIC] */
    @Override // polyglot.types.TypeSystem
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void checkClassConformance(polyglot.types.ClassType r6) throws polyglot.types.SemanticException {
        /*
            Method dump skipped, instructions count: 439
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: polyglot.ext.jl.types.TypeSystem_c.checkClassConformance(polyglot.types.ClassType):void");
    }

    @Override // polyglot.types.TypeSystem
    public Type staticTarget(Type t) {
        return t;
    }

    protected void initFlags() {
        this.flagsForName = new HashMap();
        this.flagsForName.put(Jimple.PUBLIC, Flags.PUBLIC);
        this.flagsForName.put(Jimple.PRIVATE, Flags.PRIVATE);
        this.flagsForName.put(Jimple.PROTECTED, Flags.PROTECTED);
        this.flagsForName.put(Jimple.STATIC, Flags.STATIC);
        this.flagsForName.put(Jimple.FINAL, Flags.FINAL);
        this.flagsForName.put(Jimple.SYNCHRONIZED, Flags.SYNCHRONIZED);
        this.flagsForName.put(Jimple.TRANSIENT, Flags.TRANSIENT);
        this.flagsForName.put(Jimple.NATIVE, Flags.NATIVE);
        this.flagsForName.put("interface", Flags.INTERFACE);
        this.flagsForName.put(Jimple.ABSTRACT, Flags.ABSTRACT);
        this.flagsForName.put(Jimple.VOLATILE, Flags.VOLATILE);
        this.flagsForName.put(Jimple.STRICTFP, Flags.STRICTFP);
    }

    @Override // polyglot.types.TypeSystem
    public Flags createNewFlag(String name, Flags after) {
        Flags f = Flags.createFlag(name, name, name, after);
        this.flagsForName.put(name, f);
        return f;
    }

    @Override // polyglot.types.TypeSystem
    public Flags NoFlags() {
        return Flags.NONE;
    }

    @Override // polyglot.types.TypeSystem
    public Flags Public() {
        return Flags.PUBLIC;
    }

    @Override // polyglot.types.TypeSystem
    public Flags Private() {
        return Flags.PRIVATE;
    }

    @Override // polyglot.types.TypeSystem
    public Flags Protected() {
        return Flags.PROTECTED;
    }

    @Override // polyglot.types.TypeSystem
    public Flags Static() {
        return Flags.STATIC;
    }

    @Override // polyglot.types.TypeSystem
    public Flags Final() {
        return Flags.FINAL;
    }

    @Override // polyglot.types.TypeSystem
    public Flags Synchronized() {
        return Flags.SYNCHRONIZED;
    }

    @Override // polyglot.types.TypeSystem
    public Flags Transient() {
        return Flags.TRANSIENT;
    }

    @Override // polyglot.types.TypeSystem
    public Flags Native() {
        return Flags.NATIVE;
    }

    @Override // polyglot.types.TypeSystem
    public Flags Interface() {
        return Flags.INTERFACE;
    }

    @Override // polyglot.types.TypeSystem
    public Flags Abstract() {
        return Flags.ABSTRACT;
    }

    @Override // polyglot.types.TypeSystem
    public Flags Volatile() {
        return Flags.VOLATILE;
    }

    @Override // polyglot.types.TypeSystem
    public Flags StrictFP() {
        return Flags.STRICTFP;
    }

    @Override // polyglot.types.TypeSystem
    public Flags flagsForBits(int bits) {
        Flags f = Flags.NONE;
        if ((bits & 1) != 0) {
            f = f.Public();
        }
        if ((bits & 2) != 0) {
            f = f.Private();
        }
        if ((bits & 4) != 0) {
            f = f.Protected();
        }
        if ((bits & 8) != 0) {
            f = f.Static();
        }
        if ((bits & 16) != 0) {
            f = f.Final();
        }
        if ((bits & 32) != 0) {
            f = f.Synchronized();
        }
        if ((bits & 128) != 0) {
            f = f.Transient();
        }
        if ((bits & 256) != 0) {
            f = f.Native();
        }
        if ((bits & 512) != 0) {
            f = f.Interface();
        }
        if ((bits & 1024) != 0) {
            f = f.Abstract();
        }
        if ((bits & 64) != 0) {
            f = f.Volatile();
        }
        if ((bits & 2048) != 0) {
            f = f.StrictFP();
        }
        return f;
    }

    public Flags flagsForName(String name) {
        Flags f = (Flags) this.flagsForName.get(name);
        if (f == null) {
            throw new InternalCompilerError(new StringBuffer().append("No flag named \"").append(name).append("\".").toString());
        }
        return f;
    }

    public String toString() {
        return StringUtil.getShortNameComponent(getClass().getName());
    }
}
