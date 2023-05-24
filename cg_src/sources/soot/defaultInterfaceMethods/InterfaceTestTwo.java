package soot.defaultInterfaceMethods;
/* compiled from: DerivedInterfaces.java */
/* loaded from: gencallgraphv3.jar:soot/defaultInterfaceMethods/InterfaceTestTwo.class */
interface InterfaceTestTwo extends InterfaceTestOne {
    @Override // soot.defaultInterfaceMethods.InterfaceTestOne
    default void print() {
        System.out.println("This is interface two");
    }
}
