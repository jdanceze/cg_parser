package fj.data;

import fj.F;
import fj.P2;
import java.io.BufferedReader;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$20.class */
public final /* synthetic */ class IOFunctions$$Lambda$20 implements F {
    private static final IOFunctions$$Lambda$20 instance = new IOFunctions$$Lambda$20();

    private IOFunctions$$Lambda$20() {
    }

    @Override // fj.F
    public Object f(Object obj) {
        P2 lambda$null$132;
        lambda$null$132 = IOFunctions.lambda$null$132((BufferedReader) obj);
        return lambda$null$132;
    }

    public static F lambdaFactory$() {
        return instance;
    }
}
