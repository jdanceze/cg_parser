package soot.dotnet.instructions;

import java.util.ArrayList;
import java.util.List;
import soot.ArrayType;
import soot.Body;
import soot.Immediate;
import soot.Type;
import soot.Value;
import soot.dotnet.exceptions.NoStatementInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.members.method.DotnetBodyVariableManager;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.types.DotnetTypeFactory;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilNewArrInstruction.class */
public class CilNewArrInstruction extends AbstractCilnstruction {
    public CilNewArrInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        throw new NoStatementInstructionException(this.instruction);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        Type type = DotnetTypeFactory.toSootType(this.instruction.getType().getFullname());
        List<Value> sizesOfArr = new ArrayList<>();
        for (ProtoIlInstructions.IlInstructionMsg index : this.instruction.getIndicesList()) {
            CilInstruction cilExpr = CilInstructionFactory.fromInstructionMsg(index, this.dotnetBody, this.cilBlock);
            Value value = cilExpr.jimplifyExpr(jb);
            Value val = value instanceof Immediate ? value : DotnetBodyVariableManager.inlineLocals(value, jb);
            sizesOfArr.add(val);
        }
        if (sizesOfArr.size() == 1) {
            return Jimple.v().newNewArrayExpr(type, sizesOfArr.get(0));
        }
        ArrayType arrayType = ArrayType.v(type, sizesOfArr.size());
        return Jimple.v().newNewMultiArrayExpr(arrayType, sizesOfArr);
    }
}
