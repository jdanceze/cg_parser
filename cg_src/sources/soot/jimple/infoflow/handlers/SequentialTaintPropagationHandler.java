package soot.jimple.infoflow.handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import soot.Unit;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.handlers.TaintPropagationHandler;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/handlers/SequentialTaintPropagationHandler.class */
public class SequentialTaintPropagationHandler implements TaintPropagationHandler {
    private final List<TaintPropagationHandler> innerHandlers;

    public SequentialTaintPropagationHandler() {
        this.innerHandlers = new ArrayList();
    }

    public SequentialTaintPropagationHandler(List<TaintPropagationHandler> handlers) {
        this.innerHandlers = new ArrayList(handlers);
    }

    public void addHandler(TaintPropagationHandler handler) {
        if (handler != null) {
            this.innerHandlers.add(handler);
        }
    }

    public List<TaintPropagationHandler> getHandlers() {
        return this.innerHandlers;
    }

    @Override // soot.jimple.infoflow.handlers.TaintPropagationHandler
    public void notifyFlowIn(Unit stmt, Abstraction taint, InfoflowManager manager, TaintPropagationHandler.FlowFunctionType type) {
        for (TaintPropagationHandler handler : this.innerHandlers) {
            handler.notifyFlowIn(stmt, taint, manager, type);
        }
    }

    @Override // soot.jimple.infoflow.handlers.TaintPropagationHandler
    public Set<Abstraction> notifyFlowOut(Unit stmt, Abstraction d1, Abstraction incoming, Set<Abstraction> outgoing, InfoflowManager manager, TaintPropagationHandler.FlowFunctionType type) {
        Set<Abstraction> resultSet = new HashSet<>();
        for (TaintPropagationHandler handler : this.innerHandlers) {
            Set<Abstraction> handlerResults = handler.notifyFlowOut(stmt, d1, incoming, outgoing, manager, type);
            if (handlerResults != null && !handlerResults.isEmpty()) {
                resultSet.addAll(handlerResults);
            }
        }
        return resultSet;
    }

    public void addAllHandlers(TaintPropagationHandler[] handlers) {
        if (handlers != null && handlers.length > 0) {
            for (TaintPropagationHandler handler : handlers) {
                addHandler(handler);
            }
        }
    }
}
