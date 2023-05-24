package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.Catch;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.ast.Try;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Try_c.class */
public class Try_c extends Stmt_c implements Try {
    protected Block tryBlock;
    protected List catchBlocks;
    protected Block finallyBlock;
    static Class class$polyglot$ast$Catch;

    public Try_c(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock) {
        super(pos);
        Class cls;
        this.tryBlock = tryBlock;
        if (class$polyglot$ast$Catch == null) {
            cls = class$("polyglot.ast.Catch");
            class$polyglot$ast$Catch = cls;
        } else {
            cls = class$polyglot$ast$Catch;
        }
        this.catchBlocks = TypedList.copyAndCheck(catchBlocks, cls, true);
        this.finallyBlock = finallyBlock;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // polyglot.ast.Try
    public Block tryBlock() {
        return this.tryBlock;
    }

    @Override // polyglot.ast.Try
    public Try tryBlock(Block tryBlock) {
        Try_c n = (Try_c) copy();
        n.tryBlock = tryBlock;
        return n;
    }

    @Override // polyglot.ast.Try
    public List catchBlocks() {
        return Collections.unmodifiableList(this.catchBlocks);
    }

    @Override // polyglot.ast.Try
    public Try catchBlocks(List catchBlocks) {
        Class cls;
        Try_c n = (Try_c) copy();
        if (class$polyglot$ast$Catch == null) {
            cls = class$("polyglot.ast.Catch");
            class$polyglot$ast$Catch = cls;
        } else {
            cls = class$polyglot$ast$Catch;
        }
        n.catchBlocks = TypedList.copyAndCheck(catchBlocks, cls, true);
        return n;
    }

    @Override // polyglot.ast.Try
    public Block finallyBlock() {
        return this.finallyBlock;
    }

    @Override // polyglot.ast.Try
    public Try finallyBlock(Block finallyBlock) {
        Try_c n = (Try_c) copy();
        n.finallyBlock = finallyBlock;
        return n;
    }

    protected Try_c reconstruct(Block tryBlock, List catchBlocks, Block finallyBlock) {
        Class cls;
        if (tryBlock != this.tryBlock || !CollectionUtil.equals(catchBlocks, this.catchBlocks) || finallyBlock != this.finallyBlock) {
            Try_c n = (Try_c) copy();
            n.tryBlock = tryBlock;
            if (class$polyglot$ast$Catch == null) {
                cls = class$("polyglot.ast.Catch");
                class$polyglot$ast$Catch = cls;
            } else {
                cls = class$polyglot$ast$Catch;
            }
            n.catchBlocks = TypedList.copyAndCheck(catchBlocks, cls, true);
            n.finallyBlock = finallyBlock;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Block tryBlock = (Block) visitChild(this.tryBlock, v);
        List catchBlocks = visitList(this.catchBlocks, v);
        Block finallyBlock = (Block) visitChild(this.finallyBlock, v);
        return reconstruct(tryBlock, catchBlocks, finallyBlock);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor exceptionCheckEnter(ExceptionChecker ec) throws SemanticException {
        return ((ExceptionChecker) super.exceptionCheckEnter(ec)).bypassChildren(this);
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x007b  */
    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public polyglot.ast.Node exceptionCheck(polyglot.visit.ExceptionChecker r6) throws polyglot.types.SemanticException {
        /*
            Method dump skipped, instructions count: 468
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: polyglot.ext.jl.ast.Try_c.exceptionCheck(polyglot.visit.ExceptionChecker):polyglot.ast.Node");
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("try ");
        sb.append(this.tryBlock.toString());
        int count = 0;
        Iterator it = this.catchBlocks.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Catch cb = (Catch) it.next();
            int i = count;
            count++;
            if (i > 2) {
                sb.append("...");
                break;
            }
            sb.append(Instruction.argsep);
            sb.append(cb.toString());
        }
        if (this.finallyBlock != null) {
            sb.append(" finally ");
            sb.append(this.finallyBlock.toString());
        }
        return sb.toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write("try");
        printSubStmt(this.tryBlock, w, tr);
        for (Catch cb : this.catchBlocks) {
            w.newline(0);
            printBlock(cb, w, tr);
        }
        if (this.finallyBlock != null) {
            w.newline(0);
            w.write("finally");
            printSubStmt(this.finallyBlock, w, tr);
        }
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.tryBlock.entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        Term next;
        TypeSystem ts = v.typeSystem();
        CFGBuilder v1 = v.push(this, false);
        CFGBuilder v2 = v.push(this, true);
        for (Type type : ts.uncheckedExceptions()) {
            v1.visitThrow(this.tryBlock.entry(), type);
        }
        if (this.finallyBlock == null) {
            next = this;
        } else {
            next = this.finallyBlock.entry();
            v.visitCFG(this.finallyBlock, this);
        }
        v1.visitCFG(this.tryBlock, next);
        for (Catch cb : this.catchBlocks) {
            v2.visitCFG(cb, next);
        }
        return succs;
    }
}
