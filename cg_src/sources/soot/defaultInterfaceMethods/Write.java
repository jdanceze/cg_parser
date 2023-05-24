package soot.defaultInterfaceMethods;
/* compiled from: InterfaceSameSignature.java */
/* loaded from: gencallgraphv3.jar:soot/defaultInterfaceMethods/Write.class */
interface Write {
    default void write() {
        System.out.println("Writing to console output..");
    }

    default void print() {
        System.out.println("This is a write method");
    }
}
