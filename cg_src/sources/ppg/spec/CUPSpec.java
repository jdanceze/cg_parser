package ppg.spec;

import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;
import ppg.PPGError;
import ppg.atoms.GrammarPart;
import ppg.atoms.Nonterminal;
import ppg.atoms.Precedence;
import ppg.atoms.Production;
import ppg.atoms.SymbolList;
import ppg.util.CodeWriter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/spec/CUPSpec.class */
public class CUPSpec extends Spec {
    private Vector productions;
    private Hashtable ntProds;
    private String start;
    private final int NT_NOT_FOUND = -1;

    public CUPSpec(String pkg, Vector imp, Vector codeParts, Vector syms, Vector precedence, String startSym, Vector prods) {
        this.packageName = pkg;
        this.imports = imp;
        replaceCode(codeParts);
        this.symbols = syms;
        this.prec = precedence;
        this.start = startSym;
        this.productions = prods;
        this.ntProds = new Hashtable();
        hashNonterminals();
    }

    public void setStart(String startSym) {
        if (startSym != null) {
            this.start = startSym;
        }
    }

    private void hashNonterminals() {
        this.ntProds.clear();
        if (this.productions == null) {
            return;
        }
        for (int i = 0; i < this.productions.size(); i++) {
            Production prod = (Production) this.productions.elementAt(i);
            this.ntProds.put(prod.getLHS().getName(), new Integer(i));
        }
    }

    @Override // ppg.spec.Spec
    public CUPSpec coalesce() {
        return this;
    }

    public Production findProduction(Production p) {
        Nonterminal nt = p.getLHS();
        int pos = errorNotFound(findNonterminal(nt), nt);
        Production sourceProd = (Production) this.productions.elementAt(pos);
        Vector sourceRHSList = sourceProd.getRHS();
        Vector rhs = p.getRHS();
        Production result = new Production(nt, new Vector());
        for (int i = 0; i < rhs.size(); i++) {
            Vector toMatch = (Vector) rhs.elementAt(i);
            int j = 0;
            while (true) {
                if (j < sourceRHSList.size()) {
                    Vector source = (Vector) sourceRHSList.elementAt(j);
                    if (!Production.isSameProduction(toMatch, source)) {
                        j++;
                    } else {
                        Vector clone = new Vector();
                        for (int k = 0; k < source.size(); k++) {
                            clone.addElement(((GrammarPart) source.elementAt(k)).clone());
                        }
                        result.addToRHS(clone);
                    }
                }
            }
        }
        return result;
    }

    public void removeEmptyProductions() {
        int i = 0;
        while (i < this.productions.size()) {
            Production prod = (Production) this.productions.elementAt(i);
            if (prod.getRHS().size() == 0) {
                this.productions.removeElementAt(i);
                i--;
            }
            i++;
        }
    }

    public Object clone() {
        String newPkgName = this.packageName == null ? null : this.packageName.toString();
        Vector newImports = new Vector();
        for (int i = 0; i < this.imports.size(); i++) {
            newImports.addElement(((String) this.imports.elementAt(i)).toString());
        }
        Vector newCode = new Vector();
        if (this.actionCode != null) {
            newCode.addElement(this.actionCode);
        }
        if (this.initCode != null) {
            newCode.addElement(this.initCode);
        }
        if (this.parserCode != null) {
            newCode.addElement(this.parserCode);
        }
        if (this.scanCode != null) {
            newCode.addElement(this.scanCode);
        }
        Vector newSymbols = new Vector();
        for (int i2 = 0; i2 < this.symbols.size(); i2++) {
            newSymbols.addElement(((SymbolList) this.symbols.elementAt(i2)).clone());
        }
        Vector newPrec = new Vector();
        for (int i3 = 0; i3 < this.prec.size(); i3++) {
            newPrec.addElement(((Precedence) this.prec.elementAt(i3)).clone());
        }
        String newStart = this.start == null ? null : this.start.toString();
        Vector newProductions = new Vector();
        for (int i4 = 0; i4 < this.productions.size(); i4++) {
            newProductions.addElement(((Production) this.productions.elementAt(i4)).clone());
        }
        return new CUPSpec(newPkgName, newImports, newCode, newSymbols, newPrec, newStart, newProductions);
    }

    public void addSymbols(Vector syms) {
        if (syms == null) {
            return;
        }
        for (int i = 0; i < syms.size(); i++) {
            this.symbols.addElement(syms.elementAt(i));
        }
    }

    public void dropSymbol(String gs) throws PPGError {
        boolean dropped = false;
        for (int i = 0; i < this.symbols.size(); i++) {
            SymbolList list = (SymbolList) this.symbols.elementAt(i);
            dropped = dropped || list.dropSymbol(gs);
        }
    }

    public void dropProductions(Production p) {
        Nonterminal nt = p.getLHS();
        int pos = errorNotFound(findNonterminal(nt), nt);
        Production prod = (Production) this.productions.elementAt(pos);
        prod.drop(p);
    }

    public void dropProductions(Nonterminal nt) {
        int pos = errorNotFound(findNonterminal(nt), nt);
        Production prod = (Production) this.productions.elementAt(pos);
        prod.drop((Production) prod.clone());
    }

    public void dropAllProductions(String nt) {
        int pos = findNonterminal(nt);
        if (pos == -1) {
            return;
        }
        this.productions.removeElementAt(pos);
        hashNonterminals();
    }

    public void addProductions(Production p) {
        Nonterminal nt = p.getLHS();
        int pos = findNonterminal(nt);
        if (pos == -1) {
            this.ntProds.put(nt.getName(), new Integer(this.productions.size()));
            this.productions.addElement(p);
            return;
        }
        Production prod = (Production) this.productions.elementAt(pos);
        prod.add(p);
    }

    private int findNonterminal(Nonterminal nt) {
        return findNonterminal(nt.getName());
    }

    private int findNonterminal(String nt) {
        Integer pos = (Integer) this.ntProds.get(nt);
        if (pos == null) {
            return -1;
        }
        return pos.intValue();
    }

    private int errorNotFound(int i, Nonterminal nt) {
        if (i == -1) {
            System.err.println(new StringBuffer().append("ppg: nonterminal ").append(nt).append(" not found.").toString());
            System.exit(1);
        }
        return i;
    }

    @Override // ppg.parse.Unparse
    public void unparse(CodeWriter cw) {
        cw.begin(0);
        if (this.packageName != null) {
            cw.write(new StringBuffer().append("package ").append(this.packageName).append(";").toString());
            cw.newline();
            cw.newline();
        }
        for (int i = 0; i < this.imports.size(); i++) {
            cw.write(new StringBuffer().append("import ").append((String) this.imports.elementAt(i)).append(";").toString());
            cw.newline();
        }
        if (this.imports.size() > 0) {
            cw.newline();
        }
        if (this.actionCode != null) {
            cw.write(this.actionCode.toString());
        }
        if (this.initCode != null) {
            cw.write(this.initCode.toString());
        }
        if (this.parserCode != null) {
            cw.write(this.parserCode.toString());
        }
        if (this.scanCode != null) {
            cw.write(this.scanCode.toString());
        }
        cw.newline();
        for (int i2 = 0; i2 < this.symbols.size(); i2++) {
            cw.write(((SymbolList) this.symbols.elementAt(i2)).toString());
            cw.newline();
        }
        cw.newline();
        for (int i3 = 0; i3 < this.prec.size(); i3++) {
            cw.write(((Precedence) this.prec.elementAt(i3)).toString());
            cw.newline();
        }
        cw.newline();
        if (this.start != null) {
            cw.write(new StringBuffer().append("start with ").append(this.start).append(";").toString());
            cw.newline();
            cw.newline();
        }
        for (int i4 = 0; i4 < this.productions.size(); i4++) {
            ((Production) this.productions.elementAt(i4)).unparse(cw);
        }
        cw.newline();
        cw.end();
    }

    public void export(PrintStream out) throws Exception {
        out.println(new StringBuffer().append("package ").append(this.packageName).append(";").toString());
        out.println();
        for (int i = 0; i < this.imports.size(); i++) {
            out.println(new StringBuffer().append("import ").append((String) this.imports.elementAt(i)).append(";").toString());
        }
        out.println();
        if (this.actionCode != null) {
            out.println(this.actionCode.toString());
        }
        if (this.initCode != null) {
            out.println(this.initCode.toString());
        }
        if (this.parserCode != null) {
            out.println(this.parserCode.toString());
        }
        if (this.scanCode != null) {
            out.println(this.scanCode.toString());
        }
        out.println();
        for (int i2 = 0; i2 < this.symbols.size(); i2++) {
            out.println(((SymbolList) this.symbols.elementAt(i2)).toString());
        }
        out.println();
        for (int i3 = 0; i3 < this.prec.size(); i3++) {
            out.println(((Precedence) this.prec.elementAt(i3)).toString());
        }
        out.println();
        out.println(new StringBuffer().append("start with ").append(this.start).append(";").toString());
        out.println();
        for (int i4 = 0; i4 < this.productions.size(); i4++) {
            out.println(((Production) this.productions.elementAt(i4)).toString());
        }
        out.println();
        out.flush();
        out.close();
    }
}
