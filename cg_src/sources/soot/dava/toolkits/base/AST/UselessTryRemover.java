package soot.dava.toolkits.base.AST;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.Singletons;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTTryNode;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/UselessTryRemover.class */
public class UselessTryRemover extends ASTAnalysis {
    public UselessTryRemover(Singletons.Global g) {
    }

    public static UselessTryRemover v() {
        return G.v().soot_dava_toolkits_base_AST_UselessTryRemover();
    }

    @Override // soot.dava.toolkits.base.AST.ASTAnalysis
    public int getAnalysisDepth() {
        return 0;
    }

    @Override // soot.dava.toolkits.base.AST.ASTAnalysis
    public void analyseASTNode(ASTNode n) {
        List<Object> subBody;
        Iterator<Object> sbit = n.get_SubBodies().iterator();
        while (sbit.hasNext()) {
            List<Object> toRemove = new ArrayList<>();
            if (n instanceof ASTTryNode) {
                subBody = (List) ((ASTTryNode.container) sbit.next()).o;
            } else {
                subBody = (List) sbit.next();
            }
            for (Object child : subBody) {
                if (child instanceof ASTTryNode) {
                    ASTTryNode tryNode = (ASTTryNode) child;
                    tryNode.perform_Analysis(TryContentsFinder.v());
                    if (tryNode.get_CatchList().isEmpty() || tryNode.isEmpty()) {
                        toRemove.add(tryNode);
                    }
                }
            }
            Iterator<Object> trit = toRemove.iterator();
            while (trit.hasNext()) {
                ASTTryNode tryNode2 = (ASTTryNode) trit.next();
                subBody.addAll(subBody.indexOf(tryNode2), tryNode2.get_TryBody());
                subBody.remove(tryNode2);
            }
            if (!toRemove.isEmpty()) {
                G.v().ASTAnalysis_modified = true;
            }
        }
    }
}
