package soot.jimple.toolkits.typing.fast;

import java.util.Collection;
import java.util.Collections;
import soot.Type;
import soot.Value;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/AugEvalFunction.class */
public class AugEvalFunction implements IEvalFunction {
    private final JimpleBody jb;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AugEvalFunction.class.desiredAssertionStatus();
    }

    public AugEvalFunction(JimpleBody jb) {
        this.jb = jb;
    }

    /* JADX WARN: Code restructure failed: missing block: B:152:0x02ef, code lost:
        if (r0.equals("java.lang.Cloneable") == false) goto L174;
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x02fc, code lost:
        if (r0.equals(soot.dotnet.types.DotnetBasicTypes.SYSTEM_ARRAY) == false) goto L174;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x0309, code lost:
        if (r0.equals(soot.JavaBasicTypes.JAVA_LANG_OBJECT) == false) goto L174;
     */
    /* JADX WARN: Code restructure failed: missing block: B:161:0x0316, code lost:
        if (r0.equals(soot.JavaBasicTypes.JAVA_IO_SERIALIZABLE) == false) goto L174;
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x0325, code lost:
        return new soot.jimple.toolkits.typing.fast.WeakObjectType(r0);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static soot.Type eval_(soot.jimple.toolkits.typing.fast.Typing r6, soot.Value r7, soot.jimple.Stmt r8, soot.jimple.JimpleBody r9) {
        /*
            Method dump skipped, instructions count: 1199
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.toolkits.typing.fast.AugEvalFunction.eval_(soot.jimple.toolkits.typing.fast.Typing, soot.Value, soot.jimple.Stmt, soot.jimple.JimpleBody):soot.Type");
    }

    @Override // soot.jimple.toolkits.typing.fast.IEvalFunction
    public Collection<Type> eval(Typing tg, Value expr, Stmt stmt) {
        return Collections.singletonList(eval_(tg, expr, stmt, this.jb));
    }
}
