package org.apache.tools.ant.taskdefs.optional.vss;

import java.io.File;
import java.text.SimpleDateFormat;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.EnumeratedAttribute;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/vss/MSVSSHISTORY.class */
public class MSVSSHISTORY extends MSVSS {
    @Override // org.apache.tools.ant.taskdefs.optional.vss.MSVSS
    Commandline buildCmdLine() {
        Commandline commandLine = new Commandline();
        if (getVsspath() == null) {
            throw new BuildException("vsspath attribute must be set!", getLocation());
        }
        commandLine.setExecutable(getSSCommand());
        commandLine.createArgument().setValue(MSVSSConstants.COMMAND_HISTORY);
        commandLine.createArgument().setValue(getVsspath());
        commandLine.createArgument().setValue(MSVSSConstants.FLAG_AUTORESPONSE_DEF);
        commandLine.createArgument().setValue(getVersionDate());
        commandLine.createArgument().setValue(getVersionLabel());
        commandLine.createArgument().setValue(getRecursive());
        commandLine.createArgument().setValue(getStyle());
        commandLine.createArgument().setValue(getLogin());
        commandLine.createArgument().setValue(getOutput());
        return commandLine;
    }

    public void setRecursive(boolean recursive) {
        super.setInternalRecursive(recursive);
    }

    public void setUser(String user) {
        super.setInternalUser(user);
    }

    public void setFromDate(String fromDate) {
        super.setInternalFromDate(fromDate);
    }

    public void setToDate(String toDate) {
        super.setInternalToDate(toDate);
    }

    public void setFromLabel(String fromLabel) {
        super.setInternalFromLabel(fromLabel);
    }

    public void setToLabel(String toLabel) {
        super.setInternalToLabel(toLabel);
    }

    public void setNumdays(int numd) {
        super.setInternalNumDays(numd);
    }

    public void setOutput(File outfile) {
        if (outfile != null) {
            super.setInternalOutputFilename(outfile.getAbsolutePath());
        }
    }

    public void setDateFormat(String dateFormat) {
        super.setInternalDateFormat(new SimpleDateFormat(dateFormat));
    }

    public void setStyle(BriefCodediffNofile attr) {
        String option = attr.getValue();
        boolean z = true;
        switch (option.hashCode()) {
            case -1040106819:
                if (option.equals(MSVSSConstants.STYLE_NOFILE)) {
                    z = true;
                    break;
                }
                break;
            case -867501198:
                if (option.equals(MSVSSConstants.STYLE_CODEDIFF)) {
                    z = true;
                    break;
                }
                break;
            case 94005370:
                if (option.equals(MSVSSConstants.STYLE_BRIEF)) {
                    z = false;
                    break;
                }
                break;
            case 1544803905:
                if (option.equals("default")) {
                    z = true;
                    break;
                }
                break;
        }
        switch (z) {
            case false:
                super.setInternalStyle(MSVSSConstants.FLAG_BRIEF);
                return;
            case true:
                super.setInternalStyle(MSVSSConstants.FLAG_CODEDIFF);
                return;
            case true:
                super.setInternalStyle("");
                return;
            case true:
                super.setInternalStyle(MSVSSConstants.FLAG_NO_FILE);
                return;
            default:
                throw new BuildException("Style " + attr + " unknown.", getLocation());
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/vss/MSVSSHISTORY$BriefCodediffNofile.class */
    public static class BriefCodediffNofile extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{MSVSSConstants.STYLE_BRIEF, MSVSSConstants.STYLE_CODEDIFF, MSVSSConstants.STYLE_NOFILE, "default"};
        }
    }
}
