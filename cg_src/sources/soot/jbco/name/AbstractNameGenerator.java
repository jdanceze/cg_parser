package soot.jbco.name;

import soot.jbco.util.Rand;
/* loaded from: gencallgraphv3.jar:soot/jbco/name/AbstractNameGenerator.class */
public abstract class AbstractNameGenerator implements NameGenerator {
    protected abstract char[][] getChars();

    @Override // soot.jbco.name.NameGenerator
    public String generateName(int size) {
        if (size > 21845) {
            throw new IllegalArgumentException("Cannot generate junk name: too long for JVM.");
        }
        char[][] chars = getChars();
        int index = Rand.getInt(chars.length);
        int length = chars[index].length;
        char[] newName = new char[size];
        do {
            newName[0] = chars[index][Rand.getInt(length)];
        } while (!Character.isJavaIdentifierStart(newName[0]));
        for (int i = 1; i < newName.length; i++) {
            int rand = Rand.getInt(length);
            newName[i] = chars[index][rand];
        }
        return String.valueOf(newName);
    }
}
