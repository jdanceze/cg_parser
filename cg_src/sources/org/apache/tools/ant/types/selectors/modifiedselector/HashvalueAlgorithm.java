package org.apache.tools.ant.types.selectors.modifiedselector;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/modifiedselector/HashvalueAlgorithm.class */
public class HashvalueAlgorithm implements Algorithm {
    @Override // org.apache.tools.ant.types.selectors.modifiedselector.Algorithm
    public boolean isValid() {
        return true;
    }

    @Override // org.apache.tools.ant.types.selectors.modifiedselector.Algorithm
    public String getValue(File file) {
        if (!file.canRead()) {
            return null;
        }
        try {
            Reader r = new FileReader(file);
            int hash = FileUtils.readFully(r).hashCode();
            String num = Integer.toString(hash);
            r.close();
            return num;
        } catch (Exception e) {
            return null;
        }
    }

    public String toString() {
        return "HashvalueAlgorithm";
    }
}
