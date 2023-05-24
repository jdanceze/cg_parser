package soot.defaultInterfaceMethods;
/* loaded from: gencallgraphv3.jar:soot/defaultInterfaceMethods/InterfaceInheritance.class */
public class InterfaceInheritance implements InterfaceTestB {
    public void main() {
        InterfaceInheritance testClass = new InterfaceInheritance();
        testClass.print();
    }

    @Override // soot.defaultInterfaceMethods.InterfaceTestB
    public void printMessage() {
        System.out.println("This is print method of main class");
    }
}
