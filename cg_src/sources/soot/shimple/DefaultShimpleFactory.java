package soot.shimple;

import soot.Body;
import soot.jimple.toolkits.scalar.UnreachableCodeEliminator;
import soot.shimple.toolkits.graph.GlobalValueNumberer;
import soot.shimple.toolkits.graph.SimpleGlobalValueNumberer;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BlockGraphConverter;
import soot.toolkits.graph.CytronDominanceFrontier;
import soot.toolkits.graph.DominanceFrontier;
import soot.toolkits.graph.DominatorTree;
import soot.toolkits.graph.DominatorsFinder;
import soot.toolkits.graph.ExceptionalBlockGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.HashReversibleGraph;
import soot.toolkits.graph.ReversibleGraph;
import soot.toolkits.graph.SimpleDominatorsFinder;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/shimple/DefaultShimpleFactory.class */
public class DefaultShimpleFactory implements ShimpleFactory {
    protected final Body body;
    protected UnitGraph ug;
    protected BlockGraph bg;
    protected DominatorsFinder<Block> dFinder;
    protected DominatorTree<Block> dTree;
    protected DominanceFrontier<Block> dFrontier;
    protected GlobalValueNumberer gvn;
    protected ReversibleGraph<Block> rbg;
    protected DominatorsFinder<Block> rdFinder;
    protected DominatorTree<Block> rdTree;
    protected DominanceFrontier<Block> rdFrontier;

    public DefaultShimpleFactory(Body body) {
        this.body = body;
    }

    @Override // soot.shimple.ShimpleFactory
    public void clearCache() {
        this.ug = null;
        this.bg = null;
        this.dFinder = null;
        this.dTree = null;
        this.dFrontier = null;
        this.gvn = null;
        this.rbg = null;
        this.rdFinder = null;
        this.rdTree = null;
        this.rdFrontier = null;
    }

    public Body getBody() {
        Body body = this.body;
        if (body == null) {
            throw new RuntimeException("Assertion failed: Call setBody() first.");
        }
        return body;
    }

    @Override // soot.shimple.ShimpleFactory
    public ReversibleGraph<Block> getReverseBlockGraph() {
        ReversibleGraph<Block> rbg = this.rbg;
        if (rbg == null) {
            rbg = new HashReversibleGraph<>(getBlockGraph());
            rbg.reverse();
            this.rbg = rbg;
        }
        return rbg;
    }

    @Override // soot.shimple.ShimpleFactory
    public DominatorsFinder<Block> getReverseDominatorsFinder() {
        DominatorsFinder<Block> rdFinder = this.rdFinder;
        if (rdFinder == null) {
            rdFinder = new SimpleDominatorsFinder<>(getReverseBlockGraph());
            this.rdFinder = rdFinder;
        }
        return rdFinder;
    }

    @Override // soot.shimple.ShimpleFactory
    public DominatorTree<Block> getReverseDominatorTree() {
        DominatorTree<Block> rdTree = this.rdTree;
        if (rdTree == null) {
            rdTree = new DominatorTree<>(getReverseDominatorsFinder());
            this.rdTree = rdTree;
        }
        return rdTree;
    }

    @Override // soot.shimple.ShimpleFactory
    public DominanceFrontier<Block> getReverseDominanceFrontier() {
        DominanceFrontier<Block> rdFrontier = this.rdFrontier;
        if (rdFrontier == null) {
            rdFrontier = new CytronDominanceFrontier<>(getReverseDominatorTree());
            this.rdFrontier = rdFrontier;
        }
        return rdFrontier;
    }

    @Override // soot.shimple.ShimpleFactory
    public BlockGraph getBlockGraph() {
        BlockGraph bg = this.bg;
        if (bg == null) {
            bg = new ExceptionalBlockGraph((ExceptionalUnitGraph) getUnitGraph());
            BlockGraphConverter.addStartStopNodesTo(bg);
            this.bg = bg;
        }
        return bg;
    }

    @Override // soot.shimple.ShimpleFactory
    public UnitGraph getUnitGraph() {
        UnitGraph ug = this.ug;
        if (ug == null) {
            Body body = getBody();
            UnreachableCodeEliminator.v().transform(body);
            ug = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body);
            this.ug = ug;
        }
        return ug;
    }

    @Override // soot.shimple.ShimpleFactory
    public DominatorsFinder<Block> getDominatorsFinder() {
        DominatorsFinder<Block> dFinder = this.dFinder;
        if (dFinder == null) {
            dFinder = new SimpleDominatorsFinder<>(getBlockGraph());
            this.dFinder = dFinder;
        }
        return dFinder;
    }

    @Override // soot.shimple.ShimpleFactory
    public DominatorTree<Block> getDominatorTree() {
        DominatorTree<Block> dTree = this.dTree;
        if (dTree == null) {
            dTree = new DominatorTree<>(getDominatorsFinder());
            this.dTree = dTree;
        }
        return dTree;
    }

    @Override // soot.shimple.ShimpleFactory
    public DominanceFrontier<Block> getDominanceFrontier() {
        DominanceFrontier<Block> dFrontier = this.dFrontier;
        if (dFrontier == null) {
            dFrontier = new CytronDominanceFrontier<>(getDominatorTree());
            this.dFrontier = dFrontier;
        }
        return dFrontier;
    }

    @Override // soot.shimple.ShimpleFactory
    public GlobalValueNumberer getGlobalValueNumberer() {
        GlobalValueNumberer gvn = this.gvn;
        if (gvn == null) {
            gvn = new SimpleGlobalValueNumberer(getBlockGraph());
            this.gvn = gvn;
        }
        return gvn;
    }
}
