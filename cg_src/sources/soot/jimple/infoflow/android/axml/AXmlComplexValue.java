package soot.jimple.infoflow.android.axml;

import java.nio.ByteBuffer;
import pxb.android.Item;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/axml/AXmlComplexValue.class */
public class AXmlComplexValue implements Item {
    private final Unit unit;
    private final int mantissa;
    private final int radix;

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/axml/AXmlComplexValue$Unit.class */
    public enum Unit {
        DIP(1),
        Fraction(0),
        FractionParent(1),
        IN(4),
        MM(5),
        PT(3),
        PX(0),
        SP(2);
        
        private int unit;
        private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$android$axml$AXmlComplexValue$Unit;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static Unit[] valuesCustom() {
            Unit[] valuesCustom = values();
            int length = valuesCustom.length;
            Unit[] unitArr = new Unit[length];
            System.arraycopy(valuesCustom, 0, unitArr, 0, length);
            return unitArr;
        }

        static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$android$axml$AXmlComplexValue$Unit() {
            int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$android$axml$AXmlComplexValue$Unit;
            if (iArr != null) {
                return iArr;
            }
            int[] iArr2 = new int[valuesCustom().length];
            try {
                iArr2[DIP.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr2[Fraction.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr2[FractionParent.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr2[IN.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr2[MM.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr2[PT.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                iArr2[PX.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                iArr2[SP.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            $SWITCH_TABLE$soot$jimple$infoflow$android$axml$AXmlComplexValue$Unit = iArr2;
            return iArr2;
        }

        Unit(int c) {
            this.unit = c;
        }

        public int getUnit() {
            return this.unit;
        }

        @Override // java.lang.Enum
        public String toString() {
            switch ($SWITCH_TABLE$soot$jimple$infoflow$android$axml$AXmlComplexValue$Unit()[ordinal()]) {
                case 1:
                    return "dip";
                case 2:
                case 3:
                    return "%";
                case 4:
                    return "in";
                case 5:
                    return "mm";
                case 6:
                    return "pt";
                case 7:
                    return "px";
                case 8:
                    return "sp";
                default:
                    return "";
            }
        }
    }

    public AXmlComplexValue(Unit unit, int mantissa, int radix) {
        this.unit = unit;
        this.mantissa = mantissa;
        this.radix = radix;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public int getMantissa() {
        return this.mantissa;
    }

    public int getRadix() {
        return this.radix;
    }

    public String toString() {
        return Integer.toString(this.mantissa) + '.' + Integer.toString(this.radix) + this.unit.toString();
    }

    public int hashCode() {
        int result = (31 * 1) + this.mantissa;
        return (31 * ((31 * result) + this.radix)) + (this.unit == null ? 0 : this.unit.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AXmlComplexValue other = (AXmlComplexValue) obj;
        if (this.mantissa != other.mantissa || this.radix != other.radix || this.unit != other.unit) {
            return false;
        }
        return true;
    }

    @Override // pxb.android.Item
    public void writeout(ByteBuffer out) {
        out.putInt(getInt());
    }

    public int getInt() {
        int val = 0 | this.mantissa;
        return (((val << 4) | this.radix) << 4) | this.unit.getUnit();
    }

    public static AXmlComplexValue parseComplexValue(int complexValue) {
        int unitVal = complexValue >> 0;
        Unit complexUnit = parseComplexUnit(unitVal & 15);
        int radixVal = complexValue >> 4;
        int radixVal2 = radixVal & 3;
        int mantissa = complexValue >> 8;
        return new AXmlComplexValue(complexUnit, mantissa & 16777215, radixVal2);
    }

    public static Unit parseComplexUnit(int unitVal) {
        switch (unitVal) {
            case 0:
                return Unit.PX;
            case 1:
                return Unit.DIP;
            case 2:
                return Unit.SP;
            case 3:
                return Unit.PT;
            case 4:
                return Unit.IN;
            case 5:
                return Unit.MM;
            default:
                throw new RuntimeException(String.format("Unknown complex unit %d", Integer.valueOf(unitVal)));
        }
    }
}
