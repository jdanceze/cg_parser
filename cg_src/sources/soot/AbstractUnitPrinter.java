package soot;

import java.util.HashSet;
import soot.dava.internal.AST.ASTNode;
import soot.jimple.Constant;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/AbstractUnitPrinter.class */
public abstract class AbstractUnitPrinter implements UnitPrinter {
    protected boolean startOfLine = true;
    protected String indent = "        ";
    protected StringBuffer output = new StringBuffer();
    protected AttributesUnitPrinter pt;
    protected HashSet<String> quotableLocals;

    @Override // soot.UnitPrinter
    public void setPositionTagger(AttributesUnitPrinter pt) {
        this.pt = pt;
        pt.setUnitPrinter(this);
    }

    @Override // soot.UnitPrinter
    public AttributesUnitPrinter getPositionTagger() {
        return this.pt;
    }

    @Override // soot.UnitPrinter
    public void startUnit(Unit u) {
        handleIndent();
        if (this.pt != null) {
            this.pt.startUnit(u);
        }
    }

    @Override // soot.UnitPrinter
    public void endUnit(Unit u) {
        if (this.pt != null) {
            this.pt.endUnit(u);
        }
    }

    @Override // soot.UnitPrinter
    public void startUnitBox(UnitBox ub) {
        handleIndent();
    }

    @Override // soot.UnitPrinter
    public void endUnitBox(UnitBox ub) {
    }

    @Override // soot.UnitPrinter
    public void startValueBox(ValueBox vb) {
        handleIndent();
        if (this.pt != null) {
            this.pt.startValueBox(vb);
        }
    }

    @Override // soot.UnitPrinter
    public void endValueBox(ValueBox vb) {
        if (this.pt != null) {
            this.pt.endValueBox(vb);
        }
    }

    @Override // soot.UnitPrinter
    public void noIndent() {
        this.startOfLine = false;
    }

    @Override // soot.UnitPrinter
    public void incIndent() {
        this.indent = String.valueOf(this.indent) + ASTNode.TAB;
    }

    @Override // soot.UnitPrinter
    public void decIndent() {
        if (this.indent.length() >= 4) {
            this.indent = this.indent.substring(4);
        }
    }

    @Override // soot.UnitPrinter
    public void setIndent(String indent) {
        this.indent = indent;
    }

    @Override // soot.UnitPrinter
    public String getIndent() {
        return this.indent;
    }

    @Override // soot.UnitPrinter
    public void newline() {
        this.output.append('\n');
        this.startOfLine = true;
        if (this.pt != null) {
            this.pt.newline();
        }
    }

    @Override // soot.UnitPrinter
    public void local(Local l) {
        handleIndent();
        if (this.quotableLocals == null) {
            initializeQuotableLocals();
        }
        String name = l.getName();
        if (this.quotableLocals.contains(name)) {
            this.output.append('\'').append(name).append('\'');
        } else {
            this.output.append(name);
        }
    }

    @Override // soot.UnitPrinter
    public void constant(Constant c) {
        handleIndent();
        this.output.append(c.toString());
    }

    public String toString() {
        String ret = this.output.toString();
        this.output = new StringBuffer();
        return ret;
    }

    @Override // soot.UnitPrinter
    public StringBuffer output() {
        return this.output;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleIndent() {
        if (this.startOfLine) {
            this.output.append(this.indent);
        }
        this.startOfLine = false;
    }

    protected void initializeQuotableLocals() {
        this.quotableLocals = new HashSet<>(Jimple.jimpleKeywordList());
    }
}
