package org.apache.tools.bzip2;

import java.util.BitSet;
import javassist.compiler.TokenId;
import net.bytebuddy.agent.VirtualMachine;
import org.apache.tools.bzip2.CBZip2OutputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/bzip2/BlockSort.class */
class BlockSort {
    private static final int QSORT_STACK_SIZE = 1000;
    private static final int FALLBACK_QSORT_STACK_SIZE = 100;
    private static final int STACK_SIZE = 1000;
    private int workDone;
    private int workLimit;
    private boolean firstAttempt;
    private final int[] stack_ll = new int[1000];
    private final int[] stack_hh = new int[1000];
    private final int[] stack_dd = new int[1000];
    private final int[] mainSort_runningOrder = new int[256];
    private final int[] mainSort_copy = new int[256];
    private final boolean[] mainSort_bigDone = new boolean[256];
    private final int[] ftab = new int[VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket.Factory.MacLibrary.CS_DARWIN_USER_TEMP_DIR];
    private final char[] quadrant;
    private static final int FALLBACK_QSORT_SMALL_THRESH = 10;
    private int[] eclass;
    private static final int[] INCS = {1, 4, 13, 40, 121, TokenId.LSHIFT, 1093, 3280, 9841, 29524, 88573, 265720, 797161, 2391484};
    private static final int SMALL_THRESH = 20;
    private static final int DEPTH_THRESH = 10;
    private static final int WORK_FACTOR = 30;
    private static final int SETMASK = 2097152;
    private static final int CLEARMASK = -2097153;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BlockSort(CBZip2OutputStream.Data data) {
        this.quadrant = data.sfmap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void blockSort(CBZip2OutputStream.Data data, int last) {
        this.workLimit = 30 * last;
        this.workDone = 0;
        this.firstAttempt = true;
        if (last + 1 < 10000) {
            fallbackSort(data, last);
        } else {
            mainSort(data, last);
            if (this.firstAttempt && this.workDone > this.workLimit) {
                fallbackSort(data, last);
            }
        }
        int[] fmap = data.fmap;
        data.origPtr = -1;
        for (int i = 0; i <= last; i++) {
            if (fmap[i] == 0) {
                data.origPtr = i;
                return;
            }
        }
    }

    final void fallbackSort(CBZip2OutputStream.Data data, int last) {
        data.block[0] = data.block[last + 1];
        fallbackSort(data.fmap, data.block, last + 1);
        for (int i = 0; i < last + 1; i++) {
            int[] iArr = data.fmap;
            int i2 = i;
            iArr[i2] = iArr[i2] - 1;
        }
        for (int i3 = 0; i3 < last + 1; i3++) {
            if (data.fmap[i3] == -1) {
                data.fmap[i3] = last;
                return;
            }
        }
    }

    private void fallbackSimpleSort(int[] fmap, int[] eclass, int lo, int hi) {
        if (lo == hi) {
            return;
        }
        if (hi - lo > 3) {
            for (int i = hi - 4; i >= lo; i--) {
                int tmp = fmap[i];
                int ec_tmp = eclass[tmp];
                int j = i + 4;
                while (j <= hi && ec_tmp > eclass[fmap[j]]) {
                    fmap[j - 4] = fmap[j];
                    j += 4;
                }
                fmap[j - 4] = tmp;
            }
        }
        for (int i2 = hi - 1; i2 >= lo; i2--) {
            int tmp2 = fmap[i2];
            int ec_tmp2 = eclass[tmp2];
            int j2 = i2 + 1;
            while (j2 <= hi && ec_tmp2 > eclass[fmap[j2]]) {
                fmap[j2 - 1] = fmap[j2];
                j2++;
            }
            fmap[j2 - 1] = tmp2;
        }
    }

    private void fswap(int[] fmap, int zz1, int zz2) {
        int zztmp = fmap[zz1];
        fmap[zz1] = fmap[zz2];
        fmap[zz2] = zztmp;
    }

    private void fvswap(int[] fmap, int yyp1, int yyp2, int yyn) {
        while (yyn > 0) {
            fswap(fmap, yyp1, yyp2);
            yyp1++;
            yyp2++;
            yyn--;
        }
    }

    private int fmin(int a, int b) {
        return a < b ? a : b;
    }

    private void fpush(int sp, int lz, int hz) {
        this.stack_ll[sp] = lz;
        this.stack_hh[sp] = hz;
    }

    private int[] fpop(int sp) {
        return new int[]{this.stack_ll[sp], this.stack_hh[sp]};
    }

    private void fallbackQSort3(int[] fmap, int[] eclass, int loSt, int hiSt) {
        long med;
        long r = 0;
        int sp = 0 + 1;
        fpush(0, loSt, hiSt);
        while (sp > 0) {
            sp--;
            int[] s = fpop(sp);
            int lo = s[0];
            int hi = s[1];
            if (hi - lo < 10) {
                fallbackSimpleSort(fmap, eclass, lo, hi);
            } else {
                r = ((r * 7621) + 1) % 32768;
                long r3 = r % 3;
                if (r3 == 0) {
                    med = eclass[fmap[lo]];
                } else if (r3 == 1) {
                    med = eclass[fmap[(lo + hi) >>> 1]];
                } else {
                    med = eclass[fmap[hi]];
                }
                int ltLo = lo;
                int unLo = lo;
                int gtHi = hi;
                int unHi = hi;
                while (true) {
                    if (unLo <= unHi) {
                        int n = eclass[fmap[unLo]] - ((int) med);
                        if (n == 0) {
                            fswap(fmap, unLo, ltLo);
                            ltLo++;
                            unLo++;
                        } else if (n <= 0) {
                            unLo++;
                        }
                    }
                    while (unLo <= unHi) {
                        int n2 = eclass[fmap[unHi]] - ((int) med);
                        if (n2 == 0) {
                            fswap(fmap, unHi, gtHi);
                            gtHi--;
                            unHi--;
                        } else if (n2 < 0) {
                            break;
                        } else {
                            unHi--;
                        }
                    }
                    if (unLo > unHi) {
                        break;
                    }
                    fswap(fmap, unLo, unHi);
                    unLo++;
                    unHi--;
                }
                if (gtHi >= ltLo) {
                    int n3 = fmin(ltLo - lo, unLo - ltLo);
                    fvswap(fmap, lo, unLo - n3, n3);
                    int m = fmin(hi - gtHi, gtHi - unHi);
                    fvswap(fmap, unHi + 1, (hi - m) + 1, m);
                    int n4 = ((lo + unLo) - ltLo) - 1;
                    int m2 = (hi - (gtHi - unHi)) + 1;
                    if (n4 - lo > hi - m2) {
                        int sp2 = sp + 1;
                        fpush(sp, lo, n4);
                        sp = sp2 + 1;
                        fpush(sp2, m2, hi);
                    } else {
                        int sp3 = sp + 1;
                        fpush(sp, m2, hi);
                        sp = sp3 + 1;
                        fpush(sp3, lo, n4);
                    }
                }
            }
        }
    }

    private int[] getEclass() {
        if (this.eclass == null) {
            this.eclass = new int[this.quadrant.length / 2];
        }
        return this.eclass;
    }

    final void fallbackSort(int[] fmap, byte[] block, int nblock) {
        int nNotDone;
        int[] ftab = new int[257];
        int[] eclass = getEclass();
        for (int i = 0; i < nblock; i++) {
            eclass[i] = 0;
        }
        for (int i2 = 0; i2 < nblock; i2++) {
            int i3 = block[i2] & 255;
            ftab[i3] = ftab[i3] + 1;
        }
        for (int i4 = 1; i4 < 257; i4++) {
            int i5 = i4;
            ftab[i5] = ftab[i5] + ftab[i4 - 1];
        }
        for (int i6 = 0; i6 < nblock; i6++) {
            int j = block[i6] & 255;
            int k = ftab[j] - 1;
            ftab[j] = k;
            fmap[k] = i6;
        }
        int nBhtab = 64 + nblock;
        BitSet bhtab = new BitSet(nBhtab);
        for (int i7 = 0; i7 < 256; i7++) {
            bhtab.set(ftab[i7]);
        }
        for (int i8 = 0; i8 < 32; i8++) {
            bhtab.set(nblock + (2 * i8));
            bhtab.clear(nblock + (2 * i8) + 1);
        }
        int H = 1;
        do {
            int j2 = 0;
            for (int i9 = 0; i9 < nblock; i9++) {
                if (bhtab.get(i9)) {
                    j2 = i9;
                }
                int k2 = fmap[i9] - H;
                if (k2 < 0) {
                    k2 += nblock;
                }
                eclass[k2] = j2;
            }
            nNotDone = 0;
            int r = -1;
            while (true) {
                int k3 = bhtab.nextClearBit(r + 1);
                int l = k3 - 1;
                if (l >= nblock) {
                    break;
                }
                r = bhtab.nextSetBit(k3 + 1) - 1;
                if (r >= nblock) {
                    break;
                } else if (r > l) {
                    nNotDone += (r - l) + 1;
                    fallbackQSort3(fmap, eclass, l, r);
                    int cc = -1;
                    for (int i10 = l; i10 <= r; i10++) {
                        int cc1 = eclass[fmap[i10]];
                        if (cc != cc1) {
                            bhtab.set(i10);
                            cc = cc1;
                        }
                    }
                }
            }
            H *= 2;
            if (H > nblock) {
                return;
            }
        } while (nNotDone != 0);
    }

    private boolean mainSimpleSort(CBZip2OutputStream.Data dataShadow, int lo, int hi, int d, int lastShadow) {
        int bigN = (hi - lo) + 1;
        if (bigN < 2) {
            return this.firstAttempt && this.workDone > this.workLimit;
        }
        int hp = 0;
        while (INCS[hp] < bigN) {
            hp++;
        }
        int[] fmap = dataShadow.fmap;
        char[] quadrant = this.quadrant;
        byte[] block = dataShadow.block;
        int lastPlus1 = lastShadow + 1;
        boolean firstAttemptShadow = this.firstAttempt;
        int workLimitShadow = this.workLimit;
        int workDoneShadow = this.workDone;
        loop1: while (true) {
            hp--;
            if (hp < 0) {
                break;
            }
            int h = INCS[hp];
            int mj = (lo + h) - 1;
            int i = lo + h;
            while (i <= hi) {
                int k = 3;
                while (i <= hi) {
                    k--;
                    if (k < 0) {
                        break;
                    }
                    int v = fmap[i];
                    int vd = v + d;
                    int j = i;
                    boolean onceRun = false;
                    int a = 0;
                    while (true) {
                        if (onceRun) {
                            fmap[j] = a;
                            int i2 = j - h;
                            j = i2;
                            if (i2 <= mj) {
                                break;
                            }
                        } else {
                            onceRun = true;
                        }
                        a = fmap[j - h];
                        int i1 = a + d;
                        if (block[i1 + 1] == block[vd + 1]) {
                            if (block[i1 + 2] == block[vd + 2]) {
                                if (block[i1 + 3] == block[vd + 3]) {
                                    if (block[i1 + 4] == block[vd + 4]) {
                                        if (block[i1 + 5] == block[vd + 5]) {
                                            int i12 = i1 + 6;
                                            int i22 = vd + 6;
                                            if (block[i12] == block[i22]) {
                                                int x = lastShadow;
                                                while (true) {
                                                    if (x > 0) {
                                                        x -= 4;
                                                        if (block[i12 + 1] == block[i22 + 1]) {
                                                            if (quadrant[i12] == quadrant[i22]) {
                                                                if (block[i12 + 2] == block[i22 + 2]) {
                                                                    if (quadrant[i12 + 1] == quadrant[i22 + 1]) {
                                                                        if (block[i12 + 3] == block[i22 + 3]) {
                                                                            if (quadrant[i12 + 2] == quadrant[i22 + 2]) {
                                                                                if (block[i12 + 4] == block[i22 + 4]) {
                                                                                    if (quadrant[i12 + 3] == quadrant[i22 + 3]) {
                                                                                        i12 += 4;
                                                                                        if (i12 >= lastPlus1) {
                                                                                            i12 -= lastPlus1;
                                                                                        }
                                                                                        i22 += 4;
                                                                                        if (i22 >= lastPlus1) {
                                                                                            i22 -= lastPlus1;
                                                                                        }
                                                                                        workDoneShadow++;
                                                                                    } else if (quadrant[i12 + 3] > quadrant[i22 + 3]) {
                                                                                    }
                                                                                } else if ((block[i12 + 4] & 255) > (block[i22 + 4] & 255)) {
                                                                                }
                                                                            } else if (quadrant[i12 + 2] > quadrant[i22 + 2]) {
                                                                            }
                                                                        } else if ((block[i12 + 3] & 255) > (block[i22 + 3] & 255)) {
                                                                        }
                                                                    } else if (quadrant[i12 + 1] > quadrant[i22 + 1]) {
                                                                    }
                                                                } else if ((block[i12 + 2] & 255) > (block[i22 + 2] & 255)) {
                                                                }
                                                            } else if (quadrant[i12] > quadrant[i22]) {
                                                            }
                                                        } else if ((block[i12 + 1] & 255) > (block[i22 + 1] & 255)) {
                                                        }
                                                    }
                                                }
                                            } else if ((block[i12] & 255) <= (block[i22] & 255)) {
                                                break;
                                            }
                                        } else if ((block[i1 + 5] & 255) <= (block[vd + 5] & 255)) {
                                            break;
                                        }
                                    } else if ((block[i1 + 4] & 255) <= (block[vd + 4] & 255)) {
                                        break;
                                    }
                                } else if ((block[i1 + 3] & 255) <= (block[vd + 3] & 255)) {
                                    break;
                                }
                            } else if ((block[i1 + 2] & 255) <= (block[vd + 2] & 255)) {
                                break;
                            }
                        } else if ((block[i1 + 1] & 255) <= (block[vd + 1] & 255)) {
                            break;
                        }
                    }
                    fmap[j] = v;
                    i++;
                }
                if (firstAttemptShadow && i <= hi && workDoneShadow > workLimitShadow) {
                    break loop1;
                }
            }
        }
        this.workDone = workDoneShadow;
        return firstAttemptShadow && workDoneShadow > workLimitShadow;
    }

    private static void vswap(int[] fmap, int p1, int p2, int n) {
        int n2 = n + p1;
        while (p1 < n2) {
            int t = fmap[p1];
            int i = p1;
            p1++;
            fmap[i] = fmap[p2];
            int i2 = p2;
            p2++;
            fmap[i2] = t;
        }
    }

    private static byte med3(byte a, byte b, byte c) {
        return a < b ? b < c ? b : a < c ? c : a : b > c ? b : a > c ? c : a;
    }

    private void mainQSort3(CBZip2OutputStream.Data dataShadow, int loSt, int hiSt, int dSt, int last) {
        int i;
        int i2;
        int[] stack_ll = this.stack_ll;
        int[] stack_hh = this.stack_hh;
        int[] stack_dd = this.stack_dd;
        int[] fmap = dataShadow.fmap;
        byte[] block = dataShadow.block;
        stack_ll[0] = loSt;
        stack_hh[0] = hiSt;
        stack_dd[0] = dSt;
        int sp = 1;
        while (true) {
            sp--;
            if (sp >= 0) {
                int lo = stack_ll[sp];
                int hi = stack_hh[sp];
                int d = stack_dd[sp];
                if (hi - lo < 20 || d > 10) {
                    if (mainSimpleSort(dataShadow, lo, hi, d, last)) {
                        return;
                    }
                } else {
                    int d1 = d + 1;
                    int med = med3(block[fmap[lo] + d1], block[fmap[hi] + d1], block[fmap[(lo + hi) >>> 1] + d1]) & 255;
                    int unLo = lo;
                    int unHi = hi;
                    int ltLo = lo;
                    int gtHi = hi;
                    while (true) {
                        if (unLo <= unHi) {
                            int n = (block[fmap[unLo] + d1] & 255) - med;
                            if (n == 0) {
                                int temp = fmap[unLo];
                                int i3 = unLo;
                                unLo++;
                                fmap[i3] = fmap[ltLo];
                                int i4 = ltLo;
                                ltLo++;
                                fmap[i4] = temp;
                            } else if (n < 0) {
                                unLo++;
                            }
                        }
                        while (unLo <= unHi) {
                            int n2 = (block[fmap[unHi] + d1] & 255) - med;
                            if (n2 != 0) {
                                if (n2 <= 0) {
                                    break;
                                }
                                unHi--;
                            } else {
                                int temp2 = fmap[unHi];
                                int i5 = unHi;
                                unHi--;
                                fmap[i5] = fmap[gtHi];
                                int i6 = gtHi;
                                gtHi--;
                                fmap[i6] = temp2;
                            }
                        }
                        if (unLo > unHi) {
                            break;
                        }
                        int temp3 = fmap[unLo];
                        int i7 = unLo;
                        unLo++;
                        fmap[i7] = fmap[unHi];
                        int i8 = unHi;
                        unHi--;
                        fmap[i8] = temp3;
                    }
                    if (gtHi < ltLo) {
                        stack_ll[sp] = lo;
                        stack_hh[sp] = hi;
                        stack_dd[sp] = d1;
                        sp++;
                    } else {
                        int n3 = ltLo - lo < unLo - ltLo ? ltLo - lo : unLo - ltLo;
                        vswap(fmap, lo, unLo - n3, n3);
                        if (hi - gtHi < gtHi - unHi) {
                            i = hi;
                            i2 = gtHi;
                        } else {
                            i = gtHi;
                            i2 = unHi;
                        }
                        int m = i - i2;
                        vswap(fmap, unLo, (hi - m) + 1, m);
                        int n4 = ((lo + unLo) - ltLo) - 1;
                        int m2 = (hi - (gtHi - unHi)) + 1;
                        stack_ll[sp] = lo;
                        stack_hh[sp] = n4;
                        stack_dd[sp] = d;
                        int sp2 = sp + 1;
                        stack_ll[sp2] = n4 + 1;
                        stack_hh[sp2] = m2 - 1;
                        stack_dd[sp2] = d1;
                        int sp3 = sp2 + 1;
                        stack_ll[sp3] = m2;
                        stack_hh[sp3] = hi;
                        stack_dd[sp3] = d;
                        sp = sp3 + 1;
                    }
                }
            } else {
                return;
            }
        }
    }

    final void mainSort(CBZip2OutputStream.Data dataShadow, int lastShadow) {
        int[] runningOrder = this.mainSort_runningOrder;
        int[] copy = this.mainSort_copy;
        boolean[] bigDone = this.mainSort_bigDone;
        int[] ftab = this.ftab;
        byte[] block = dataShadow.block;
        int[] fmap = dataShadow.fmap;
        char[] quadrant = this.quadrant;
        int workLimitShadow = this.workLimit;
        boolean firstAttemptShadow = this.firstAttempt;
        int i = 65537;
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            ftab[i] = 0;
        }
        for (int i2 = 0; i2 < 20; i2++) {
            block[lastShadow + i2 + 2] = block[(i2 % (lastShadow + 1)) + 1];
        }
        int i3 = lastShadow + 20 + 1;
        while (true) {
            i3--;
            if (i3 < 0) {
                break;
            }
            quadrant[i3] = 0;
        }
        block[0] = block[lastShadow + 1];
        int c1 = block[0] & 255;
        for (int i4 = 0; i4 <= lastShadow; i4++) {
            int c2 = block[i4 + 1] & 255;
            int i5 = (c1 << 8) + c2;
            ftab[i5] = ftab[i5] + 1;
            c1 = c2;
        }
        for (int i6 = 1; i6 <= 65536; i6++) {
            int i7 = i6;
            ftab[i7] = ftab[i7] + ftab[i6 - 1];
        }
        int c12 = block[1] & 255;
        for (int i8 = 0; i8 < lastShadow; i8++) {
            int c22 = block[i8 + 2] & 255;
            int i9 = (c12 << 8) + c22;
            int i10 = ftab[i9] - 1;
            ftab[i9] = i10;
            fmap[i10] = i8;
            c12 = c22;
        }
        int i11 = ((block[lastShadow + 1] & 255) << 8) + (block[1] & 255);
        int i12 = ftab[i11] - 1;
        ftab[i11] = i12;
        fmap[i12] = lastShadow;
        int i13 = 256;
        while (true) {
            i13--;
            if (i13 < 0) {
                break;
            }
            bigDone[i13] = false;
            runningOrder[i13] = i13;
        }
        int h = 364;
        while (h != 1) {
            h /= 3;
            for (int i14 = h; i14 <= 255; i14++) {
                int vv = runningOrder[i14];
                int a = ftab[(vv + 1) << 8] - ftab[vv << 8];
                int b = h - 1;
                int j = i14;
                int i15 = runningOrder[j - h];
                while (true) {
                    int ro = i15;
                    if (ftab[(ro + 1) << 8] - ftab[ro << 8] > a) {
                        runningOrder[j] = ro;
                        j -= h;
                        if (j <= b) {
                            break;
                        }
                        i15 = runningOrder[j - h];
                    }
                }
                runningOrder[j] = vv;
            }
        }
        for (int i16 = 0; i16 <= 255; i16++) {
            int ss = runningOrder[i16];
            for (int j2 = 0; j2 <= 255; j2++) {
                int sb = (ss << 8) + j2;
                int ftab_sb = ftab[sb];
                if ((ftab_sb & 2097152) != 2097152) {
                    int lo = ftab_sb & CLEARMASK;
                    int hi = (ftab[sb + 1] & CLEARMASK) - 1;
                    if (hi > lo) {
                        mainQSort3(dataShadow, lo, hi, 2, lastShadow);
                        if (firstAttemptShadow && this.workDone > workLimitShadow) {
                            return;
                        }
                    }
                    ftab[sb] = ftab_sb | 2097152;
                }
            }
            for (int j3 = 0; j3 <= 255; j3++) {
                copy[j3] = ftab[(j3 << 8) + ss] & CLEARMASK;
            }
            int hj = ftab[(ss + 1) << 8] & CLEARMASK;
            for (int j4 = ftab[ss << 8] & CLEARMASK; j4 < hj; j4++) {
                int fmap_j = fmap[j4];
                int c13 = block[fmap_j] & 255;
                if (!bigDone[c13]) {
                    fmap[copy[c13]] = fmap_j == 0 ? lastShadow : fmap_j - 1;
                    copy[c13] = copy[c13] + 1;
                }
            }
            int j5 = 256;
            while (true) {
                j5--;
                if (j5 < 0) {
                    break;
                }
                int i17 = (j5 << 8) + ss;
                ftab[i17] = ftab[i17] | 2097152;
            }
            bigDone[ss] = true;
            if (i16 < 255) {
                int bbStart = ftab[ss << 8] & CLEARMASK;
                int bbSize = (ftab[(ss + 1) << 8] & CLEARMASK) - bbStart;
                int shifts = 0;
                while ((bbSize >> shifts) > 65534) {
                    shifts++;
                }
                for (int j6 = 0; j6 < bbSize; j6++) {
                    int a2update = fmap[bbStart + j6];
                    char qVal = (char) (j6 >> shifts);
                    quadrant[a2update] = qVal;
                    if (a2update < 20) {
                        quadrant[a2update + lastShadow + 1] = qVal;
                    }
                }
            }
        }
    }
}
