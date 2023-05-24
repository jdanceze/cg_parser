package soot.toolkits.graph;

import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/Block.class */
public class Block implements Iterable<Unit> {
    private static final Logger logger = LoggerFactory.getLogger(Block.class);
    private Unit mHead;
    private Unit mTail;
    private final Body mBody;
    private List<Block> mPreds;
    private List<Block> mSuccessors;
    private int mBlockLength;
    private int mIndexInMethod;

    public Block(Unit aHead, Unit aTail, Body aBody, int aIndexInMethod, int aBlockLength, BlockGraph aBlockGraph) {
        this.mBlockLength = 0;
        this.mIndexInMethod = 0;
        this.mHead = aHead;
        this.mTail = aTail;
        this.mBody = aBody;
        this.mIndexInMethod = aIndexInMethod;
        this.mBlockLength = aBlockLength;
    }

    public Body getBody() {
        return this.mBody;
    }

    @Override // java.lang.Iterable
    public Iterator<Unit> iterator() {
        if (this.mBody == null) {
            return null;
        }
        return this.mBody.getUnits().iterator(this.mHead, this.mTail);
    }

    public void insertBefore(Unit toInsert, Unit point) {
        if (point == this.mHead) {
            this.mHead = toInsert;
        }
        this.mBody.getUnits().insertBefore(toInsert, point);
    }

    public void insertAfter(Unit toInsert, Unit point) {
        if (point == this.mTail) {
            this.mTail = toInsert;
        }
        this.mBody.getUnits().insertAfter(toInsert, point);
    }

    public boolean remove(Unit item) {
        Chain<Unit> methodBody = this.mBody.getUnits();
        if (item == this.mHead) {
            this.mHead = methodBody.getSuccOf(item);
        } else if (item == this.mTail) {
            this.mTail = methodBody.getPredOf(item);
        }
        return methodBody.remove(item);
    }

    public Unit getSuccOf(Unit aItem) {
        if (aItem == this.mTail) {
            return null;
        }
        return this.mBody.getUnits().getSuccOf((UnitPatchingChain) aItem);
    }

    public Unit getPredOf(Unit aItem) {
        if (aItem == this.mHead) {
            return null;
        }
        return this.mBody.getUnits().getPredOf((UnitPatchingChain) aItem);
    }

    public void setIndexInMethod(int aIndexInMethod) {
        this.mIndexInMethod = aIndexInMethod;
    }

    public int getIndexInMethod() {
        return this.mIndexInMethod;
    }

    public Unit getHead() {
        return this.mHead;
    }

    public Unit getTail() {
        return this.mTail;
    }

    public void setPreds(List<Block> preds) {
        this.mPreds = preds;
    }

    public List<Block> getPreds() {
        return this.mPreds;
    }

    public void setSuccs(List<Block> succs) {
        this.mSuccessors = succs;
    }

    public List<Block> getSuccs() {
        return this.mSuccessors;
    }

    public String toShortString() {
        return "Block #" + this.mIndexInMethod;
    }

    public String toString() {
        Unit someUnit;
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("Block ").append(this.mIndexInMethod).append(':').append(System.lineSeparator());
        strBuf.append("[preds: ");
        if (this.mPreds != null) {
            for (Block b : this.mPreds) {
                strBuf.append(b.getIndexInMethod()).append(' ');
            }
        }
        strBuf.append("] [succs: ");
        if (this.mSuccessors != null) {
            for (Block b2 : this.mSuccessors) {
                strBuf.append(b2.getIndexInMethod()).append(' ');
            }
        }
        strBuf.append(']').append(System.lineSeparator());
        Unit tail = this.mTail;
        Iterator<Unit> basicBlockIt = this.mBody.getUnits().iterator(this.mHead, tail);
        if (basicBlockIt.hasNext()) {
            strBuf.append(basicBlockIt.next().toString()).append(';').append(System.lineSeparator());
            while (basicBlockIt.hasNext() && (someUnit = basicBlockIt.next()) != tail) {
                strBuf.append(someUnit.toString()).append(';').append(System.lineSeparator());
            }
            if (tail == null) {
                strBuf.append("error: null tail found; block length: ").append(this.mBlockLength).append(System.lineSeparator());
            } else if (tail != this.mHead) {
                strBuf.append(tail.toString()).append(';').append(System.lineSeparator());
            }
        }
        return strBuf.toString();
    }
}
