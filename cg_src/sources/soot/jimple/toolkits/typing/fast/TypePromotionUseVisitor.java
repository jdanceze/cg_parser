package soot.jimple.toolkits.typing.fast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.IntType;
import soot.Local;
import soot.ShortType;
import soot.Type;
import soot.Value;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/TypePromotionUseVisitor.class */
public class TypePromotionUseVisitor implements IUseVisitor {
    private static final Logger logger = LoggerFactory.getLogger(TypePromotionUseVisitor.class);
    private final JimpleBody jb;
    private final Typing tg;
    private final ByteType byteType = ByteType.v();
    private final Integer32767Type integer32767Type = Integer32767Type.v();
    private final Integer127Type integer127Type = Integer127Type.v();
    public boolean fail = false;
    public boolean typingChanged = false;

    public TypePromotionUseVisitor(JimpleBody jb, Typing tg) {
        this.jb = jb;
        this.tg = tg;
    }

    private Type promote(Type tlow, Type thigh) {
        if (tlow instanceof Integer1Type) {
            if (thigh instanceof IntType) {
                return Integer127Type.v();
            }
            if (thigh instanceof ShortType) {
                return this.byteType;
            }
            if ((thigh instanceof BooleanType) || (thigh instanceof ByteType) || (thigh instanceof CharType) || (thigh instanceof Integer127Type) || (thigh instanceof Integer32767Type)) {
                return thigh;
            }
            throw new RuntimeException();
        } else if (tlow instanceof Integer127Type) {
            if (thigh instanceof ShortType) {
                return this.byteType;
            }
            if (thigh instanceof IntType) {
                return this.integer127Type;
            }
            if ((thigh instanceof ByteType) || (thigh instanceof CharType) || (thigh instanceof Integer32767Type)) {
                return thigh;
            }
            throw new RuntimeException();
        } else if (tlow instanceof Integer32767Type) {
            if (thigh instanceof IntType) {
                return this.integer32767Type;
            }
            if ((thigh instanceof ShortType) || (thigh instanceof CharType)) {
                return thigh;
            }
            throw new RuntimeException();
        } else {
            throw new RuntimeException();
        }
    }

    @Override // soot.jimple.toolkits.typing.fast.IUseVisitor
    public Value visit(Value op, Type useType, Stmt stmt, boolean checkOnly) {
        if (finish()) {
            return op;
        }
        Type t = AugEvalFunction.eval_(this.tg, op, stmt, this.jb);
        if (!AugHierarchy.ancestor_(useType, t)) {
            logger.error(String.format("Failed Typing in %s at statement %s: Is not cast compatible: %s <-- %s", this.jb.getMethod().getSignature(), stmt, useType, t));
            this.fail = true;
        } else if (!checkOnly && (op instanceof Local) && ((t instanceof Integer1Type) || (t instanceof Integer127Type) || (t instanceof Integer32767Type) || (t instanceof WeakObjectType))) {
            Local v = (Local) op;
            if (!TypeResolver.typesEqual(t, useType)) {
                Type t_ = promote(t, useType);
                if (!TypeResolver.typesEqual(t, t_)) {
                    this.tg.set(v, t_);
                    this.typingChanged = true;
                }
            }
        }
        return op;
    }

    @Override // soot.jimple.toolkits.typing.fast.IUseVisitor
    public boolean finish() {
        return this.typingChanged || this.fail;
    }
}
