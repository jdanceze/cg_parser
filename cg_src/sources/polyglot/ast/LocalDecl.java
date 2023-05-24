package polyglot.ast;

import polyglot.types.Flags;
import polyglot.types.LocalInstance;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/LocalDecl.class */
public interface LocalDecl extends ForInit, VarDecl {
    LocalDecl flags(Flags flags);

    LocalDecl type(TypeNode typeNode);

    LocalDecl name(String str);

    Expr init();

    LocalDecl init(Expr expr);

    LocalDecl localInstance(LocalInstance localInstance);
}
