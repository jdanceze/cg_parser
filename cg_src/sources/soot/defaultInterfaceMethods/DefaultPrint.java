package soot.defaultInterfaceMethods;
/* compiled from: SuperClassImplementsInterface.java */
/* loaded from: gencallgraphv3.jar:soot/defaultInterfaceMethods/DefaultPrint.class */
interface DefaultPrint {
    default void print() {
        System.out.println("This is default print method");
    }
}
