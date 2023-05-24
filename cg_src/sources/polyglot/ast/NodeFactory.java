package polyglot.ast;

import java.util.List;
import polyglot.ast.Assign;
import polyglot.ast.Binary;
import polyglot.ast.Branch;
import polyglot.ast.ConstructorCall;
import polyglot.ast.FloatLit;
import polyglot.ast.Import;
import polyglot.ast.IntLit;
import polyglot.ast.Special;
import polyglot.ast.Unary;
import polyglot.types.Flags;
import polyglot.types.Package;
import polyglot.types.Type;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/NodeFactory.class */
public interface NodeFactory {
    Disamb disamb();

    AmbExpr AmbExpr(Position position, String str);

    AmbReceiver AmbReceiver(Position position, String str);

    AmbReceiver AmbReceiver(Position position, Prefix prefix, String str);

    AmbQualifierNode AmbQualifierNode(Position position, String str);

    AmbQualifierNode AmbQualifierNode(Position position, QualifierNode qualifierNode, String str);

    AmbPrefix AmbPrefix(Position position, String str);

    AmbPrefix AmbPrefix(Position position, Prefix prefix, String str);

    AmbTypeNode AmbTypeNode(Position position, String str);

    AmbTypeNode AmbTypeNode(Position position, QualifierNode qualifierNode, String str);

    ArrayTypeNode ArrayTypeNode(Position position, TypeNode typeNode);

    CanonicalTypeNode CanonicalTypeNode(Position position, Type type);

    ArrayAccess ArrayAccess(Position position, Expr expr, Expr expr2);

    ArrayInit ArrayInit(Position position);

    ArrayInit ArrayInit(Position position, List list);

    Assert Assert(Position position, Expr expr);

    Assert Assert(Position position, Expr expr, Expr expr2);

    Assign Assign(Position position, Expr expr, Assign.Operator operator, Expr expr2);

    LocalAssign LocalAssign(Position position, Local local, Assign.Operator operator, Expr expr);

    FieldAssign FieldAssign(Position position, Field field, Assign.Operator operator, Expr expr);

    ArrayAccessAssign ArrayAccessAssign(Position position, ArrayAccess arrayAccess, Assign.Operator operator, Expr expr);

    AmbAssign AmbAssign(Position position, Expr expr, Assign.Operator operator, Expr expr2);

    Binary Binary(Position position, Expr expr, Binary.Operator operator, Expr expr2);

    Block Block(Position position);

    Block Block(Position position, Stmt stmt);

    Block Block(Position position, Stmt stmt, Stmt stmt2);

    Block Block(Position position, Stmt stmt, Stmt stmt2, Stmt stmt3);

    Block Block(Position position, Stmt stmt, Stmt stmt2, Stmt stmt3, Stmt stmt4);

    Block Block(Position position, List list);

    SwitchBlock SwitchBlock(Position position, List list);

    BooleanLit BooleanLit(Position position, boolean z);

    Branch Break(Position position);

    Branch Break(Position position, String str);

    Branch Continue(Position position);

    Branch Continue(Position position, String str);

    Branch Branch(Position position, Branch.Kind kind);

    Branch Branch(Position position, Branch.Kind kind, String str);

    Call Call(Position position, String str);

    Call Call(Position position, String str, Expr expr);

    Call Call(Position position, String str, Expr expr, Expr expr2);

    Call Call(Position position, String str, Expr expr, Expr expr2, Expr expr3);

    Call Call(Position position, String str, Expr expr, Expr expr2, Expr expr3, Expr expr4);

    Call Call(Position position, String str, List list);

    Call Call(Position position, Receiver receiver, String str);

    Call Call(Position position, Receiver receiver, String str, Expr expr);

    Call Call(Position position, Receiver receiver, String str, Expr expr, Expr expr2);

    Call Call(Position position, Receiver receiver, String str, Expr expr, Expr expr2, Expr expr3);

    Call Call(Position position, Receiver receiver, String str, Expr expr, Expr expr2, Expr expr3, Expr expr4);

    Call Call(Position position, Receiver receiver, String str, List list);

    Case Default(Position position);

    Case Case(Position position, Expr expr);

    Cast Cast(Position position, TypeNode typeNode, Expr expr);

    Catch Catch(Position position, Formal formal, Block block);

    CharLit CharLit(Position position, char c);

    ClassBody ClassBody(Position position, List list);

    ClassDecl ClassDecl(Position position, Flags flags, String str, TypeNode typeNode, List list, ClassBody classBody);

    ClassLit ClassLit(Position position, TypeNode typeNode);

    Conditional Conditional(Position position, Expr expr, Expr expr2, Expr expr3);

    ConstructorCall ThisCall(Position position, List list);

    ConstructorCall ThisCall(Position position, Expr expr, List list);

    ConstructorCall SuperCall(Position position, List list);

    ConstructorCall SuperCall(Position position, Expr expr, List list);

    ConstructorCall ConstructorCall(Position position, ConstructorCall.Kind kind, List list);

    ConstructorCall ConstructorCall(Position position, ConstructorCall.Kind kind, Expr expr, List list);

    ConstructorDecl ConstructorDecl(Position position, Flags flags, String str, List list, List list2, Block block);

    FieldDecl FieldDecl(Position position, Flags flags, TypeNode typeNode, String str);

    FieldDecl FieldDecl(Position position, Flags flags, TypeNode typeNode, String str, Expr expr);

    Do Do(Position position, Stmt stmt, Expr expr);

    Empty Empty(Position position);

    Eval Eval(Position position, Expr expr);

    Field Field(Position position, String str);

    Field Field(Position position, Receiver receiver, String str);

    FloatLit FloatLit(Position position, FloatLit.Kind kind, double d);

    For For(Position position, List list, Expr expr, List list2, Stmt stmt);

    Formal Formal(Position position, Flags flags, TypeNode typeNode, String str);

    If If(Position position, Expr expr, Stmt stmt);

    If If(Position position, Expr expr, Stmt stmt, Stmt stmt2);

    Import Import(Position position, Import.Kind kind, String str);

    Initializer Initializer(Position position, Flags flags, Block block);

    Instanceof Instanceof(Position position, Expr expr, TypeNode typeNode);

    IntLit IntLit(Position position, IntLit.Kind kind, long j);

    Labeled Labeled(Position position, String str, Stmt stmt);

    Local Local(Position position, String str);

    LocalClassDecl LocalClassDecl(Position position, ClassDecl classDecl);

    LocalDecl LocalDecl(Position position, Flags flags, TypeNode typeNode, String str);

    LocalDecl LocalDecl(Position position, Flags flags, TypeNode typeNode, String str, Expr expr);

    MethodDecl MethodDecl(Position position, Flags flags, TypeNode typeNode, String str, List list, List list2, Block block);

    New New(Position position, TypeNode typeNode, List list);

    New New(Position position, TypeNode typeNode, List list, ClassBody classBody);

    New New(Position position, Expr expr, TypeNode typeNode, List list);

    New New(Position position, Expr expr, TypeNode typeNode, List list, ClassBody classBody);

    NewArray NewArray(Position position, TypeNode typeNode, List list);

    NewArray NewArray(Position position, TypeNode typeNode, List list, int i);

    NewArray NewArray(Position position, TypeNode typeNode, int i, ArrayInit arrayInit);

    NewArray NewArray(Position position, TypeNode typeNode, List list, int i, ArrayInit arrayInit);

    NullLit NullLit(Position position);

    Return Return(Position position);

    Return Return(Position position, Expr expr);

    SourceCollection SourceCollection(Position position, List list);

    SourceFile SourceFile(Position position, List list);

    SourceFile SourceFile(Position position, List list, List list2);

    SourceFile SourceFile(Position position, PackageNode packageNode, List list, List list2);

    Special This(Position position);

    Special This(Position position, TypeNode typeNode);

    Special Super(Position position);

    Special Super(Position position, TypeNode typeNode);

    Special Special(Position position, Special.Kind kind);

    Special Special(Position position, Special.Kind kind, TypeNode typeNode);

    StringLit StringLit(Position position, String str);

    Switch Switch(Position position, Expr expr, List list);

    Synchronized Synchronized(Position position, Expr expr, Block block);

    Throw Throw(Position position, Expr expr);

    Try Try(Position position, Block block, List list);

    Try Try(Position position, Block block, List list, Block block2);

    PackageNode PackageNode(Position position, Package r2);

    Unary Unary(Position position, Unary.Operator operator, Expr expr);

    Unary Unary(Position position, Expr expr, Unary.Operator operator);

    While While(Position position, Expr expr, Stmt stmt);
}
