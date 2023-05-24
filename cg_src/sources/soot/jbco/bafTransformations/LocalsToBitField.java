package soot.jbco.bafTransformations;

import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.PatchingChain;
import soot.PrimType;
import soot.ShortType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.baf.Baf;
import soot.baf.IdentityInst;
import soot.baf.IncInst;
import soot.baf.LoadInst;
import soot.baf.StoreInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.Rand;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.ParameterRef;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jbco/bafTransformations/LocalsToBitField.class */
public class LocalsToBitField extends BodyTransformer implements IJbcoTransform {
    int replaced = 0;
    int locals = 0;
    public static String[] dependancies = {"jtp.jbco_jl", "bb.jbco_plvb", "bb.jbco_ful", "bb.lp"};
    public static String name = "bb.jbco_plvb";

    @Override // soot.jbco.IJbcoTransform
    public String[] getDependencies() {
        return dependancies;
    }

    @Override // soot.jbco.IJbcoTransform
    public String getName() {
        return name;
    }

    @Override // soot.jbco.IJbcoTransform
    public void outputSummary() {
        out.println("Local fields inserted into bitfield: " + this.replaced);
        out.println("Original number of locals: " + this.locals);
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        IncInst ii;
        Local bafLoc;
        Local nloc;
        int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
        if (weight == 0) {
            return;
        }
        Chain<Local> bLocals = b.getLocals();
        PatchingChain<Unit> u = b.getUnits();
        Unit first = null;
        List<Value> params = new ArrayList<>();
        Iterator<Unit> uit = u.iterator();
        while (uit.hasNext()) {
            Unit unit = uit.next();
            if (unit instanceof IdentityInst) {
                IdentityInst ii2 = (IdentityInst) unit;
                if (ii2.getRightOpBox().getValue() instanceof ParameterRef) {
                    Value v = ii2.getLeftOp();
                    if (v instanceof Local) {
                        params.add(v);
                        first = unit;
                    }
                }
            }
        }
        Map<Local, Local> bafToJLocals = new HashMap<>();
        for (Local jl : Main.methods2JLocals.get(b.getMethod())) {
            Iterator<Local> blocIt = bLocals.iterator();
            while (true) {
                if (!blocIt.hasNext()) {
                    break;
                }
                Local bl = blocIt.next();
                if (bl.getName().equals(jl.getName())) {
                    bafToJLocals.put(bl, jl);
                    break;
                }
            }
        }
        List<Local> booleans = new ArrayList<>();
        List<Local> bytes = new ArrayList<>();
        List<Local> chars = new ArrayList<>();
        List<Local> ints = new ArrayList<>();
        Map<Local, Integer> sizes = new HashMap<>();
        for (Local bl2 : bLocals) {
            if (!params.contains(bl2)) {
                this.locals++;
                Local jlocal = bafToJLocals.get(bl2);
                if (jlocal != null) {
                    Type t = jlocal.getType();
                    if ((t instanceof PrimType) && !(t instanceof DoubleType) && !(t instanceof LongType) && Rand.getInt(10) <= weight) {
                        if (t instanceof BooleanType) {
                            booleans.add(bl2);
                            sizes.put(bl2, 1);
                        } else if (t instanceof ByteType) {
                            bytes.add(bl2);
                            sizes.put(bl2, 8);
                        } else if (t instanceof CharType) {
                            chars.add(bl2);
                            sizes.put(bl2, 16);
                        } else if (t instanceof IntType) {
                            ints.add(bl2);
                            sizes.put(bl2, 32);
                        }
                    }
                }
            }
        }
        int count = 0;
        Map<Local, Local> bafToNewLocs = new HashMap<>();
        Map<Local, Map<Local, Integer>> newLocs = new HashMap<>();
        for (int total = booleans.size() + (bytes.size() * 8) + (chars.size() * 16) + (ints.size() * 32); total >= 32 && booleans.size() + bytes.size() + chars.size() + ints.size() > 2; total = booleans.size() + (bytes.size() * 8) + (chars.size() * 16) + (ints.size() * 32)) {
            int i = count;
            count++;
            Local nloc2 = Baf.v().newLocal("newDumby" + i, LongType.v());
            Map<Local, Integer> nlocMap = new HashMap<>();
            boolean done = false;
            int index = 0;
            while (index < 64 && !done) {
                int max = 64 - index;
                int rand = Rand.getInt(max > 31 ? 4 : max > 15 ? 3 : max > 7 ? 2 : 1);
                int max2 = index;
                switch (rand) {
                    case 3:
                        if (ints.size() > 0) {
                            Local l = ints.remove(Rand.getInt(ints.size()));
                            nlocMap.put(l, Integer.valueOf(index));
                            bafToNewLocs.put(l, nloc2);
                            index = getNewIndex(index + 32, ints, chars, bytes, booleans);
                            break;
                        }
                    case 2:
                        if (chars.size() > 0) {
                            Local l2 = chars.remove(Rand.getInt(chars.size()));
                            nlocMap.put(l2, Integer.valueOf(index));
                            bafToNewLocs.put(l2, nloc2);
                            index = getNewIndex(index + 16, ints, chars, bytes, booleans);
                            break;
                        }
                    case 1:
                        if (bytes.size() > 0) {
                            Local l3 = bytes.remove(Rand.getInt(bytes.size()));
                            nlocMap.put(l3, Integer.valueOf(index));
                            bafToNewLocs.put(l3, nloc2);
                            index = getNewIndex(index + 8, ints, chars, bytes, booleans);
                            break;
                        }
                    case 0:
                        if (booleans.size() > 0) {
                            Local l4 = booleans.remove(Rand.getInt(booleans.size()));
                            nlocMap.put(l4, Integer.valueOf(index));
                            bafToNewLocs.put(l4, nloc2);
                            index = getNewIndex(index + 1, ints, chars, bytes, booleans);
                            break;
                        }
                        break;
                }
                if (max2 == index) {
                    done = true;
                }
            }
            newLocs.put(nloc2, nlocMap);
            bLocals.add(nloc2);
            if (first != null) {
                u.insertAfter(Baf.v().newStoreInst(LongType.v(), nloc2), first);
                u.insertAfter(Baf.v().newPushInst(LongConstant.v(0L)), first);
            } else {
                u.addFirst((PatchingChain<Unit>) Baf.v().newStoreInst(LongType.v(), nloc2));
                u.addFirst((PatchingChain<Unit>) Baf.v().newPushInst(LongConstant.v(0L)));
            }
        }
        if (bafToNewLocs.size() == 0) {
            return;
        }
        Iterator<Unit> it = u.snapshotIterator();
        while (it.hasNext()) {
            Unit unit2 = it.next();
            if (unit2 instanceof StoreInst) {
                StoreInst si = (StoreInst) unit2;
                Local bafLoc2 = si.getLocal();
                Local nloc3 = bafToNewLocs.get(bafLoc2);
                if (nloc3 != null) {
                    Local jloc = bafToJLocals.get(bafLoc2);
                    int index2 = newLocs.get(nloc3).get(bafLoc2).intValue();
                    int size = sizes.get(bafLoc2).intValue();
                    long longmask = ((size == 1 ? 1L : size == 8 ? 255L : size == 16 ? 65535L : ExpandableListView.PACKED_POSITION_VALUE_NULL) << index2) ^ (-1);
                    u.insertBefore(Baf.v().newPrimitiveCastInst(jloc.getType(), LongType.v()), unit2);
                    if (index2 > 0) {
                        u.insertBefore(Baf.v().newPushInst(IntConstant.v(index2)), unit2);
                        u.insertBefore(Baf.v().newShlInst(LongType.v()), unit2);
                    }
                    u.insertBefore(Baf.v().newPushInst(LongConstant.v(longmask ^ (-1))), unit2);
                    u.insertBefore(Baf.v().newAndInst(LongType.v()), unit2);
                    u.insertBefore(Baf.v().newLoadInst(LongType.v(), nloc3), unit2);
                    u.insertBefore(Baf.v().newPushInst(LongConstant.v(longmask)), unit2);
                    u.insertBefore(Baf.v().newAndInst(LongType.v()), unit2);
                    u.insertBefore(Baf.v().newXorInst(LongType.v()), unit2);
                    u.insertBefore(Baf.v().newStoreInst(LongType.v(), nloc3), unit2);
                    u.remove(unit2);
                }
            } else if (unit2 instanceof LoadInst) {
                LoadInst li = (LoadInst) unit2;
                Local bafLoc3 = li.getLocal();
                Local nloc4 = bafToNewLocs.get(bafLoc3);
                if (nloc4 != null) {
                    int index3 = newLocs.get(nloc4).get(bafLoc3).intValue();
                    int size2 = sizes.get(bafLoc3).intValue();
                    long j = size2 == 1 ? 1L : size2 == 8 ? 255L : size2 == 16 ? 65535L : ExpandableListView.PACKED_POSITION_VALUE_NULL;
                    u.insertBefore(Baf.v().newLoadInst(LongType.v(), nloc4), unit2);
                    u.insertBefore(Baf.v().newPushInst(LongConstant.v(j << index3)), unit2);
                    u.insertBefore(Baf.v().newAndInst(LongType.v()), unit2);
                    if (index3 > 0) {
                        u.insertBefore(Baf.v().newPushInst(IntConstant.v(index3)), unit2);
                        u.insertBefore(Baf.v().newShrInst(LongType.v()), unit2);
                    }
                    Type origType = bafToJLocals.get(bafLoc3).getType();
                    Type t2 = getType(origType);
                    u.insertBefore(Baf.v().newPrimitiveCastInst(LongType.v(), t2), unit2);
                    if (!(origType instanceof IntType) && !(origType instanceof BooleanType)) {
                        u.insertBefore(Baf.v().newPrimitiveCastInst(t2, origType), unit2);
                    }
                    u.remove(unit2);
                }
            } else if ((unit2 instanceof IncInst) && (nloc = bafToNewLocs.get((bafLoc = (ii = (IncInst) unit2).getLocal()))) != null) {
                Type jlocType = getType(bafToJLocals.get(bafLoc).getType());
                int index4 = newLocs.get(nloc).get(bafLoc).intValue();
                int size3 = sizes.get(bafLoc).intValue();
                long longmask2 = (size3 == 1 ? 1L : size3 == 8 ? 255L : size3 == 16 ? 65535L : ExpandableListView.PACKED_POSITION_VALUE_NULL) << index4;
                u.insertBefore(Baf.v().newPushInst(ii.getConstant()), unit2);
                u.insertBefore(Baf.v().newLoadInst(LongType.v(), nloc), unit2);
                u.insertBefore(Baf.v().newPushInst(LongConstant.v(longmask2)), unit2);
                u.insertBefore(Baf.v().newAndInst(LongType.v()), unit2);
                if (index4 > 0) {
                    u.insertBefore(Baf.v().newPushInst(IntConstant.v(index4)), unit2);
                    u.insertBefore(Baf.v().newShrInst(LongType.v()), unit2);
                }
                u.insertBefore(Baf.v().newPrimitiveCastInst(LongType.v(), ii.getConstant().getType()), unit2);
                u.insertBefore(Baf.v().newAddInst(ii.getConstant().getType()), unit2);
                u.insertBefore(Baf.v().newPrimitiveCastInst(jlocType, LongType.v()), unit2);
                if (index4 > 0) {
                    u.insertBefore(Baf.v().newPushInst(IntConstant.v(index4)), unit2);
                    u.insertBefore(Baf.v().newShlInst(LongType.v()), unit2);
                }
                u.insertBefore(Baf.v().newLoadInst(LongType.v(), nloc), unit2);
                u.insertBefore(Baf.v().newPushInst(LongConstant.v(longmask2 ^ (-1))), unit2);
                u.insertBefore(Baf.v().newAndInst(LongType.v()), unit2);
                u.insertBefore(Baf.v().newXorInst(LongType.v()), unit2);
                u.insertBefore(Baf.v().newStoreInst(LongType.v(), nloc), unit2);
                u.remove(unit2);
            }
        }
        Iterator<Local> localIterator = bLocals.snapshotIterator();
        while (localIterator.hasNext()) {
            Local l5 = localIterator.next();
            if (bafToNewLocs.containsKey(l5)) {
                bLocals.remove(l5);
                this.replaced++;
            }
        }
    }

    private Type getType(Type t) {
        if ((t instanceof BooleanType) || (t instanceof CharType) || (t instanceof ShortType) || (t instanceof ByteType)) {
            return IntType.v();
        }
        return t;
    }

    private int getNewIndex(int index, List<Local> ints, List<Local> chars, List<Local> bytes, List<Local> booleans) {
        int max = 0;
        if (booleans.size() > 0 && index < 63) {
            max = 64;
        } else if (bytes.size() > 0 && index < 56) {
            max = 57;
        } else if (chars.size() > 0 && index < 48) {
            max = 49;
        } else if (ints.size() > 0 && index < 32) {
            max = 33;
        }
        if (max != 0) {
            int rand = Rand.getInt(4);
            int max2 = max - index;
            if (max2 > rand) {
                max2 = rand;
            } else if (max2 != 1) {
                max2 = Rand.getInt(max2);
            }
            index += max2;
        }
        return index;
    }
}
