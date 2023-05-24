package polyglot.visit;

import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/CFGBuildError.class */
public class CFGBuildError extends InternalCompilerError {
    public CFGBuildError(String msg) {
        super(msg);
    }

    public CFGBuildError(Position position, String msg) {
        super(position, msg);
    }

    public CFGBuildError(String msg, Position position) {
        super(msg, position);
    }
}
