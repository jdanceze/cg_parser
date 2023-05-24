package soot.jimple.toolkits.thread.mhp;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/MonitorDepth.class */
public class MonitorDepth {
    private String objName;
    private int depth;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MonitorDepth(String s, int d) {
        this.objName = s;
        this.depth = d;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getObjName() {
        return this.objName;
    }

    protected void SetObjName(String s) {
        this.objName = s;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getDepth() {
        return this.depth;
    }

    protected void setDepth(int d) {
        this.depth = d;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void decreaseDepth() {
        if (this.depth > 0) {
            this.depth--;
            return;
        }
        throw new RuntimeException("Error! You can not decrease a monitor depth of " + this.depth);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void increaseDepth() {
        this.depth++;
    }
}
