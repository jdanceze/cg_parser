package soot.jimple.spark.geom.geomPA;

import soot.jimple.spark.geom.dataRep.RectangleNode;
import soot.jimple.spark.geom.dataRep.SegmentNode;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/geomPA/IFigureManager.class */
public abstract class IFigureManager {
    private static SegmentNode segHeader = null;
    private static SegmentNode rectHeader = null;

    public abstract SegmentNode[] getFigures();

    public abstract int[] getSizes();

    public abstract boolean isThereUnprocessedFigures();

    public abstract void flush();

    public abstract SegmentNode addNewFigure(int i, RectangleNode rectangleNode);

    public abstract void mergeFigures(int i);

    public abstract void removeUselessSegments();

    /* JADX INFO: Access modifiers changed from: protected */
    public static SegmentNode getSegmentNode() {
        SegmentNode ret;
        if (segHeader != null) {
            ret = segHeader;
            segHeader = ret.next;
            ret.next = null;
            ret.is_new = true;
        } else {
            ret = new SegmentNode();
        }
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static RectangleNode getRectangleNode() {
        RectangleNode ret;
        if (rectHeader != null) {
            ret = (RectangleNode) rectHeader;
            rectHeader = ret.next;
            ret.next = null;
            ret.is_new = true;
        } else {
            ret = new RectangleNode();
        }
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static SegmentNode reclaimSegmentNode(SegmentNode p) {
        SegmentNode q = p.next;
        p.next = segHeader;
        segHeader = p;
        return q;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static SegmentNode reclaimRectangleNode(SegmentNode p) {
        SegmentNode q = p.next;
        p.next = rectHeader;
        rectHeader = p;
        return q;
    }

    public static void cleanCache() {
        segHeader = null;
        rectHeader = null;
    }
}
