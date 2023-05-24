package soot;

import android.os.DropBoxManager;
import android.provider.MediaStore;
import heros.solver.CountingThreadPoolExecutor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.GZIPOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Singletons;
import soot.baf.Baf;
import soot.baf.BafASMBackend;
import soot.baf.BafBody;
import soot.baf.JasminClass;
import soot.baf.toolkits.base.LoadStoreOptimizer;
import soot.baf.toolkits.base.PeepholeOptimizer;
import soot.baf.toolkits.base.StoreChainOptimizer;
import soot.coffi.CFG;
import soot.coffi.Instruction;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.DavaBuildFile;
import soot.dava.DavaPrinter;
import soot.dava.DavaStaticBlockCleaner;
import soot.dava.toolkits.base.AST.interProcedural.InterProceduralAnalyses;
import soot.dava.toolkits.base.AST.transformations.RemoveEmptyBodyDefaultConstructor;
import soot.dava.toolkits.base.AST.transformations.VoidReturnRemover;
import soot.dava.toolkits.base.misc.PackageNamer;
import soot.dava.toolkits.base.misc.ThrowFinder;
import soot.grimp.Grimp;
import soot.grimp.GrimpBody;
import soot.grimp.toolkits.base.ConstructorFolder;
import soot.jimple.JimpleBody;
import soot.jimple.paddle.PaddleHook;
import soot.jimple.spark.SparkTransformer;
import soot.jimple.spark.fieldrw.FieldTagAggregator;
import soot.jimple.spark.fieldrw.FieldTagger;
import soot.jimple.toolkits.annotation.AvailExprTagger;
import soot.jimple.toolkits.annotation.DominatorsTagger;
import soot.jimple.toolkits.annotation.LineNumberAdder;
import soot.jimple.toolkits.annotation.arraycheck.ArrayBoundsChecker;
import soot.jimple.toolkits.annotation.arraycheck.RectangularArrayFinder;
import soot.jimple.toolkits.annotation.callgraph.CallGraphGrapher;
import soot.jimple.toolkits.annotation.callgraph.CallGraphTagger;
import soot.jimple.toolkits.annotation.defs.ReachingDefsTagger;
import soot.jimple.toolkits.annotation.fields.UnreachableFieldsTagger;
import soot.jimple.toolkits.annotation.liveness.LiveVarsTagger;
import soot.jimple.toolkits.annotation.logic.LoopInvariantFinder;
import soot.jimple.toolkits.annotation.methods.UnreachableMethodsTagger;
import soot.jimple.toolkits.annotation.nullcheck.NullCheckEliminator;
import soot.jimple.toolkits.annotation.nullcheck.NullPointerChecker;
import soot.jimple.toolkits.annotation.nullcheck.NullPointerColorer;
import soot.jimple.toolkits.annotation.parity.ParityTagger;
import soot.jimple.toolkits.annotation.profiling.ProfilingGenerator;
import soot.jimple.toolkits.annotation.purity.PurityAnalysis;
import soot.jimple.toolkits.annotation.qualifiers.TightestQualifiersTagger;
import soot.jimple.toolkits.annotation.tags.ArrayNullTagAggregator;
import soot.jimple.toolkits.base.Aggregator;
import soot.jimple.toolkits.base.RenameDuplicatedClasses;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraphPack;
import soot.jimple.toolkits.callgraph.UnreachableMethodTransformer;
import soot.jimple.toolkits.invoke.StaticInliner;
import soot.jimple.toolkits.invoke.StaticMethodBinder;
import soot.jimple.toolkits.pointer.CastCheckEliminatorDumper;
import soot.jimple.toolkits.pointer.DependenceTagAggregator;
import soot.jimple.toolkits.pointer.ParameterAliasTagger;
import soot.jimple.toolkits.pointer.SideEffectTagger;
import soot.jimple.toolkits.reflection.ConstantInvokeMethodBaseTransformer;
import soot.jimple.toolkits.scalar.CommonSubexpressionEliminator;
import soot.jimple.toolkits.scalar.ConditionalBranchFolder;
import soot.jimple.toolkits.scalar.ConstantPropagatorAndFolder;
import soot.jimple.toolkits.scalar.CopyPropagator;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.jimple.toolkits.scalar.EmptySwitchEliminator;
import soot.jimple.toolkits.scalar.LocalNameStandardizer;
import soot.jimple.toolkits.scalar.NopEliminator;
import soot.jimple.toolkits.scalar.UnconditionalBranchFolder;
import soot.jimple.toolkits.scalar.UnreachableCodeEliminator;
import soot.jimple.toolkits.scalar.pre.BusyCodeMotion;
import soot.jimple.toolkits.scalar.pre.LazyCodeMotion;
import soot.jimple.toolkits.thread.mhp.MhpTransformer;
import soot.jimple.toolkits.thread.synchronization.LockAllocator;
import soot.jimple.toolkits.typing.TypeAssigner;
import soot.options.Options;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.shimple.ShimpleTransformer;
import soot.shimple.toolkits.scalar.SConstantPropagatorAndFolder;
import soot.sootify.TemplatePrinter;
import soot.tagkit.InnerClassTagAggregator;
import soot.tagkit.LineNumberTagAggregator;
import soot.toDex.DexPrinter;
import soot.toolkits.exceptions.DuplicateCatchAllTrapRemover;
import soot.toolkits.exceptions.TrapTightener;
import soot.toolkits.graph.interaction.InteractionHandler;
import soot.toolkits.scalar.ConstantInitializerToTagTransformer;
import soot.toolkits.scalar.ConstantValueToInitializerTransformer;
import soot.toolkits.scalar.LocalPacker;
import soot.toolkits.scalar.LocalSplitter;
import soot.toolkits.scalar.SharedInitializationLocalSplitter;
import soot.toolkits.scalar.UnusedLocalEliminator;
import soot.util.EscapedWriter;
import soot.util.JasminOutputStream;
import soot.util.PhaseDumper;
import soot.xml.TagCollector;
import soot.xml.XMLPrinter;
/* loaded from: gencallgraphv3.jar:soot/PackManager.class */
public class PackManager {
    private static final Logger logger = LoggerFactory.getLogger(PackManager.class);
    public static boolean DEBUG = false;
    private final Map<String, Pack> packNameToPack = new HashMap();
    private final List<Pack> packList = new LinkedList();
    private boolean onlyStandardPacks = false;
    private JarOutputStream jarFile = null;
    protected DexPrinter dexPrinter = null;

    public PackManager(Singletons.Global g) {
        PhaseOptions.v().setPackManager(this);
        init();
    }

    public static PackManager v() {
        return G.v().soot_PackManager();
    }

    public boolean onlyStandardPacks() {
        return this.onlyStandardPacks;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyAddPack() {
        this.onlyStandardPacks = false;
    }

    private void init() {
        Pack p = new JimpleBodyPack();
        addPack(p);
        p.add(new Transform("jb.tt", TrapTightener.v()));
        p.add(new Transform("jb.dtr", DuplicateCatchAllTrapRemover.v()));
        p.add(new Transform("jb.ese", EmptySwitchEliminator.v()));
        p.add(new Transform("jb.ls", LocalSplitter.v()));
        p.add(new Transform("jb.sils", SharedInitializationLocalSplitter.v()));
        p.add(new Transform("jb.a", Aggregator.v()));
        p.add(new Transform("jb.ule", UnusedLocalEliminator.v()));
        p.add(new Transform("jb.tr", TypeAssigner.v()));
        p.add(new Transform("jb.ulp", LocalPacker.v()));
        p.add(new Transform("jb.lns", LocalNameStandardizer.v()));
        p.add(new Transform("jb.cp", CopyPropagator.v()));
        p.add(new Transform("jb.dae", DeadAssignmentEliminator.v()));
        p.add(new Transform("jb.cp-ule", UnusedLocalEliminator.v()));
        p.add(new Transform("jb.lp", LocalPacker.v()));
        p.add(new Transform("jb.ne", NopEliminator.v()));
        p.add(new Transform("jb.uce", UnreachableCodeEliminator.v()));
        p.add(new Transform("jb.cbf", ConditionalBranchFolder.v()));
        Pack p2 = new JavaToJimpleBodyPack();
        addPack(p2);
        p2.add(new Transform("jj.ls", LocalSplitter.v()));
        p2.add(new Transform("jj.sils", SharedInitializationLocalSplitter.v()));
        p2.add(new Transform("jj.a", Aggregator.v()));
        p2.add(new Transform("jj.ule", UnusedLocalEliminator.v()));
        p2.add(new Transform("jj.ne", NopEliminator.v()));
        p2.add(new Transform("jj.tr", TypeAssigner.v()));
        p2.add(new Transform("jj.ulp", LocalPacker.v()));
        p2.add(new Transform("jj.lns", LocalNameStandardizer.v()));
        p2.add(new Transform("jj.cp", CopyPropagator.v()));
        p2.add(new Transform("jj.dae", DeadAssignmentEliminator.v()));
        p2.add(new Transform("jj.cp-ule", UnusedLocalEliminator.v()));
        p2.add(new Transform("jj.lp", LocalPacker.v()));
        p2.add(new Transform("jj.uce", UnreachableCodeEliminator.v()));
        Pack p3 = new ScenePack("wjpp");
        addPack(p3);
        p3.add(new Transform("wjpp.cimbt", ConstantInvokeMethodBaseTransformer.v()));
        addPack(new ScenePack("wspp"));
        Pack p4 = new CallGraphPack("cg");
        addPack(p4);
        p4.add(new Transform("cg.cha", CHATransformer.v()));
        p4.add(new Transform("cg.spark", SparkTransformer.v()));
        p4.add(new Transform("cg.paddle", PaddleHook.v()));
        addPack(new ScenePack("wstp"));
        addPack(new ScenePack("wsop"));
        Pack p5 = new ScenePack("wjtp");
        addPack(p5);
        p5.add(new Transform("wjtp.mhp", MhpTransformer.v()));
        p5.add(new Transform("wjtp.tn", LockAllocator.v()));
        p5.add(new Transform("wjtp.rdc", RenameDuplicatedClasses.v()));
        Pack p6 = new ScenePack("wjop");
        addPack(p6);
        p6.add(new Transform("wjop.smb", StaticMethodBinder.v()));
        p6.add(new Transform("wjop.si", StaticInliner.v()));
        Pack p7 = new ScenePack("wjap");
        addPack(p7);
        p7.add(new Transform("wjap.ra", RectangularArrayFinder.v()));
        p7.add(new Transform("wjap.umt", UnreachableMethodsTagger.v()));
        p7.add(new Transform("wjap.uft", UnreachableFieldsTagger.v()));
        p7.add(new Transform("wjap.tqt", TightestQualifiersTagger.v()));
        p7.add(new Transform("wjap.cgg", CallGraphGrapher.v()));
        p7.add(new Transform("wjap.purity", PurityAnalysis.v()));
        addPack(new BodyPack(Shimple.PHASE));
        addPack(new BodyPack("stp"));
        Pack p8 = new BodyPack("sop");
        addPack(p8);
        p8.add(new Transform("sop.cpf", SConstantPropagatorAndFolder.v()));
        addPack(new BodyPack("jtp"));
        Pack p9 = new BodyPack("jop");
        addPack(p9);
        p9.add(new Transform("jop.cse", CommonSubexpressionEliminator.v()));
        p9.add(new Transform("jop.bcm", BusyCodeMotion.v()));
        p9.add(new Transform("jop.lcm", LazyCodeMotion.v()));
        p9.add(new Transform("jop.cp", CopyPropagator.v()));
        p9.add(new Transform("jop.cpf", ConstantPropagatorAndFolder.v()));
        p9.add(new Transform("jop.cbf", ConditionalBranchFolder.v()));
        p9.add(new Transform("jop.dae", DeadAssignmentEliminator.v()));
        p9.add(new Transform("jop.nce", new NullCheckEliminator()));
        p9.add(new Transform("jop.uce1", UnreachableCodeEliminator.v()));
        p9.add(new Transform("jop.ubf1", UnconditionalBranchFolder.v()));
        p9.add(new Transform("jop.uce2", UnreachableCodeEliminator.v()));
        p9.add(new Transform("jop.ubf2", UnconditionalBranchFolder.v()));
        p9.add(new Transform("jop.ule", UnusedLocalEliminator.v()));
        Pack p10 = new BodyPack("jap");
        addPack(p10);
        p10.add(new Transform("jap.npc", NullPointerChecker.v()));
        p10.add(new Transform("jap.npcolorer", NullPointerColorer.v()));
        p10.add(new Transform("jap.abc", ArrayBoundsChecker.v()));
        p10.add(new Transform("jap.profiling", ProfilingGenerator.v()));
        p10.add(new Transform("jap.sea", SideEffectTagger.v()));
        p10.add(new Transform("jap.fieldrw", FieldTagger.v()));
        p10.add(new Transform("jap.cgtagger", CallGraphTagger.v()));
        p10.add(new Transform("jap.parity", ParityTagger.v()));
        p10.add(new Transform("jap.pat", ParameterAliasTagger.v()));
        p10.add(new Transform("jap.rdtagger", ReachingDefsTagger.v()));
        p10.add(new Transform("jap.lvtagger", LiveVarsTagger.v()));
        p10.add(new Transform("jap.che", CastCheckEliminatorDumper.v()));
        p10.add(new Transform("jap.umt", new UnreachableMethodTransformer()));
        p10.add(new Transform("jap.lit", LoopInvariantFinder.v()));
        p10.add(new Transform("jap.aet", AvailExprTagger.v()));
        p10.add(new Transform("jap.dmt", DominatorsTagger.v()));
        Pack p11 = new BodyPack("gb");
        addPack(p11);
        p11.add(new Transform("gb.a1", Aggregator.v()));
        p11.add(new Transform("gb.cf", ConstructorFolder.v()));
        p11.add(new Transform("gb.a2", Aggregator.v()));
        p11.add(new Transform("gb.ule", UnusedLocalEliminator.v()));
        addPack(new BodyPack("gop"));
        Pack p12 = new BodyPack("bb");
        addPack(p12);
        p12.add(new Transform("bb.lso", LoadStoreOptimizer.v()));
        p12.add(new Transform("bb.pho", PeepholeOptimizer.v()));
        p12.add(new Transform("bb.ule", UnusedLocalEliminator.v()));
        p12.add(new Transform("bb.lp", LocalPacker.v()));
        p12.add(new Transform("bb.sco", StoreChainOptimizer.v()));
        p12.add(new Transform("bb.ne", NopEliminator.v()));
        addPack(new BodyPack("bop"));
        Pack p13 = new BodyPack(DropBoxManager.EXTRA_TAG);
        addPack(p13);
        p13.add(new Transform("tag.ln", LineNumberTagAggregator.v()));
        p13.add(new Transform("tag.an", ArrayNullTagAggregator.v()));
        p13.add(new Transform("tag.dep", DependenceTagAggregator.v()));
        p13.add(new Transform("tag.fieldrw", FieldTagAggregator.v()));
        Pack p14 = new BodyPack("db");
        addPack(p14);
        p14.add(new Transform("db.transformations", null));
        p14.add(new Transform("db.renamer", null));
        p14.add(new Transform("db.deobfuscate", null));
        p14.add(new Transform("db.force-recompile", null));
        this.onlyStandardPacks = true;
    }

    private void addPack(Pack p) {
        if (this.packNameToPack.containsKey(p.getPhaseName())) {
            throw new RuntimeException("Duplicate pack " + p.getPhaseName());
        }
        this.packNameToPack.put(p.getPhaseName(), p);
        this.packList.add(p);
    }

    public boolean hasPack(String phaseName) {
        return getPhase(phaseName) != null;
    }

    public Pack getPack(String phaseName) {
        Pack p = this.packNameToPack.get(phaseName);
        return p;
    }

    public boolean hasPhase(String phaseName) {
        return getPhase(phaseName) != null;
    }

    public HasPhaseOptions getPhase(String phaseName) {
        int index = phaseName.indexOf(46);
        if (index < 0) {
            return getPack(phaseName);
        }
        String packName = phaseName.substring(0, index);
        if (hasPack(packName)) {
            return getPack(packName).get(phaseName);
        }
        return null;
    }

    public Transform getTransform(String phaseName) {
        return (Transform) getPhase(phaseName);
    }

    public Collection<Pack> allPacks() {
        return Collections.unmodifiableList(this.packList);
    }

    public void runPacks() {
        if (Options.v().oaat()) {
            runPacksForOneClassAtATime();
        } else {
            runPacksNormally();
        }
    }

    private void runPacksForOneClassAtATime() {
        if (Options.v().src_prec() == 1 && Options.v().keep_line_number()) {
            LineNumberAdder.v().internalTransform("", null);
        }
        setupJAR();
        boolean validate = Options.v().validate();
        SourceLocator srcLoc = SourceLocator.v();
        Scene scene = Scene.v();
        for (String path : Options.v().process_dir()) {
            for (String cl : srcLoc.getClassesUnder(path)) {
                scene.forceResolve(cl, 2).setApplicationClass();
            }
            for (String cl2 : srcLoc.getClassesUnder(path)) {
                ClassSource source = srcLoc.getClassSource(cl2);
                if (source == null) {
                    throw new RuntimeException("Could not locate class source");
                }
                try {
                    SootClass clazz = scene.getSootClass(cl2);
                    clazz.setResolvingLevel(3);
                    source.resolve(clazz);
                    source.close();
                    for (SootClass sc : scene.getApplicationClasses()) {
                        if (validate) {
                            sc.validate();
                        }
                        if (!sc.isPhantom) {
                            ConstantInitializerToTagTransformer.v().transformClass(sc, true);
                        }
                    }
                    runBodyPacks(clazz);
                    writeClass(clazz);
                    if (!Options.v().no_writeout_body_releasing()) {
                        releaseBodies(clazz);
                    }
                } catch (Throwable th) {
                    source.close();
                    throw th;
                }
            }
        }
        tearDownJAR();
        handleInnerClasses();
    }

    private void runPacksNormally() {
        if (Options.v().src_prec() == 1 && Options.v().keep_line_number()) {
            LineNumberAdder.v().internalTransform("", null);
        }
        if (Options.v().whole_program() || Options.v().whole_shimple()) {
            runWholeProgramPacks();
        }
        retrieveAllBodies();
        boolean validate = Options.v().validate();
        for (SootClass sc : Scene.v().getApplicationClasses()) {
            if (validate) {
                sc.validate();
            }
            if (!sc.isPhantom) {
                ConstantInitializerToTagTransformer.v().transformClass(sc, true);
            }
        }
        if (soot.jbco.Main.metrics) {
            coffiMetrics();
            System.exit(0);
        }
        preProcessDAVA();
        if (Options.v().interactive_mode()) {
            if (InteractionHandler.v().getInteractionListener() == null) {
                logger.debug("Cannot run in interactive mode. No listeners available. Continuing in regular mode.");
                Options.v().set_interactive_mode(false);
            } else {
                logger.debug("Running in interactive mode.");
            }
        }
        runBodyPacks();
        handleInnerClasses();
    }

    public void coffiMetrics() {
        int tV = 0;
        int tE = 0;
        int hM = 0;
        double aM = 0.0d;
        HashMap<SootMethod, int[]> hashVem = CFG.methodsToVEM;
        for (int[] vem : hashVem.values()) {
            tV += vem[0];
            tE += vem[1];
            aM += vem[2];
            if (vem[2] > hM) {
                hM = vem[2];
            }
        }
        if (hashVem.size() > 0) {
            aM /= hashVem.size();
        }
        logger.debug("Vertices, Edges, Avg Degree, Highest Deg:    " + tV + "  " + tE + "  " + aM + "  " + hM);
    }

    public void runBodyPacks() {
        runBodyPacks(reachableClasses());
    }

    public JarOutputStream getJarFile() {
        return this.jarFile;
    }

    public void writeOutput() {
        setupJAR();
        if (Options.v().verbose()) {
            PhaseDumper.v().dumpBefore(MediaStore.EXTRA_OUTPUT);
        }
        switch (Options.v().output_format()) {
            case 10:
            case 11:
                writeDexOutput();
                break;
            case 12:
            case 13:
            case 14:
            default:
                writeOutput(reachableClasses());
                tearDownJAR();
                break;
            case 15:
                postProcessDAVA();
                outputDava();
                break;
        }
        postProcessXML(reachableClasses());
        if (!Options.v().no_writeout_body_releasing()) {
            releaseBodies(reachableClasses());
        }
        if (Options.v().verbose()) {
            PhaseDumper.v().dumpAfter(MediaStore.EXTRA_OUTPUT);
        }
    }

    protected void writeDexOutput() {
        this.dexPrinter = new DexPrinter();
        writeOutput(reachableClasses());
        this.dexPrinter.print();
        this.dexPrinter = null;
    }

    private void setupJAR() {
        if (Options.v().output_jar()) {
            String outFileName = SourceLocator.v().getOutputJarName();
            try {
                this.jarFile = new JarOutputStream(new FileOutputStream(outFileName));
                return;
            } catch (IOException e) {
                throw new CompilationDeathException("Cannot open output Jar file " + outFileName);
            }
        }
        this.jarFile = null;
    }

    private void runWholeProgramPacks() {
        if (Options.v().whole_shimple()) {
            ShimpleTransformer.v().transform();
            getPack("wspp").apply();
            getPack("cg").apply();
            getPack("wstp").apply();
            getPack("wsop").apply();
        } else {
            getPack("wjpp").apply();
            getPack("cg").apply();
            getPack("wjtp").apply();
            getPack("wjop").apply();
            getPack("wjap").apply();
        }
        PaddleHook.v().finishPhases();
    }

    private void preProcessDAVA() {
        if (Options.v().output_format() == 15) {
            if (!PhaseOptions.getBoolean(PhaseOptions.v().getPhaseOptions("db"), "source-is-javac")) {
                if (DEBUG) {
                    System.out.println("Source is not Javac hence invoking ThrowFinder");
                }
                ThrowFinder.v().find();
            } else if (DEBUG) {
                System.out.println("Source is javac hence we dont need to invoke ThrowFinder");
            }
            PackageNamer.v().fixNames();
        }
    }

    private void runBodyPacks(Iterator<SootClass> classes) {
        int threadNum = Options.v().num_threads();
        if (threadNum < 1) {
            threadNum = Runtime.getRuntime().availableProcessors();
        }
        CountingThreadPoolExecutor executor = new CountingThreadPoolExecutor(threadNum, threadNum, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        while (classes.hasNext()) {
            SootClass c = classes.next();
            executor.execute(()
            /*  JADX ERROR: Method code generation error
                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0041: INVOKE  
                  (r0v3 'executor' heros.solver.CountingThreadPoolExecutor A[D('executor' heros.solver.CountingThreadPoolExecutor)])
                  (wrap: java.lang.Runnable : 0x003c: INVOKE_CUSTOM (r1v5 java.lang.Runnable A[REMOVE]) = 
                  (r10v0 'this' soot.PackManager A[D('this' soot.PackManager), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                  (r0v19 'c' soot.SootClass A[D('c' soot.SootClass), DONT_INLINE])
                
                 handle type: INVOKE_DIRECT
                 lambda: java.lang.Runnable.run():void
                 call insn: ?: INVOKE  (r1 I:soot.PackManager), (r2 I:soot.SootClass) type: DIRECT call: soot.PackManager.lambda$0(soot.SootClass):void)
                 type: VIRTUAL call: heros.solver.CountingThreadPoolExecutor.execute(java.lang.Runnable):void in method: soot.PackManager.runBodyPacks(java.util.Iterator<soot.SootClass>):void, file: gencallgraphv3.jar:soot/PackManager.class
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:289)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:252)
                	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:226)
                	at jadx.core.dex.regions.loops.LoopRegion.generate(LoopRegion.java:171)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:296)
                	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:275)
                	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:377)
                	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:306)
                	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:272)
                	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
                	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                Caused by: java.lang.IndexOutOfBoundsException: Index 1 out of bounds for length 1
                	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:100)
                	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:106)
                	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:302)
                	at java.base/java.util.Objects.checkIndex(Objects.java:385)
                	at java.base/java.util.ArrayList.get(ArrayList.java:427)
                	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:998)
                	at jadx.core.codegen.InsnGen.makeInvokeLambda(InsnGen.java:903)
                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:794)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:401)
                	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:143)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:119)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:106)
                	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:1075)
                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:851)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:401)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:282)
                	... 21 more
                */
            /*
                this = this;
                soot.options.Options r0 = soot.options.Options.v()
                int r0 = r0.num_threads()
                r12 = r0
                r0 = r12
                r1 = 1
                if (r0 >= r1) goto L13
                java.lang.Runtime r0 = java.lang.Runtime.getRuntime()
                int r0 = r0.availableProcessors()
                r12 = r0
            L13:
                heros.solver.CountingThreadPoolExecutor r0 = new heros.solver.CountingThreadPoolExecutor
                r1 = r0
                r2 = r12
                r3 = r12
                r4 = 30
                java.util.concurrent.TimeUnit r5 = java.util.concurrent.TimeUnit.SECONDS
                java.util.concurrent.LinkedBlockingQueue r6 = new java.util.concurrent.LinkedBlockingQueue
                r7 = r6
                r7.<init>()
                r1.<init>(r2, r3, r4, r5, r6)
                r13 = r0
                goto L44
            L2d:
                r0 = r11
                java.lang.Object r0 = r0.next()
                soot.SootClass r0 = (soot.SootClass) r0
                r14 = r0
                r0 = r13
                r1 = r10
                r2 = r14
                void r1 = () -> { // java.lang.Runnable.run():void
                    r1.lambda$0(r2);
                }
                r0.execute(r1)
            L44:
                r0 = r11
                boolean r0 = r0.hasNext()
                if (r0 != 0) goto L2d
                r0 = r13
                r0.awaitCompletion()     // Catch: java.lang.InterruptedException -> L58
                r0 = r13
                r0.shutdown()     // Catch: java.lang.InterruptedException -> L58
                goto L79
            L58:
                r14 = move-exception
                java.lang.RuntimeException r0 = new java.lang.RuntimeException
                r1 = r0
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r3 = r2
                java.lang.String r4 = "Could not wait for pack threads to finish: "
                r3.<init>(r4)
                r3 = r14
                java.lang.String r3 = r3.getMessage()
                java.lang.StringBuilder r2 = r2.append(r3)
                java.lang.String r2 = r2.toString()
                r3 = r14
                r1.<init>(r2, r3)
                throw r0
            L79:
                r0 = r13
                java.lang.Throwable r0 = r0.getException()
                r14 = r0
                r0 = r14
                if (r0 == 0) goto L9c
                r0 = r14
                boolean r0 = r0 instanceof java.lang.RuntimeException
                if (r0 == 0) goto L92
                r0 = r14
                java.lang.RuntimeException r0 = (java.lang.RuntimeException) r0
                throw r0
            L92:
                java.lang.RuntimeException r0 = new java.lang.RuntimeException
                r1 = r0
                r2 = r14
                r1.<init>(r2)
                throw r0
            L9c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: soot.PackManager.runBodyPacks(java.util.Iterator):void");
        }

        private void handleInnerClasses() {
            InnerClassTagAggregator.v().internalTransform("", null);
        }

        protected void writeOutput(Iterator<SootClass> classes) {
            int i;
            if (Options.v().output_format() == 14 && this.jarFile == null) {
                i = Runtime.getRuntime().availableProcessors();
            } else {
                i = 1;
            }
            int threadNum = i;
            CountingThreadPoolExecutor executor = new CountingThreadPoolExecutor(threadNum, threadNum, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue());
            while (classes.hasNext()) {
                SootClass c = classes.next();
                executor.execute(()
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x004b: INVOKE  
                      (r0v4 'executor' heros.solver.CountingThreadPoolExecutor A[D('executor' heros.solver.CountingThreadPoolExecutor)])
                      (wrap: java.lang.Runnable : 0x0046: INVOKE_CUSTOM (r1v5 java.lang.Runnable A[REMOVE]) = 
                      (r10v0 'this' soot.PackManager A[D('this' soot.PackManager), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                      (r0v20 'c' soot.SootClass A[D('c' soot.SootClass), DONT_INLINE])
                    
                     handle type: INVOKE_DIRECT
                     lambda: java.lang.Runnable.run():void
                     call insn: ?: INVOKE  (r1 I:soot.PackManager), (r2 I:soot.SootClass) type: DIRECT call: soot.PackManager.lambda$1(soot.SootClass):void)
                     type: VIRTUAL call: heros.solver.CountingThreadPoolExecutor.execute(java.lang.Runnable):void in method: soot.PackManager.writeOutput(java.util.Iterator<soot.SootClass>):void, file: gencallgraphv3.jar:soot/PackManager.class
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:289)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:252)
                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                    	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:226)
                    	at jadx.core.dex.regions.loops.LoopRegion.generate(LoopRegion.java:171)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:296)
                    	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:275)
                    	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:377)
                    	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:306)
                    	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:272)
                    	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
                    	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                    	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                    Caused by: java.lang.IndexOutOfBoundsException: Index 1 out of bounds for length 1
                    	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:100)
                    	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:106)
                    	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:302)
                    	at java.base/java.util.Objects.checkIndex(Objects.java:385)
                    	at java.base/java.util.ArrayList.get(ArrayList.java:427)
                    	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:998)
                    	at jadx.core.codegen.InsnGen.makeInvokeLambda(InsnGen.java:903)
                    	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:794)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:401)
                    	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:143)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:119)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:106)
                    	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:1075)
                    	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:851)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:401)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:282)
                    	... 21 more
                    */
                /*
                    this = this;
                    soot.options.Options r0 = soot.options.Options.v()
                    int r0 = r0.output_format()
                    r1 = 14
                    if (r0 != r1) goto L1b
                    r0 = r10
                    java.util.jar.JarOutputStream r0 = r0.jarFile
                    if (r0 != 0) goto L1b
                    java.lang.Runtime r0 = java.lang.Runtime.getRuntime()
                    int r0 = r0.availableProcessors()
                    goto L1c
                L1b:
                    r0 = 1
                L1c:
                    r12 = r0
                    heros.solver.CountingThreadPoolExecutor r0 = new heros.solver.CountingThreadPoolExecutor
                    r1 = r0
                    r2 = r12
                    r3 = r12
                    r4 = 30
                    java.util.concurrent.TimeUnit r5 = java.util.concurrent.TimeUnit.SECONDS
                    java.util.concurrent.LinkedBlockingQueue r6 = new java.util.concurrent.LinkedBlockingQueue
                    r7 = r6
                    r7.<init>()
                    r1.<init>(r2, r3, r4, r5, r6)
                    r13 = r0
                    goto L4e
                L37:
                    r0 = r11
                    java.lang.Object r0 = r0.next()
                    soot.SootClass r0 = (soot.SootClass) r0
                    r14 = r0
                    r0 = r13
                    r1 = r10
                    r2 = r14
                    void r1 = () -> { // java.lang.Runnable.run():void
                        r1.lambda$1(r2);
                    }
                    r0.execute(r1)
                L4e:
                    r0 = r11
                    boolean r0 = r0.hasNext()
                    if (r0 != 0) goto L37
                    r0 = r13
                    r0.awaitCompletion()     // Catch: java.lang.InterruptedException -> L62
                    r0 = r13
                    r0.shutdown()     // Catch: java.lang.InterruptedException -> L62
                    goto L83
                L62:
                    r14 = move-exception
                    java.lang.RuntimeException r0 = new java.lang.RuntimeException
                    r1 = r0
                    java.lang.StringBuilder r2 = new java.lang.StringBuilder
                    r3 = r2
                    java.lang.String r4 = "Could not wait for writer threads to finish: "
                    r3.<init>(r4)
                    r3 = r14
                    java.lang.String r3 = r3.getMessage()
                    java.lang.StringBuilder r2 = r2.append(r3)
                    java.lang.String r2 = r2.toString()
                    r3 = r14
                    r1.<init>(r2, r3)
                    throw r0
                L83:
                    r0 = r13
                    java.lang.Throwable r0 = r0.getException()
                    r14 = r0
                    r0 = r14
                    if (r0 == 0) goto La6
                    r0 = r14
                    boolean r0 = r0 instanceof java.lang.RuntimeException
                    if (r0 == 0) goto L9c
                    r0 = r14
                    java.lang.RuntimeException r0 = (java.lang.RuntimeException) r0
                    throw r0
                L9c:
                    java.lang.RuntimeException r0 = new java.lang.RuntimeException
                    r1 = r0
                    r2 = r14
                    r1.<init>(r2)
                    throw r0
                La6:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: soot.PackManager.writeOutput(java.util.Iterator):void");
            }

            private void tearDownJAR() {
                try {
                    if (this.jarFile != null) {
                        this.jarFile.close();
                    }
                } catch (IOException e) {
                    throw new CompilationDeathException("Error closing output jar: " + e);
                }
            }

            private void releaseBodies(Iterator<SootClass> classes) {
                while (classes.hasNext()) {
                    releaseBodies(classes.next());
                }
            }

            private Iterator<SootClass> reachableClasses() {
                return Scene.v().getApplicationClasses().snapshotIterator();
            }

            private void postProcessDAVA() {
                boolean transformations = PhaseOptions.getBoolean(PhaseOptions.v().getPhaseOptions("db.transformations"), "enabled");
                for (SootClass s : Scene.v().getApplicationClasses()) {
                    DavaStaticBlockCleaner.v().staticBlockInlining(s);
                    VoidReturnRemover.cleanClass(s);
                    RemoveEmptyBodyDefaultConstructor.checkAndRemoveDefault(s);
                    logger.debug("Analyzing " + SourceLocator.v().getFileNameFor(s, Options.v().output_format()) + "... ");
                    for (SootMethod m : s.getMethods()) {
                        if (m.hasActiveBody()) {
                            DavaBody body = (DavaBody) m.getActiveBody();
                            if (transformations) {
                                body.analyzeAST();
                            } else {
                                body.applyBugFixes();
                            }
                        }
                    }
                }
                if (transformations) {
                    InterProceduralAnalyses.applyInterProceduralAnalyses();
                }
            }

            private void outputDava() {
                OutputStream streamOut;
                String pathForBuild = null;
                ArrayList<String> decompiledClasses = new ArrayList<>();
                for (SootClass s : Scene.v().getApplicationClasses()) {
                    String fileName = SourceLocator.v().getFileNameFor(s, Options.v().output_format());
                    decompiledClasses.add(fileName.substring(fileName.lastIndexOf(47) + 1));
                    if (pathForBuild == null) {
                        pathForBuild = fileName.substring(0, fileName.lastIndexOf(47) + 1);
                    }
                    if (Options.v().gzip()) {
                        fileName = String.valueOf(fileName) + ".gz";
                    }
                    try {
                        if (this.jarFile != null) {
                            this.jarFile.putNextEntry(new JarEntry(fileName.replace('\\', '/')));
                            streamOut = this.jarFile;
                        } else {
                            streamOut = new FileOutputStream(fileName);
                        }
                        if (Options.v().gzip()) {
                            streamOut = new GZIPOutputStream(streamOut);
                        }
                        PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));
                        logger.debug("Generating " + fileName + "... ");
                        DavaPrinter.v().printTo(s, writerOut);
                        try {
                            writerOut.flush();
                            if (this.jarFile == null) {
                                writerOut.close();
                            } else {
                                this.jarFile.closeEntry();
                            }
                        } catch (IOException e) {
                            throw new CompilationDeathException("Cannot close output file " + fileName);
                        }
                    } catch (IOException e2) {
                        throw new CompilationDeathException("Cannot output file " + fileName, e2);
                    }
                }
                if (pathForBuild != null) {
                    if (pathForBuild.endsWith("src/")) {
                        pathForBuild = pathForBuild.substring(0, pathForBuild.length() - 4);
                    }
                    String fileName2 = String.valueOf(pathForBuild) + org.apache.tools.ant.Main.DEFAULT_BUILD_FILENAME;
                    try {
                        OutputStream streamOut2 = new FileOutputStream(fileName2);
                        try {
                            PrintWriter writerOut2 = new PrintWriter(new OutputStreamWriter(streamOut2));
                            DavaBuildFile.generate(writerOut2, decompiledClasses);
                            writerOut2.flush();
                            if (streamOut2 != null) {
                                streamOut2.close();
                            }
                        } catch (Throwable th) {
                            if (streamOut2 != null) {
                                streamOut2.close();
                            }
                            throw th;
                        }
                    } catch (IOException e3) {
                        throw new CompilationDeathException("Cannot open output file " + fileName2, e3);
                    }
                }
            }

            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            private void runBodyPacks(SootClass c) {
                ShimpleBody sBody;
                int format = Options.v().output_format();
                if (format == 15) {
                    logger.debug("Decompiling {}...", c.getName());
                    G.v().SootMethodAddedByDava = false;
                } else {
                    logger.debug("Transforming {}...", c.getName());
                }
                boolean produceBaf = false;
                boolean produceGrimp = false;
                boolean produceDava = false;
                boolean produceJimple = true;
                boolean produceShimple = false;
                switch (format) {
                    case 1:
                    case 2:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 16:
                        break;
                    case 3:
                    case 4:
                        produceShimple = true;
                        produceJimple = false;
                        break;
                    case 5:
                    case 6:
                        produceBaf = true;
                        break;
                    case 7:
                    case 8:
                        produceGrimp = true;
                        break;
                    case 13:
                    case 14:
                    case 17:
                        produceGrimp = Options.v().via_grimp();
                        produceBaf = !produceGrimp;
                        break;
                    case 15:
                        produceDava = true;
                        produceGrimp = true;
                        break;
                    default:
                        throw new RuntimeException();
                }
                TagCollector tc = (format == 1 || !Options.v().xml_attributes()) ? null : new TagCollector();
                boolean wholeShimple = Options.v().whole_shimple();
                if (Options.v().via_shimple()) {
                    produceShimple = true;
                }
                Iterator it = new ArrayList(c.getMethods()).iterator();
                while (it.hasNext()) {
                    SootMethod m = (SootMethod) it.next();
                    if (DEBUG && !m.getExceptions().isEmpty()) {
                        System.out.println("PackManager printing out jimple body exceptions for method " + m.toString() + Instruction.argsep + m.getExceptions().toString());
                    }
                    if (m.isConcrete()) {
                        if (produceShimple || wholeShimple) {
                            Body body = m.retrieveActiveBody();
                            if (m.hasActiveBody()) {
                                if (body instanceof ShimpleBody) {
                                    sBody = (ShimpleBody) body;
                                    if (!sBody.isSSA()) {
                                        sBody.rebuild();
                                    }
                                } else {
                                    sBody = Shimple.v().newBody(body);
                                }
                                m.setActiveBody(sBody);
                                getPack("stp").apply(sBody);
                                getPack("sop").apply(sBody);
                                if (produceJimple || (wholeShimple && !produceShimple)) {
                                    m.setActiveBody(sBody.toJimpleBody());
                                }
                            }
                        }
                        if (produceJimple) {
                            Body body2 = m.retrieveActiveBody();
                            getTransform("jb.cp").apply(body2);
                            getTransform("jb.cbf").apply(body2);
                            getTransform("jb.uce").apply(body2);
                            getTransform("jb.dae").apply(body2);
                            getTransform("jb.cp-ule").apply(body2);
                            getPack("jtp").apply(body2);
                            if (Options.v().validate()) {
                                body2.validate();
                            }
                            getPack("jop").apply(body2);
                            getPack("jap").apply(body2);
                            if (tc != null) {
                                tc.collectBodyTags(body2);
                            }
                        }
                        if (m.hasActiveBody()) {
                            if (produceGrimp) {
                                GrimpBody newBody = Grimp.v().newBody(m.getActiveBody(), "gb");
                                m.setActiveBody(newBody);
                                getPack("gop").apply(newBody);
                            } else if (produceBaf) {
                                m.setActiveBody(convertJimpleBodyToBaf(m));
                            }
                        }
                    }
                }
                if (tc != null) {
                    processXMLForClass(c, tc);
                }
                if (produceDava) {
                    for (SootMethod m2 : c.getMethods()) {
                        if (m2.isConcrete() && m2.hasActiveBody()) {
                            m2.setActiveBody(Dava.v().newBody(m2.getActiveBody()));
                        }
                    }
                    if (G.v().SootMethodAddedByDava) {
                        Iterator<SootMethod> it2 = G.v().SootMethodsAdded.iterator();
                        while (it2.hasNext()) {
                            c.addMethod(it2.next());
                        }
                        G.v().SootMethodsAdded = new ArrayList<>();
                        G.v().SootMethodAddedByDava = false;
                    }
                }
            }

            public BafBody convertJimpleBodyToBaf(SootMethod m) {
                JimpleBody body = (JimpleBody) m.getActiveBody().clone();
                BafBody bafBody = Baf.v().newBody(body);
                getPack("bop").apply(bafBody);
                getPack(DropBoxManager.EXTRA_TAG).apply(bafBody);
                if (Options.v().validate()) {
                    bafBody.validate();
                }
                return bafBody;
            }

            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            protected void writeClass(SootClass c) {
                OutputStream streamOut;
                int format = Options.v().output_format();
                switch (format) {
                    case 1:
                        if (!c.isPhantom) {
                            ConstantValueToInitializerTransformer.v().transformClass(c);
                            break;
                        }
                        break;
                    case 10:
                    case 11:
                        this.dexPrinter.add(c);
                        return;
                    case 12:
                    case 15:
                        return;
                }
                String fileName = SourceLocator.v().getFileNameFor(c, format);
                if (Options.v().gzip()) {
                    fileName = String.valueOf(fileName) + ".gz";
                }
                try {
                    if (this.jarFile != null) {
                        fileName = fileName.replace("\\", "/");
                        JarEntry entry = new JarEntry(fileName);
                        entry.setMethod(8);
                        this.jarFile.putNextEntry(entry);
                        streamOut = this.jarFile;
                    } else {
                        new File(fileName).getParentFile().mkdirs();
                        streamOut = new FileOutputStream(fileName);
                    }
                    if (Options.v().gzip()) {
                        streamOut = new GZIPOutputStream(streamOut);
                    }
                    if (format == 14 && Options.v().jasmin_backend()) {
                        streamOut = new JasminOutputStream(streamOut);
                    }
                    PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));
                    logger.debug("Writing to " + fileName);
                    if (Options.v().xml_attributes()) {
                        Printer.v().setOption(16);
                    }
                    switch (format) {
                        case 1:
                        case 3:
                        case 5:
                        case 7:
                            writerOut = new PrintWriter(new EscapedWriter(new OutputStreamWriter(streamOut)));
                            Printer.v().printTo(c, writerOut);
                            break;
                        case 2:
                        case 4:
                        case 6:
                        case 8:
                            Printer.v().setOption(1);
                            Printer.v().printTo(c, writerOut);
                            break;
                        case 9:
                            writerOut = new PrintWriter(new EscapedWriter(new OutputStreamWriter(streamOut)));
                            XMLPrinter.v().printJimpleStyleTo(c, writerOut);
                            break;
                        case 10:
                        case 11:
                        case 12:
                        case 15:
                        default:
                            throw new RuntimeException();
                        case 14:
                            if (!Options.v().jasmin_backend()) {
                                createASMBackend(c).generateClassFile(streamOut);
                                break;
                            }
                        case 13:
                            createJasminBackend(c).print(writerOut);
                            break;
                        case 16:
                            writerOut = new PrintWriter(new OutputStreamWriter(streamOut));
                            TemplatePrinter.v().printTo(c, writerOut);
                            break;
                        case 17:
                            createASMBackend(c).generateTextualRepresentation(writerOut);
                            break;
                    }
                    try {
                        writerOut.flush();
                        if (this.jarFile == null) {
                            streamOut.close();
                            writerOut.close();
                            return;
                        }
                        this.jarFile.closeEntry();
                    } catch (IOException e) {
                        throw new CompilationDeathException("Cannot close output file " + fileName);
                    }
                } catch (IOException e2) {
                    throw new CompilationDeathException("Cannot output file " + fileName, e2);
                }
            }

            private AbstractJasminClass createJasminBackend(SootClass c) {
                if (c.containsBafBody()) {
                    return new JasminClass(c);
                }
                return new soot.jimple.JasminClass(c);
            }

            protected BafASMBackend createASMBackend(SootClass c) {
                return new BafASMBackend(c, Options.v().java_version());
            }

            private void postProcessXML(Iterator<SootClass> classes) {
                if (!Options.v().xml_attributes() || Options.v().output_format() != 1) {
                    return;
                }
                while (classes.hasNext()) {
                    SootClass c = classes.next();
                    processXMLForClass(c);
                }
            }

            private void processXMLForClass(SootClass c, TagCollector tc) {
                int ofmt = Options.v().output_format();
                int format = ofmt != 12 ? ofmt : 1;
                String fileName = SourceLocator.v().getFileNameFor(c, format);
                XMLAttributesPrinter xap = new XMLAttributesPrinter(fileName, SourceLocator.v().getOutputDir());
                xap.printAttrs(c, tc);
            }

            private void processXMLForClass(SootClass c) {
                int format = Options.v().output_format();
                String fileName = SourceLocator.v().getFileNameFor(c, format);
                XMLAttributesPrinter xap = new XMLAttributesPrinter(fileName, SourceLocator.v().getOutputDir());
                xap.printAttrs(c);
            }

            private void releaseBodies(SootClass cl) {
                Iterator<SootMethod> methodIt = cl.methodIterator();
                while (methodIt.hasNext()) {
                    SootMethod m = methodIt.next();
                    if (m.hasActiveBody()) {
                        m.releaseActiveBody();
                    }
                }
            }

            private void retrieveAllBodies() {
                int threadNum = Options.v().coffi() ? 1 : Runtime.getRuntime().availableProcessors();
                CountingThreadPoolExecutor executor = new CountingThreadPoolExecutor(threadNum, threadNum, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue());
                Iterator<SootClass> clIt = reachableClasses();
                while (clIt.hasNext()) {
                    SootClass cl = clIt.next();
                    Iterator it = new ArrayList(cl.getMethods()).iterator();
                    while (it.hasNext()) {
                        SootMethod m = (SootMethod) it.next();
                        if (m.isConcrete()) {
                            executor.execute(() -> {
                                m.retrieveActiveBody();
                            });
                        }
                    }
                }
                try {
                    executor.awaitCompletion();
                    executor.shutdown();
                    Throwable exception = executor.getException();
                    if (exception != null) {
                        if (exception instanceof RuntimeException) {
                            throw ((RuntimeException) exception);
                        }
                        throw new RuntimeException(exception);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException("Could not wait for loader threads to finish: " + e.getMessage(), e);
                }
            }
        }
