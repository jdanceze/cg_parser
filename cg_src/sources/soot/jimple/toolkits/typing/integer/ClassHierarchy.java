package soot.jimple.toolkits.typing.integer;

import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.G;
import soot.IntType;
import soot.ShortType;
import soot.Singletons;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/integer/ClassHierarchy.class */
public class ClassHierarchy {
    public final TypeNode BOOLEAN = new TypeNode(0, BooleanType.v());
    public final TypeNode BYTE = new TypeNode(1, ByteType.v());
    public final TypeNode SHORT = new TypeNode(2, ShortType.v());
    public final TypeNode CHAR = new TypeNode(3, CharType.v());
    public final TypeNode INT = new TypeNode(4, IntType.v());
    public final TypeNode TOP = new TypeNode(5, null);
    public final TypeNode R0_1 = new TypeNode(6, null);
    public final TypeNode R0_127 = new TypeNode(7, null);
    public final TypeNode R0_32767 = new TypeNode(8, null);
    private final boolean[][] ancestors_1;
    private final boolean[][] ancestors_2;
    private final boolean[][] descendants_1;
    private final boolean[][] descendants_2;
    private final TypeNode[][] lca_1;
    private final TypeNode[][] lca_2;
    private final TypeNode[][] gcd_1;
    private final TypeNode[][] gcd_2;

    /* JADX WARN: Type inference failed for: r1v10, types: [boolean[], boolean[][]] */
    /* JADX WARN: Type inference failed for: r1v12, types: [boolean[], boolean[][]] */
    /* JADX WARN: Type inference failed for: r1v14, types: [boolean[], boolean[][]] */
    /* JADX WARN: Type inference failed for: r1v16, types: [boolean[], boolean[][]] */
    /* JADX WARN: Type inference failed for: r1v18, types: [soot.jimple.toolkits.typing.integer.TypeNode[], soot.jimple.toolkits.typing.integer.TypeNode[][]] */
    /* JADX WARN: Type inference failed for: r1v20, types: [soot.jimple.toolkits.typing.integer.TypeNode[], soot.jimple.toolkits.typing.integer.TypeNode[][]] */
    /* JADX WARN: Type inference failed for: r1v22, types: [soot.jimple.toolkits.typing.integer.TypeNode[], soot.jimple.toolkits.typing.integer.TypeNode[][]] */
    /* JADX WARN: Type inference failed for: r1v24, types: [soot.jimple.toolkits.typing.integer.TypeNode[], soot.jimple.toolkits.typing.integer.TypeNode[][]] */
    public ClassHierarchy(Singletons.Global g) {
        boolean[] zArr = new boolean[9];
        zArr[5] = true;
        boolean[] zArr2 = new boolean[9];
        zArr2[2] = true;
        zArr2[4] = true;
        zArr2[5] = true;
        boolean[] zArr3 = new boolean[9];
        zArr3[4] = true;
        zArr3[5] = true;
        boolean[] zArr4 = new boolean[9];
        zArr4[4] = true;
        zArr4[5] = true;
        boolean[] zArr5 = new boolean[9];
        zArr5[5] = true;
        this.ancestors_1 = new boolean[]{zArr, zArr2, zArr3, zArr4, zArr5, new boolean[9], new boolean[]{true, true, true, true, true, true, false, true, true}, new boolean[]{false, true, true, true, true, true, false, false, true}, new boolean[]{false, false, true, true, true, true}};
        boolean[] zArr6 = new boolean[9];
        zArr6[2] = true;
        zArr6[4] = true;
        boolean[] zArr7 = new boolean[9];
        zArr7[4] = true;
        boolean[] zArr8 = new boolean[9];
        zArr8[4] = true;
        boolean[] zArr9 = new boolean[9];
        zArr9[2] = true;
        zArr9[3] = true;
        zArr9[4] = true;
        this.ancestors_2 = new boolean[]{new boolean[]{false, true, true, true, true, false, false, true, true}, zArr6, zArr7, zArr8, new boolean[9], new boolean[0], new boolean[0], new boolean[]{false, true, true, true, true, false, false, false, true}, zArr9};
        boolean[] zArr10 = new boolean[9];
        zArr10[6] = true;
        boolean[] zArr11 = new boolean[9];
        zArr11[6] = true;
        zArr11[7] = true;
        boolean[] zArr12 = new boolean[9];
        zArr12[6] = true;
        zArr12[7] = true;
        zArr12[8] = true;
        boolean[] zArr13 = new boolean[9];
        zArr13[6] = true;
        boolean[] zArr14 = new boolean[9];
        zArr14[6] = true;
        zArr14[7] = true;
        this.descendants_1 = new boolean[]{zArr10, zArr11, new boolean[]{false, true, false, false, false, false, true, true, true}, zArr12, new boolean[]{false, true, true, true, false, false, true, true, true}, new boolean[]{true, true, true, true, true, false, true, true, true}, new boolean[9], zArr13, zArr14};
        boolean[] zArr15 = new boolean[9];
        zArr15[0] = true;
        zArr15[7] = true;
        boolean[] zArr16 = new boolean[9];
        zArr16[0] = true;
        zArr16[7] = true;
        zArr16[8] = true;
        boolean[] zArr17 = new boolean[9];
        zArr17[0] = true;
        boolean[] zArr18 = new boolean[9];
        zArr18[0] = true;
        zArr18[7] = true;
        this.descendants_2 = new boolean[]{new boolean[9], zArr15, new boolean[]{true, true, false, false, false, false, false, true, true}, zArr16, new boolean[]{true, true, true, true, false, false, false, true, true}, new boolean[0], new boolean[0], zArr17, zArr18};
        this.lca_1 = new TypeNode[]{new TypeNode[]{this.BOOLEAN, this.TOP, this.TOP, this.TOP, this.TOP, this.TOP, this.BOOLEAN, this.TOP, this.TOP}, new TypeNode[]{this.TOP, this.BYTE, this.SHORT, this.INT, this.INT, this.TOP, this.BYTE, this.BYTE, this.SHORT}, new TypeNode[]{this.TOP, this.SHORT, this.SHORT, this.INT, this.INT, this.TOP, this.SHORT, this.SHORT, this.SHORT}, new TypeNode[]{this.TOP, this.INT, this.INT, this.CHAR, this.INT, this.TOP, this.CHAR, this.CHAR, this.CHAR}, new TypeNode[]{this.TOP, this.INT, this.INT, this.INT, this.INT, this.TOP, this.INT, this.INT, this.INT}, new TypeNode[]{this.TOP, this.TOP, this.TOP, this.TOP, this.TOP, this.TOP, this.TOP, this.TOP, this.TOP}, new TypeNode[]{this.BOOLEAN, this.BYTE, this.SHORT, this.CHAR, this.INT, this.TOP, this.R0_1, this.R0_127, this.R0_32767}, new TypeNode[]{this.TOP, this.BYTE, this.SHORT, this.CHAR, this.INT, this.TOP, this.R0_127, this.R0_127, this.R0_32767}, new TypeNode[]{this.TOP, this.SHORT, this.SHORT, this.CHAR, this.INT, this.TOP, this.R0_32767, this.R0_32767, this.R0_32767}};
        TypeNode[] typeNodeArr = new TypeNode[9];
        typeNodeArr[0] = this.BOOLEAN;
        typeNodeArr[1] = this.BYTE;
        typeNodeArr[2] = this.SHORT;
        typeNodeArr[3] = this.CHAR;
        typeNodeArr[4] = this.INT;
        typeNodeArr[7] = this.R0_127;
        typeNodeArr[8] = this.R0_32767;
        TypeNode[] typeNodeArr2 = new TypeNode[9];
        typeNodeArr2[0] = this.BYTE;
        typeNodeArr2[1] = this.BYTE;
        typeNodeArr2[2] = this.SHORT;
        typeNodeArr2[3] = this.INT;
        typeNodeArr2[4] = this.INT;
        typeNodeArr2[7] = this.BYTE;
        typeNodeArr2[8] = this.SHORT;
        TypeNode[] typeNodeArr3 = new TypeNode[9];
        typeNodeArr3[0] = this.SHORT;
        typeNodeArr3[1] = this.SHORT;
        typeNodeArr3[2] = this.SHORT;
        typeNodeArr3[3] = this.INT;
        typeNodeArr3[4] = this.INT;
        typeNodeArr3[7] = this.SHORT;
        typeNodeArr3[8] = this.SHORT;
        TypeNode[] typeNodeArr4 = new TypeNode[9];
        typeNodeArr4[0] = this.CHAR;
        typeNodeArr4[1] = this.INT;
        typeNodeArr4[2] = this.INT;
        typeNodeArr4[3] = this.CHAR;
        typeNodeArr4[4] = this.INT;
        typeNodeArr4[7] = this.CHAR;
        typeNodeArr4[8] = this.CHAR;
        TypeNode[] typeNodeArr5 = new TypeNode[9];
        typeNodeArr5[0] = this.INT;
        typeNodeArr5[1] = this.INT;
        typeNodeArr5[2] = this.INT;
        typeNodeArr5[3] = this.INT;
        typeNodeArr5[4] = this.INT;
        typeNodeArr5[7] = this.INT;
        typeNodeArr5[8] = this.INT;
        TypeNode[] typeNodeArr6 = new TypeNode[9];
        typeNodeArr6[0] = this.R0_127;
        typeNodeArr6[1] = this.BYTE;
        typeNodeArr6[2] = this.SHORT;
        typeNodeArr6[3] = this.CHAR;
        typeNodeArr6[4] = this.INT;
        typeNodeArr6[7] = this.R0_127;
        typeNodeArr6[8] = this.R0_32767;
        TypeNode[] typeNodeArr7 = new TypeNode[9];
        typeNodeArr7[0] = this.R0_32767;
        typeNodeArr7[1] = this.SHORT;
        typeNodeArr7[2] = this.SHORT;
        typeNodeArr7[3] = this.CHAR;
        typeNodeArr7[4] = this.INT;
        typeNodeArr7[7] = this.R0_32767;
        typeNodeArr7[8] = this.R0_32767;
        this.lca_2 = new TypeNode[]{typeNodeArr, typeNodeArr2, typeNodeArr3, typeNodeArr4, typeNodeArr5, new TypeNode[0], new TypeNode[0], typeNodeArr6, typeNodeArr7};
        this.gcd_1 = new TypeNode[]{new TypeNode[]{this.BOOLEAN, this.R0_1, this.R0_1, this.R0_1, this.R0_1, this.BOOLEAN, this.R0_1, this.R0_1, this.R0_1}, new TypeNode[]{this.R0_1, this.BYTE, this.BYTE, this.R0_127, this.BYTE, this.BYTE, this.R0_1, this.R0_127, this.R0_127}, new TypeNode[]{this.R0_1, this.BYTE, this.SHORT, this.R0_32767, this.SHORT, this.SHORT, this.R0_1, this.R0_127, this.R0_32767}, new TypeNode[]{this.R0_1, this.R0_127, this.R0_32767, this.CHAR, this.CHAR, this.CHAR, this.R0_1, this.R0_127, this.R0_32767}, new TypeNode[]{this.R0_1, this.BYTE, this.SHORT, this.CHAR, this.INT, this.INT, this.R0_1, this.R0_127, this.R0_32767}, new TypeNode[]{this.BOOLEAN, this.BYTE, this.SHORT, this.CHAR, this.INT, this.TOP, this.R0_1, this.R0_127, this.R0_32767}, new TypeNode[]{this.R0_1, this.R0_1, this.R0_1, this.R0_1, this.R0_1, this.R0_1, this.R0_1, this.R0_1, this.R0_1}, new TypeNode[]{this.R0_1, this.R0_127, this.R0_127, this.R0_127, this.R0_127, this.R0_127, this.R0_1, this.R0_127, this.R0_127}, new TypeNode[]{this.R0_1, this.R0_127, this.R0_32767, this.R0_32767, this.R0_32767, this.R0_32767, this.R0_1, this.R0_127, this.R0_32767}};
        TypeNode[] typeNodeArr8 = new TypeNode[9];
        typeNodeArr8[0] = this.BOOLEAN;
        typeNodeArr8[1] = this.BOOLEAN;
        typeNodeArr8[2] = this.BOOLEAN;
        typeNodeArr8[3] = this.BOOLEAN;
        typeNodeArr8[4] = this.BOOLEAN;
        typeNodeArr8[7] = this.BOOLEAN;
        typeNodeArr8[8] = this.BOOLEAN;
        TypeNode[] typeNodeArr9 = new TypeNode[9];
        typeNodeArr9[0] = this.BOOLEAN;
        typeNodeArr9[1] = this.BYTE;
        typeNodeArr9[2] = this.BYTE;
        typeNodeArr9[3] = this.R0_127;
        typeNodeArr9[4] = this.BYTE;
        typeNodeArr9[7] = this.R0_127;
        typeNodeArr9[8] = this.R0_127;
        TypeNode[] typeNodeArr10 = new TypeNode[9];
        typeNodeArr10[0] = this.BOOLEAN;
        typeNodeArr10[1] = this.BYTE;
        typeNodeArr10[2] = this.SHORT;
        typeNodeArr10[3] = this.R0_32767;
        typeNodeArr10[4] = this.SHORT;
        typeNodeArr10[7] = this.R0_127;
        typeNodeArr10[8] = this.R0_32767;
        TypeNode[] typeNodeArr11 = new TypeNode[9];
        typeNodeArr11[0] = this.BOOLEAN;
        typeNodeArr11[1] = this.R0_127;
        typeNodeArr11[2] = this.R0_32767;
        typeNodeArr11[3] = this.CHAR;
        typeNodeArr11[4] = this.CHAR;
        typeNodeArr11[7] = this.R0_127;
        typeNodeArr11[8] = this.R0_32767;
        TypeNode[] typeNodeArr12 = new TypeNode[9];
        typeNodeArr12[0] = this.BOOLEAN;
        typeNodeArr12[1] = this.BYTE;
        typeNodeArr12[2] = this.SHORT;
        typeNodeArr12[3] = this.CHAR;
        typeNodeArr12[4] = this.INT;
        typeNodeArr12[7] = this.R0_127;
        typeNodeArr12[8] = this.R0_32767;
        TypeNode[] typeNodeArr13 = new TypeNode[9];
        typeNodeArr13[0] = this.BOOLEAN;
        typeNodeArr13[1] = this.R0_127;
        typeNodeArr13[2] = this.R0_127;
        typeNodeArr13[3] = this.R0_127;
        typeNodeArr13[4] = this.R0_127;
        typeNodeArr13[7] = this.R0_127;
        typeNodeArr13[8] = this.R0_127;
        TypeNode[] typeNodeArr14 = new TypeNode[9];
        typeNodeArr14[0] = this.BOOLEAN;
        typeNodeArr14[1] = this.R0_127;
        typeNodeArr14[2] = this.R0_32767;
        typeNodeArr14[3] = this.R0_32767;
        typeNodeArr14[4] = this.R0_32767;
        typeNodeArr14[7] = this.R0_127;
        typeNodeArr14[8] = this.R0_32767;
        this.gcd_2 = new TypeNode[]{typeNodeArr8, typeNodeArr9, typeNodeArr10, typeNodeArr11, typeNodeArr12, new TypeNode[0], new TypeNode[0], typeNodeArr13, typeNodeArr14};
    }

    public static ClassHierarchy v() {
        return G.v().soot_jimple_toolkits_typing_integer_ClassHierarchy();
    }

    public TypeNode typeNode(Type type) {
        if (type instanceof IntType) {
            return this.INT;
        }
        if (type instanceof BooleanType) {
            return this.BOOLEAN;
        }
        if (type instanceof ByteType) {
            return this.BYTE;
        }
        if (type instanceof ShortType) {
            return this.SHORT;
        }
        if (type instanceof CharType) {
            return this.CHAR;
        }
        throw new InternalTypingException(type);
    }

    public boolean hasAncestor_1(int t1, int t2) {
        return this.ancestors_1[t1][t2];
    }

    public boolean hasAncestor_2(int t1, int t2) {
        return this.ancestors_2[t1][t2];
    }

    public boolean hasDescendant_1(int t1, int t2) {
        return this.descendants_1[t1][t2];
    }

    public boolean hasDescendant_2(int t1, int t2) {
        return this.descendants_2[t1][t2];
    }

    public TypeNode lca_1(int t1, int t2) {
        return this.lca_1[t1][t2];
    }

    private int convert(int n) {
        switch (n) {
            case 5:
                return 4;
            case 6:
                return 0;
            default:
                return n;
        }
    }

    public TypeNode lca_2(int t1, int t2) {
        return this.lca_2[convert(t1)][convert(t2)];
    }

    public TypeNode gcd_1(int t1, int t2) {
        return this.gcd_1[t1][t2];
    }

    public TypeNode gcd_2(int t1, int t2) {
        return this.gcd_2[convert(t1)][convert(t2)];
    }
}
