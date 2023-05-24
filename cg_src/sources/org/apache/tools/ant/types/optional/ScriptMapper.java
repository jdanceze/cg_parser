package org.apache.tools.ant.types.optional;

import java.util.ArrayList;
import org.apache.tools.ant.util.FileNameMapper;
import soot.jimple.infoflow.rifl.RIFLConstants;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/optional/ScriptMapper.class */
public class ScriptMapper extends AbstractScriptComponent implements FileNameMapper {
    private ArrayList<String> files;

    @Override // org.apache.tools.ant.util.FileNameMapper
    public void setFrom(String from) {
    }

    @Override // org.apache.tools.ant.util.FileNameMapper
    public void setTo(String to) {
    }

    public void clear() {
        this.files = new ArrayList<>(1);
    }

    public void addMappedName(String mapping) {
        this.files.add(mapping);
    }

    @Override // org.apache.tools.ant.util.FileNameMapper
    public String[] mapFileName(String sourceFileName) {
        initScriptRunner();
        getRunner().addBean(RIFLConstants.SOURCE_TAG, sourceFileName);
        clear();
        executeScript("ant_mapper");
        if (this.files.isEmpty()) {
            return null;
        }
        return (String[]) this.files.toArray(new String[this.files.size()]);
    }
}
