package soot.jimple.infoflow.android.source;

import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.data.CategoryDefinition;
import soot.jimple.infoflow.android.source.parsers.xml.AbstractXMLSourceSinkParser;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory;
import soot.jimple.infoflow.sourcesSinks.definitions.SourceSinkType;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/source/ConfigurationBasedCategoryFilter.class */
public class ConfigurationBasedCategoryFilter implements AbstractXMLSourceSinkParser.ICategoryFilter {
    private final InfoflowAndroidConfiguration.SourceSinkConfiguration config;

    public ConfigurationBasedCategoryFilter(InfoflowAndroidConfiguration.SourceSinkConfiguration config) {
        this.config = config;
    }

    @Override // soot.jimple.infoflow.android.source.parsers.xml.AbstractXMLSourceSinkParser.ICategoryFilter
    public boolean acceptsCategory(ISourceSinkCategory category) {
        if (category instanceof CategoryDefinition) {
            CategoryDefinition catDef = ((CategoryDefinition) category).getIdOnlyDescription();
            InfoflowConfiguration.CategoryMode sourceType = this.config.getSourceCategoriesAndModes().get(catDef);
            InfoflowConfiguration.CategoryMode sinkType = this.config.getSinkCategoriesAndModes().get(catDef);
            if (sourceType != null && sourceType == InfoflowConfiguration.CategoryMode.Exclude && sinkType != null && sinkType == InfoflowConfiguration.CategoryMode.Exclude) {
                return false;
            }
            if (this.config.getSinkFilterMode() == InfoflowConfiguration.SourceSinkFilterMode.UseOnlyIncluded) {
                if (sourceType != null && sourceType == InfoflowConfiguration.CategoryMode.Include) {
                    return true;
                }
                if (sinkType != null && sinkType == InfoflowConfiguration.CategoryMode.Include) {
                    return true;
                }
                return false;
            }
            return true;
        }
        return true;
    }

    @Override // soot.jimple.infoflow.android.source.parsers.xml.AbstractXMLSourceSinkParser.ICategoryFilter
    public SourceSinkType filter(ISourceSinkCategory category, SourceSinkType sourceSinkType) {
        if (category instanceof CategoryDefinition) {
            CategoryDefinition catDef = (CategoryDefinition) category;
            catDef.getIdOnlyDescription();
            InfoflowConfiguration.CategoryMode sourceMode = this.config.getSourceCategoriesAndModes().get(category);
            InfoflowConfiguration.CategoryMode sinkMode = this.config.getSinkCategoriesAndModes().get(category);
            if (sourceSinkType == SourceSinkType.Source || sourceSinkType == SourceSinkType.Both) {
                if (this.config.getSourceFilterMode() == InfoflowConfiguration.SourceSinkFilterMode.UseAllButExcluded) {
                    if (sourceMode != null && sourceMode == InfoflowConfiguration.CategoryMode.Exclude) {
                        sourceSinkType = sourceSinkType.removeType(SourceSinkType.Source);
                    }
                } else if (this.config.getSourceFilterMode() == InfoflowConfiguration.SourceSinkFilterMode.UseOnlyIncluded && (sourceMode == null || sourceMode != InfoflowConfiguration.CategoryMode.Include)) {
                    sourceSinkType = sourceSinkType.removeType(SourceSinkType.Source);
                }
            }
            if (sourceSinkType == SourceSinkType.Sink || sourceSinkType == SourceSinkType.Both) {
                if (this.config.getSinkFilterMode() == InfoflowConfiguration.SourceSinkFilterMode.UseAllButExcluded) {
                    if (sinkMode != null && sinkMode == InfoflowConfiguration.CategoryMode.Exclude) {
                        sourceSinkType = sourceSinkType.removeType(SourceSinkType.Sink);
                    }
                } else if (this.config.getSinkFilterMode() == InfoflowConfiguration.SourceSinkFilterMode.UseOnlyIncluded && (sinkMode == null || sinkMode != InfoflowConfiguration.CategoryMode.Include)) {
                    sourceSinkType = sourceSinkType.removeType(SourceSinkType.Sink);
                }
            }
            return sourceSinkType;
        }
        return SourceSinkType.Undefined;
    }
}
