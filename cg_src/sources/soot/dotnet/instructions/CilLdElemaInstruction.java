package soot.dotnet.instructions;

import java.util.ArrayList;
import java.util.List;
import soot.Body;
import soot.Immediate;
import soot.Local;
import soot.Type;
import soot.UnitPatchingChain;
import soot.Value;
import soot.dotnet.exceptions.NoStatementInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.members.method.DotnetBodyVariableManager;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilLdElemaInstruction.class */
public class CilLdElemaInstruction extends AbstractCilnstruction {
    private boolean isMultiArrayRef;
    private final List<Value> indices;
    private Value baseArrayLocal;

    public CilLdElemaInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
        this.isMultiArrayRef = false;
        this.indices = new ArrayList();
        if (instruction.getIndicesCount() > 1) {
            this.isMultiArrayRef = true;
        }
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        throw new NoStatementInstructionException(this.instruction);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        CilInstruction cilExpr = CilInstructionFactory.fromInstructionMsg(this.instruction.getArray(), this.dotnetBody, this.cilBlock);
        this.baseArrayLocal = cilExpr.jimplifyExpr(jb);
        if (this.instruction.getIndicesCount() == 1) {
            Value ind = CilInstructionFactory.fromInstructionMsg(this.instruction.getIndices(0), this.dotnetBody, this.cilBlock).jimplifyExpr(jb);
            Value index = ind instanceof Immediate ? ind : DotnetBodyVariableManager.inlineLocals(ind, jb);
            return Jimple.v().newArrayRef(this.baseArrayLocal, index);
        }
        for (ProtoIlInstructions.IlInstructionMsg ind2 : this.instruction.getIndicesList()) {
            Value indExpr = CilInstructionFactory.fromInstructionMsg(ind2, this.dotnetBody, this.cilBlock).jimplifyExpr(jb);
            Value index2 = indExpr instanceof Immediate ? indExpr : DotnetBodyVariableManager.inlineLocals(indExpr, jb);
            this.indices.add(index2);
        }
        return Jimple.v().newArrayRef(this.baseArrayLocal, this.indices.get(0));
    }

    public boolean isMultiArrayRef() {
        return this.isMultiArrayRef;
    }

    public Value getBaseArrayLocal() {
        return this.baseArrayLocal;
    }

    public List<Value> getIndices() {
        return this.indices;
    }

    public Value resolveRewriteMultiArrAccess(Body jb) {
        ArrayRef newArrayRef;
        int size = getIndices().size();
        Local rLocalVar = null;
        for (int z = 0; z < size; z++) {
            if (z == 0) {
                newArrayRef = Jimple.v().newArrayRef(getBaseArrayLocal(), getIndices().get(z));
            } else {
                newArrayRef = Jimple.v().newArrayRef(rLocalVar, getIndices().get(z));
            }
            ArrayRef arrayRef = newArrayRef;
            Type arrayType = arrayRef.getType();
            Local lLocalVar = this.dotnetBody.variableManager.localGenerator.generateLocal(arrayType);
            AssignStmt assignStmt = Jimple.v().newAssignStmt(lLocalVar, arrayRef);
            jb.getUnits().add((UnitPatchingChain) assignStmt);
            rLocalVar = lLocalVar;
        }
        return rLocalVar;
    }
}
