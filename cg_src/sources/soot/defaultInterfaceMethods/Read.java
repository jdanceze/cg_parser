package soot.defaultInterfaceMethods;
/* compiled from: InterfaceSameSignature.java */
/* loaded from: gencallgraphv3.jar:soot/defaultInterfaceMethods/Read.class */
interface Read {
    default void read() {
        System.out.println("Reading the console input..");
    }

    default void print() {
        System.out.println("This is a read method");
    }
}
