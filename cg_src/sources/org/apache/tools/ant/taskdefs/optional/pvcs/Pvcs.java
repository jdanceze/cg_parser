package org.apache.tools.ant.taskdefs.optional.pvcs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import org.apache.http.message.BasicHeaderValueFormatter;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/pvcs/Pvcs.class */
public class Pvcs extends Task {
    private static final int POS_1 = 1;
    private static final int POS_2 = 2;
    private static final int POS_3 = 3;
    private static final String PCLI_EXE = "pcli";
    private static final String GET_EXE = "get";
    private String revision;
    private String userId;
    private String config;
    private String pvcsProject = null;
    private Vector<PvcsProject> pvcsProjects = new Vector<>();
    private String workspace = null;
    private String repository = null;
    private String pvcsbin = null;
    private String force = null;
    private String promotiongroup = null;
    private String label = null;
    private boolean ignorerc = false;
    private boolean updateOnly = false;
    private String lineStart = "\"P:";
    private String filenameFormat = "{0}-arc({1})";

    protected int runCmd(Commandline cmd, ExecuteStreamHandler out) {
        try {
            Project aProj = getProject();
            Execute exe = new Execute(out);
            exe.setAntRun(aProj);
            exe.setWorkingDirectory(aProj.getBaseDir());
            exe.setCommandline(cmd.getCommandline());
            return exe.execute();
        } catch (IOException e) {
            String msg = "Failed executing: " + cmd.toString() + ". Exception: " + e.getMessage();
            throw new BuildException(msg, getLocation());
        }
    }

    private String getExecutable(String exe) {
        StringBuilder correctedExe = new StringBuilder();
        if (getPvcsbin() != null) {
            if (this.pvcsbin.endsWith(File.separator)) {
                correctedExe.append(this.pvcsbin);
            } else {
                correctedExe.append(this.pvcsbin).append(File.separator);
            }
        }
        return correctedExe.append(exe).toString();
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.repository == null || this.repository.trim().isEmpty()) {
            throw new BuildException("Required argument repository not specified");
        }
        Commandline commandLine = new Commandline();
        commandLine.setExecutable(getExecutable(PCLI_EXE));
        commandLine.createArgument().setValue("lvf");
        commandLine.createArgument().setValue("-z");
        commandLine.createArgument().setValue("-aw");
        if (getWorkspace() != null) {
            commandLine.createArgument().setValue("-sp" + getWorkspace());
        }
        commandLine.createArgument().setValue("-pr" + getRepository());
        String uid = getUserId();
        if (uid != null) {
            commandLine.createArgument().setValue("-id" + uid);
        }
        if (getPvcsproject() == null && getPvcsprojects().isEmpty()) {
            this.pvcsProject = "/";
        }
        if (getPvcsproject() != null) {
            commandLine.createArgument().setValue(getPvcsproject());
        }
        if (!getPvcsprojects().isEmpty()) {
            Iterator<PvcsProject> it = getPvcsprojects().iterator();
            while (it.hasNext()) {
                PvcsProject pvcsProject = it.next();
                String projectName = pvcsProject.getName();
                if (projectName == null || projectName.trim().isEmpty()) {
                    throw new BuildException("name is a required attribute of pvcsproject");
                }
                commandLine.createArgument().setValue(projectName);
            }
        }
        File tmp = null;
        File tmp2 = null;
        try {
            try {
                Random rand = new Random(System.currentTimeMillis());
                tmp = new File("pvcs_ant_" + rand.nextLong() + ".log");
                OutputStream fos = Files.newOutputStream(tmp.toPath(), new OpenOption[0]);
                tmp2 = new File("pvcs_ant_" + rand.nextLong() + ".log");
                log(commandLine.describeCommand(), 3);
                try {
                    int result = runCmd(commandLine, new PumpStreamHandler(fos, new LogOutputStream((Task) this, 1)));
                    FileUtils.close(fos);
                    if (Execute.isFailure(result) && !this.ignorerc) {
                        String msg = "Failed executing: " + commandLine.toString();
                        throw new BuildException(msg, getLocation());
                    } else if (!tmp.exists()) {
                        throw new BuildException("Communication between ant and pvcs failed. No output generated from executing PVCS commandline interface \"pcli\" and \"get\"");
                    } else {
                        log("Creating folders", 2);
                        createFolders(tmp);
                        massagePCLI(tmp, tmp2);
                        commandLine.clearArgs();
                        commandLine.setExecutable(getExecutable(GET_EXE));
                        if (getConfig() != null && !getConfig().isEmpty()) {
                            commandLine.createArgument().setValue("-c" + getConfig());
                        }
                        if (getForce() != null && getForce().equals("yes")) {
                            commandLine.createArgument().setValue("-Y");
                        } else {
                            commandLine.createArgument().setValue(MSVSSConstants.VALUE_NO);
                        }
                        if (getPromotiongroup() != null) {
                            commandLine.createArgument().setValue("-G" + getPromotiongroup());
                        } else if (getLabel() != null) {
                            commandLine.createArgument().setValue("-v" + getLabel());
                        } else if (getRevision() != null) {
                            commandLine.createArgument().setValue("-r" + getRevision());
                        }
                        if (this.updateOnly) {
                            commandLine.createArgument().setValue(MSVSSConstants.FLAG_USER);
                        }
                        commandLine.createArgument().setValue("@" + tmp2.getAbsolutePath());
                        log("Getting files", 2);
                        log("Executing " + commandLine.toString(), 3);
                        int result2 = runCmd(commandLine, new LogStreamHandler((Task) this, 2, 1));
                        if (result2 != 0 && !this.ignorerc) {
                            String msg2 = "Failed executing: " + commandLine.toString() + ". Return code was " + result2;
                            throw new BuildException(msg2, getLocation());
                        }
                    }
                } catch (Throwable th) {
                    FileUtils.close(fos);
                    throw th;
                }
            } catch (IOException | ParseException e) {
                String msg3 = "Failed executing: " + commandLine.toString() + ". Exception: " + e.getMessage();
                throw new BuildException(msg3, getLocation());
            }
        } finally {
            if (tmp != null) {
                tmp.delete();
            }
            if (tmp2 != null) {
                tmp2.delete();
            }
        }
    }

    private void createFolders(File file) throws IOException, ParseException {
        BufferedReader in = new BufferedReader(new FileReader(file));
        try {
            MessageFormat mf = new MessageFormat(getFilenameFormat());
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                log("Considering \"" + line + "\"", 3);
                if (line.startsWith(BasicHeaderValueFormatter.UNSAFE_CHARS) || line.startsWith("\"/") || (line.length() > 3 && line.startsWith("\"") && Character.isLetter(line.charAt(1)) && String.valueOf(line.charAt(2)).equals(":") && String.valueOf(line.charAt(3)).equals("\\"))) {
                    Object[] objs = mf.parse(line);
                    String f = (String) objs[1];
                    int index = f.lastIndexOf(File.separator);
                    if (index > -1) {
                        File dir = new File(f.substring(0, index));
                        if (dir.exists()) {
                            log(dir.getAbsolutePath() + " exists. Skipping", 3);
                        } else {
                            log("Creating " + dir.getAbsolutePath(), 3);
                            if (dir.mkdirs() || dir.isDirectory()) {
                                log("Created " + dir.getAbsolutePath(), 2);
                            } else {
                                log("Failed to create " + dir.getAbsolutePath(), 2);
                            }
                        }
                    } else {
                        log("File separator problem with " + line, 1);
                    }
                } else {
                    log("Skipped \"" + line + "\"", 3);
                }
            }
            in.close();
        } catch (Throwable th) {
            try {
                in.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private void massagePCLI(File in, File out) throws IOException {
        BufferedReader inReader = new BufferedReader(new FileReader(in));
        try {
            BufferedWriter outWriter = new BufferedWriter(new FileWriter(out));
            for (String line : () -> {
                return inReader.lines().map(s -> {
                    return s.replace('\\', '/');
                }).iterator();
            }) {
                outWriter.write(line);
                outWriter.newLine();
            }
            outWriter.close();
            inReader.close();
        } catch (Throwable th) {
            try {
                inReader.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public String getRepository() {
        return this.repository;
    }

    public String getFilenameFormat() {
        return this.filenameFormat;
    }

    public void setFilenameFormat(String f) {
        this.filenameFormat = f;
    }

    public String getLineStart() {
        return this.lineStart;
    }

    public void setLineStart(String l) {
        this.lineStart = l;
    }

    public void setRepository(String repo) {
        this.repository = repo;
    }

    public String getPvcsproject() {
        return this.pvcsProject;
    }

    public void setPvcsproject(String prj) {
        this.pvcsProject = prj;
    }

    public Vector<PvcsProject> getPvcsprojects() {
        return this.pvcsProjects;
    }

    public String getWorkspace() {
        return this.workspace;
    }

    public void setWorkspace(String ws) {
        this.workspace = ws;
    }

    public String getPvcsbin() {
        return this.pvcsbin;
    }

    public void setPvcsbin(String bin) {
        this.pvcsbin = bin;
    }

    public String getForce() {
        return this.force;
    }

    public void setForce(String f) {
        this.force = "yes".equalsIgnoreCase(f) ? "yes" : "no";
    }

    public String getPromotiongroup() {
        return this.promotiongroup;
    }

    public void setPromotiongroup(String w) {
        this.promotiongroup = w;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String l) {
        this.label = l;
    }

    public String getRevision() {
        return this.revision;
    }

    public void setRevision(String r) {
        this.revision = r;
    }

    public boolean getIgnoreReturnCode() {
        return this.ignorerc;
    }

    public void setIgnoreReturnCode(boolean b) {
        this.ignorerc = b;
    }

    public void addPvcsproject(PvcsProject p) {
        this.pvcsProjects.addElement(p);
    }

    public boolean getUpdateOnly() {
        return this.updateOnly;
    }

    public void setUpdateOnly(boolean l) {
        this.updateOnly = l;
    }

    public String getConfig() {
        return this.config;
    }

    public void setConfig(File f) {
        this.config = f.toString();
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String u) {
        this.userId = u;
    }
}
