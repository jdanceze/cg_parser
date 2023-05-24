package soot.jimple.spark.geom.geomE;

import soot.jimple.spark.geom.dataRep.RectangleNode;
import soot.jimple.spark.geom.dataRep.SegmentNode;
import soot.jimple.spark.geom.geomPA.IFigureManager;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/geomE/GeometricManager.class */
public class GeometricManager extends IFigureManager {
    public static final int Divisions = 2;
    public static final int ONE_TO_ONE = 0;
    public static final int MANY_TO_MANY = 1;
    public static final int Undefined_Mapping = -1;
    private SegmentNode[] header = new SegmentNode[2];
    private int[] size = new int[2];
    private boolean hasNewFigure = false;

    @Override // soot.jimple.spark.geom.geomPA.IFigureManager
    public SegmentNode[] getFigures() {
        return this.header;
    }

    @Override // soot.jimple.spark.geom.geomPA.IFigureManager
    public int[] getSizes() {
        return this.size;
    }

    @Override // soot.jimple.spark.geom.geomPA.IFigureManager
    public boolean isThereUnprocessedFigures() {
        return this.hasNewFigure;
    }

    @Override // soot.jimple.spark.geom.geomPA.IFigureManager
    public void flush() {
        this.hasNewFigure = false;
        for (int i = 0; i < 2; i++) {
            SegmentNode segmentNode = this.header[i];
            while (true) {
                SegmentNode p = segmentNode;
                if (p != null && p.is_new) {
                    p.is_new = false;
                    segmentNode = p.next;
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.jimple.spark.geom.geomPA.IFigureManager
    public SegmentNode addNewFigure(int code, RectangleNode pnew) {
        SegmentNode p;
        if (checkRedundancy(code, pnew)) {
            return null;
        }
        filterOutDuplicates(code, pnew);
        if (code == 0) {
            p = getSegmentNode();
            p.copySegment(pnew);
        } else {
            p = getRectangleNode();
            ((RectangleNode) p).copyRectangle(pnew);
        }
        this.hasNewFigure = true;
        p.next = this.header[code];
        this.header[code] = p;
        int[] iArr = this.size;
        iArr[code] = iArr[code] + 1;
        return p;
    }

    @Override // soot.jimple.spark.geom.geomPA.IFigureManager
    public void mergeFigures(int buget_size) {
        if (!this.hasNewFigure) {
            return;
        }
        for (int i = 0; i < 2; i++) {
            RectangleNode p = null;
            if (this.size[i] > buget_size && this.header[i].is_new) {
                switch (i) {
                    case 0:
                        p = mergeOneToOne();
                        break;
                    case 1:
                        p = mergeManyToMany();
                        break;
                }
            }
            if (p != null) {
                if (i == 0) {
                    if (!checkRedundancy(1, p)) {
                        filterOutDuplicates(1, p);
                    }
                }
                p.next = this.header[1];
                this.header[1] = p;
                int[] iArr = this.size;
                iArr[1] = iArr[1] + 1;
            }
        }
    }

    @Override // soot.jimple.spark.geom.geomPA.IFigureManager
    public void removeUselessSegments() {
        SegmentNode p = this.header[0];
        SegmentNode q = null;
        int countAll = 0;
        while (p != null) {
            SegmentNode temp = p.next;
            if (!isContainedInRectangles(p)) {
                p.next = q;
                q = p;
                countAll++;
            } else {
                reclaimSegmentNode(p);
            }
            p = temp;
        }
        this.size[0] = countAll;
        this.header[0] = q;
    }

    private boolean isContainedInRectangles(SegmentNode pnew) {
        SegmentNode segmentNode = this.header[1];
        while (true) {
            SegmentNode p = segmentNode;
            if (p != null) {
                if (pnew.I1 >= p.I1 && pnew.I2 >= p.I2 && pnew.I1 + pnew.L <= p.I1 + p.L && pnew.I2 + pnew.L <= p.I2 + ((RectangleNode) p).L_prime) {
                    return true;
                }
                segmentNode = p.next;
            } else {
                return false;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x00ce, code lost:
        r10 = r10 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean checkRedundancy(int r8, soot.jimple.spark.geom.dataRep.RectangleNode r9) {
        /*
            r7 = this;
            r0 = r8
            if (r0 != 0) goto Lc
            r0 = r9
            r1 = r9
            long r1 = r1.L
            r0.L_prime = r1
        Lc:
            r0 = r8
            r10 = r0
            goto Ld1
        L11:
            r0 = r7
            soot.jimple.spark.geom.dataRep.SegmentNode[] r0 = r0.header
            r1 = r10
            r0 = r0[r1]
            r11 = r0
            goto Lc9
        L1c:
            r0 = r10
            switch(r0) {
                case 0: goto L34;
                case 1: goto L73;
                default: goto Lc2;
            }
        L34:
            r0 = r11
            long r0 = r0.I2
            r1 = r11
            long r1 = r1.I1
            long r0 = r0 - r1
            r1 = r9
            long r1 = r1.I2
            r2 = r9
            long r2 = r2.I1
            long r1 = r1 - r2
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto Lc2
            r0 = r9
            long r0 = r0.I1
            r1 = r11
            long r1 = r1.I1
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 < 0) goto Lc2
            r0 = r9
            long r0 = r0.I1
            r1 = r9
            long r1 = r1.L
            long r0 = r0 + r1
            r1 = r11
            long r1 = r1.I1
            r2 = r11
            long r2 = r2.L
            long r1 = r1 + r2
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 > 0) goto Lc2
            r0 = 1
            return r0
        L73:
            r0 = r9
            long r0 = r0.I1
            r1 = r11
            long r1 = r1.I1
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 < 0) goto Lc2
            r0 = r9
            long r0 = r0.I2
            r1 = r11
            long r1 = r1.I2
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 < 0) goto Lc2
            r0 = r9
            long r0 = r0.I1
            r1 = r9
            long r1 = r1.L
            long r0 = r0 + r1
            r1 = r11
            long r1 = r1.I1
            r2 = r11
            long r2 = r2.L
            long r1 = r1 + r2
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 > 0) goto Lc2
            r0 = r9
            long r0 = r0.I2
            r1 = r9
            long r1 = r1.L_prime
            long r0 = r0 + r1
            r1 = r11
            long r1 = r1.I2
            r2 = r11
            soot.jimple.spark.geom.dataRep.RectangleNode r2 = (soot.jimple.spark.geom.dataRep.RectangleNode) r2
            long r2 = r2.L_prime
            long r1 = r1 + r2
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 > 0) goto Lc2
            r0 = 1
            return r0
        Lc2:
            r0 = r11
            soot.jimple.spark.geom.dataRep.SegmentNode r0 = r0.next
            r11 = r0
        Lc9:
            r0 = r11
            if (r0 != 0) goto L1c
            int r10 = r10 + 1
        Ld1:
            r0 = r10
            r1 = 1
            if (r0 <= r1) goto L11
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.spark.geom.geomE.GeometricManager.checkRedundancy(int, soot.jimple.spark.geom.dataRep.RectangleNode):boolean");
    }

    private void filterOutDuplicates(int code, SegmentNode p) {
        for (int i = code; i > -1; i--) {
            SegmentNode pold = this.header[i];
            SegmentNode q_head = null;
            SegmentNode q_tail = null;
            int countAll = 0;
            while (pold != null) {
                boolean flag = false;
                switch (i) {
                    case 0:
                        if (code == 1) {
                            if (pold.I1 >= p.I1 && pold.I2 >= p.I2 && pold.I1 + pold.L <= p.I1 + p.L && pold.I2 + pold.L <= p.I2 + ((RectangleNode) p).L_prime) {
                                flag = true;
                                break;
                            }
                        } else if (p.I2 - p.I1 == pold.I2 - pold.I1 && pold.I1 >= p.I1 && pold.I1 + pold.L <= p.I1 + p.L) {
                            flag = true;
                            break;
                        }
                        break;
                    case 1:
                        if (pold.I1 >= p.I1 && pold.I2 >= p.I2 && pold.I1 + pold.L <= p.I1 + p.L && pold.I2 + ((RectangleNode) pold).L_prime <= p.I2 + ((RectangleNode) p).L_prime) {
                            flag = true;
                            break;
                        }
                        break;
                }
                if (!flag) {
                    if (q_head == null) {
                        q_head = pold;
                    } else {
                        q_tail.next = pold;
                    }
                    q_tail = pold;
                    countAll++;
                    pold = pold.next;
                } else if (i == 0) {
                    pold = reclaimSegmentNode(pold);
                } else {
                    pold = reclaimRectangleNode(pold);
                }
            }
            if (q_tail != null) {
                q_tail.next = null;
            }
            this.header[i] = q_head;
            this.size[i] = countAll;
        }
    }

    private RectangleNode mergeManyToMany() {
        long x_min = Long.MAX_VALUE;
        long y_min = Long.MAX_VALUE;
        long x_max = Long.MIN_VALUE;
        long y_max = Long.MIN_VALUE;
        this.header[1] = null;
        this.size[1] = 0;
        for (RectangleNode p = (RectangleNode) this.header[1]; p != null; p = (RectangleNode) reclaimRectangleNode(p)) {
            if (p.I1 < x_min) {
                x_min = p.I1;
            }
            if (p.I2 < y_min) {
                y_min = p.I2;
            }
            if (p.I1 + p.L > x_max) {
                x_max = p.I1 + p.L;
            }
            if (p.I2 + p.L_prime > y_max) {
                y_max = p.I2 + p.L_prime;
            }
        }
        RectangleNode p2 = getRectangleNode();
        p2.I1 = x_min;
        p2.I2 = y_min;
        p2.L = x_max - x_min;
        p2.L_prime = y_max - y_min;
        p2.next = null;
        return p2;
    }

    private RectangleNode mergeOneToOne() {
        long x_min = Long.MAX_VALUE;
        long y_min = Long.MAX_VALUE;
        long x_max = Long.MIN_VALUE;
        long y_max = Long.MIN_VALUE;
        this.header[0] = null;
        this.size[0] = 0;
        for (SegmentNode p = this.header[0]; p != null; p = reclaimSegmentNode(p)) {
            if (p.I1 < x_min) {
                x_min = p.I1;
            }
            if (p.I2 < y_min) {
                y_min = p.I2;
            }
            if (p.I1 + p.L > x_max) {
                x_max = p.I1 + p.L;
            }
            if (p.I2 + p.L > y_max) {
                y_max = p.I2 + p.L;
            }
        }
        RectangleNode q = getRectangleNode();
        q.I1 = x_min;
        q.I2 = y_min;
        q.L = x_max - x_min;
        q.L_prime = y_max - y_min;
        return q;
    }
}
