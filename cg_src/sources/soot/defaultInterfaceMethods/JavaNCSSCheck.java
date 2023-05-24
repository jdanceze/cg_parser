package soot.defaultInterfaceMethods;

import soot.defaultInterfaceDifferentPackage.AbstractCheck;
/* loaded from: gencallgraphv3.jar:soot/defaultInterfaceMethods/JavaNCSSCheck.class */
public class JavaNCSSCheck extends AbstractCheck {
    public void main() {
        JavaNCSSCheck mainClass = new JavaNCSSCheck();
        mainClass.finishTree();
    }

    public void finishTree() {
        log("1234", "Test");
    }
}
