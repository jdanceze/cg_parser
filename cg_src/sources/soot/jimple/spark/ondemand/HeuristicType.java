package soot.jimple.spark.ondemand;

import soot.jimple.spark.internal.TypeManager;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/HeuristicType.class */
public enum HeuristicType {
    MANUAL,
    INCR,
    EVERY,
    MANUALINCR,
    NOTHING;
    
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$spark$ondemand$HeuristicType;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static HeuristicType[] valuesCustom() {
        HeuristicType[] valuesCustom = values();
        int length = valuesCustom.length;
        HeuristicType[] heuristicTypeArr = new HeuristicType[length];
        System.arraycopy(valuesCustom, 0, heuristicTypeArr, 0, length);
        return heuristicTypeArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$spark$ondemand$HeuristicType() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$spark$ondemand$HeuristicType;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[valuesCustom().length];
        try {
            iArr2[EVERY.ordinal()] = 3;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[INCR.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[MANUAL.ordinal()] = 1;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[MANUALINCR.ordinal()] = 4;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[NOTHING.ordinal()] = 5;
        } catch (NoSuchFieldError unused5) {
        }
        $SWITCH_TABLE$soot$jimple$spark$ondemand$HeuristicType = iArr2;
        return iArr2;
    }

    public static FieldCheckHeuristic getHeuristic(HeuristicType type, TypeManager tm, int maxPasses) {
        FieldCheckHeuristic ret = null;
        switch ($SWITCH_TABLE$soot$jimple$spark$ondemand$HeuristicType()[type.ordinal()]) {
            case 1:
                ret = new ManualFieldCheckHeuristic();
                break;
            case 2:
                ret = new InnerTypesIncrementalHeuristic(tm, maxPasses);
                break;
            case 3:
                ret = new EverythingHeuristic();
                break;
            case 4:
                ret = new ManualAndInnerHeuristic(tm, maxPasses);
                break;
            case 5:
                ret = new NothingHeuristic();
                break;
        }
        return ret;
    }
}
