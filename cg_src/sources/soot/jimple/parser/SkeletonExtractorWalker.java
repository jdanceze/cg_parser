package soot.jimple.parser;

import java.util.ArrayList;
import java.util.List;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootResolver;
import soot.Type;
import soot.jimple.parser.node.AFile;
import soot.jimple.parser.node.AMethodMember;
import soot.jimple.parser.node.AThrowsClause;
import soot.jimple.parser.node.PModifier;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/SkeletonExtractorWalker.class */
public class SkeletonExtractorWalker extends Walker {
    public SkeletonExtractorWalker(SootResolver aResolver, SootClass aSootClass) {
        super(aSootClass, aResolver);
    }

    public SkeletonExtractorWalker(SootResolver aResolver) {
        super(aResolver);
    }

    @Override // soot.jimple.parser.Walker, soot.jimple.parser.analysis.DepthFirstAdapter, soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAFile(AFile node) {
        inAFile(node);
        Object[] temp = node.getModifier().toArray();
        for (Object element : temp) {
            ((PModifier) element).apply(this);
        }
        if (node.getFileType() != null) {
            node.getFileType().apply(this);
        }
        if (node.getClassName() != null) {
            node.getClassName().apply(this);
        }
        String className = (String) this.mProductions.removeLast();
        if (this.mSootClass == null) {
            this.mSootClass = new SootClass(className);
            this.mSootClass.setResolvingLevel(2);
        } else if (!className.equals(this.mSootClass.getName())) {
            throw new RuntimeException("expected:  " + className + ", but got: " + this.mSootClass.getName());
        }
        if (node.getExtendsClause() != null) {
            node.getExtendsClause().apply(this);
        }
        if (node.getImplementsClause() != null) {
            node.getImplementsClause().apply(this);
        }
        if (node.getFileBody() != null) {
            node.getFileBody().apply(this);
        }
        outAFile(node);
    }

    @Override // soot.jimple.parser.Walker, soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAFile(AFile node) {
        List<String> implementsList = null;
        String superClass = null;
        if (node.getImplementsClause() != null) {
            implementsList = (List) this.mProductions.removeLast();
        }
        if (node.getExtendsClause() != null) {
            superClass = (String) this.mProductions.removeLast();
        }
        String classType = (String) this.mProductions.removeLast();
        int modifierFlags = processModifiers(node.getModifier());
        if (classType.equals("interface")) {
            modifierFlags |= 512;
        }
        this.mSootClass.setModifiers(modifierFlags);
        if (superClass != null) {
            this.mSootClass.setSuperclass(this.mResolver.makeClassRef(superClass));
        }
        if (implementsList != null) {
            for (String str : implementsList) {
                SootClass interfaceClass = this.mResolver.makeClassRef(str);
                this.mSootClass.addInterface(interfaceClass);
            }
        }
        this.mProductions.addLast(this.mSootClass);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter, soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAMethodMember(AMethodMember node) {
        inAMethodMember(node);
        Object[] temp = node.getModifier().toArray();
        for (Object element : temp) {
            ((PModifier) element).apply(this);
        }
        if (node.getType() != null) {
            node.getType().apply(this);
        }
        if (node.getName() != null) {
            node.getName().apply(this);
        }
        if (node.getLParen() != null) {
            node.getLParen().apply(this);
        }
        if (node.getParameterList() != null) {
            node.getParameterList().apply(this);
        }
        if (node.getRParen() != null) {
            node.getRParen().apply(this);
        }
        if (node.getThrowsClause() != null) {
            node.getThrowsClause().apply(this);
        }
        outAMethodMember(node);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.jimple.parser.Walker, soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAMethodMember(AMethodMember node) {
        List parameterList;
        SootMethod method;
        List<SootClass> throwsClause = null;
        if (node.getThrowsClause() != null) {
            throwsClause = (List) this.mProductions.removeLast();
        }
        if (node.getParameterList() != null) {
            parameterList = (List) this.mProductions.removeLast();
        } else {
            parameterList = new ArrayList();
        }
        Object o = this.mProductions.removeLast();
        String name = (String) o;
        Type type = (Type) this.mProductions.removeLast();
        int modifier = processModifiers(node.getModifier());
        if (throwsClause != null) {
            method = Scene.v().makeSootMethod(name, parameterList, type, modifier, throwsClause);
        } else {
            method = Scene.v().makeSootMethod(name, parameterList, type, modifier);
        }
        this.mSootClass.addMethod(method);
    }

    @Override // soot.jimple.parser.Walker, soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAThrowsClause(AThrowsClause node) {
        List<String> l = (List) this.mProductions.removeLast();
        ArrayList arrayList = new ArrayList(l.size());
        for (String className : l) {
            arrayList.add(this.mResolver.makeClassRef(className));
        }
        this.mProductions.addLast(arrayList);
    }
}
