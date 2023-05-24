package polyglot.ext.jl.qq;

import java_cup.runtime.Symbol;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/qq/QQParser.class */
public interface QQParser {
    Symbol qq_expr() throws Exception;

    Symbol qq_stmt() throws Exception;

    Symbol qq_type() throws Exception;

    Symbol qq_decl() throws Exception;

    Symbol qq_file() throws Exception;

    Symbol qq_member() throws Exception;
}
