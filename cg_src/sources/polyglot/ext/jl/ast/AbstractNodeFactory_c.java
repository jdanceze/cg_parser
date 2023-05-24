package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import polyglot.ast.AmbPrefix;
import polyglot.ast.AmbQualifierNode;
import polyglot.ast.AmbReceiver;
import polyglot.ast.AmbTypeNode;
import polyglot.ast.ArrayInit;
import polyglot.ast.Assert;
import polyglot.ast.Block;
import polyglot.ast.Branch;
import polyglot.ast.Call;
import polyglot.ast.Case;
import polyglot.ast.ClassBody;
import polyglot.ast.ConstructorCall;
import polyglot.ast.Disamb;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.FieldDecl;
import polyglot.ast.If;
import polyglot.ast.LocalDecl;
import polyglot.ast.New;
import polyglot.ast.NewArray;
import polyglot.ast.NodeFactory;
import polyglot.ast.Receiver;
import polyglot.ast.Return;
import polyglot.ast.SourceFile;
import polyglot.ast.Special;
import polyglot.ast.Stmt;
import polyglot.ast.Try;
import polyglot.ast.TypeNode;
import polyglot.ast.Unary;
import polyglot.types.Flags;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/AbstractNodeFactory_c.class */
public abstract class AbstractNodeFactory_c implements NodeFactory {
    @Override // polyglot.ast.NodeFactory
    public Disamb disamb() {
        return new Disamb_c();
    }

    @Override // polyglot.ast.NodeFactory
    public final AmbPrefix AmbPrefix(Position pos, String name) {
        return AmbPrefix(pos, null, name);
    }

    @Override // polyglot.ast.NodeFactory
    public final AmbReceiver AmbReceiver(Position pos, String name) {
        return AmbReceiver(pos, null, name);
    }

    @Override // polyglot.ast.NodeFactory
    public final AmbQualifierNode AmbQualifierNode(Position pos, String name) {
        return AmbQualifierNode(pos, null, name);
    }

    @Override // polyglot.ast.NodeFactory
    public final AmbTypeNode AmbTypeNode(Position pos, String name) {
        return AmbTypeNode(pos, null, name);
    }

    @Override // polyglot.ast.NodeFactory
    public final ArrayInit ArrayInit(Position pos) {
        return ArrayInit(pos, Collections.EMPTY_LIST);
    }

    @Override // polyglot.ast.NodeFactory
    public final Assert Assert(Position pos, Expr cond) {
        return Assert(pos, cond, null);
    }

    @Override // polyglot.ast.NodeFactory
    public final Block Block(Position pos) {
        return Block(pos, Collections.EMPTY_LIST);
    }

    @Override // polyglot.ast.NodeFactory
    public final Block Block(Position pos, Stmt s1) {
        List l = new ArrayList(1);
        l.add(s1);
        return Block(pos, l);
    }

    @Override // polyglot.ast.NodeFactory
    public final Block Block(Position pos, Stmt s1, Stmt s2) {
        List l = new ArrayList(2);
        l.add(s1);
        l.add(s2);
        return Block(pos, l);
    }

    @Override // polyglot.ast.NodeFactory
    public final Block Block(Position pos, Stmt s1, Stmt s2, Stmt s3) {
        List l = new ArrayList(3);
        l.add(s1);
        l.add(s2);
        l.add(s3);
        return Block(pos, l);
    }

    @Override // polyglot.ast.NodeFactory
    public final Block Block(Position pos, Stmt s1, Stmt s2, Stmt s3, Stmt s4) {
        List l = new ArrayList(4);
        l.add(s1);
        l.add(s2);
        l.add(s3);
        l.add(s4);
        return Block(pos, l);
    }

    @Override // polyglot.ast.NodeFactory
    public final Branch Break(Position pos) {
        return Branch(pos, Branch.BREAK, null);
    }

    @Override // polyglot.ast.NodeFactory
    public final Branch Break(Position pos, String label) {
        return Branch(pos, Branch.BREAK, label);
    }

    @Override // polyglot.ast.NodeFactory
    public final Branch Continue(Position pos) {
        return Branch(pos, Branch.CONTINUE, null);
    }

    @Override // polyglot.ast.NodeFactory
    public final Branch Continue(Position pos, String label) {
        return Branch(pos, Branch.CONTINUE, label);
    }

    @Override // polyglot.ast.NodeFactory
    public final Branch Branch(Position pos, Branch.Kind kind) {
        return Branch(pos, kind, null);
    }

    @Override // polyglot.ast.NodeFactory
    public final Call Call(Position pos, String name) {
        return Call(pos, (Receiver) null, name, Collections.EMPTY_LIST);
    }

    @Override // polyglot.ast.NodeFactory
    public final Call Call(Position pos, String name, Expr a1) {
        List l = new ArrayList(1);
        l.add(a1);
        return Call(pos, (Receiver) null, name, l);
    }

    @Override // polyglot.ast.NodeFactory
    public final Call Call(Position pos, String name, Expr a1, Expr a2) {
        List l = new ArrayList(2);
        l.add(a1);
        l.add(a2);
        return Call(pos, (Receiver) null, name, l);
    }

    @Override // polyglot.ast.NodeFactory
    public final Call Call(Position pos, String name, Expr a1, Expr a2, Expr a3) {
        List l = new ArrayList(3);
        l.add(a1);
        l.add(a2);
        l.add(a3);
        return Call(pos, (Receiver) null, name, l);
    }

    @Override // polyglot.ast.NodeFactory
    public final Call Call(Position pos, String name, Expr a1, Expr a2, Expr a3, Expr a4) {
        List l = new ArrayList(4);
        l.add(a1);
        l.add(a2);
        l.add(a3);
        l.add(a4);
        return Call(pos, (Receiver) null, name, l);
    }

    @Override // polyglot.ast.NodeFactory
    public final Call Call(Position pos, String name, List args) {
        return Call(pos, (Receiver) null, name, args);
    }

    @Override // polyglot.ast.NodeFactory
    public final Call Call(Position pos, Receiver target, String name) {
        return Call(pos, target, name, Collections.EMPTY_LIST);
    }

    @Override // polyglot.ast.NodeFactory
    public final Call Call(Position pos, Receiver target, String name, Expr a1) {
        List l = new ArrayList(1);
        l.add(a1);
        return Call(pos, target, name, l);
    }

    @Override // polyglot.ast.NodeFactory
    public final Call Call(Position pos, Receiver target, String name, Expr a1, Expr a2) {
        List l = new ArrayList(2);
        l.add(a1);
        l.add(a2);
        return Call(pos, target, name, l);
    }

    @Override // polyglot.ast.NodeFactory
    public final Call Call(Position pos, Receiver target, String name, Expr a1, Expr a2, Expr a3) {
        List l = new ArrayList(3);
        l.add(a1);
        l.add(a2);
        l.add(a3);
        return Call(pos, target, name, l);
    }

    @Override // polyglot.ast.NodeFactory
    public final Call Call(Position pos, Receiver target, String name, Expr a1, Expr a2, Expr a3, Expr a4) {
        List l = new ArrayList(4);
        l.add(a1);
        l.add(a2);
        l.add(a3);
        l.add(a4);
        return Call(pos, target, name, l);
    }

    @Override // polyglot.ast.NodeFactory
    public final Case Default(Position pos) {
        return Case(pos, null);
    }

    @Override // polyglot.ast.NodeFactory
    public final ConstructorCall ThisCall(Position pos, List args) {
        return ConstructorCall(pos, ConstructorCall.THIS, null, args);
    }

    @Override // polyglot.ast.NodeFactory
    public final ConstructorCall ThisCall(Position pos, Expr outer, List args) {
        return ConstructorCall(pos, ConstructorCall.THIS, outer, args);
    }

    @Override // polyglot.ast.NodeFactory
    public final ConstructorCall SuperCall(Position pos, List args) {
        return ConstructorCall(pos, ConstructorCall.SUPER, null, args);
    }

    @Override // polyglot.ast.NodeFactory
    public final ConstructorCall SuperCall(Position pos, Expr outer, List args) {
        return ConstructorCall(pos, ConstructorCall.SUPER, outer, args);
    }

    @Override // polyglot.ast.NodeFactory
    public final ConstructorCall ConstructorCall(Position pos, ConstructorCall.Kind kind, List args) {
        return ConstructorCall(pos, kind, null, args);
    }

    @Override // polyglot.ast.NodeFactory
    public final FieldDecl FieldDecl(Position pos, Flags flags, TypeNode type, String name) {
        return FieldDecl(pos, flags, type, name, null);
    }

    @Override // polyglot.ast.NodeFactory
    public final Field Field(Position pos, String name) {
        return Field(pos, null, name);
    }

    @Override // polyglot.ast.NodeFactory
    public final If If(Position pos, Expr cond, Stmt consequent) {
        return If(pos, cond, consequent, null);
    }

    @Override // polyglot.ast.NodeFactory
    public final LocalDecl LocalDecl(Position pos, Flags flags, TypeNode type, String name) {
        return LocalDecl(pos, flags, type, name, null);
    }

    @Override // polyglot.ast.NodeFactory
    public final New New(Position pos, TypeNode type, List args) {
        return New(pos, null, type, args, null);
    }

    @Override // polyglot.ast.NodeFactory
    public final New New(Position pos, TypeNode type, List args, ClassBody body) {
        return New(pos, null, type, args, body);
    }

    @Override // polyglot.ast.NodeFactory
    public final New New(Position pos, Expr outer, TypeNode objectType, List args) {
        return New(pos, outer, objectType, args, null);
    }

    @Override // polyglot.ast.NodeFactory
    public final NewArray NewArray(Position pos, TypeNode base, List dims) {
        return NewArray(pos, base, dims, 0, null);
    }

    @Override // polyglot.ast.NodeFactory
    public final NewArray NewArray(Position pos, TypeNode base, List dims, int addDims) {
        return NewArray(pos, base, dims, addDims, null);
    }

    @Override // polyglot.ast.NodeFactory
    public final NewArray NewArray(Position pos, TypeNode base, int addDims, ArrayInit init) {
        return NewArray(pos, base, Collections.EMPTY_LIST, addDims, init);
    }

    @Override // polyglot.ast.NodeFactory
    public final Return Return(Position pos) {
        return Return(pos, null);
    }

    @Override // polyglot.ast.NodeFactory
    public final SourceFile SourceFile(Position pos, List decls) {
        return SourceFile(pos, null, Collections.EMPTY_LIST, decls);
    }

    @Override // polyglot.ast.NodeFactory
    public final SourceFile SourceFile(Position pos, List imports, List decls) {
        return SourceFile(pos, null, imports, decls);
    }

    @Override // polyglot.ast.NodeFactory
    public final Special This(Position pos) {
        return Special(pos, Special.THIS, null);
    }

    @Override // polyglot.ast.NodeFactory
    public final Special This(Position pos, TypeNode outer) {
        return Special(pos, Special.THIS, outer);
    }

    @Override // polyglot.ast.NodeFactory
    public final Special Super(Position pos) {
        return Special(pos, Special.SUPER, null);
    }

    @Override // polyglot.ast.NodeFactory
    public final Special Super(Position pos, TypeNode outer) {
        return Special(pos, Special.SUPER, outer);
    }

    @Override // polyglot.ast.NodeFactory
    public final Special Special(Position pos, Special.Kind kind) {
        return Special(pos, kind, null);
    }

    @Override // polyglot.ast.NodeFactory
    public final Try Try(Position pos, Block tryBlock, List catchBlocks) {
        return Try(pos, tryBlock, catchBlocks, null);
    }

    @Override // polyglot.ast.NodeFactory
    public final Unary Unary(Position pos, Expr expr, Unary.Operator op) {
        return Unary(pos, op, expr);
    }
}
