package soot.jimple.infoflow.results.xml;

import java.util.Set;
import soot.jimple.infoflow.results.InfoflowPerformanceData;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/results/xml/SerializedInfoflowResults.class */
public class SerializedInfoflowResults {
    private int fileFormatVersion = -1;
    private final MultiMap<SerializedSinkInfo, SerializedSourceInfo> results = new HashMultiMap();
    private InfoflowPerformanceData performanceData = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setFileFormatVersion(int version) {
        this.fileFormatVersion = version;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addResult(SerializedSourceInfo source, SerializedSinkInfo sink) {
        this.results.put(sink, source);
    }

    public int getResultCount() {
        int cnt = 0;
        for (SerializedSinkInfo sink : this.results.keySet()) {
            Set<SerializedSourceInfo> sources = this.results.get(sink);
            cnt += sources == null ? 0 : sources.size();
        }
        return cnt;
    }

    public InfoflowPerformanceData getPerformanceData() {
        return this.performanceData;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InfoflowPerformanceData getOrCreatePerformanceData() {
        if (this.performanceData == null) {
            this.performanceData = new InfoflowPerformanceData();
        }
        return this.performanceData;
    }

    public int hashCode() {
        int result = (31 * 1) + this.fileFormatVersion;
        return (31 * ((31 * result) + (this.performanceData == null ? 0 : this.performanceData.hashCode()))) + (this.results == null ? 0 : this.results.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SerializedInfoflowResults other = (SerializedInfoflowResults) obj;
        if (this.fileFormatVersion != other.fileFormatVersion) {
            return false;
        }
        if (this.performanceData == null) {
            if (other.performanceData != null) {
                return false;
            }
        } else if (!this.performanceData.equals(other.performanceData)) {
            return false;
        }
        if (this.results == null) {
            if (other.results != null) {
                return false;
            }
            return true;
        } else if (!this.results.equals(other.results)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isEmpty() {
        return this.results.isEmpty();
    }

    public int getFileFormatVersion() {
        return this.fileFormatVersion;
    }

    public MultiMap<SerializedSinkInfo, SerializedSourceInfo> getResults() {
        return this.results;
    }
}
