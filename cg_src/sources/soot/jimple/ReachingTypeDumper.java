package soot.jimple;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import polyglot.main.Report;
import soot.Body;
import soot.Local;
import soot.PointsToAnalysis;
import soot.RefLikeType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/ReachingTypeDumper.class */
public class ReachingTypeDumper {
    protected PointsToAnalysis pa;
    protected String output_dir;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/ReachingTypeDumper$StringComparator.class */
    public static class StringComparator<T> implements Comparator<T> {
        private StringComparator() {
        }

        /* synthetic */ StringComparator(StringComparator stringComparator) {
            this();
        }

        @Override // java.util.Comparator
        public int compare(T o1, T o2) {
            return o1.toString().compareTo(o2.toString());
        }
    }

    public ReachingTypeDumper(PointsToAnalysis pa, String output_dir) {
        this.pa = pa;
        this.output_dir = output_dir;
    }

    public void dump() {
        try {
            PrintWriter file = new PrintWriter(new FileOutputStream(new File(this.output_dir, Report.types)));
            for (SootClass cls : Scene.v().getApplicationClasses()) {
                handleClass(file, cls);
            }
            for (SootClass cls2 : Scene.v().getLibraryClasses()) {
                handleClass(file, cls2);
            }
            file.close();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't dump reaching types." + e);
        }
    }

    protected void handleClass(PrintWriter out, SootClass c) {
        for (SootMethod m : c.getMethods()) {
            if (m.isConcrete()) {
                Body b = m.retrieveActiveBody();
                Local[] sortedLocals = (Local[]) b.getLocals().toArray(new Local[b.getLocalCount()]);
                Arrays.sort(sortedLocals, new StringComparator(null));
                for (Local l : sortedLocals) {
                    out.println("V " + m + l);
                    if (l.getType() instanceof RefLikeType) {
                        Set<Type> types = this.pa.reachingObjects(l).possibleTypes();
                        Type[] sortedTypes = (Type[]) types.toArray(new Type[types.size()]);
                        Arrays.sort(sortedTypes, new StringComparator(null));
                        for (Type type : sortedTypes) {
                            out.println("T " + type);
                        }
                    }
                }
            }
        }
    }
}
