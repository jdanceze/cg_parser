package org.mockito.internal.reporting;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/reporting/Discrepancy.class */
public class Discrepancy {
    private final int wantedCount;
    private final int actualCount;

    public Discrepancy(int wantedCount, int actualCount) {
        this.wantedCount = wantedCount;
        this.actualCount = actualCount;
    }

    public int getWantedCount() {
        return this.wantedCount;
    }

    public String getPluralizedWantedCount() {
        return Pluralizer.pluralize(this.wantedCount);
    }

    public int getActualCount() {
        return this.actualCount;
    }

    public String getPluralizedActualCount() {
        return Pluralizer.pluralize(this.actualCount);
    }
}
