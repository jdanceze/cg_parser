package soot.jimple.spark.ondemand.genericutil;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/genericutil/Averager.class */
public class Averager {
    private double curAverage;
    private long numSamples;

    public void addSample(double sample) {
        this.curAverage = ((this.curAverage * this.numSamples) + sample) / (this.numSamples + 1);
        this.numSamples++;
    }

    public double getCurAverage() {
        return this.curAverage;
    }

    public long getNumSamples() {
        return this.numSamples;
    }
}
