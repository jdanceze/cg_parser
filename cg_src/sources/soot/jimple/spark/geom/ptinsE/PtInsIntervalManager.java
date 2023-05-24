package soot.jimple.spark.geom.ptinsE;

import soot.jimple.spark.geom.dataRep.RectangleNode;
import soot.jimple.spark.geom.dataRep.SegmentNode;
import soot.jimple.spark.geom.geomPA.Constants;
import soot.jimple.spark.geom.geomPA.IFigureManager;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/ptinsE/PtInsIntervalManager.class */
public class PtInsIntervalManager extends IFigureManager {
    public static final int Divisions = 3;
    public static final int ALL_TO_ALL = -1;
    public static final int ALL_TO_MANY = 0;
    public static final int MANY_TO_ALL = 1;
    public static final int ONE_TO_ONE = 2;
    int[] size = new int[3];
    SegmentNode[] header = new SegmentNode[3];
    private boolean hasNewObject = false;

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
        return this.hasNewObject;
    }

    @Override // soot.jimple.spark.geom.geomPA.IFigureManager
    public void flush() {
        this.hasNewObject = false;
        for (int i = 0; i < 3; i++) {
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

    @Override // soot.jimple.spark.geom.geomPA.IFigureManager
    public SegmentNode addNewFigure(int code, RectangleNode pnew) {
        SegmentNode p;
        if (code == -1) {
            if (this.header[0] != null && this.header[0].I2 == 0) {
                return null;
            }
            p = new SegmentNode();
            p.I2 = 0L;
            p.I1 = 0L;
            p.L = Constants.MAX_CONTEXTS;
            for (int i = 0; i < 3; i++) {
                this.size[i] = 0;
                this.header[i] = null;
            }
        } else {
            if (code == 0 || code == 2) {
                SegmentNode segmentNode = this.header[0];
                while (true) {
                    SegmentNode p2 = segmentNode;
                    if (p2 == null) {
                        break;
                    } else if (p2.I2 <= pnew.I2 && p2.I2 + p2.L >= pnew.I2 + pnew.L) {
                        return null;
                    } else {
                        segmentNode = p2.next;
                    }
                }
            }
            if (code == 1 || code == 2) {
                SegmentNode segmentNode2 = this.header[1];
                while (true) {
                    SegmentNode p3 = segmentNode2;
                    if (p3 == null) {
                        break;
                    } else if (p3.I1 <= pnew.I1 && p3.I1 + p3.L >= pnew.I1 + pnew.L) {
                        return null;
                    } else {
                        segmentNode2 = p3.next;
                    }
                }
            }
            if (code == 2) {
                SegmentNode segmentNode3 = this.header[2];
                while (true) {
                    SegmentNode p4 = segmentNode3;
                    if (p4 == null) {
                        break;
                    } else if (p4.I1 - p4.I2 == pnew.I1 - pnew.I2 && p4.I1 <= pnew.I1 && p4.I1 + p4.L >= pnew.I1 + pnew.L) {
                        return null;
                    } else {
                        segmentNode3 = p4.next;
                    }
                }
            }
            p = new SegmentNode(pnew);
            if (code == 0) {
                clean_garbage_all_to_many(p);
            } else if (code == 1) {
                clean_garbage_many_to_all(p);
            } else {
                clean_garbage_one_to_one(p);
            }
        }
        this.hasNewObject = true;
        int[] iArr = this.size;
        iArr[code] = iArr[code] + 1;
        p.next = this.header[code];
        this.header[code] = p;
        return p;
    }

    @Override // soot.jimple.spark.geom.geomPA.IFigureManager
    public void mergeFigures(int upperSize) {
        if (this.size[2] > upperSize && this.header[2].is_new) {
            SegmentNode p = generate_all_to_many(this.header[2]);
            clean_garbage_all_to_many(p);
            p.next = this.header[0];
            this.header[0] = p;
            this.header[2] = null;
            int[] iArr = this.size;
            iArr[0] = iArr[0] + 1;
            this.size[2] = 0;
        }
        if (this.size[1] > upperSize && this.header[1].is_new) {
            this.header[1] = generate_many_to_all(this.header[1]);
            this.size[1] = 1;
        }
        if (this.size[0] > upperSize && this.header[0].is_new) {
            this.header[0] = generate_all_to_many(this.header[0]);
            this.size[0] = 1;
        }
    }

    @Override // soot.jimple.spark.geom.geomPA.IFigureManager
    public void removeUselessSegments() {
        SegmentNode p = this.header[2];
        this.size[2] = 0;
        SegmentNode q = null;
        while (p != null) {
            boolean contained = false;
            for (int i = 0; i < 2; i++) {
                SegmentNode segmentNode = this.header[i];
                while (true) {
                    SegmentNode temp = segmentNode;
                    if (temp != null) {
                        if ((temp.I1 == 0 || (temp.I1 <= p.I1 && temp.I1 + temp.L >= p.I1 + p.L)) && (temp.I2 == 0 || (temp.I2 <= p.I2 && temp.I2 + temp.L >= p.I2 + p.L))) {
                            break;
                        }
                        segmentNode = temp.next;
                    }
                }
                contained = true;
            }
            SegmentNode temp2 = p.next;
            if (!contained) {
                p.next = q;
                q = p;
                int[] iArr = this.size;
                iArr[2] = iArr[2] + 1;
            }
            p = temp2;
        }
        this.header[2] = q;
    }

    private SegmentNode generate_all_to_many(SegmentNode mp) {
        long left = mp.I2;
        long right = left + mp.L;
        SegmentNode segmentNode = mp.next;
        while (true) {
            SegmentNode p = segmentNode;
            if (p != null) {
                if (p.I2 < left) {
                    left = p.I2;
                }
                long t = p.I2 + p.L;
                if (t > right) {
                    right = t;
                }
                segmentNode = p.next;
            } else {
                mp.I1 = 0L;
                mp.I2 = left;
                mp.L = right - left;
                mp.next = null;
                return mp;
            }
        }
    }

    private SegmentNode generate_many_to_all(SegmentNode mp) {
        long left = mp.I1;
        long right = left + mp.L;
        SegmentNode segmentNode = mp.next;
        while (true) {
            SegmentNode p = segmentNode;
            if (p != null) {
                if (p.I1 < left) {
                    left = p.I1;
                }
                long t = p.I1 + p.L;
                if (t > right) {
                    right = t;
                }
                segmentNode = p.next;
            } else {
                mp.I1 = left;
                mp.I2 = 0L;
                mp.L = right - left;
                mp.next = null;
                return mp;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x008d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void clean_garbage_many_to_all(soot.jimple.spark.geom.dataRep.SegmentNode r7) {
        /*
            Method dump skipped, instructions count: 205
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.spark.geom.ptinsE.PtInsIntervalManager.clean_garbage_many_to_all(soot.jimple.spark.geom.dataRep.SegmentNode):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x008f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void clean_garbage_all_to_many(soot.jimple.spark.geom.dataRep.SegmentNode r7) {
        /*
            Method dump skipped, instructions count: 207
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.spark.geom.ptinsE.PtInsIntervalManager.clean_garbage_all_to_many(soot.jimple.spark.geom.dataRep.SegmentNode):void");
    }

    private void clean_garbage_one_to_one(SegmentNode predator) {
        SegmentNode q = null;
        SegmentNode p = null;
        int num = 0;
        for (SegmentNode list = this.header[2]; list != null; list = list.next) {
            long L = list.L;
            if (predator.I2 - predator.I1 != list.I2 - list.I1 || predator.I1 > list.I1 || predator.I1 + predator.L < list.I2 + L) {
                if (q == null) {
                    SegmentNode segmentNode = list;
                    q = segmentNode;
                    p = segmentNode;
                } else {
                    q.next = list;
                    q = list;
                }
                num++;
            }
        }
        if (q != null) {
            q.next = null;
        }
        this.header[2] = p;
        this.size[2] = num;
    }
}
