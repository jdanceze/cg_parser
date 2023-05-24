package soot.javaToJimple.jj.ast;

import java.util.List;
import polyglot.ast.ArrayAccess;
import polyglot.ast.ArrayAccessAssign;
import polyglot.ast.ArrayInit;
import polyglot.ast.Assign;
import polyglot.ast.Binary;
import polyglot.ast.Call;
import polyglot.ast.Cast;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.FieldAssign;
import polyglot.ast.FieldDecl;
import polyglot.ast.Local;
import polyglot.ast.LocalAssign;
import polyglot.ast.LocalDecl;
import polyglot.ast.NewArray;
import polyglot.ast.Return;
import polyglot.ast.TypeNode;
import polyglot.ast.Unary;
import polyglot.ext.jl.ast.NodeFactory_c;
import polyglot.types.Flags;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/jj/ast/JjNodeFactory_c.class */
public class JjNodeFactory_c extends NodeFactory_c implements JjNodeFactory {
    @Override // soot.javaToJimple.jj.ast.JjNodeFactory
    public JjComma_c JjComma(Position pos, Expr first, Expr second) {
        JjComma_c n = new JjComma_c(pos, first, second);
        return n;
    }

    public JjAccessField_c JjAccessField(Position pos, Call getMeth, Call setMeth, Field field) {
        JjAccessField_c n = new JjAccessField_c(pos, getMeth, setMeth, field);
        return n;
    }

    @Override // polyglot.ext.jl.ast.NodeFactory_c, polyglot.ast.NodeFactory
    public Unary Unary(Position pos, Unary.Operator op, Expr expr) {
        Unary n = new JjUnary_c(pos, op, expr);
        return (Unary) ((Unary) n.ext(extFactory().extUnary())).del(delFactory().delUnary());
    }

    @Override // polyglot.ext.jl.ast.NodeFactory_c, polyglot.ast.NodeFactory
    public Binary Binary(Position pos, Expr left, Binary.Operator op, Expr right) {
        Binary n = new JjBinary_c(pos, left, op, right);
        return (Binary) ((Binary) n.ext(extFactory().extBinary())).del(delFactory().delBinary());
    }

    @Override // polyglot.ext.jl.ast.NodeFactory_c, polyglot.ast.NodeFactory
    public Assign Assign(Position pos, Expr left, Assign.Operator op, Expr right) {
        if (left instanceof Local) {
            return LocalAssign(pos, (Local) left, op, right);
        }
        if (left instanceof Field) {
            return FieldAssign(pos, (Field) left, op, right);
        }
        if (left instanceof ArrayAccess) {
            return ArrayAccessAssign(pos, (ArrayAccess) left, op, right);
        }
        return AmbAssign(pos, left, op, right);
    }

    @Override // polyglot.ext.jl.ast.NodeFactory_c, polyglot.ast.NodeFactory
    public LocalAssign LocalAssign(Position pos, Local left, Assign.Operator op, Expr right) {
        LocalAssign n = new JjLocalAssign_c(pos, left, op, right);
        return (LocalAssign) ((LocalAssign) n.ext(extFactory().extLocalAssign())).del(delFactory().delLocalAssign());
    }

    @Override // polyglot.ext.jl.ast.NodeFactory_c, polyglot.ast.NodeFactory
    public LocalDecl LocalDecl(Position pos, Flags flags, TypeNode type, String name, Expr init) {
        LocalDecl n = new JjLocalDecl_c(pos, flags, type, name, init);
        return (LocalDecl) ((LocalDecl) n.ext(extFactory().extLocalDecl())).del(delFactory().delLocalDecl());
    }

    @Override // polyglot.ext.jl.ast.NodeFactory_c, polyglot.ast.NodeFactory
    public FieldAssign FieldAssign(Position pos, Field left, Assign.Operator op, Expr right) {
        FieldAssign n = new JjFieldAssign_c(pos, left, op, right);
        return (FieldAssign) ((FieldAssign) n.ext(extFactory().extFieldAssign())).del(delFactory().delFieldAssign());
    }

    @Override // polyglot.ext.jl.ast.NodeFactory_c, polyglot.ast.NodeFactory
    public FieldDecl FieldDecl(Position pos, Flags flags, TypeNode type, String name, Expr init) {
        FieldDecl n = new JjFieldDecl_c(pos, flags, type, name, init);
        return (FieldDecl) ((FieldDecl) n.ext(extFactory().extFieldDecl())).del(delFactory().delFieldDecl());
    }

    @Override // polyglot.ext.jl.ast.NodeFactory_c, polyglot.ast.NodeFactory
    public ArrayAccessAssign ArrayAccessAssign(Position pos, ArrayAccess left, Assign.Operator op, Expr right) {
        ArrayAccessAssign n = new JjArrayAccessAssign_c(pos, left, op, right);
        return (ArrayAccessAssign) ((ArrayAccessAssign) n.ext(extFactory().extArrayAccessAssign())).del(delFactory().delArrayAccessAssign());
    }

    @Override // polyglot.ext.jl.ast.NodeFactory_c, polyglot.ast.NodeFactory
    public Cast Cast(Position pos, TypeNode type, Expr expr) {
        Cast n = new JjCast_c(pos, type, expr);
        return (Cast) ((Cast) n.ext(extFactory().extCast())).del(delFactory().delCast());
    }

    @Override // polyglot.ext.jl.ast.NodeFactory_c, polyglot.ast.NodeFactory
    public NewArray NewArray(Position pos, TypeNode base, List dims, int addDims, ArrayInit init) {
        return super.NewArray(pos, base, dims, addDims, init);
    }

    @Override // polyglot.ext.jl.ast.NodeFactory_c, polyglot.ast.NodeFactory
    public ArrayInit ArrayInit(Position pos, List elements) {
        ArrayInit n = new JjArrayInit_c(pos, elements);
        return (ArrayInit) ((ArrayInit) n.ext(extFactory().extArrayInit())).del(delFactory().delArrayInit());
    }

    @Override // polyglot.ext.jl.ast.NodeFactory_c, polyglot.ast.NodeFactory
    public Return Return(Position pos, Expr expr) {
        Return n = new JjReturn_c(pos, expr);
        return (Return) ((Return) n.ext(extFactory().extReturn())).del(delFactory().delReturn());
    }
}
