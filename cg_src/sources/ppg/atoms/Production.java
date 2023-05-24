package ppg.atoms;

import java.util.Vector;
import ppg.parse.Unparse;
import ppg.util.CodeWriter;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/atoms/Production.class */
public class Production implements Unparse {
    private Nonterminal lhs;
    private Vector rhs;
    private static String HEADER = "ppg [nterm]: ";

    public Production(Nonterminal lhs, Vector rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Nonterminal getLHS() {
        return this.lhs;
    }

    public void setLHS(Nonterminal nt) {
        this.lhs = nt;
    }

    public Vector getRHS() {
        return this.rhs;
    }

    public Object clone() {
        return new Production((Nonterminal) this.lhs.clone(), (Vector) this.rhs.clone());
    }

    public void drop(Production prod) {
        Vector toDrop = prod.getRHS();
        for (int i = 0; i < toDrop.size(); i++) {
            Vector target = (Vector) toDrop.elementAt(i);
            int j = 0;
            while (true) {
                if (j < this.rhs.size()) {
                    Vector source = (Vector) this.rhs.elementAt(j);
                    if (isSameProduction(target, source)) {
                        this.rhs.removeElementAt(j);
                        break;
                    }
                    if (j == this.rhs.size() - 1) {
                        System.err.println(new StringBuffer().append(HEADER).append("no match found for production:").toString());
                        System.err.print(new StringBuffer().append(prod.getLHS()).append(" ::= ").toString());
                        for (int k = 0; k < target.size(); k++) {
                            System.err.print(new StringBuffer().append(target.elementAt(k)).append(Instruction.argsep).toString());
                        }
                        System.exit(1);
                    }
                    j++;
                }
            }
        }
    }

    public static boolean isSameProduction(Vector u, Vector v) {
        int uIdx = 0;
        int vIdx = 0;
        while (uIdx < u.size() && vIdx < v.size()) {
            GrammarPart ug = (GrammarPart) u.elementAt(uIdx);
            if (ug instanceof SemanticAction) {
                uIdx++;
            } else {
                GrammarPart vg = (GrammarPart) v.elementAt(vIdx);
                if (vg instanceof SemanticAction) {
                    vIdx++;
                } else if (!ug.equals(vg)) {
                    return false;
                } else {
                    uIdx++;
                    vIdx++;
                }
            }
        }
        if (uIdx == u.size() && vIdx == v.size()) {
            return true;
        }
        if (uIdx < u.size()) {
            while (uIdx < u.size()) {
                if (((GrammarPart) u.elementAt(uIdx)) instanceof SemanticAction) {
                    uIdx++;
                } else {
                    return false;
                }
            }
            return true;
        }
        while (vIdx < v.size()) {
            if (((GrammarPart) v.elementAt(vIdx)) instanceof SemanticAction) {
                vIdx++;
            } else {
                return false;
            }
        }
        return true;
    }

    public void union(Production prod) {
        Vector additional = prod.getRHS();
        union(additional);
    }

    public void union(Vector prodList) {
        for (int i = 0; i < prodList.size(); i++) {
            Vector toAdd = (Vector) prodList.elementAt(i);
            for (int j = 0; j < this.rhs.size(); j++) {
                Vector current = (Vector) this.rhs.elementAt(i);
                if (isSameProduction(toAdd, current)) {
                    break;
                }
                if (j == this.rhs.size() - 1) {
                    this.rhs.addElement(toAdd);
                }
            }
        }
    }

    public void add(Production prod) {
        Vector additional = prod.getRHS();
        for (int i = 0; i < additional.size(); i++) {
            this.rhs.addElement(additional.elementAt(i));
        }
    }

    public void addToRHS(Vector rhsPart) {
        this.rhs.addElement(rhsPart);
    }

    private void assertSameLHS(Production prod, String function) {
        if (!prod.getLHS().equals(this.lhs)) {
            System.err.println(new StringBuffer().append(HEADER).append("nonterminals do not match in Production.").append(function).append("(): current is ").append(this.lhs).append(", given: ").append(prod.getLHS()).toString());
            System.exit(1);
        }
    }

    @Override // ppg.parse.Unparse
    public void unparse(CodeWriter cw) {
        cw.begin(0);
        cw.write(new StringBuffer().append(this.lhs.toString()).append(" ::=").toString());
        cw.allowBreak(3);
        for (int i = 0; i < this.rhs.size(); i++) {
            Vector rhs_part = (Vector) this.rhs.elementAt(i);
            for (int j = 0; j < rhs_part.size(); j++) {
                cw.write(Instruction.argsep);
                ((GrammarPart) rhs_part.elementAt(j)).unparse(cw);
            }
            if (i < this.rhs.size() - 1) {
                cw.allowBreak(0);
                cw.write(" | ");
            }
        }
        cw.write(";");
        cw.newline();
        cw.newline();
        cw.end();
    }

    public String toString() {
        String result = this.lhs.toString();
        String result2 = new StringBuffer().append(result).append(" ::=").toString();
        for (int i = 0; i < this.rhs.size(); i++) {
            Vector rhs_part = (Vector) this.rhs.elementAt(i);
            for (int j = 0; j < rhs_part.size(); j++) {
                result2 = new StringBuffer().append(result2).append(Instruction.argsep).append(rhs_part.elementAt(j).toString()).toString();
            }
            if (i < this.rhs.size() - 1) {
                result2 = new StringBuffer().append(result2).append(" | ").toString();
            }
        }
        return new StringBuffer().append(result2).append(";").toString();
    }
}
