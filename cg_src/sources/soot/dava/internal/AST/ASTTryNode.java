package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Local;
import soot.SootClass;
import soot.UnitPrinter;
import soot.coffi.Instruction;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.ASTAnalysis;
import soot.dava.toolkits.base.AST.TryContentsFinder;
import soot.dava.toolkits.base.AST.analysis.Analysis;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTTryNode.class */
public class ASTTryNode extends ASTLabeledNode {
    private List<Object> tryBody;
    private List<Object> catchList;
    private Map<Object, Object> exceptionMap;
    private Map<Object, Object> paramMap;
    private container tryBodyContainer;

    /* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTTryNode$container.class */
    public class container {
        public Object o;

        public container(Object o) {
            this.o = o;
        }

        public void replaceBody(Object newBody) {
            this.o = newBody;
        }
    }

    public ASTTryNode(SETNodeLabel label, List<Object> tryBody, List<Object> catchList, Map<Object, Object> exceptionMap, Map<Object, Object> paramMap) {
        super(label);
        this.tryBody = tryBody;
        this.tryBodyContainer = new container(tryBody);
        this.catchList = new ArrayList();
        for (Object obj : catchList) {
            this.catchList.add(new container(obj));
        }
        this.exceptionMap = new HashMap();
        Iterator<Object> cit = this.catchList.iterator();
        while (cit.hasNext()) {
            container c = (container) cit.next();
            this.exceptionMap.put(c, exceptionMap.get(c.o));
        }
        this.paramMap = new HashMap();
        Iterator<Object> cit2 = this.catchList.iterator();
        while (cit2.hasNext()) {
            container c2 = (container) cit2.next();
            this.paramMap.put(c2, paramMap.get(c2.o));
        }
        this.subBodies.add(this.tryBodyContainer);
        for (Object obj2 : this.catchList) {
            this.subBodies.add(obj2);
        }
    }

    public void replaceTryBody(List<Object> tryBody) {
        this.tryBody = tryBody;
        this.tryBodyContainer = new container(tryBody);
        List<Object> oldSubBodies = this.subBodies;
        this.subBodies = new ArrayList();
        this.subBodies.add(this.tryBodyContainer);
        Iterator<Object> oldIt = oldSubBodies.iterator();
        oldIt.next();
        while (oldIt.hasNext()) {
            this.subBodies.add(oldIt.next());
        }
    }

    @Override // soot.dava.internal.AST.ASTNode
    protected void perform_AnalysisOnSubBodies(ASTAnalysis a) {
        if (a instanceof TryContentsFinder) {
            Iterator<Object> sbit = this.subBodies.iterator();
            while (sbit.hasNext()) {
                container subBody = (container) sbit.next();
                for (ASTNode n : (List) subBody.o) {
                    n.perform_Analysis(a);
                    TryContentsFinder.v().add_ExceptionSet(subBody, TryContentsFinder.v().get_ExceptionSet(n));
                }
            }
            a.analyseASTNode(this);
            return;
        }
        super.perform_AnalysisOnSubBodies(a);
    }

    public boolean isEmpty() {
        return this.tryBody.isEmpty();
    }

    public List<Object> get_TryBody() {
        return this.tryBody;
    }

    public container get_TryBodyContainer() {
        return this.tryBodyContainer;
    }

    public List<Object> get_CatchList() {
        return this.catchList;
    }

    public Map<Object, Object> get_ExceptionMap() {
        return this.exceptionMap;
    }

    public Map<Object, Object> get_ParamMap() {
        return this.paramMap;
    }

    public Set<Object> get_ExceptionSet() {
        HashSet<Object> s = new HashSet<>();
        for (Object obj : this.catchList) {
            s.add(this.exceptionMap.get(obj));
        }
        return s;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        ArrayList<Object> newCatchList = new ArrayList<>();
        Iterator<Object> it = this.catchList.iterator();
        while (it.hasNext()) {
            newCatchList.add(((container) it.next()).o);
        }
        return new ASTTryNode(get_Label(), this.tryBody, newCatchList, this.exceptionMap, this.paramMap);
    }

    @Override // soot.dava.internal.AST.ASTNode, soot.Unit
    public void toString(UnitPrinter up) {
        label_toString(up);
        up.literal("try");
        up.newline();
        up.literal("{");
        up.newline();
        up.incIndent();
        body_toString(up, this.tryBody);
        up.decIndent();
        up.literal("}");
        up.newline();
        Iterator<Object> cit = this.catchList.iterator();
        while (cit.hasNext()) {
            container catchBody = (container) cit.next();
            up.literal(Jimple.CATCH);
            up.literal(Instruction.argsep);
            up.literal("(");
            up.type(((SootClass) this.exceptionMap.get(catchBody)).getType());
            up.literal(Instruction.argsep);
            up.local((Local) this.paramMap.get(catchBody));
            up.literal(")");
            up.newline();
            up.literal("{");
            up.newline();
            up.incIndent();
            body_toString(up, (List) catchBody.o);
            up.decIndent();
            up.literal("}");
            up.newline();
        }
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append(label_toString());
        b.append("try");
        b.append("\n");
        b.append("{");
        b.append("\n");
        b.append(body_toString(this.tryBody));
        b.append("}");
        b.append("\n");
        Iterator<Object> cit = this.catchList.iterator();
        while (cit.hasNext()) {
            container catchBody = (container) cit.next();
            b.append("catch (");
            b.append(((SootClass) this.exceptionMap.get(catchBody)).getName());
            b.append(Instruction.argsep);
            b.append(((Local) this.paramMap.get(catchBody)).getName());
            b.append(")");
            b.append("\n");
            b.append("{");
            b.append("\n");
            b.append(body_toString((List) catchBody.o));
            b.append("}");
            b.append("\n");
        }
        return b.toString();
    }

    @Override // soot.dava.internal.AST.ASTNode
    public void apply(Analysis a) {
        a.caseASTTryNode(this);
    }
}
