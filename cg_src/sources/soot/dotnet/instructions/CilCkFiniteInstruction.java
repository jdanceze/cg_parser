package soot.dotnet.instructions;

import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.UnitPatchingChain;
import soot.Value;
import soot.dotnet.exceptions.NoExpressionInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.types.DotnetBasicTypes;
import soot.jimple.DoubleConstant;
import soot.jimple.EqExpr;
import soot.jimple.Jimple;
import soot.jimple.NeExpr;
import soot.jimple.NopStmt;
import soot.jimple.ThrowStmt;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilCkFiniteInstruction.class */
public class CilCkFiniteInstruction extends AbstractCilnstruction {
    public CilCkFiniteInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        CilInstruction cilExpr = CilInstructionFactory.fromInstructionMsg(this.instruction.getArgument(), this.dotnetBody, this.cilBlock);
        Value argument = cilExpr.jimplifyExpr(jb);
        DoubleConstant posInfinity = DoubleConstant.v(Double.POSITIVE_INFINITY);
        DoubleConstant negInfinity = DoubleConstant.v(Double.NEGATIVE_INFINITY);
        EqExpr eqPosInfExpr = Jimple.v().newEqExpr(argument, posInfinity);
        EqExpr eqNegInfExpr = Jimple.v().newEqExpr(argument, negInfinity);
        NeExpr eqNaNExpr = Jimple.v().newNeExpr(argument, argument);
        SootClass exceptionClass = Scene.v().getSootClass(DotnetBasicTypes.SYSTEM_ARITHMETICEXCEPTION);
        Local tmpLocalVar = this.dotnetBody.variableManager.localGenerator.generateLocal(exceptionClass.getType());
        ThrowStmt throwStmt = Jimple.v().newThrowStmt(tmpLocalVar);
        jb.getUnits().add((UnitPatchingChain) Jimple.v().newIfStmt(eqPosInfExpr, throwStmt));
        jb.getUnits().add((UnitPatchingChain) Jimple.v().newIfStmt(eqNegInfExpr, throwStmt));
        jb.getUnits().add((UnitPatchingChain) Jimple.v().newIfStmt(eqNaNExpr, throwStmt));
        NopStmt nopStmt = Jimple.v().newNopStmt();
        jb.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(nopStmt));
        jb.getUnits().add((UnitPatchingChain) throwStmt);
        jb.getUnits().add((UnitPatchingChain) nopStmt);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        throw new NoExpressionInstructionException(this.instruction);
    }
}
