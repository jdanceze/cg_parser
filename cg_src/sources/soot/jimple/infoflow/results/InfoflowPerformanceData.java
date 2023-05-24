package soot.jimple.infoflow.results;

import java.util.Objects;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/results/InfoflowPerformanceData.class */
public class InfoflowPerformanceData {
    private int callgraphConstructionSeconds = -1;
    private int taintPropagationSeconds = -1;
    private int pathReconstructionSeconds = -1;
    private int totalRuntimeSeconds = -1;
    private int maxMemoryConsumption = -1;
    private long edgePropagationCount = -1;
    private int sourceCount = -1;
    private int sinkCount = -1;
    private long infoflowPropagationCount = -1;
    private long aliasPropagationCount = -1;

    public int getCallgraphConstructionSeconds() {
        return this.callgraphConstructionSeconds;
    }

    public void setCallgraphConstructionSeconds(int callgraphSeconds) {
        this.callgraphConstructionSeconds = callgraphSeconds;
    }

    public int getTaintPropagationSeconds() {
        return this.taintPropagationSeconds;
    }

    public void setTaintPropagationSeconds(int taintPropagationSeconds) {
        this.taintPropagationSeconds = taintPropagationSeconds;
    }

    public int getPathReconstructionSeconds() {
        return this.pathReconstructionSeconds;
    }

    public void setPathReconstructionSeconds(int pathReconstructionSeconds) {
        this.pathReconstructionSeconds = pathReconstructionSeconds;
    }

    public int getTotalRuntimeSeconds() {
        return this.totalRuntimeSeconds;
    }

    public void setTotalRuntimeSeconds(int totalRuntimeSeconds) {
        this.totalRuntimeSeconds = totalRuntimeSeconds;
    }

    public int getMaxMemoryConsumption() {
        return this.maxMemoryConsumption;
    }

    public void setMaxMemoryConsumption(int maxMemoryConsumption) {
        this.maxMemoryConsumption = maxMemoryConsumption;
    }

    public boolean isEmpty() {
        return this.callgraphConstructionSeconds <= 0 && this.taintPropagationSeconds <= 0 && this.pathReconstructionSeconds <= 0 && this.totalRuntimeSeconds <= 0 && this.maxMemoryConsumption <= 0;
    }

    public void add(InfoflowPerformanceData performanceData) {
        if (performanceData.callgraphConstructionSeconds > 0) {
            if (this.callgraphConstructionSeconds < 0) {
                this.callgraphConstructionSeconds = performanceData.callgraphConstructionSeconds;
            } else {
                this.callgraphConstructionSeconds += performanceData.callgraphConstructionSeconds;
            }
        }
        if (performanceData.taintPropagationSeconds > 0) {
            if (this.taintPropagationSeconds < 0) {
                this.taintPropagationSeconds = performanceData.taintPropagationSeconds;
            } else {
                this.taintPropagationSeconds += performanceData.taintPropagationSeconds;
            }
        }
        if (performanceData.totalRuntimeSeconds > 0) {
            if (this.totalRuntimeSeconds < 0) {
                this.totalRuntimeSeconds = performanceData.totalRuntimeSeconds;
            } else {
                this.totalRuntimeSeconds += performanceData.totalRuntimeSeconds;
            }
        }
        if (performanceData.maxMemoryConsumption > 0) {
            if (this.maxMemoryConsumption < 0) {
                this.maxMemoryConsumption = performanceData.maxMemoryConsumption;
            } else {
                this.maxMemoryConsumption = Math.max(this.maxMemoryConsumption, performanceData.maxMemoryConsumption);
            }
        }
        if (performanceData.sourceCount > 0) {
            if (this.sourceCount < 0) {
                this.sourceCount = performanceData.sourceCount;
            } else {
                this.sourceCount += performanceData.sourceCount;
            }
        }
        if (performanceData.sinkCount > 0) {
            if (this.sinkCount < 0) {
                this.sinkCount = performanceData.sinkCount;
            } else {
                this.sinkCount += performanceData.sinkCount;
            }
        }
    }

    public void updateMaxMemoryConsumption(int usedMemory) {
        int mem = this.maxMemoryConsumption;
        if (mem < 0) {
            this.maxMemoryConsumption = usedMemory;
        } else {
            this.maxMemoryConsumption = Math.max(mem, usedMemory);
        }
    }

    public void addTaintPropagationSeconds(int toAdd) {
        int time = this.taintPropagationSeconds;
        if (time < 0) {
            this.taintPropagationSeconds = toAdd;
        } else {
            this.taintPropagationSeconds = time + toAdd;
        }
    }

    public void addEdgePropagationCount(long toAdd) {
        long edges = this.edgePropagationCount;
        if (edges < 0) {
            this.edgePropagationCount = toAdd;
        } else {
            this.edgePropagationCount = edges + toAdd;
        }
    }

    public void setSourceCount(int sourceCount) {
        this.sourceCount = sourceCount;
    }

    public int getSourceCount() {
        return this.sourceCount;
    }

    public void setSinkCount(int sinkCount) {
        this.sinkCount = sinkCount;
    }

    public int getSinkCount() {
        return this.sinkCount;
    }

    public long getEdgePropagationCount() {
        return this.edgePropagationCount;
    }

    public void setInfoflowPropagationCount(long infoflowPropagationCount) {
        this.infoflowPropagationCount = infoflowPropagationCount;
    }

    public void setAliasPropagationCount(long aliasPropagationCount) {
        this.aliasPropagationCount = aliasPropagationCount;
    }

    public long getInfoflowPropagationCount() {
        return this.infoflowPropagationCount;
    }

    public long getAliasPropagationCount() {
        return this.aliasPropagationCount;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.callgraphConstructionSeconds > 0) {
            sb.append(String.format("Callgraph Construction: %d seconds\n", Integer.valueOf(this.callgraphConstructionSeconds)));
        }
        if (this.taintPropagationSeconds > 0) {
            sb.append(String.format("Taint Propagation: %d seconds\n", Integer.valueOf(this.taintPropagationSeconds)));
        }
        if (this.pathReconstructionSeconds > 0) {
            sb.append(String.format("Path Reconstruction: %d seconds\n", Integer.valueOf(this.pathReconstructionSeconds)));
        }
        if (this.totalRuntimeSeconds > 0) {
            sb.append(String.format("Total Runtime: %d seconds\n", Integer.valueOf(this.totalRuntimeSeconds)));
        }
        if (this.maxMemoryConsumption > 0) {
            sb.append(String.format("Max Memory Consumption: %d MB\n", Integer.valueOf(this.maxMemoryConsumption)));
        }
        if (this.edgePropagationCount > 0) {
            sb.append(String.format("Edge Propagation Count: %d\n", Long.valueOf(this.edgePropagationCount)));
        }
        return sb.toString();
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.aliasPropagationCount), Integer.valueOf(this.callgraphConstructionSeconds), Long.valueOf(this.edgePropagationCount), Long.valueOf(this.infoflowPropagationCount), Integer.valueOf(this.maxMemoryConsumption), Integer.valueOf(this.pathReconstructionSeconds), Integer.valueOf(this.sinkCount), Integer.valueOf(this.sourceCount), Integer.valueOf(this.taintPropagationSeconds), Integer.valueOf(this.totalRuntimeSeconds));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        InfoflowPerformanceData other = (InfoflowPerformanceData) obj;
        return this.aliasPropagationCount == other.aliasPropagationCount && this.callgraphConstructionSeconds == other.callgraphConstructionSeconds && this.edgePropagationCount == other.edgePropagationCount && this.infoflowPropagationCount == other.infoflowPropagationCount && this.maxMemoryConsumption == other.maxMemoryConsumption && this.pathReconstructionSeconds == other.pathReconstructionSeconds && this.sinkCount == other.sinkCount && this.sourceCount == other.sourceCount && this.taintPropagationSeconds == other.taintPropagationSeconds && this.totalRuntimeSeconds == other.totalRuntimeSeconds;
    }
}
