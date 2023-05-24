package soot.jimple.spark.geom.utils;

import java.io.PrintStream;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/utils/Histogram.class */
public class Histogram {
    private int[] limits;
    private int count = 0;
    private int[] results;

    public Histogram(int[] limits) {
        this.results = null;
        this.limits = limits;
        this.results = new int[limits.length + 1];
        for (int i = 0; i <= limits.length; i++) {
            this.results[i] = 0;
        }
    }

    public void printResult(PrintStream output) {
        if (this.count == 0) {
            output.println("No samples are inserted, no output!");
            return;
        }
        output.println("Samples : " + this.count);
        for (int i = 0; i < this.results.length; i++) {
            if (i == 0) {
                output.print("<=" + this.limits[0] + ": " + this.results[i]);
            } else if (i == this.results.length - 1) {
                output.print(">" + this.limits[this.limits.length - 1] + ": " + this.results[i]);
            } else {
                output.print(String.valueOf(this.limits[i - 1]) + "< x <=" + this.limits[i] + ": " + this.results[i]);
            }
            output.printf(", percentage = %.2f\n", Double.valueOf((this.results[i] * 100.0d) / this.count));
        }
    }

    public void printResult(PrintStream output, String title) {
        output.println(title);
        printResult(output);
    }

    public void printResult(PrintStream output, String title, Histogram other) {
        output.println(title);
        if (this.count == 0) {
            output.println("No samples are inserted, no output!");
            return;
        }
        output.println("Samples : " + this.count + " (" + other.count + ")");
        for (int i = 0; i < this.results.length; i++) {
            if (i == 0) {
                output.printf("<= %d: %d (%d)", Integer.valueOf(this.limits[0]), Integer.valueOf(this.results[i]), Integer.valueOf(other.results[i]));
            } else if (i == this.results.length - 1) {
                output.printf("> %d: %d (%d)", Integer.valueOf(this.limits[this.limits.length - 1]), Integer.valueOf(this.results[i]), Integer.valueOf(other.results[i]));
            } else {
                output.printf("%d < x <= %d: %d (%d)", Integer.valueOf(this.limits[i - 1]), Integer.valueOf(this.limits[i]), Integer.valueOf(this.results[i]), Integer.valueOf(other.results[i]));
            }
            output.printf(", percentage = %.2f%% (%.2f%%) \n", Double.valueOf((this.results[i] * 100.0d) / this.count), Double.valueOf((other.results[i] * 100.0d) / other.count));
        }
        output.println();
    }

    public void addNumber(int num) {
        this.count++;
        int i = 0;
        while (true) {
            if (i >= this.limits.length) {
                break;
            } else if (num > this.limits[i]) {
                i++;
            } else {
                int[] iArr = this.results;
                int i2 = i;
                iArr[i2] = iArr[i2] + 1;
                break;
            }
        }
        if (i == this.limits.length) {
            int[] iArr2 = this.results;
            int i3 = i;
            iArr2[i3] = iArr2[i3] + 1;
        }
    }

    public void merge(Histogram other) {
        for (int i = 0; i <= this.limits.length; i++) {
            int[] iArr = this.results;
            int i2 = i;
            iArr[i2] = iArr[i2] + other.results[i];
        }
        this.count += other.count;
    }

    public int getTotalNumofSamples() {
        return this.count;
    }

    public void scaleToSamples(int usrSamples) {
        double ratio = usrSamples / this.count;
        this.count = 0;
        for (int i = 0; i <= this.limits.length; i++) {
            this.results[i] = (int) Math.round(this.results[i] * ratio);
            this.count += this.results[i];
        }
    }

    public int getResult(int inx) {
        if (inx >= this.limits.length) {
            return 0;
        }
        return this.results[inx];
    }
}
