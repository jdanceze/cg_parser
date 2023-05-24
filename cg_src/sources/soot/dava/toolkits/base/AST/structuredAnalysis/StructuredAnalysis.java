package soot.dava.toolkits.base.AST.structuredAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Local;
import soot.Value;
import soot.dava.internal.AST.ASTAggregatedCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTLabeledNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnaryBinaryCondition;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/structuredAnalysis/StructuredAnalysis.class */
public abstract class StructuredAnalysis<E> {
    public static boolean DEBUG = false;
    public static boolean DEBUG_IF = false;
    public static boolean DEBUG_WHILE = false;
    public static boolean DEBUG_STATEMENTS = false;
    public static boolean DEBUG_TRY = false;
    DavaFlowSet<E> NOPATH = emptyFlowSet();
    final int UNDEFINED = 0;
    final int UNION = 1;
    final int INTERSECTION = 2;
    HashMap<Object, DavaFlowSet<E>> beforeSets = new HashMap<>();
    HashMap<Object, DavaFlowSet<E>> afterSets = new HashMap<>();
    public int MERGETYPE = 0;

    public abstract void setMergeType();

    public abstract DavaFlowSet<E> newInitialFlow();

    public abstract DavaFlowSet<E> emptyFlowSet();

    public abstract DavaFlowSet<E> cloneFlowSet(DavaFlowSet<E> davaFlowSet);

    public abstract DavaFlowSet<E> processStatement(Stmt stmt, DavaFlowSet<E> davaFlowSet);

    public abstract DavaFlowSet<E> processUnaryBinaryCondition(ASTUnaryBinaryCondition aSTUnaryBinaryCondition, DavaFlowSet<E> davaFlowSet);

    public abstract DavaFlowSet<E> processSynchronizedLocal(Local local, DavaFlowSet<E> davaFlowSet);

    public abstract DavaFlowSet<E> processSwitchKey(Value value, DavaFlowSet<E> davaFlowSet);

    public StructuredAnalysis() {
        setMergeType();
        if (this.MERGETYPE == 0) {
            throw new RuntimeException("MERGETYPE UNDEFINED");
        }
    }

    public void print(Object toPrint) {
        System.out.println(toPrint.toString());
    }

    public DavaFlowSet<E> processCondition(ASTCondition cond, DavaFlowSet<E> input) {
        if (cond instanceof ASTUnaryBinaryCondition) {
            return processUnaryBinaryCondition((ASTUnaryBinaryCondition) cond, input);
        }
        if (cond instanceof ASTAggregatedCondition) {
            ASTCondition left = ((ASTAggregatedCondition) cond).getLeftOp();
            DavaFlowSet<E> output1 = processCondition(left, input);
            ASTCondition right = ((ASTAggregatedCondition) cond).getRightOp();
            DavaFlowSet<E> output2 = processCondition(right, output1);
            return merge(output1, output2);
        }
        throw new RuntimeException("Unknown ASTCondition found in structred flow analysis");
    }

    public DavaFlowSet<E> process(Object body, DavaFlowSet<E> input) {
        if (body instanceof ASTNode) {
            this.beforeSets.put(body, input);
            DavaFlowSet<E> temp = processASTNode((ASTNode) body, input);
            this.afterSets.put(body, temp);
            return temp;
        } else if (body instanceof Stmt) {
            this.beforeSets.put(body, input);
            DavaFlowSet<E> result = processAbruptStatements((Stmt) body, input);
            this.afterSets.put(body, result);
            return result;
        } else if (body instanceof AugmentedStmt) {
            AugmentedStmt as = (AugmentedStmt) body;
            Stmt s = as.get_Stmt();
            this.beforeSets.put(s, input);
            DavaFlowSet<E> result2 = processAbruptStatements(s, input);
            this.afterSets.put(s, result2);
            return result2;
        } else if (body instanceof List) {
            DavaFlowSet<E> result3 = input;
            for (Object temp2 : (List) body) {
                if (!(temp2 instanceof ASTNode)) {
                    throw new RuntimeException("Body sent to be processed by StructuredAnalysis contains a list which does not have ASTNodes");
                }
                this.beforeSets.put(temp2, result3);
                result3 = processASTNode((ASTNode) temp2, result3);
                this.afterSets.put(temp2, result3);
            }
            return result3;
        } else {
            throw new RuntimeException("Body sent to be processed by StructuredAnalysis is not a valid body");
        }
    }

    public DavaFlowSet<E> processASTNode(ASTNode node, DavaFlowSet<E> input) {
        if (node instanceof ASTDoWhileNode) {
            return processASTDoWhileNode((ASTDoWhileNode) node, input);
        }
        if (node instanceof ASTForLoopNode) {
            return processASTForLoopNode((ASTForLoopNode) node, input);
        }
        if (node instanceof ASTIfElseNode) {
            return processASTIfElseNode((ASTIfElseNode) node, input);
        }
        if (node instanceof ASTIfNode) {
            return processASTIfNode((ASTIfNode) node, input);
        }
        if (node instanceof ASTLabeledBlockNode) {
            return processASTLabeledBlockNode((ASTLabeledBlockNode) node, input);
        }
        if (node instanceof ASTMethodNode) {
            return processASTMethodNode((ASTMethodNode) node, input);
        }
        if (node instanceof ASTStatementSequenceNode) {
            return processASTStatementSequenceNode((ASTStatementSequenceNode) node, input);
        }
        if (node instanceof ASTSwitchNode) {
            return processASTSwitchNode((ASTSwitchNode) node, input);
        }
        if (node instanceof ASTSynchronizedBlockNode) {
            return processASTSynchronizedBlockNode((ASTSynchronizedBlockNode) node, input);
        }
        if (node instanceof ASTTryNode) {
            return processASTTryNode((ASTTryNode) node, input);
        }
        if (node instanceof ASTWhileNode) {
            return processASTWhileNode((ASTWhileNode) node, input);
        }
        if (node instanceof ASTUnconditionalLoopNode) {
            return processASTUnconditionalLoopNode((ASTUnconditionalLoopNode) node, input);
        }
        throw new RuntimeException("processASTNode called using unknown node type");
    }

    public final DavaFlowSet<E> processSingleSubBodyNode(ASTNode node, DavaFlowSet<E> input) {
        List<Object> subBodies = node.get_SubBodies();
        if (subBodies.size() != 1) {
            throw new RuntimeException("processSingleSubBodyNode called with a node without one subBody");
        }
        List subBody = (List) subBodies.get(0);
        return process(subBody, input);
    }

    public String getLabel(ASTNode node) {
        Object temp;
        if ((node instanceof ASTLabeledNode) && (temp = ((ASTLabeledNode) node).get_Label()) != null) {
            return temp.toString();
        }
        return null;
    }

    public DavaFlowSet<E> processAbruptStatements(Stmt s, DavaFlowSet<E> input) {
        if ((s instanceof ReturnStmt) || (s instanceof RetStmt) || (s instanceof ReturnVoidStmt)) {
            return this.NOPATH;
        }
        if (s instanceof DAbruptStmt) {
            DAbruptStmt abStmt = (DAbruptStmt) s;
            if (!abStmt.is_Continue() && !abStmt.is_Break()) {
                throw new RuntimeException("Found a DAbruptStmt which is neither break nor continue!!");
            }
            DavaFlowSet<E> temp = this.NOPATH;
            SETNodeLabel nodeLabel = abStmt.getLabel();
            if (nodeLabel != null && nodeLabel.toString() != null) {
                if (abStmt.is_Continue()) {
                    temp.addToContinueList(nodeLabel.toString(), input);
                } else if (abStmt.is_Break()) {
                    temp.addToBreakList(nodeLabel.toString(), input);
                } else {
                    throw new RuntimeException("Found abruptstmt which is neither break nor continue");
                }
            } else if (abStmt.is_Continue()) {
                temp.addToImplicitContinues(abStmt, input);
            } else if (abStmt.is_Break()) {
                temp.addToImplicitBreaks(abStmt, input);
            } else {
                throw new RuntimeException("Found abruptstmt which is neither break nor continue");
            }
            return temp;
        }
        return processStatement(s, input);
    }

    public DavaFlowSet<E> processASTMethodNode(ASTMethodNode node, DavaFlowSet<E> input) {
        DavaFlowSet<E> temp = processSingleSubBodyNode(node, input);
        return temp;
    }

    public DavaFlowSet<E> processASTStatementSequenceNode(ASTStatementSequenceNode node, DavaFlowSet<E> input) {
        DavaFlowSet<E> output = cloneFlowSet(input);
        for (AugmentedStmt as : node.getStatements()) {
            Stmt s = as.get_Stmt();
            output = process(s, output);
            if (DEBUG_STATEMENTS) {
                System.out.println("After Processing statement " + s + output.toString());
            }
        }
        return output;
    }

    public DavaFlowSet<E> processASTLabeledBlockNode(ASTLabeledBlockNode node, DavaFlowSet<E> input) {
        DavaFlowSet<E> output1 = processSingleSubBodyNode(node, input);
        String label = getLabel(node);
        return handleBreak(label, output1, node);
    }

    public DavaFlowSet<E> processASTSynchronizedBlockNode(ASTSynchronizedBlockNode node, DavaFlowSet<E> input) {
        DavaFlowSet<E> output = processSingleSubBodyNode(node, processSynchronizedLocal(node.getLocal(), input));
        String label = getLabel(node);
        return handleBreak(label, output, node);
    }

    public DavaFlowSet<E> processASTIfNode(ASTIfNode node, DavaFlowSet<E> input) {
        DavaFlowSet<E> input2 = processCondition(node.get_Condition(), input);
        DavaFlowSet<E> output1 = processSingleSubBodyNode(node, input2);
        DavaFlowSet<E> output2 = merge(input2, output1);
        String label = getLabel(node);
        DavaFlowSet<E> temp = handleBreak(label, output2, node);
        if (DEBUG_IF) {
            System.out.println("Exiting if node" + temp.toString());
        }
        return temp;
    }

    public DavaFlowSet<E> processASTIfElseNode(ASTIfElseNode node, DavaFlowSet<E> input) {
        List<Object> subBodies = node.get_SubBodies();
        if (subBodies.size() != 2) {
            throw new RuntimeException("processASTIfElseNode called with a node without two subBodies");
        }
        List subBodyOne = (List) subBodies.get(0);
        List subBodyTwo = (List) subBodies.get(1);
        DavaFlowSet<E> input2 = processCondition(node.get_Condition(), input);
        DavaFlowSet<E> clonedInput = cloneFlowSet(input2);
        DavaFlowSet<E> output1 = process(subBodyOne, clonedInput);
        DavaFlowSet<E> clonedInput2 = cloneFlowSet(input2);
        DavaFlowSet<E> output2 = process(subBodyTwo, clonedInput2);
        DavaFlowSet<E> temp = merge(output1, output2);
        String label = getLabel(node);
        DavaFlowSet<E> output12 = handleBreak(label, temp, node);
        return output12;
    }

    public DavaFlowSet<E> processASTWhileNode(ASTWhileNode node, DavaFlowSet<E> input) {
        DavaFlowSet<E> lastin;
        DavaFlowSet<E> initialInput = cloneFlowSet(input);
        String label = getLabel(node);
        DavaFlowSet<E> input2 = processCondition(node.get_Condition(), input);
        if (DEBUG_WHILE) {
            System.out.println("Going int while (condition processed): " + input2.toString());
        }
        do {
            lastin = cloneFlowSet(input2);
            DavaFlowSet<E> output = processSingleSubBodyNode(node, input2);
            input2 = processCondition(node.get_Condition(), merge(initialInput, handleContinue(label, output, node)));
        } while (isDifferent(lastin, input2));
        DavaFlowSet<E> temp = handleBreak(label, input2, node);
        if (DEBUG_WHILE) {
            System.out.println("Going out of while: " + temp.toString());
        }
        return temp;
    }

    public DavaFlowSet<E> processASTDoWhileNode(ASTDoWhileNode node, DavaFlowSet<E> input) {
        DavaFlowSet<E> lastin;
        DavaFlowSet<E> output;
        DavaFlowSet<E> initialInput = cloneFlowSet(input);
        String label = getLabel(node);
        if (DEBUG_WHILE) {
            System.out.println("Going into do-while: " + initialInput.toString());
        }
        do {
            lastin = cloneFlowSet(input);
            DavaFlowSet<E> output2 = processSingleSubBodyNode(node, input);
            output = processCondition(node.get_Condition(), handleContinue(label, output2, node));
            input = merge(initialInput, output);
        } while (isDifferent(lastin, input));
        DavaFlowSet<E> temp = handleBreak(label, output, node);
        if (DEBUG_WHILE) {
            System.out.println("Going out of do-while: " + temp.toString());
        }
        return temp;
    }

    public DavaFlowSet<E> processASTUnconditionalLoopNode(ASTUnconditionalLoopNode node, DavaFlowSet<E> input) {
        DavaFlowSet<E> lastin;
        DavaFlowSet<E> output;
        DavaFlowSet<E> initialInput = cloneFlowSet(input);
        if (DEBUG_WHILE) {
            System.out.println("Going into while(true): " + initialInput.toString());
        }
        String label = getLabel(node);
        do {
            lastin = cloneFlowSet(input);
            output = handleContinue(label, processSingleSubBodyNode(node, input), node);
            input = merge(initialInput, output);
        } while (isDifferent(lastin, input));
        DavaFlowSet<E> temp = getMergedBreakList(label, output, node);
        if (DEBUG_WHILE) {
            System.out.println("Going out of while(true): " + temp.toString());
        }
        return temp;
    }

    public DavaFlowSet<E> processASTForLoopNode(ASTForLoopNode node, DavaFlowSet<E> input) {
        DavaFlowSet<E> lastin;
        for (AugmentedStmt as : node.getInit()) {
            Stmt s = as.get_Stmt();
            input = process(s, input);
        }
        DavaFlowSet<E> initialInput = cloneFlowSet(input);
        DavaFlowSet<E> input2 = processCondition(node.get_Condition(), input);
        String label = getLabel(node);
        do {
            lastin = cloneFlowSet(input2);
            DavaFlowSet<E> output1 = processSingleSubBodyNode(node, input2);
            DavaFlowSet<E> output2 = cloneFlowSet(handleContinue(label, output1, node));
            for (AugmentedStmt as2 : node.getUpdate()) {
                Stmt s2 = as2.get_Stmt();
                output2 = process(s2, output2);
            }
            DavaFlowSet<E> input3 = merge(initialInput, output2);
            input2 = processCondition(node.get_Condition(), input3);
        } while (isDifferent(lastin, input2));
        return handleBreak(label, input2, node);
    }

    public DavaFlowSet<E> processASTSwitchNode(ASTSwitchNode node, DavaFlowSet<E> input) {
        DavaFlowSet<E> output;
        if (DEBUG) {
            System.out.println("Going into switch: " + input.toString());
        }
        List<Object> indexList = node.getIndexList();
        Map<Object, List<Object>> index2BodyList = node.getIndex2BodyList();
        DavaFlowSet<E> input2 = processSwitchKey(node.get_Key(), input);
        DavaFlowSet<E> initialIn = cloneFlowSet(input2);
        DavaFlowSet<E> out = null;
        DavaFlowSet<E> defaultOut = null;
        List<DavaFlowSet<E>> toMergeBreaks = new ArrayList<>();
        for (Object currentIndex : indexList) {
            List body = index2BodyList.get(currentIndex);
            if (body != null) {
                out = process(body, input2);
                toMergeBreaks.add(cloneFlowSet(out));
                if (currentIndex instanceof String) {
                    defaultOut = out;
                }
                input2 = merge(out, initialIn);
            }
        }
        if (out != null) {
            if (defaultOut != null) {
                output = merge(defaultOut, out);
            } else {
                output = merge(initialIn, out);
            }
        } else {
            output = initialIn;
        }
        String label = getLabel(node);
        List<DavaFlowSet<E>> outList = new ArrayList<>();
        for (DavaFlowSet<E> mset : toMergeBreaks) {
            outList.add(handleBreak(label, mset, node));
        }
        DavaFlowSet<E> finalOut = output;
        for (DavaFlowSet<E> outIt : outList) {
            finalOut = merge(finalOut, outIt);
        }
        if (DEBUG) {
            System.out.println("Going out of switch: " + finalOut.toString());
        }
        return finalOut;
    }

    public DavaFlowSet<E> processASTTryNode(ASTTryNode node, DavaFlowSet<E> input) {
        if (DEBUG_TRY) {
            System.out.println("TRY START is:" + input);
        }
        List<Object> tryBody = node.get_TryBody();
        DavaFlowSet<E> tryBodyOutput = process(tryBody, input);
        DavaFlowSet<E> inputCatch = newInitialFlow();
        if (DEBUG_TRY) {
            System.out.println("TRY initialFLOW is:" + inputCatch);
        }
        List<Object> catchList = node.get_CatchList();
        Iterator<Object> it = catchList.iterator();
        List<DavaFlowSet<E>> catchOutput = new ArrayList<>();
        while (it.hasNext()) {
            ASTTryNode.container catchBody = (ASTTryNode.container) it.next();
            List<E> body = (List) catchBody.o;
            DavaFlowSet<E> tempResult = process(body, cloneFlowSet(inputCatch));
            catchOutput.add(tempResult);
        }
        String label = getLabel(node);
        List<DavaFlowSet<E>> outList = new ArrayList<>();
        outList.add(handleBreak(label, tryBodyOutput, node));
        for (DavaFlowSet<E> co : catchOutput) {
            DavaFlowSet<E> temp = handleBreak(label, co, node);
            if (DEBUG_TRY) {
                System.out.println("TRY handling breaks is:" + temp);
            }
            outList.add(temp);
        }
        DavaFlowSet<E> out = tryBodyOutput;
        for (DavaFlowSet<E> co2 : outList) {
            out = merge(out, co2);
        }
        if (DEBUG_TRY) {
            System.out.println("TRY after merge outList is:" + out);
        }
        for (DavaFlowSet<E> co3 : catchOutput) {
            out = merge(out, co3);
        }
        if (DEBUG_TRY) {
            System.out.println("TRY END RESULT is:" + out);
        }
        return out;
    }

    public DavaFlowSet<E> merge(DavaFlowSet<E> in1, DavaFlowSet<E> in2) {
        if (this.MERGETYPE == 0) {
            throw new RuntimeException("Use the setMergeType method to set the type of merge used in the analysis");
        }
        if (in1 == this.NOPATH && in2 != this.NOPATH) {
            DavaFlowSet<E> out = in2.mo2534clone();
            out.copyInternalDataFrom(in1);
            return out;
        } else if (in1 != this.NOPATH && in2 == this.NOPATH) {
            DavaFlowSet<E> out2 = in1.mo2534clone();
            out2.copyInternalDataFrom(in2);
            return out2;
        } else if (in1 == this.NOPATH && in2 == this.NOPATH) {
            DavaFlowSet<E> out3 = in1.mo2534clone();
            out3.copyInternalDataFrom(in2);
            return out3;
        } else {
            DavaFlowSet<E> out4 = emptyFlowSet();
            if (this.MERGETYPE == 1) {
                in1.union(in2, out4);
            } else if (this.MERGETYPE == 2) {
                in1.intersection(in2, out4);
            } else {
                throw new RuntimeException("Merge type value" + this.MERGETYPE + " not recognized");
            }
            out4.copyInternalDataFrom(in1);
            out4.copyInternalDataFrom(in2);
            return out4;
        }
    }

    public DavaFlowSet<E> mergeExplicitAndImplicit(String label, DavaFlowSet<E> output, List<DavaFlowSet<E>> explicitSet, List<DavaFlowSet<E>> implicitSet) {
        DavaFlowSet<E> toReturn = output.mo2534clone();
        if (label != null && explicitSet != null && explicitSet.size() != 0) {
            Iterator<DavaFlowSet<E>> it = explicitSet.iterator();
            DavaFlowSet<E> merge = merge(output, it.next());
            while (true) {
                toReturn = merge;
                if (!it.hasNext()) {
                    break;
                }
                merge = merge(toReturn, it.next());
            }
        }
        if (implicitSet != null) {
            for (DavaFlowSet<E> davaFlowSet : implicitSet) {
                toReturn = merge(toReturn, davaFlowSet);
            }
        }
        return toReturn;
    }

    public DavaFlowSet<E> handleBreak(String label, DavaFlowSet<E> out, ASTNode node) {
        List<DavaFlowSet<E>> explicitSet = out.getBreakSet(label);
        if (node == null) {
            throw new RuntimeException("ASTNode sent to handleBreak was null");
        }
        List<DavaFlowSet<E>> implicitSet = out.getImplicitlyBrokenSets(node);
        return mergeExplicitAndImplicit(label, out, explicitSet, implicitSet);
    }

    public DavaFlowSet<E> handleContinue(String label, DavaFlowSet<E> out, ASTNode node) {
        List<DavaFlowSet<E>> explicitSet = out.getContinueSet(label);
        if (node == null) {
            throw new RuntimeException("ASTNode sent to handleContinue was null");
        }
        List<DavaFlowSet<E>> implicitSet = out.getImplicitlyContinuedSets(node);
        return mergeExplicitAndImplicit(label, out, explicitSet, implicitSet);
    }

    private DavaFlowSet<E> getMergedBreakList(String label, DavaFlowSet<E> output, ASTNode node) {
        DavaFlowSet<E> toReturn;
        List<DavaFlowSet<E>> breakSet = output.getBreakSet(label);
        if (breakSet == null) {
            toReturn = this.NOPATH;
        } else if (breakSet.size() == 0) {
            toReturn = this.NOPATH;
        } else {
            Iterator<DavaFlowSet<E>> it = breakSet.iterator();
            DavaFlowSet<E> next = it.next();
            while (true) {
                toReturn = next;
                if (!it.hasNext()) {
                    break;
                }
                next = merge(toReturn, it.next());
            }
        }
        List<DavaFlowSet<E>> implicitSet = output.getImplicitlyBrokenSets(node);
        if (implicitSet != null) {
            Iterator<DavaFlowSet<E>> it2 = implicitSet.iterator();
            if (implicitSet.size() > 0) {
                toReturn = it2.next();
            }
            while (it2.hasNext()) {
                toReturn = merge(toReturn, it2.next());
            }
        }
        return toReturn;
    }

    public boolean isDifferent(DavaFlowSet<E> oldObj, DavaFlowSet<E> newObj) {
        if (oldObj.equals(newObj) && oldObj.internalDataMatchesTo(newObj)) {
            return false;
        }
        return true;
    }

    public DavaFlowSet<E> getBeforeSet(Object beforeThis) {
        return this.beforeSets.get(beforeThis);
    }

    public DavaFlowSet<E> getAfterSet(Object afterThis) {
        return this.afterSets.get(afterThis);
    }

    public void debug(String methodName, String debug) {
        if (DEBUG) {
            System.out.println("Class: StructuredAnalysis MethodName: " + methodName + "    DEBUG: " + debug);
        }
    }

    public void debug(String debug) {
        if (DEBUG) {
            System.out.println("Class: StructuredAnalysis DEBUG: " + debug);
        }
    }
}
