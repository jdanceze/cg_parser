package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/BlockGraphConverter.class */
public class BlockGraphConverter {
    public static void addStartStopNodesTo(BlockGraph graph) {
        List<Block> heads = graph.getHeads();
        int headCount = heads.size();
        if (headCount != 0 && (headCount != 1 || !(heads.get(0) instanceof DummyBlock))) {
            List<Block> blocks = graph.getBlocks();
            DummyBlock head = new DummyBlock(graph.getBody(), 0);
            head.makeHeadBlock(heads);
            graph.mHeads = Collections.singletonList(head);
            for (Block block : blocks) {
                block.setIndexInMethod(block.getIndexInMethod() + 1);
            }
            List<Block> newBlocks = new ArrayList<>();
            newBlocks.add(head);
            newBlocks.addAll(blocks);
            graph.mBlocks = newBlocks;
        }
        List<Block> tails = graph.getTails();
        int tailCount = tails.size();
        if (tailCount != 0) {
            if (tailCount != 1 || !(tails.get(0) instanceof DummyBlock)) {
                List<Block> blocks2 = graph.getBlocks();
                DummyBlock tail = new DummyBlock(graph.getBody(), blocks2.size());
                tail.makeTailBlock(tails);
                graph.mTails = Collections.singletonList(tail);
                blocks2.add(tail);
            }
        }
    }

    public static void reverse(BlockGraph graph) {
        for (Block block : graph.getBlocks()) {
            List<Block> succs = block.getSuccs();
            List<Block> preds = block.getPreds();
            block.setSuccs(preds);
            block.setPreds(succs);
        }
        List<Block> heads = graph.getHeads();
        List<Block> tails = graph.getTails();
        graph.mHeads = new ArrayList(tails);
        graph.mTails = new ArrayList(heads);
    }

    public static void main(String[] args) {
        Scene.v().loadClassAndSupport(args[0]);
        SootClass sc = Scene.v().getSootClass(args[0]);
        SootMethod sm = sc.getMethod(args[1]);
        Body b = sm.retrieveActiveBody();
        CompleteBlockGraph cfg = new CompleteBlockGraph(b);
        System.out.println(cfg);
        addStartStopNodesTo(cfg);
        System.out.println(cfg);
        reverse(cfg);
        System.out.println(cfg);
    }
}
