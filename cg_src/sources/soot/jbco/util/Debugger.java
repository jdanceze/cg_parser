package soot.jbco.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.PatchingChain;
import soot.Trap;
import soot.Unit;
import soot.baf.JSRInst;
import soot.baf.TableSwitchInst;
import soot.baf.TargetArgInst;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/jbco/util/Debugger.class */
public class Debugger {
    public static void printBaf(Body b) {
        System.out.println(String.valueOf(b.getMethod().getName()) + "\n");
        int i = 0;
        Map<Unit, Integer> index = new HashMap<>();
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            int i2 = i;
            i++;
            index.put(it.next(), new Integer(i2));
        }
        Iterator<Unit> it2 = b.getUnits().iterator();
        while (it2.hasNext()) {
            Object o = it2.next();
            System.out.println(String.valueOf(index.get(o).toString()) + Instruction.argsep + o + Instruction.argsep + (o instanceof TargetArgInst ? index.get(((TargetArgInst) o).getTarget()).toString() : ""));
        }
        System.out.println("\n");
    }

    public static void printUnits(Body b, String msg) {
        int i = 0;
        Map<Unit, Integer> numbers = new HashMap<>();
        PatchingChain<Unit> u = b.getUnits();
        Iterator<Unit> it = u.snapshotIterator();
        while (it.hasNext()) {
            int i2 = i;
            i++;
            numbers.put(it.next(), new Integer(i2));
        }
        int jsr = 0;
        System.out.println("\r\r" + b.getMethod().getName() + "  " + msg);
        Iterator<Unit> udit = u.snapshotIterator();
        while (udit.hasNext()) {
            Unit unit = udit.next();
            Integer numb = numbers.get(unit);
            if (numb.intValue() == 149) {
                System.out.println("hi");
            }
            if (unit instanceof TargetArgInst) {
                if (unit instanceof JSRInst) {
                    jsr++;
                }
                TargetArgInst ti = (TargetArgInst) unit;
                if (ti.getTarget() == null) {
                    System.out.println(unit + " null null null null null null null null null");
                } else {
                    System.out.println(String.valueOf(numbers.get(unit).toString()) + Instruction.argsep + unit + "   #" + numbers.get(ti.getTarget()).toString());
                }
            } else if (unit instanceof TableSwitchInst) {
                TableSwitchInst tswi = (TableSwitchInst) unit;
                System.out.println(String.valueOf(numbers.get(unit).toString()) + " SWITCH:");
                System.out.println("\tdefault: " + tswi.getDefaultTarget() + "  " + numbers.get(tswi.getDefaultTarget()).toString());
                int index = 0;
                for (int x = tswi.getLowIndex(); x <= tswi.getHighIndex(); x++) {
                    int i3 = index;
                    index++;
                    System.out.println("\t " + x + ": " + tswi.getTarget(index) + "  " + numbers.get(tswi.getTarget(i3)).toString());
                }
            } else {
                System.out.println(String.valueOf(numb.toString()) + Instruction.argsep + unit);
            }
        }
        for (Trap t : b.getTraps()) {
            System.out.println(numbers.get(t.getBeginUnit()) + Instruction.argsep + t.getBeginUnit() + " to " + numbers.get(t.getEndUnit()) + Instruction.argsep + t.getEndUnit() + "  at " + numbers.get(t.getHandlerUnit()) + Instruction.argsep + t.getHandlerUnit());
        }
        if (jsr > 0) {
            System.out.println("\r\tJSR Instructions: " + jsr);
        }
    }

    public static void printUnits(PatchingChain<Unit> u, String msg) {
        int i = 0;
        HashMap<Unit, Integer> numbers = new HashMap<>();
        Iterator<Unit> it = u.snapshotIterator();
        while (it.hasNext()) {
            int i2 = i;
            i++;
            numbers.put(it.next(), new Integer(i2));
        }
        System.out.println("\r\r***********  " + msg);
        Iterator<Unit> udit = u.snapshotIterator();
        while (udit.hasNext()) {
            Unit unit = udit.next();
            Integer numb = numbers.get(unit);
            if (numb.intValue() == 149) {
                System.out.println("hi");
            }
            if (unit instanceof TargetArgInst) {
                TargetArgInst ti = (TargetArgInst) unit;
                if (ti.getTarget() == null) {
                    System.out.println(unit + " null null null null null null null null null");
                } else {
                    System.out.println(String.valueOf(numbers.get(unit).toString()) + Instruction.argsep + unit + "   #" + numbers.get(ti.getTarget()).toString());
                }
            } else if (unit instanceof TableSwitchInst) {
                TableSwitchInst tswi = (TableSwitchInst) unit;
                System.out.println(String.valueOf(numbers.get(unit).toString()) + " SWITCH:");
                System.out.println("\tdefault: " + tswi.getDefaultTarget() + "  " + numbers.get(tswi.getDefaultTarget()).toString());
                int index = 0;
                for (int x = tswi.getLowIndex(); x <= tswi.getHighIndex(); x++) {
                    int i3 = index;
                    index++;
                    System.out.println("\t " + x + ": " + tswi.getTarget(index) + "  " + numbers.get(tswi.getTarget(i3)).toString());
                }
            } else {
                System.out.println(String.valueOf(numb.toString()) + Instruction.argsep + unit);
            }
        }
    }
}
