package soot.jimple.toolkits.callgraph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.MethodOrMethodContext;
import soot.util.queue.ChunkedQueue;
import soot.util.queue.QueueReader;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/ReachableMethods.class */
public class ReachableMethods {
    protected final ChunkedQueue<MethodOrMethodContext> reachables;
    protected final Set<MethodOrMethodContext> set;
    protected final QueueReader<MethodOrMethodContext> allReachables;
    protected QueueReader<MethodOrMethodContext> unprocessedMethods;
    protected Iterator<Edge> edgeSource;
    protected CallGraph cg;
    protected Filter filter;

    public ReachableMethods(CallGraph graph, Iterator<? extends MethodOrMethodContext> entryPoints, Filter filter) {
        this.reachables = new ChunkedQueue<>();
        this.set = new HashSet();
        this.allReachables = this.reachables.reader();
        this.filter = filter;
        this.cg = graph;
        addMethods(entryPoints);
        this.unprocessedMethods = this.reachables.reader();
        this.edgeSource = filter == null ? graph.listener() : filter.wrap(graph.listener());
    }

    public ReachableMethods(CallGraph graph, Iterator<? extends MethodOrMethodContext> entryPoints) {
        this(graph, entryPoints, null);
    }

    public ReachableMethods(CallGraph graph, Collection<? extends MethodOrMethodContext> entryPoints) {
        this(graph, entryPoints.iterator());
    }

    protected void addMethods(Iterator<? extends MethodOrMethodContext> methods) {
        while (methods.hasNext()) {
            addMethod(methods.next());
        }
    }

    protected void addMethod(MethodOrMethodContext m) {
        if (this.set.add(m)) {
            this.reachables.add(m);
        }
    }

    public void update() {
        MethodOrMethodContext srcMethod;
        while (this.edgeSource.hasNext()) {
            Edge e = this.edgeSource.next();
            if (e != null && (srcMethod = e.getSrc()) != null && !e.isInvalid() && this.set.contains(srcMethod)) {
                addMethod(e.getTgt());
            }
        }
        while (this.unprocessedMethods.hasNext()) {
            MethodOrMethodContext m = this.unprocessedMethods.next();
            Iterator<Edge> targets = this.cg.edgesOutOf(m);
            if (this.filter != null) {
                targets = this.filter.wrap(targets);
            }
            addMethods(new Targets(targets));
        }
    }

    public QueueReader<MethodOrMethodContext> listener() {
        return this.allReachables.m3089clone();
    }

    public QueueReader<MethodOrMethodContext> newListener() {
        return this.reachables.reader();
    }

    public boolean contains(MethodOrMethodContext m) {
        return this.set.contains(m);
    }

    public int size() {
        return this.set.size();
    }
}
