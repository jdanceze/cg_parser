package soot.jbco.name;
/* loaded from: gencallgraphv3.jar:soot/jbco/name/JunkNameGenerator.class */
public class JunkNameGenerator extends AbstractNameGenerator implements NameGenerator {
    private static final char[][] stringChars = {new char[]{'S', '5', '$'}, new char[]{'l', '1', 'I'}, new char[]{'_'}};

    @Override // soot.jbco.name.AbstractNameGenerator
    protected char[][] getChars() {
        return stringChars;
    }
}
