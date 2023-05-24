package polyglot.ast;

import java.util.List;
import polyglot.types.Context;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.visit.AddMemberVisitor;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/NodeOps.class */
public interface NodeOps {
    Node visitChildren(NodeVisitor nodeVisitor);

    Context enterScope(Context context);

    Context enterScope(Node node, Context context);

    void addDecls(Context context);

    NodeVisitor buildTypesEnter(TypeBuilder typeBuilder) throws SemanticException;

    Node buildTypes(TypeBuilder typeBuilder) throws SemanticException;

    NodeVisitor disambiguateEnter(AmbiguityRemover ambiguityRemover) throws SemanticException;

    Node disambiguate(AmbiguityRemover ambiguityRemover) throws SemanticException;

    NodeVisitor addMembersEnter(AddMemberVisitor addMemberVisitor) throws SemanticException;

    Node addMembers(AddMemberVisitor addMemberVisitor) throws SemanticException;

    NodeVisitor typeCheckEnter(TypeChecker typeChecker) throws SemanticException;

    Node typeCheck(TypeChecker typeChecker) throws SemanticException;

    NodeVisitor exceptionCheckEnter(ExceptionChecker exceptionChecker) throws SemanticException;

    Node exceptionCheck(ExceptionChecker exceptionChecker) throws SemanticException;

    List throwTypes(TypeSystem typeSystem);

    void prettyPrint(CodeWriter codeWriter, PrettyPrinter prettyPrinter);

    void translate(CodeWriter codeWriter, Translator translator);
}
