package soot.defaultInterfaceMethods;
/* loaded from: gencallgraphv3.jar:soot/defaultInterfaceMethods/InterfaceSameSignature.class */
public class InterfaceSameSignature implements Read, Write {
    @Override // soot.defaultInterfaceMethods.Read, soot.defaultInterfaceMethods.Write
    public void print() {
        super.print();
        super.print();
    }

    public void main() {
        InterfaceSameSignature testClass = new InterfaceSameSignature();
        testClass.read();
        testClass.write();
        testClass.print();
    }
}
