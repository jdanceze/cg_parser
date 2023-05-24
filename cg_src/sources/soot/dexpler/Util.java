package soot.dexpler;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.JavaBasicTypes;
import soot.Local;
import soot.LocalGenerator;
import soot.LongType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.Type;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IdentityStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.jimple.toolkits.scalar.LocalCreation;
/* loaded from: gencallgraphv3.jar:soot/dexpler/Util.class */
public class Util {
    public static String dottedClassName(String typeDescriptor) {
        if (!isByteCodeClassName(typeDescriptor)) {
            int idx = 0;
            while (idx < typeDescriptor.length() && typeDescriptor.charAt(idx) == '[') {
                idx++;
            }
            String c = typeDescriptor.substring(idx);
            if (c.length() == 1 && (c.startsWith("I") || c.startsWith("B") || c.startsWith("C") || c.startsWith("S") || c.startsWith("J") || c.startsWith("D") || c.startsWith("F") || c.startsWith("Z"))) {
                Type ty = getType(typeDescriptor);
                return ty == null ? "" : getType(typeDescriptor).toString();
            }
            throw new IllegalArgumentException("typeDescriptor is not a class typedescriptor: '" + typeDescriptor + "'");
        }
        int idx2 = 0;
        while (idx2 < typeDescriptor.length() && typeDescriptor.charAt(idx2) == '[') {
            idx2++;
        }
        String className = typeDescriptor.substring(idx2);
        return className.substring(className.indexOf(76) + 1, className.indexOf(59)).replace('/', '.');
    }

    public static Type getType(String type) {
        Type v;
        int idx = 0;
        int arraySize = 0;
        Type returnType = null;
        boolean notFound = true;
        while (idx < type.length() && notFound) {
            switch (type.charAt(idx)) {
                case 'B':
                    v = ByteType.v();
                    break;
                case 'C':
                    v = CharType.v();
                    break;
                case 'D':
                    v = DoubleType.v();
                    break;
                case 'E':
                case 'G':
                case 'H':
                case 'K':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'T':
                case 'U':
                case 'W':
                case 'X':
                case 'Y':
                default:
                    throw new RuntimeException("unknown type: '" + type + "'");
                case 'F':
                    v = FloatType.v();
                    break;
                case 'I':
                    v = IntType.v();
                    break;
                case 'J':
                    v = LongType.v();
                    break;
                case 'L':
                    String objectName = type.replaceAll("^[^L]*L", "").replaceAll(";$", "");
                    v = RefType.v(objectName.replace("/", "."));
                    break;
                case 'S':
                    v = ShortType.v();
                    break;
                case 'V':
                    v = VoidType.v();
                    break;
                case 'Z':
                    v = BooleanType.v();
                    break;
                case '[':
                    while (true) {
                        if (idx >= type.length()) {
                            continue;
                        } else if (type.charAt(idx) != '[') {
                            break;
                        } else {
                            arraySize++;
                            idx++;
                        }
                    }
                    break;
            }
            returnType = v;
            notFound = false;
            idx++;
        }
        if (returnType != null && arraySize > 0) {
            returnType = ArrayType.v(returnType, arraySize);
        }
        return returnType;
    }

    public static boolean isByteCodeClassName(String className) {
        if ((className.startsWith("L") || className.startsWith("[")) && className.endsWith(";")) {
            return className.indexOf(47) != -1 || className.indexOf(46) == -1;
        }
        return false;
    }

    public static <T> T[] concat(T[] tArr, T[] tArr2) {
        T[] tArr3 = (T[]) Arrays.copyOf(tArr, tArr.length + tArr2.length);
        System.arraycopy(tArr2, 0, tArr3, tArr.length, tArr2.length);
        return tArr3;
    }

    public static boolean isFloatLike(Type t) {
        return t.equals(FloatType.v()) || t.equals(DoubleType.v()) || t.equals(RefType.v(JavaBasicTypes.JAVA_LANG_FLOAT)) || t.equals(RefType.v(JavaBasicTypes.JAVA_LANG_DOUBLE));
    }

    public static void emptyBody(Body jBody) {
        AssignStmt ass;
        List<Unit> idStmts = new ArrayList<>();
        List<Local> idLocals = new ArrayList<>();
        Iterator<Unit> it = jBody.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof IdentityStmt) {
                IdentityStmt i = (IdentityStmt) u;
                if ((i.getRightOp() instanceof ParameterRef) || (i.getRightOp() instanceof ThisRef)) {
                    idStmts.add(u);
                    idLocals.add((Local) i.getLeftOp());
                }
            }
        }
        jBody.getUnits().clear();
        jBody.getLocals().clear();
        jBody.getTraps().clear();
        LocalGenerator lg = Scene.v().createLocalGenerator(jBody);
        for (Unit u2 : idStmts) {
            jBody.getUnits().add((UnitPatchingChain) u2);
        }
        for (Local l : idLocals) {
            jBody.getLocals().add(l);
        }
        Type rType = jBody.getMethod().getReturnType();
        jBody.getUnits().add((UnitPatchingChain) Jimple.v().newNopStmt());
        if (rType instanceof VoidType) {
            jBody.getUnits().add((UnitPatchingChain) Jimple.v().newReturnVoidStmt());
            return;
        }
        Type t = jBody.getMethod().getReturnType();
        Local l2 = lg.generateLocal(t);
        if ((t instanceof RefType) || (t instanceof ArrayType)) {
            ass = Jimple.v().newAssignStmt(l2, NullConstant.v());
        } else if (t instanceof LongType) {
            ass = Jimple.v().newAssignStmt(l2, LongConstant.v(0L));
        } else if (t instanceof FloatType) {
            ass = Jimple.v().newAssignStmt(l2, FloatConstant.v(0.0f));
        } else if (t instanceof IntType) {
            ass = Jimple.v().newAssignStmt(l2, IntConstant.v(0));
        } else if (t instanceof DoubleType) {
            ass = Jimple.v().newAssignStmt(l2, DoubleConstant.v(Const.default_value_double));
        } else if ((t instanceof BooleanType) || (t instanceof ByteType) || (t instanceof CharType) || (t instanceof ShortType)) {
            ass = Jimple.v().newAssignStmt(l2, IntConstant.v(0));
        } else {
            throw new RuntimeException("error: return type unknown: " + t + " class: " + t.getClass());
        }
        jBody.getUnits().add((UnitPatchingChain) ass);
        jBody.getUnits().add((UnitPatchingChain) Jimple.v().newReturnStmt(l2));
    }

    public static void addExceptionAfterUnit(Body b, String exceptionType, Unit u, String m) {
        LocalCreation lc = Scene.v().createLocalCreation(b.getLocals());
        Local l = lc.newLocal(RefType.v(exceptionType));
        ArrayList arrayList = new ArrayList();
        AssignStmt newAssignStmt = Jimple.v().newAssignStmt(l, Jimple.v().newNewExpr(RefType.v(exceptionType)));
        InvokeStmt newInvokeStmt = Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(l, Scene.v().makeMethodRef(Scene.v().getSootClass(exceptionType), "<init>", Collections.singletonList(RefType.v("java.lang.String")), VoidType.v(), false), StringConstant.v(m)));
        ThrowStmt newThrowStmt = Jimple.v().newThrowStmt(l);
        arrayList.add(newAssignStmt);
        arrayList.add(newInvokeStmt);
        arrayList.add(newThrowStmt);
        b.getUnits().insertBefore((List<ArrayList>) arrayList, (ArrayList) u);
    }

    public static List<String> splitParameters(String parameters) {
        List<String> pList = new ArrayList<>();
        boolean object = false;
        String curr = "";
        for (int idx = 0; idx < parameters.length(); idx++) {
            char c = parameters.charAt(idx);
            curr = String.valueOf(curr) + c;
            switch (c) {
                case ';':
                    object = false;
                    pList.add(curr);
                    curr = "";
                    break;
                case 'L':
                    object = true;
                    break;
                case '[':
                    break;
                default:
                    if (object) {
                        break;
                    } else {
                        pList.add(curr);
                        curr = "";
                        break;
                    }
            }
        }
        return pList;
    }
}
