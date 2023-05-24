package polyglot.ext.jl.qq;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/qq/CUP$Grm$actions.class */
class CUP$Grm$actions {
    private final Grm parser;
    static Class class$polyglot$ast$Expr;
    static Class class$polyglot$ast$Catch;
    static Class class$polyglot$ast$Eval;
    static Class class$polyglot$ast$ForUpdate;
    static Class class$polyglot$ast$ForInit;
    static Class class$polyglot$ast$Case;
    static Class class$polyglot$ast$SwitchElement;
    static Class class$polyglot$ast$Stmt;
    static Class class$polyglot$ast$ClassMember;
    static Class class$polyglot$ast$TypeNode;
    static Class class$polyglot$ast$Formal;
    static Class class$polyglot$parse$VarDeclarator;
    static Class class$polyglot$ast$TopLevelDecl;
    static Class class$polyglot$ast$Import;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CUP$Grm$actions(Grm parser) {
        this.parser = parser;
    }

    /*  JADX ERROR: Type inference failed with exception
        jadx.core.utils.exceptions.JadxOverflowException: Type update terminated with stack overflow, arg: (r1v116 ?? I:int)
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:56)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:30)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:18)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:114)
        */
    public final java_cup.runtime.Symbol CUP$Grm$do_action(int r10, java_cup.runtime.lr_parser r11, java.util.Stack r12, int r13) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 60064
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: polyglot.ext.jl.qq.CUP$Grm$actions.CUP$Grm$do_action(int, java_cup.runtime.lr_parser, java.util.Stack, int):java_cup.runtime.Symbol");
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}
