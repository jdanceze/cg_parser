package soot.util.cfgcmd;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.toolkits.graph.ArrayRefBlockGraph;
import soot.toolkits.graph.BriefBlockGraph;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.ClassicCompleteBlockGraph;
import soot.toolkits.graph.ClassicCompleteUnitGraph;
import soot.toolkits.graph.CompleteBlockGraph;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalBlockGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.TrapUnitGraph;
import soot.toolkits.graph.ZonedBlockGraph;
import soot.util.cfgcmd.CFGOptionMatcher;
import soot.util.dot.DotGraph;
/* loaded from: gencallgraphv3.jar:soot/util/cfgcmd/CFGGraphType.class */
public abstract class CFGGraphType extends CFGOptionMatcher.CFGOption {
    private static final boolean DEBUG = true;
    private static final Logger logger = LoggerFactory.getLogger(CFGGraphType.class);
    public static final CFGGraphType BRIEF_UNIT_GRAPH = new CFGGraphType("BriefUnitGraph") { // from class: soot.util.cfgcmd.CFGGraphType.1
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return new BriefUnitGraph(b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG(g, b);
        }
    };
    public static final CFGGraphType EXCEPTIONAL_UNIT_GRAPH = new CFGGraphType("ExceptionalUnitGraph") { // from class: soot.util.cfgcmd.CFGGraphType.2
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG((ExceptionalUnitGraph) g);
        }
    };
    public static final CFGGraphType COMPLETE_UNIT_GRAPH = new CFGGraphType("CompleteUnitGraph") { // from class: soot.util.cfgcmd.CFGGraphType.3
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return new CompleteUnitGraph(b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG((CompleteUnitGraph) g);
        }
    };
    public static final CFGGraphType TRAP_UNIT_GRAPH = new CFGGraphType("TrapUnitGraph") { // from class: soot.util.cfgcmd.CFGGraphType.4
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return new TrapUnitGraph(b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG(g, b);
        }
    };
    public static final CFGGraphType CLASSIC_COMPLETE_UNIT_GRAPH = new CFGGraphType("ClassicCompleteUnitGraph") { // from class: soot.util.cfgcmd.CFGGraphType.5
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return new ClassicCompleteUnitGraph(b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG(g, b);
        }
    };
    public static final CFGGraphType BRIEF_BLOCK_GRAPH = new CFGGraphType("BriefBlockGraph") { // from class: soot.util.cfgcmd.CFGGraphType.6
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return new BriefBlockGraph(b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG(g, b);
        }
    };
    public static final CFGGraphType EXCEPTIONAL_BLOCK_GRAPH = new CFGGraphType("ExceptionalBlockGraph") { // from class: soot.util.cfgcmd.CFGGraphType.7
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return new ExceptionalBlockGraph(b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG((ExceptionalBlockGraph) g);
        }
    };
    public static final CFGGraphType COMPLETE_BLOCK_GRAPH = new CFGGraphType("CompleteBlockGraph") { // from class: soot.util.cfgcmd.CFGGraphType.8
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return new CompleteBlockGraph(b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG(g, b);
        }
    };
    public static final CFGGraphType CLASSIC_COMPLETE_BLOCK_GRAPH = new CFGGraphType("ClassicCompleteBlockGraph") { // from class: soot.util.cfgcmd.CFGGraphType.9
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return new ClassicCompleteBlockGraph(b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG(g, b);
        }
    };
    public static final CFGGraphType ARRAY_REF_BLOCK_GRAPH = new CFGGraphType("ArrayRefBlockGraph") { // from class: soot.util.cfgcmd.CFGGraphType.10
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return new ArrayRefBlockGraph(b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG(g, b);
        }
    };
    public static final CFGGraphType ZONED_BLOCK_GRAPH = new CFGGraphType("ZonedBlockGraph") { // from class: soot.util.cfgcmd.CFGGraphType.11
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return new ZonedBlockGraph(b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG(g, b);
        }
    };
    public static final CFGGraphType ALT_BRIEF_UNIT_GRAPH = new CFGGraphType("AltBriefUnitGraph") { // from class: soot.util.cfgcmd.CFGGraphType.12
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return CFGGraphType.loadAltGraph("soot.toolkits.graph.BriefUnitGraph", b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG(g, b);
        }
    };
    public static final CFGGraphType ALT_COMPLETE_UNIT_GRAPH = new CFGGraphType("AltCompleteUnitGraph") { // from class: soot.util.cfgcmd.CFGGraphType.13
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return CFGGraphType.loadAltGraph("soot.toolkits.graph.CompleteUnitGraph", b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG(g, b);
        }
    };
    public static final CFGGraphType ALT_TRAP_UNIT_GRAPH = new CFGGraphType("AltTrapUnitGraph") { // from class: soot.util.cfgcmd.CFGGraphType.14
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return CFGGraphType.loadAltGraph("soot.toolkits.graph.TrapUnitGraph", b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG(g, b);
        }
    };
    public static final CFGGraphType ALT_ARRAY_REF_BLOCK_GRAPH = new CFGGraphType("AltArrayRefBlockGraph") { // from class: soot.util.cfgcmd.CFGGraphType.15
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return CFGGraphType.loadAltGraph("soot.toolkits.graph.ArrayRefBlockGraph", b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG(g, b);
        }
    };
    public static final CFGGraphType ALT_BRIEF_BLOCK_GRAPH = new CFGGraphType("AltBriefBlockGraph") { // from class: soot.util.cfgcmd.CFGGraphType.16
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return CFGGraphType.loadAltGraph("soot.toolkits.graph.BriefBlockGraph", b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG(g, b);
        }
    };
    public static final CFGGraphType ALT_COMPLETE_BLOCK_GRAPH = new CFGGraphType("AltCompleteBlockGraph") { // from class: soot.util.cfgcmd.CFGGraphType.17
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return CFGGraphType.loadAltGraph("soot.toolkits.graph.CompleteBlockGraph", b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG(g, b);
        }
    };
    public static final CFGGraphType ALT_ZONED_BLOCK_GRAPH = new CFGGraphType("AltZonedBlockGraph") { // from class: soot.util.cfgcmd.CFGGraphType.18
        @Override // soot.util.cfgcmd.CFGGraphType
        public DirectedGraph<?> buildGraph(Body b) {
            return CFGGraphType.loadAltGraph("soot.toolkits.graph.ZonedBlockGraph", b);
        }

        @Override // soot.util.cfgcmd.CFGGraphType
        public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph<?> g, Body b) {
            return drawer.drawCFG(g, b);
        }
    };
    private static final CFGOptionMatcher graphTypeOptions = new CFGOptionMatcher(new CFGGraphType[]{BRIEF_UNIT_GRAPH, EXCEPTIONAL_UNIT_GRAPH, COMPLETE_UNIT_GRAPH, TRAP_UNIT_GRAPH, CLASSIC_COMPLETE_UNIT_GRAPH, BRIEF_BLOCK_GRAPH, EXCEPTIONAL_BLOCK_GRAPH, COMPLETE_BLOCK_GRAPH, CLASSIC_COMPLETE_BLOCK_GRAPH, ARRAY_REF_BLOCK_GRAPH, ZONED_BLOCK_GRAPH, ALT_ARRAY_REF_BLOCK_GRAPH, ALT_BRIEF_UNIT_GRAPH, ALT_COMPLETE_UNIT_GRAPH, ALT_TRAP_UNIT_GRAPH, ALT_BRIEF_BLOCK_GRAPH, ALT_COMPLETE_BLOCK_GRAPH, ALT_ZONED_BLOCK_GRAPH});

    public abstract DirectedGraph<?> buildGraph(Body body);

    public abstract DotGraph drawGraph(CFGToDotGraph cFGToDotGraph, DirectedGraph<?> directedGraph, Body body);

    /* synthetic */ CFGGraphType(String str, CFGGraphType cFGGraphType) {
        this(str);
    }

    private CFGGraphType(String name) {
        super(name);
    }

    public static CFGGraphType getGraphType(String option) {
        return (CFGGraphType) graphTypeOptions.match(option);
    }

    public static String help(int initialIndent, int rightMargin, int hangingIndent) {
        return graphTypeOptions.help(initialIndent, rightMargin, hangingIndent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static DirectedGraph<?> loadAltGraph(String className, Body b) {
        try {
            Class<?> graphClass = AltClassLoader.v().loadClass(className);
            Constructor<?> constructor = graphClass.getConstructor(Body.class);
            DirectedGraph<?> result = (DirectedGraph) constructor.newInstance(b);
            return result;
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), (Throwable) e);
            throw new IllegalArgumentException("Unable to find " + className + " in alternate classpath: " + e.getMessage());
        } catch (IllegalAccessException e2) {
            logger.error(e2.getMessage(), (Throwable) e2);
            throw new IllegalArgumentException("Unable to access " + className + "(Body) in alternate classpath: " + e2.getMessage());
        } catch (InstantiationException e3) {
            logger.error(e3.getMessage(), (Throwable) e3);
            throw new IllegalArgumentException("Unable to instantiate " + className + " in alternate classpath: " + e3.getMessage());
        } catch (NoSuchMethodException e4) {
            logger.error(e4.getMessage(), (Throwable) e4);
            throw new IllegalArgumentException("There is no " + className + "(Body) constructor: " + e4.getMessage());
        } catch (InvocationTargetException e5) {
            logger.error(e5.getMessage(), (Throwable) e5);
            throw new IllegalArgumentException("Unable to invoke " + className + "(Body) in alternate classpath: " + e5.getMessage());
        }
    }
}
