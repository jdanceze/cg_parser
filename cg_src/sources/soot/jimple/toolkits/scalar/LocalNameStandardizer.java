package soot.jimple.toolkits.scalar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import soot.ErroneousType;
import soot.FloatType;
import soot.G;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.NullType;
import soot.PhaseOptions;
import soot.ShortType;
import soot.Singletons;
import soot.StmtAddressType;
import soot.Type;
import soot.UnknownType;
import soot.Value;
import soot.ValueBox;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/LocalNameStandardizer.class */
public class LocalNameStandardizer extends BodyTransformer {
    public LocalNameStandardizer(Singletons.Global g) {
    }

    public static LocalNameStandardizer v() {
        return G.v().soot_jimple_toolkits_scalar_LocalNameStandardizer();
    }

    private static int digits(int n) {
        int len = String.valueOf(n).length();
        return n < 0 ? len - 1 : len;
    }

    private static String genName(String prefix, String type, int n, int digits) {
        return String.format("%s%s%0" + digits + "d", prefix, type, Integer.valueOf(n));
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        boolean sortLocals = PhaseOptions.getBoolean(options, "sort-locals");
        if (sortLocals) {
            Chain<Local> locals = body.getLocals();
            ArrayList<Local> sortedLocals = new ArrayList<>(locals);
            Collections.sort(sortedLocals, new Comparator<Local>(body) { // from class: soot.jimple.toolkits.scalar.LocalNameStandardizer.1
                private Map<Local, Integer> firstOccuranceCache = new HashMap();
                private final List<ValueBox> defs;

                {
                    this.defs = body.getDefBoxes();
                }

                @Override // java.util.Comparator
                public int compare(Local arg0, Local arg1) {
                    int ret = arg0.getType().toString().compareTo(arg1.getType().toString());
                    if (ret == 0) {
                        ret = Integer.compare(getFirstOccurance(arg0), getFirstOccurance(arg1));
                    }
                    return ret;
                }

                private int getFirstOccurance(Local l) {
                    Integer cur = this.firstOccuranceCache.get(l);
                    if (cur != null) {
                        return cur.intValue();
                    }
                    int count = 0;
                    int first = -1;
                    Iterator<ValueBox> it = this.defs.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        ValueBox vb = it.next();
                        Value v = vb.getValue();
                        if ((v instanceof Local) && l.equals(v)) {
                            first = count;
                            break;
                        }
                        count++;
                    }
                    this.firstOccuranceCache.put(l, Integer.valueOf(first));
                    return first;
                }
            });
            locals.clear();
            locals.addAll(sortedLocals);
        }
        if (!PhaseOptions.getBoolean(options, "only-stack-locals")) {
            BooleanType booleanType = BooleanType.v();
            ByteType byteType = ByteType.v();
            ShortType shortType = ShortType.v();
            CharType charType = CharType.v();
            IntType intType = IntType.v();
            LongType longType = LongType.v();
            DoubleType doubleType = DoubleType.v();
            FloatType floatType = FloatType.v();
            ErroneousType erroneousType = ErroneousType.v();
            UnknownType unknownType = UnknownType.v();
            StmtAddressType stmtAddressType = StmtAddressType.v();
            NullType nullType = NullType.v();
            Chain<Local> locals2 = body.getLocals();
            int maxDigits = sortLocals ? digits(locals2.size()) : 1;
            int objectCount = 0;
            int intCount = 0;
            int longCount = 0;
            int floatCount = 0;
            int doubleCount = 0;
            int addressCount = 0;
            int errorCount = 0;
            int nullCount = 0;
            for (Local l : locals2) {
                String prefix = l.getName().startsWith("$") ? "$" : "";
                Type type = l.getType();
                if (booleanType.equals(type)) {
                    int i = intCount;
                    intCount++;
                    l.setName(genName(prefix, "z", i, maxDigits));
                } else if (byteType.equals(type)) {
                    int i2 = longCount;
                    longCount++;
                    l.setName(genName(prefix, "b", i2, maxDigits));
                } else if (shortType.equals(type)) {
                    int i3 = longCount;
                    longCount++;
                    l.setName(genName(prefix, "s", i3, maxDigits));
                } else if (charType.equals(type)) {
                    int i4 = longCount;
                    longCount++;
                    l.setName(genName(prefix, "c", i4, maxDigits));
                } else if (intType.equals(type)) {
                    int i5 = longCount;
                    longCount++;
                    l.setName(genName(prefix, "i", i5, maxDigits));
                } else if (longType.equals(type)) {
                    int i6 = longCount;
                    longCount++;
                    l.setName(genName(prefix, "l", i6, maxDigits));
                } else if (doubleType.equals(type)) {
                    int i7 = doubleCount;
                    doubleCount++;
                    l.setName(genName(prefix, "d", i7, maxDigits));
                } else if (floatType.equals(type)) {
                    int i8 = floatCount;
                    floatCount++;
                    l.setName(genName(prefix, "f", i8, maxDigits));
                } else if (stmtAddressType.equals(type)) {
                    int i9 = addressCount;
                    addressCount++;
                    l.setName(genName(prefix, "a", i9, maxDigits));
                } else if (erroneousType.equals(type) || unknownType.equals(type)) {
                    int i10 = errorCount;
                    errorCount++;
                    l.setName(genName(prefix, "e", i10, maxDigits));
                } else if (nullType.equals(type)) {
                    int i11 = nullCount;
                    nullCount++;
                    l.setName(genName(prefix, "n", i11, maxDigits));
                } else {
                    int i12 = objectCount;
                    objectCount++;
                    l.setName(genName(prefix, "r", i12, maxDigits));
                }
            }
        }
    }
}
