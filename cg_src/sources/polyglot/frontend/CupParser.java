package polyglot.frontend;

import java.io.IOException;
import java_cup.runtime.Symbol;
import java_cup.runtime.lr_parser;
import polyglot.ast.Node;
import polyglot.ast.SourceFile;
import polyglot.util.ErrorQueue;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/CupParser.class */
public class CupParser implements Parser {
    lr_parser grm;
    Source source;
    ErrorQueue eq;

    public CupParser(lr_parser grm, Source source, ErrorQueue eq) {
        this.grm = grm;
        this.source = source;
        this.eq = eq;
    }

    @Override // polyglot.frontend.Parser
    public Node parse() {
        try {
            Symbol sym = this.grm.parse();
            if (sym != null && (sym.value instanceof Node)) {
                SourceFile sf = (SourceFile) sym.value;
                return sf.source(this.source);
            }
            this.eq.enqueue(4, new StringBuffer().append("Unable to parse ").append(this.source.name()).append(".").toString());
            return null;
        } catch (IOException e) {
            this.eq.enqueue(2, e.getMessage());
            return null;
        } catch (RuntimeException e2) {
            throw e2;
        } catch (Exception e3) {
            if (e3.getMessage() != null) {
                this.eq.enqueue(4, e3.getMessage());
                return null;
            }
            return null;
        }
    }

    public String toString() {
        return new StringBuffer().append("CupParser(").append(this.grm.getClass().getName()).append(")").toString();
    }
}
