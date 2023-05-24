package soot.dava.toolkits.base.AST.transformations;

import soot.BooleanType;
import soot.Value;
import soot.dava.internal.AST.ASTAggregatedCondition;
import soot.dava.internal.AST.ASTAndCondition;
import soot.dava.internal.AST.ASTBinaryCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTControlFlowNode;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTOrCondition;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.javaRep.DIntConstant;
import soot.dava.internal.javaRep.DNotExpr;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.ConditionExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/SimplifyConditions.class */
public class SimplifyConditions extends DepthFirstAdapter {
    public static boolean DEBUG = false;
    public boolean changed;

    public SimplifyConditions() {
        this.changed = false;
    }

    public SimplifyConditions(boolean verbose) {
        super(verbose);
        this.changed = false;
    }

    public void fixedPoint(ASTControlFlowNode node) {
        do {
            if (DEBUG) {
                System.out.println("Invoking simplify");
            }
            this.changed = false;
            ASTCondition cond = node.get_Condition();
            ASTCondition returned = simplifyTheCondition(cond);
            if (returned != null) {
                node.set_Condition(returned);
            }
        } while (this.changed);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTIfNode(ASTIfNode node) {
        fixedPoint(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTIfElseNode(ASTIfElseNode node) {
        fixedPoint(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTWhileNode(ASTWhileNode node) {
        fixedPoint(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTDoWhileNode(ASTDoWhileNode node) {
        fixedPoint(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTForLoopNode(ASTForLoopNode node) {
        fixedPoint(node);
    }

    public ASTCondition applyDeMorgans(ASTAggregatedCondition aggCond) {
        ASTAggregatedCondition newCond;
        ASTAggregatedCondition aggCond2;
        ASTCondition left = aggCond.getLeftOp();
        ASTCondition right = aggCond.getRightOp();
        if (aggCond.isNotted() && (left instanceof ASTBinaryCondition) && (right instanceof ASTBinaryCondition)) {
            left.flip();
            right.flip();
            if (aggCond instanceof ASTAndCondition) {
                aggCond2 = new ASTOrCondition(left, right);
            } else {
                aggCond2 = new ASTAndCondition(left, right);
            }
            return aggCond2;
        } else if ((left.isNotted() && right.isNotted() && !(left instanceof ASTBinaryCondition) && !(right instanceof ASTBinaryCondition)) || ((left.isNotted() && aggCond.isNotted() && !(left instanceof ASTBinaryCondition)) || (right.isNotted() && aggCond.isNotted() && !(right instanceof ASTBinaryCondition)))) {
            left.flip();
            right.flip();
            if (aggCond instanceof ASTAndCondition) {
                newCond = new ASTOrCondition(left, right);
            } else {
                newCond = new ASTAndCondition(left, right);
            }
            if (aggCond.isNotted()) {
                return newCond;
            }
            newCond.flip();
            return newCond;
        } else {
            return null;
        }
    }

    public ASTCondition simplifyIfAtleastOneConstant(ASTAggregatedCondition aggCond) {
        ASTCondition left = aggCond.getLeftOp();
        ASTCondition right = aggCond.getRightOp();
        Boolean leftBool = null;
        Boolean rightBool = null;
        if (left instanceof ASTUnaryCondition) {
            leftBool = isBooleanConstant(((ASTUnaryCondition) left).getValue());
        }
        if (right instanceof ASTUnaryCondition) {
            rightBool = isBooleanConstant(((ASTUnaryCondition) right).getValue());
        }
        if (leftBool == null && rightBool == null) {
            return null;
        }
        if (aggCond instanceof ASTAndCondition) {
            if (leftBool != null && rightBool != null) {
                if (leftBool.booleanValue() && rightBool.booleanValue()) {
                    return new ASTUnaryCondition(DIntConstant.v(1, BooleanType.v()));
                }
                return new ASTUnaryCondition(DIntConstant.v(0, BooleanType.v()));
            } else if (leftBool != null) {
                if (leftBool.booleanValue()) {
                    return right;
                }
                return new ASTUnaryCondition(DIntConstant.v(0, BooleanType.v()));
            } else if (rightBool != null) {
                if (rightBool.booleanValue()) {
                    return left;
                }
                return aggCond;
            } else {
                return null;
            }
        } else if (aggCond instanceof ASTOrCondition) {
            if (leftBool != null && rightBool != null) {
                if (!leftBool.booleanValue() && !rightBool.booleanValue()) {
                    return new ASTUnaryCondition(DIntConstant.v(0, BooleanType.v()));
                }
                return new ASTUnaryCondition(DIntConstant.v(1, BooleanType.v()));
            } else if (leftBool != null) {
                if (leftBool.booleanValue()) {
                    return new ASTUnaryCondition(DIntConstant.v(1, BooleanType.v()));
                }
                return right;
            } else if (rightBool != null) {
                if (rightBool.booleanValue()) {
                    return aggCond;
                }
                return left;
            } else {
                return null;
            }
        } else {
            throw new RuntimeException("Found unknown aggregated condition");
        }
    }

    public Boolean isBooleanConstant(Value internal) {
        if (!(internal instanceof DIntConstant)) {
            return null;
        }
        if (DEBUG) {
            System.out.println("Found Constant");
        }
        DIntConstant intConst = (DIntConstant) internal;
        if (!(intConst.type instanceof BooleanType)) {
            return null;
        }
        if (DEBUG) {
            System.out.println("Found Boolean Constant");
        }
        if (intConst.value == 1) {
            return new Boolean(true);
        }
        if (intConst.value == 0) {
            return new Boolean(false);
        }
        throw new RuntimeException("BooleanType found with value different than 0 or 1");
    }

    public ASTCondition simplifyTheCondition(ASTCondition cond) {
        if (cond instanceof ASTAggregatedCondition) {
            ASTAggregatedCondition aggCond = (ASTAggregatedCondition) cond;
            ASTCondition leftCond = simplifyTheCondition(aggCond.getLeftOp());
            ASTCondition rightCond = simplifyTheCondition(aggCond.getRightOp());
            if (leftCond != null) {
                aggCond.setLeftOp(leftCond);
            }
            if (rightCond != null) {
                aggCond.setRightOp(rightCond);
            }
            ASTCondition returned = simplifyIfAtleastOneConstant(aggCond);
            if (returned != null) {
                this.changed = true;
                return returned;
            }
            ASTCondition returned2 = applyDeMorgans(aggCond);
            if (returned2 != null) {
                this.changed = true;
                return returned2;
            }
            return aggCond;
        } else if (cond instanceof ASTUnaryCondition) {
            ASTUnaryCondition unary = (ASTUnaryCondition) cond;
            Value unaryVal = unary.getValue();
            if (unaryVal instanceof DNotExpr) {
                if (DEBUG) {
                    System.out.println("Found NotExpr in unary COndition" + unaryVal);
                }
                DNotExpr notted = (DNotExpr) unaryVal;
                Value internal = notted.getOp();
                Boolean isIt = isBooleanConstant(internal);
                if (isIt != null) {
                    if (isIt.booleanValue()) {
                        if (DEBUG) {
                            System.out.println("CONVERTED !true to false");
                        }
                        this.changed = true;
                        return new ASTUnaryCondition(DIntConstant.v(0, BooleanType.v()));
                    } else if (!isIt.booleanValue()) {
                        if (DEBUG) {
                            System.out.println("CONVERTED !false to true");
                        }
                        this.changed = true;
                        return new ASTUnaryCondition(DIntConstant.v(1, BooleanType.v()));
                    } else {
                        throw new RuntimeException("BooleanType found with value different than 0 or 1");
                    }
                } else if (DEBUG) {
                    System.out.println("Not boolean type");
                }
            }
            return unary;
        } else if (cond instanceof ASTBinaryCondition) {
            ASTBinaryCondition binary = (ASTBinaryCondition) cond;
            ConditionExpr expr = binary.getConditionExpr();
            ASTUnaryCondition temp = evaluateBinaryCondition(expr);
            if (DEBUG) {
                System.out.println("changed binary condition " + cond + " to" + temp);
            }
            if (temp != null) {
                this.changed = true;
            }
            return temp;
        } else {
            throw new RuntimeException("Method getUseList in ASTUsesAndDefs encountered unknown condition type");
        }
    }

    public ASTUnaryCondition evaluateBinaryCondition(ConditionExpr expr) {
        String symbol = expr.getSymbol();
        int op = -1;
        if (symbol.indexOf("==") > -1) {
            if (DEBUG) {
                System.out.println("==");
            }
            op = 1;
        } else if (symbol.indexOf(">=") > -1) {
            if (DEBUG) {
                System.out.println(">=");
            }
            op = 2;
        } else if (symbol.indexOf(62) > -1) {
            if (DEBUG) {
                System.out.println(">");
            }
            op = 3;
        } else if (symbol.indexOf("<=") > -1) {
            if (DEBUG) {
                System.out.println("<=");
            }
            op = 4;
        } else if (symbol.indexOf(60) > -1) {
            if (DEBUG) {
                System.out.println("<");
            }
            op = 5;
        } else if (symbol.indexOf("!=") > -1) {
            if (DEBUG) {
                System.out.println("!=");
            }
            op = 6;
        }
        Value leftOp = expr.getOp1();
        Value rightOp = expr.getOp2();
        Boolean result = null;
        if ((leftOp instanceof LongConstant) && (rightOp instanceof LongConstant)) {
            if (DEBUG) {
                System.out.println("long constants!!");
            }
            long left = ((LongConstant) leftOp).value;
            long right = ((LongConstant) rightOp).value;
            result = longSwitch(op, left, right);
        } else if ((leftOp instanceof DoubleConstant) && (rightOp instanceof DoubleConstant)) {
            double left2 = ((DoubleConstant) leftOp).value;
            double right2 = ((DoubleConstant) rightOp).value;
            result = doubleSwitch(op, left2, right2);
        } else if ((leftOp instanceof FloatConstant) && (rightOp instanceof FloatConstant)) {
            float left3 = ((FloatConstant) leftOp).value;
            float right3 = ((FloatConstant) rightOp).value;
            result = floatSwitch(op, left3, right3);
        } else if ((leftOp instanceof IntConstant) && (rightOp instanceof IntConstant)) {
            int left4 = ((IntConstant) leftOp).value;
            int right4 = ((IntConstant) rightOp).value;
            result = intSwitch(op, left4, right4);
        }
        if (result != null) {
            if (result.booleanValue()) {
                return new ASTUnaryCondition(DIntConstant.v(1, BooleanType.v()));
            }
            return new ASTUnaryCondition(DIntConstant.v(0, BooleanType.v()));
        }
        return null;
    }

    public Boolean longSwitch(int op, long l, long r) {
        switch (op) {
            case 1:
                if (l == r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 2:
                if (l >= r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 3:
                if (l > r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 4:
                if (l <= r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 5:
                if (l < r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 6:
                if (l != r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            default:
                if (DEBUG) {
                    System.out.println("got here");
                    return null;
                }
                return null;
        }
    }

    public Boolean doubleSwitch(int op, double l, double r) {
        switch (op) {
            case 1:
                if (l == r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 2:
                if (l >= r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 3:
                if (l > r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 4:
                if (l <= r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 5:
                if (l < r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 6:
                if (l != r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            default:
                return null;
        }
    }

    public Boolean floatSwitch(int op, float l, float r) {
        switch (op) {
            case 1:
                if (l == r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 2:
                if (l >= r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 3:
                if (l > r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 4:
                if (l <= r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 5:
                if (l < r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 6:
                if (l != r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            default:
                return null;
        }
    }

    public Boolean intSwitch(int op, int l, int r) {
        switch (op) {
            case 1:
                if (l == r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 2:
                if (l >= r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 3:
                if (l > r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 4:
                if (l <= r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 5:
                if (l < r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            case 6:
                if (l != r) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            default:
                return null;
        }
    }
}
