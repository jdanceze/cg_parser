package soot.jbco.bafTransformations;

import java.util.Map;
import java.util.Stack;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Type;
import soot.Unit;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.Debugger;
/* loaded from: gencallgraphv3.jar:soot/jbco/bafTransformations/BAFPrintout.class */
public class BAFPrintout extends BodyTransformer implements IJbcoTransform {
    public static String name = "bb.printout";
    static boolean stack = false;

    @Override // soot.jbco.IJbcoTransform
    public void outputSummary() {
    }

    @Override // soot.jbco.IJbcoTransform
    public String[] getDependencies() {
        return new String[0];
    }

    @Override // soot.jbco.IJbcoTransform
    public String getName() {
        return name;
    }

    public BAFPrintout() {
    }

    public BAFPrintout(String newname, boolean print_stack) {
        name = newname;
        stack = print_stack;
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Map<Unit, Stack<Type>> stacks;
        System.out.println("\n" + b.getMethod().getSignature());
        if (stack) {
            Map<Local, Local> b2j = Main.methods2Baf2JLocals.get(b.getMethod());
            try {
                if (b2j == null) {
                    stacks = StackTypeHeightCalculator.calculateStackHeights(b);
                } else {
                    stacks = StackTypeHeightCalculator.calculateStackHeights(b, b2j);
                }
                StackTypeHeightCalculator.printStack(b.getUnits(), stacks, true);
            } catch (Exception exc) {
                System.out.println("\n**************Exception calculating height " + exc + ", printing plain bytecode now\n\n");
                Debugger.printUnits(b, "  FINAL");
            }
        } else {
            Debugger.printUnits(b, "  FINAL");
        }
        System.out.println();
    }
}
