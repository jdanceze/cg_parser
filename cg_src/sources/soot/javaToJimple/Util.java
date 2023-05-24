package soot.javaToJimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.tools.tar.TarConstants;
import polyglot.ast.Node;
import polyglot.types.ArrayType;
import polyglot.types.ClassType;
import polyglot.types.Flags;
import polyglot.types.Type;
import polyglot.util.IdentityKey;
import polyglot.util.Position;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FastHierarchy;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LocalGenerator;
import soot.LongType;
import soot.NullType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.UnitPatchingChain;
import soot.ValueBox;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.tagkit.EnclosingTag;
import soot.tagkit.Host;
import soot.tagkit.InnerClassTag;
import soot.tagkit.QualifyingTag;
import soot.tagkit.SourceLineNumberTag;
import soot.tagkit.SourceLnNamePosTag;
import soot.tagkit.SourceLnPosTag;
import soot.tagkit.SourcePositionTag;
import soot.tagkit.SyntheticTag;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/Util.class */
public class Util {
    public static void addInnerClassTag(SootClass sc, String innerName, String outerName, String simpleName, int access) {
        String innerName2 = innerName.replaceAll(".", "/");
        if (outerName != null) {
            outerName = outerName.replaceAll(".", "/");
        }
        sc.addTag(new InnerClassTag(innerName2, outerName, simpleName, access));
    }

    public static String getParamNameForClassLit(Type type) {
        String name;
        Type arrType;
        String fieldName;
        String name2 = "";
        if (type.isArray()) {
            int dims = ((ArrayType) type).dims();
            Type base = ((ArrayType) type).base();
            while (true) {
                arrType = base;
                if (!(arrType instanceof ArrayType)) {
                    break;
                }
                base = ((ArrayType) arrType).base();
            }
            if (arrType.isBoolean()) {
                fieldName = "Z";
            } else if (arrType.isByte()) {
                fieldName = "B";
            } else if (arrType.isChar()) {
                fieldName = "C";
            } else if (arrType.isDouble()) {
                fieldName = "D";
            } else if (arrType.isFloat()) {
                fieldName = "F";
            } else if (arrType.isInt()) {
                fieldName = "I";
            } else if (arrType.isLong()) {
                fieldName = "J";
            } else if (arrType.isShort()) {
                fieldName = "S";
            } else {
                String typeSt = getSootType(arrType).toString();
                fieldName = "L" + typeSt;
            }
            for (int i = 0; i < dims; i++) {
                name2 = String.valueOf(name2) + "[";
            }
            name = String.valueOf(name2) + fieldName;
            if (!arrType.isPrimitive()) {
                name = String.valueOf(name) + ";";
            }
        } else {
            name = getSootType(type).toString();
        }
        return name;
    }

    public static String getFieldNameForClassLit(Type type) {
        String fieldName;
        Type arrType;
        if (type.isArray()) {
            int dims = ((ArrayType) type).dims();
            Type base = ((ArrayType) type).base();
            while (true) {
                arrType = base;
                if (!(arrType instanceof ArrayType)) {
                    break;
                }
                base = ((ArrayType) arrType).base();
            }
            String fieldName2 = "array$";
            for (int i = 0; i < dims - 1; i++) {
                fieldName2 = String.valueOf(fieldName2) + "$";
            }
            if (arrType.isBoolean()) {
                fieldName = String.valueOf(fieldName2) + "Z";
            } else if (arrType.isByte()) {
                fieldName = String.valueOf(fieldName2) + "B";
            } else if (arrType.isChar()) {
                fieldName = String.valueOf(fieldName2) + "C";
            } else if (arrType.isDouble()) {
                fieldName = String.valueOf(fieldName2) + "D";
            } else if (arrType.isFloat()) {
                fieldName = String.valueOf(fieldName2) + "F";
            } else if (arrType.isInt()) {
                fieldName = String.valueOf(fieldName2) + "I";
            } else if (arrType.isLong()) {
                fieldName = String.valueOf(fieldName2) + "J";
            } else if (arrType.isShort()) {
                fieldName = String.valueOf(fieldName2) + "S";
            } else {
                String typeSt = getSootType(arrType).toString();
                fieldName = String.valueOf(fieldName2) + "L" + typeSt.replaceAll(".", "$");
            }
        } else {
            String typeSt2 = getSootType(type).toString();
            fieldName = String.valueOf("class$") + typeSt2.replaceAll(".", "$");
        }
        return fieldName;
    }

    public static String getSourceFileOfClass(SootClass sootClass) {
        String name = sootClass.getName();
        int index = name.indexOf("$");
        if (index != -1) {
            name = name.substring(0, index);
        }
        return name;
    }

    public static void addLnPosTags(Host host, Position pos) {
        if (pos != null && Options.v().keep_line_number()) {
            if (pos.file() != null) {
                host.addTag(new SourceLnNamePosTag(pos.file(), pos.line(), pos.endLine(), pos.column(), pos.endColumn()));
            } else {
                host.addTag(new SourceLnPosTag(pos.line(), pos.endLine(), pos.column(), pos.endColumn()));
            }
        }
    }

    public static void addLnPosTags(Host host, int sline, int eline, int spos, int epos) {
        if (Options.v().keep_line_number()) {
            host.addTag(new SourceLnPosTag(sline, eline, spos, epos));
        }
    }

    public static void addPosTag(Host host, Position pos) {
        if (pos != null) {
            addPosTag(host, pos.column(), pos.endColumn());
        }
    }

    public static void addMethodPosTag(Host meth, int start, int end) {
        meth.addTag(new SourcePositionTag(start, end));
    }

    public static void addPosTag(Host host, int sc, int ec) {
        host.addTag(new SourcePositionTag(sc, ec));
    }

    public static void addMethodLineTag(Host host, int sline, int eline) {
        if (Options.v().keep_line_number()) {
            host.addTag(new SourceLineNumberTag(sline, eline));
        }
    }

    public static void addLineTag(Host host, Node node) {
        if (Options.v().keep_line_number() && node.position() != null) {
            host.addTag(new SourceLineNumberTag(node.position().line(), node.position().line()));
        }
    }

    public static void addLineTag(Host host, int sLine, int eLine) {
        host.addTag(new SourceLineNumberTag(sLine, eLine));
    }

    public static Local getThis(soot.Type sootType, Body body, HashMap getThisMap, LocalGenerator lg) {
        if (InitialResolver.v().hierarchy() == null) {
            InitialResolver.v().hierarchy(new FastHierarchy());
        }
        FastHierarchy fh = InitialResolver.v().hierarchy();
        Local specialThisLocal = body.getThisLocal();
        if (specialThisLocal.getType().equals(sootType)) {
            getThisMap.put(sootType, specialThisLocal);
            return specialThisLocal;
        } else if (bodyHasLocal(body, sootType)) {
            Local l = getLocalOfType(body, sootType);
            getThisMap.put(sootType, l);
            return l;
        } else {
            SootClass classToInvoke = ((RefType) specialThisLocal.getType()).getSootClass();
            SootField outerThisField = classToInvoke.getFieldByName("this$0");
            Local t1 = lg.generateLocal(outerThisField.getType());
            FieldRef fieldRef = Jimple.v().newInstanceFieldRef(specialThisLocal, outerThisField.makeRef());
            AssignStmt fieldAssignStmt = Jimple.v().newAssignStmt(t1, fieldRef);
            body.getUnits().add((UnitPatchingChain) fieldAssignStmt);
            if (fh.canStoreType(t1.getType(), sootType)) {
                getThisMap.put(sootType, t1);
                return t1;
            }
            return getThisGivenOuter(sootType, getThisMap, body, lg, t1);
        }
    }

    private static Local getLocalOfType(Body body, soot.Type type) {
        FastHierarchy fh = InitialResolver.v().hierarchy();
        Iterator stmtsIt = body.getUnits().iterator();
        Local correctLocal = null;
        while (stmtsIt.hasNext()) {
            Stmt s = (Stmt) stmtsIt.next();
            if ((s instanceof IdentityStmt) && (s.hasTag(EnclosingTag.NAME) || s.hasTag(QualifyingTag.NAME))) {
                for (ValueBox vb : s.getDefBoxes()) {
                    if ((vb.getValue() instanceof Local) && fh.canStoreType(type, vb.getValue().getType())) {
                        correctLocal = (Local) vb.getValue();
                    }
                }
            }
        }
        return correctLocal;
    }

    private static boolean bodyHasLocal(Body body, soot.Type type) {
        FastHierarchy fh = InitialResolver.v().hierarchy();
        Iterator stmtsIt = body.getUnits().iterator();
        while (stmtsIt.hasNext()) {
            Stmt s = (Stmt) stmtsIt.next();
            if ((s instanceof IdentityStmt) && (s.hasTag(EnclosingTag.NAME) || s.hasTag(QualifyingTag.NAME))) {
                for (ValueBox vb : s.getDefBoxes()) {
                    if ((vb.getValue() instanceof Local) && fh.canStoreType(type, vb.getValue().getType())) {
                        return true;
                    }
                }
                continue;
            }
        }
        return false;
    }

    public static Local getThisGivenOuter(soot.Type sootType, HashMap getThisMap, Body body, LocalGenerator lg, Local t2) {
        if (InitialResolver.v().hierarchy() == null) {
            InitialResolver.v().hierarchy(new FastHierarchy());
        }
        FastHierarchy fh = InitialResolver.v().hierarchy();
        while (!fh.canStoreType(t2.getType(), sootType)) {
            SootClass classToInvoke = ((RefType) t2.getType()).getSootClass();
            SootMethod methToInvoke = makeOuterThisAccessMethod(classToInvoke);
            Local t3 = lg.generateLocal(methToInvoke.getReturnType());
            ArrayList methParams = new ArrayList();
            methParams.add(t2);
            Local res = getPrivateAccessFieldInvoke(methToInvoke.makeRef(), methParams, body, lg);
            AssignStmt assign = Jimple.v().newAssignStmt(t3, res);
            body.getUnits().add((UnitPatchingChain) assign);
            t2 = t3;
        }
        getThisMap.put(sootType, t2);
        return t2;
    }

    private static SootMethod makeOuterThisAccessMethod(SootClass classToInvoke) {
        String name = "access$" + InitialResolver.v().getNextPrivateAccessCounter() + TarConstants.VERSION_POSIX;
        ArrayList paramTypes = new ArrayList();
        paramTypes.add(classToInvoke.getType());
        SootMethod meth = Scene.v().makeSootMethod(name, paramTypes, classToInvoke.getFieldByName("this$0").getType(), 8);
        classToInvoke.addMethod(meth);
        PrivateFieldAccMethodSource src = new PrivateFieldAccMethodSource(classToInvoke.getFieldByName("this$0").getType(), "this$0", classToInvoke.getFieldByName("this$0").isStatic(), classToInvoke);
        meth.setActiveBody(src.getBody(meth, null));
        meth.addTag(new SyntheticTag());
        return meth;
    }

    public static Local getPrivateAccessFieldInvoke(SootMethodRef toInvoke, ArrayList params, Body body, LocalGenerator lg) {
        InvokeExpr invoke = Jimple.v().newStaticInvokeExpr(toInvoke, params);
        Local retLocal = lg.generateLocal(toInvoke.returnType());
        AssignStmt stmt = Jimple.v().newAssignStmt(retLocal, invoke);
        body.getUnits().add((UnitPatchingChain) stmt);
        return retLocal;
    }

    public static boolean isSubType(ClassType type, ClassType superType) {
        if (type.equals(superType)) {
            return true;
        }
        if (type.superType() == null) {
            return false;
        }
        return isSubType((ClassType) type.superType(), superType);
    }

    public static soot.Type getSootType(Type type) {
        String className;
        soot.Type sootType;
        Type polyglotBase;
        if (type == null) {
            throw new RuntimeException("Trying to get soot type for null polyglot type");
        }
        if (type.isInt()) {
            sootType = IntType.v();
        } else if (type.isArray()) {
            Type base = ((ArrayType) type).base();
            while (true) {
                polyglotBase = base;
                if (!(polyglotBase instanceof ArrayType)) {
                    break;
                }
                base = ((ArrayType) polyglotBase).base();
            }
            soot.Type baseType = getSootType(polyglotBase);
            int dims = ((ArrayType) type).dims();
            sootType = soot.ArrayType.v(baseType, dims);
        } else if (type.isBoolean()) {
            sootType = BooleanType.v();
        } else if (type.isByte()) {
            sootType = ByteType.v();
        } else if (type.isChar()) {
            sootType = CharType.v();
        } else if (type.isDouble()) {
            sootType = DoubleType.v();
        } else if (type.isFloat()) {
            sootType = FloatType.v();
        } else if (type.isLong()) {
            sootType = LongType.v();
        } else if (type.isShort()) {
            sootType = ShortType.v();
        } else if (type.isNull()) {
            sootType = NullType.v();
        } else if (type.isVoid()) {
            sootType = VoidType.v();
        } else if (type.isClass()) {
            ClassType classType = (ClassType) type;
            if (classType.isNested()) {
                if (classType.isAnonymous() && InitialResolver.v().getAnonTypeMap() != null && InitialResolver.v().getAnonTypeMap().containsKey(new IdentityKey(classType))) {
                    className = InitialResolver.v().getAnonTypeMap().get(new IdentityKey(classType));
                } else if (classType.isLocal() && InitialResolver.v().getLocalTypeMap() != null && InitialResolver.v().getLocalTypeMap().containsKey(new IdentityKey(classType))) {
                    className = InitialResolver.v().getLocalTypeMap().get(new IdentityKey(classType));
                } else {
                    String pkgName = "";
                    if (classType.package_() != null) {
                        pkgName = classType.package_().fullName();
                    }
                    className = classType.name();
                    if (classType.outer().isAnonymous() || classType.outer().isLocal()) {
                        className = String.valueOf(getSootType(classType.outer()).toString()) + "$" + className;
                    } else {
                        while (classType.outer() != null) {
                            className = String.valueOf(classType.outer().name()) + "$" + className;
                            classType = classType.outer();
                        }
                        if (!pkgName.equals("")) {
                            className = String.valueOf(pkgName) + "." + className;
                        }
                    }
                }
            } else {
                className = classType.fullName();
            }
            sootType = RefType.v(className);
        } else {
            throw new RuntimeException("Unknown Type");
        }
        return sootType;
    }

    public static int getModifier(Flags flags) {
        int modifier = 0;
        if (flags.isPublic()) {
            modifier = 0 | 1;
        }
        if (flags.isPrivate()) {
            modifier |= 2;
        }
        if (flags.isProtected()) {
            modifier |= 4;
        }
        if (flags.isFinal()) {
            modifier |= 16;
        }
        if (flags.isStatic()) {
            modifier |= 8;
        }
        if (flags.isNative()) {
            modifier |= 256;
        }
        if (flags.isAbstract()) {
            modifier |= 1024;
        }
        if (flags.isVolatile()) {
            modifier |= 64;
        }
        if (flags.isTransient()) {
            modifier |= 128;
        }
        if (flags.isSynchronized()) {
            modifier |= 32;
        }
        if (flags.isInterface()) {
            modifier |= 512;
        }
        if (flags.isStrictFP()) {
            modifier |= 2048;
        }
        return modifier;
    }
}
