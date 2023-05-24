package soot.toolkits.astmetrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import polyglot.ast.Block;
import polyglot.ast.Branch;
import polyglot.ast.CodeDecl;
import polyglot.ast.Expr;
import polyglot.ast.Formal;
import polyglot.ast.If;
import polyglot.ast.Initializer;
import polyglot.ast.Labeled;
import polyglot.ast.LocalClassDecl;
import polyglot.ast.Loop;
import polyglot.ast.Node;
import polyglot.ast.ProcedureDecl;
import polyglot.ast.Stmt;
import polyglot.ast.Switch;
import polyglot.ast.Synchronized;
import polyglot.ast.Try;
import polyglot.util.CodeWriter;
import polyglot.visit.NodeVisitor;
/* loaded from: gencallgraphv3.jar:soot/toolkits/astmetrics/StmtSumWeightedByDepth.class */
public class StmtSumWeightedByDepth extends ASTMetric {
    int currentDepth;
    int sum;
    int maxDepth;
    int numNodes;
    Stack<ArrayList> labelNodesSoFar;
    ArrayList<Node> blocksWithAbruptFlow;
    HashMap<Node, Integer> stmtToMetric;
    HashMap<Node, Integer> stmtToMetricDepth;
    public static boolean tmpAbruptChecker = false;

    public StmtSumWeightedByDepth(Node node) {
        super(node);
        this.labelNodesSoFar = new Stack<>();
        this.blocksWithAbruptFlow = new ArrayList<>();
        this.stmtToMetric = new HashMap<>();
        this.stmtToMetricDepth = new HashMap<>();
    }

    @Override // soot.toolkits.astmetrics.ASTMetric
    public void printAstMetric(Node n, CodeWriter w) {
        if ((n instanceof Stmt) && this.stmtToMetric.containsKey(n)) {
            w.write(" // sum= " + this.stmtToMetric.get(n) + " : depth= " + this.stmtToMetricDepth.get(n) + "\t");
        }
    }

    @Override // soot.toolkits.astmetrics.ASTMetric
    public void reset() {
        this.currentDepth = 1;
        this.maxDepth = 1;
        this.sum = 0;
        this.numNodes = 0;
    }

    @Override // soot.toolkits.astmetrics.ASTMetric
    public void addMetrics(ClassData data) {
        data.addMetric(new MetricData("D-W-Complexity", new Double(this.sum)));
        data.addMetric(new MetricData("AST-Node-Count", new Integer(this.numNodes)));
    }

    private void increaseDepth() {
        System.out.println("Increasing depth");
        this.currentDepth++;
        if (this.currentDepth > this.maxDepth) {
            this.maxDepth = this.currentDepth;
        }
    }

    private void decreaseDepth() {
        System.out.println("Decreasing depth");
        this.currentDepth--;
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node parent, Node n) {
        this.numNodes++;
        if (n instanceof CodeDecl) {
            this.labelNodesSoFar.push(new ArrayList());
        } else if (n instanceof Labeled) {
            this.labelNodesSoFar.peek().add(((Labeled) n).label());
        }
        if ((n instanceof If) || (n instanceof Loop) || (n instanceof Try) || (n instanceof Switch) || (n instanceof LocalClassDecl) || (n instanceof Synchronized) || (n instanceof ProcedureDecl) || (n instanceof Initializer)) {
            this.sum += this.currentDepth * 2;
            System.out.println(n);
            increaseDepth();
        } else if ((parent instanceof Block) && (n instanceof Block)) {
            tmpAbruptChecker = false;
            n.visit(new NodeVisitor() { // from class: soot.toolkits.astmetrics.StmtSumWeightedByDepth.1
                @Override // polyglot.visit.NodeVisitor
                public NodeVisitor enter(Node parent2, Node node) {
                    if (node instanceof Branch) {
                        Branch b = (Branch) node;
                        if (b.label() != null && StmtSumWeightedByDepth.this.labelNodesSoFar.peek().contains(b.label())) {
                            StmtSumWeightedByDepth.tmpAbruptChecker = true;
                        }
                    }
                    return enter(node);
                }

                @Override // polyglot.visit.NodeVisitor
                public Node override(Node parent2, Node node) {
                    if (StmtSumWeightedByDepth.tmpAbruptChecker) {
                        return node;
                    }
                    return null;
                }
            });
            if (tmpAbruptChecker) {
                this.blocksWithAbruptFlow.add(n);
                this.sum += this.currentDepth * 2;
                System.out.println(n);
                increaseDepth();
            }
        } else if ((n instanceof Expr) || (n instanceof Formal)) {
            System.out.print(String.valueOf(this.sum) + "  " + n + "  ");
            this.sum += this.currentDepth * 2;
            System.out.println(this.sum);
        }
        if (n instanceof Stmt) {
            this.stmtToMetric.put(n, new Integer(this.sum));
            this.stmtToMetricDepth.put(n, new Integer(this.currentDepth));
        }
        return enter(n);
    }

    @Override // polyglot.visit.NodeVisitor
    public Node leave(Node old, Node n, NodeVisitor v) {
        if (n instanceof CodeDecl) {
            this.labelNodesSoFar.pop();
        }
        if ((n instanceof If) || (n instanceof Loop) || (n instanceof Try) || (n instanceof Switch) || (n instanceof LocalClassDecl) || (n instanceof Synchronized) || (n instanceof ProcedureDecl) || (n instanceof Initializer)) {
            decreaseDepth();
        } else if ((n instanceof Block) && this.blocksWithAbruptFlow.contains(n)) {
            decreaseDepth();
        }
        return n;
    }
}
