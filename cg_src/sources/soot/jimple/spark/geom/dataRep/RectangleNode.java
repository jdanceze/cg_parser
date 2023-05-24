package soot.jimple.spark.geom.dataRep;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/dataRep/RectangleNode.class */
public class RectangleNode extends SegmentNode {
    public long L_prime;

    public RectangleNode() {
    }

    public RectangleNode(RectangleNode other) {
        copyRectangle(other);
    }

    public void copyRectangle(RectangleNode other) {
        this.I1 = other.I1;
        this.I2 = other.I2;
        this.L = other.L;
        this.L_prime = other.L_prime;
    }

    public RectangleNode(long I1, long I2, long L, long LL) {
        super(I1, I2, L);
        this.L_prime = LL;
    }

    public boolean equals(RectangleNode other) {
        if (this.I1 == other.I1 && this.I2 == other.I2 && this.L == other.L && this.L_prime == other.L_prime) {
            return true;
        }
        return false;
    }

    @Override // soot.jimple.spark.geom.dataRep.SegmentNode
    public long yEnd() {
        return this.I2 + this.L_prime;
    }

    @Override // soot.jimple.spark.geom.dataRep.SegmentNode
    public boolean intersect(SegmentNode q) {
        if (q instanceof SegmentNode) {
            if (point_within_rectangle(q.I1, q.I2, this) || point_within_rectangle((q.I1 + q.L) - 1, (q.I2 + q.L) - 1, this) || diagonal_line_intersect_horizontal(q, this.I1, this.I2, this.L) || diagonal_line_intersect_horizontal(q, this.I1, (this.I2 + this.L_prime) - 1, this.L) || diagonal_line_intersect_vertical(q, this.I1, this.I2, this.L_prime) || diagonal_line_intersect_vertical(q, (this.I1 + this.L) - 1, this.I2, this.L_prime)) {
                return true;
            }
            return false;
        }
        RectangleNode rect_q = (RectangleNode) q;
        if (this.I2 >= rect_q.I2 + rect_q.L_prime || this.I2 + this.L_prime <= rect_q.I2 || this.I1 + this.L <= rect_q.I1 || this.I1 >= rect_q.I1 + rect_q.L) {
            return false;
        }
        return true;
    }

    private boolean point_within_rectangle(long x, long y, RectangleNode rect) {
        if (x >= rect.I1 && x < rect.I1 + rect.L && y >= rect.I2 && y < rect.I2 + rect.L_prime) {
            return true;
        }
        return false;
    }

    private boolean diagonal_line_intersect_vertical(SegmentNode p, long x, long y, long L) {
        if (x >= p.I1 && x < p.I1 + p.L) {
            long y_cross = (x - p.I1) + p.I2;
            if (y_cross >= y && y_cross < y + L) {
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean diagonal_line_intersect_horizontal(SegmentNode p, long x, long y, long L) {
        if (y >= p.I2 && y < p.I2 + p.L) {
            long x_cross = (y - p.I2) + p.I1;
            if (x_cross >= x && x_cross < x + L) {
                return true;
            }
            return false;
        }
        return false;
    }
}
