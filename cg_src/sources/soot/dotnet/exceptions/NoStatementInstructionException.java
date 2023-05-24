package soot.dotnet.exceptions;

import soot.dotnet.proto.ProtoIlInstructions;
/* loaded from: gencallgraphv3.jar:soot/dotnet/exceptions/NoStatementInstructionException.class */
public class NoStatementInstructionException extends RuntimeException {
    public NoStatementInstructionException() {
        super("CilInstruction is not a statement instruction!");
    }

    public NoStatementInstructionException(ProtoIlInstructions.IlInstructionMsg instructionMsg) {
        super("CilInstruction " + instructionMsg.getOpCode().name() + " is not a statement instruction!");
    }
}
