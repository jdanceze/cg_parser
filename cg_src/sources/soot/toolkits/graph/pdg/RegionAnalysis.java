package soot.toolkits.graph.pdg;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.G;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.coffi.Instruction;
import soot.options.Options;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BriefBlockGraph;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.DominatorNode;
import soot.toolkits.graph.DominatorTree;
import soot.toolkits.graph.ExceptionalBlockGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/pdg/RegionAnalysis.class */
public class RegionAnalysis {
    private static final Logger logger = LoggerFactory.getLogger(RegionAnalysis.class);
    protected SootClass m_class;
    protected SootMethod m_method;
    protected Body m_methodBody;
    protected UnitGraph m_cfg;
    protected UnitGraph m_reverseCFG;
    protected BlockGraph m_blockCFG;
    protected BlockGraph m_reverseBlockCFG;
    private MHGDominatorTree<Block> m_dom;
    private MHGDominatorTree<Block> m_pdom;
    protected Hashtable<Integer, Region> m_regions = new Hashtable<>();
    protected List<Region> m_regionsList = null;
    private int m_regCount = 0;
    protected Region m_topLevelRegion = null;
    protected Hashtable<Block, Region> m_block2region = null;

    public RegionAnalysis(UnitGraph cfg, SootMethod m, SootClass c) {
        this.m_class = null;
        this.m_method = null;
        this.m_methodBody = cfg.getBody();
        this.m_cfg = cfg;
        this.m_method = m;
        this.m_class = c;
        if (Options.v().verbose()) {
            logger.debug("[RegionAnalysis]~~~~~~~~~~~~~~~ Begin Region Analsis for method: " + m.getName() + " ~~~~~~~~~~~~~~~~~~~~");
        }
        findWeakRegions();
        if (Options.v().verbose()) {
            logger.debug("[RegionAnalysis]~~~~~~~~~~~~~~~ End:" + m.getName() + " ~~~~~~~~~~~~~~~~~~~~");
        }
    }

    private void findWeakRegions() {
        if (this.m_cfg instanceof ExceptionalUnitGraph) {
            this.m_blockCFG = new ExceptionalBlockGraph((ExceptionalUnitGraph) this.m_cfg);
        } else if (this.m_cfg instanceof EnhancedUnitGraph) {
            this.m_blockCFG = new EnhancedBlockGraph((EnhancedUnitGraph) this.m_cfg);
        } else if (this.m_cfg instanceof BriefUnitGraph) {
            this.m_blockCFG = new BriefBlockGraph((BriefUnitGraph) this.m_cfg);
        } else {
            throw new RuntimeException("Unsupported CFG passed into the RegionAnalyis constructor!");
        }
        this.m_dom = new MHGDominatorTree<>(new MHGDominatorsFinder(this.m_blockCFG));
        try {
            this.m_pdom = new MHGDominatorTree<>(new MHGPostDominatorsFinder(this.m_blockCFG));
            if (Options.v().verbose()) {
                logger.debug("[RegionAnalysis] PostDominator tree: ");
            }
            this.m_regCount = -1;
            if (this.m_blockCFG.getHeads().size() == 1) {
                this.m_regCount++;
                this.m_regions.put(Integer.valueOf(this.m_regCount), createRegion(this.m_regCount));
                weakRegionDFS2(this.m_blockCFG.getHeads().get(0), this.m_regCount);
            } else if (this.m_blockCFG.getTails().size() == 1) {
                this.m_regCount++;
                this.m_regions.put(Integer.valueOf(this.m_regCount), createRegion(this.m_regCount));
                weakRegionDFS(this.m_blockCFG.getTails().get(0), this.m_regCount);
            } else {
                if (Options.v().verbose()) {
                    logger.warn("RegionAnalysis: the CFG is multi-headed and tailed, so, the results of this analysis might not be reliable!");
                }
                for (int i = 0; i < this.m_blockCFG.getTails().size(); i++) {
                    this.m_regCount++;
                    this.m_regions.put(Integer.valueOf(this.m_regCount), createRegion(this.m_regCount));
                    weakRegionDFS(this.m_blockCFG.getTails().get(i), this.m_regCount);
                }
            }
        } catch (RuntimeException e) {
            logger.debug("[RegionAnalysis] Exception in findWeakRegions: " + e);
        }
    }

    private void weakRegionDFS(Block v, int r) {
        try {
            this.m_regions.get(Integer.valueOf(r)).add(v);
            DominatorNode<Block> parentOfV = this.m_dom.getParentOf(this.m_dom.getDode(v));
            Block u2 = parentOfV == null ? null : parentOfV.getGode();
            List<DominatorNode<Block>> children = this.m_pdom.getChildrenOf(this.m_pdom.getDode(v));
            for (int i = 0; i < children.size(); i++) {
                DominatorNode<Block> w = children.get(i);
                Block u1 = w.getGode();
                if (u2 != null && u1.equals(u2)) {
                    weakRegionDFS(w.getGode(), r);
                } else {
                    this.m_regCount++;
                    this.m_regions.put(Integer.valueOf(this.m_regCount), createRegion(this.m_regCount));
                    weakRegionDFS(w.getGode(), this.m_regCount);
                }
            }
        } catch (RuntimeException e) {
            logger.debug("[RegionAnalysis] Exception in weakRegionDFS: ", (Throwable) e);
            logger.debug("v is  " + v.toShortString() + " in region " + r);
            G.v().out.flush();
        }
    }

    private void weakRegionDFS2(Block v, int r) {
        this.m_regions.get(Integer.valueOf(r)).add2Back(v);
        DominatorNode<Block> parentOfV = this.m_pdom.getParentOf(this.m_pdom.getDode(v));
        Block u2 = parentOfV == null ? null : parentOfV.getGode();
        List<DominatorNode<Block>> children = this.m_dom.getChildrenOf(this.m_dom.getDode(v));
        for (int i = 0; i < children.size(); i++) {
            DominatorNode<Block> w = children.get(i);
            Block u1 = w.getGode();
            if (u2 != null && u1.equals(u2)) {
                weakRegionDFS2(w.getGode(), r);
            } else {
                this.m_regCount++;
                this.m_regions.put(Integer.valueOf(this.m_regCount), createRegion(this.m_regCount));
                weakRegionDFS2(w.getGode(), this.m_regCount);
            }
        }
    }

    public List<Region> getRegions() {
        if (this.m_regionsList == null) {
            this.m_regionsList = new ArrayList(this.m_regions.values());
        }
        return this.m_regionsList;
    }

    public Hashtable<Unit, Region> getUnit2RegionMap() {
        Hashtable<Unit, Region> unit2region = new Hashtable<>();
        List<Region> regions = getRegions();
        for (Region r : regions) {
            List<Unit> units = r.getUnits();
            for (Unit u : units) {
                unit2region.put(u, r);
            }
        }
        return unit2region;
    }

    public Hashtable<Block, Region> getBlock2RegionMap() {
        if (this.m_block2region == null) {
            this.m_block2region = new Hashtable<>();
            List<Region> regions = getRegions();
            for (Region r : regions) {
                List<Block> blocks = r.getBlocks();
                for (Block u : blocks) {
                    this.m_block2region.put(u, r);
                }
            }
        }
        return this.m_block2region;
    }

    public BlockGraph getBlockCFG() {
        return this.m_blockCFG;
    }

    public DominatorTree<Block> getPostDominatorTree() {
        return this.m_pdom;
    }

    public DominatorTree<Block> getDominatorTree() {
        return this.m_dom;
    }

    public void reset() {
        this.m_regions.clear();
        this.m_regionsList.clear();
        this.m_regionsList = null;
        this.m_block2region.clear();
        this.m_block2region = null;
        this.m_regCount = 0;
    }

    protected Region createRegion(int id) {
        Region region = new Region(id, this.m_method, this.m_class, this.m_cfg);
        if (id == 0) {
            this.m_topLevelRegion = region;
        }
        return region;
    }

    public Region getTopLevelRegion() {
        return this.m_topLevelRegion;
    }

    public static String CFGtoString(DirectedGraph<Block> cfg, boolean blockDetail) {
        String s = String.valueOf("") + "Headers: " + cfg.getHeads().size() + Instruction.argsep + cfg.getHeads();
        for (Block node : cfg) {
            String s2 = String.valueOf(String.valueOf(s) + "Node = " + node.toShortString() + "\n") + "Preds:\n";
            Iterator<Block> predsIt = cfg.getPredsOf(node).iterator();
            while (predsIt.hasNext()) {
                s2 = String.valueOf(String.valueOf(s2) + "     ") + predsIt.next().toShortString() + "\n";
            }
            s = String.valueOf(s2) + "Succs:\n";
            Iterator<Block> succsIt = cfg.getSuccsOf(node).iterator();
            while (succsIt.hasNext()) {
                s = String.valueOf(String.valueOf(s) + "     ") + succsIt.next().toShortString() + "\n";
            }
        }
        if (blockDetail) {
            s = String.valueOf(s) + "Blocks Detail:";
            Iterator<Block> it = cfg.iterator();
            while (it.hasNext()) {
                s = String.valueOf(s) + it.next() + "\n";
            }
        }
        return s;
    }
}
