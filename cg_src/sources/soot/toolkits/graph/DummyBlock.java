package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Unit;
/* compiled from: BlockGraphConverter.java */
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/DummyBlock.class */
class DummyBlock extends Block {
    /* JADX INFO: Access modifiers changed from: package-private */
    public DummyBlock(Body body, int indexInMethod) {
        super(null, null, body, indexInMethod, 0, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void makeHeadBlock(List<Block> oldHeads) {
        setPreds(new ArrayList());
        setSuccs(new ArrayList(oldHeads));
        for (Block oldHead : oldHeads) {
            List<Block> newPreds = new ArrayList<>();
            newPreds.add(this);
            List<Block> oldPreds = oldHead.getPreds();
            if (oldPreds != null) {
                newPreds.addAll(oldPreds);
            }
            oldHead.setPreds(newPreds);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void makeTailBlock(List<Block> oldTails) {
        setSuccs(new ArrayList());
        setPreds(new ArrayList(oldTails));
        for (Block oldTail : oldTails) {
            List<Block> newSuccs = new ArrayList<>();
            newSuccs.add(this);
            List<Block> oldSuccs = oldTail.getSuccs();
            if (oldSuccs != null) {
                newSuccs.addAll(oldSuccs);
            }
            oldTail.setSuccs(newSuccs);
        }
    }

    @Override // soot.toolkits.graph.Block, java.lang.Iterable
    public Iterator<Unit> iterator() {
        return Collections.emptyIterator();
    }
}
