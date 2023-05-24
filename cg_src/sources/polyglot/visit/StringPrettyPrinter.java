package polyglot.visit;

import java.io.CharArrayWriter;
import java.io.IOException;
import polyglot.ast.Node;
import polyglot.util.CodeWriter;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/StringPrettyPrinter.class */
public class StringPrettyPrinter extends PrettyPrinter {
    int maxdepth;
    int depth = 0;

    public StringPrettyPrinter(int maxdepth) {
        this.maxdepth = maxdepth;
    }

    @Override // polyglot.visit.PrettyPrinter
    public void print(Node parent, Node child, CodeWriter w) {
        this.depth++;
        if (this.depth < this.maxdepth) {
            super.print(parent, child, w);
        } else {
            w.write("...");
        }
        this.depth--;
    }

    public String toString(Node ast) {
        StringCodeWriter w = new StringCodeWriter(new CharArrayWriter());
        print(null, ast, w);
        try {
            w.flush();
        } catch (IOException e) {
        }
        return w.toString();
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/StringPrettyPrinter$StringCodeWriter.class */
    public static class StringCodeWriter extends CodeWriter {
        CharArrayWriter w;

        public StringCodeWriter(CharArrayWriter w) {
            super(w, 1000);
            this.w = w;
        }

        @Override // polyglot.util.CodeWriter
        public void write(String s) {
            StringBuffer sb = new StringBuffer();
            char last = 0;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (!Character.isSpaceChar(c) || !Character.isSpaceChar(last)) {
                    sb.append(c);
                    last = c;
                }
            }
            super.write(sb.toString());
        }

        @Override // polyglot.util.CodeWriter
        public void newline(int n) {
            super.write(Instruction.argsep);
        }

        @Override // polyglot.util.CodeWriter
        public void allowBreak(int n) {
            super.write(Instruction.argsep);
        }

        @Override // polyglot.util.CodeWriter
        public void allowBreak(int n, String alt) {
            super.write(alt);
        }

        @Override // polyglot.util.CodeWriter
        public void begin(int n) {
            super.begin(0);
        }

        @Override // polyglot.util.CodeWriter
        public String toString() {
            return this.w.toString();
        }
    }
}
