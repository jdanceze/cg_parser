package scm;

import jas.ClassEnv;
import jas.CodeAttr;
import jas.ExceptAttr;
/* compiled from: AutoProcs.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/scmaddMethod.class */
class scmaddMethod extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            throw new SchemeError("jas-class-addmethod expects 6 arguments");
        }
        Obj tmp = args.car != null ? args.car.eval(f) : null;
        Cell t = args.cdr;
        if (tmp == null || (tmp instanceof primnode)) {
            if (tmp == null || (((primnode) tmp).val instanceof ClassEnv)) {
                ClassEnv arg0 = tmp != null ? (ClassEnv) ((primnode) tmp).val : null;
                if (t == null) {
                    throw new SchemeError("jas-class-addmethod expects 6 arguments");
                }
                Obj tmp2 = t.car != null ? t.car.eval(f) : null;
                Cell t2 = t.cdr;
                if (tmp2 instanceof Selfrep) {
                    short arg1 = (short) ((Selfrep) tmp2).num;
                    if (t2 == null) {
                        throw new SchemeError("jas-class-addmethod expects 6 arguments");
                    }
                    Obj tmp3 = t2.car != null ? t2.car.eval(f) : null;
                    Cell t3 = t2.cdr;
                    if (tmp3 == null || (tmp3 instanceof Selfrep)) {
                        String arg2 = tmp3 != null ? ((Selfrep) tmp3).val : null;
                        if (t3 == null) {
                            throw new SchemeError("jas-class-addmethod expects 6 arguments");
                        }
                        Obj tmp4 = t3.car != null ? t3.car.eval(f) : null;
                        Cell t4 = t3.cdr;
                        if (tmp4 == null || (tmp4 instanceof Selfrep)) {
                            String arg3 = tmp4 != null ? ((Selfrep) tmp4).val : null;
                            if (t4 == null) {
                                throw new SchemeError("jas-class-addmethod expects 6 arguments");
                            }
                            Obj tmp5 = t4.car != null ? t4.car.eval(f) : null;
                            Cell t5 = t4.cdr;
                            if (tmp5 == null || (tmp5 instanceof primnode)) {
                                if (tmp5 == null || (((primnode) tmp5).val instanceof CodeAttr)) {
                                    CodeAttr arg4 = tmp5 != null ? (CodeAttr) ((primnode) tmp5).val : null;
                                    if (t5 == null) {
                                        throw new SchemeError("jas-class-addmethod expects 6 arguments");
                                    }
                                    Obj tmp6 = t5.car != null ? t5.car.eval(f) : null;
                                    Cell cell = t5.cdr;
                                    if (tmp6 == null || (tmp6 instanceof primnode)) {
                                        if (tmp6 == null || (((primnode) tmp6).val instanceof ExceptAttr)) {
                                            ExceptAttr arg5 = tmp6 != null ? (ExceptAttr) ((primnode) tmp6).val : null;
                                            arg0.addMethod(arg1, arg2, arg3, arg4, arg5);
                                            return null;
                                        }
                                        throw new SchemeError("jas-class-addmethod expects a ExceptAttr for arg #6");
                                    }
                                    throw new SchemeError("jas-class-addmethod expects a ExceptAttr for arg #6");
                                }
                                throw new SchemeError("jas-class-addmethod expects a CodeAttr for arg #5");
                            }
                            throw new SchemeError("jas-class-addmethod expects a CodeAttr for arg #5");
                        }
                        throw new SchemeError("jas-class-addmethod expects a String for arg #4");
                    }
                    throw new SchemeError("jas-class-addmethod expects a String for arg #3");
                }
                throw new SchemeError("jas-class-addmethod expects a number for arg #2");
            }
            throw new SchemeError("jas-class-addmethod expects a ClassEnv for arg #1");
        }
        throw new SchemeError("jas-class-addmethod expects a ClassEnv for arg #1");
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#jas-class-addmethod#>";
    }
}
