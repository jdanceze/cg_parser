package soot.jimple.parser.node;

import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/Token.class */
public abstract class Token extends Node {
    private String text;
    private int line;
    private int pos;

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLine() {
        return this.line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getPos() {
        return this.pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String toString() {
        return String.valueOf(this.text) + Instruction.argsep;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        throw new RuntimeException("Not a child.");
    }
}
