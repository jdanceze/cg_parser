package soot.dotnet.instructions;

import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/AbstractCilnstruction.class */
public abstract class AbstractCilnstruction implements CilInstruction {
    protected final ProtoIlInstructions.IlInstructionMsg instruction;
    protected final DotnetBody dotnetBody;
    protected final CilBlock cilBlock;

    public AbstractCilnstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        this.instruction = instruction;
        this.dotnetBody = dotnetBody;
        this.cilBlock = cilBlock;
    }
}
