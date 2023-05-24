package org.apache.tools.ant.types;

import java.util.ArrayList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/FilterSetCollection.class */
public class FilterSetCollection {
    private List<FilterSet> filterSets = new ArrayList();

    public FilterSetCollection() {
    }

    public FilterSetCollection(FilterSet filterSet) {
        addFilterSet(filterSet);
    }

    public void addFilterSet(FilterSet filterSet) {
        this.filterSets.add(filterSet);
    }

    public String replaceTokens(String line) {
        String replacedLine = line;
        for (FilterSet filterSet : this.filterSets) {
            replacedLine = filterSet.replaceTokens(replacedLine);
        }
        return replacedLine;
    }

    public boolean hasFilters() {
        return this.filterSets.stream().anyMatch((v0) -> {
            return v0.hasFilters();
        });
    }
}
