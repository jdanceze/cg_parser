package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Trap;
import soot.Unit;
import soot.jimple.NopStmt;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/BlockGraph.class */
public abstract class BlockGraph implements DirectedBodyGraph<Block> {
    protected Body mBody;
    protected Chain<Unit> mUnits;
    protected List<Block> mBlocks;
    protected List<Block> mHeads;
    protected List<Block> mTails;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !BlockGraph.class.desiredAssertionStatus();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BlockGraph(UnitGraph unitGraph) {
        this.mBody = unitGraph.getBody();
        this.mUnits = this.mBody.getUnits();
        buildBlocks(computeLeaders(unitGraph), unitGraph);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Set<Unit> computeLeaders(UnitGraph unitGraph) {
        Body body = unitGraph.getBody();
        if (body != this.mBody) {
            throw new RuntimeException("BlockGraph.computeLeaders() called with a UnitGraph that doesn't match its mBody.");
        }
        Set<Unit> leaders = new HashSet<>();
        for (Trap trap : body.getTraps()) {
            leaders.add(trap.getHandlerUnit());
        }
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (unitGraph.getPredsOf(u).size() != 1) {
                leaders.add(u);
            }
            List<Unit> successors = unitGraph.getSuccsOf(u);
            if (successors.size() > 1 || u.branches()) {
                for (Unit next : successors) {
                    leaders.add(next);
                }
            }
        }
        return leaders;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Map<Unit, Block> buildBlocks(Set<Unit> leaders, UnitGraph unitGraph) {
        ArrayList<Block> blockList = new ArrayList<>(leaders.size());
        ArrayList<Block> headList = new ArrayList<>();
        ArrayList<Block> tailList = new ArrayList<>();
        Map<Unit, Block> unitToBlock = new HashMap<>();
        Unit blockHead = null;
        int blockLength = 0;
        Iterator<Unit> unitIt = this.mUnits.iterator();
        if (unitIt.hasNext()) {
            blockHead = unitIt.next();
            if (!leaders.contains(blockHead)) {
                throw new RuntimeException("BlockGraph: first unit not a leader!");
            }
            blockLength = 0 + 1;
        }
        Unit blockTail = blockHead;
        int indexInMethod = 0;
        while (unitIt.hasNext()) {
            Unit u = unitIt.next();
            if (leaders.contains(u)) {
                addBlock(blockHead, blockTail, indexInMethod, blockLength, blockList, unitToBlock);
                indexInMethod++;
                blockHead = u;
                blockLength = 0;
            }
            blockTail = u;
            blockLength++;
        }
        if (blockLength > 0) {
            addBlock(blockHead, blockTail, indexInMethod, blockLength, blockList, unitToBlock);
        }
        for (Unit headUnit : unitGraph.getHeads()) {
            Block headBlock = unitToBlock.get(headUnit);
            if (headBlock.getHead() == headUnit) {
                headList.add(headBlock);
            } else {
                throw new RuntimeException("BlockGraph(): head Unit is not the first unit in the corresponding Block!");
            }
        }
        for (Unit tailUnit : unitGraph.getTails()) {
            Block tailBlock = unitToBlock.get(tailUnit);
            if (tailBlock.getTail() == tailUnit) {
                tailList.add(tailBlock);
            } else {
                throw new RuntimeException("BlockGraph(): tail Unit is not the last unit in the corresponding Block!");
            }
        }
        Iterator<Block> blockIt = blockList.iterator();
        while (blockIt.hasNext()) {
            Block block = blockIt.next();
            List<Unit> predUnits = unitGraph.getPredsOf(block.getHead());
            if (predUnits.isEmpty()) {
                block.setPreds(Collections.emptyList());
            } else {
                List<Block> predBlocks = new ArrayList<>(predUnits.size());
                for (Unit predUnit : predUnits) {
                    if (!$assertionsDisabled && predUnit == null) {
                        throw new AssertionError();
                    }
                    Block predBlock = unitToBlock.get(predUnit);
                    if (predBlock == null) {
                        throw new RuntimeException("BlockGraph(): block head predecessor (" + predUnit + ") mapped to null block!");
                    }
                    predBlocks.add(predBlock);
                }
                block.setPreds(Collections.unmodifiableList(predBlocks));
                if (block.getHead() == this.mUnits.getFirst()) {
                    headList.add(block);
                }
            }
            List<Unit> succUnits = unitGraph.getSuccsOf(block.getTail());
            if (succUnits.isEmpty()) {
                block.setSuccs(Collections.emptyList());
                if (tailList.contains(block)) {
                    continue;
                } else if (block.getPreds().isEmpty() && block.getHead() == block.getTail() && (block.getHead() instanceof NopStmt)) {
                    blockIt.remove();
                } else {
                    throw new RuntimeException("Block with no successors is not a tail!: " + block.toString());
                }
            } else {
                List<Block> succBlocks = new ArrayList<>(succUnits.size());
                for (Unit succUnit : succUnits) {
                    if (!$assertionsDisabled && succUnit == null) {
                        throw new AssertionError();
                    }
                    Block succBlock = unitToBlock.get(succUnit);
                    if (succBlock == null) {
                        throw new RuntimeException("BlockGraph(): block tail successor (" + succUnit + ") mapped to null block!");
                    }
                    succBlocks.add(succBlock);
                }
                block.setSuccs(Collections.unmodifiableList(succBlocks));
            }
        }
        blockList.trimToSize();
        this.mBlocks = Collections.unmodifiableList(blockList);
        headList.trimToSize();
        this.mHeads = headList.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(headList);
        tailList.trimToSize();
        this.mTails = tailList.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(tailList);
        return unitToBlock;
    }

    private void addBlock(Unit head, Unit tail, int index, int length, List<Block> blockList, Map<Unit, Block> unitToBlock) {
        Block block = new Block(head, tail, this.mBody, index, length, this);
        blockList.add(block);
        unitToBlock.put(tail, block);
        unitToBlock.put(head, block);
    }

    @Override // soot.toolkits.graph.DirectedBodyGraph
    public Body getBody() {
        return this.mBody;
    }

    public List<Block> getBlocks() {
        return this.mBlocks;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (Block someBlock : this.mBlocks) {
            buf.append(someBlock.toString()).append('\n');
        }
        return buf.toString();
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<Block> getHeads() {
        return this.mHeads;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<Block> getTails() {
        return this.mTails;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<Block> getPredsOf(Block b) {
        return b.getPreds();
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<Block> getSuccsOf(Block b) {
        return b.getSuccs();
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public int size() {
        return this.mBlocks.size();
    }

    @Override // soot.toolkits.graph.DirectedGraph, java.lang.Iterable
    public Iterator<Block> iterator() {
        return this.mBlocks.iterator();
    }
}
