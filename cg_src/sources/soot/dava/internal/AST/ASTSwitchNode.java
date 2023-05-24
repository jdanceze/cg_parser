package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.coffi.Instruction;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.ASTAnalysis;
import soot.dava.toolkits.base.AST.ASTWalker;
import soot.dava.toolkits.base.AST.TryContentsFinder;
import soot.dava.toolkits.base.AST.analysis.Analysis;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTSwitchNode.class */
public class ASTSwitchNode extends ASTLabeledNode {
    private ValueBox keyBox;
    private List<Object> indexList;
    private Map<Object, List<Object>> index2BodyList;

    public ASTSwitchNode(SETNodeLabel label, Value key, List<Object> indexList, Map<Object, List<Object>> index2BodyList) {
        super(label);
        this.keyBox = Jimple.v().newRValueBox(key);
        this.indexList = indexList;
        this.index2BodyList = index2BodyList;
        for (Object obj : indexList) {
            List body = index2BodyList.get(obj);
            if (body != null) {
                this.subBodies.add(body);
            }
        }
    }

    public List<Object> getIndexList() {
        return this.indexList;
    }

    public Map<Object, List<Object>> getIndex2BodyList() {
        return this.index2BodyList;
    }

    public void replaceIndex2BodyList(Map<Object, List<Object>> index2BodyList) {
        this.index2BodyList = index2BodyList;
        this.subBodies = new ArrayList();
        for (Object obj : this.indexList) {
            List body = index2BodyList.get(obj);
            if (body != null) {
                this.subBodies.add(body);
            }
        }
    }

    public ValueBox getKeyBox() {
        return this.keyBox;
    }

    public Value get_Key() {
        return this.keyBox.getValue();
    }

    public void set_Key(Value key) {
        this.keyBox = Jimple.v().newRValueBox(key);
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new ASTSwitchNode(get_Label(), get_Key(), this.indexList, this.index2BodyList);
    }

    @Override // soot.dava.internal.AST.ASTLabeledNode, soot.dava.internal.AST.ASTNode
    public void perform_Analysis(ASTAnalysis a) {
        ASTWalker.v().walk_value(a, get_Key());
        if (a instanceof TryContentsFinder) {
            TryContentsFinder.v().add_ExceptionSet(this, TryContentsFinder.v().remove_CurExceptionSet());
        }
        perform_AnalysisOnSubBodies(a);
    }

    @Override // soot.dava.internal.AST.ASTNode, soot.Unit
    public void toString(UnitPrinter up) {
        label_toString(up);
        up.literal("switch");
        up.literal(Instruction.argsep);
        up.literal("(");
        this.keyBox.toString(up);
        up.literal(")");
        up.newline();
        up.literal("{");
        up.newline();
        Iterator<Object> it = this.indexList.iterator();
        while (it.hasNext()) {
            Object index = it.next();
            up.incIndent();
            if (index instanceof String) {
                up.literal("default");
            } else {
                up.literal(Jimple.CASE);
                up.literal(Instruction.argsep);
                up.literal(index.toString());
            }
            up.literal(":");
            up.newline();
            List<Object> subBody = this.index2BodyList.get(index);
            if (subBody != null) {
                up.incIndent();
                body_toString(up, subBody);
                if (it.hasNext()) {
                    up.newline();
                }
                up.decIndent();
            }
            up.decIndent();
        }
        up.literal("}");
        up.newline();
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append(label_toString());
        b.append("switch (");
        b.append(get_Key());
        b.append(")");
        b.append("\n");
        b.append("{");
        b.append("\n");
        Iterator<Object> it = this.indexList.iterator();
        while (it.hasNext()) {
            Object index = it.next();
            b.append(ASTNode.TAB);
            if (index instanceof String) {
                b.append("default");
            } else {
                b.append("case ");
                b.append(((Integer) index).toString());
            }
            b.append(":");
            b.append("\n");
            List<Object> subBody = this.index2BodyList.get(index);
            if (subBody != null) {
                b.append(body_toString(subBody));
                if (it.hasNext()) {
                    b.append("\n");
                }
            }
        }
        b.append("}");
        b.append("\n");
        return b.toString();
    }

    @Override // soot.dava.internal.AST.ASTNode
    public void apply(Analysis a) {
        a.caseASTSwitchNode(this);
    }
}
