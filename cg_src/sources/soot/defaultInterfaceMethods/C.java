package soot.defaultInterfaceMethods;
/* compiled from: MaximallySpecificSuperInterface.java */
/* loaded from: gencallgraphv3.jar:soot/defaultInterfaceMethods/C.class */
interface C extends D {
    @Override // soot.defaultInterfaceMethods.D
    default void print() {
        System.out.println("C");
    }
}
