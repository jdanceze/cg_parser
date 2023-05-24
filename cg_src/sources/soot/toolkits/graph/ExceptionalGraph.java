package soot.toolkits.graph;

import java.util.Collection;
import java.util.List;
import soot.Body;
import soot.Trap;
import soot.toolkits.exceptions.ThrowableSet;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/ExceptionalGraph.class */
public interface ExceptionalGraph<N> extends DirectedBodyGraph<N> {

    /* loaded from: gencallgraphv3.jar:soot/toolkits/graph/ExceptionalGraph$ExceptionDest.class */
    public interface ExceptionDest<N> {
        Trap getTrap();

        ThrowableSet getThrowables();

        N getHandlerNode();
    }

    @Override // soot.toolkits.graph.DirectedBodyGraph
    Body getBody();

    List<N> getUnexceptionalPredsOf(N n);

    List<N> getUnexceptionalSuccsOf(N n);

    List<N> getExceptionalPredsOf(N n);

    List<N> getExceptionalSuccsOf(N n);

    Collection<? extends ExceptionDest<N>> getExceptionDests(N n);
}
