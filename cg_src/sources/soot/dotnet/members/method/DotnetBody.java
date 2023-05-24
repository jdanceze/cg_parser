package soot.dotnet.members.method;

import soot.Body;
import soot.Immediate;
import soot.Local;
import soot.LocalGenerator;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.Type;
import soot.UnitPatchingChain;
import soot.Value;
import soot.VoidType;
import soot.dotnet.instructions.CilBlockContainer;
import soot.dotnet.members.DotnetMethod;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.types.DotnetTypeFactory;
import soot.jimple.CastExpr;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NullConstant;
/* loaded from: gencallgraphv3.jar:soot/dotnet/members/method/DotnetBody.class */
public class DotnetBody {
    private final ProtoIlInstructions.IlFunctionMsg ilFunctionMsg;
    private JimpleBody jb;
    public BlockEntryPointsManager blockEntryPointsManager = new BlockEntryPointsManager();
    public DotnetBodyVariableManager variableManager;
    private final DotnetMethod dotnetMethodSig;

    public DotnetMethod getDotnetMethodSig() {
        return this.dotnetMethodSig;
    }

    public DotnetBody(DotnetMethod methodSignature, ProtoIlInstructions.IlFunctionMsg ilFunctionMsg) {
        this.dotnetMethodSig = methodSignature;
        this.ilFunctionMsg = ilFunctionMsg;
    }

    public void jimplify(JimpleBody jb) {
        this.jb = jb;
        this.variableManager = new DotnetBodyVariableManager(this, this.jb);
        addThisStmt();
        this.variableManager.fillMethodParameter();
        this.variableManager.addInitLocalVariables(this.ilFunctionMsg.getVariablesList());
        CilBlockContainer blockContainer = new CilBlockContainer(this.ilFunctionMsg.getBody(), this);
        Body b = blockContainer.jimplify();
        this.jb.getUnits().addAll(b.getUnits());
        this.jb.getTraps().addAll(b.getTraps());
        this.blockEntryPointsManager.swapGotoEntriesInJBody(this.jb);
    }

    private void addThisStmt() {
        if (this.dotnetMethodSig.isStatic()) {
            return;
        }
        RefType thisType = this.dotnetMethodSig.getDeclaringClass().getType();
        Local l = Jimple.v().newLocal("this", thisType);
        IdentityStmt identityStmt = Jimple.v().newIdentityStmt(l, Jimple.v().newThisRef(thisType));
        this.jb.getLocals().add(l);
        this.jb.getUnits().add((UnitPatchingChain) identityStmt);
    }

    public static Value inlineCastExpr(Value v) {
        if (v instanceof Immediate) {
            return v;
        }
        if (v instanceof CastExpr) {
            return inlineCastExpr(((CastExpr) v).getOp());
        }
        return v;
    }

    public static JimpleBody getEmptyJimpleBody(SootMethod m) {
        JimpleBody b = Jimple.v().newBody(m);
        resolveEmptyJimpleBody(b, m);
        return b;
    }

    public static void resolveEmptyJimpleBody(JimpleBody b, SootMethod m) {
        if (!m.isStatic()) {
            RefType thisType = m.getDeclaringClass().getType();
            Local l = Jimple.v().newLocal("this", thisType);
            IdentityStmt identityStmt = Jimple.v().newIdentityStmt(l, Jimple.v().newThisRef(thisType));
            b.getLocals().add(l);
            b.getUnits().add((UnitPatchingChain) identityStmt);
        }
        for (int i = 0; i < m.getParameterCount(); i++) {
            Type parameterType = m.getParameterType(i);
            Local paramLocal = Jimple.v().newLocal("arg" + i, parameterType);
            b.getLocals().add(paramLocal);
            b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(paramLocal, Jimple.v().newParameterRef(parameterType, i)));
        }
        LocalGenerator lg = Scene.v().createLocalGenerator(b);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newThrowStmt(lg.generateLocal(RefType.v("java.lang.Throwable"))));
        if (m.getReturnType() instanceof VoidType) {
            b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnVoidStmt());
        } else if (m.getReturnType() instanceof PrimType) {
            b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnStmt(DotnetTypeFactory.initType(m.getReturnType())));
        } else {
            b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnStmt(NullConstant.v()));
        }
    }
}
