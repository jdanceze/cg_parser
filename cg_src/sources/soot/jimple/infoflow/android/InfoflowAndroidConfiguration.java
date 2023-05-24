package soot.jimple.infoflow.android;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.data.CategoryDefinition;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/InfoflowAndroidConfiguration.class */
public class InfoflowAndroidConfiguration extends InfoflowConfiguration {
    private boolean oneComponentAtATime = false;
    private final CallbackConfiguration callbackConfig = new CallbackConfiguration();
    private final SourceSinkConfiguration sourceSinkConfig = new SourceSinkConfiguration();
    private final IccConfiguration iccConfig = new IccConfiguration();
    private final AnalysisFileConfiguration analysisFileConfig = new AnalysisFileConfiguration();
    private boolean mergeDexFiles = false;
    private static boolean createActivityEntryMethods = true;

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/InfoflowAndroidConfiguration$CallbackAnalyzer.class */
    public enum CallbackAnalyzer {
        Default,
        Fast;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static CallbackAnalyzer[] valuesCustom() {
            CallbackAnalyzer[] valuesCustom = values();
            int length = valuesCustom.length;
            CallbackAnalyzer[] callbackAnalyzerArr = new CallbackAnalyzer[length];
            System.arraycopy(valuesCustom, 0, callbackAnalyzerArr, 0, length);
            return callbackAnalyzerArr;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/InfoflowAndroidConfiguration$AnalysisFileConfiguration.class */
    public static class AnalysisFileConfiguration {
        private String targetAPKFile = "";
        private String sourceSinkFile = "";
        private String androidPlatformDir = "";
        private String additionalClasspath = "";
        private String outputFile = "";

        public void merge(AnalysisFileConfiguration fileConfig) {
            this.targetAPKFile = fileConfig.targetAPKFile;
            this.sourceSinkFile = fileConfig.sourceSinkFile;
            this.androidPlatformDir = fileConfig.androidPlatformDir;
            this.additionalClasspath = fileConfig.additionalClasspath;
            this.outputFile = fileConfig.outputFile;
        }

        public boolean validate() {
            return (this.targetAPKFile == null || this.targetAPKFile.isEmpty() || this.sourceSinkFile == null || this.sourceSinkFile.isEmpty() || this.androidPlatformDir == null || this.androidPlatformDir.isEmpty()) ? false : true;
        }

        public String getTargetAPKFile() {
            return this.targetAPKFile;
        }

        public void setTargetAPKFile(String targetAPKFile) {
            this.targetAPKFile = targetAPKFile;
        }

        public String getAndroidPlatformDir() {
            return this.androidPlatformDir;
        }

        public void setAndroidPlatformDir(String androidPlatformDir) {
            this.androidPlatformDir = androidPlatformDir;
        }

        public String getSourceSinkFile() {
            return this.sourceSinkFile;
        }

        public void setSourceSinkFile(String sourceSinkFile) {
            this.sourceSinkFile = sourceSinkFile;
        }

        public String getAdditionalClasspath() {
            return this.additionalClasspath;
        }

        public void setAdditionalClasspath(String additionalClasspath) {
            this.additionalClasspath = additionalClasspath;
        }

        public String getOutputFile() {
            return this.outputFile;
        }

        public void setOutputFile(String outputFile) {
            this.outputFile = outputFile;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.additionalClasspath == null ? 0 : this.additionalClasspath.hashCode());
            return (31 * ((31 * ((31 * ((31 * result) + (this.androidPlatformDir == null ? 0 : this.androidPlatformDir.hashCode()))) + (this.outputFile == null ? 0 : this.outputFile.hashCode()))) + (this.sourceSinkFile == null ? 0 : this.sourceSinkFile.hashCode()))) + (this.targetAPKFile == null ? 0 : this.targetAPKFile.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            AnalysisFileConfiguration other = (AnalysisFileConfiguration) obj;
            if (this.additionalClasspath == null) {
                if (other.additionalClasspath != null) {
                    return false;
                }
            } else if (!this.additionalClasspath.equals(other.additionalClasspath)) {
                return false;
            }
            if (this.androidPlatformDir == null) {
                if (other.androidPlatformDir != null) {
                    return false;
                }
            } else if (!this.androidPlatformDir.equals(other.androidPlatformDir)) {
                return false;
            }
            if (this.outputFile == null) {
                if (other.outputFile != null) {
                    return false;
                }
            } else if (!this.outputFile.equals(other.outputFile)) {
                return false;
            }
            if (this.sourceSinkFile == null) {
                if (other.sourceSinkFile != null) {
                    return false;
                }
            } else if (!this.sourceSinkFile.equals(other.sourceSinkFile)) {
                return false;
            }
            if (this.targetAPKFile == null) {
                if (other.targetAPKFile != null) {
                    return false;
                }
                return true;
            } else if (!this.targetAPKFile.equals(other.targetAPKFile)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/InfoflowAndroidConfiguration$IccConfiguration.class */
    public static class IccConfiguration {
        private boolean iccEnabled = false;
        private String iccModel = null;
        private boolean iccResultsPurify = true;

        public void merge(IccConfiguration iccConfig) {
            this.iccEnabled = iccConfig.iccEnabled;
            this.iccModel = iccConfig.iccModel;
            this.iccResultsPurify = iccConfig.iccResultsPurify;
        }

        public String getIccModel() {
            return this.iccModel;
        }

        public void setIccModel(String iccModel) {
            this.iccModel = iccModel;
        }

        public boolean isIccEnabled() {
            return (this.iccModel == null || this.iccModel.isEmpty()) ? false : true;
        }

        public boolean isIccResultsPurifyEnabled() {
            return isIccEnabled() && this.iccResultsPurify;
        }

        public void setIccResultsPurify(boolean iccResultsPurify) {
            this.iccResultsPurify = iccResultsPurify;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.iccEnabled ? 1231 : 1237);
            return (31 * ((31 * result) + (this.iccModel == null ? 0 : this.iccModel.hashCode()))) + (this.iccResultsPurify ? 1231 : 1237);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            IccConfiguration other = (IccConfiguration) obj;
            if (this.iccEnabled != other.iccEnabled) {
                return false;
            }
            if (this.iccModel == null) {
                if (other.iccModel != null) {
                    return false;
                }
            } else if (!this.iccModel.equals(other.iccModel)) {
                return false;
            }
            if (this.iccResultsPurify != other.iccResultsPurify) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/InfoflowAndroidConfiguration$CallbackConfiguration.class */
    public static class CallbackConfiguration {
        private boolean enableCallbacks = true;
        private CallbackAnalyzer callbackAnalyzer = CallbackAnalyzer.Default;
        private boolean filterThreadCallbacks = true;
        private int maxCallbacksPerComponent = 100;
        private int callbackAnalysisTimeout = 0;
        private int maxCallbackAnalysisDepth = -1;
        private boolean serializeCallbacks = false;
        private String callbacksFile = "";

        public void merge(CallbackConfiguration cbConfig) {
            this.enableCallbacks = cbConfig.enableCallbacks;
            this.callbackAnalyzer = cbConfig.callbackAnalyzer;
            this.filterThreadCallbacks = cbConfig.filterThreadCallbacks;
            this.maxCallbacksPerComponent = cbConfig.maxCallbacksPerComponent;
            this.callbackAnalysisTimeout = cbConfig.callbackAnalysisTimeout;
            this.maxCallbackAnalysisDepth = cbConfig.maxCallbackAnalysisDepth;
            this.serializeCallbacks = cbConfig.serializeCallbacks;
            this.callbacksFile = cbConfig.callbacksFile;
        }

        public void setEnableCallbacks(boolean enableCallbacks) {
            this.enableCallbacks = enableCallbacks;
        }

        public boolean getEnableCallbacks() {
            return this.enableCallbacks;
        }

        public void setCallbackAnalyzer(CallbackAnalyzer callbackAnalyzer) {
            this.callbackAnalyzer = callbackAnalyzer;
        }

        public CallbackAnalyzer getCallbackAnalyzer() {
            return this.callbackAnalyzer;
        }

        public void setFilterThreadCallbacks(boolean filterThreadCallbacks) {
            this.filterThreadCallbacks = filterThreadCallbacks;
        }

        public boolean getFilterThreadCallbacks() {
            return this.filterThreadCallbacks;
        }

        public int getMaxCallbacksPerComponent() {
            return this.maxCallbacksPerComponent;
        }

        public void setMaxCallbacksPerComponent(int maxCallbacksPerComponent) {
            this.maxCallbacksPerComponent = maxCallbacksPerComponent;
        }

        public int getCallbackAnalysisTimeout() {
            return this.callbackAnalysisTimeout;
        }

        public void setCallbackAnalysisTimeout(int callbackAnalysisTimeout) {
            this.callbackAnalysisTimeout = callbackAnalysisTimeout;
        }

        public int getMaxAnalysisCallbackDepth() {
            return this.maxCallbackAnalysisDepth;
        }

        public void setMaxAnalysisCallbackDepth(int maxCallbackAnalysisDepth) {
            this.maxCallbackAnalysisDepth = maxCallbackAnalysisDepth;
        }

        public boolean isSerializeCallbacks() {
            return this.serializeCallbacks;
        }

        public void setSerializeCallbacks(boolean serializeCallbacks) {
            this.serializeCallbacks = serializeCallbacks;
        }

        public String getCallbacksFile() {
            return this.callbacksFile;
        }

        public void setCallbacksFile(String callbacksFile) {
            this.callbacksFile = callbacksFile;
        }

        public int hashCode() {
            int result = (31 * 1) + this.callbackAnalysisTimeout;
            return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.callbackAnalyzer == null ? 0 : this.callbackAnalyzer.hashCode()))) + (this.callbacksFile == null ? 0 : this.callbacksFile.hashCode()))) + (this.enableCallbacks ? 1231 : 1237))) + (this.filterThreadCallbacks ? 1231 : 1237))) + this.maxCallbackAnalysisDepth)) + this.maxCallbacksPerComponent)) + (this.serializeCallbacks ? 1231 : 1237);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            CallbackConfiguration other = (CallbackConfiguration) obj;
            if (this.callbackAnalysisTimeout != other.callbackAnalysisTimeout || this.callbackAnalyzer != other.callbackAnalyzer) {
                return false;
            }
            if (this.callbacksFile == null) {
                if (other.callbacksFile != null) {
                    return false;
                }
            } else if (!this.callbacksFile.equals(other.callbacksFile)) {
                return false;
            }
            if (this.enableCallbacks != other.enableCallbacks || this.filterThreadCallbacks != other.filterThreadCallbacks || this.maxCallbackAnalysisDepth != other.maxCallbackAnalysisDepth || this.maxCallbacksPerComponent != other.maxCallbacksPerComponent || this.serializeCallbacks != other.serializeCallbacks) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/InfoflowAndroidConfiguration$SourceSinkConfiguration.class */
    public static class SourceSinkConfiguration extends InfoflowConfiguration.SourceSinkConfiguration {
        private Map<CategoryDefinition, InfoflowConfiguration.CategoryMode> sourceCategories = new HashMap();
        private Map<CategoryDefinition, InfoflowConfiguration.CategoryMode> sinkCategories = new HashMap();

        public void merge(SourceSinkConfiguration ssConfig) {
            super.merge((InfoflowConfiguration.SourceSinkConfiguration) ssConfig);
            this.sourceCategories.putAll(ssConfig.sourceCategories);
            this.sinkCategories.putAll(ssConfig.sinkCategories);
        }

        public Set<CategoryDefinition> getSourceCategories() {
            return this.sourceCategories.keySet();
        }

        public Set<CategoryDefinition> getSinkCategories() {
            return this.sinkCategories.keySet();
        }

        public Map<CategoryDefinition, InfoflowConfiguration.CategoryMode> getSourceCategoriesAndModes() {
            return this.sourceCategories;
        }

        public Map<CategoryDefinition, InfoflowConfiguration.CategoryMode> getSinkCategoriesAndModes() {
            return this.sinkCategories;
        }

        public void addSourceCategory(CategoryDefinition category, InfoflowConfiguration.CategoryMode mode) {
            this.sourceCategories.put(category, mode);
        }

        public void addSinkCategory(CategoryDefinition category, InfoflowConfiguration.CategoryMode mode) {
            this.sinkCategories.put(category, mode);
        }

        @Override // soot.jimple.infoflow.InfoflowConfiguration.SourceSinkConfiguration
        public int hashCode() {
            int result = 31 * super.hashCode();
            return (31 * ((31 * result) + (this.sinkCategories == null ? 0 : this.sinkCategories.hashCode()))) + (this.sourceCategories == null ? 0 : this.sourceCategories.hashCode());
        }

        @Override // soot.jimple.infoflow.InfoflowConfiguration.SourceSinkConfiguration
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            SourceSinkConfiguration other = (SourceSinkConfiguration) obj;
            if (!super.equals(obj)) {
                return false;
            }
            if (this.sinkCategories == null) {
                if (other.sinkCategories != null) {
                    return false;
                }
            } else if (!this.sinkCategories.equals(other.sinkCategories)) {
                return false;
            }
            if (this.sourceCategories == null) {
                if (other.sourceCategories != null) {
                    return false;
                }
                return true;
            } else if (!this.sourceCategories.equals(other.sourceCategories)) {
                return false;
            } else {
                return true;
            }
        }
    }

    public InfoflowAndroidConfiguration() {
        setEnableArraySizeTainting(false);
        setInspectSources(false);
        setInspectSinks(false);
        setIgnoreFlowsInSystemPackages(true);
        setExcludeSootLibraryClasses(true);
    }

    @Override // soot.jimple.infoflow.InfoflowConfiguration
    public void merge(InfoflowConfiguration config) {
        super.merge(config);
        if (config instanceof InfoflowAndroidConfiguration) {
            InfoflowAndroidConfiguration androidConfig = (InfoflowAndroidConfiguration) config;
            this.oneComponentAtATime = androidConfig.oneComponentAtATime;
            this.callbackConfig.merge(androidConfig.callbackConfig);
            this.sourceSinkConfig.merge(androidConfig.sourceSinkConfig);
            this.iccConfig.merge(androidConfig.iccConfig);
            this.analysisFileConfig.merge(androidConfig.analysisFileConfig);
            this.mergeDexFiles = androidConfig.mergeDexFiles;
            createActivityEntryMethods = createActivityEntryMethods;
        }
    }

    public CallbackConfiguration getCallbackConfig() {
        return this.callbackConfig;
    }

    @Override // soot.jimple.infoflow.InfoflowConfiguration
    public SourceSinkConfiguration getSourceSinkConfig() {
        return this.sourceSinkConfig;
    }

    public IccConfiguration getIccConfig() {
        return this.iccConfig;
    }

    public AnalysisFileConfiguration getAnalysisFileConfig() {
        return this.analysisFileConfig;
    }

    public void setOneComponentAtATime(boolean oneComponentAtATime) {
        this.oneComponentAtATime = oneComponentAtATime;
    }

    public boolean getOneComponentAtATime() {
        return this.oneComponentAtATime;
    }

    public boolean getMergeDexFiles() {
        return this.mergeDexFiles;
    }

    public void setMergeDexFiles(boolean mergeDexFiles) {
        this.mergeDexFiles = mergeDexFiles;
    }

    public static boolean getCreateActivityEntryMethods() {
        return createActivityEntryMethods;
    }

    public static void setCreateActivityEntryMethods(boolean createActivityEntryMethods2) {
        createActivityEntryMethods = createActivityEntryMethods2;
    }

    @Override // soot.jimple.infoflow.InfoflowConfiguration
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.analysisFileConfig == null ? 0 : this.analysisFileConfig.hashCode()))) + (this.callbackConfig == null ? 0 : this.callbackConfig.hashCode()))) + (this.iccConfig == null ? 0 : this.iccConfig.hashCode()))) + (this.mergeDexFiles ? 1231 : 1237))) + (this.oneComponentAtATime ? 1231 : 1237))) + (this.sourceSinkConfig == null ? 0 : this.sourceSinkConfig.hashCode());
    }

    @Override // soot.jimple.infoflow.InfoflowConfiguration
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        InfoflowAndroidConfiguration other = (InfoflowAndroidConfiguration) obj;
        if (this.analysisFileConfig == null) {
            if (other.analysisFileConfig != null) {
                return false;
            }
        } else if (!this.analysisFileConfig.equals(other.analysisFileConfig)) {
            return false;
        }
        if (this.callbackConfig == null) {
            if (other.callbackConfig != null) {
                return false;
            }
        } else if (!this.callbackConfig.equals(other.callbackConfig)) {
            return false;
        }
        if (this.iccConfig == null) {
            if (other.iccConfig != null) {
                return false;
            }
        } else if (!this.iccConfig.equals(other.iccConfig)) {
            return false;
        }
        if (this.mergeDexFiles != other.mergeDexFiles || this.oneComponentAtATime != other.oneComponentAtATime) {
            return false;
        }
        if (this.sourceSinkConfig == null) {
            if (other.sourceSinkConfig != null) {
                return false;
            }
            return true;
        } else if (!this.sourceSinkConfig.equals(other.sourceSinkConfig)) {
            return false;
        } else {
            return true;
        }
    }
}
