package soot.defaultInterfaceMethods;
/* loaded from: gencallgraphv3.jar:soot/defaultInterfaceMethods/SimpleDefaultInterface.class */
public class SimpleDefaultInterface implements Default {
    public void main() {
        SimpleDefaultInterface testClass = new SimpleDefaultInterface();
        testClass.target();
        testClass.printMessage();
    }

    @Override // soot.defaultInterfaceMethods.Default
    public void printMessage() {
        System.out.println("Hello World!");
    }
}
