package soot.javaToJimple.jj.ast;

import polyglot.ast.Binary;
import polyglot.ast.Expr;
import polyglot.ext.jl.ast.Binary_c;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/jj/ast/JjBinary_c.class */
public class JjBinary_c extends Binary_c {
    public JjBinary_c(Position pos, Expr left, Binary.Operator op, Expr right) {
        super(pos, left, op, right);
    }

    @Override // polyglot.ext.jl.ast.Binary_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        Expr other;
        if (child == this.left) {
            other = this.right;
        } else if (child == this.right) {
            other = this.left;
        } else {
            return child.type();
        }
        TypeSystem ts = av.typeSystem();
        if (this.op == EQ || this.op == NE) {
            if (other.type().isReference() || other.type().isNull()) {
                return ts.Object();
            }
            if (other.type().isBoolean()) {
                return ts.Boolean();
            }
            if (other.type().isNumeric()) {
                if (other.type().isDouble() || child.type().isDouble()) {
                    return ts.Double();
                }
                if (other.type().isFloat() || child.type().isFloat()) {
                    return ts.Float();
                }
                if (other.type().isLong() || child.type().isLong()) {
                    return ts.Long();
                }
                return ts.Int();
            }
        }
        if (this.op == ADD && ts.equals(this.type, ts.String())) {
            return ts.String();
        }
        if (this.op == GT || this.op == LT || this.op == GE || this.op == LE) {
            if (other.type().isBoolean()) {
                return ts.Boolean();
            }
            if (other.type().isNumeric()) {
                if (other.type().isDouble() || child.type().isDouble()) {
                    return ts.Double();
                }
                if (other.type().isFloat() || child.type().isFloat()) {
                    return ts.Float();
                }
                if (other.type().isLong() || child.type().isLong()) {
                    return ts.Long();
                }
                return ts.Int();
            }
        }
        if (this.op == COND_OR || this.op == COND_AND) {
            return ts.Boolean();
        }
        if (this.op == BIT_AND || this.op == BIT_OR || this.op == BIT_XOR) {
            if (other.type().isBoolean()) {
                return ts.Boolean();
            }
            if (other.type().isNumeric()) {
                if (other.type().isLong() || child.type().isLong()) {
                    return ts.Long();
                }
                return ts.Int();
            }
        }
        if ((this.op == ADD || this.op == SUB || this.op == MUL || this.op == DIV || this.op == MOD) && other.type().isNumeric()) {
            if (other.type().isDouble() || child.type().isDouble()) {
                return ts.Double();
            }
            if (other.type().isFloat() || child.type().isFloat()) {
                return ts.Float();
            }
            if (other.type().isLong() || child.type().isLong()) {
                return ts.Long();
            }
            return ts.Int();
        } else if (this.op == SHL || this.op == SHR || this.op == USHR) {
            if (child == this.right || !child.type().isLong()) {
                return ts.Int();
            }
            return child.type();
        } else {
            return child.type();
        }
    }
}
