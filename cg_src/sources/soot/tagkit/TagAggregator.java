package soot.tagkit;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.baf.BafBody;
/* loaded from: gencallgraphv3.jar:soot/tagkit/TagAggregator.class */
public abstract class TagAggregator extends BodyTransformer {
    public abstract boolean wantTag(Tag tag);

    public abstract void considerTag(Tag tag, Unit unit, LinkedList<Tag> linkedList, LinkedList<Unit> linkedList2);

    public abstract String aggregatedName();

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        BafBody body = (BafBody) b;
        LinkedList<Tag> tags = new LinkedList<>();
        LinkedList<Unit> units = new LinkedList<>();
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            for (Tag tag : unit.getTags()) {
                if (wantTag(tag)) {
                    considerTag(tag, unit, tags, units);
                }
            }
        }
        if (units.size() > 0) {
            b.addTag(new CodeAttribute(aggregatedName(), new LinkedList(units), new LinkedList(tags)));
        }
        fini();
    }

    public void fini() {
    }
}
