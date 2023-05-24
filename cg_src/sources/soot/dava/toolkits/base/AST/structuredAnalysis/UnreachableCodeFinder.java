package soot.dava.toolkits.base.AST.structuredAnalysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Local;
import soot.Value;
import soot.dava.DecompilationException;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnaryBinaryCondition;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/structuredAnalysis/UnreachableCodeFinder.class */
public class UnreachableCodeFinder extends StructuredAnalysis {
    public static boolean DEBUG = false;

    /* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/structuredAnalysis/UnreachableCodeFinder$UnreachableCodeFlowSet.class */
    public class UnreachableCodeFlowSet extends DavaFlowSet {
        public UnreachableCodeFlowSet() {
        }

        @Override // soot.dava.toolkits.base.AST.structuredAnalysis.DavaFlowSet, soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
        public UnreachableCodeFlowSet clone() {
            if (size() != 1) {
                throw new DecompilationException("unreachableCodeFlow set size should always be 1");
            }
            Boolean temp = (Boolean) this.elements[0];
            UnreachableCodeFlowSet toReturn = new UnreachableCodeFlowSet();
            toReturn.add(new Boolean(temp.booleanValue()));
            toReturn.copyInternalDataFrom(this);
            return toReturn;
        }

        @Override // soot.dava.toolkits.base.AST.structuredAnalysis.DavaFlowSet, soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
        public void intersection(FlowSet otherFlow, FlowSet destFlow) {
            UnreachableCodeFlowSet workingSet;
            if (UnreachableCodeFinder.DEBUG) {
                System.out.println("In intersection");
            }
            if (!(otherFlow instanceof UnreachableCodeFlowSet) || !(destFlow instanceof UnreachableCodeFlowSet)) {
                super.intersection(otherFlow, destFlow);
                return;
            }
            UnreachableCodeFlowSet other = (UnreachableCodeFlowSet) otherFlow;
            UnreachableCodeFlowSet dest = (UnreachableCodeFlowSet) destFlow;
            if (dest == other || dest == this) {
                workingSet = new UnreachableCodeFlowSet();
            } else {
                workingSet = dest;
                workingSet.clear();
            }
            if (other.size() != 1 || size() != 1) {
                System.out.println("Other size = " + other.size());
                System.out.println("This size = " + size());
                throw new DecompilationException("UnreachableCodeFlowSet size should always be one");
            }
            Boolean thisPath = (Boolean) this.elements[0];
            Boolean otherPath = (Boolean) other.elements[0];
            if (!thisPath.booleanValue() && !otherPath.booleanValue()) {
                workingSet.add(new Boolean(false));
            } else {
                workingSet.add(new Boolean(true));
            }
            workingSet.copyInternalDataFrom(this);
            if (otherFlow instanceof DavaFlowSet) {
                workingSet.copyInternalDataFrom((DavaFlowSet) otherFlow);
            }
            if (workingSet != dest) {
                workingSet.copy(dest);
            }
            if (UnreachableCodeFinder.DEBUG) {
                System.out.println("destFlow contains size:" + ((UnreachableCodeFlowSet) destFlow).size());
            }
        }
    }

    public UnreachableCodeFinder(Object analyze) {
        process(analyze, newInitialFlow());
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public void setMergeType() {
        this.MERGETYPE = 2;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet newInitialFlow() {
        DavaFlowSet newSet = emptyFlowSet();
        newSet.add(new Boolean(true));
        return newSet;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet emptyFlowSet() {
        return new UnreachableCodeFlowSet();
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public UnreachableCodeFlowSet cloneFlowSet(DavaFlowSet flowSet) {
        if (flowSet instanceof UnreachableCodeFlowSet) {
            return ((UnreachableCodeFlowSet) flowSet).clone();
        }
        throw new RuntimeException("Clone only implemented for UnreachableCodeFlowSet");
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processUnaryBinaryCondition(ASTUnaryBinaryCondition cond, DavaFlowSet input) {
        return input;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processSynchronizedLocal(Local local, DavaFlowSet input) {
        return input;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processSwitchKey(Value key, DavaFlowSet input) {
        return input;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processStatement(Stmt s, DavaFlowSet input) {
        return input;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processAbruptStatements(Stmt s, DavaFlowSet input) {
        if (DEBUG) {
            System.out.println("processing stmt " + s);
        }
        if ((s instanceof ReturnStmt) || (s instanceof RetStmt) || (s instanceof ReturnVoidStmt)) {
            UnreachableCodeFlowSet toReturn = new UnreachableCodeFlowSet();
            toReturn.add(new Boolean(false));
            toReturn.copyInternalDataFrom(input);
            if (DEBUG) {
                System.out.println("\tstmt is a return stmt. Hence sending forward false");
            }
            return toReturn;
        } else if (s instanceof DAbruptStmt) {
            DAbruptStmt abStmt = (DAbruptStmt) s;
            if (!abStmt.is_Continue() && !abStmt.is_Break()) {
                throw new RuntimeException("Found a DAbruptStmt which is neither break nor continue!!");
            }
            DavaFlowSet temp = new UnreachableCodeFlowSet();
            SETNodeLabel nodeLabel = abStmt.getLabel();
            if (abStmt.is_Break()) {
                if (nodeLabel != null && nodeLabel.toString() != null) {
                    temp.addToBreakList(nodeLabel.toString(), input);
                } else {
                    temp.addToImplicitBreaks(abStmt, input);
                }
            }
            temp.add(new Boolean(false));
            temp.copyInternalDataFrom(input);
            if (DEBUG) {
                System.out.println("\tstmt is an abrupt stmt. Hence sending forward false");
            }
            return temp;
        } else {
            if (DEBUG) {
                System.out.println("\tstmt is not an abrupt stmt.");
            }
            return processStatement(s, input);
        }
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet handleBreak(String label, DavaFlowSet output, ASTNode node) {
        if (DEBUG) {
            System.out.println("Handling break. Output contains" + ((UnreachableCodeFlowSet) output).size());
        }
        if (!(output instanceof UnreachableCodeFlowSet)) {
            throw new RuntimeException("handleBreak is only implemented for UnreachableCodeFlowSet type");
        }
        List explicitSet = output.getBreakSet(label);
        if (node == null) {
            throw new RuntimeException("ASTNode sent to handleBreak was null");
        }
        List implicitSet = output.getImplicitlyBrokenSets(node);
        DavaFlowSet toReturn = emptyFlowSet();
        toReturn.copyInternalDataFrom(output);
        if ((explicitSet != null && explicitSet.size() > 0) || (implicitSet != null && implicitSet.size() > 0)) {
            if (DEBUG) {
                System.out.println("\tBreak sets (implicit and explicit are nonempty hence forwarding true");
            }
            toReturn.add(new Boolean(true));
            return toReturn;
        }
        toReturn.add(new Boolean(false));
        if (DEBUG) {
            System.out.println("\tBreak sets (implicit and explicit are empty hence forwarding merge of false with inset");
        }
        return merge(toReturn, output);
    }

    public boolean isReachable(Object input) {
        if (!(input instanceof UnreachableCodeFlowSet)) {
            throw new DecompilationException("Implemented only for UnreachableCodeFlowSet");
        }
        UnreachableCodeFlowSet checking = (UnreachableCodeFlowSet) input;
        if (checking.size() != 1) {
            throw new DecompilationException("unreachableCodeFlow set size should always be 1");
        }
        if (((Boolean) checking.elements[0]).booleanValue()) {
            if (DEBUG) {
                System.out.println("\t Reachable");
                return true;
            }
            return true;
        } else if (DEBUG) {
            System.out.println("\t NOT Reachable");
            return false;
        } else {
            return false;
        }
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processASTStatementSequenceNode(ASTStatementSequenceNode node, DavaFlowSet input) {
        if (DEBUG) {
            System.out.println("Processing statement sequence node");
        }
        if (!isReachable(input)) {
            return input;
        }
        return super.processASTStatementSequenceNode(node, input);
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processASTLabeledBlockNode(ASTLabeledBlockNode node, DavaFlowSet input) {
        if (DEBUG) {
            System.out.println("Processing labeled block node node");
        }
        if (!isReachable(input)) {
            return input;
        }
        return super.processASTLabeledBlockNode(node, input);
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processASTSynchronizedBlockNode(ASTSynchronizedBlockNode node, DavaFlowSet input) {
        if (DEBUG) {
            System.out.println("Processing synchronized block node");
        }
        if (!isReachable(input)) {
            return input;
        }
        return super.processASTSynchronizedBlockNode(node, input);
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processASTIfElseNode(ASTIfElseNode node, DavaFlowSet input) {
        if (DEBUG) {
            System.out.println("Processing ifelse node");
        }
        if (!isReachable(input)) {
            return input;
        }
        return super.processASTIfElseNode(node, input);
    }

    public DavaFlowSet ifNotReachableReturnInputElseProcessBodyAndReturnTrue(ASTNode node, DavaFlowSet input) {
        if (DEBUG) {
            System.out.println("Processing " + node.getClass() + " node");
        }
        if (!isReachable(input)) {
            return input;
        }
        DavaFlowSet output = processSingleSubBodyNode(node, input);
        UnreachableCodeFlowSet toReturn = new UnreachableCodeFlowSet();
        toReturn.add(new Boolean(true));
        toReturn.copyInternalDataFrom(output);
        return toReturn;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processASTIfNode(ASTIfNode node, DavaFlowSet input) {
        return ifNotReachableReturnInputElseProcessBodyAndReturnTrue(node, input);
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processASTWhileNode(ASTWhileNode node, DavaFlowSet input) {
        return ifNotReachableReturnInputElseProcessBodyAndReturnTrue(node, input);
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processASTDoWhileNode(ASTDoWhileNode node, DavaFlowSet input) {
        return ifNotReachableReturnInputElseProcessBodyAndReturnTrue(node, input);
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processASTUnconditionalLoopNode(ASTUnconditionalLoopNode node, DavaFlowSet input) {
        return ifNotReachableReturnInputElseProcessBodyAndReturnTrue(node, input);
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processASTForLoopNode(ASTForLoopNode node, DavaFlowSet input) {
        return ifNotReachableReturnInputElseProcessBodyAndReturnTrue(node, input);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v35, types: [soot.dava.toolkits.base.AST.structuredAnalysis.DavaFlowSet] */
    /* JADX WARN: Type inference failed for: r0v43, types: [soot.dava.toolkits.base.AST.structuredAnalysis.DavaFlowSet] */
    /* JADX WARN: Type inference failed for: r0v45, types: [soot.dava.toolkits.base.AST.structuredAnalysis.DavaFlowSet] */
    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processASTSwitchNode(ASTSwitchNode node, DavaFlowSet input) {
        if (DEBUG) {
            System.out.println("Processing switch node");
        }
        if (!isReachable(input)) {
            return input;
        }
        List<Object> indexList = node.getIndexList();
        Map<Object, List<Object>> index2BodyList = node.getIndex2BodyList();
        UnreachableCodeFlowSet cloneFlowSet = cloneFlowSet(input);
        DavaFlowSet out = null;
        DavaFlowSet defaultOut = null;
        List<DavaFlowSet> toMergeBreaks = new ArrayList<>();
        for (Object currentIndex : indexList) {
            List body = index2BodyList.get(currentIndex);
            if (body != null) {
                out = process(body, cloneFlowSet((DavaFlowSet) cloneFlowSet));
                toMergeBreaks.add(cloneFlowSet(out));
                if (currentIndex instanceof String) {
                    defaultOut = out;
                }
            }
        }
        UnreachableCodeFlowSet unreachableCodeFlowSet = cloneFlowSet;
        if (out != null) {
            if (defaultOut != null) {
                unreachableCodeFlowSet = merge(defaultOut, out);
            } else {
                unreachableCodeFlowSet = merge(cloneFlowSet, out);
            }
        }
        String label = getLabel(node);
        List<DavaFlowSet> outList = new ArrayList<>();
        for (DavaFlowSet tmb : toMergeBreaks) {
            outList.add(handleBreak(label, tmb, node));
        }
        UnreachableCodeFlowSet unreachableCodeFlowSet2 = unreachableCodeFlowSet;
        for (DavaFlowSet fo : outList) {
            unreachableCodeFlowSet2 = merge(unreachableCodeFlowSet2, fo);
        }
        return unreachableCodeFlowSet2;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processASTTryNode(ASTTryNode node, DavaFlowSet input) {
        if (DEBUG) {
            System.out.println("Processing try node");
        }
        if (!isReachable(input)) {
            return input;
        }
        List<Object> tryBody = node.get_TryBody();
        DavaFlowSet tryBodyOutput = process(tryBody, input);
        DavaFlowSet inputCatch = newInitialFlow();
        List<Object> catchList = node.get_CatchList();
        Iterator<Object> it = catchList.iterator();
        List<DavaFlowSet> catchOutput = new ArrayList<>();
        while (it.hasNext()) {
            ASTTryNode.container catchBody = (ASTTryNode.container) it.next();
            List body = (List) catchBody.o;
            DavaFlowSet tempResult = process(body, cloneFlowSet(inputCatch));
            catchOutput.add(tempResult);
        }
        String label = getLabel(node);
        List<DavaFlowSet> outList = new ArrayList<>();
        outList.add(handleBreak(label, tryBodyOutput, node));
        for (DavaFlowSet co : catchOutput) {
            DavaFlowSet temp = handleBreak(label, co, node);
            outList.add(temp);
        }
        DavaFlowSet out = tryBodyOutput;
        for (DavaFlowSet oe : outList) {
            out = merge(out, oe);
        }
        for (DavaFlowSet ce : catchOutput) {
            out = merge(out, ce);
        }
        return out;
    }

    public boolean isConstructReachable(Object construct) {
        Object temp = getBeforeSet(construct);
        if (temp == null) {
            return true;
        }
        if (DEBUG) {
            System.out.println("Have before set");
        }
        Boolean reachable = (Boolean) ((UnreachableCodeFlowSet) temp).elements[0];
        return reachable.booleanValue();
    }
}
