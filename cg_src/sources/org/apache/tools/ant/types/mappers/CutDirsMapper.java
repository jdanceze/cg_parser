package org.apache.tools.ant.types.mappers;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileNameMapper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/mappers/CutDirsMapper.class */
public class CutDirsMapper implements FileNameMapper {
    private int dirs = 0;

    public void setDirs(int dirs) {
        this.dirs = dirs;
    }

    @Override // org.apache.tools.ant.util.FileNameMapper
    public void setFrom(String ignore) {
    }

    @Override // org.apache.tools.ant.util.FileNameMapper
    public void setTo(String ignore) {
    }

    @Override // org.apache.tools.ant.util.FileNameMapper
    public String[] mapFileName(String sourceFileName) {
        if (this.dirs <= 0) {
            throw new BuildException("dirs must be set to a positive number");
        }
        char fileSep = File.separatorChar;
        if (sourceFileName == null) {
            return null;
        }
        String fileSepCorrected = sourceFileName.replace('/', fileSep).replace('\\', fileSep);
        int nthMatch = fileSepCorrected.indexOf(fileSep);
        for (int n = 1; nthMatch > -1 && n < this.dirs; n++) {
            nthMatch = fileSepCorrected.indexOf(fileSep, nthMatch + 1);
        }
        if (nthMatch == -1) {
            return null;
        }
        return new String[]{sourceFileName.substring(nthMatch + 1)};
    }
}
