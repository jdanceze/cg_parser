package soot.dotnet.instructions;

import soot.Body;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilInstruction.class */
public interface CilInstruction {
    void jimplify(Body body);

    Value jimplifyExpr(Body body);
}
