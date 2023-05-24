package polyglot.ext.jl.parse;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java_cup.runtime.Symbol;
import java_cup.runtime.lr_parser;
import javassist.compiler.TokenId;
import org.hamcrest.generator.qdox.parser.impl.Parser;
import polyglot.ast.ArrayAccess;
import polyglot.ast.ArrayInit;
import polyglot.ast.Assert;
import polyglot.ast.Assign;
import polyglot.ast.Binary;
import polyglot.ast.Block;
import polyglot.ast.Branch;
import polyglot.ast.Call;
import polyglot.ast.Case;
import polyglot.ast.Cast;
import polyglot.ast.Catch;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassDecl;
import polyglot.ast.ConstructorCall;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.Do;
import polyglot.ast.Empty;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.FloatLit;
import polyglot.ast.For;
import polyglot.ast.Formal;
import polyglot.ast.If;
import polyglot.ast.Import;
import polyglot.ast.IntLit;
import polyglot.ast.Labeled;
import polyglot.ast.Lit;
import polyglot.ast.MethodDecl;
import polyglot.ast.NewArray;
import polyglot.ast.PackageNode;
import polyglot.ast.Return;
import polyglot.ast.SourceFile;
import polyglot.ast.Stmt;
import polyglot.ast.Switch;
import polyglot.ast.Synchronized;
import polyglot.ast.Throw;
import polyglot.ast.Try;
import polyglot.ast.TypeNode;
import polyglot.ast.Unary;
import polyglot.ast.While;
import polyglot.lex.BooleanLiteral;
import polyglot.lex.CharacterLiteral;
import polyglot.lex.DoubleLiteral;
import polyglot.lex.FloatLiteral;
import polyglot.lex.Identifier;
import polyglot.lex.IntegerLiteral;
import polyglot.lex.LongLiteral;
import polyglot.lex.NullLiteral;
import polyglot.lex.StringLiteral;
import polyglot.lex.Token;
import polyglot.parse.VarDeclarator;
import polyglot.types.Flags;
import polyglot.util.Position;
import polyglot.util.TypedList;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/parse/CUP$Grm$actions.class */
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

    public final Symbol CUP$Grm$do_action(int CUP$Grm$act_num, lr_parser CUP$Grm$parser, Stack CUP$Grm$stack, int CUP$Grm$top) throws Exception {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class cls6;
        Class cls7;
        Class cls8;
        Class cls9;
        Class cls10;
        Class cls11;
        Class cls12;
        Class cls13;
        Class cls14;
        Class cls15;
        Class cls16;
        Class cls17;
        Class cls18;
        Class cls19;
        Class cls20;
        Class cls21;
        Class cls22;
        Class cls23;
        Class cls24;
        Class cls25;
        Class cls26;
        Class cls27;
        Class cls28;
        Class cls29;
        Class cls30;
        Class cls31;
        Class cls32;
        Class cls33;
        Class cls34;
        Class cls35;
        Class cls36;
        Class cls37;
        Class cls38;
        Class cls39;
        Class cls40;
        Class cls41;
        Branch RESULT;
        Branch RESULT2;
        Class cls42;
        Class cls43;
        Class cls44;
        Class cls45;
        Class cls46;
        switch (CUP$Grm$act_num) {
            case 0:
                int i = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i2 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                SourceFile start_val = (SourceFile) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                Symbol CUP$Grm$result = new Symbol(0, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, start_val);
                CUP$Grm$parser.done_parsing();
                return CUP$Grm$result;
            case 1:
                int i3 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i4 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                SourceFile RESULT3 = this.parser.eq.hasErrors() ? null : (SourceFile) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Symbol CUP$Grm$result2 = new Symbol(1, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT3);
                return CUP$Grm$result2;
            case 2:
                int i5 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i6 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                IntegerLiteral a = (IntegerLiteral) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Lit RESULT4 = this.parser.nf.IntLit(this.parser.pos(a), IntLit.INT, a.getValue().intValue());
                Symbol CUP$Grm$result3 = new Symbol(2, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT4);
                return CUP$Grm$result3;
            case 3:
                int i7 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i8 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                LongLiteral a2 = (LongLiteral) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Lit RESULT5 = this.parser.nf.IntLit(this.parser.pos(a2), IntLit.LONG, a2.getValue().longValue());
                Symbol CUP$Grm$result4 = new Symbol(2, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT5);
                return CUP$Grm$result4;
            case 4:
                int i9 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i10 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                DoubleLiteral a3 = (DoubleLiteral) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Lit RESULT6 = this.parser.nf.FloatLit(this.parser.pos(a3), FloatLit.DOUBLE, a3.getValue().doubleValue());
                Symbol CUP$Grm$result5 = new Symbol(2, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT6);
                return CUP$Grm$result5;
            case 5:
                int i11 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i12 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                FloatLiteral a4 = (FloatLiteral) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Lit RESULT7 = this.parser.nf.FloatLit(this.parser.pos(a4), FloatLit.FLOAT, a4.getValue().floatValue());
                Symbol CUP$Grm$result6 = new Symbol(2, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT7);
                return CUP$Grm$result6;
            case 6:
                int i13 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i14 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                BooleanLiteral a5 = (BooleanLiteral) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Lit RESULT8 = this.parser.nf.BooleanLit(this.parser.pos(a5), a5.getValue().booleanValue());
                Symbol CUP$Grm$result7 = new Symbol(2, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT8);
                return CUP$Grm$result7;
            case 7:
                int i15 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i16 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                CharacterLiteral a6 = (CharacterLiteral) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Lit RESULT9 = this.parser.nf.CharLit(this.parser.pos(a6), a6.getValue().charValue());
                Symbol CUP$Grm$result8 = new Symbol(2, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT9);
                return CUP$Grm$result8;
            case 8:
                int i17 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i18 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                StringLiteral a7 = (StringLiteral) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Lit RESULT10 = this.parser.nf.StringLit(this.parser.pos(a7), a7.getValue());
                Symbol CUP$Grm$result9 = new Symbol(2, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT10);
                return CUP$Grm$result9;
            case 9:
                int i19 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i20 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Lit RESULT11 = this.parser.nf.NullLit(this.parser.pos((NullLiteral) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value));
                Symbol CUP$Grm$result10 = new Symbol(2, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT11);
                return CUP$Grm$result10;
            case 10:
                int i21 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i22 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                IntegerLiteral a8 = (IntegerLiteral) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Lit RESULT12 = this.parser.nf.IntLit(this.parser.pos(a8), IntLit.INT, a8.getValue().intValue());
                Symbol CUP$Grm$result11 = new Symbol(3, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT12);
                return CUP$Grm$result11;
            case 11:
                int i23 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i24 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                LongLiteral a9 = (LongLiteral) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Lit RESULT13 = this.parser.nf.IntLit(this.parser.pos(a9), IntLit.LONG, a9.getValue().longValue());
                Symbol CUP$Grm$result12 = new Symbol(3, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT13);
                return CUP$Grm$result12;
            case 12:
                int i25 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i26 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result13 = new Symbol(4, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result13;
            case 13:
                int i27 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i28 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result14 = new Symbol(4, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result14;
            case 14:
                int i29 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i30 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result15 = new Symbol(5, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result15;
            case 15:
                int i31 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i32 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                TypeNode RESULT14 = this.parser.nf.CanonicalTypeNode(this.parser.pos((Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value), this.parser.ts.Boolean());
                Symbol CUP$Grm$result16 = new Symbol(5, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT14);
                return CUP$Grm$result16;
            case 16:
                int i33 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i34 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result17 = new Symbol(6, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result17;
            case 17:
                int i35 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i36 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result18 = new Symbol(6, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result18;
            case 18:
                int i37 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i38 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                TypeNode RESULT15 = this.parser.nf.CanonicalTypeNode(this.parser.pos((Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value), this.parser.ts.Byte());
                Symbol CUP$Grm$result19 = new Symbol(7, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT15);
                return CUP$Grm$result19;
            case 19:
                int i39 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i40 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                TypeNode RESULT16 = this.parser.nf.CanonicalTypeNode(this.parser.pos((Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value), this.parser.ts.Char());
                Symbol CUP$Grm$result20 = new Symbol(7, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT16);
                return CUP$Grm$result20;
            case 20:
                int i41 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i42 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                TypeNode RESULT17 = this.parser.nf.CanonicalTypeNode(this.parser.pos((Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value), this.parser.ts.Short());
                Symbol CUP$Grm$result21 = new Symbol(7, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT17);
                return CUP$Grm$result21;
            case 21:
                int i43 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i44 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                TypeNode RESULT18 = this.parser.nf.CanonicalTypeNode(this.parser.pos((Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value), this.parser.ts.Int());
                Symbol CUP$Grm$result22 = new Symbol(7, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT18);
                return CUP$Grm$result22;
            case 22:
                int i45 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i46 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                TypeNode RESULT19 = this.parser.nf.CanonicalTypeNode(this.parser.pos((Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value), this.parser.ts.Long());
                Symbol CUP$Grm$result23 = new Symbol(7, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT19);
                return CUP$Grm$result23;
            case 23:
                int i47 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i48 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                TypeNode RESULT20 = this.parser.nf.CanonicalTypeNode(this.parser.pos((Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value), this.parser.ts.Float());
                Symbol CUP$Grm$result24 = new Symbol(8, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT20);
                return CUP$Grm$result24;
            case 24:
                int i49 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i50 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                TypeNode RESULT21 = this.parser.nf.CanonicalTypeNode(this.parser.pos((Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value), this.parser.ts.Double());
                Symbol CUP$Grm$result25 = new Symbol(8, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT21);
                return CUP$Grm$result25;
            case 25:
                int i51 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i52 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result26 = new Symbol(9, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result26;
            case 26:
                int i53 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i54 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result27 = new Symbol(9, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result27;
            case 27:
                int i55 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i56 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                TypeNode RESULT22 = ((Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value).toType();
                Symbol CUP$Grm$result28 = new Symbol(10, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT22);
                return CUP$Grm$result28;
            case 28:
                int i57 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i58 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result29 = new Symbol(11, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result29;
            case 29:
                int i59 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i60 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result30 = new Symbol(12, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result30;
            case 30:
                int i61 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i62 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                TypeNode a10 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i63 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i64 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Integer b = (Integer) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                TypeNode RESULT23 = this.parser.array(a10, b.intValue());
                Symbol CUP$Grm$result31 = new Symbol(13, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT23);
                return CUP$Grm$result31;
            case 31:
                int i65 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i66 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Name a11 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i67 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i68 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Integer b2 = (Integer) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                TypeNode RESULT24 = this.parser.array(a11.toType(), b2.intValue());
                Symbol CUP$Grm$result32 = new Symbol(13, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT24);
                return CUP$Grm$result32;
            case 32:
                int i69 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i70 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result33 = new Symbol(14, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result33;
            case 33:
                int i71 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i72 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result34 = new Symbol(14, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result34;
            case 34:
                int i73 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i74 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Identifier a12 = (Identifier) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Name RESULT25 = new Name(this.parser, this.parser.pos(a12), a12.getIdentifier());
                Symbol CUP$Grm$result35 = new Symbol(15, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT25);
                return CUP$Grm$result35;
            case 35:
                int i75 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i76 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Name a13 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i77 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i78 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Identifier b3 = (Identifier) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Name RESULT26 = new Name(this.parser, this.parser.pos(a13, b3), a13, b3.getIdentifier());
                Symbol CUP$Grm$result36 = new Symbol(16, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT26);
                return CUP$Grm$result36;
            case 36:
                int i79 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i80 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                PackageNode a14 = (PackageNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i81 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i82 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List b4 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i83 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i84 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                List c = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                SourceFile RESULT27 = this.parser.nf.SourceFile(new Position(this.parser.lexer.file()), a14, b4, c);
                Symbol CUP$Grm$result37 = new Symbol(17, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT27);
                return CUP$Grm$result37;
            case 37:
                int i85 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i86 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                List c2 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                SourceFile RESULT28 = this.parser.nf.SourceFile(new Position(this.parser.lexer.file()), null, Collections.EMPTY_LIST, c2);
                Symbol CUP$Grm$result38 = new Symbol(17, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT28);
                return CUP$Grm$result38;
            case 38:
                int i87 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i88 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result39 = new Symbol(18, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (PackageNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result39;
            case 39:
                Symbol CUP$Grm$result40 = new Symbol(18, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Object) null);
                return CUP$Grm$result40;
            case 40:
                int i89 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i90 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result41 = new Symbol(20, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result41;
            case 41:
                LinkedList linkedList = new LinkedList();
                if (class$polyglot$ast$Import == null) {
                    cls = class$("polyglot.ast.Import");
                    class$polyglot$ast$Import = cls;
                } else {
                    cls = class$polyglot$ast$Import;
                }
                List RESULT29 = new TypedList(linkedList, cls, false);
                Symbol CUP$Grm$result42 = new Symbol(20, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT29);
                return CUP$Grm$result42;
            case 42:
                int i91 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i92 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result43 = new Symbol(22, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result43;
            case 43:
                LinkedList linkedList2 = new LinkedList();
                if (class$polyglot$ast$TopLevelDecl == null) {
                    cls2 = class$("polyglot.ast.TopLevelDecl");
                    class$polyglot$ast$TopLevelDecl = cls2;
                } else {
                    cls2 = class$polyglot$ast$TopLevelDecl;
                }
                List RESULT30 = new TypedList(linkedList2, cls2, false);
                Symbol CUP$Grm$result44 = new Symbol(22, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT30);
                return CUP$Grm$result44;
            case 44:
                int i93 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i94 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Import a15 = (Import) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList3 = new LinkedList();
                if (class$polyglot$ast$Import == null) {
                    cls3 = class$("polyglot.ast.Import");
                    class$polyglot$ast$Import = cls3;
                } else {
                    cls3 = class$polyglot$ast$Import;
                }
                List l = new TypedList(linkedList3, cls3, false);
                l.add(a15);
                Symbol CUP$Grm$result45 = new Symbol(21, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
                return CUP$Grm$result45;
            case 45:
                int i95 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i96 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List a16 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i97 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i98 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Import b5 = (Import) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a16.add(b5);
                Symbol CUP$Grm$result46 = new Symbol(21, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a16);
                return CUP$Grm$result46;
            case 46:
                int i99 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i100 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                ClassDecl a17 = (ClassDecl) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList4 = new LinkedList();
                if (class$polyglot$ast$TopLevelDecl == null) {
                    cls4 = class$("polyglot.ast.TopLevelDecl");
                    class$polyglot$ast$TopLevelDecl = cls4;
                } else {
                    cls4 = class$polyglot$ast$TopLevelDecl;
                }
                List l2 = new TypedList(linkedList4, cls4, false);
                if (a17 != null) {
                    l2.add(a17);
                }
                Symbol CUP$Grm$result47 = new Symbol(23, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l2);
                return CUP$Grm$result47;
            case 47:
                int i101 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i102 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List a18 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i103 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i104 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                ClassDecl b6 = (ClassDecl) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                if (b6 != null) {
                    a18.add(b6);
                }
                Symbol CUP$Grm$result48 = new Symbol(23, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a18);
                return CUP$Grm$result48;
            case 48:
                int i105 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i106 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                PackageNode RESULT31 = ((Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value).toPackage();
                Symbol CUP$Grm$result49 = new Symbol(19, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT31);
                return CUP$Grm$result49;
            case 49:
                int i107 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i108 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result50 = new Symbol(24, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Import) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result50;
            case 50:
                int i109 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i110 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result51 = new Symbol(24, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Import) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result51;
            case 51:
                int i111 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i112 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token a19 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i113 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i114 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Name b7 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i115 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i116 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token c3 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Import RESULT32 = this.parser.nf.Import(this.parser.pos(a19, c3), Import.CLASS, b7.toString());
                Symbol CUP$Grm$result52 = new Symbol(25, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT32);
                return CUP$Grm$result52;
            case 52:
                int i117 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i118 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Token a20 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i119 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i120 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Name b8 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i121 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i122 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token c4 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Import RESULT33 = this.parser.nf.Import(this.parser.pos(a20, c4), Import.PACKAGE, b8.toString());
                Symbol CUP$Grm$result53 = new Symbol(26, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT33);
                return CUP$Grm$result53;
            case 53:
                int i123 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i124 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result54 = new Symbol(27, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (ClassDecl) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result54;
            case 54:
                int i125 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i126 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result55 = new Symbol(27, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (ClassDecl) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result55;
            case 55:
                Symbol CUP$Grm$result56 = new Symbol(27, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Object) null);
                return CUP$Grm$result56;
            case 56:
                Flags RESULT34 = Flags.NONE;
                Symbol CUP$Grm$result57 = new Symbol(28, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT34);
                return CUP$Grm$result57;
            case 57:
                int i127 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i128 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result58 = new Symbol(28, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Flags) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result58;
            case 58:
                int i129 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i130 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result59 = new Symbol(29, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Flags) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result59;
            case 59:
                int i131 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i132 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Flags a21 = (Flags) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i133 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i134 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Flags b9 = (Flags) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                if (a21.intersects(b9)) {
                    this.parser.die(this.parser.position());
                }
                Flags RESULT35 = a21.set(b9);
                Symbol CUP$Grm$result60 = new Symbol(29, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT35);
                return CUP$Grm$result60;
            case 60:
                int i135 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i136 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token token = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Flags RESULT36 = Flags.PUBLIC;
                Symbol CUP$Grm$result61 = new Symbol(30, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT36);
                return CUP$Grm$result61;
            case 61:
                int i137 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i138 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token token2 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Flags RESULT37 = Flags.PROTECTED;
                Symbol CUP$Grm$result62 = new Symbol(30, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT37);
                return CUP$Grm$result62;
            case 62:
                int i139 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i140 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token token3 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Flags RESULT38 = Flags.PRIVATE;
                Symbol CUP$Grm$result63 = new Symbol(30, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT38);
                return CUP$Grm$result63;
            case 63:
                int i141 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i142 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token token4 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Flags RESULT39 = Flags.STATIC;
                Symbol CUP$Grm$result64 = new Symbol(30, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT39);
                return CUP$Grm$result64;
            case 64:
                int i143 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i144 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token token5 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Flags RESULT40 = Flags.ABSTRACT;
                Symbol CUP$Grm$result65 = new Symbol(30, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT40);
                return CUP$Grm$result65;
            case 65:
                int i145 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i146 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token token6 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Flags RESULT41 = Flags.FINAL;
                Symbol CUP$Grm$result66 = new Symbol(30, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT41);
                return CUP$Grm$result66;
            case 66:
                int i147 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i148 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token token7 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Flags RESULT42 = Flags.NATIVE;
                Symbol CUP$Grm$result67 = new Symbol(30, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT42);
                return CUP$Grm$result67;
            case 67:
                int i149 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i150 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token token8 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Flags RESULT43 = Flags.SYNCHRONIZED;
                Symbol CUP$Grm$result68 = new Symbol(30, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT43);
                return CUP$Grm$result68;
            case 68:
                int i151 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i152 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token token9 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Flags RESULT44 = Flags.TRANSIENT;
                Symbol CUP$Grm$result69 = new Symbol(30, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT44);
                return CUP$Grm$result69;
            case 69:
                int i153 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i154 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token token10 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Flags RESULT45 = Flags.VOLATILE;
                Symbol CUP$Grm$result70 = new Symbol(30, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT45);
                return CUP$Grm$result70;
            case 70:
                int i155 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i156 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token token11 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Flags RESULT46 = Flags.STRICTFP;
                Symbol CUP$Grm$result71 = new Symbol(30, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT46);
                return CUP$Grm$result71;
            case 71:
                int i157 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
                int i158 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
                Flags a22 = (Flags) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
                int i159 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i160 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Token n = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i161 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i162 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Identifier b10 = (Identifier) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i163 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i164 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                TypeNode c5 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i165 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i166 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List d = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i167 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i168 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                ClassBody e = (ClassBody) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                ClassDecl RESULT47 = this.parser.nf.ClassDecl(this.parser.pos(n, e), a22, b10.getIdentifier(), c5, d, e);
                Symbol CUP$Grm$result72 = new Symbol(31, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT47);
                return CUP$Grm$result72;
            case 72:
                int i169 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i170 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result73 = new Symbol(32, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result73;
            case 73:
                Symbol CUP$Grm$result74 = new Symbol(33, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Object) null);
                return CUP$Grm$result74;
            case 74:
                int i171 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i172 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result75 = new Symbol(33, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result75;
            case 75:
                int i173 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i174 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result76 = new Symbol(34, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result76;
            case 76:
                LinkedList linkedList5 = new LinkedList();
                if (class$polyglot$ast$TypeNode == null) {
                    cls5 = class$("polyglot.ast.TypeNode");
                    class$polyglot$ast$TypeNode = cls5;
                } else {
                    cls5 = class$polyglot$ast$TypeNode;
                }
                List RESULT48 = new TypedList(linkedList5, cls5, false);
                Symbol CUP$Grm$result77 = new Symbol(35, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT48);
                return CUP$Grm$result77;
            case 77:
                int i175 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i176 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result78 = new Symbol(35, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result78;
            case 78:
                int i177 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i178 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                TypeNode a23 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList6 = new LinkedList();
                if (class$polyglot$ast$TypeNode == null) {
                    cls6 = class$("polyglot.ast.TypeNode");
                    class$polyglot$ast$TypeNode = cls6;
                } else {
                    cls6 = class$polyglot$ast$TypeNode;
                }
                List l3 = new TypedList(linkedList6, cls6, false);
                l3.add(a23);
                Symbol CUP$Grm$result79 = new Symbol(36, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l3);
                return CUP$Grm$result79;
            case 79:
                int i179 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i180 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List a24 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i181 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i182 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                TypeNode b11 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a24.add(b11);
                Symbol CUP$Grm$result80 = new Symbol(36, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a24);
                return CUP$Grm$result80;
            case 80:
                int i183 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i184 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token n2 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i185 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i186 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List a25 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i187 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i188 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token b12 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                ClassBody RESULT49 = this.parser.nf.ClassBody(this.parser.pos(n2, b12), a25);
                Symbol CUP$Grm$result81 = new Symbol(37, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT49);
                return CUP$Grm$result81;
            case 81:
                LinkedList linkedList7 = new LinkedList();
                if (class$polyglot$ast$ClassMember == null) {
                    cls7 = class$("polyglot.ast.ClassMember");
                    class$polyglot$ast$ClassMember = cls7;
                } else {
                    cls7 = class$polyglot$ast$ClassMember;
                }
                List RESULT50 = new TypedList(linkedList7, cls7, false);
                Symbol CUP$Grm$result82 = new Symbol(39, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT50);
                return CUP$Grm$result82;
            case 82:
                int i189 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i190 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result83 = new Symbol(39, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result83;
            case 83:
                int i191 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i192 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result84 = new Symbol(38, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result84;
            case 84:
                int i193 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i194 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List a26 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i195 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i196 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                List b13 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a26.addAll(b13);
                Symbol CUP$Grm$result85 = new Symbol(38, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a26);
                return CUP$Grm$result85;
            case 85:
                int i197 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i198 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result86 = new Symbol(40, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result86;
            case 86:
                int i199 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i200 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Block a27 = (Block) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList8 = new LinkedList();
                if (class$polyglot$ast$ClassMember == null) {
                    cls8 = class$("polyglot.ast.ClassMember");
                    class$polyglot$ast$ClassMember = cls8;
                } else {
                    cls8 = class$polyglot$ast$ClassMember;
                }
                List l4 = new TypedList(linkedList8, cls8, false);
                l4.add(this.parser.nf.Initializer(this.parser.pos(a27), Flags.STATIC, a27));
                Symbol CUP$Grm$result87 = new Symbol(40, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l4);
                return CUP$Grm$result87;
            case 87:
                int i201 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i202 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                ConstructorDecl a28 = (ConstructorDecl) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList9 = new LinkedList();
                if (class$polyglot$ast$ClassMember == null) {
                    cls9 = class$("polyglot.ast.ClassMember");
                    class$polyglot$ast$ClassMember = cls9;
                } else {
                    cls9 = class$polyglot$ast$ClassMember;
                }
                List l5 = new TypedList(linkedList9, cls9, false);
                l5.add(a28);
                Symbol CUP$Grm$result88 = new Symbol(40, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l5);
                return CUP$Grm$result88;
            case 88:
                int i203 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i204 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Block a29 = (Block) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList10 = new LinkedList();
                if (class$polyglot$ast$ClassMember == null) {
                    cls10 = class$("polyglot.ast.ClassMember");
                    class$polyglot$ast$ClassMember = cls10;
                } else {
                    cls10 = class$polyglot$ast$ClassMember;
                }
                List l6 = new TypedList(linkedList10, cls10, false);
                l6.add(this.parser.nf.Initializer(this.parser.pos(a29), Flags.NONE, a29));
                Symbol CUP$Grm$result89 = new Symbol(40, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l6);
                return CUP$Grm$result89;
            case 89:
                LinkedList linkedList11 = new LinkedList();
                if (class$polyglot$ast$ClassMember == null) {
                    cls11 = class$("polyglot.ast.ClassMember");
                    class$polyglot$ast$ClassMember = cls11;
                } else {
                    cls11 = class$polyglot$ast$ClassMember;
                }
                Symbol CUP$Grm$result90 = new Symbol(40, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, new TypedList(linkedList11, cls11, false));
                return CUP$Grm$result90;
            case 90:
                int i205 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i206 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token token12 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList12 = new LinkedList();
                if (class$polyglot$ast$ClassMember == null) {
                    cls12 = class$("polyglot.ast.ClassMember");
                    class$polyglot$ast$ClassMember = cls12;
                } else {
                    cls12 = class$polyglot$ast$ClassMember;
                }
                Symbol CUP$Grm$result91 = new Symbol(40, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, new TypedList(linkedList12, cls12, false));
                return CUP$Grm$result91;
            case 91:
                int i207 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i208 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token token13 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList13 = new LinkedList();
                if (class$polyglot$ast$ClassMember == null) {
                    cls13 = class$("polyglot.ast.ClassMember");
                    class$polyglot$ast$ClassMember = cls13;
                } else {
                    cls13 = class$polyglot$ast$ClassMember;
                }
                Symbol CUP$Grm$result92 = new Symbol(40, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, new TypedList(linkedList13, cls13, false));
                return CUP$Grm$result92;
            case 92:
                int i209 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i210 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result93 = new Symbol(41, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result93;
            case 93:
                int i211 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i212 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                MethodDecl a30 = (MethodDecl) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList14 = new LinkedList();
                if (class$polyglot$ast$ClassMember == null) {
                    cls14 = class$("polyglot.ast.ClassMember");
                    class$polyglot$ast$ClassMember = cls14;
                } else {
                    cls14 = class$polyglot$ast$ClassMember;
                }
                List l7 = new TypedList(linkedList14, cls14, false);
                l7.add(a30);
                Symbol CUP$Grm$result94 = new Symbol(41, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l7);
                return CUP$Grm$result94;
            case 94:
                int i213 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
                int i214 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
                Flags a31 = (Flags) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
                int i215 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i216 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Token n3 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i217 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i218 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Identifier b14 = (Identifier) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i219 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i220 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                TypeNode c6 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i221 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i222 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List d2 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i223 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i224 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                ClassBody e2 = (ClassBody) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList15 = new LinkedList();
                if (class$polyglot$ast$ClassMember == null) {
                    cls15 = class$("polyglot.ast.ClassMember");
                    class$polyglot$ast$ClassMember = cls15;
                } else {
                    cls15 = class$polyglot$ast$ClassMember;
                }
                List l8 = new TypedList(linkedList15, cls15, false);
                l8.add(this.parser.nf.ClassDecl(this.parser.pos(n3, e2), a31, b14.getIdentifier(), c6, d2, e2));
                Symbol CUP$Grm$result95 = new Symbol(41, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l8);
                return CUP$Grm$result95;
            case 95:
                int i225 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i226 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                ClassDecl a32 = (ClassDecl) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList16 = new LinkedList();
                if (class$polyglot$ast$ClassMember == null) {
                    cls16 = class$("polyglot.ast.ClassMember");
                    class$polyglot$ast$ClassMember = cls16;
                } else {
                    cls16 = class$polyglot$ast$ClassMember;
                }
                List l9 = new TypedList(linkedList16, cls16, false);
                l9.add(a32);
                Symbol CUP$Grm$result96 = new Symbol(41, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l9);
                return CUP$Grm$result96;
            case 96:
                int i227 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i228 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Flags a33 = (Flags) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i229 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i230 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                TypeNode b15 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i231 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i232 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List<VarDeclarator> c7 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i233 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i234 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token e3 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList17 = new LinkedList();
                if (class$polyglot$ast$ClassMember == null) {
                    cls17 = class$("polyglot.ast.ClassMember");
                    class$polyglot$ast$ClassMember = cls17;
                } else {
                    cls17 = class$polyglot$ast$ClassMember;
                }
                List l10 = new TypedList(linkedList17, cls17, false);
                for (VarDeclarator d3 : c7) {
                    l10.add(this.parser.nf.FieldDecl(this.parser.pos(b15, e3), a33, this.parser.array(b15, d3.dims), d3.name, d3.init));
                }
                Symbol CUP$Grm$result97 = new Symbol(42, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l10);
                return CUP$Grm$result97;
            case 97:
                int i235 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i236 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                VarDeclarator a34 = (VarDeclarator) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList18 = new LinkedList();
                if (class$polyglot$parse$VarDeclarator == null) {
                    cls18 = class$("polyglot.parse.VarDeclarator");
                    class$polyglot$parse$VarDeclarator = cls18;
                } else {
                    cls18 = class$polyglot$parse$VarDeclarator;
                }
                List l11 = new TypedList(linkedList18, cls18, false);
                l11.add(a34);
                Symbol CUP$Grm$result98 = new Symbol(43, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l11);
                return CUP$Grm$result98;
            case 98:
                int i237 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i238 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List a35 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i239 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i240 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                VarDeclarator b16 = (VarDeclarator) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a35.add(b16);
                Symbol CUP$Grm$result99 = new Symbol(43, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a35);
                return CUP$Grm$result99;
            case 99:
                int i241 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i242 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result100 = new Symbol(44, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (VarDeclarator) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result100;
            case 100:
                int i243 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i244 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                VarDeclarator a36 = (VarDeclarator) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i245 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i246 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b17 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a36.init = b17;
                Symbol CUP$Grm$result101 = new Symbol(44, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a36);
                return CUP$Grm$result101;
            case 101:
                int i247 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i248 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Identifier a37 = (Identifier) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                VarDeclarator RESULT51 = new VarDeclarator(this.parser.pos(a37), a37.getIdentifier());
                Symbol CUP$Grm$result102 = new Symbol(45, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT51);
                return CUP$Grm$result102;
            case 102:
                int i249 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i250 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                VarDeclarator a38 = (VarDeclarator) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                a38.dims++;
                Symbol CUP$Grm$result103 = new Symbol(45, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a38);
                return CUP$Grm$result103;
            case 103:
                int i251 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i252 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result104 = new Symbol(46, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result104;
            case 104:
                int i253 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i254 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result105 = new Symbol(46, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (ArrayInit) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result105;
            case 105:
                int i255 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i256 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                MethodDecl a39 = (MethodDecl) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i257 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i258 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Block b18 = (Block) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                MethodDecl RESULT52 = (MethodDecl) a39.body(b18);
                Symbol CUP$Grm$result106 = new Symbol(47, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT52);
                return CUP$Grm$result106;
            case 106:
                int i259 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).left;
                int i260 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).right;
                Flags a40 = (Flags) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).value;
                int i261 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
                int i262 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
                TypeNode b19 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
                int i263 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
                int i264 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
                Identifier c8 = (Identifier) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
                int i265 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i266 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                List d4 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i267 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i268 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token g = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i269 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i270 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Integer e4 = (Integer) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i271 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i272 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                List f = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                MethodDecl RESULT53 = this.parser.nf.MethodDecl(this.parser.pos(b19, g, c8), a40, this.parser.array(b19, e4.intValue()), c8.getIdentifier(), d4, f, null);
                Symbol CUP$Grm$result107 = new Symbol(48, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT53);
                return CUP$Grm$result107;
            case 107:
                int i273 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
                int i274 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
                Flags a41 = (Flags) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
                int i275 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
                int i276 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
                Token b20 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
                int i277 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i278 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Identifier c9 = (Identifier) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i279 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i280 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List d5 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i281 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i282 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Token g2 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i283 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i284 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                List f2 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                MethodDecl RESULT54 = this.parser.nf.MethodDecl(this.parser.pos(b20, g2, c9), a41, this.parser.nf.CanonicalTypeNode(this.parser.pos(b20), this.parser.ts.Void()), c9.getIdentifier(), d5, f2, null);
                Symbol CUP$Grm$result108 = new Symbol(48, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT54);
                return CUP$Grm$result108;
            case 108:
                LinkedList linkedList19 = new LinkedList();
                if (class$polyglot$ast$Formal == null) {
                    cls19 = class$("polyglot.ast.Formal");
                    class$polyglot$ast$Formal = cls19;
                } else {
                    cls19 = class$polyglot$ast$Formal;
                }
                List RESULT55 = new TypedList(linkedList19, cls19, false);
                Symbol CUP$Grm$result109 = new Symbol(49, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT55);
                return CUP$Grm$result109;
            case 109:
                int i285 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i286 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result110 = new Symbol(49, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result110;
            case 110:
                int i287 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i288 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Formal a42 = (Formal) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList20 = new LinkedList();
                if (class$polyglot$ast$Formal == null) {
                    cls20 = class$("polyglot.ast.Formal");
                    class$polyglot$ast$Formal = cls20;
                } else {
                    cls20 = class$polyglot$ast$Formal;
                }
                List l12 = new TypedList(linkedList20, cls20, false);
                l12.add(a42);
                Symbol CUP$Grm$result111 = new Symbol(50, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l12);
                return CUP$Grm$result111;
            case 111:
                int i289 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i290 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List a43 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i291 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i292 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Formal b21 = (Formal) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a43.add(b21);
                Symbol CUP$Grm$result112 = new Symbol(50, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a43);
                return CUP$Grm$result112;
            case 112:
                int i293 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i294 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                TypeNode a44 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i295 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i296 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                VarDeclarator b22 = (VarDeclarator) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Formal RESULT56 = this.parser.nf.Formal(this.parser.pos(a44, b22, b22), Flags.NONE, this.parser.array(a44, b22.dims), b22.name);
                Symbol CUP$Grm$result113 = new Symbol(51, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT56);
                return CUP$Grm$result113;
            case 113:
                int i297 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i298 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                TypeNode a45 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i299 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i300 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                VarDeclarator b23 = (VarDeclarator) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Formal RESULT57 = this.parser.nf.Formal(this.parser.pos(a45, b23, b23), Flags.FINAL, this.parser.array(a45, b23.dims), b23.name);
                Symbol CUP$Grm$result114 = new Symbol(51, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT57);
                return CUP$Grm$result114;
            case 114:
                LinkedList linkedList21 = new LinkedList();
                if (class$polyglot$ast$TypeNode == null) {
                    cls21 = class$("polyglot.ast.TypeNode");
                    class$polyglot$ast$TypeNode = cls21;
                } else {
                    cls21 = class$polyglot$ast$TypeNode;
                }
                List RESULT58 = new TypedList(linkedList21, cls21, false);
                Symbol CUP$Grm$result115 = new Symbol(52, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT58);
                return CUP$Grm$result115;
            case 115:
                int i301 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i302 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result116 = new Symbol(52, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result116;
            case 116:
                int i303 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i304 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result117 = new Symbol(53, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result117;
            case 117:
                int i305 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i306 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                TypeNode a46 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList22 = new LinkedList();
                if (class$polyglot$ast$TypeNode == null) {
                    cls22 = class$("polyglot.ast.TypeNode");
                    class$polyglot$ast$TypeNode = cls22;
                } else {
                    cls22 = class$polyglot$ast$TypeNode;
                }
                List l13 = new TypedList(linkedList22, cls22, false);
                l13.add(a46);
                Symbol CUP$Grm$result118 = new Symbol(54, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l13);
                return CUP$Grm$result118;
            case 118:
                int i307 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i308 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List a47 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i309 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i310 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                TypeNode b24 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a47.add(b24);
                Symbol CUP$Grm$result119 = new Symbol(54, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a47);
                return CUP$Grm$result119;
            case 119:
                int i311 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i312 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result120 = new Symbol(55, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Block) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result120;
            case 120:
                Symbol CUP$Grm$result121 = new Symbol(55, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Object) null);
                return CUP$Grm$result121;
            case 121:
                int i313 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i314 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result122 = new Symbol(56, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Block) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result122;
            case 122:
                int i315 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
                int i316 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
                Flags m = (Flags) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
                int i317 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
                int i318 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
                Name a48 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
                int i319 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i320 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                List b25 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i321 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i322 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List c10 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i323 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i324 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Block d6 = (Block) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                ConstructorDecl RESULT59 = this.parser.nf.ConstructorDecl(this.parser.pos(a48, d6), m, a48.toString(), b25, c10, d6);
                Symbol CUP$Grm$result123 = new Symbol(57, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT59);
                return CUP$Grm$result123;
            case 123:
                int i325 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i326 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Token n4 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i327 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i328 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                ConstructorCall a49 = (ConstructorCall) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i329 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i330 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List b26 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i331 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i332 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d7 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList23 = new LinkedList();
                if (class$polyglot$ast$Stmt == null) {
                    cls23 = class$("polyglot.ast.Stmt");
                    class$polyglot$ast$Stmt = cls23;
                } else {
                    cls23 = class$polyglot$ast$Stmt;
                }
                List l14 = new TypedList(linkedList23, cls23, false);
                l14.add(a49);
                l14.addAll(b26);
                Block RESULT60 = this.parser.nf.Block(this.parser.pos(n4, d7), l14);
                Symbol CUP$Grm$result124 = new Symbol(58, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT60);
                return CUP$Grm$result124;
            case 124:
                int i333 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i334 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token n5 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i335 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i336 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                ConstructorCall a50 = (ConstructorCall) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i337 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i338 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d8 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Block RESULT61 = this.parser.nf.Block(this.parser.pos(n5, d8), a50);
                Symbol CUP$Grm$result125 = new Symbol(58, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT61);
                return CUP$Grm$result125;
            case 125:
                int i339 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i340 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token n6 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i341 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i342 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List a51 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i343 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i344 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d9 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a51.add(0, this.parser.nf.SuperCall(this.parser.pos(n6, d9), Collections.EMPTY_LIST));
                Block RESULT62 = this.parser.nf.Block(this.parser.pos(n6, d9), a51);
                Symbol CUP$Grm$result126 = new Symbol(58, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT62);
                return CUP$Grm$result126;
            case 126:
                int i345 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i346 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Token n7 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i347 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i348 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d10 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Block RESULT63 = this.parser.nf.Block(this.parser.pos(n7, d10), this.parser.nf.SuperCall(this.parser.pos(n7, d10), Collections.EMPTY_LIST));
                Symbol CUP$Grm$result127 = new Symbol(58, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT63);
                return CUP$Grm$result127;
            case 127:
                int i349 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i350 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Token a52 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i351 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i352 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List b27 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i353 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i354 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token c11 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                ConstructorCall RESULT64 = this.parser.nf.ThisCall(this.parser.pos(a52, c11), b27);
                Symbol CUP$Grm$result128 = new Symbol(59, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT64);
                return CUP$Grm$result128;
            case 128:
                int i355 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i356 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Token a53 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i357 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i358 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List b28 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i359 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i360 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token c12 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                ConstructorCall RESULT65 = this.parser.nf.SuperCall(this.parser.pos(a53, c12), b28);
                Symbol CUP$Grm$result129 = new Symbol(59, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT65);
                return CUP$Grm$result129;
            case 129:
                int i361 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
                int i362 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
                Expr a54 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
                int i363 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i364 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Token n8 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i365 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i366 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List b29 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i367 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i368 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token c13 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                ConstructorCall RESULT66 = this.parser.nf.ThisCall(this.parser.pos(a54, c13, n8), a54, b29);
                Symbol CUP$Grm$result130 = new Symbol(59, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT66);
                return CUP$Grm$result130;
            case 130:
                int i369 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
                int i370 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
                Expr a55 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
                int i371 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i372 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Token n9 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i373 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i374 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List b30 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i375 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i376 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token c14 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                ConstructorCall RESULT67 = this.parser.nf.SuperCall(this.parser.pos(a55, c14, n9), a55, b30);
                Symbol CUP$Grm$result131 = new Symbol(59, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT67);
                return CUP$Grm$result131;
            case 131:
                int i377 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i378 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Flags a56 = (Flags) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i379 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i380 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Token n10 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i381 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i382 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Identifier b31 = (Identifier) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i383 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i384 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List c15 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i385 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i386 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                ClassBody d11 = (ClassBody) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                ClassDecl RESULT68 = this.parser.nf.ClassDecl(this.parser.pos(n10, d11), a56.Interface(), b31.getIdentifier(), null, c15, d11);
                Symbol CUP$Grm$result132 = new Symbol(60, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT68);
                return CUP$Grm$result132;
            case 132:
                LinkedList linkedList24 = new LinkedList();
                if (class$polyglot$ast$TypeNode == null) {
                    cls24 = class$("polyglot.ast.TypeNode");
                    class$polyglot$ast$TypeNode = cls24;
                } else {
                    cls24 = class$polyglot$ast$TypeNode;
                }
                List RESULT69 = new TypedList(linkedList24, cls24, false);
                Symbol CUP$Grm$result133 = new Symbol(61, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT69);
                return CUP$Grm$result133;
            case 133:
                int i387 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i388 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result134 = new Symbol(61, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result134;
            case 134:
                int i389 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i390 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                TypeNode a57 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList25 = new LinkedList();
                if (class$polyglot$ast$TypeNode == null) {
                    cls25 = class$("polyglot.ast.TypeNode");
                    class$polyglot$ast$TypeNode = cls25;
                } else {
                    cls25 = class$polyglot$ast$TypeNode;
                }
                List l15 = new TypedList(linkedList25, cls25, false);
                l15.add(a57);
                Symbol CUP$Grm$result135 = new Symbol(62, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l15);
                return CUP$Grm$result135;
            case 135:
                int i391 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i392 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List a58 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i393 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i394 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                TypeNode b32 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a58.add(b32);
                Symbol CUP$Grm$result136 = new Symbol(62, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a58);
                return CUP$Grm$result136;
            case 136:
                int i395 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i396 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token n11 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i397 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i398 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List a59 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i399 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i400 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d12 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                ClassBody RESULT70 = this.parser.nf.ClassBody(this.parser.pos(n11, d12), a59);
                Symbol CUP$Grm$result137 = new Symbol(63, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT70);
                return CUP$Grm$result137;
            case 137:
                LinkedList linkedList26 = new LinkedList();
                if (class$polyglot$ast$ClassMember == null) {
                    cls26 = class$("polyglot.ast.ClassMember");
                    class$polyglot$ast$ClassMember = cls26;
                } else {
                    cls26 = class$polyglot$ast$ClassMember;
                }
                List RESULT71 = new TypedList(linkedList26, cls26, false);
                Symbol CUP$Grm$result138 = new Symbol(64, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT71);
                return CUP$Grm$result138;
            case 138:
                int i401 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i402 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result139 = new Symbol(64, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result139;
            case 139:
                int i403 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i404 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result140 = new Symbol(65, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result140;
            case 140:
                int i405 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i406 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List a60 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i407 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i408 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                List b33 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a60.addAll(b33);
                Symbol CUP$Grm$result141 = new Symbol(65, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a60);
                return CUP$Grm$result141;
            case 141:
                int i409 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i410 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result142 = new Symbol(66, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result142;
            case 142:
                int i411 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i412 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                MethodDecl a61 = (MethodDecl) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList27 = new LinkedList();
                if (class$polyglot$ast$ClassMember == null) {
                    cls27 = class$("polyglot.ast.ClassMember");
                    class$polyglot$ast$ClassMember = cls27;
                } else {
                    cls27 = class$polyglot$ast$ClassMember;
                }
                List l16 = new TypedList(linkedList27, cls27, false);
                l16.add(a61);
                Symbol CUP$Grm$result143 = new Symbol(66, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l16);
                return CUP$Grm$result143;
            case 143:
                int i413 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i414 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                ClassDecl a62 = (ClassDecl) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList28 = new LinkedList();
                if (class$polyglot$ast$ClassMember == null) {
                    cls28 = class$("polyglot.ast.ClassMember");
                    class$polyglot$ast$ClassMember = cls28;
                } else {
                    cls28 = class$polyglot$ast$ClassMember;
                }
                List l17 = new TypedList(linkedList28, cls28, false);
                l17.add(a62);
                Symbol CUP$Grm$result144 = new Symbol(66, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l17);
                return CUP$Grm$result144;
            case 144:
                int i415 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i416 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                ClassDecl a63 = (ClassDecl) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList29 = new LinkedList();
                if (class$polyglot$ast$ClassMember == null) {
                    cls29 = class$("polyglot.ast.ClassMember");
                    class$polyglot$ast$ClassMember = cls29;
                } else {
                    cls29 = class$polyglot$ast$ClassMember;
                }
                List l18 = new TypedList(linkedList29, cls29, false);
                l18.add(a63);
                Symbol CUP$Grm$result145 = new Symbol(66, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l18);
                return CUP$Grm$result145;
            case 145:
                List RESULT72 = Collections.EMPTY_LIST;
                Symbol CUP$Grm$result146 = new Symbol(66, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT72);
                return CUP$Grm$result146;
            case 146:
                int i417 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i418 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result147 = new Symbol(67, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result147;
            case 147:
                int i419 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i420 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Symbol CUP$Grm$result148 = new Symbol(68, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (MethodDecl) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value);
                return CUP$Grm$result148;
            case 148:
                int i421 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i422 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Token n12 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i423 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i424 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List a64 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i425 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i426 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d13 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                ArrayInit RESULT73 = this.parser.nf.ArrayInit(this.parser.pos(n12, d13), a64);
                Symbol CUP$Grm$result149 = new Symbol(69, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT73);
                return CUP$Grm$result149;
            case 149:
                int i427 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i428 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token n13 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i429 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i430 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List a65 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i431 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i432 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d14 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                ArrayInit RESULT74 = this.parser.nf.ArrayInit(this.parser.pos(n13, d14), a65);
                Symbol CUP$Grm$result150 = new Symbol(69, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT74);
                return CUP$Grm$result150;
            case 150:
                int i433 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i434 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token n14 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i435 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i436 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d15 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                ArrayInit RESULT75 = this.parser.nf.ArrayInit(this.parser.pos(n14, d15));
                Symbol CUP$Grm$result151 = new Symbol(69, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT75);
                return CUP$Grm$result151;
            case 151:
                int i437 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i438 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Token n15 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i439 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i440 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d16 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                ArrayInit RESULT76 = this.parser.nf.ArrayInit(this.parser.pos(n15, d16));
                Symbol CUP$Grm$result152 = new Symbol(69, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT76);
                return CUP$Grm$result152;
            case 152:
                int i441 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i442 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr a66 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList30 = new LinkedList();
                if (class$polyglot$ast$Expr == null) {
                    cls30 = class$("polyglot.ast.Expr");
                    class$polyglot$ast$Expr = cls30;
                } else {
                    cls30 = class$polyglot$ast$Expr;
                }
                List l19 = new TypedList(linkedList30, cls30, false);
                l19.add(a66);
                Symbol CUP$Grm$result153 = new Symbol(70, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l19);
                return CUP$Grm$result153;
            case 153:
                int i443 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i444 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List a67 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i445 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i446 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b34 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a67.add(b34);
                Symbol CUP$Grm$result154 = new Symbol(70, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a67);
                return CUP$Grm$result154;
            case 154:
                int i447 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i448 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token n16 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i449 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i450 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List a68 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i451 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i452 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d17 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Block RESULT77 = this.parser.nf.Block(this.parser.pos(n16, d17), a68);
                Symbol CUP$Grm$result155 = new Symbol(71, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT77);
                return CUP$Grm$result155;
            case 155:
                int i453 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i454 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d18 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Block RESULT78 = this.parser.nf.Block(this.parser.pos(d18), Collections.EMPTY_LIST);
                Symbol CUP$Grm$result156 = new Symbol(71, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT78);
                return CUP$Grm$result156;
            case 156:
                LinkedList linkedList31 = new LinkedList();
                if (class$polyglot$ast$Stmt == null) {
                    cls31 = class$("polyglot.ast.Stmt");
                    class$polyglot$ast$Stmt = cls31;
                } else {
                    cls31 = class$polyglot$ast$Stmt;
                }
                List RESULT79 = new TypedList(linkedList31, cls31, false);
                Symbol CUP$Grm$result157 = new Symbol(72, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT79);
                return CUP$Grm$result157;
            case 157:
                int i455 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i456 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result158 = new Symbol(72, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result158;
            case 158:
                int i457 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i458 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                List a69 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList32 = new LinkedList();
                if (class$polyglot$ast$Stmt == null) {
                    cls32 = class$("polyglot.ast.Stmt");
                    class$polyglot$ast$Stmt = cls32;
                } else {
                    cls32 = class$polyglot$ast$Stmt;
                }
                List l20 = new TypedList(linkedList32, cls32, false);
                l20.addAll(a69);
                Symbol CUP$Grm$result159 = new Symbol(73, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l20);
                return CUP$Grm$result159;
            case 159:
                int i459 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i460 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List a70 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i461 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i462 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                List b35 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a70.addAll(b35);
                Symbol CUP$Grm$result160 = new Symbol(73, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a70);
                return CUP$Grm$result160;
            case 160:
                int i463 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i464 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result161 = new Symbol(74, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result161;
            case 161:
                int i465 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i466 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Stmt a71 = (Stmt) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList33 = new LinkedList();
                if (class$polyglot$ast$Stmt == null) {
                    cls33 = class$("polyglot.ast.Stmt");
                    class$polyglot$ast$Stmt = cls33;
                } else {
                    cls33 = class$polyglot$ast$Stmt;
                }
                List l21 = new TypedList(linkedList33, cls33, false);
                l21.add(a71);
                Symbol CUP$Grm$result162 = new Symbol(74, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l21);
                return CUP$Grm$result162;
            case 162:
                int i467 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i468 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                ClassDecl a72 = (ClassDecl) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList34 = new LinkedList();
                if (class$polyglot$ast$Stmt == null) {
                    cls34 = class$("polyglot.ast.Stmt");
                    class$polyglot$ast$Stmt = cls34;
                } else {
                    cls34 = class$polyglot$ast$Stmt;
                }
                List l22 = new TypedList(linkedList34, cls34, false);
                l22.add(this.parser.nf.LocalClassDecl(this.parser.pos(a72), a72));
                Symbol CUP$Grm$result163 = new Symbol(74, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l22);
                return CUP$Grm$result163;
            case 163:
                int i469 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i470 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Symbol CUP$Grm$result164 = new Symbol(75, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value);
                return CUP$Grm$result164;
            case 164:
                int i471 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i472 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                TypeNode a73 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i473 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i474 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                List b36 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                List RESULT80 = this.parser.variableDeclarators(a73, b36, Flags.NONE);
                Symbol CUP$Grm$result165 = new Symbol(76, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT80);
                return CUP$Grm$result165;
            case 165:
                int i475 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i476 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                TypeNode a74 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i477 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i478 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                List b37 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                List RESULT81 = this.parser.variableDeclarators(a74, b37, Flags.FINAL);
                Symbol CUP$Grm$result166 = new Symbol(76, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT81);
                return CUP$Grm$result166;
            case 166:
                int i479 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i480 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result167 = new Symbol(77, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Stmt) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result167;
            case 167:
                int i481 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i482 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result168 = new Symbol(77, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Labeled) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result168;
            case 168:
                int i483 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i484 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result169 = new Symbol(77, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (If) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result169;
            case 169:
                int i485 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i486 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result170 = new Symbol(77, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (If) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result170;
            case 170:
                int i487 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i488 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result171 = new Symbol(77, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (While) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result171;
            case 171:
                int i489 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i490 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result172 = new Symbol(77, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (For) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result172;
            case 172:
                int i491 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i492 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Stmt RESULT82 = this.parser.nf.Empty(this.parser.pos((Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value));
                Symbol CUP$Grm$result173 = new Symbol(77, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT82);
                return CUP$Grm$result173;
            case 173:
                int i493 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i494 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result174 = new Symbol(78, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Stmt) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result174;
            case 174:
                int i495 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i496 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result175 = new Symbol(78, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Labeled) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result175;
            case 175:
                int i497 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i498 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result176 = new Symbol(78, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (If) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result176;
            case 176:
                int i499 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i500 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result177 = new Symbol(78, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (While) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result177;
            case 177:
                int i501 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i502 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result178 = new Symbol(78, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (For) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result178;
            case 178:
                int i503 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i504 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result179 = new Symbol(79, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Block) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result179;
            case 179:
                int i505 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i506 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result180 = new Symbol(79, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Empty) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result180;
            case 180:
                int i507 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i508 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result181 = new Symbol(79, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Stmt) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result181;
            case 181:
                int i509 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i510 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result182 = new Symbol(79, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Switch) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result182;
            case 182:
                int i511 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i512 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result183 = new Symbol(79, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Do) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result183;
            case 183:
                int i513 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i514 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result184 = new Symbol(79, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Branch) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result184;
            case 184:
                int i515 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i516 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result185 = new Symbol(79, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Branch) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result185;
            case 185:
                int i517 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i518 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result186 = new Symbol(79, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Return) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result186;
            case 186:
                int i519 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i520 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result187 = new Symbol(79, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Synchronized) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result187;
            case 187:
                int i521 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i522 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result188 = new Symbol(79, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Throw) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result188;
            case 188:
                int i523 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i524 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result189 = new Symbol(79, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Try) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result189;
            case 189:
                int i525 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i526 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result190 = new Symbol(79, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Assert) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result190;
            case 190:
                int i527 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i528 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Empty RESULT83 = this.parser.nf.Empty(this.parser.pos((Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value));
                Symbol CUP$Grm$result191 = new Symbol(80, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT83);
                return CUP$Grm$result191;
            case 191:
                int i529 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i530 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Identifier a75 = (Identifier) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i531 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i532 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Stmt b38 = (Stmt) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Labeled RESULT84 = this.parser.nf.Labeled(this.parser.pos(a75, b38), a75.getIdentifier(), b38);
                Symbol CUP$Grm$result192 = new Symbol(81, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT84);
                return CUP$Grm$result192;
            case 192:
                int i533 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i534 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Identifier a76 = (Identifier) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i535 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i536 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Stmt b39 = (Stmt) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Labeled RESULT85 = this.parser.nf.Labeled(this.parser.pos(a76, b39), a76.getIdentifier(), b39);
                Symbol CUP$Grm$result193 = new Symbol(82, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT85);
                return CUP$Grm$result193;
            case 193:
                int i537 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i538 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Expr a77 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i539 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i540 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d19 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Stmt RESULT86 = this.parser.nf.Eval(this.parser.pos(a77, d19), a77);
                Symbol CUP$Grm$result194 = new Symbol(83, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT86);
                return CUP$Grm$result194;
            case 194:
                int i541 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i542 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result195 = new Symbol(84, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result195;
            case 195:
                int i543 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i544 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result196 = new Symbol(84, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Unary) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result196;
            case 196:
                int i545 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i546 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result197 = new Symbol(84, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Unary) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result197;
            case 197:
                int i547 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i548 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result198 = new Symbol(84, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Unary) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result198;
            case 198:
                int i549 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i550 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result199 = new Symbol(84, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Unary) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result199;
            case 199:
                int i551 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i552 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result200 = new Symbol(84, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Call) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result200;
            case 200:
                int i553 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i554 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result201 = new Symbol(84, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result201;
            case 201:
                int i555 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i556 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Token n17 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i557 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i558 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a78 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i559 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i560 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Stmt b40 = (Stmt) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                If RESULT87 = this.parser.nf.If(this.parser.pos(n17, b40), a78, b40);
                Symbol CUP$Grm$result202 = new Symbol(85, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT87);
                return CUP$Grm$result202;
            case 202:
                int i561 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
                int i562 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
                Token n18 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
                int i563 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i564 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Expr a79 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i565 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i566 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Stmt b41 = (Stmt) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i567 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i568 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Stmt c16 = (Stmt) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                If RESULT88 = this.parser.nf.If(this.parser.pos(n18, c16), a79, b41, c16);
                Symbol CUP$Grm$result203 = new Symbol(86, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT88);
                return CUP$Grm$result203;
            case 203:
                int i569 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
                int i570 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
                Token n19 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
                int i571 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i572 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Expr a80 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i573 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i574 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Stmt b42 = (Stmt) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i575 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i576 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Stmt c17 = (Stmt) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                If RESULT89 = this.parser.nf.If(this.parser.pos(n19, c17), a80, b42, c17);
                Symbol CUP$Grm$result204 = new Symbol(87, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT89);
                return CUP$Grm$result204;
            case 204:
                int i577 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i578 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Token n20 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i579 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i580 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a81 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i581 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i582 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                List b43 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Switch RESULT90 = this.parser.nf.Switch(this.parser.pos(n20, b43), a81, b43);
                Symbol CUP$Grm$result205 = new Symbol(88, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT90);
                return CUP$Grm$result205;
            case 205:
                int i583 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i584 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List a82 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i585 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i586 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List b44 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                a82.addAll(b44);
                Symbol CUP$Grm$result206 = new Symbol(89, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a82);
                return CUP$Grm$result206;
            case 206:
                int i587 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i588 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Symbol CUP$Grm$result207 = new Symbol(89, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value);
                return CUP$Grm$result207;
            case 207:
                int i589 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i590 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Symbol CUP$Grm$result208 = new Symbol(89, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value);
                return CUP$Grm$result208;
            case 208:
                LinkedList linkedList35 = new LinkedList();
                if (class$polyglot$ast$SwitchElement == null) {
                    cls35 = class$("polyglot.ast.SwitchElement");
                    class$polyglot$ast$SwitchElement = cls35;
                } else {
                    cls35 = class$polyglot$ast$SwitchElement;
                }
                List RESULT91 = new TypedList(linkedList35, cls35, false);
                Symbol CUP$Grm$result209 = new Symbol(89, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT91);
                return CUP$Grm$result209;
            case 209:
                int i591 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i592 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result210 = new Symbol(90, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result210;
            case 210:
                int i593 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i594 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List a83 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i595 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i596 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                List b45 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a83.addAll(b45);
                Symbol CUP$Grm$result211 = new Symbol(90, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a83);
                return CUP$Grm$result211;
            case 211:
                int i597 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i598 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List a84 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i599 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i600 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                List b46 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList36 = new LinkedList();
                if (class$polyglot$ast$SwitchElement == null) {
                    cls36 = class$("polyglot.ast.SwitchElement");
                    class$polyglot$ast$SwitchElement = cls36;
                } else {
                    cls36 = class$polyglot$ast$SwitchElement;
                }
                List l23 = new TypedList(linkedList36, cls36, false);
                l23.addAll(a84);
                l23.add(this.parser.nf.SwitchBlock(this.parser.pos(a84, b46), b46));
                Symbol CUP$Grm$result212 = new Symbol(91, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l23);
                return CUP$Grm$result212;
            case 212:
                int i601 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i602 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Case a85 = (Case) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList37 = new LinkedList();
                if (class$polyglot$ast$Case == null) {
                    cls37 = class$("polyglot.ast.Case");
                    class$polyglot$ast$Case = cls37;
                } else {
                    cls37 = class$polyglot$ast$Case;
                }
                List l24 = new TypedList(linkedList37, cls37, false);
                l24.add(a85);
                Symbol CUP$Grm$result213 = new Symbol(92, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l24);
                return CUP$Grm$result213;
            case 213:
                int i603 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i604 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List a86 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i605 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i606 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Case b47 = (Case) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a86.add(b47);
                Symbol CUP$Grm$result214 = new Symbol(92, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a86);
                return CUP$Grm$result214;
            case 214:
                int i607 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i608 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token n21 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i609 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i610 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Expr a87 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i611 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i612 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d20 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Case RESULT92 = this.parser.nf.Case(this.parser.pos(n21, d20), a87);
                Symbol CUP$Grm$result215 = new Symbol(93, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT92);
                return CUP$Grm$result215;
            case 215:
                int i613 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i614 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Token n22 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i615 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i616 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d21 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Case RESULT93 = this.parser.nf.Default(this.parser.pos(n22, d21));
                Symbol CUP$Grm$result216 = new Symbol(93, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT93);
                return CUP$Grm$result216;
            case 216:
                int i617 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i618 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Token n23 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i619 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i620 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a88 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i621 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i622 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Stmt b48 = (Stmt) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                While RESULT94 = this.parser.nf.While(this.parser.pos(n23, b48), a88, b48);
                Symbol CUP$Grm$result217 = new Symbol(94, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT94);
                return CUP$Grm$result217;
            case 217:
                int i623 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i624 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Token n24 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i625 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i626 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a89 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i627 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i628 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Stmt b49 = (Stmt) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                While RESULT95 = this.parser.nf.While(this.parser.pos(n24, b49), a89, b49);
                Symbol CUP$Grm$result218 = new Symbol(95, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT95);
                return CUP$Grm$result218;
            case 218:
                int i629 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
                int i630 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
                Token n25 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
                int i631 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
                int i632 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
                Stmt a90 = (Stmt) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
                int i633 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i634 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr b50 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i635 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i636 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d22 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Do RESULT96 = this.parser.nf.Do(this.parser.pos(n25, d22), a90, b50);
                Symbol CUP$Grm$result219 = new Symbol(96, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT96);
                return CUP$Grm$result219;
            case 219:
                int i637 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 8)).left;
                int i638 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 8)).right;
                Token n26 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 8)).value;
                int i639 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
                int i640 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
                List a91 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
                int i641 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i642 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Expr b51 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i643 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i644 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Token e5 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i645 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i646 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List c18 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i647 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i648 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Stmt d23 = (Stmt) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                For RESULT97 = this.parser.nf.For(this.parser.pos(n26, e5), a91, b51, c18, d23);
                Symbol CUP$Grm$result220 = new Symbol(97, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 8)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT97);
                return CUP$Grm$result220;
            case 220:
                int i649 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 8)).left;
                int i650 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 8)).right;
                Token n27 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 8)).value;
                int i651 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
                int i652 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
                List a92 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
                int i653 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i654 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Expr b52 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i655 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i656 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Token e6 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i657 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i658 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List c19 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i659 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i660 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Stmt d24 = (Stmt) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                For RESULT98 = this.parser.nf.For(this.parser.pos(n27, e6), a92, b52, c19, d24);
                Symbol CUP$Grm$result221 = new Symbol(98, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 8)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT98);
                return CUP$Grm$result221;
            case 221:
                LinkedList linkedList38 = new LinkedList();
                if (class$polyglot$ast$ForInit == null) {
                    cls38 = class$("polyglot.ast.ForInit");
                    class$polyglot$ast$ForInit = cls38;
                } else {
                    cls38 = class$polyglot$ast$ForInit;
                }
                List RESULT99 = new TypedList(linkedList38, cls38, false);
                Symbol CUP$Grm$result222 = new Symbol(99, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT99);
                return CUP$Grm$result222;
            case 222:
                int i661 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i662 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result223 = new Symbol(99, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result223;
            case 223:
                int i663 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i664 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result224 = new Symbol(100, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result224;
            case 224:
                int i665 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i666 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                List a93 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList39 = new LinkedList();
                if (class$polyglot$ast$ForInit == null) {
                    cls39 = class$("polyglot.ast.ForInit");
                    class$polyglot$ast$ForInit = cls39;
                } else {
                    cls39 = class$polyglot$ast$ForInit;
                }
                List l25 = new TypedList(linkedList39, cls39, false);
                l25.addAll(a93);
                Symbol CUP$Grm$result225 = new Symbol(100, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l25);
                return CUP$Grm$result225;
            case 225:
                LinkedList linkedList40 = new LinkedList();
                if (class$polyglot$ast$ForUpdate == null) {
                    cls40 = class$("polyglot.ast.ForUpdate");
                    class$polyglot$ast$ForUpdate = cls40;
                } else {
                    cls40 = class$polyglot$ast$ForUpdate;
                }
                List RESULT100 = new TypedList(linkedList40, cls40, false);
                Symbol CUP$Grm$result226 = new Symbol(101, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT100);
                return CUP$Grm$result226;
            case 226:
                int i667 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i668 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result227 = new Symbol(101, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result227;
            case jasmin.sym.i_ixor /* 227 */:
                int i669 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i670 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result228 = new Symbol(102, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result228;
            case jasmin.sym.i_jsr /* 228 */:
                int i671 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i672 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr a94 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList41 = new LinkedList();
                if (class$polyglot$ast$Eval == null) {
                    cls41 = class$("polyglot.ast.Eval");
                    class$polyglot$ast$Eval = cls41;
                } else {
                    cls41 = class$polyglot$ast$Eval;
                }
                List l26 = new TypedList(linkedList41, cls41, false);
                l26.add(this.parser.nf.Eval(this.parser.pos(a94), a94));
                Symbol CUP$Grm$result229 = new Symbol(103, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l26);
                return CUP$Grm$result229;
            case jasmin.sym.i_jsr_w /* 229 */:
                int i673 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i674 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List a95 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i675 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i676 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b53 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a95.add(this.parser.nf.Eval(this.parser.pos(a95, b53, b53), b53));
                Symbol CUP$Grm$result230 = new Symbol(103, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a95);
                return CUP$Grm$result230;
            case jasmin.sym.i_l2d /* 230 */:
                Symbol CUP$Grm$result231 = new Symbol(104, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Object) null);
                return CUP$Grm$result231;
            case jasmin.sym.i_l2f /* 231 */:
                int i677 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i678 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Identifier a96 = (Identifier) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Name RESULT101 = new Name(this.parser, this.parser.pos(a96), a96.getIdentifier());
                Symbol CUP$Grm$result232 = new Symbol(104, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT101);
                return CUP$Grm$result232;
            case 232:
                int i679 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i680 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token n28 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i681 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i682 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Name a97 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i683 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i684 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d25 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                if (a97 == null) {
                    RESULT = this.parser.nf.Break(this.parser.pos(n28, d25));
                } else {
                    RESULT = this.parser.nf.Break(this.parser.pos(n28, d25), a97.toString());
                }
                Symbol CUP$Grm$result233 = new Symbol(105, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
                return CUP$Grm$result233;
            case 233:
                int i685 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i686 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token n29 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i687 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i688 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Name a98 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i689 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i690 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d26 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                if (a98 == null) {
                    RESULT2 = this.parser.nf.Continue(this.parser.pos(n29, d26));
                } else {
                    RESULT2 = this.parser.nf.Continue(this.parser.pos(n29, d26), a98.toString());
                }
                Symbol CUP$Grm$result234 = new Symbol(106, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT2);
                return CUP$Grm$result234;
            case 234:
                int i691 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i692 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token n30 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i693 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i694 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Expr a99 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i695 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i696 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d27 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Return RESULT102 = this.parser.nf.Return(this.parser.pos(n30, d27), a99);
                Symbol CUP$Grm$result235 = new Symbol(107, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT102);
                return CUP$Grm$result235;
            case 235:
                int i697 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i698 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token n31 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i699 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i700 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Expr a100 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i701 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i702 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d28 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Throw RESULT103 = this.parser.nf.Throw(this.parser.pos(n31, d28), a100);
                Symbol CUP$Grm$result236 = new Symbol(108, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT103);
                return CUP$Grm$result236;
            case 236:
                int i703 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i704 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Token n32 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i705 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i706 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a101 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i707 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i708 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Block b54 = (Block) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Synchronized RESULT104 = this.parser.nf.Synchronized(this.parser.pos(n32, b54), a101, b54);
                Symbol CUP$Grm$result237 = new Symbol(109, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT104);
                return CUP$Grm$result237;
            case 237:
                int i709 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i710 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token n33 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i711 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i712 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Block a102 = (Block) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i713 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i714 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                List b55 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Try RESULT105 = this.parser.nf.Try(this.parser.pos(n33, b55), a102, b55);
                Symbol CUP$Grm$result238 = new Symbol(110, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT105);
                return CUP$Grm$result238;
            case 238:
                int i715 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i716 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Token n34 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i717 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i718 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Block a103 = (Block) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i719 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i720 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List b56 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i721 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i722 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Block c20 = (Block) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Try RESULT106 = this.parser.nf.Try(this.parser.pos(n34, c20), a103, b56, c20);
                Symbol CUP$Grm$result239 = new Symbol(110, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT106);
                return CUP$Grm$result239;
            case 239:
                LinkedList linkedList42 = new LinkedList();
                if (class$polyglot$ast$Catch == null) {
                    cls42 = class$("polyglot.ast.Catch");
                    class$polyglot$ast$Catch = cls42;
                } else {
                    cls42 = class$polyglot$ast$Catch;
                }
                List RESULT107 = new TypedList(linkedList42, cls42, false);
                Symbol CUP$Grm$result240 = new Symbol(111, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT107);
                return CUP$Grm$result240;
            case 240:
                int i723 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i724 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result241 = new Symbol(111, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result241;
            case jasmin.sym.i_ldc_w /* 241 */:
                int i725 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i726 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Catch a104 = (Catch) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList43 = new LinkedList();
                if (class$polyglot$ast$Catch == null) {
                    cls43 = class$("polyglot.ast.Catch");
                    class$polyglot$ast$Catch = cls43;
                } else {
                    cls43 = class$polyglot$ast$Catch;
                }
                List l27 = new TypedList(linkedList43, cls43, false);
                l27.add(a104);
                Symbol CUP$Grm$result242 = new Symbol(112, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l27);
                return CUP$Grm$result242;
            case 242:
                int i727 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i728 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List a105 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i729 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i730 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Catch b57 = (Catch) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a105.add(b57);
                Symbol CUP$Grm$result243 = new Symbol(112, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a105);
                return CUP$Grm$result243;
            case 243:
                int i731 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i732 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Token n35 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i733 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i734 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Formal a106 = (Formal) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i735 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i736 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Block b58 = (Block) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Catch RESULT108 = this.parser.nf.Catch(this.parser.pos(n35, b58), a106, b58);
                Symbol CUP$Grm$result244 = new Symbol(113, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT108);
                return CUP$Grm$result244;
            case 244:
                int i737 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i738 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result245 = new Symbol(114, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Block) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result245;
            case 245:
                int i739 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i740 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token x = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i741 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i742 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Expr a107 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i743 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i744 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d29 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Assert RESULT109 = this.parser.nf.Assert(this.parser.pos(x, d29), a107);
                Symbol CUP$Grm$result246 = new Symbol(115, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT109);
                return CUP$Grm$result246;
            case 246:
                int i745 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i746 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Token x2 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i747 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i748 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Expr a108 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i749 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i750 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Expr b59 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i751 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i752 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d30 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Assert RESULT110 = this.parser.nf.Assert(this.parser.pos(x2, d30), a108, b59);
                Symbol CUP$Grm$result247 = new Symbol(115, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT110);
                return CUP$Grm$result247;
            case 247:
                int i753 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i754 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result248 = new Symbol(116, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result248;
            case 248:
                int i755 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i756 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result249 = new Symbol(116, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (NewArray) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result249;
            case 249:
                int i757 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i758 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result250 = new Symbol(117, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Lit) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result250;
            case 250:
                int i759 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i760 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr RESULT111 = this.parser.nf.This(this.parser.pos((Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value));
                Symbol CUP$Grm$result251 = new Symbol(117, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT111);
                return CUP$Grm$result251;
            case 251:
                int i761 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i762 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Symbol CUP$Grm$result252 = new Symbol(117, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value);
                return CUP$Grm$result252;
            case 252:
                int i763 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i764 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result253 = new Symbol(117, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result253;
            case jasmin.sym.i_lrem /* 253 */:
                int i765 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i766 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result254 = new Symbol(117, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Field) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result254;
            case 254:
                int i767 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i768 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result255 = new Symbol(117, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Call) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result255;
            case 255:
                int i769 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i770 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result256 = new Symbol(117, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (ArrayAccess) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result256;
            case 256:
                int i771 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i772 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                TypeNode a109 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i773 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i774 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token n36 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT112 = this.parser.nf.ClassLit(this.parser.pos(a109, n36, n36), a109);
                Symbol CUP$Grm$result257 = new Symbol(117, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT112);
                return CUP$Grm$result257;
            case 257:
                int i775 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i776 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token a110 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i777 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i778 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token n37 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT113 = this.parser.nf.ClassLit(this.parser.pos(a110, n37, n37), this.parser.nf.CanonicalTypeNode(this.parser.pos(a110), this.parser.ts.Void()));
                Symbol CUP$Grm$result258 = new Symbol(117, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT113);
                return CUP$Grm$result258;
            case 258:
                int i779 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i780 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                TypeNode a111 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i781 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i782 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token n38 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT114 = this.parser.nf.ClassLit(this.parser.pos(a111, n38, n38), a111);
                Symbol CUP$Grm$result259 = new Symbol(117, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT114);
                return CUP$Grm$result259;
            case 259:
                int i783 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i784 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Name a112 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i785 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i786 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token n39 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT115 = this.parser.nf.ClassLit(this.parser.pos(a112, n39, n39), a112.toType());
                Symbol CUP$Grm$result260 = new Symbol(117, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT115);
                return CUP$Grm$result260;
            case 260:
                int i787 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i788 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Name a113 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i789 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i790 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token n40 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT116 = this.parser.nf.This(this.parser.pos(a113, n40, n40), a113.toType());
                Symbol CUP$Grm$result261 = new Symbol(117, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT116);
                return CUP$Grm$result261;
            case 261:
                int i791 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i792 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Token n41 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i793 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i794 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                TypeNode a114 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i795 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i796 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List b60 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i797 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i798 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d31 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT117 = this.parser.nf.New(this.parser.pos(n41, d31), a114, b60);
                Symbol CUP$Grm$result262 = new Symbol(118, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT117);
                return CUP$Grm$result262;
            case 262:
                int i799 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
                int i800 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
                Token n42 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
                int i801 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i802 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                TypeNode a115 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i803 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i804 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List b61 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i805 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i806 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                ClassBody c21 = (ClassBody) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT118 = this.parser.nf.New(this.parser.pos(n42, c21), a115, b61, c21);
                Symbol CUP$Grm$result263 = new Symbol(118, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT118);
                return CUP$Grm$result263;
            case 263:
                int i807 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
                int i808 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
                Expr a116 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
                int i809 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i810 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Name b62 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i811 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i812 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List c22 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i813 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i814 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d32 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT119 = this.parser.nf.New(this.parser.pos(a116, d32), a116, b62.toType(), c22);
                Symbol CUP$Grm$result264 = new Symbol(118, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT119);
                return CUP$Grm$result264;
            case 264:
                int i815 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).left;
                int i816 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).right;
                Expr a117 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).value;
                int i817 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i818 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Name b63 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i819 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i820 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List c23 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i821 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i822 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                ClassBody d33 = (ClassBody) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT120 = this.parser.nf.New(this.parser.pos(a117, d33), a117, b63.toType(), c23, d33);
                Symbol CUP$Grm$result265 = new Symbol(118, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT120);
                return CUP$Grm$result265;
            case 265:
                int i823 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
                int i824 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
                Name a118 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
                int i825 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i826 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Name b64 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i827 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i828 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List c24 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i829 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i830 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d34 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT121 = this.parser.nf.New(this.parser.pos(a118, d34), a118.toExpr(), b64.toType(), c24);
                Symbol CUP$Grm$result266 = new Symbol(118, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT121);
                return CUP$Grm$result266;
            case 266:
                int i831 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).left;
                int i832 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).right;
                Name a119 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).value;
                int i833 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i834 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Name b65 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i835 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i836 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List c25 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i837 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i838 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                ClassBody d35 = (ClassBody) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT122 = this.parser.nf.New(this.parser.pos(a119, d35), a119.toExpr(), b65.toType(), c25, d35);
                Symbol CUP$Grm$result267 = new Symbol(118, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT122);
                return CUP$Grm$result267;
            case 267:
                LinkedList linkedList44 = new LinkedList();
                if (class$polyglot$ast$Expr == null) {
                    cls44 = class$("polyglot.ast.Expr");
                    class$polyglot$ast$Expr = cls44;
                } else {
                    cls44 = class$polyglot$ast$Expr;
                }
                List RESULT123 = new TypedList(linkedList44, cls44, false);
                Symbol CUP$Grm$result268 = new Symbol(119, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT123);
                return CUP$Grm$result268;
            case 268:
                int i839 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i840 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result269 = new Symbol(119, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result269;
            case 269:
                int i841 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i842 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr a120 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList45 = new LinkedList();
                if (class$polyglot$ast$Expr == null) {
                    cls45 = class$("polyglot.ast.Expr");
                    class$polyglot$ast$Expr = cls45;
                } else {
                    cls45 = class$polyglot$ast$Expr;
                }
                List l28 = new TypedList(linkedList45, cls45, false);
                l28.add(a120);
                Symbol CUP$Grm$result270 = new Symbol(120, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l28);
                return CUP$Grm$result270;
            case 270:
                int i843 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i844 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                List a121 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i845 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i846 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b66 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a121.add(b66);
                Symbol CUP$Grm$result271 = new Symbol(120, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a121);
                return CUP$Grm$result271;
            case 271:
                int i847 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i848 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Token n43 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i849 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i850 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                TypeNode a122 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i851 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i852 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List b67 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i853 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i854 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Integer c26 = (Integer) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                NewArray RESULT124 = this.parser.nf.NewArray(this.parser.pos(n43, b67), a122, b67, c26.intValue());
                Symbol CUP$Grm$result272 = new Symbol(121, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT124);
                return CUP$Grm$result272;
            case 272:
                int i855 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i856 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Token n44 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i857 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i858 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                TypeNode a123 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i859 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i860 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List b68 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i861 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i862 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Integer c27 = (Integer) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                NewArray RESULT125 = this.parser.nf.NewArray(this.parser.pos(n44, b68), a123, b68, c27.intValue());
                Symbol CUP$Grm$result273 = new Symbol(121, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT125);
                return CUP$Grm$result273;
            case 273:
                int i863 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i864 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Token n45 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i865 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i866 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                TypeNode a124 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i867 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i868 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Integer b69 = (Integer) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i869 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i870 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                ArrayInit c28 = (ArrayInit) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                NewArray RESULT126 = this.parser.nf.NewArray(this.parser.pos(n45, c28), a124, b69.intValue(), c28);
                Symbol CUP$Grm$result274 = new Symbol(121, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT126);
                return CUP$Grm$result274;
            case 274:
                int i871 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i872 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Token n46 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i873 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i874 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                TypeNode a125 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i875 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i876 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Integer b70 = (Integer) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i877 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i878 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                ArrayInit c29 = (ArrayInit) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                NewArray RESULT127 = this.parser.nf.NewArray(this.parser.pos(n46, c29), a125, b70.intValue(), c29);
                Symbol CUP$Grm$result275 = new Symbol(121, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT127);
                return CUP$Grm$result275;
            case 275:
                int i879 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i880 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr a126 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                LinkedList linkedList46 = new LinkedList();
                if (class$polyglot$ast$Expr == null) {
                    cls46 = class$("polyglot.ast.Expr");
                    class$polyglot$ast$Expr = cls46;
                } else {
                    cls46 = class$polyglot$ast$Expr;
                }
                List l29 = new TypedList(linkedList46, cls46, false);
                l29.add(a126);
                Symbol CUP$Grm$result276 = new Symbol(122, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l29);
                return CUP$Grm$result276;
            case 276:
                int i881 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i882 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List a127 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i883 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i884 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b71 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                a127.add(b71);
                Symbol CUP$Grm$result277 = new Symbol(122, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a127);
                return CUP$Grm$result277;
            case 277:
                int i885 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i886 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token x3 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i887 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i888 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Expr a128 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i889 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i890 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token y = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT128 = (Expr) a128.position(this.parser.pos(x3, y, a128));
                Symbol CUP$Grm$result278 = new Symbol(123, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT128);
                return CUP$Grm$result278;
            case 278:
                Integer RESULT129 = new Integer(0);
                Symbol CUP$Grm$result279 = new Symbol(124, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT129);
                return CUP$Grm$result279;
            case 279:
                int i891 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i892 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result280 = new Symbol(124, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Integer) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result280;
            case 280:
                Integer RESULT130 = new Integer(1);
                Symbol CUP$Grm$result281 = new Symbol(125, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT130);
                return CUP$Grm$result281;
            case 281:
                int i893 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i894 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Integer RESULT131 = new Integer(((Integer) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value).intValue() + 1);
                Symbol CUP$Grm$result282 = new Symbol(125, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT131);
                return CUP$Grm$result282;
            case 282:
                int i895 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i896 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a129 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i897 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i898 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Identifier b72 = (Identifier) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Field RESULT132 = this.parser.nf.Field(this.parser.pos(a129, b72, b72), a129, b72.getIdentifier());
                Symbol CUP$Grm$result283 = new Symbol(126, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT132);
                return CUP$Grm$result283;
            case 283:
                int i899 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i900 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token n47 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i901 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i902 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Identifier a130 = (Identifier) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Field RESULT133 = this.parser.nf.Field(this.parser.pos(a130), this.parser.nf.Super(this.parser.pos(n47)), a130.getIdentifier());
                Symbol CUP$Grm$result284 = new Symbol(126, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT133);
                return CUP$Grm$result284;
            case 284:
                int i903 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i904 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Name a131 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i905 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i906 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Token n48 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i907 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i908 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Identifier b73 = (Identifier) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Field RESULT134 = this.parser.nf.Field(this.parser.pos(b73), this.parser.nf.Super(this.parser.pos(n48), a131.toType()), b73.getIdentifier());
                Symbol CUP$Grm$result285 = new Symbol(126, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT134);
                return CUP$Grm$result285;
            case 285:
                int i909 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i910 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Name a132 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i911 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i912 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List b74 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i913 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i914 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d36 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Call RESULT135 = this.parser.nf.Call(this.parser.pos(a132, d36), a132.prefix == null ? null : a132.prefix.toReceiver(), a132.name, b74);
                Symbol CUP$Grm$result286 = new Symbol(127, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT135);
                return CUP$Grm$result286;
            case 286:
                int i915 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
                int i916 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
                Expr a133 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
                int i917 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i918 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Identifier b75 = (Identifier) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i919 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i920 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List c30 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i921 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i922 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d37 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Call RESULT136 = this.parser.nf.Call(this.parser.pos(b75, d37), a133, b75.getIdentifier(), c30);
                Symbol CUP$Grm$result287 = new Symbol(127, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT136);
                return CUP$Grm$result287;
            case 287:
                int i923 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
                int i924 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
                Token a134 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
                int i925 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i926 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Identifier b76 = (Identifier) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i927 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i928 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List c31 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i929 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i930 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d38 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Call RESULT137 = this.parser.nf.Call(this.parser.pos(a134, d38, b76), this.parser.nf.Super(this.parser.pos(a134)), b76.getIdentifier(), c31);
                Symbol CUP$Grm$result288 = new Symbol(127, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT137);
                return CUP$Grm$result288;
            case Parser.IMPLEMENTS /* 288 */:
                int i931 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).left;
                int i932 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).right;
                Name a135 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).value;
                int i933 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
                int i934 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
                Token n49 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
                int i935 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i936 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Identifier b77 = (Identifier) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i937 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i938 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                List c32 = (List) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i939 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i940 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d39 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Call RESULT138 = this.parser.nf.Call(this.parser.pos(b77, d39), this.parser.nf.Super(this.parser.pos(n49), a135.toType()), b77.getIdentifier(), c32);
                Symbol CUP$Grm$result289 = new Symbol(127, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT138);
                return CUP$Grm$result289;
            case Parser.SUPER /* 289 */:
                int i941 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i942 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Name a136 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i943 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i944 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Expr b78 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i945 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i946 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d40 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                ArrayAccess RESULT139 = this.parser.nf.ArrayAccess(this.parser.pos(a136, d40), a136.toExpr(), b78);
                Symbol CUP$Grm$result290 = new Symbol(128, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT139);
                return CUP$Grm$result290;
            case Parser.DEFAULT /* 290 */:
                int i947 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i948 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Expr a137 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i949 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i950 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Expr b79 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i951 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i952 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token d41 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                ArrayAccess RESULT140 = this.parser.nf.ArrayAccess(this.parser.pos(a137, d41), a137, b79);
                Symbol CUP$Grm$result291 = new Symbol(128, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT140);
                return CUP$Grm$result291;
            case Parser.BRACEOPEN /* 291 */:
                int i953 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i954 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result292 = new Symbol(129, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result292;
            case Parser.BRACECLOSE /* 292 */:
                int i955 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i956 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr RESULT141 = ((Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value).toExpr();
                Symbol CUP$Grm$result293 = new Symbol(129, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT141);
                return CUP$Grm$result293;
            case Parser.SQUAREOPEN /* 293 */:
                int i957 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i958 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result294 = new Symbol(129, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Unary) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result294;
            case Parser.SQUARECLOSE /* 294 */:
                int i959 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i960 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result295 = new Symbol(129, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Unary) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result295;
            case Parser.PARENOPEN /* 295 */:
                int i961 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i962 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Expr a138 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i963 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i964 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token b80 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Unary RESULT142 = this.parser.nf.Unary(this.parser.pos(a138, b80), a138, Unary.POST_INC);
                Symbol CUP$Grm$result296 = new Symbol(130, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT142);
                return CUP$Grm$result296;
            case Parser.PARENCLOSE /* 296 */:
                int i965 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i966 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Expr a139 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i967 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i968 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Token b81 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Unary RESULT143 = this.parser.nf.Unary(this.parser.pos(a139, b81), a139, Unary.POST_DEC);
                Symbol CUP$Grm$result297 = new Symbol(131, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT143);
                return CUP$Grm$result297;
            case Parser.LESSTHAN /* 297 */:
                int i969 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i970 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result298 = new Symbol(132, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Unary) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result298;
            case Parser.GREATERTHAN /* 298 */:
                int i971 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i972 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result299 = new Symbol(132, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Unary) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result299;
            case Parser.LESSEQUALS /* 299 */:
                int i973 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i974 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Token b82 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i975 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i976 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr a140 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT144 = this.parser.nf.Unary(this.parser.pos(b82, a140, a140), Unary.POS, a140);
                Symbol CUP$Grm$result300 = new Symbol(132, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT144);
                return CUP$Grm$result300;
            case 300:
                int i977 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i978 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Token b83 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i979 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i980 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr a141 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT145 = this.parser.nf.Unary(this.parser.pos(b83, a141, a141), Unary.NEG, a141);
                Symbol CUP$Grm$result301 = new Symbol(132, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT145);
                return CUP$Grm$result301;
            case 301:
                int i981 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i982 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Token b84 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i983 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i984 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Lit a142 = (Lit) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT146 = this.parser.nf.Unary(this.parser.pos(b84, a142, a142), Unary.NEG, a142);
                Symbol CUP$Grm$result302 = new Symbol(132, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT146);
                return CUP$Grm$result302;
            case 302:
                int i985 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i986 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result303 = new Symbol(132, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result303;
            case 303:
                int i987 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i988 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Token b85 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i989 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i990 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr a143 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Unary RESULT147 = this.parser.nf.Unary(this.parser.pos(b85, a143, a143), Unary.PRE_INC, a143);
                Symbol CUP$Grm$result304 = new Symbol(134, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT147);
                return CUP$Grm$result304;
            case 304:
                int i991 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i992 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Token b86 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i993 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i994 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr a144 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Unary RESULT148 = this.parser.nf.Unary(this.parser.pos(b86, a144, a144), Unary.PRE_DEC, a144);
                Symbol CUP$Grm$result305 = new Symbol(135, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT148);
                return CUP$Grm$result305;
            case 305:
                int i995 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i996 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result306 = new Symbol(133, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result306;
            case 306:
                int i997 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i998 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Token b87 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i999 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1000 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr a145 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT149 = this.parser.nf.Unary(this.parser.pos(b87, a145, a145), Unary.BIT_NOT, a145);
                Symbol CUP$Grm$result307 = new Symbol(133, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT149);
                return CUP$Grm$result307;
            case 307:
                int i1001 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i1002 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Token b88 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i1003 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1004 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr a146 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT150 = this.parser.nf.Unary(this.parser.pos(b88, a146, a146), Unary.NOT, a146);
                Symbol CUP$Grm$result308 = new Symbol(133, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT150);
                return CUP$Grm$result308;
            case 308:
                int i1005 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1006 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result309 = new Symbol(133, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Cast) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result309;
            case 309:
                int i1007 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i1008 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Token p = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i1009 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i1010 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                TypeNode a147 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i1011 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1012 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Integer b89 = (Integer) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1013 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1014 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr c33 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Cast RESULT151 = this.parser.nf.Cast(this.parser.pos(p, c33, a147), this.parser.array(a147, b89.intValue()), c33);
                Symbol CUP$Grm$result310 = new Symbol(136, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT151);
                return CUP$Grm$result310;
            case 310:
                int i1015 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i1016 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Token p2 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i1017 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1018 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a148 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1019 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1020 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b90 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Cast RESULT152 = this.parser.nf.Cast(this.parser.pos(p2, b90, a148), this.parser.exprToType(a148), b90);
                Symbol CUP$Grm$result311 = new Symbol(136, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT152);
                return CUP$Grm$result311;
            case 311:
                int i1021 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i1022 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Token p3 = (Token) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i1023 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
                int i1024 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
                Name a149 = (Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
                int i1025 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1026 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Integer b91 = (Integer) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1027 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1028 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr c34 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Cast RESULT153 = this.parser.nf.Cast(this.parser.pos(p3, c34, a149), this.parser.array(a149.toType(), b91.intValue()), c34);
                Symbol CUP$Grm$result312 = new Symbol(136, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT153);
                return CUP$Grm$result312;
            case 312:
                int i1029 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1030 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result313 = new Symbol(137, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result313;
            case 313:
                int i1031 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1032 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a150 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1033 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1034 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b92 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT154 = this.parser.nf.Binary(this.parser.pos(a150, b92), a150, Binary.MUL, b92);
                Symbol CUP$Grm$result314 = new Symbol(137, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT154);
                return CUP$Grm$result314;
            case 314:
                int i1035 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1036 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a151 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1037 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1038 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b93 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT155 = this.parser.nf.Binary(this.parser.pos(a151, b93), a151, Binary.DIV, b93);
                Symbol CUP$Grm$result315 = new Symbol(137, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT155);
                return CUP$Grm$result315;
            case 315:
                int i1039 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1040 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a152 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1041 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1042 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b94 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT156 = this.parser.nf.Binary(this.parser.pos(a152, b94), a152, Binary.MOD, b94);
                Symbol CUP$Grm$result316 = new Symbol(137, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT156);
                return CUP$Grm$result316;
            case 316:
                int i1043 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1044 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result317 = new Symbol(138, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result317;
            case 317:
                int i1045 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1046 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a153 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1047 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1048 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b95 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT157 = this.parser.nf.Binary(this.parser.pos(a153, b95), a153, Binary.ADD, b95);
                Symbol CUP$Grm$result318 = new Symbol(138, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT157);
                return CUP$Grm$result318;
            case 318:
                int i1049 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1050 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a154 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1051 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1052 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b96 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT158 = this.parser.nf.Binary(this.parser.pos(a154, b96), a154, Binary.SUB, b96);
                Symbol CUP$Grm$result319 = new Symbol(138, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT158);
                return CUP$Grm$result319;
            case 319:
                int i1053 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1054 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result320 = new Symbol(139, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result320;
            case 320:
                int i1055 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1056 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a155 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1057 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1058 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b97 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT159 = this.parser.nf.Binary(this.parser.pos(a155, b97), a155, Binary.SHL, b97);
                Symbol CUP$Grm$result321 = new Symbol(139, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT159);
                return CUP$Grm$result321;
            case 321:
                int i1059 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1060 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a156 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1061 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1062 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b98 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT160 = this.parser.nf.Binary(this.parser.pos(a156, b98), a156, Binary.SHR, b98);
                Symbol CUP$Grm$result322 = new Symbol(139, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT160);
                return CUP$Grm$result322;
            case 322:
                int i1063 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1064 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a157 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1065 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1066 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b99 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT161 = this.parser.nf.Binary(this.parser.pos(a157, b99), a157, Binary.USHR, b99);
                Symbol CUP$Grm$result323 = new Symbol(139, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT161);
                return CUP$Grm$result323;
            case 323:
                int i1067 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1068 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result324 = new Symbol(140, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result324;
            case 324:
                int i1069 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1070 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a158 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1071 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1072 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b100 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT162 = this.parser.nf.Binary(this.parser.pos(a158, b100), a158, Binary.LT, b100);
                Symbol CUP$Grm$result325 = new Symbol(140, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT162);
                return CUP$Grm$result325;
            case 325:
                int i1073 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1074 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a159 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1075 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1076 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b101 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT163 = this.parser.nf.Binary(this.parser.pos(a159, b101), a159, Binary.GT, b101);
                Symbol CUP$Grm$result326 = new Symbol(140, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT163);
                return CUP$Grm$result326;
            case 326:
                int i1077 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1078 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a160 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1079 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1080 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b102 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT164 = this.parser.nf.Binary(this.parser.pos(a160, b102), a160, Binary.LE, b102);
                Symbol CUP$Grm$result327 = new Symbol(140, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT164);
                return CUP$Grm$result327;
            case 327:
                int i1081 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1082 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a161 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1083 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1084 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b103 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT165 = this.parser.nf.Binary(this.parser.pos(a161, b103), a161, Binary.GE, b103);
                Symbol CUP$Grm$result328 = new Symbol(140, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT165);
                return CUP$Grm$result328;
            case 328:
                int i1085 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1086 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a162 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1087 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1088 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                TypeNode b104 = (TypeNode) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT166 = this.parser.nf.Instanceof(this.parser.pos(a162, b104), a162, b104);
                Symbol CUP$Grm$result329 = new Symbol(140, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT166);
                return CUP$Grm$result329;
            case 329:
                int i1089 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1090 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result330 = new Symbol(141, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result330;
            case 330:
                int i1091 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1092 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a163 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1093 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1094 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b105 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT167 = this.parser.nf.Binary(this.parser.pos(a163, b105), a163, Binary.EQ, b105);
                Symbol CUP$Grm$result331 = new Symbol(141, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT167);
                return CUP$Grm$result331;
            case 331:
                int i1095 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1096 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a164 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1097 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1098 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b106 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT168 = this.parser.nf.Binary(this.parser.pos(a164, b106), a164, Binary.NE, b106);
                Symbol CUP$Grm$result332 = new Symbol(141, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT168);
                return CUP$Grm$result332;
            case 332:
                int i1099 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1100 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result333 = new Symbol(142, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result333;
            case 333:
                int i1101 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1102 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a165 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1103 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1104 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b107 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT169 = this.parser.nf.Binary(this.parser.pos(a165, b107), a165, Binary.BIT_AND, b107);
                Symbol CUP$Grm$result334 = new Symbol(142, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT169);
                return CUP$Grm$result334;
            case 334:
                int i1105 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1106 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result335 = new Symbol(143, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result335;
            case 335:
                int i1107 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1108 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a166 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1109 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1110 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b108 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT170 = this.parser.nf.Binary(this.parser.pos(a166, b108), a166, Binary.BIT_XOR, b108);
                Symbol CUP$Grm$result336 = new Symbol(143, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT170);
                return CUP$Grm$result336;
            case 336:
                int i1111 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1112 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result337 = new Symbol(144, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result337;
            case 337:
                int i1113 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1114 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a167 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1115 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1116 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b109 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT171 = this.parser.nf.Binary(this.parser.pos(a167, b109), a167, Binary.BIT_OR, b109);
                Symbol CUP$Grm$result338 = new Symbol(144, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT171);
                return CUP$Grm$result338;
            case 338:
                int i1117 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1118 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result339 = new Symbol(145, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result339;
            case 339:
                int i1119 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1120 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a168 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1121 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1122 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b110 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT172 = this.parser.nf.Binary(this.parser.pos(a168, b110), a168, Binary.COND_AND, b110);
                Symbol CUP$Grm$result340 = new Symbol(145, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT172);
                return CUP$Grm$result340;
            case TokenId.THROW /* 340 */:
                int i1123 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1124 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result341 = new Symbol(146, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result341;
            case TokenId.THROWS /* 341 */:
                int i1125 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1126 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a169 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1127 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1128 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr b111 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT173 = this.parser.nf.Binary(this.parser.pos(a169, b111), a169, Binary.COND_OR, b111);
                Symbol CUP$Grm$result342 = new Symbol(146, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT173);
                return CUP$Grm$result342;
            case TokenId.TRANSIENT /* 342 */:
                int i1129 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1130 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result343 = new Symbol(147, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result343;
            case TokenId.TRY /* 343 */:
                int i1131 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
                int i1132 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
                Expr a170 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
                int i1133 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1134 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr b112 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1135 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1136 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr c35 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT174 = this.parser.nf.Conditional(this.parser.pos(a170, c35), a170, b112, c35);
                Symbol CUP$Grm$result344 = new Symbol(147, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT174);
                return CUP$Grm$result344;
            case TokenId.VOID /* 344 */:
                int i1137 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1138 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result345 = new Symbol(148, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result345;
            case TokenId.VOLATILE /* 345 */:
                int i1139 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1140 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result346 = new Symbol(148, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result346;
            case TokenId.WHILE /* 346 */:
                int i1141 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
                int i1142 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
                Expr a171 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
                int i1143 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
                int i1144 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
                Assign.Operator b113 = (Assign.Operator) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
                int i1145 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1146 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr c36 = (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
                Expr RESULT175 = this.parser.nf.Assign(this.parser.pos(a171, c36), a171, b113, c36);
                Symbol CUP$Grm$result347 = new Symbol(149, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT175);
                return CUP$Grm$result347;
            case TokenId.STRICT /* 347 */:
                int i1147 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1148 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Expr RESULT176 = ((Name) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value).toExpr();
                Symbol CUP$Grm$result348 = new Symbol(150, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT176);
                return CUP$Grm$result348;
            case 348:
                int i1149 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1150 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result349 = new Symbol(150, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Field) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result349;
            case 349:
                int i1151 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1152 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result350 = new Symbol(150, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (ArrayAccess) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result350;
            case TokenId.NEQ /* 350 */:
                Assign.Operator RESULT177 = Assign.ASSIGN;
                Symbol CUP$Grm$result351 = new Symbol(151, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT177);
                return CUP$Grm$result351;
            case TokenId.MOD_E /* 351 */:
                Assign.Operator RESULT178 = Assign.MUL_ASSIGN;
                Symbol CUP$Grm$result352 = new Symbol(151, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT178);
                return CUP$Grm$result352;
            case TokenId.AND_E /* 352 */:
                Assign.Operator RESULT179 = Assign.DIV_ASSIGN;
                Symbol CUP$Grm$result353 = new Symbol(151, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT179);
                return CUP$Grm$result353;
            case TokenId.MUL_E /* 353 */:
                Assign.Operator RESULT180 = Assign.MOD_ASSIGN;
                Symbol CUP$Grm$result354 = new Symbol(151, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT180);
                return CUP$Grm$result354;
            case TokenId.PLUS_E /* 354 */:
                Assign.Operator RESULT181 = Assign.ADD_ASSIGN;
                Symbol CUP$Grm$result355 = new Symbol(151, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT181);
                return CUP$Grm$result355;
            case TokenId.MINUS_E /* 355 */:
                Assign.Operator RESULT182 = Assign.SUB_ASSIGN;
                Symbol CUP$Grm$result356 = new Symbol(151, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT182);
                return CUP$Grm$result356;
            case TokenId.DIV_E /* 356 */:
                Assign.Operator RESULT183 = Assign.SHL_ASSIGN;
                Symbol CUP$Grm$result357 = new Symbol(151, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT183);
                return CUP$Grm$result357;
            case TokenId.LE /* 357 */:
                Assign.Operator RESULT184 = Assign.SHR_ASSIGN;
                Symbol CUP$Grm$result358 = new Symbol(151, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT184);
                return CUP$Grm$result358;
            case TokenId.EQ /* 358 */:
                Assign.Operator RESULT185 = Assign.USHR_ASSIGN;
                Symbol CUP$Grm$result359 = new Symbol(151, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT185);
                return CUP$Grm$result359;
            case TokenId.GE /* 359 */:
                Assign.Operator RESULT186 = Assign.BIT_AND_ASSIGN;
                Symbol CUP$Grm$result360 = new Symbol(151, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT186);
                return CUP$Grm$result360;
            case TokenId.EXOR_E /* 360 */:
                Assign.Operator RESULT187 = Assign.BIT_XOR_ASSIGN;
                Symbol CUP$Grm$result361 = new Symbol(151, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT187);
                return CUP$Grm$result361;
            case TokenId.OR_E /* 361 */:
                Assign.Operator RESULT188 = Assign.BIT_OR_ASSIGN;
                Symbol CUP$Grm$result362 = new Symbol(151, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT188);
                return CUP$Grm$result362;
            case TokenId.PLUSPLUS /* 362 */:
                Symbol CUP$Grm$result363 = new Symbol(152, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Object) null);
                return CUP$Grm$result363;
            case TokenId.MINUSMINUS /* 363 */:
                int i1153 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1154 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result364 = new Symbol(152, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result364;
            case TokenId.LSHIFT /* 364 */:
                int i1155 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1156 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result365 = new Symbol(153, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result365;
            case TokenId.LSHIFT_E /* 365 */:
                int i1157 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
                int i1158 = ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
                Symbol CUP$Grm$result366 = new Symbol(154, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, (Expr) ((Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value);
                return CUP$Grm$result366;
            default:
                throw new Exception("Invalid action number found in internal parse table");
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}
