package soot.jimple.spark.pag;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.AnySubType;
import soot.Context;
import soot.RefLikeType;
import soot.Type;
import soot.toolkits.scalar.Pair;
import soot.util.ArrayNumberer;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/VarNode.class */
public abstract class VarNode extends ValNode implements Comparable {
    private static final Logger logger = LoggerFactory.getLogger(VarNode.class);
    protected Object variable;
    protected Map<SparkField, FieldRefNode> fields;
    protected int finishingNumber;
    protected boolean interProcTarget;
    protected boolean interProcSource;
    protected int numDerefs;

    public Context context() {
        return null;
    }

    public Collection<FieldRefNode> getAllFieldRefs() {
        if (this.fields == null) {
            return Collections.emptyList();
        }
        return this.fields.values();
    }

    public FieldRefNode dot(SparkField field) {
        if (this.fields == null) {
            return null;
        }
        return this.fields.get(field);
    }

    @Override // java.lang.Comparable
    public int compareTo(Object o) {
        VarNode other = (VarNode) o;
        if (other.finishingNumber == this.finishingNumber && other != this) {
            logger.debug("This is: " + this + " with id " + getNumber() + " and number " + this.finishingNumber);
            logger.debug("Other is: " + other + " with id " + other.getNumber() + " and number " + other.finishingNumber);
            throw new RuntimeException("Comparison error");
        }
        return other.finishingNumber - this.finishingNumber;
    }

    public void setFinishingNumber(int i) {
        this.finishingNumber = i;
        if (i > this.pag.maxFinishNumber) {
            this.pag.maxFinishNumber = i;
        }
    }

    public Object getVariable() {
        return this.variable;
    }

    public void setInterProcTarget() {
        this.interProcTarget = true;
    }

    public boolean isInterProcTarget() {
        return this.interProcTarget;
    }

    public void setInterProcSource() {
        this.interProcSource = true;
    }

    public boolean isInterProcSource() {
        return this.interProcSource;
    }

    public boolean isThisPtr() {
        if (this.variable instanceof Pair) {
            Pair o = (Pair) this.variable;
            return o.isThisParameter();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VarNode(PAG pag, Object variable, Type t) {
        super(pag, t);
        this.finishingNumber = 0;
        this.interProcTarget = false;
        this.interProcSource = false;
        this.numDerefs = 0;
        if (!(t instanceof RefLikeType) || (t instanceof AnySubType)) {
            throw new RuntimeException("Attempt to create VarNode of type " + t);
        }
        this.variable = variable;
        pag.getVarNodeNumberer().add((ArrayNumberer<VarNode>) this);
        int i = pag.maxFinishNumber + 1;
        pag.maxFinishNumber = i;
        setFinishingNumber(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addField(FieldRefNode frn, SparkField field) {
        if (this.fields == null) {
            this.fields = new HashMap();
        }
        this.fields.put(field, frn);
    }
}
