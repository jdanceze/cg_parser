package soot.baf.toolkits.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
/* loaded from: gencallgraphv3.jar:soot/baf/toolkits/base/PeepholeOptimizer.class */
public class PeepholeOptimizer extends BodyTransformer {
    private final String packageName = "soot.baf.toolkits.base";
    private final Map<String, Class<?>> peepholeMap = new HashMap();
    private static final Logger logger = LoggerFactory.getLogger(PeepholeOptimizer.class);
    private static boolean peepholesLoaded = false;
    private static final Object loaderLock = new Object();

    public PeepholeOptimizer(Singletons.Global g) {
    }

    public static PeepholeOptimizer v() {
        return G.v().soot_baf_toolkits_base_PeepholeOptimizer();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v29, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r0v30, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v40, types: [java.util.List, java.util.LinkedList] */
    /* JADX WARN: Type inference failed for: r0v44 */
    /* JADX WARN: Type inference failed for: r0v56, types: [java.lang.Class] */
    /* JADX WARN: Type inference failed for: r0v61, types: [java.lang.Class, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r0v66, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r0v68, types: [int] */
    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        if (!peepholesLoaded) {
            Class<?> cls = loaderLock;
            synchronized (cls) {
                if (!peepholesLoaded) {
                    peepholesLoaded = true;
                    InputStream peepholeListingStream = PeepholeOptimizer.class.getResourceAsStream("/peephole.dat");
                    if (peepholeListingStream == null) {
                        logger.warn("Could not find peephole.dat in the classpath");
                        return;
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(peepholeListingStream));
                    String line = null;
                    cls = new LinkedList();
                    try {
                        line = reader.readLine();
                        while (true) {
                            cls = line;
                            if (cls == 0) {
                                break;
                            }
                            cls = line.length();
                            if (cls > 0 && line.charAt(0) != '#') {
                                cls.add(line);
                            }
                            line = reader.readLine();
                        }
                        try {
                            reader.close();
                            cls = peepholeListingStream;
                            cls.close();
                        } catch (IOException e) {
                            logger.debug(e.getMessage(), (Throwable) e);
                        }
                        for (String peepholeName : cls) {
                            cls = this.peepholeMap.get(peepholeName);
                            if (cls == 0) {
                                try {
                                    cls = Class.forName("soot.baf.toolkits.base." + peepholeName);
                                    this.peepholeMap.put(peepholeName, cls);
                                } catch (ClassNotFoundException e2) {
                                    throw new RuntimeException(e2.toString());
                                }
                            }
                        }
                    } catch (IOException e3) {
                        throw new RuntimeException("IO error occured while reading file:  " + line + System.getProperty("line.separator") + e3);
                    }
                }
            }
        }
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String peepholeName2 : this.peepholeMap.keySet()) {
                boolean peepholeWorked = true;
                while (peepholeWorked) {
                    peepholeWorked = false;
                    try {
                        Peephole p = (Peephole) this.peepholeMap.get(peepholeName2).newInstance();
                        if (p.apply(body)) {
                            peepholeWorked = true;
                            changed = true;
                        }
                    } catch (IllegalAccessException e4) {
                        throw new RuntimeException(e4.toString());
                    } catch (InstantiationException e5) {
                        throw new RuntimeException(e5.toString());
                    }
                }
            }
        }
    }
}
