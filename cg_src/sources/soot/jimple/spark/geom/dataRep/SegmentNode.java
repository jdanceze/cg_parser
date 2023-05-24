package soot.jimple.spark.geom.dataRep;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/dataRep/SegmentNode.class */
public class SegmentNode implements Comparable<SegmentNode> {
    public long I1;
    public long I2;
    public long L;
    public SegmentNode next = null;
    public boolean is_new = true;

    public SegmentNode() {
    }

    public SegmentNode(SegmentNode other) {
        copySegment(other);
    }

    public void copySegment(SegmentNode other) {
        this.I1 = other.I1;
        this.I2 = other.I2;
        this.L = other.L;
    }

    public SegmentNode(long i1, long i2, long l) {
        this.I1 = i1;
        this.I2 = i2;
        this.L = l;
    }

    public boolean equals(SegmentNode other) {
        if (!(other instanceof RectangleNode) && this.I1 == other.I1 && this.I2 == other.I2 && this.L == other.L) {
            return true;
        }
        return false;
    }

    @Override // java.lang.Comparable
    public int compareTo(SegmentNode o) {
        long d = this.I1 - o.I1;
        if (d != 0) {
            return d < 0 ? -1 : 1;
        }
        long d2 = this.I2 - o.I2;
        if (d2 != 0) {
            return d2 < 0 ? -1 : 1;
        }
        long d3 = this.L - o.L;
        if (d3 != 0) {
            return d3 < 0 ? -1 : 1;
        } else if ((this instanceof RectangleNode) && (o instanceof RectangleNode)) {
            long d4 = ((RectangleNode) this).L_prime - ((RectangleNode) o).L_prime;
            if (d4 != 0) {
                return d4 < 0 ? -1 : 1;
            }
            return 0;
        } else {
            return 0;
        }
    }

    public long xEnd() {
        return this.I1 + this.L;
    }

    public long yEnd() {
        return this.I2 + this.L;
    }

    public boolean intersect(SegmentNode q) {
        if (q instanceof RectangleNode) {
            return q.intersect(this);
        }
        if (this.I2 - this.I1 == q.I2 - q.I1) {
            return this.I1 <= q.I1 ? q.I1 < this.I1 + this.L : this.I1 < q.I1 + q.L;
        }
        return false;
    }

    public boolean projYIntersect(SegmentNode q) {
        long py1 = this.I2;
        long py2 = yEnd();
        long qy1 = q.I2;
        long qy2 = q.yEnd();
        return py1 <= qy1 ? qy1 < py2 : py1 < qy2;
    }
}
