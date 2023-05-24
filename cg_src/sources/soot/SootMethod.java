package soot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.dava.DavaBody;
import soot.dava.toolkits.base.renamer.RemoveFullyQualifiedName;
import soot.dotnet.members.DotnetMethod;
import soot.jimple.Jimple;
import soot.options.Options;
import soot.tagkit.AbstractHost;
import soot.util.IterableSet;
import soot.util.Numberable;
import soot.util.NumberedString;
/* loaded from: gencallgraphv3.jar:soot/SootMethod.class */
public class SootMethod extends AbstractHost implements ClassMember, Numberable, MethodOrMethodContext, SootMethodInterface {
    private static final Logger logger = LoggerFactory.getLogger(SootMethod.class);
    public static final String constructorName = "<init>";
    public static final String staticInitializerName = "<clinit>";
    protected String name;
    protected Type[] parameterTypes;
    protected Type returnType;
    protected boolean isDeclared;
    protected SootClass declaringClass;
    protected int modifiers;
    protected boolean isPhantom;
    protected List<SootClass> exceptions;
    protected volatile Body activeBody;
    protected volatile MethodSource ms;
    protected volatile String sig;
    protected volatile String subSig;
    protected NumberedString subsignature;
    protected int number;

    public SootMethod(String name, List<Type> parameterTypes, Type returnType) {
        this(name, parameterTypes, returnType, 0, Collections.emptyList());
    }

    public SootMethod(String name, List<Type> parameterTypes, Type returnType, int modifiers) {
        this(name, parameterTypes, returnType, modifiers, Collections.emptyList());
    }

    public SootMethod(String name, List<Type> parameterTypes, Type returnType, int modifiers, List<SootClass> thrownExceptions) {
        this.isPhantom = false;
        this.number = 0;
        this.name = name;
        if (parameterTypes != null && !parameterTypes.isEmpty()) {
            this.parameterTypes = (Type[]) parameterTypes.toArray(new Type[parameterTypes.size()]);
        }
        this.returnType = returnType;
        this.modifiers = modifiers;
        if (thrownExceptions != null && !thrownExceptions.isEmpty()) {
            this.exceptions = new ArrayList(thrownExceptions);
        }
        this.subsignature = Scene.v().getSubSigNumberer().findOrAdd(getSubSignature());
    }

    public int equivHashCode() {
        return (this.returnType.hashCode() * 101) + (this.modifiers * 17) + this.name.hashCode();
    }

    @Override // soot.SootMethodInterface
    public String getName() {
        return this.name;
    }

    public synchronized void setName(String name) {
        boolean wasDeclared = this.isDeclared;
        SootClass oldDeclaringClass = this.declaringClass;
        if (wasDeclared) {
            oldDeclaringClass.removeMethod(this);
        }
        this.name = name;
        this.sig = null;
        this.subSig = null;
        this.subsignature = Scene.v().getSubSigNumberer().findOrAdd(getSubSignature());
        if (wasDeclared) {
            oldDeclaringClass.addMethod(this);
        }
    }

    public synchronized void setDeclaringClass(SootClass declClass) {
        if (declClass != null) {
            Scene.v().getMethodNumberer().add(this);
        }
        this.declaringClass = declClass;
        this.sig = null;
    }

    @Override // soot.ClassMember
    public SootClass getDeclaringClass() {
        if (!this.isDeclared) {
            throw new RuntimeException("not declared: " + getName());
        }
        return this.declaringClass;
    }

    public void setDeclared(boolean isDeclared) {
        this.isDeclared = isDeclared;
    }

    @Override // soot.ClassMember
    public boolean isDeclared() {
        return this.isDeclared;
    }

    @Override // soot.ClassMember
    public boolean isPhantom() {
        return this.isPhantom;
    }

    public boolean isConcrete() {
        return (isPhantom() || isAbstract() || isNative()) ? false : true;
    }

    @Override // soot.ClassMember
    public void setPhantom(boolean value) {
        if (value) {
            if (!Scene.v().allowsPhantomRefs()) {
                throw new RuntimeException("Phantom refs not allowed");
            }
            if (!Options.v().allow_phantom_elms() && this.declaringClass != null && !this.declaringClass.isPhantom()) {
                throw new RuntimeException("Declaring class would have to be phantom");
            }
        }
        this.isPhantom = value;
    }

    @Override // soot.ClassMember
    public int getModifiers() {
        return this.modifiers;
    }

    @Override // soot.ClassMember
    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    @Override // soot.SootMethodInterface
    public Type getReturnType() {
        return this.returnType;
    }

    public synchronized void setReturnType(Type t) {
        boolean wasDeclared = this.isDeclared;
        SootClass oldDeclaringClass = this.declaringClass;
        if (wasDeclared) {
            oldDeclaringClass.removeMethod(this);
        }
        this.returnType = t;
        this.sig = null;
        this.subSig = null;
        this.subsignature = Scene.v().getSubSigNumberer().findOrAdd(getSubSignature());
        if (wasDeclared) {
            oldDeclaringClass.addMethod(this);
        }
    }

    public int getParameterCount() {
        if (this.parameterTypes == null) {
            return 0;
        }
        return this.parameterTypes.length;
    }

    @Override // soot.SootMethodInterface
    public Type getParameterType(int n) {
        return this.parameterTypes[n];
    }

    @Override // soot.SootMethodInterface
    public List<Type> getParameterTypes() {
        return this.parameterTypes == null ? Collections.emptyList() : Arrays.asList(this.parameterTypes);
    }

    public synchronized void setParameterTypes(List<Type> l) {
        boolean wasDeclared = this.isDeclared;
        SootClass oldDeclaringClass = this.declaringClass;
        if (wasDeclared) {
            oldDeclaringClass.removeMethod(this);
        }
        this.parameterTypes = (Type[]) l.toArray(new Type[l.size()]);
        this.sig = null;
        this.subSig = null;
        this.subsignature = Scene.v().getSubSigNumberer().findOrAdd(getSubSignature());
        if (wasDeclared) {
            oldDeclaringClass.addMethod(this);
        }
    }

    public MethodSource getSource() {
        return this.ms;
    }

    public synchronized void setSource(MethodSource ms) {
        this.ms = ms;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Body getActiveBody() {
        Body activeBody = this.activeBody;
        if (activeBody != null) {
            return activeBody;
        }
        synchronized (this) {
            Body activeBody2 = this.activeBody;
            if (activeBody2 != null) {
                return activeBody2;
            }
            if (this.declaringClass != null) {
                this.declaringClass.checkLevel(3);
            }
            if ((this.declaringClass != null && this.declaringClass.isPhantomClass()) || isPhantom()) {
                throw new RuntimeException("cannot get active body for phantom method: " + getSignature());
            }
            if (soot.jbco.Main.metrics) {
                return null;
            }
            throw new RuntimeException("no active body present for method " + getSignature());
        }
    }

    public synchronized void setActiveBody(Body body) {
        if (this.declaringClass != null && this.declaringClass.isPhantomClass()) {
            throw new RuntimeException("cannot set active body for phantom class! " + this);
        }
        setPhantom(false);
        if (!isConcrete()) {
            throw new RuntimeException("cannot set body for non-concrete method! " + this);
        }
        if (body != null) {
            body.setMethod(this);
        }
        this.activeBody = body;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Body retrieveActiveBody() {
        Body activeBody = this.activeBody;
        if (activeBody != null) {
            return activeBody;
        }
        synchronized (this) {
            Body activeBody2 = this.activeBody;
            if (activeBody2 != null) {
                return activeBody2;
            }
            if (this.declaringClass != null) {
                this.declaringClass.checkLevel(3);
            }
            if ((this.declaringClass != null && this.declaringClass.isPhantomClass()) || isPhantom()) {
                throw new RuntimeException("cannot get resident body for phantom method : " + this);
            }
            if (this.ms == null) {
                throw new RuntimeException("No method source set for method " + this);
            }
            Body activeBody3 = this.ms.getBody(this, "jb");
            setActiveBody(activeBody3);
            if (Options.v().drop_bodies_after_load()) {
                this.ms = null;
            }
            return activeBody3;
        }
    }

    public boolean hasActiveBody() {
        return this.activeBody != null;
    }

    public synchronized void releaseActiveBody() {
        this.activeBody = null;
    }

    public void addExceptionIfAbsent(SootClass e) {
        if (!throwsException(e)) {
            addException(e);
        }
    }

    public void addException(SootClass e) {
        logger.trace("Adding exception {}", e);
        if (this.exceptions == null) {
            this.exceptions = new ArrayList();
        } else if (this.exceptions.contains(e)) {
            throw new RuntimeException("already throws exception " + e.getName());
        }
        this.exceptions.add(e);
    }

    public void removeException(SootClass e) {
        logger.trace("Removing exception {}", e);
        if (this.exceptions == null || !this.exceptions.contains(e)) {
            throw new RuntimeException("does not throw exception " + e.getName());
        }
        this.exceptions.remove(e);
    }

    public boolean throwsException(SootClass e) {
        return this.exceptions != null && this.exceptions.contains(e);
    }

    public void setExceptions(List<SootClass> exceptions) {
        this.exceptions = (exceptions == null || exceptions.isEmpty()) ? null : new ArrayList(exceptions);
    }

    public List<SootClass> getExceptions() {
        if (this.exceptions == null) {
            this.exceptions = new ArrayList();
        }
        return this.exceptions;
    }

    public List<SootClass> getExceptionsUnsafe() {
        return this.exceptions;
    }

    @Override // soot.ClassMember
    public boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    @Override // soot.ClassMember
    public boolean isPrivate() {
        return Modifier.isPrivate(getModifiers());
    }

    @Override // soot.ClassMember
    public boolean isPublic() {
        return Modifier.isPublic(getModifiers());
    }

    @Override // soot.ClassMember
    public boolean isProtected() {
        return Modifier.isProtected(getModifiers());
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(getModifiers());
    }

    public boolean isFinal() {
        return Modifier.isFinal(getModifiers());
    }

    public boolean isNative() {
        return Modifier.isNative(getModifiers());
    }

    public boolean isSynchronized() {
        return Modifier.isSynchronized(getModifiers());
    }

    public boolean isMain() {
        if (isPublic() && isStatic()) {
            return Scene.v().getSubSigNumberer().findOrAdd(Options.v().src_prec() != 7 ? JavaMethods.SIG_MAIN : DotnetMethod.MAIN_METHOD_SIGNATURE).equals(this.subsignature);
        }
        return false;
    }

    public boolean isConstructor() {
        return "<init>".equals(this.name);
    }

    public boolean isStaticInitializer() {
        return "<clinit>".equals(this.name);
    }

    public boolean isEntryMethod() {
        if (isStatic() && "<clinit>".equals(this.subsignature.getString())) {
            return true;
        }
        return isMain();
    }

    public boolean isJavaLibraryMethod() {
        return getDeclaringClass().isJavaLibraryClass();
    }

    public String getBytecodeParms() {
        StringBuilder buffer = new StringBuilder();
        for (Type type : getParameterTypes()) {
            buffer.append(AbstractJasminClass.jasminDescriptorOf(type));
        }
        return buffer.toString().intern();
    }

    public String getBytecodeSignature() {
        return ('<' + Scene.v().quotedNameOf(getDeclaringClass().getName()) + ": " + getName() + AbstractJasminClass.jasminDescriptorOf(makeRef()) + '>').intern();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v9 */
    @Override // soot.SootMethodInterface
    public String getSignature() {
        String sig = this.sig;
        if (sig == null) {
            ?? r0 = this;
            synchronized (r0) {
                sig = this.sig;
                if (sig == null) {
                    String signature = getSignature(getDeclaringClass(), getSubSignature());
                    sig = signature;
                    this.sig = signature;
                }
                r0 = r0;
            }
        }
        return sig;
    }

    public static String getSignature(SootClass cl, String name, List<Type> params, Type returnType) {
        return getSignature(cl, getSubSignatureImpl(name, params, returnType));
    }

    public static String getSignature(SootClass cl, String subSignature) {
        return '<' + Scene.v().quotedNameOf(cl.getName()) + ": " + subSignature + '>';
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v9 */
    public String getSubSignature() {
        String subSig = this.subSig;
        if (subSig == null) {
            ?? r0 = this;
            synchronized (r0) {
                subSig = this.subSig;
                if (subSig == null) {
                    String subSignatureImpl = getSubSignatureImpl(getName(), getParameterTypes(), getReturnType());
                    subSig = subSignatureImpl;
                    this.subSig = subSignatureImpl;
                }
                r0 = r0;
            }
        }
        return subSig;
    }

    public static String getSubSignature(String name, List<Type> params, Type returnType) {
        return getSubSignatureImpl(name, params, returnType);
    }

    private static String getSubSignatureImpl(String name, List<Type> params, Type returnType) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(returnType.toQuotedString());
        buffer.append(' ');
        buffer.append(Scene.v().quotedNameOf(name));
        buffer.append('(');
        if (params != null) {
            int e = params.size();
            for (int i = 0; i < e; i++) {
                if (i > 0) {
                    buffer.append(',');
                }
                buffer.append(params.get(i).toQuotedString());
            }
        }
        buffer.append(')');
        return buffer.toString();
    }

    public NumberedString getNumberedSubSignature() {
        return this.subsignature;
    }

    public String toString() {
        return getSignature();
    }

    public String getDavaDeclaration() {
        if ("<clinit>".equals(getName())) {
            return Jimple.STATIC;
        }
        StringBuilder buffer = new StringBuilder();
        StringTokenizer st = new StringTokenizer(Modifier.toString(getModifiers()));
        if (st.hasMoreTokens()) {
            buffer.append(st.nextToken());
            while (st.hasMoreTokens()) {
                buffer.append(' ').append(st.nextToken());
            }
        }
        if (buffer.length() != 0) {
            buffer.append(' ');
        }
        if ("<init>".equals(getName())) {
            buffer.append(getDeclaringClass().getShortJavaStyleName());
        } else {
            Type t = getReturnType();
            String tempString = t.toString();
            if (hasActiveBody()) {
                DavaBody body = (DavaBody) getActiveBody();
                IterableSet<String> importSet = body.getImportList();
                if (!importSet.contains(tempString)) {
                    body.addToImportList(tempString);
                }
                tempString = RemoveFullyQualifiedName.getReducedName(importSet, tempString, t);
            }
            buffer.append(tempString).append(' ');
            buffer.append(Scene.v().quotedNameOf(getName()));
        }
        int count = 0;
        buffer.append('(');
        Iterator<Type> typeIt = getParameterTypes().iterator();
        while (typeIt.hasNext()) {
            Type t2 = typeIt.next();
            String tempString2 = t2.toString();
            if (hasActiveBody()) {
                DavaBody body2 = (DavaBody) getActiveBody();
                IterableSet<String> importSet2 = body2.getImportList();
                if (!importSet2.contains(tempString2)) {
                    body2.addToImportList(tempString2);
                }
                tempString2 = RemoveFullyQualifiedName.getReducedName(importSet2, tempString2, t2);
            }
            buffer.append(tempString2).append(' ');
            buffer.append(' ');
            if (hasActiveBody()) {
                int i = count;
                count++;
                buffer.append(((DavaBody) getActiveBody()).get_ParamMap().get(Integer.valueOf(i)));
            } else if (t2 == BooleanType.v()) {
                int i2 = count;
                count++;
                buffer.append('z').append(i2);
            } else if (t2 == ByteType.v()) {
                int i3 = count;
                count++;
                buffer.append('b').append(i3);
            } else if (t2 == ShortType.v()) {
                int i4 = count;
                count++;
                buffer.append('s').append(i4);
            } else if (t2 == CharType.v()) {
                int i5 = count;
                count++;
                buffer.append('c').append(i5);
            } else if (t2 == IntType.v()) {
                int i6 = count;
                count++;
                buffer.append('i').append(i6);
            } else if (t2 == LongType.v()) {
                int i7 = count;
                count++;
                buffer.append('l').append(i7);
            } else if (t2 == DoubleType.v()) {
                int i8 = count;
                count++;
                buffer.append('d').append(i8);
            } else if (t2 == FloatType.v()) {
                int i9 = count;
                count++;
                buffer.append('f').append(i9);
            } else if (t2 == StmtAddressType.v()) {
                int i10 = count;
                count++;
                buffer.append('a').append(i10);
            } else if (t2 == ErroneousType.v()) {
                int i11 = count;
                count++;
                buffer.append('e').append(i11);
            } else if (t2 == NullType.v()) {
                int i12 = count;
                count++;
                buffer.append('n').append(i12);
            } else {
                int i13 = count;
                count++;
                buffer.append('r').append(i13);
            }
            if (typeIt.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append(')');
        if (this.exceptions != null) {
            Iterator<SootClass> exceptionIt = getExceptions().iterator();
            if (exceptionIt.hasNext()) {
                buffer.append(" throws ").append(exceptionIt.next().getName());
                while (exceptionIt.hasNext()) {
                    buffer.append(", ").append(exceptionIt.next().getName());
                }
            }
        }
        return buffer.toString().intern();
    }

    public String getDeclaration() {
        StringBuilder buffer = new StringBuilder();
        StringTokenizer st = new StringTokenizer(Modifier.toString(getModifiers()));
        if (st.hasMoreTokens()) {
            buffer.append(st.nextToken());
            while (st.hasMoreTokens()) {
                buffer.append(' ').append(st.nextToken());
            }
        }
        if (buffer.length() != 0) {
            buffer.append(' ');
        }
        buffer.append(getReturnType().toQuotedString()).append(' ');
        buffer.append(Scene.v().quotedNameOf(getName()));
        buffer.append('(');
        Iterator<Type> typeIt = getParameterTypes().iterator();
        while (typeIt.hasNext()) {
            Type t = typeIt.next();
            buffer.append(t.toQuotedString());
            if (typeIt.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append(')');
        if (this.exceptions != null) {
            Iterator<SootClass> exceptionIt = getExceptions().iterator();
            if (exceptionIt.hasNext()) {
                buffer.append(" throws ").append(Scene.v().quotedNameOf(exceptionIt.next().getName()));
                while (exceptionIt.hasNext()) {
                    buffer.append(", ").append(Scene.v().quotedNameOf(exceptionIt.next().getName()));
                }
            }
        }
        return buffer.toString().intern();
    }

    @Override // soot.util.Numberable
    public final int getNumber() {
        return this.number;
    }

    @Override // soot.util.Numberable
    public final void setNumber(int number) {
        this.number = number;
    }

    @Override // soot.MethodOrMethodContext
    public SootMethod method() {
        return this;
    }

    @Override // soot.MethodOrMethodContext
    public Context context() {
        return null;
    }

    public SootMethodRef makeRef() {
        return Scene.v().makeMethodRef(this.declaringClass, this.name, this.parameterTypes == null ? null : Arrays.asList(this.parameterTypes), this.returnType, isStatic());
    }

    public boolean isValidResolve(SootMethodRef ref) {
        return isStatic() == ref.isStatic() && Objects.equals(getDeclaringClass(), ref.getDeclaringClass()) && Objects.equals(getName(), ref.getName()) && Objects.equals(getReturnType(), ref.getReturnType()) && Objects.equals(getParameterTypes(), ref.getParameterTypes());
    }

    @Override // soot.tagkit.AbstractHost, soot.tagkit.Host
    public int getJavaSourceStartLineNumber() {
        super.getJavaSourceStartLineNumber();
        if (this.line == -1 && hasActiveBody()) {
            Iterator<Unit> it = getActiveBody().getUnits().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Unit u = it.next();
                int l = u.getJavaSourceStartLineNumber();
                if (l > -1) {
                    this.line = l - 1;
                    break;
                }
            }
        }
        return this.line;
    }
}
