package soot.lambdaMetaFactory;
/* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/Adapt.class */
public class Adapt {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/Adapt$FloatInterfaceParam.class */
    public interface FloatInterfaceParam {
        void adapt(float f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/Adapt$IntInterfaceParam.class */
    public interface IntInterfaceParam {
        void adapt(int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/Adapt$IntInterfaceReturn.class */
    public interface IntInterfaceReturn {
        int adapt();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/Adapt$IntegerInterfaceParam.class */
    public interface IntegerInterfaceParam {
        void adapt(Integer num);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/Adapt$IntegerInterfaceReturn.class */
    public interface IntegerInterfaceReturn {
        Integer adapt();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/Adapt$LongInterfaceParam.class */
    public interface LongInterfaceParam {
        void adapt(long j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/Adapt$LongInterfaceReturn.class */
    public interface LongInterfaceReturn {
        long adapt();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/Adapt$NumberInterfaceParam.class */
    public interface NumberInterfaceParam {
        void adapt(Number number);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/Adapt$ObjectInterfaceParam.class */
    public interface ObjectInterfaceParam {
        void adapt(Object obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/Adapt$ShortInterfaceParam.class */
    public interface ShortInterfaceParam {
        void adapt(Short sh);
    }

    /* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/Adapt$ShortInterfaceReturn.class */
    interface ShortInterfaceReturn {
        short adapt();
    }

    public void parameterBoxingTarget() {
        parameterBoxing(p -> {
            System.out.println(p);
        }, p2 -> {
            System.out.println(p2);
        }, p3 -> {
            System.out.println(p3);
        });
    }

    public void parameterBoxing(IntInterfaceParam I0param, IntegerInterfaceParam I1param, LongInterfaceParam L0param) {
        I0param.getClass();
        IntegerInterfaceParam I1var = (v1) -> {
            r0.adapt(v1);
        };
        I1var.adapt(1);
        I0param.getClass();
        ShortInterfaceParam S1var = (v1) -> {
            r0.adapt(v1);
        };
        S1var.adapt((short) 1);
        I1param.getClass();
        IntInterfaceParam I0var = (v1) -> {
            r0.adapt(v1);
        };
        I0var.adapt(1);
    }

    public void parameterWidening() {
        parameterWidening(p -> {
            System.out.println(p);
        }, p2 -> {
            System.out.println(p2);
        }, p3 -> {
            System.out.println(p3);
        }, p4 -> {
            System.out.println(p4);
        }, p5 -> {
            System.out.println(p5);
        }, p6 -> {
            System.out.println(p6);
        });
    }

    public void parameterWidening(IntInterfaceParam I0param, IntegerInterfaceParam I1param, LongInterfaceParam L0param, FloatInterfaceParam F0param, NumberInterfaceParam Nparam, ObjectInterfaceParam Oparam) {
        L0param.getClass();
        IntInterfaceParam I0var = (v1) -> {
            r0.adapt(v1);
        };
        I0var.adapt(1);
        F0param.getClass();
        IntInterfaceParam I0var2 = (v1) -> {
            r0.adapt(v1);
        };
        I0var2.adapt(1);
        Nparam.getClass();
        (v1) -> {
            r0.adapt(v1);
        };
        Oparam.getClass();
        (v1) -> {
            r0.adapt(v1);
        };
    }

    public void returnBoxing() {
        returnBoxing(() -> {
            return 1;
        }, () -> {
            return new Integer(2);
        }, () -> {
            return 3L;
        });
    }

    public void returnBoxing(IntInterfaceReturn I0param, IntegerInterfaceReturn I1param, LongInterfaceReturn L0param) {
        I0param.getClass();
        IntegerInterfaceReturn I1var = this::adapt;
        I1var.adapt();
        I1param.getClass();
        IntInterfaceReturn I0var = this::adapt;
        I0var.adapt();
    }

    public void returnWidening() {
        returnWidening(() -> {
            return 1;
        }, () -> {
            return new Integer(2);
        }, () -> {
            return 3L;
        });
    }

    public void returnWidening(IntInterfaceReturn I0param, IntegerInterfaceReturn I1param, LongInterfaceReturn L0param) {
        I0param.getClass();
        LongInterfaceReturn L0var = this::adapt;
        L0var.adapt();
    }
}
