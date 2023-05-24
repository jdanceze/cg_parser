package soot.xml;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import soot.Body;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.ValueBox;
import soot.tagkit.Host;
import soot.tagkit.JimpleLineNumberTag;
import soot.tagkit.KeyTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/xml/TagCollector.class */
public class TagCollector {
    private final ArrayList<Attribute> attributes = new ArrayList<>();
    private final ArrayList<Key> keys = new ArrayList<>();

    public boolean isEmpty() {
        return this.attributes.isEmpty() && this.keys.isEmpty();
    }

    public void collectTags(SootClass sc) {
        collectTags(sc, true);
    }

    public void collectTags(SootClass sc, boolean includeBodies) {
        collectClassTags(sc);
        for (SootField sf : sc.getFields()) {
            collectFieldTags(sf);
        }
        for (SootMethod sm : sc.getMethods()) {
            collectMethodTags(sm);
            if (includeBodies && sm.hasActiveBody()) {
                collectBodyTags(sm.getActiveBody());
            }
        }
    }

    public void collectKeyTags(SootClass sc) {
        for (Tag next : sc.getTags()) {
            if (next instanceof KeyTag) {
                KeyTag kt = (KeyTag) next;
                Key k = new Key(kt.red(), kt.green(), kt.blue(), kt.key());
                k.aType(kt.analysisType());
                this.keys.add(k);
            }
        }
    }

    public void printKeys(PrintWriter writerOut) {
        Iterator<Key> it = this.keys.iterator();
        while (it.hasNext()) {
            Key k = it.next();
            k.print(writerOut);
        }
    }

    private void addAttribute(Attribute a) {
        if (!a.isEmpty()) {
            this.attributes.add(a);
        }
    }

    private void collectHostTags(Host h) {
        collectHostTags(h, t -> {
            return true;
        });
    }

    private void collectHostTags(Host h, Predicate<Tag> include) {
        List<Tag> tags = h.getTags();
        if (!tags.isEmpty()) {
            Attribute a = new Attribute();
            for (Tag t : tags) {
                if (include.test(t)) {
                    a.addTag(t);
                }
            }
            addAttribute(a);
        }
    }

    public void collectClassTags(SootClass sc) {
        collectHostTags(sc, t -> {
            return !(t instanceof SourceFileTag);
        });
    }

    public void collectFieldTags(SootField sf) {
        collectHostTags(sf);
    }

    public void collectMethodTags(SootMethod sm) {
        if (sm.hasActiveBody()) {
            collectHostTags(sm);
        }
    }

    public synchronized void collectBodyTags(Body b) {
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Attribute ua = new Attribute();
            JimpleLineNumberTag jlnt = null;
            for (Tag t : u.getTags()) {
                ua.addTag(t);
                if (t instanceof JimpleLineNumberTag) {
                    jlnt = (JimpleLineNumberTag) t;
                }
            }
            addAttribute(ua);
            for (ValueBox vb : u.getUseAndDefBoxes()) {
                if (!vb.getTags().isEmpty()) {
                    Attribute va = new Attribute();
                    for (Tag t2 : vb.getTags()) {
                        va.addTag(t2);
                        if (jlnt != null) {
                            va.addTag(jlnt);
                        }
                    }
                    addAttribute(va);
                }
            }
        }
    }

    public void printTags(PrintWriter writerOut) {
        Iterator<Attribute> it = this.attributes.iterator();
        while (it.hasNext()) {
            Attribute a = it.next();
            a.print(writerOut);
        }
    }
}
