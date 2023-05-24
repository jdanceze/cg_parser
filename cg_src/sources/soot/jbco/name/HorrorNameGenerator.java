package soot.jbco.name;
/* loaded from: gencallgraphv3.jar:soot/jbco/name/HorrorNameGenerator.class */
public class HorrorNameGenerator extends AbstractNameGenerator implements NameGenerator {
    private static final char[][] stringChars = {new char[]{'S', '5', '$'}, new char[]{'l', '1', 'I', 921}, new char[]{'0', 'O', 1054, 927, 1365}, new char[]{'o', 1086, 959}, new char[]{'T', 1058, 932}, new char[]{'H', 1053, 919}, new char[]{'E', 1045, 917}, new char[]{'P', 1056, 929}, new char[]{'B', 1042, 914}, new char[]{'_'}};

    @Override // soot.jbco.name.AbstractNameGenerator
    protected char[][] getChars() {
        return stringChars;
    }
}
