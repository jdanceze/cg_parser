package soot.jimple.infoflow.data;

import heros.solver.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.util.extensiblelist.ExtensibleList;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/SourceContextAndPath.class */
public class SourceContextAndPath extends SourceContext implements Cloneable {
    protected ExtensibleList<Abstraction> path;
    protected ExtensibleList<Stmt> callStack;
    protected int neighborCounter;
    protected InfoflowConfiguration config;
    private int hashCode;

    public SourceContextAndPath(InfoflowConfiguration config, ISourceSinkDefinition definition, AccessPath value, Stmt stmt) {
        this(config, definition, value, stmt, null);
    }

    public SourceContextAndPath(InfoflowConfiguration config, ISourceSinkDefinition definition, AccessPath value, Stmt stmt, Object userData) {
        super(definition, value, stmt, userData);
        this.path = null;
        this.callStack = null;
        this.neighborCounter = 0;
        this.hashCode = 0;
        this.config = config;
    }

    public List<Stmt> getPath() {
        if (this.path == null) {
            return Collections.emptyList();
        }
        List<Stmt> stmtPath = new ArrayList<>(this.path.size());
        Iterator<Abstraction> it = this.path.reverseIterator();
        while (it.hasNext()) {
            Abstraction abs = it.next();
            if (abs.getCurrentStmt() != null) {
                stmtPath.add(abs.getCurrentStmt());
            }
        }
        return stmtPath;
    }

    public List<Abstraction> getAbstractionPath() {
        if (this.path == null) {
            return null;
        }
        List<Abstraction> reversePath = new ArrayList<>(this.path.size());
        Iterator<Abstraction> it = this.path.reverseIterator();
        while (it.hasNext()) {
            reversePath.add(it.next());
        }
        return reversePath;
    }

    public Abstraction getLastAbstraction() {
        return this.path.getLast();
    }

    private int getCallStackSize() {
        if (isCallStackEmpty()) {
            return 0;
        }
        return this.callStack.size();
    }

    public SourceContextAndPath extendPath(SourceContextAndPath other) {
        Stmt next;
        if (this.path == null || other.path == null || other.path.size() <= this.path.size()) {
            return null;
        }
        Stack<Abstraction> pathStack = new Stack<>();
        Abstraction lastAbs = getLastAbstraction();
        boolean foundCommonAbs = false;
        Iterator<Abstraction> pathIt = other.path.reverseIterator();
        while (pathIt.hasNext()) {
            Abstraction next2 = pathIt.next();
            if (next2 == lastAbs || (next2.neighbors != null && next2.neighbors.contains(lastAbs))) {
                foundCommonAbs = true;
                break;
            }
            pathStack.push(next2);
        }
        if (!foundCommonAbs) {
            return null;
        }
        SourceContextAndPath extendedScap = mo2740clone();
        while (!pathStack.isEmpty()) {
            extendedScap.path.add(pathStack.pop());
        }
        int newCallStackCapacity = other.getCallStackSize() - getCallStackSize();
        if (newCallStackCapacity < 0) {
            return null;
        }
        if (newCallStackCapacity > 0) {
            Stack<Stmt> callStackBuf = new Stack<>();
            Stmt topStmt = this.callStack == null ? null : this.callStack.getLast();
            Iterator<Stmt> callStackIt = other.callStack.reverseIterator();
            while (callStackIt.hasNext() && (next = callStackIt.next()) != topStmt) {
                callStackBuf.push(next);
            }
            if (callStackBuf.size() > 0) {
                if (extendedScap.callStack == null) {
                    extendedScap.callStack = new ExtensibleList<>();
                }
                while (!callStackBuf.isEmpty()) {
                    extendedScap.callStack.add(callStackBuf.pop());
                }
            }
        }
        return extendedScap;
    }

    public SourceContextAndPath extendPath(Abstraction abs) {
        return extendPath(abs, null);
    }

    public SourceContextAndPath extendPath(Abstraction abs, InfoflowConfiguration config) {
        if (abs == null) {
            return this;
        }
        if (abs.getCurrentStmt() == null && abs.getCorrespondingCallSite() == null) {
            return this;
        }
        InfoflowConfiguration.PathConfiguration pathConfig = config == null ? null : config.getPathConfiguration();
        boolean trackPath = pathConfig == null ? true : pathConfig.getPathReconstructionMode().reconstructPaths();
        if (abs.getCorrespondingCallSite() == null && !trackPath) {
            return this;
        }
        if (this.path != null) {
            Iterator<Abstraction> it = this.path.reverseIterator();
            while (it.hasNext()) {
                Abstraction a = it.next();
                if (a == abs) {
                    return null;
                }
            }
        }
        SourceContextAndPath scap = null;
        if (trackPath && abs.getCurrentStmt() != null) {
            if (this.path != null) {
                Abstraction topAbs = this.path.getLast();
                if (topAbs.equals(abs) && topAbs.getCorrespondingCallSite() != null && topAbs.getCorrespondingCallSite() == abs.getCorrespondingCallSite() && topAbs.getCurrentStmt() != abs.getCurrentStmt()) {
                    return null;
                }
            }
            scap = mo2740clone();
            if (scap.path == null) {
                scap.path = new ExtensibleList<>();
            }
            scap.path.add(abs);
            if (pathConfig != null && pathConfig.getMaxPathLength() > 0 && scap.path.size() > pathConfig.getMaxPathLength()) {
                return null;
            }
        }
        if (abs.getCorrespondingCallSite() != null && abs.getCorrespondingCallSite() != abs.getCurrentStmt()) {
            if (scap == null) {
                scap = mo2740clone();
            }
            if (scap.callStack == null) {
                scap.callStack = new ExtensibleList<>();
            } else if (pathConfig != null && pathConfig.getMaxCallStackSize() > 0 && scap.callStack.size() >= pathConfig.getMaxCallStackSize()) {
                return null;
            }
            scap.callStack.add(abs.getCorrespondingCallSite());
        }
        this.neighborCounter = abs.getNeighbors() == null ? 0 : abs.getNeighbors().size();
        return scap == null ? this : scap;
    }

    public Pair<SourceContextAndPath, Stmt> popTopCallStackItem() {
        Stmt lastStmt;
        if (this.callStack == null || this.callStack.isEmpty()) {
            return null;
        }
        SourceContextAndPath scap = mo2740clone();
        Object c = scap.callStack.removeLast();
        if (c instanceof ExtensibleList) {
            lastStmt = scap.callStack.getLast();
            scap.callStack = (ExtensibleList) c;
        } else {
            lastStmt = (Stmt) c;
        }
        if (scap.callStack.isEmpty()) {
            scap.callStack = null;
        }
        return new Pair<>(scap, lastStmt);
    }

    public boolean isCallStackEmpty() {
        return this.callStack == null || this.callStack.isEmpty();
    }

    public void setNeighborCounter(int counter) {
        this.neighborCounter = counter;
    }

    public int getNeighborCounter() {
        return this.neighborCounter;
    }

    @Override // soot.jimple.infoflow.data.SourceContext
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof SourceContextAndPath)) {
            return false;
        }
        SourceContextAndPath scap = (SourceContextAndPath) other;
        if (this.hashCode != 0 && scap.hashCode != 0 && this.hashCode != scap.hashCode) {
            return false;
        }
        boolean mergeDifferentPaths = (this.config.getPathAgnosticResults() || this.path == null || scap.path == null) ? false : true;
        if (mergeDifferentPaths && this.path.size() != scap.path.size()) {
            return false;
        }
        if (this.callStack == null || this.callStack.isEmpty()) {
            if (scap.callStack != null && !scap.callStack.isEmpty()) {
                return false;
            }
        } else if (scap.callStack == null || scap.callStack.isEmpty() || this.callStack.size() != scap.callStack.size() || !this.callStack.equals(scap.callStack)) {
            return false;
        }
        if (mergeDifferentPaths && !this.path.equals(scap.path)) {
            return false;
        }
        return super.equals(other);
    }

    @Override // soot.jimple.infoflow.data.SourceContext
    public int hashCode() {
        if (this.hashCode != 0) {
            return this.hashCode;
        }
        int result = super.hashCode();
        if (!this.config.getPathAgnosticResults()) {
            result = (31 * result) + (this.path == null ? 0 : this.path.hashCode());
        }
        this.hashCode = (31 * result) + (this.callStack == null ? 0 : this.callStack.hashCode());
        return this.hashCode;
    }

    @Override // soot.jimple.infoflow.data.SourceContext
    /* renamed from: clone */
    public SourceContextAndPath mo2740clone() {
        SourceContextAndPath scap = new SourceContextAndPath(this.config, this.definition, this.accessPath, this.stmt, this.userData);
        if (this.path != null) {
            scap.path = new ExtensibleList<>(this.path);
        }
        if (this.callStack != null) {
            scap.callStack = new ExtensibleList<>(this.callStack);
        }
        return scap;
    }

    @Override // soot.jimple.infoflow.data.SourceContext
    public String toString() {
        return String.valueOf(super.toString()) + "\n\ton Path: " + getAbstractionPath();
    }
}
