package polyglot.ast;

import java.util.List;
import polyglot.types.Flags;
import polyglot.types.ParsedClassType;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/ClassDecl.class */
public interface ClassDecl extends Term, TopLevelDecl, ClassMember {
    ParsedClassType type();

    ClassDecl type(ParsedClassType parsedClassType);

    @Override // polyglot.ast.TopLevelDecl
    Flags flags();

    ClassDecl flags(Flags flags);

    @Override // polyglot.ast.TopLevelDecl
    String name();

    ClassDecl name(String str);

    TypeNode superClass();

    ClassDecl superClass(TypeNode typeNode);

    List interfaces();

    ClassDecl interfaces(List list);

    ClassBody body();

    ClassDecl body(ClassBody classBody);
}
