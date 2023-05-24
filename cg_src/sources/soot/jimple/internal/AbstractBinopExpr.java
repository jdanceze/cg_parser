package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.RefType;
import soot.ShortType;
import soot.SootClass;
import soot.Type;
import soot.UnitPrinter;
import soot.UnknownType;
import soot.Value;
import soot.ValueBox;
import soot.dotnet.types.DotnetBasicTypes;
import soot.grimp.PrecedenceTest;
import soot.jimple.Expr;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractBinopExpr.class */
public abstract class AbstractBinopExpr implements Expr {
    protected final ValueBox op1Box;
    protected final ValueBox op2Box;

    /* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractBinopExpr$BinopExprEnum.class */
    public enum BinopExprEnum {
        ABASTRACT_INT_LONG_BINOP_EXPR,
        ABSTRACT_FLOAT_BINOP_EXPR;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static BinopExprEnum[] valuesCustom() {
            BinopExprEnum[] valuesCustom = values();
            int length = valuesCustom.length;
            BinopExprEnum[] binopExprEnumArr = new BinopExprEnum[length];
            System.arraycopy(valuesCustom, 0, binopExprEnumArr, 0, length);
            return binopExprEnumArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract String getSymbol();

    @Override // soot.Value
    public abstract Object clone();

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractBinopExpr(ValueBox op1Box, ValueBox op2Box) {
        this.op1Box = op1Box;
        this.op2Box = op2Box;
    }

    public Value getOp1() {
        return this.op1Box.getValue();
    }

    public Value getOp2() {
        return this.op2Box.getValue();
    }

    public ValueBox getOp1Box() {
        return this.op1Box;
    }

    public ValueBox getOp2Box() {
        return this.op2Box;
    }

    public void setOp1(Value op1) {
        this.op1Box.setValue(op1);
    }

    public void setOp2(Value op2) {
        this.op2Box.setValue(op2);
    }

    @Override // soot.Value
    public final List<ValueBox> getUseBoxes() {
        List<ValueBox> list = new ArrayList<>();
        list.addAll(this.op1Box.getValue().getUseBoxes());
        list.add(this.op1Box);
        list.addAll(this.op2Box.getValue().getUseBoxes());
        list.add(this.op2Box);
        return list;
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        if (o instanceof AbstractBinopExpr) {
            AbstractBinopExpr abe = (AbstractBinopExpr) o;
            return this.op1Box.getValue().equivTo(abe.op1Box.getValue()) && this.op2Box.getValue().equivTo(abe.op2Box.getValue()) && getSymbol().equals(abe.getSymbol());
        }
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return (((this.op1Box.getValue().equivHashCode() * 101) + this.op2Box.getValue().equivHashCode()) + 17) ^ getSymbol().hashCode();
    }

    public String toString() {
        return String.valueOf(this.op1Box.getValue().toString()) + getSymbol() + this.op2Box.getValue().toString();
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        boolean needsBrackets = PrecedenceTest.needsBrackets(this.op1Box, this);
        if (needsBrackets) {
            up.literal("(");
        }
        this.op1Box.toString(up);
        if (needsBrackets) {
            up.literal(")");
        }
        up.literal(getSymbol());
        boolean needsBrackets2 = PrecedenceTest.needsBracketsRight(this.op2Box, this);
        if (needsBrackets2) {
            up.literal("(");
        }
        this.op2Box.toString(up);
        if (needsBrackets2) {
            up.literal(")");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Type getType(BinopExprEnum exprTypes) {
        Type t1 = this.op1Box.getValue().getType();
        Type t2 = this.op2Box.getValue().getType();
        IntType tyInt = IntType.v();
        ByteType tyByte = ByteType.v();
        ShortType tyShort = ShortType.v();
        CharType tyChar = CharType.v();
        BooleanType tyBool = BooleanType.v();
        if ((tyInt.equals(t1) || tyByte.equals(t1) || tyShort.equals(t1) || tyChar.equals(t1) || tyBool.equals(t1)) && (tyInt.equals(t2) || tyByte.equals(t2) || tyShort.equals(t2) || tyChar.equals(t2) || tyBool.equals(t2))) {
            return tyInt;
        }
        LongType tyLong = LongType.v();
        if (tyLong.equals(t1) || tyLong.equals(t2)) {
            return tyLong;
        }
        if (exprTypes.equals(BinopExprEnum.ABSTRACT_FLOAT_BINOP_EXPR)) {
            DoubleType tyDouble = DoubleType.v();
            if (tyDouble.equals(t1) || tyDouble.equals(t2)) {
                return tyDouble;
            }
            FloatType tyFloat = FloatType.v();
            if (tyFloat.equals(t1) || tyFloat.equals(t2)) {
                return tyFloat;
            }
        }
        if (Options.v().src_prec() == 7 && (isSuperclassSystemEnum(t1) || isSuperclassSystemEnum(t2))) {
            return tyInt;
        }
        return UnknownType.v();
    }

    public boolean isSuperclassSystemEnum(Type t) {
        SootClass sootClass;
        SootClass superclass;
        if (Options.v().src_prec() == 7 && (t instanceof RefType) && (sootClass = ((RefType) t).getSootClass()) != null && (superclass = sootClass.getSuperclass()) != null && superclass.getName().equals(DotnetBasicTypes.SYSTEM_ENUM)) {
            return true;
        }
        return false;
    }
}
