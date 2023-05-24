package soot.jimple.infoflow.results;

import heros.solver.Pair;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.util.ConcurrentHashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/results/InfoflowResults.class */
public class InfoflowResults {
    public static final int TERMINATION_SUCCESS = 0;
    public static final int TERMINATION_DATA_FLOW_TIMEOUT = 1;
    public static final int TERMINATION_DATA_FLOW_OOM = 2;
    public static final int TERMINATION_PATH_RECONSTRUCTION_TIMEOUT = 4;
    public static final int TERMINATION_PATH_RECONSTRUCTION_OOM = 8;
    private static final Logger logger = LoggerFactory.getLogger(InfoflowResults.class);
    protected volatile MultiMap<ResultSinkInfo, ResultSourceInfo> results;
    protected volatile InfoflowPerformanceData performanceData;
    protected volatile List<String> exceptions;
    protected int terminationState;
    protected volatile boolean pathAgnosticResults;

    public InfoflowResults() {
        this.results = null;
        this.performanceData = null;
        this.exceptions = null;
        this.terminationState = 0;
        this.pathAgnosticResults = true;
    }

    public InfoflowResults(boolean pathAgnosticResults) {
        this.results = null;
        this.performanceData = null;
        this.exceptions = null;
        this.terminationState = 0;
        this.pathAgnosticResults = true;
        this.pathAgnosticResults = pathAgnosticResults;
    }

    public List<String> getExceptions() {
        return this.exceptions;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX WARN: Type inference failed for: r0v6, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v9 */
    public void addException(String ex) {
        if (this.exceptions == null) {
            ?? r0 = this;
            synchronized (r0) {
                if (this.exceptions == null) {
                    this.exceptions = new ArrayList();
                }
                r0 = r0;
            }
        }
        this.exceptions.add(ex);
    }

    public int size() {
        if (this.results == null) {
            return 0;
        }
        return this.results.size();
    }

    public int numConnections() {
        int num = 0;
        if (this.results != null) {
            for (ResultSinkInfo sink : this.results.keySet()) {
                num += this.results.get(sink).size();
            }
        }
        return num;
    }

    public boolean isEmpty() {
        return this.results == null || this.results.isEmpty();
    }

    public boolean containsSink(Stmt sink) {
        for (ResultSinkInfo si : this.results.keySet()) {
            if (si.getStmt().equals(sink)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsSinkMethod(String sinkSignature) {
        return !findSinkByMethodSignature(sinkSignature).isEmpty();
    }

    public void addResult(ISourceSinkDefinition sinkDefinition, AccessPath sink, Stmt sinkStmt, ISourceSinkDefinition sourceDefinition, AccessPath source, Stmt sourceStmt) {
        addResult(new ResultSinkInfo(sinkDefinition, sink, sinkStmt), new ResultSourceInfo(sourceDefinition, source, sourceStmt, this.pathAgnosticResults));
    }

    public Pair<ResultSourceInfo, ResultSinkInfo> addResult(ISourceSinkDefinition sinkDefinition, AccessPath sink, Stmt sinkStmt, ISourceSinkDefinition sourceDefinition, AccessPath source, Stmt sourceStmt, Object userData, List<Abstraction> propagationPath, InfoflowManager manager) {
        List<Stmt> stmtPath = null;
        List<AccessPath> apPath = null;
        List<Stmt> csPath = null;
        if (propagationPath != null) {
            stmtPath = new ArrayList<>(propagationPath.size());
            apPath = new ArrayList<>(propagationPath.size());
            if (!manager.getConfig().getPathAgnosticResults()) {
                csPath = new ArrayList<>(propagationPath.size());
            }
            for (Abstraction pathAbs : propagationPath) {
                if (pathAbs.getCurrentStmt() != null) {
                    stmtPath.add(pathAbs.getCurrentStmt());
                    apPath.add(pathAbs.getAccessPath());
                    if (csPath != null) {
                        csPath.add(pathAbs.getCorrespondingCallSite());
                    }
                }
            }
        }
        return addResult(sinkDefinition, sink, sinkStmt, sourceDefinition, source, sourceStmt, userData, stmtPath, apPath, csPath, manager);
    }

    public Pair<ResultSourceInfo, ResultSinkInfo> addResult(ISourceSinkDefinition sinkDefinition, AccessPath sink, Stmt sinkStmt, ISourceSinkDefinition sourceDefinition, AccessPath source, Stmt sourceStmt, Object userData, List<Stmt> propagationPath, List<AccessPath> propagationAccessPath, List<Stmt> propagationCallSites) {
        return addResult(sinkDefinition, sink, sinkStmt, sourceDefinition, source, sourceStmt, userData, propagationPath, propagationAccessPath, propagationCallSites, null);
    }

    public Pair<ResultSourceInfo, ResultSinkInfo> addResult(ISourceSinkDefinition sinkDefinition, AccessPath sink, Stmt sinkStmt, ISourceSinkDefinition sourceDefinition, AccessPath source, Stmt sourceStmt, Object userData, List<Stmt> propagationPath, List<AccessPath> propagationAccessPath, List<Stmt> propagationCallSites, InfoflowManager manager) {
        ResultSourceInfo sourceObj = new ResultSourceInfo(sourceDefinition, source, sourceStmt, userData, propagationPath, propagationAccessPath, propagationCallSites, this.pathAgnosticResults);
        ResultSinkInfo sinkObj = new ResultSinkInfo(sinkDefinition, sink, sinkStmt);
        addResult(sinkObj, sourceObj);
        return new Pair<>(sourceObj, sinkObj);
    }

    public void addResult(DataFlowResult res) {
        if (res != null) {
            addResult(res.getSink(), res.getSource());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v7 */
    /* JADX WARN: Type inference failed for: r0v8, types: [java.lang.Throwable] */
    public void addResult(ResultSinkInfo sink, ResultSourceInfo source) {
        if (this.results == null) {
            ?? r0 = this;
            synchronized (r0) {
                if (this.results == null) {
                    this.results = new ConcurrentHashMultiMap();
                }
                r0 = r0;
            }
        }
        boolean put = this.results.put(sink, source);
        if (!put) {
            logger.debug("Found two equal paths");
        }
    }

    public void addAll(InfoflowResults results) {
        if (results == null) {
            return;
        }
        if (results.getExceptions() != null) {
            for (String e : results.getExceptions()) {
                addException(e);
            }
        }
        if (!results.isEmpty() && !results.getResults().isEmpty()) {
            for (ResultSinkInfo sink : results.getResults().keySet()) {
                for (ResultSourceInfo source : results.getResults().get(sink)) {
                    addResult(sink, source);
                }
            }
        }
        if (results.performanceData != null) {
            if (this.performanceData == null) {
                this.performanceData = results.performanceData;
            } else {
                this.performanceData.add(results.performanceData);
            }
        }
        this.terminationState |= results.terminationState;
    }

    public void addAll(Set<DataFlowResult> results) {
        if (results == null || results.isEmpty()) {
            return;
        }
        for (DataFlowResult res : results) {
            addResult(res);
        }
    }

    public MultiMap<ResultSinkInfo, ResultSourceInfo> getResults() {
        return this.results;
    }

    public Set<DataFlowResult> getResultSet() {
        if (this.results == null || this.results.isEmpty()) {
            return null;
        }
        Set<DataFlowResult> set = new HashSet<>(this.results.size() * 10);
        for (ResultSinkInfo sink : this.results.keySet()) {
            for (ResultSourceInfo source : this.results.get(sink)) {
                set.add(new DataFlowResult(source, sink));
            }
        }
        return set;
    }

    public boolean isPathBetween(Stmt sink, Stmt source) {
        if (this.results == null) {
            return false;
        }
        Set<ResultSourceInfo> sources = null;
        Iterator<ResultSinkInfo> it = this.results.keySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ResultSinkInfo sI = it.next();
            if (sI.getStmt().equals(sink)) {
                sources = this.results.get(sI);
                break;
            }
        }
        if (sources == null) {
            return false;
        }
        for (ResultSourceInfo src : sources) {
            if (src.getAccessPath().equals(source)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPathBetween(String sink, String source) {
        if (this.results == null) {
            return false;
        }
        for (ResultSinkInfo si : this.results.keySet()) {
            if (si.getAccessPath().getPlainValue().toString().equals(sink)) {
                Set<ResultSourceInfo> sources = this.results.get(si);
                for (ResultSourceInfo src : sources) {
                    if (src.getStmt().toString().contains(source)) {
                        return true;
                    }
                }
                continue;
            }
        }
        return false;
    }

    public boolean isPathBetweenMethods(String sinkSignature, String sourceSignature) {
        List<ResultSinkInfo> sinkVals = findSinkByMethodSignature(sinkSignature);
        for (ResultSinkInfo si : sinkVals) {
            Set<ResultSourceInfo> sources = this.results.get(si);
            if (sources == null) {
                return false;
            }
            for (ResultSourceInfo src : sources) {
                if (src.getStmt().containsInvokeExpr()) {
                    InvokeExpr expr = src.getStmt().getInvokeExpr();
                    if (expr.getMethod().getSignature().equals(sourceSignature)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private List<ResultSinkInfo> findSinkByMethodSignature(String sinkSignature) {
        if (this.results == null) {
            return Collections.emptyList();
        }
        List<ResultSinkInfo> sinkVals = new ArrayList<>();
        for (ResultSinkInfo si : this.results.keySet()) {
            if (si.getStmt().containsInvokeExpr()) {
                InvokeExpr expr = si.getStmt().getInvokeExpr();
                if (expr.getMethod().getSignature().equals(sinkSignature)) {
                    sinkVals.add(si);
                }
            }
        }
        return sinkVals;
    }

    public void printResults() {
        if (this.results == null) {
            return;
        }
        for (ResultSinkInfo sink : this.results.keySet()) {
            logger.info("Found a flow to sink {}, from the following sources:", sink);
            for (ResultSourceInfo source : this.results.get(sink)) {
                logger.info("\t- {}", source.getStmt());
                if (source.getPath() != null) {
                    logger.info("\t\ton Path {}", Arrays.toString(source.getPath()));
                }
            }
        }
    }

    public void printResults(Writer wr) throws IOException {
        if (this.results == null) {
            return;
        }
        for (ResultSinkInfo sink : this.results.keySet()) {
            wr.write("Found a flow to sink " + sink + ", from the following sources:\n");
            for (ResultSourceInfo source : this.results.get(sink)) {
                wr.write("\t- " + source.getStmt() + "\n");
                if (source.getPath() != null) {
                    wr.write("\t\ton Path " + Arrays.toString(source.getPath()) + "\n");
                }
            }
        }
    }

    public void clear() {
        this.results = null;
    }

    public int getTerminationState() {
        return this.terminationState;
    }

    public void setTerminationState(int terminationState) {
        this.terminationState = terminationState;
    }

    public boolean wasAbortedTimeout() {
        return (this.terminationState & 1) == 1 || (this.terminationState & 4) == 4;
    }

    public boolean wasTerminatedOutOfMemory() {
        return (this.terminationState & 2) == 2 || (this.terminationState & 8) == 8;
    }

    public InfoflowPerformanceData getPerformanceData() {
        return this.performanceData;
    }

    public void setPerformanceData(InfoflowPerformanceData performanceData) {
        this.performanceData = performanceData;
    }

    public void addPerformanceData(InfoflowPerformanceData performanceData) {
        if (performanceData == this.performanceData) {
            return;
        }
        if (this.performanceData == null) {
            this.performanceData = performanceData;
        } else {
            this.performanceData.add(performanceData);
        }
    }

    public void remove(DataFlowResult result) {
        this.results.remove(result.getSink(), result.getSource());
    }

    public String toString() {
        if (this.results == null) {
            return "<no results>";
        }
        boolean isFirst = true;
        StringBuilder sb = new StringBuilder();
        for (ResultSinkInfo sink : this.results.keySet()) {
            for (ResultSourceInfo source : this.results.get(sink)) {
                if (!isFirst) {
                    sb.append(", ");
                }
                isFirst = false;
                sb.append(source);
                sb.append(" -> ");
                sb.append(sink);
            }
        }
        return sb.toString();
    }

    public int hashCode() {
        int result = (31 * 1) + (this.exceptions == null ? 0 : this.exceptions.hashCode());
        return (31 * ((31 * ((31 * result) + (this.performanceData == null ? 0 : this.performanceData.hashCode()))) + (this.results == null ? 0 : this.results.hashCode()))) + this.terminationState;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        InfoflowResults other = (InfoflowResults) obj;
        if (this.exceptions == null) {
            if (other.exceptions != null) {
                return false;
            }
        } else if (!this.exceptions.equals(other.exceptions)) {
            return false;
        }
        if (this.performanceData == null) {
            if (other.performanceData != null) {
                return false;
            }
        } else if (!this.performanceData.equals(other.performanceData)) {
            return false;
        }
        if (this.results == null) {
            if (other.results != null) {
                return false;
            }
        } else if (!this.results.equals(other.results)) {
            return false;
        }
        if (this.terminationState != other.terminationState) {
            return false;
        }
        return true;
    }
}
