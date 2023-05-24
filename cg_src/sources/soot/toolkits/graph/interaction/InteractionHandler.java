package soot.toolkits.graph.interaction;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.G;
import soot.PhaseOptions;
import soot.Singletons;
import soot.SootMethod;
import soot.Transform;
import soot.jimple.toolkits.annotation.callgraph.CallGraphGrapher;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/interaction/InteractionHandler.class */
public class InteractionHandler {
    private static final Logger logger = LoggerFactory.getLogger(InteractionHandler.class);
    private ArrayList<Object> stopUnitList;
    private CallGraphGrapher grapher;
    private SootMethod nextMethod;
    private boolean interactThisAnalysis;
    private boolean interactionCon;
    private IInteractionListener interactionListener;
    private String currentPhaseName;
    private boolean currentPhaseEnabled;
    private boolean doneCurrent;
    private boolean autoCon;
    private boolean cgReset = false;
    private boolean cgDone = false;

    public InteractionHandler(Singletons.Global g) {
    }

    public static InteractionHandler v() {
        return G.v().soot_toolkits_graph_interaction_InteractionHandler();
    }

    public ArrayList<Object> getStopUnitList() {
        return this.stopUnitList;
    }

    public void addToStopUnitList(Object elem) {
        if (this.stopUnitList == null) {
            this.stopUnitList = new ArrayList<>();
        }
        this.stopUnitList.add(elem);
    }

    public void removeFromStopUnitList(Object elem) {
        if (this.stopUnitList.contains(elem)) {
            this.stopUnitList.remove(elem);
        }
    }

    public void handleNewAnalysis(Transform t, Body b) {
        if (PhaseOptions.getBoolean(PhaseOptions.v().getPhaseOptions(t.getPhaseName()), "enabled")) {
            String name = String.valueOf(t.getPhaseName()) + " for method: " + b.getMethod().getName();
            currentPhaseName(name);
            currentPhaseEnabled(true);
            doneCurrent(false);
            return;
        }
        currentPhaseEnabled(false);
        setInteractThisAnalysis(false);
    }

    public void handleCfgEvent(DirectedGraph<?> g) {
        if (currentPhaseEnabled()) {
            logger.debug("Analyzing: " + currentPhaseName());
            doInteraction(new InteractionEvent(0, currentPhaseName()));
        }
        if (isInteractThisAnalysis()) {
            doInteraction(new InteractionEvent(2, g));
        }
    }

    public void handleStopAtNodeEvent(Object u) {
        if (isInteractThisAnalysis()) {
            doInteraction(new InteractionEvent(13, u));
        }
    }

    public void handleBeforeAnalysisEvent(Object beforeFlow) {
        if (isInteractThisAnalysis()) {
            if (autoCon()) {
                doInteraction(new InteractionEvent(11, beforeFlow));
            } else {
                doInteraction(new InteractionEvent(4, beforeFlow));
            }
        }
    }

    public void handleAfterAnalysisEvent(Object afterFlow) {
        if (isInteractThisAnalysis()) {
            if (autoCon()) {
                doInteraction(new InteractionEvent(12, afterFlow));
            } else {
                doInteraction(new InteractionEvent(5, afterFlow));
            }
        }
    }

    public void handleTransformDone(Transform t, Body b) {
        doneCurrent(true);
        if (isInteractThisAnalysis()) {
            doInteraction(new InteractionEvent(6, null));
        }
    }

    public void handleCallGraphStart(Object info, CallGraphGrapher grapher) {
        setGrapher(grapher);
        doInteraction(new InteractionEvent(50, info));
        if (!isCgReset()) {
            handleCallGraphNextMethod();
            return;
        }
        setCgReset(false);
        handleReset();
    }

    public void handleCallGraphNextMethod() {
        if (!cgDone()) {
            getGrapher().setNextMethod(getNextMethod());
            getGrapher().handleNextMethod();
        }
    }

    public void setCgReset(boolean v) {
        this.cgReset = v;
    }

    public boolean isCgReset() {
        return this.cgReset;
    }

    public void handleReset() {
        if (!cgDone()) {
            getGrapher().reset();
        }
    }

    public void handleCallGraphPart(Object info) {
        doInteraction(new InteractionEvent(52, info));
        if (!isCgReset()) {
            handleCallGraphNextMethod();
            return;
        }
        setCgReset(false);
        handleReset();
    }

    private void setGrapher(CallGraphGrapher g) {
        this.grapher = g;
    }

    private CallGraphGrapher getGrapher() {
        return this.grapher;
    }

    public void setNextMethod(SootMethod m) {
        this.nextMethod = m;
    }

    private SootMethod getNextMethod() {
        return this.nextMethod;
    }

    private synchronized void doInteraction(InteractionEvent event) {
        getInteractionListener().setEvent(event);
        getInteractionListener().handleEvent();
    }

    public synchronized void waitForContinue() {
        try {
            wait();
        } catch (InterruptedException e) {
            logger.debug(e.getMessage());
        }
    }

    public void setInteractThisAnalysis(boolean b) {
        this.interactThisAnalysis = b;
    }

    public boolean isInteractThisAnalysis() {
        return this.interactThisAnalysis;
    }

    public synchronized void setInteractionCon() {
        notify();
    }

    public boolean isInteractionCon() {
        return this.interactionCon;
    }

    public void setInteractionListener(IInteractionListener listener) {
        this.interactionListener = listener;
    }

    public IInteractionListener getInteractionListener() {
        return this.interactionListener;
    }

    public void currentPhaseName(String name) {
        this.currentPhaseName = name;
    }

    public String currentPhaseName() {
        return this.currentPhaseName;
    }

    public void currentPhaseEnabled(boolean b) {
        this.currentPhaseEnabled = b;
    }

    public boolean currentPhaseEnabled() {
        return this.currentPhaseEnabled;
    }

    public void cgDone(boolean b) {
        this.cgDone = b;
    }

    public boolean cgDone() {
        return this.cgDone;
    }

    public void doneCurrent(boolean b) {
        this.doneCurrent = b;
    }

    public boolean doneCurrent() {
        return this.doneCurrent;
    }

    public void autoCon(boolean b) {
        this.autoCon = b;
    }

    public boolean autoCon() {
        return this.autoCon;
    }

    public void stopInteraction(boolean b) {
        Options.v().set_interactive_mode(false);
    }
}
