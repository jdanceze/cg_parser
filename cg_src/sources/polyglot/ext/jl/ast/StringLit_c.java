package polyglot.ext.jl.ast;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import polyglot.ast.Node;
import polyglot.ast.StringLit;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.StringUtil;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/StringLit_c.class */
public class StringLit_c extends Lit_c implements StringLit {
    protected String value;
    protected int MAX_LENGTH;

    public StringLit_c(Position pos, String value) {
        super(pos);
        this.MAX_LENGTH = 60;
        this.value = value;
    }

    @Override // polyglot.ast.StringLit
    public String value() {
        return this.value;
    }

    @Override // polyglot.ast.StringLit
    public StringLit value(String value) {
        StringLit_c n = (StringLit_c) copy();
        n.value = value;
        return n;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        return type(tc.typeSystem().String());
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        if (StringUtil.unicodeEscape(this.value).length() > 11) {
            return new StringBuffer().append("\"").append(StringUtil.unicodeEscape(this.value).substring(0, 8)).append("...\"").toString();
        }
        return new StringBuffer().append("\"").append(StringUtil.unicodeEscape(this.value)).append("\"").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        List<String> l = breakupString();
        if (l.size() > 1) {
            w.write("(");
        }
        for (String str : l) {
            w.begin(0);
        }
        Iterator i = l.iterator();
        while (i.hasNext()) {
            String s = (String) i.next();
            w.write("\"");
            w.write(StringUtil.escape(s));
            w.write("\"");
            w.end();
            if (i.hasNext()) {
                w.write(" +");
                w.allowBreak(0, Instruction.argsep);
            }
        }
        if (l.size() > 1) {
            w.write(")");
        }
    }

    public List breakupString() {
        List result = new LinkedList();
        int n = this.value.length();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= n) {
                break;
            }
            int len = 0;
            int j = i2;
            while (j < n) {
                char c = this.value.charAt(j);
                int k = StringUtil.unicodeEscape(c).length();
                if (len + k > this.MAX_LENGTH) {
                    break;
                }
                len += k;
                j++;
            }
            result.add(this.value.substring(i2, j));
            i = j;
        }
        if (result.isEmpty()) {
            if (!this.value.equals("")) {
                throw new InternalCompilerError("breakupString failed");
            }
            result.add(this.value);
        }
        return result;
    }

    @Override // polyglot.ext.jl.ast.Lit_c, polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Object constantValue() {
        return this.value;
    }
}
