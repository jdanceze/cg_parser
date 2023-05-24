package soot.defaultInterfaceMethods;
/* loaded from: gencallgraphv3.jar:soot/defaultInterfaceMethods/SuperClassImplementsInterface.class */
public class SuperClassImplementsInterface implements DefaultPrint {
    public void main() {
        SuperClassImplementsInterface main = new SuperClassImplementsInterface();
        main.print();
    }

    @Override // soot.defaultInterfaceMethods.DefaultPrint
    public void print() {
        System.out.println("This is super class print method");
    }
}
