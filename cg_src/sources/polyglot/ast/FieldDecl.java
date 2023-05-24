package polyglot.ast;

import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.InitializerInstance;
import polyglot.types.Type;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/FieldDecl.class */
public interface FieldDecl extends ClassMember {
    Type declType();

    Flags flags();

    FieldDecl flags(Flags flags);

    TypeNode type();

    FieldDecl type(TypeNode typeNode);

    String name();

    FieldDecl name(String str);

    Expr init();

    FieldDecl init(Expr expr);

    FieldInstance fieldInstance();

    FieldDecl fieldInstance(FieldInstance fieldInstance);

    InitializerInstance initializerInstance();

    FieldDecl initializerInstance(InitializerInstance initializerInstance);
}
