package soot.toDex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import soot.Local;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.Constant;
/* loaded from: gencallgraphv3.jar:soot/toDex/RegisterAllocator.class */
public class RegisterAllocator {
    private int nextRegNum;
    private int paramRegCount;
    private int lastReg;
    private Register currentLocalRegister;
    private List<Register> classConstantReg = new ArrayList();
    private List<Register> nullConstantReg = new ArrayList();
    private List<Register> floatConstantReg = new ArrayList();
    private List<Register> intConstantReg = new ArrayList();
    private List<Register> longConstantReg = new ArrayList();
    private List<Register> doubleConstantReg = new ArrayList();
    private List<Register> stringConstantReg = new ArrayList();
    private AtomicInteger classI = new AtomicInteger(0);
    private AtomicInteger nullI = new AtomicInteger(0);
    private AtomicInteger floatI = new AtomicInteger(0);
    private AtomicInteger intI = new AtomicInteger(0);
    private AtomicInteger longI = new AtomicInteger(0);
    private AtomicInteger doubleI = new AtomicInteger(0);
    private AtomicInteger stringI = new AtomicInteger(0);
    private Set<Register> lockedRegisters = new HashSet();
    private Map<Local, Integer> localToLastRegNum = new HashMap();

    /* JADX WARN: Incorrect condition in loop: B:33:0x0129 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private soot.toDex.Register asConstant(soot.jimple.Constant r7, soot.toDex.ConstantVisitor r8) {
        /*
            Method dump skipped, instructions count: 315
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.toDex.RegisterAllocator.asConstant(soot.jimple.Constant, soot.toDex.ConstantVisitor):soot.toDex.Register");
    }

    public void resetImmediateConstantsPool() {
        this.classI = new AtomicInteger(0);
        this.nullI = new AtomicInteger(0);
        this.floatI = new AtomicInteger(0);
        this.intI = new AtomicInteger(0);
        this.longI = new AtomicInteger(0);
        this.doubleI = new AtomicInteger(0);
        this.stringI = new AtomicInteger(0);
    }

    public Map<Local, Integer> getLocalToRegisterMapping() {
        return this.localToLastRegNum;
    }

    public Register asLocal(Local local) {
        Register localRegister;
        Integer oldRegNum = this.localToLastRegNum.get(local);
        if (oldRegNum != null) {
            localRegister = new Register(local.getType(), oldRegNum.intValue());
        } else {
            localRegister = new Register(local.getType(), this.nextRegNum);
            this.localToLastRegNum.put(local, Integer.valueOf(this.nextRegNum));
            this.nextRegNum += SootToDexUtils.getDexWords(local.getType());
        }
        return localRegister;
    }

    public void asParameter(SootMethod sm, Local l) {
        if (this.localToLastRegNum.containsKey(l)) {
            return;
        }
        int paramRegNum = 0;
        boolean found = false;
        if (!sm.isStatic()) {
            try {
                if (sm.getActiveBody().getThisLocal() == l) {
                    paramRegNum = 0;
                    found = true;
                }
            } catch (RuntimeException e) {
            }
        }
        if (!found) {
            int i = 0;
            while (true) {
                if (i >= sm.getParameterCount()) {
                    break;
                } else if (sm.getActiveBody().getParameterLocal(i) == l) {
                    if (!sm.isStatic()) {
                        paramRegNum++;
                    }
                    found = true;
                } else {
                    Type paramType = sm.getParameterType(i);
                    paramRegNum += SootToDexUtils.getDexWords(paramType);
                    i++;
                }
            }
        }
        if (!found) {
            throw new RuntimeException("Parameter local not found");
        }
        this.localToLastRegNum.put(l, Integer.valueOf(paramRegNum));
        int wordsforParameters = SootToDexUtils.getDexWords(l.getType());
        this.nextRegNum = Math.max(this.nextRegNum + wordsforParameters, paramRegNum + wordsforParameters);
        this.paramRegCount += wordsforParameters;
    }

    public Register asImmediate(Value v, ConstantVisitor constantV) {
        if (v instanceof Constant) {
            return asConstant((Constant) v, constantV);
        }
        if (v instanceof Local) {
            return asLocal((Local) v);
        }
        throw new RuntimeException("expected Immediate (Constant or Local), but was: " + v.getClass());
    }

    public Register asTmpReg(Type regType) {
        int newRegCount = getRegCount();
        if (this.lastReg == newRegCount) {
            return this.currentLocalRegister;
        }
        this.currentLocalRegister = asLocal(new TemporaryRegisterLocal(regType));
        this.lastReg = newRegCount;
        return this.currentLocalRegister;
    }

    public void increaseRegCount(int amount) {
        this.nextRegNum += amount;
    }

    public int getParamRegCount() {
        return this.paramRegCount;
    }

    public int getRegCount() {
        return this.nextRegNum;
    }

    public void lockRegister(Register reg) {
        this.lockedRegisters.add(reg);
    }
}
