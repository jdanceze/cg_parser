package org.mockito.internal.reporting;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/reporting/Pluralizer.class */
public class Pluralizer {
    public static String pluralize(int number) {
        return number == 1 ? "1 time" : number + " times";
    }

    public static String were_exactly_x_interactions(int x) {
        if (x == 1) {
            return "was exactly 1 interaction";
        }
        return "were exactly " + x + " interactions";
    }
}
