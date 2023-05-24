package soot.sootify;

import java.util.HashSet;
import java.util.Set;
import org.apache.tools.ant.types.selectors.SizeSelector;
import soot.Local;
import soot.SootField;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.EqExpr;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.JimpleValueSwitch;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LtExpr;
import soot.jimple.MethodHandle;
import soot.jimple.MethodType;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.ParameterRef;
import soot.jimple.RemExpr;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.ThisRef;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;
import soot.jimple.infoflow.android.source.parsers.xml.XMLConstants;
/* loaded from: gencallgraphv3.jar:soot/sootify/ValueTemplatePrinter.class */
public class ValueTemplatePrinter implements JimpleValueSwitch {
    private final TemplatePrinter p;
    private final TypeTemplatePrinter ttp;
    private String varName;
    private final Set<String> varnamesAlreadyUsed = new HashSet();

    public ValueTemplatePrinter(TemplatePrinter p) {
        this.p = p;
        this.ttp = new TypeTemplatePrinter(p);
        this.varnamesAlreadyUsed.add("b");
        this.varnamesAlreadyUsed.add("m");
        this.varnamesAlreadyUsed.add(SizeSelector.UNITS_KEY);
    }

    public String printValueAssignment(Value value, String varName) {
        suggestVariableName(varName);
        value.apply(this);
        return getLastAssignedVarName();
    }

    private void printConstant(Value v, String... ops) {
        String stmtClassName = v.getClass().getSimpleName();
        this.p.print("Value " + this.varName + " = ");
        this.p.printNoIndent(stmtClassName);
        this.p.printNoIndent(".v(");
        int i = 1;
        for (String op : ops) {
            this.p.printNoIndent(op);
            if (i < ops.length) {
                this.p.printNoIndent(",");
            }
            i++;
        }
        this.p.printNoIndent(")");
        this.p.printlnNoIndent(";");
    }

    private void printExpr(Value v, String... ops) {
        String stmtClassName = v.getClass().getSimpleName();
        if (stmtClassName.charAt(0) == 'J') {
            stmtClassName = stmtClassName.substring(1);
        }
        this.p.print("Value " + this.varName + " = ");
        printFactoryMethodCall(stmtClassName, ops);
        this.p.printlnNoIndent(";");
    }

    private void printFactoryMethodCall(String stmtClassName, String... ops) {
        this.p.printNoIndent("Jimple.v().new");
        this.p.printNoIndent(stmtClassName);
        this.p.printNoIndent("(");
        int i = 1;
        for (String op : ops) {
            this.p.printNoIndent(op);
            if (i < ops.length) {
                this.p.printNoIndent(",");
            }
            i++;
        }
        this.p.printNoIndent(")");
    }

    public void suggestVariableName(String name) {
        String actualName;
        int i = 0;
        do {
            actualName = String.valueOf(name) + i;
            i++;
        } while (this.varnamesAlreadyUsed.contains(actualName));
        this.varName = actualName;
        this.varnamesAlreadyUsed.add(actualName);
    }

    public String getLastAssignedVarName() {
        return this.varName;
    }

    @Override // soot.jimple.ConstantSwitch
    public void caseDoubleConstant(DoubleConstant v) {
        printConstant(v, Double.toString(v.value));
    }

    @Override // soot.jimple.ConstantSwitch
    public void caseFloatConstant(FloatConstant v) {
        printConstant(v, Float.toString(v.value));
    }

    @Override // soot.jimple.ConstantSwitch
    public void caseIntConstant(IntConstant v) {
        printConstant(v, Integer.toString(v.value));
    }

    @Override // soot.jimple.ConstantSwitch
    public void caseLongConstant(LongConstant v) {
        printConstant(v, Long.toString(v.value));
    }

    @Override // soot.jimple.ConstantSwitch
    public void caseNullConstant(NullConstant v) {
        printConstant(v, new String[0]);
    }

    @Override // soot.jimple.ConstantSwitch
    public void caseStringConstant(StringConstant v) {
        printConstant(v, "\"" + v.value + "\"");
    }

    @Override // soot.jimple.ConstantSwitch
    public void caseClassConstant(ClassConstant v) {
        printConstant(v, "\"" + v.value + "\"");
    }

    @Override // soot.jimple.ExprSwitch
    public void caseAddExpr(AddExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ConstantSwitch
    public void caseMethodHandle(MethodHandle handle) {
        throw new UnsupportedOperationException("we have not yet determined how to print Java 7 method handles");
    }

    @Override // soot.jimple.ConstantSwitch
    public void caseMethodType(MethodType type) {
        throw new UnsupportedOperationException("we have not yet determined how to print Java 8 method handles");
    }

    private void printBinaryExpr(BinopExpr v) {
        String className = v.getClass().getSimpleName();
        if (className.charAt(0) == 'J') {
            className = className.substring(1);
        }
        String oldName = this.varName;
        String v1 = printValueAssignment(v.getOp1(), "left");
        String v2 = printValueAssignment(v.getOp2(), "right");
        this.p.println("Value " + oldName + " = Jimple.v().new" + className + "(" + v1 + "," + v2 + ");");
        this.varName = oldName;
    }

    @Override // soot.jimple.ExprSwitch
    public void caseAndExpr(AndExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseCmpExpr(CmpExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseCmpgExpr(CmpgExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseCmplExpr(CmplExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseDivExpr(DivExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseEqExpr(EqExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseNeExpr(NeExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseGeExpr(GeExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseGtExpr(GtExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseLeExpr(LeExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseLtExpr(LtExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseMulExpr(MulExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseOrExpr(OrExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseRemExpr(RemExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseShlExpr(ShlExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseShrExpr(ShrExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseUshrExpr(UshrExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseSubExpr(SubExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseXorExpr(XorExpr v) {
        printBinaryExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
        printInvokeExpr(v);
    }

    private void printInvokeExpr(InvokeExpr v) {
        this.p.openBlock();
        String oldName = this.varName;
        SootMethodRef method = v.getMethodRef();
        SootMethod m = method.resolve();
        if (!m.isStatic()) {
            Local base = (Local) ((InstanceInvokeExpr) v).getBase();
            this.p.println("Local base = localByName(b,\"" + base.getName() + "\");");
        }
        this.p.println("List<Type> parameterTypes = new LinkedList<Type>();");
        int i = 0;
        for (Type t : m.getParameterTypes()) {
            this.ttp.setVariableName("type" + i);
            t.apply(this.ttp);
            this.p.println("parameterTypes.add(type" + i + ");");
            i++;
        }
        this.ttp.setVariableName("returnType");
        m.getReturnType().apply(this.ttp);
        this.p.print("SootMethodRef methodRef = ");
        this.p.printNoIndent("Scene.v().makeMethodRef(");
        String className = m.getDeclaringClass().getName();
        this.p.printNoIndent("Scene.v().getSootClass(\"" + className + "\"),");
        this.p.printNoIndent("\"" + m.getName() + "\",");
        this.p.printNoIndent("parameterTypes,");
        this.p.printNoIndent("returnType,");
        this.p.printlnNoIndent(String.valueOf(m.isStatic()) + ");");
        printExpr(v, XMLConstants.BASE_TAG, "methodRef");
        this.varName = oldName;
        this.p.closeBlock();
    }

    @Override // soot.jimple.ExprSwitch
    public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
        printInvokeExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseStaticInvokeExpr(StaticInvokeExpr v) {
        printInvokeExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
        printInvokeExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
        printInvokeExpr(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseCastExpr(CastExpr v) {
        String oldName = this.varName;
        suggestVariableName("type");
        String lhsName = this.varName;
        this.ttp.setVariableName(this.varName);
        v.getType().apply(this.ttp);
        String rhsName = printValueAssignment(v.getOp(), "op");
        this.p.println("Value " + oldName + " = Jimple.v().newCastExpr(" + lhsName + "," + rhsName + ");");
        this.varName = oldName;
    }

    @Override // soot.jimple.ExprSwitch
    public void caseInstanceOfExpr(InstanceOfExpr v) {
        String oldName = this.varName;
        suggestVariableName("type");
        String lhsName = this.varName;
        this.ttp.setVariableName(this.varName);
        v.getType().apply(this.ttp);
        String rhsName = printValueAssignment(v.getOp(), "op");
        this.p.println("Value " + oldName + " = Jimple.v().newInstanceOfExpr(" + lhsName + "," + rhsName + ");");
        this.varName = oldName;
    }

    @Override // soot.jimple.ExprSwitch
    public void caseNewArrayExpr(NewArrayExpr v) {
        String oldName = this.varName;
        Value size = v.getSize();
        suggestVariableName("size");
        String sizeName = this.varName;
        size.apply(this);
        suggestVariableName("type");
        String lhsName = this.varName;
        this.ttp.setVariableName(this.varName);
        v.getType().apply(this.ttp);
        this.p.println("Value " + oldName + " = Jimple.v().newNewArrayExpr(" + lhsName + ", " + sizeName + ");");
        this.varName = oldName;
    }

    @Override // soot.jimple.ExprSwitch
    public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
        this.p.openBlock();
        String oldName = this.varName;
        this.ttp.setVariableName("arrayType");
        v.getType().apply(this.ttp);
        this.p.println("List<IntConstant> sizes = new LinkedList<IntConstant>();");
        int i = 0;
        for (Value s : v.getSizes()) {
            suggestVariableName("size" + i);
            s.apply(this);
            i++;
            this.p.println("sizes.add(sizes" + i + ");");
        }
        this.p.println("Value " + oldName + " = Jimple.v().newNewMultiArrayExpr(arrayType, sizes);");
        this.varName = oldName;
        this.p.closeBlock();
    }

    @Override // soot.jimple.ExprSwitch
    public void caseNewExpr(NewExpr v) {
        String oldName = this.varName;
        suggestVariableName("type");
        String typeName = this.varName;
        this.ttp.setVariableName(this.varName);
        v.getType().apply(this.ttp);
        this.p.println("Value " + oldName + " = Jimple.v().newNewExpr(" + typeName + ");");
        this.varName = oldName;
    }

    @Override // soot.jimple.ExprSwitch
    public void caseLengthExpr(LengthExpr v) {
        String oldName = this.varName;
        Value op = v.getOp();
        suggestVariableName("op");
        String opName = this.varName;
        op.apply(this);
        this.p.println("Value " + oldName + " = Jimple.v().newLengthExpr(" + opName + ");");
        this.varName = oldName;
    }

    @Override // soot.jimple.ExprSwitch
    public void caseNegExpr(NegExpr v) {
        String oldName = this.varName;
        Value op = v.getOp();
        suggestVariableName("op");
        String opName = this.varName;
        op.apply(this);
        this.p.println("Value " + oldName + " = Jimple.v().newNegExpr(" + opName + ");");
        this.varName = oldName;
    }

    @Override // soot.jimple.RefSwitch
    public void caseArrayRef(ArrayRef v) {
        String oldName = this.varName;
        Value base = v.getBase();
        suggestVariableName(XMLConstants.BASE_TAG);
        String baseName = this.varName;
        base.apply(this);
        Value index = v.getIndex();
        suggestVariableName(XMLConstants.INDEX_ATTRIBUTE);
        String indexName = this.varName;
        index.apply(this);
        this.p.println("Value " + oldName + " = Jimple.v().newArrayRef(" + baseName + ", " + indexName + ");");
        this.varName = oldName;
    }

    @Override // soot.jimple.RefSwitch
    public void caseStaticFieldRef(StaticFieldRef v) {
        printFieldRef(v);
    }

    private void printFieldRef(FieldRef v) {
        String refTypeName = v.getClass().getSimpleName();
        this.p.openBlock();
        String oldName = this.varName;
        SootField f = v.getField();
        this.ttp.setVariableName("type");
        f.getType().apply(this.ttp);
        this.p.print("SootFieldRef fieldRef = ");
        this.p.printNoIndent("Scene.v().makeFieldRef(");
        String className = f.getDeclaringClass().getName();
        this.p.printNoIndent("Scene.v().getSootClass(\"" + className + "\"),");
        this.p.printNoIndent("\"" + f.getName() + "\",");
        this.p.printNoIndent("type,");
        this.p.printNoIndent(String.valueOf(f.isStatic()) + ");");
        this.p.println("Value " + oldName + " = Jimple.v().new" + refTypeName + "(fieldRef);");
        this.varName = oldName;
        this.p.closeBlock();
    }

    @Override // soot.jimple.RefSwitch
    public void caseInstanceFieldRef(InstanceFieldRef v) {
        printFieldRef(v);
    }

    @Override // soot.jimple.RefSwitch
    public void caseParameterRef(ParameterRef v) {
        String oldName = this.varName;
        Type paramType = v.getType();
        suggestVariableName("paramType");
        String paramTypeName = this.varName;
        this.ttp.setVariableName(paramTypeName);
        paramType.apply(this.ttp);
        suggestVariableName("number");
        this.p.println("int " + this.varName + "=" + v.getIndex() + ";");
        this.p.println("Value " + oldName + " = Jimple.v().newParameterRef(" + paramTypeName + ", " + this.varName + ");");
        this.varName = oldName;
    }

    @Override // soot.jimple.RefSwitch
    public void caseCaughtExceptionRef(CaughtExceptionRef v) {
        this.p.println("Value " + this.varName + " = Jimple.v().newCaughtExceptionRef();");
    }

    @Override // soot.jimple.RefSwitch
    public void caseThisRef(ThisRef v) {
        String oldName = this.varName;
        suggestVariableName("type");
        String typeName = this.varName;
        this.ttp.setVariableName(typeName);
        v.getType().apply(this.ttp);
        this.p.println("Value " + oldName + " = Jimple.v().newThisRef(" + typeName + ");");
        this.varName = oldName;
    }

    @Override // soot.jimple.ImmediateSwitch
    public void caseLocal(Local l) {
        String oldName = this.varName;
        this.p.println("Local " + this.varName + " = localByName(b,\"" + l.getName() + "\");");
        this.varName = oldName;
    }

    @Override // soot.jimple.ExprSwitch, soot.jimple.ConstantSwitch
    public void defaultCase(Object object) {
        throw new InternalError();
    }
}
