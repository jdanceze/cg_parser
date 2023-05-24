package soot.dava;

import soot.Body;
import soot.G;
import soot.JavaMethods;
import soot.Modifier;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/dava/DavaStaticBlockCleaner.class */
public class DavaStaticBlockCleaner {
    SootClass sootClass;

    public DavaStaticBlockCleaner(Singletons.Global g) {
    }

    public static DavaStaticBlockCleaner v() {
        return G.v().soot_dava_DavaStaticBlockCleaner();
    }

    public void staticBlockInlining(SootClass sootClass) {
        this.sootClass = sootClass;
        if (!sootClass.declaresMethod(JavaMethods.SIG_CLINIT)) {
            return;
        }
        SootMethod clinit = sootClass.getMethod(JavaMethods.SIG_CLINIT);
        if (!clinit.hasActiveBody()) {
            throw new RuntimeException("method " + clinit.getName() + " has no active body!");
        }
        Body clinitBody = clinit.getActiveBody();
        Chain units = ((DavaBody) clinitBody).getUnits();
        if (units.size() != 1) {
            throw new RuntimeException("DavaBody AST doesn't have single root.");
        }
        ASTNode AST = (ASTNode) units.getFirst();
        if (!(AST instanceof ASTMethodNode)) {
            throw new RuntimeException("Starting node of DavaBody AST is not an ASTMethodNode");
        }
        AST.apply(new MethodCallFinder(this));
    }

    public ASTMethodNode inline(SootMethod maybeInline) {
        if (this.sootClass != null && this.sootClass.declaresMethod(maybeInline.getSubSignature()) && Modifier.isStatic(maybeInline.getModifiers())) {
            if (!maybeInline.hasActiveBody()) {
                throw new RuntimeException("method " + maybeInline.getName() + " has no active body!");
            }
            Body bod = maybeInline.getActiveBody();
            Chain units = ((DavaBody) bod).getUnits();
            if (units.size() != 1) {
                throw new RuntimeException("DavaBody AST doesn't have single root.");
            }
            ASTNode ASTtemp = (ASTNode) units.getFirst();
            if (!(ASTtemp instanceof ASTMethodNode)) {
                throw new RuntimeException("Starting node of DavaBody AST is not an ASTMethodNode");
            }
            ASTMethodNode toReturn = (ASTMethodNode) ASTtemp;
            ASTStatementSequenceNode declarations = toReturn.getDeclarations();
            if (declarations.getStatements().size() == 0) {
                return toReturn;
            }
            return null;
        }
        return null;
    }
}
