package soot.dava.toolkits.base.AST.traversals;

import java.util.ArrayList;
import java.util.List;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.DefinitionStmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/traversals/AllDefinitionsFinder.class */
public class AllDefinitionsFinder extends DepthFirstAdapter {
    ArrayList<DefinitionStmt> allDefs;

    public AllDefinitionsFinder() {
        this.allDefs = new ArrayList<>();
    }

    public AllDefinitionsFinder(boolean verbose) {
        super(verbose);
        this.allDefs = new ArrayList<>();
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inDefinitionStmt(DefinitionStmt s) {
        this.allDefs.add(s);
    }

    public List<DefinitionStmt> getAllDefs() {
        return this.allDefs;
    }
}
