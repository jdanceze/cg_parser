package fj.data;

import fj.Unit;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$19.class */
final /* synthetic */ class IOFunctions$$Lambda$19 implements IO {
    private final String arg$1;

    private IOFunctions$$Lambda$19(String str) {
        this.arg$1 = str;
    }

    private static IO get$Lambda(String str) {
        return new IOFunctions$$Lambda$19(str);
    }

    @Override // fj.data.IO
    public Object run() {
        Unit lambda$stdoutPrintln$135;
        lambda$stdoutPrintln$135 = IOFunctions.lambda$stdoutPrintln$135(this.arg$1);
        return lambda$stdoutPrintln$135;
    }

    public static IO lambdaFactory$(String str) {
        return new IOFunctions$$Lambda$19(str);
    }
}
