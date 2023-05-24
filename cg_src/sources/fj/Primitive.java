package fj;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Primitive.class */
public final class Primitive {
    public static final F<Boolean, Byte> Boolean_Byte = new F<Boolean, Byte>() { // from class: fj.Primitive.1
        @Override // fj.F
        public Byte f(Boolean b) {
            return Byte.valueOf((byte) (b.booleanValue() ? 1 : 0));
        }
    };
    public static final F<Boolean, Character> Boolean_Character = new F<Boolean, Character>() { // from class: fj.Primitive.2
        @Override // fj.F
        public Character f(Boolean b) {
            return Character.valueOf((char) (b.booleanValue() ? 1 : 0));
        }
    };
    public static final F<Boolean, Double> Boolean_Double = new F<Boolean, Double>() { // from class: fj.Primitive.3
        @Override // fj.F
        public Double f(Boolean b) {
            return Double.valueOf(b.booleanValue() ? 1.0d : Const.default_value_double);
        }
    };
    public static final F<Boolean, Float> Boolean_Float = new F<Boolean, Float>() { // from class: fj.Primitive.4
        @Override // fj.F
        public Float f(Boolean b) {
            return Float.valueOf(b.booleanValue() ? 1.0f : 0.0f);
        }
    };
    public static final F<Boolean, Integer> Boolean_Integer = new F<Boolean, Integer>() { // from class: fj.Primitive.5
        @Override // fj.F
        public Integer f(Boolean b) {
            return Integer.valueOf(b.booleanValue() ? 1 : 0);
        }
    };
    public static final F<Boolean, Long> Boolean_Long = new F<Boolean, Long>() { // from class: fj.Primitive.6
        @Override // fj.F
        public Long f(Boolean b) {
            return Long.valueOf(b.booleanValue() ? 1L : 0L);
        }
    };
    public static final F<Boolean, Short> Boolean_Short = new F<Boolean, Short>() { // from class: fj.Primitive.7
        @Override // fj.F
        public Short f(Boolean b) {
            return Short.valueOf((short) (b.booleanValue() ? 1 : 0));
        }
    };
    public static final F<Byte, Boolean> Byte_Boolean = new F<Byte, Boolean>() { // from class: fj.Primitive.8
        @Override // fj.F
        public Boolean f(Byte b) {
            return Boolean.valueOf(b.byteValue() != 0);
        }
    };
    public static final F<Byte, Character> Byte_Character = new F<Byte, Character>() { // from class: fj.Primitive.9
        @Override // fj.F
        public Character f(Byte b) {
            return Character.valueOf((char) b.byteValue());
        }
    };
    public static final F<Byte, Double> Byte_Double = new F<Byte, Double>() { // from class: fj.Primitive.10
        @Override // fj.F
        public Double f(Byte b) {
            return Double.valueOf(b.byteValue());
        }
    };
    public static final F<Byte, Float> Byte_Float = new F<Byte, Float>() { // from class: fj.Primitive.11
        @Override // fj.F
        public Float f(Byte b) {
            return Float.valueOf(b.byteValue());
        }
    };
    public static final F<Byte, Integer> Byte_Integer = new F<Byte, Integer>() { // from class: fj.Primitive.12
        @Override // fj.F
        public Integer f(Byte b) {
            return Integer.valueOf(b.byteValue());
        }
    };
    public static final F<Byte, Long> Byte_Long = new F<Byte, Long>() { // from class: fj.Primitive.13
        @Override // fj.F
        public Long f(Byte b) {
            return Long.valueOf(b.byteValue());
        }
    };
    public static final F<Byte, Short> Byte_Short = new F<Byte, Short>() { // from class: fj.Primitive.14
        @Override // fj.F
        public Short f(Byte b) {
            return Short.valueOf(b.byteValue());
        }
    };
    public static final F<Character, Boolean> Character_Boolean = new F<Character, Boolean>() { // from class: fj.Primitive.15
        @Override // fj.F
        public Boolean f(Character c) {
            return Boolean.valueOf(c.charValue() != 0);
        }
    };
    public static final F<Character, Byte> Character_Byte = new F<Character, Byte>() { // from class: fj.Primitive.16
        @Override // fj.F
        public Byte f(Character c) {
            return Byte.valueOf((byte) c.charValue());
        }
    };
    public static final F<Character, Double> Character_Double = new F<Character, Double>() { // from class: fj.Primitive.17
        @Override // fj.F
        public Double f(Character c) {
            return Double.valueOf(c.charValue());
        }
    };
    public static final F<Character, Float> Character_Float = new F<Character, Float>() { // from class: fj.Primitive.18
        @Override // fj.F
        public Float f(Character c) {
            return Float.valueOf(c.charValue());
        }
    };
    public static final F<Character, Integer> Character_Integer = new F<Character, Integer>() { // from class: fj.Primitive.19
        @Override // fj.F
        public Integer f(Character c) {
            return Integer.valueOf(c.charValue());
        }
    };
    public static final F<Character, Long> Character_Long = new F<Character, Long>() { // from class: fj.Primitive.20
        @Override // fj.F
        public Long f(Character c) {
            return Long.valueOf(c.charValue());
        }
    };
    public static final F<Character, Short> Character_Short = new F<Character, Short>() { // from class: fj.Primitive.21
        @Override // fj.F
        public Short f(Character c) {
            return Short.valueOf((short) c.charValue());
        }
    };
    public static final F<Double, Boolean> Double_Boolean = new F<Double, Boolean>() { // from class: fj.Primitive.22
        @Override // fj.F
        public Boolean f(Double d) {
            return Boolean.valueOf(d.doubleValue() != Const.default_value_double);
        }
    };
    public static final F<Double, Byte> Double_Byte = new F<Double, Byte>() { // from class: fj.Primitive.23
        @Override // fj.F
        public Byte f(Double d) {
            return Byte.valueOf((byte) d.doubleValue());
        }
    };
    public static final F<Double, Character> Double_Character = new F<Double, Character>() { // from class: fj.Primitive.24
        @Override // fj.F
        public Character f(Double d) {
            return Character.valueOf((char) d.doubleValue());
        }
    };
    public static final F<Double, Float> Double_Float = new F<Double, Float>() { // from class: fj.Primitive.25
        @Override // fj.F
        public Float f(Double d) {
            return Float.valueOf((float) d.doubleValue());
        }
    };
    public static final F<Double, Integer> Double_Integer = new F<Double, Integer>() { // from class: fj.Primitive.26
        @Override // fj.F
        public Integer f(Double d) {
            return Integer.valueOf((int) d.doubleValue());
        }
    };
    public static final F<Double, Long> Double_Long = new F<Double, Long>() { // from class: fj.Primitive.27
        @Override // fj.F
        public Long f(Double d) {
            return Long.valueOf((long) d.doubleValue());
        }
    };
    public static final F<Double, Short> Double_Short = new F<Double, Short>() { // from class: fj.Primitive.28
        @Override // fj.F
        public Short f(Double d) {
            return Short.valueOf((short) d.doubleValue());
        }
    };
    public static final F<Float, Boolean> Float_Boolean = new F<Float, Boolean>() { // from class: fj.Primitive.29
        @Override // fj.F
        public Boolean f(Float f) {
            return Boolean.valueOf(f.floatValue() != 0.0f);
        }
    };
    public static final F<Float, Byte> Float_Byte = new F<Float, Byte>() { // from class: fj.Primitive.30
        @Override // fj.F
        public Byte f(Float f) {
            return Byte.valueOf((byte) f.floatValue());
        }
    };
    public static final F<Float, Character> Float_Character = new F<Float, Character>() { // from class: fj.Primitive.31
        @Override // fj.F
        public Character f(Float f) {
            return Character.valueOf((char) f.floatValue());
        }
    };
    public static final F<Float, Double> Float_Double = new F<Float, Double>() { // from class: fj.Primitive.32
        @Override // fj.F
        public Double f(Float f) {
            return Double.valueOf(f.floatValue());
        }
    };
    public static final F<Float, Integer> Float_Integer = new F<Float, Integer>() { // from class: fj.Primitive.33
        @Override // fj.F
        public Integer f(Float f) {
            return Integer.valueOf((int) f.floatValue());
        }
    };
    public static final F<Float, Long> Float_Long = new F<Float, Long>() { // from class: fj.Primitive.34
        @Override // fj.F
        public Long f(Float f) {
            return Long.valueOf(f.floatValue());
        }
    };
    public static final F<Float, Short> Float_Short = new F<Float, Short>() { // from class: fj.Primitive.35
        @Override // fj.F
        public Short f(Float f) {
            return Short.valueOf((short) f.floatValue());
        }
    };
    public static final F<Integer, Boolean> Integer_Boolean = new F<Integer, Boolean>() { // from class: fj.Primitive.36
        @Override // fj.F
        public Boolean f(Integer i) {
            return Boolean.valueOf(i.intValue() != 0);
        }
    };
    public static final F<Integer, Byte> Integer_Byte = new F<Integer, Byte>() { // from class: fj.Primitive.37
        @Override // fj.F
        public Byte f(Integer i) {
            return Byte.valueOf((byte) i.intValue());
        }
    };
    public static final F<Integer, Character> Integer_Character = new F<Integer, Character>() { // from class: fj.Primitive.38
        @Override // fj.F
        public Character f(Integer i) {
            return Character.valueOf((char) i.intValue());
        }
    };
    public static final F<Integer, Double> Integer_Double = new F<Integer, Double>() { // from class: fj.Primitive.39
        @Override // fj.F
        public Double f(Integer i) {
            return Double.valueOf(i.intValue());
        }
    };
    public static final F<Integer, Float> Integer_Float = new F<Integer, Float>() { // from class: fj.Primitive.40
        @Override // fj.F
        public Float f(Integer i) {
            return Float.valueOf(i.intValue());
        }
    };
    public static final F<Integer, Long> Integer_Long = new F<Integer, Long>() { // from class: fj.Primitive.41
        @Override // fj.F
        public Long f(Integer i) {
            return Long.valueOf(i.intValue());
        }
    };
    public static final F<Integer, Short> Integer_Short = new F<Integer, Short>() { // from class: fj.Primitive.42
        @Override // fj.F
        public Short f(Integer i) {
            return Short.valueOf((short) i.intValue());
        }
    };
    public static final F<Long, Boolean> Long_Boolean = new F<Long, Boolean>() { // from class: fj.Primitive.43
        @Override // fj.F
        public Boolean f(Long l) {
            return Boolean.valueOf(l.longValue() != 0);
        }
    };
    public static final F<Long, Byte> Long_Byte = new F<Long, Byte>() { // from class: fj.Primitive.44
        @Override // fj.F
        public Byte f(Long l) {
            return Byte.valueOf((byte) l.longValue());
        }
    };
    public static final F<Long, Character> Long_Character = new F<Long, Character>() { // from class: fj.Primitive.45
        @Override // fj.F
        public Character f(Long l) {
            return Character.valueOf((char) l.longValue());
        }
    };
    public static final F<Long, Double> Long_Double = new F<Long, Double>() { // from class: fj.Primitive.46
        @Override // fj.F
        public Double f(Long l) {
            return Double.valueOf(l.longValue());
        }
    };
    public static final F<Long, Float> Long_Float = new F<Long, Float>() { // from class: fj.Primitive.47
        @Override // fj.F
        public Float f(Long l) {
            return Float.valueOf((float) l.longValue());
        }
    };
    public static final F<Long, Integer> Long_Integer = new F<Long, Integer>() { // from class: fj.Primitive.48
        @Override // fj.F
        public Integer f(Long l) {
            return Integer.valueOf((int) l.longValue());
        }
    };
    public static final F<Long, Short> Long_Short = new F<Long, Short>() { // from class: fj.Primitive.49
        @Override // fj.F
        public Short f(Long l) {
            return Short.valueOf((short) l.longValue());
        }
    };
    public static final F<Short, Boolean> Short_Boolean = new F<Short, Boolean>() { // from class: fj.Primitive.50
        @Override // fj.F
        public Boolean f(Short s) {
            return Boolean.valueOf(s.shortValue() != 0);
        }
    };
    public static final F<Short, Byte> Short_Byte = new F<Short, Byte>() { // from class: fj.Primitive.51
        @Override // fj.F
        public Byte f(Short s) {
            return Byte.valueOf((byte) s.shortValue());
        }
    };
    public static final F<Short, Character> Short_Character = new F<Short, Character>() { // from class: fj.Primitive.52
        @Override // fj.F
        public Character f(Short s) {
            return Character.valueOf((char) s.shortValue());
        }
    };
    public static final F<Short, Double> Short_Double = new F<Short, Double>() { // from class: fj.Primitive.53
        @Override // fj.F
        public Double f(Short s) {
            return Double.valueOf(s.shortValue());
        }
    };
    public static final F<Short, Float> Short_Float = new F<Short, Float>() { // from class: fj.Primitive.54
        @Override // fj.F
        public Float f(Short s) {
            return Float.valueOf(s.shortValue());
        }
    };
    public static final F<Short, Integer> Short_Integer = new F<Short, Integer>() { // from class: fj.Primitive.55
        @Override // fj.F
        public Integer f(Short s) {
            return Integer.valueOf(s.shortValue());
        }
    };
    public static final F<Short, Long> Short_Long = new F<Short, Long>() { // from class: fj.Primitive.56
        @Override // fj.F
        public Long f(Short s) {
            return Long.valueOf(s.shortValue());
        }
    };

    private Primitive() {
        throw new UnsupportedOperationException();
    }
}
