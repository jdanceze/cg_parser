package soot.jimple.infoflow.sourcesSinks.definitions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/definitions/FilteringSourceSinkDefinitionProvider.class */
public class FilteringSourceSinkDefinitionProvider implements ISourceSinkDefinitionProvider {
    private final ISourceSinkDefinitionProvider innerProvider;
    private final ISourceSinkFilter filter;

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/definitions/FilteringSourceSinkDefinitionProvider$ISourceSinkFilter.class */
    public interface ISourceSinkFilter {
        boolean accepts(ISourceSinkDefinition iSourceSinkDefinition);
    }

    public FilteringSourceSinkDefinitionProvider(ISourceSinkDefinitionProvider innerProvider, ISourceSinkFilter filter) {
        this.innerProvider = innerProvider;
        this.filter = filter;
    }

    private Set<ISourceSinkDefinition> filter(Collection<? extends ISourceSinkDefinition> input) {
        Set<ISourceSinkDefinition> filtered = new HashSet<>(input.size());
        for (ISourceSinkDefinition def : input) {
            if (this.filter.accepts(def)) {
                filtered.add(def);
            }
        }
        return filtered;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider
    public Set<ISourceSinkDefinition> getSources() {
        return filter(this.innerProvider.getSources());
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider
    public Set<ISourceSinkDefinition> getSinks() {
        return filter(this.innerProvider.getSinks());
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider
    public Set<ISourceSinkDefinition> getAllMethods() {
        return filter(this.innerProvider.getAllMethods());
    }
}
