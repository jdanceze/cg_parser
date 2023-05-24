package soot.defaultInterfaceMethods;
/* loaded from: gencallgraphv3.jar:soot/defaultInterfaceMethods/InterfaceReAbstracting.class */
public class InterfaceReAbstracting implements InterfaceA, InterfaceB {
    public void main() {
        InterfaceReAbstracting testClass = new InterfaceReAbstracting();
        testClass.print();
    }

    @Override // soot.defaultInterfaceMethods.InterfaceA
    public void print() {
        System.out.println("This is print method of main class");
    }
}
