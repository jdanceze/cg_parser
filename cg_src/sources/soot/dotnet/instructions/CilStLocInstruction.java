package soot.dotnet.instructions;

import java.util.List;
import soot.Body;
import soot.Local;
import soot.UnitPatchingChain;
import soot.Value;
import soot.dotnet.exceptions.NoExpressionInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.types.DotnetBasicTypes;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Jimple;
import soot.toolkits.scalar.Pair;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilStLocInstruction.class */
public class CilStLocInstruction extends AbstractCilnstruction {
    public CilStLocInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        CilInstruction cilExpr = CilInstructionFactory.fromInstructionMsg(this.instruction.getValueInstruction(), this.dotnetBody, this.cilBlock);
        Value value = cilExpr.jimplifyExpr(jb);
        if (cilExpr instanceof CilIsInstInstruction) {
            CilIsInstInstruction isInst = (CilIsInstInstruction) cilExpr;
            isInst.resolveRewritingIsInst(jb, this.dotnetBody.variableManager.addOrGetVariable(this.instruction.getVariable(), jb), value);
            return;
        }
        if ((cilExpr instanceof CilLdElemaInstruction) && ((CilLdElemaInstruction) cilExpr).isMultiArrayRef()) {
            CilLdElemaInstruction newArrInstruction = (CilLdElemaInstruction) cilExpr;
            value = newArrInstruction.resolveRewriteMultiArrAccess(jb);
        }
        Local variable = this.dotnetBody.variableManager.addOrGetVariable(this.instruction.getVariable(), value.getType(), jb);
        if (cilExpr instanceof CilCallVirtInstruction) {
            List<Pair<Local, Local>> locals = ((CilCallVirtInstruction) cilExpr).getLocalsToCastForCall();
            if (locals.size() != 0) {
                for (Pair<Local, Local> pair : locals) {
                    CastExpr castExpr = Jimple.v().newCastExpr(pair.getO1(), pair.getO2().getType());
                    AssignStmt assignStmt = Jimple.v().newAssignStmt(pair.getO2(), castExpr);
                    jb.getUnits().add((UnitPatchingChain) assignStmt);
                }
            }
        }
        if ((value instanceof Local) && !variable.getType().toString().equals(value.getType().toString()) && this.dotnetBody.variableManager.localsToCastContains(((Local) value).getName())) {
            value = Jimple.v().newCastExpr(value, variable.getType());
        }
        if ((value instanceof Local) && value.getType().toString().equals(DotnetBasicTypes.SYSTEM_OBJECT) && !variable.getType().toString().equals(DotnetBasicTypes.SYSTEM_OBJECT)) {
            value = Jimple.v().newCastExpr(value, variable.getType());
        }
        AssignStmt astm = Jimple.v().newAssignStmt(variable, value);
        jb.getUnits().add((UnitPatchingChain) astm);
        if (cilExpr instanceof AbstractNewObjInstanceInstruction) {
            ((AbstractNewObjInstanceInstruction) cilExpr).resolveCallConstructorBody(jb, variable);
        }
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        throw new NoExpressionInstructionException(this.instruction);
    }
}
