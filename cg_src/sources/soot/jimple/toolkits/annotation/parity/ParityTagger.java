package soot.jimple.toolkits.annotation.parity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Value;
import soot.ValueBox;
import soot.coffi.Instruction;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.tagkit.ColorTag;
import soot.tagkit.KeyTag;
import soot.tagkit.StringTag;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.scalar.LiveLocals;
import soot.toolkits.scalar.SimpleLiveLocals;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/parity/ParityTagger.class */
public class ParityTagger extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(ParityTagger.class);

    public ParityTagger(Singletons.Global g) {
    }

    public static ParityTagger v() {
        return G.v().soot_jimple_toolkits_annotation_parity_ParityTagger();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map options) {
        ParityAnalysis a;
        boolean isInteractive = Options.v().interactive_mode();
        Options.v().set_interactive_mode(false);
        if (isInteractive) {
            LiveLocals sll = new SimpleLiveLocals(new BriefUnitGraph(b));
            Options.v().set_interactive_mode(isInteractive);
            a = new ParityAnalysis(new BriefUnitGraph(b), sll);
        } else {
            a = new ParityAnalysis(new BriefUnitGraph(b));
        }
        Iterator sIt = b.getUnits().iterator();
        while (sIt.hasNext()) {
            Stmt s = (Stmt) sIt.next();
            HashMap parityVars = (HashMap) a.getFlowAfter(s);
            for (Value variable : parityVars.keySet()) {
                if (!(variable instanceof IntConstant) && !(variable instanceof LongConstant)) {
                    StringTag t = new StringTag("Parity variable: " + variable + Instruction.argsep + parityVars.get(variable), "Parity Analysis");
                    s.addTag(t);
                }
            }
            HashMap parityVarsUses = (HashMap) a.getFlowBefore(s);
            HashMap parityVarsDefs = (HashMap) a.getFlowAfter(s);
            for (ValueBox vb : s.getUseBoxes()) {
                if (parityVarsUses.containsKey(vb.getValue())) {
                    String type = (String) parityVarsUses.get(vb.getValue());
                    addColorTag(vb, type);
                }
            }
            for (ValueBox vb2 : s.getDefBoxes()) {
                if (parityVarsDefs.containsKey(vb2.getValue())) {
                    String type2 = (String) parityVarsDefs.get(vb2.getValue());
                    addColorTag(vb2, type2);
                }
            }
        }
        boolean keysAdded = false;
        for (Object next : b.getMethod().getDeclaringClass().getTags()) {
            if ((next instanceof KeyTag) && ((KeyTag) next).analysisType().equals("Parity Analysis")) {
                keysAdded = true;
            }
        }
        if (!keysAdded) {
            b.getMethod().getDeclaringClass().addTag(new KeyTag(255, 0, 0, "Parity: Top", "Parity Analysis"));
            b.getMethod().getDeclaringClass().addTag(new KeyTag(45, 255, 84, "Parity: Bottom", "Parity Analysis"));
            b.getMethod().getDeclaringClass().addTag(new KeyTag(255, 248, 35, "Parity: Even", "Parity Analysis"));
            b.getMethod().getDeclaringClass().addTag(new KeyTag(174, 210, 255, "Parity: Odd", "Parity Analysis"));
        }
    }

    private void addColorTag(ValueBox vb, String type) {
        if (type.equals("bottom")) {
            vb.addTag(new ColorTag(1, "Parity Analysis"));
        } else if (type.equals("top")) {
            vb.addTag(new ColorTag(0, "Parity Analysis"));
        } else if (type.equals("even")) {
            vb.addTag(new ColorTag(2, "Parity Analysis"));
        } else if (type.equals("odd")) {
            vb.addTag(new ColorTag(3, "Parity Analysis"));
        }
    }
}
