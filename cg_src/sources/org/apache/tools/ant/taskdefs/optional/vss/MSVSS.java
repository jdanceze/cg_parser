package org.apache.tools.ant.taskdefs.optional.vss;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.util.FileUtils;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/vss/MSVSS.class */
public abstract class MSVSS extends Task implements MSVSSConstants {
    private String ssDir = null;
    private String vssLogin = null;
    private String vssPath = null;
    private String serverPath = null;
    private String version = null;
    private String date = null;
    private String label = null;
    private String autoResponse = null;
    private String localPath = null;
    private String comment = null;
    private String fromLabel = null;
    private String toLabel = null;
    private String outputFileName = null;
    private String user = null;
    private String fromDate = null;
    private String toDate = null;
    private String style = null;
    private boolean quiet = false;
    private boolean recursive = false;
    private boolean writable = false;
    private boolean failOnError = true;
    private boolean getLocalCopy = true;
    private int numDays = Integer.MIN_VALUE;
    private DateFormat dateFormat = DateFormat.getDateInstance(3);
    private CurrentModUpdated timestamp = null;
    private WritableFiles writableFiles = null;

    abstract Commandline buildCmdLine();

    public final void setSsdir(String dir) {
        this.ssDir = FileUtils.translatePath(dir);
    }

    public final void setLogin(String vssLogin) {
        this.vssLogin = vssLogin;
    }

    public final void setVsspath(String vssPath) {
        String projectPath;
        if (vssPath.startsWith("vss://")) {
            projectPath = vssPath.substring(5);
        } else {
            projectPath = vssPath;
        }
        if (projectPath.startsWith("$")) {
            this.vssPath = projectPath;
        } else {
            this.vssPath = "$" + projectPath;
        }
    }

    public final void setServerpath(String serverPath) {
        this.serverPath = serverPath;
    }

    public final void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Commandline commandLine = buildCmdLine();
        int result = run(commandLine);
        if (Execute.isFailure(result) && getFailOnError()) {
            String msg = "Failed executing: " + formatCommandLine(commandLine) + " With a return code of " + result;
            throw new BuildException(msg, getLocation());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalComment(String comment) {
        this.comment = comment;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalAutoResponse(String autoResponse) {
        this.autoResponse = autoResponse;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalDate(String date) {
        this.date = date;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    protected void setInternalFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalFromLabel(String fromLabel) {
        this.fromLabel = fromLabel;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalLabel(String label) {
        this.label = label;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalLocalPath(String localPath) {
        this.localPath = localPath;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalNumDays(int numDays) {
        this.numDays = numDays;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalOutputFilename(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalRecursive(boolean recursive) {
        this.recursive = recursive;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalStyle(String style) {
        this.style = style;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalToDate(String toDate) {
        this.toDate = toDate;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalToLabel(String toLabel) {
        this.toLabel = toLabel;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalUser(String user) {
        this.user = user;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalVersion(String version) {
        this.version = version;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalWritable(boolean writable) {
        this.writable = writable;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalFileTimeStamp(CurrentModUpdated timestamp) {
        this.timestamp = timestamp;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalWritableFiles(WritableFiles writableFiles) {
        this.writableFiles = writableFiles;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalGetLocalCopy(boolean getLocalCopy) {
        this.getLocalCopy = getLocalCopy;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getSSCommand() {
        if (this.ssDir == null) {
            return MSVSSConstants.SS_EXE;
        }
        return this.ssDir.endsWith(File.separator) ? this.ssDir + MSVSSConstants.SS_EXE : this.ssDir + File.separator + MSVSSConstants.SS_EXE;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getVsspath() {
        return this.vssPath;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getQuiet() {
        return this.quiet ? MSVSSConstants.FLAG_QUIET : "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getRecursive() {
        return this.recursive ? MSVSSConstants.FLAG_RECURSION : "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getWritable() {
        return this.writable ? MSVSSConstants.FLAG_WRITABLE : "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getLabel() {
        String shortLabel = "";
        if (this.label != null && !this.label.isEmpty()) {
            shortLabel = MSVSSConstants.FLAG_LABEL + getShortLabel();
        }
        return shortLabel;
    }

    private String getShortLabel() {
        String shortLabel;
        if (this.label != null && this.label.length() > 31) {
            shortLabel = this.label.substring(0, 30);
            log("Label is longer than 31 characters, truncated to: " + shortLabel, 1);
        } else {
            shortLabel = this.label;
        }
        return shortLabel;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getStyle() {
        return this.style != null ? this.style : "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getVersionDateLabel() {
        String versionDateLabel = "";
        if (this.version != null) {
            versionDateLabel = MSVSSConstants.FLAG_VERSION + this.version;
        } else if (this.date != null) {
            versionDateLabel = MSVSSConstants.FLAG_VERSION_DATE + this.date;
        } else {
            String shortLabel = getShortLabel();
            if (shortLabel != null && !shortLabel.isEmpty()) {
                versionDateLabel = MSVSSConstants.FLAG_VERSION_LABEL + shortLabel;
            }
        }
        return versionDateLabel;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getVersion() {
        return this.version != null ? MSVSSConstants.FLAG_VERSION + this.version : "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getLocalpath() {
        String lclPath = "";
        if (this.localPath != null) {
            File dir = getProject().resolveFile(this.localPath);
            if (!dir.exists()) {
                boolean done = dir.mkdirs() || dir.exists();
                if (!done) {
                    String msg = "Directory " + this.localPath + " creation was not successful for an unknown reason";
                    throw new BuildException(msg, getLocation());
                }
                getProject().log("Created dir: " + dir.getAbsolutePath());
            }
            lclPath = MSVSSConstants.FLAG_OVERRIDE_WORKING_DIR + this.localPath;
        }
        return lclPath;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getComment() {
        return this.comment != null ? MSVSSConstants.FLAG_COMMENT + this.comment : "-C-";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getAutoresponse() {
        if (this.autoResponse == null) {
            return MSVSSConstants.FLAG_AUTORESPONSE_DEF;
        }
        if (this.autoResponse.equalsIgnoreCase("Y")) {
            return MSVSSConstants.FLAG_AUTORESPONSE_YES;
        }
        if (this.autoResponse.equalsIgnoreCase("N")) {
            return MSVSSConstants.FLAG_AUTORESPONSE_NO;
        }
        return MSVSSConstants.FLAG_AUTORESPONSE_DEF;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getLogin() {
        return this.vssLogin != null ? "-Y" + this.vssLogin : "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getOutput() {
        return this.outputFileName != null ? MSVSSConstants.FLAG_OUTPUT + this.outputFileName : "";
    }

    protected String getUser() {
        return this.user != null ? MSVSSConstants.FLAG_USER + this.user : "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getVersionLabel() {
        if (this.fromLabel == null && this.toLabel == null) {
            return "";
        }
        if (this.fromLabel != null && this.toLabel != null) {
            if (this.fromLabel.length() > 31) {
                this.fromLabel = this.fromLabel.substring(0, 30);
                log("FromLabel is longer than 31 characters, truncated to: " + this.fromLabel, 1);
            }
            if (this.toLabel.length() > 31) {
                this.toLabel = this.toLabel.substring(0, 30);
                log("ToLabel is longer than 31 characters, truncated to: " + this.toLabel, 1);
            }
            return MSVSSConstants.FLAG_VERSION_LABEL + this.toLabel + MSVSSConstants.VALUE_FROMLABEL + this.fromLabel;
        } else if (this.fromLabel != null) {
            if (this.fromLabel.length() > 31) {
                this.fromLabel = this.fromLabel.substring(0, 30);
                log("FromLabel is longer than 31 characters, truncated to: " + this.fromLabel, 1);
            }
            return "-V~L" + this.fromLabel;
        } else {
            if (this.toLabel.length() > 31) {
                this.toLabel = this.toLabel.substring(0, 30);
                log("ToLabel is longer than 31 characters, truncated to: " + this.toLabel, 1);
            }
            return MSVSSConstants.FLAG_VERSION_LABEL + this.toLabel;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getVersionDate() throws BuildException {
        if (this.fromDate == null && this.toDate == null && this.numDays == Integer.MIN_VALUE) {
            return "";
        }
        if (this.fromDate != null && this.toDate != null) {
            return MSVSSConstants.FLAG_VERSION_DATE + this.toDate + MSVSSConstants.VALUE_FROMDATE + this.fromDate;
        }
        if (this.toDate != null && this.numDays != Integer.MIN_VALUE) {
            try {
                return MSVSSConstants.FLAG_VERSION_DATE + this.toDate + MSVSSConstants.VALUE_FROMDATE + calcDate(this.toDate, this.numDays);
            } catch (ParseException e) {
                String msg = "Error parsing date: " + this.toDate;
                throw new BuildException(msg, getLocation());
            }
        } else if (this.fromDate == null || this.numDays == Integer.MIN_VALUE) {
            return this.fromDate != null ? "-V~d" + this.fromDate : MSVSSConstants.FLAG_VERSION_DATE + this.toDate;
        } else {
            try {
                return MSVSSConstants.FLAG_VERSION_DATE + calcDate(this.fromDate, this.numDays) + MSVSSConstants.VALUE_FROMDATE + this.fromDate;
            } catch (ParseException e2) {
                String msg2 = "Error parsing date: " + this.fromDate;
                throw new BuildException(msg2, getLocation());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getGetLocalCopy() {
        return !this.getLocalCopy ? MSVSSConstants.FLAG_NO_GET : "";
    }

    private boolean getFailOnError() {
        return !getWritableFiles().equals(MSVSSConstants.WRITABLE_SKIP) && this.failOnError;
    }

    public String getFileTimeStamp() {
        if (this.timestamp == null) {
            return "";
        }
        if (this.timestamp.getValue().equals(MSVSSConstants.TIME_MODIFIED)) {
            return MSVSSConstants.FLAG_FILETIME_MODIFIED;
        }
        if (this.timestamp.getValue().equals(MSVSSConstants.TIME_UPDATED)) {
            return MSVSSConstants.FLAG_FILETIME_UPDATED;
        }
        return MSVSSConstants.FLAG_FILETIME_DEF;
    }

    public String getWritableFiles() {
        if (this.writableFiles == null) {
            return "";
        }
        if (this.writableFiles.getValue().equals(MSVSSConstants.WRITABLE_REPLACE)) {
            return MSVSSConstants.FLAG_REPLACE_WRITABLE;
        }
        if (this.writableFiles.getValue().equals(MSVSSConstants.WRITABLE_SKIP)) {
            this.failOnError = false;
            return MSVSSConstants.FLAG_SKIP_WRITABLE;
        }
        return "";
    }

    private int run(Commandline cmd) {
        try {
            Execute exe = new Execute(new LogStreamHandler((Task) this, 2, 1));
            if (this.serverPath != null) {
                String[] env = exe.getEnvironment();
                if (env == null) {
                    env = new String[0];
                }
                String[] newEnv = new String[env.length + 1];
                System.arraycopy(env, 0, newEnv, 0, env.length);
                newEnv[env.length] = "SSDIR=" + this.serverPath;
                exe.setEnvironment(newEnv);
            }
            exe.setAntRun(getProject());
            exe.setWorkingDirectory(getProject().getBaseDir());
            exe.setCommandline(cmd.getCommandline());
            exe.setVMLauncher(false);
            return exe.execute();
        } catch (IOException e) {
            throw new BuildException(e, getLocation());
        }
    }

    private String calcDate(String startDate, int daysToAdd) throws ParseException {
        Calendar calendar = new GregorianCalendar();
        Date currentDate = this.dateFormat.parse(startDate);
        calendar.setTime(currentDate);
        calendar.add(5, daysToAdd);
        return this.dateFormat.format(calendar.getTime());
    }

    private String formatCommandLine(Commandline cmd) {
        StringBuffer sBuff = new StringBuffer(cmd.toString());
        int indexUser = sBuff.substring(0).indexOf("-Y");
        if (indexUser > 0) {
            int indexPass = sBuff.substring(0).indexOf(",", indexUser);
            int indexAfterPass = sBuff.substring(0).indexOf(Instruction.argsep, indexPass);
            for (int i = indexPass + 1; i < indexAfterPass; i++) {
                sBuff.setCharAt(i, '*');
            }
        }
        return sBuff.toString();
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/vss/MSVSS$CurrentModUpdated.class */
    public static class CurrentModUpdated extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{MSVSSConstants.TIME_CURRENT, MSVSSConstants.TIME_MODIFIED, MSVSSConstants.TIME_UPDATED};
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/vss/MSVSS$WritableFiles.class */
    public static class WritableFiles extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{MSVSSConstants.WRITABLE_REPLACE, MSVSSConstants.WRITABLE_SKIP, "fail"};
        }
    }
}
