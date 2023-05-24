package soot.jbco;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.Pack;
import soot.PackManager;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.Transformer;
import soot.coffi.Instruction;
import soot.jbco.bafTransformations.AddJSRs;
import soot.jbco.bafTransformations.BAFCounter;
import soot.jbco.bafTransformations.BAFPrintout;
import soot.jbco.bafTransformations.BafLineNumberer;
import soot.jbco.bafTransformations.ConstructorConfuser;
import soot.jbco.bafTransformations.FindDuplicateSequences;
import soot.jbco.bafTransformations.FixUndefinedLocals;
import soot.jbco.bafTransformations.IfNullToTryCatch;
import soot.jbco.bafTransformations.IndirectIfJumpsToCaughtGotos;
import soot.jbco.bafTransformations.Jimple2BafLocalBuilder;
import soot.jbco.bafTransformations.LocalsToBitField;
import soot.jbco.bafTransformations.MoveLoadsAboveIfs;
import soot.jbco.bafTransformations.RemoveRedundantPushStores;
import soot.jbco.bafTransformations.TryCatchCombiner;
import soot.jbco.bafTransformations.UpdateConstantsToFields;
import soot.jbco.bafTransformations.WrapSwitchesInTrys;
import soot.jbco.jimpleTransformations.AddSwitches;
import soot.jbco.jimpleTransformations.ArithmeticTransformer;
import soot.jbco.jimpleTransformations.BuildIntermediateAppClasses;
import soot.jbco.jimpleTransformations.ClassRenamer;
import soot.jbco.jimpleTransformations.CollectConstants;
import soot.jbco.jimpleTransformations.CollectJimpleLocals;
import soot.jbco.jimpleTransformations.FieldRenamer;
import soot.jbco.jimpleTransformations.GotoInstrumenter;
import soot.jbco.jimpleTransformations.LibraryMethodWrappersBuilder;
import soot.jbco.jimpleTransformations.MethodRenamer;
import soot.tagkit.LineNumberTagAggregator;
/* loaded from: gencallgraphv3.jar:soot/jbco/Main.class */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static boolean jbcoDebug = false;
    public static boolean jbcoSummary = true;
    public static boolean jbcoVerbose = false;
    public static boolean metrics = false;
    public static Map<String, Integer> transformsToWeights = new ConcurrentHashMap();
    public static Map<String, Map<Object, Integer>> transformsToMethodsToWeights = new ConcurrentHashMap();
    public static Map method2Locals2REALTypes = new ConcurrentHashMap();
    public static Map<SootMethod, Map<Local, Local>> methods2Baf2JLocals = new ConcurrentHashMap();
    public static Map<SootMethod, List<Local>> methods2JLocals = new ConcurrentHashMap();
    public static List<SootClass> IntermediateAppClasses = new CopyOnWriteArrayList();
    static List<Transformer> jbcotransforms = new CopyOnWriteArrayList();
    static String[][] optionStrings = {new String[]{"Rename Classes", "Rename Methods", "Rename Fields", "Build API Buffer Methods", "Build Library Buffer Classes", "Goto Instruction Augmentation", "Add Dead Switche Statements", "Convert Arith. Expr. To Bitshifting Ops", "Convert Branches to JSR Instructions", "Disobey Constructor Conventions", "Reuse Duplicate Sequences", "Replace If(Non)Nulls with Try-Catch", "Indirect If Instructions", "Pack Locals into Bitfields", "Reorder Loads Above Ifs", "Combine Try and Catch Blocks", "Embed Constants in Fields", "Partially Trap Switches"}, new String[]{ClassRenamer.name, MethodRenamer.name, FieldRenamer.name, LibraryMethodWrappersBuilder.name, "wjtp.jbco_bapibm", GotoInstrumenter.name, "jtp.jbco_adss", "jtp.jbco_cae2bo", "bb.jbco_cb2ji", "bb.jbco_dcc", "bb.jbco_rds", "bb.jbco_riitcb", "bb.jbco_iii", "bb.jbco_plvb", "bb.jbco_rlaii", "bb.jbco_ctbcb", "bb.jbco_ecvf", "bb.jbco_ptss"}};

    /* JADX WARN: Multi-variable type inference failed */
    public static void main(String[] argv) {
        String str;
        int rcount = 0;
        Vector<String> transformsToAdd = new Vector<>();
        boolean[] remove = new boolean[argv.length];
        for (int i = 0; i < argv.length; i++) {
            String arg = argv[i];
            if (arg.equals("-jbco:help")) {
                System.out.println("The Java Bytecode Obfuscator (JBCO)\n\nGeneral Options:");
                System.out.println("\t-jbco:help     -  print this help message.");
                System.out.println("\t-jbco:verbose  -  print extra information during obfuscation.");
                System.out.println("\t-jbco:silent   -  turn off all output, including summary information.");
                System.out.println("\t-jbco:metrics  -  calculate total vertices and edges;\n\t                  calculate avg. and highest graph degrees.");
                System.out.println("\t-jbco:debug    -  turn on extra debugging like\n\t                  stack height and type verifier.\n\nTransformations ( -t:[W:]<name>[:pattern] )\n\tW              -  specify obfuscation weight (0-9)\n\t<name>         -  name of obfuscation (from list below)\n\tpattern        -  limit to method names matched by pattern\n\t                  prepend * to pattern if a regex\n");
                for (int j = 0; j < optionStrings[0].length; j++) {
                    String line = "\t" + optionStrings[1][j];
                    int size = 20 - line.length();
                    while (true) {
                        int i2 = size;
                        size--;
                        if (i2 <= 0) {
                            break;
                        }
                        line = String.valueOf(line) + Instruction.argsep;
                    }
                    System.out.println(String.valueOf(line) + "-  " + optionStrings[0][j]);
                }
                System.exit(0);
            } else if (arg.equals("-jbco:metrics")) {
                metrics = true;
                remove[i] = true;
                rcount++;
            } else if (arg.startsWith("-jbco:name:")) {
                remove[i] = true;
                rcount++;
            } else if (arg.startsWith("-t:")) {
                String arg2 = arg.substring(3);
                int tweight = 9;
                char cweight = arg2.charAt(0);
                if (cweight >= '0' && cweight <= '9') {
                    try {
                        tweight = Integer.parseInt(arg2.substring(0, 1));
                    } catch (NumberFormatException e) {
                        logger.debug("Improperly formated transformation weight: " + argv[i]);
                        System.exit(1);
                    }
                    arg2 = arg2.substring(arg2.indexOf(58) + 1);
                }
                transformsToAdd.add(arg2);
                transformsToWeights.put(arg2, new Integer(tweight));
                if (arg2.equals(FieldRenamer.name)) {
                    FieldRenamer.v().setRenameFields(true);
                }
                remove[i] = true;
                rcount++;
            } else if (arg.equals("-jbco:verbose")) {
                jbcoVerbose = true;
                remove[i] = true;
                rcount++;
            } else if (arg.equals("-jbco:silent")) {
                jbcoSummary = false;
                jbcoVerbose = false;
                remove[i] = true;
                rcount++;
            } else if (arg.equals("-jbco:debug")) {
                jbcoDebug = true;
                remove[i] = true;
                rcount++;
            } else if (arg.startsWith("-i") && arg.length() > 4 && arg.charAt(3) == ':' && arg.charAt(2) == 't') {
                Object o = null;
                String arg3 = arg.substring(4);
                int tweight2 = 9;
                char cweight2 = arg3.charAt(0);
                if (cweight2 >= '0' && cweight2 <= '9') {
                    try {
                        tweight2 = Integer.parseInt(arg3.substring(0, 1));
                    } catch (NumberFormatException e2) {
                        logger.debug("Improperly formatted transformation weight: " + argv[i]);
                        System.exit(1);
                    }
                    if (arg3.indexOf(58) < 0) {
                        logger.debug("Illegally Formatted Option: " + argv[i]);
                        System.exit(1);
                    }
                    arg3 = arg3.substring(arg3.indexOf(58) + 1);
                }
                int index = arg3.indexOf(58);
                if (index < 0) {
                    logger.debug("Illegally Formatted Option: " + argv[i]);
                    System.exit(1);
                }
                String trans = arg3.substring(0, index);
                String arg4 = arg3.substring(index + 1, arg3.length());
                if (arg4.startsWith("*")) {
                    String arg5 = arg4.substring(1);
                    try {
                        o = Pattern.compile(arg5);
                    } catch (PatternSyntaxException e3) {
                        logger.debug("Illegal Regular Expression Pattern: " + arg5);
                        System.exit(1);
                    }
                } else {
                    o = arg4;
                }
                transformsToAdd.add(trans);
                Map<Object, Integer> htmp = transformsToMethodsToWeights.get(trans);
                Map<Object, Integer> htmp2 = htmp;
                if (htmp == null) {
                    Map<Object, Integer> htmp3 = new HashMap<>();
                    transformsToMethodsToWeights.put(trans, htmp3);
                    htmp2 = htmp3;
                }
                htmp2.put(o, new Integer(tweight2));
                remove[i] = true;
                rcount++;
            } else {
                remove[i] = false;
            }
        }
        if (rcount > 0) {
            int index2 = 0;
            String[] tmp = (String[]) argv.clone();
            argv = new String[argv.length - rcount];
            for (int i3 = 0; i3 < tmp.length; i3++) {
                if (!remove[i3]) {
                    int i4 = index2;
                    index2++;
                    argv[i4] = tmp[i3];
                }
            }
        }
        transformsToAdd.remove(CollectConstants.name);
        transformsToAdd.remove("jtp.jbco_jl");
        transformsToAdd.remove("bb.jbco_j2bl");
        transformsToAdd.remove("bb.jbco_ful");
        if (!metrics) {
            if (transformsToAdd.size() == 0) {
                logger.debug("No Jbco tasks to complete.  Shutting Down...");
                System.exit(0);
            }
            Pack wjtp = PackManager.v().getPack("wjtp");
            Pack jtp = PackManager.v().getPack("jtp");
            Pack bb = PackManager.v().getPack("bb");
            if (transformsToAdd.contains("jtp.jbco_adss")) {
                wjtp.add(new Transform(FieldRenamer.name, newTransform((Transformer) getTransform(FieldRenamer.name))));
                if (transformsToAdd.remove(FieldRenamer.name)) {
                    FieldRenamer.v().setRenameFields(true);
                }
            }
            if (transformsToAdd.contains("bb.jbco_ecvf")) {
                wjtp.add(new Transform(CollectConstants.name, newTransform((Transformer) getTransform(CollectConstants.name))));
            }
            String jl = null;
            int i5 = 0;
            while (true) {
                if (i5 < transformsToAdd.size()) {
                    if (!transformsToAdd.get(i5).startsWith("bb")) {
                        i5++;
                    } else {
                        jl = "jtp.jbco_jl";
                        jtp.add(new Transform(jl, newTransform((Transformer) getTransform(jl))));
                        bb.insertBefore(new Transform("bb.jbco_j2bl", newTransform((Transformer) getTransform("bb.jbco_j2bl"))), "bb.lso");
                        bb.insertBefore(new Transform("bb.jbco_ful", newTransform((Transformer) getTransform("bb.jbco_ful"))), "bb.lso");
                        bb.add(new Transform("bb.jbco_rrps", newTransform((Transformer) getTransform("bb.jbco_rrps"))));
                        break;
                    }
                } else {
                    break;
                }
            }
            for (int i6 = 0; i6 < transformsToAdd.size(); i6++) {
                String tname = transformsToAdd.get(i6);
                IJbcoTransform t = getTransform(tname);
                Pack p = tname.startsWith("wjtp") ? wjtp : tname.startsWith("jtp") ? jtp : bb;
                String insertBefore = p == jtp ? jl : p == bb ? "bb.jbco_ful" : null;
                if (insertBefore != null) {
                    p.insertBefore(new Transform(tname, newTransform((Transformer) t)), insertBefore);
                } else {
                    p.add(new Transform(tname, newTransform((Transformer) t)));
                }
            }
            Iterator<Transform> phases = wjtp.iterator();
            while (true) {
                if (phases.hasNext()) {
                    if (phases.next().getPhaseName().indexOf("jbco") > 0) {
                        argv = checkWhole(argv, true);
                        break;
                    }
                } else {
                    break;
                }
            }
            if (jbcoSummary) {
                int i7 = 0;
                while (i7 < 3) {
                    Iterator<Transform> phases2 = i7 == 0 ? wjtp.iterator() : i7 == 1 ? jtp.iterator() : bb.iterator();
                    Logger logger2 = logger;
                    if (i7 == 0) {
                        str = "Whole Program Jimple Transformations:";
                    } else {
                        str = i7 == 1 ? "Jimple Method Body Transformations:" : "Baf Method Body Transformations:";
                    }
                    logger2.debug(str);
                    while (phases2.hasNext()) {
                        Transform o2 = phases2.next();
                        Transformer t2 = o2.getTransformer();
                        if (t2 instanceof IJbcoTransform) {
                            logger.debug("\t" + ((IJbcoTransform) t2).getName() + "  JBCO");
                        } else {
                            logger.debug("\t" + o2.getPhaseName() + "  default");
                        }
                    }
                    i7++;
                }
            }
            bb.add(new Transform("bb.jbco_bln", new BafLineNumberer()));
            bb.add(new Transform("bb.jbco_lta", LineNumberTagAggregator.v()));
        } else {
            argv = checkWhole(argv, false);
        }
        soot.Main.main(argv);
        if (jbcoSummary) {
            logger.debug("\n***** JBCO SUMMARY *****\n");
            for (Object o3 : jbcotransforms) {
                if (o3 instanceof IJbcoTransform) {
                    ((IJbcoTransform) o3).outputSummary();
                }
            }
            logger.debug("\n***** END SUMMARY *****\n");
        }
    }

    private static String[] checkWhole(String[] argv, boolean add) {
        for (int i = 0; i < argv.length; i++) {
            if (argv[i].equals("-w")) {
                if (add) {
                    return argv;
                } else {
                    String[] tmp = new String[argv.length - 1];
                    if (i == 0) {
                        System.arraycopy(argv, 1, tmp, 0, tmp.length);
                    } else {
                        System.arraycopy(argv, 0, tmp, 0, i);
                        if (i < argv.length - 1) {
                            System.arraycopy(argv, i + 1, tmp, i, tmp.length - i);
                        }
                    }
                    return tmp;
                }
            }
        }
        if (add) {
            String[] tmp2 = new String[argv.length + 1];
            tmp2[0] = "-w";
            System.arraycopy(argv, 0, tmp2, 1, argv.length);
            return tmp2;
        }
        return argv;
    }

    private static Transformer newTransform(Transformer t) {
        jbcotransforms.add(t);
        return t;
    }

    private static IJbcoTransform getTransform(String name) {
        if (name.startsWith("bb.jbco_rrps")) {
            return new RemoveRedundantPushStores();
        }
        if (name.startsWith("bb.printout")) {
            return new BAFPrintout(name, true);
        }
        if (name.equals("bb.jbco_j2bl")) {
            return new Jimple2BafLocalBuilder();
        }
        if (name.equals(GotoInstrumenter.name)) {
            return new GotoInstrumenter();
        }
        if (name.equals("jtp.jbco_cae2bo")) {
            return new ArithmeticTransformer();
        }
        if (name.equals(CollectConstants.name)) {
            return new CollectConstants();
        }
        if (name.equals("bb.jbco_ecvf")) {
            return new UpdateConstantsToFields();
        }
        if (name.equals("bb.jbco_rds")) {
            return new FindDuplicateSequences();
        }
        if (name.equals("bb.jbco_plvb")) {
            return new LocalsToBitField();
        }
        if (name.equals("bb.jbco_rlaii")) {
            return new MoveLoadsAboveIfs();
        }
        if (name.equals("bb.jbco_ptss")) {
            return new WrapSwitchesInTrys();
        }
        if (name.equals("bb.jbco_iii")) {
            return new IndirectIfJumpsToCaughtGotos();
        }
        if (name.equals("bb.jbco_ctbcb")) {
            return new TryCatchCombiner();
        }
        if (name.equals("bb.jbco_cb2ji")) {
            return new AddJSRs();
        }
        if (name.equals("bb.jbco_riitcb")) {
            return new IfNullToTryCatch();
        }
        if (name.equals(LibraryMethodWrappersBuilder.name)) {
            return new LibraryMethodWrappersBuilder();
        }
        if (name.equals("wjtp.jbco_bapibm")) {
            return new BuildIntermediateAppClasses();
        }
        if (name.equals(ClassRenamer.name)) {
            return ClassRenamer.v();
        }
        if (name.equals("bb.jbco_ful")) {
            return new FixUndefinedLocals();
        }
        if (name.equals(FieldRenamer.name)) {
            return FieldRenamer.v();
        }
        if (name.equals(MethodRenamer.name)) {
            return MethodRenamer.v();
        }
        if (name.equals("jtp.jbco_adss")) {
            return new AddSwitches();
        }
        if (name.equals("jtp.jbco_jl")) {
            return new CollectJimpleLocals();
        }
        if (name.equals("bb.jbco_dcc")) {
            return new ConstructorConfuser();
        }
        if (name.equals("bb.jbco_counter")) {
            return new BAFCounter();
        }
        return null;
    }

    private static int getWeight(String phasename) {
        int weight = 9;
        Integer intg = transformsToWeights.get(phasename);
        if (intg != null) {
            weight = intg.intValue();
        }
        return weight;
    }

    public static int getWeight(String phasename, String method) {
        Map<Object, Integer> htmp = transformsToMethodsToWeights.get(phasename);
        int result = 10;
        if (htmp != null) {
            for (Object o : htmp.keySet()) {
                Integer intg = null;
                if (o instanceof Pattern) {
                    intg = ((Pattern) o).matcher(method).matches() ? htmp.get(o) : 0;
                } else if ((o instanceof String) && method.equals(o)) {
                    intg = htmp.get(o);
                }
                if (intg != null && intg.intValue() < result) {
                    result = intg.intValue();
                }
            }
        }
        if (result > 9 || result < 0) {
            result = getWeight(phasename);
        }
        if (jbcoVerbose) {
            logger.debug("[" + phasename + "] Processing " + method + " with weight: " + result);
        }
        return result;
    }
}
