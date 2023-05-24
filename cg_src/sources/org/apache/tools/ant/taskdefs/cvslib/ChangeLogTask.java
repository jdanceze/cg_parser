package org.apache.tools.ant.taskdefs.cvslib;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.AbstractCvsTask;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.util.DateUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/cvslib/ChangeLogTask.class */
public class ChangeLogTask extends AbstractCvsTask {
    private File usersFile;
    private File inputDir;
    private File destFile;
    private Date startDate;
    private Date endDate;
    private String startTag;
    private String endTag;
    private List<CvsUser> cvsUsers = new Vector();
    private boolean remote = false;
    private final List<FileSet> filesets = new Vector();

    public void setDir(File inputDir) {
        this.inputDir = inputDir;
    }

    public void setDestfile(File destFile) {
        this.destFile = destFile;
    }

    public void setUsersfile(File usersFile) {
        this.usersFile = usersFile;
    }

    public void addUser(CvsUser user) {
        this.cvsUsers.add(user);
    }

    public void setStart(Date start) {
        this.startDate = start;
    }

    public void setEnd(Date endDate) {
        this.endDate = endDate;
    }

    public void setDaysinpast(int days) {
        long time = System.currentTimeMillis() - ((((days * 24) * 60) * 60) * 1000);
        setStart(new Date(time));
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public void setStartTag(String start) {
        this.startTag = start;
    }

    public void setEndTag(String end) {
        this.endTag = end;
    }

    public void addFileset(FileSet fileSet) {
        this.filesets.add(fileSet);
    }

    @Override // org.apache.tools.ant.taskdefs.AbstractCvsTask, org.apache.tools.ant.Task
    public void execute() throws BuildException {
        String[] includedFiles;
        File savedDir = this.inputDir;
        try {
            validate();
            Properties userList = new Properties();
            loadUserlist(userList);
            for (CvsUser user : this.cvsUsers) {
                user.validate();
                userList.put(user.getUserID(), user.getDisplayname());
            }
            if (!this.remote) {
                setCommand("log");
                if (getTag() != null) {
                    CvsVersion myCvsVersion = new CvsVersion();
                    myCvsVersion.setProject(getProject());
                    myCvsVersion.setTaskName("cvsversion");
                    myCvsVersion.setCvsRoot(getCvsRoot());
                    myCvsVersion.setCvsRsh(getCvsRsh());
                    myCvsVersion.setPassfile(getPassFile());
                    myCvsVersion.setDest(this.inputDir);
                    myCvsVersion.execute();
                    if (myCvsVersion.supportsCvsLogWithSOption()) {
                        addCommandArgument("-S");
                    }
                }
            } else {
                setCommand("");
                addCommandArgument("rlog");
                addCommandArgument("-S");
                addCommandArgument(MSVSSConstants.VALUE_NO);
            }
            if (null != this.startTag || null != this.endTag) {
                String startValue = this.startTag == null ? "" : this.startTag;
                String endValue = this.endTag == null ? "" : this.endTag;
                addCommandArgument("-r" + startValue + "::" + endValue);
            } else if (null != this.startDate) {
                SimpleDateFormat outputDate = new SimpleDateFormat(DateUtils.ISO8601_DATE_PATTERN);
                String dateRange = ">=" + outputDate.format(this.startDate);
                addCommandArgument("-d");
                addCommandArgument(dateRange);
            }
            for (FileSet fileSet : this.filesets) {
                DirectoryScanner scanner = fileSet.getDirectoryScanner(getProject());
                for (String file : scanner.getIncludedFiles()) {
                    addCommandArgument(file);
                }
            }
            ChangeLogParser parser = new ChangeLogParser(this.remote, getPackage(), getModules());
            RedirectingStreamHandler handler = new RedirectingStreamHandler(parser);
            log(getCommand(), 3);
            setDest(this.inputDir);
            setExecuteStreamHandler(handler);
            super.execute();
            String errors = handler.getErrors();
            if (null != errors) {
                log(errors, 0);
            }
            CVSEntry[] entrySet = parser.getEntrySetAsArray();
            CVSEntry[] filteredEntrySet = filterEntrySet(entrySet);
            replaceAuthorIdWithName(userList, filteredEntrySet);
            writeChangeLog(filteredEntrySet);
            this.inputDir = savedDir;
        } catch (Throwable th) {
            this.inputDir = savedDir;
            throw th;
        }
    }

    private void validate() throws BuildException {
        if (null == this.inputDir) {
            this.inputDir = getProject().getBaseDir();
        }
        if (null == this.destFile) {
            throw new BuildException("Destfile must be set.");
        }
        if (!this.inputDir.exists()) {
            throw new BuildException("Cannot find base dir %s", this.inputDir.getAbsolutePath());
        }
        if (null != this.usersFile && !this.usersFile.exists()) {
            throw new BuildException("Cannot find user lookup list %s", this.usersFile.getAbsolutePath());
        }
        if (null == this.startTag && null == this.endTag) {
            return;
        }
        if (null != this.startDate || null != this.endDate) {
            throw new BuildException("Specify either a tag or date range, not both");
        }
    }

    private void loadUserlist(Properties userList) throws BuildException {
        if (null != this.usersFile) {
            try {
                userList.load(Files.newInputStream(this.usersFile.toPath(), new OpenOption[0]));
            } catch (IOException ioe) {
                throw new BuildException(ioe.toString(), ioe);
            }
        }
    }

    private CVSEntry[] filterEntrySet(CVSEntry[] entrySet) {
        List<CVSEntry> results = new ArrayList<>();
        for (CVSEntry cvsEntry : entrySet) {
            Date date = cvsEntry.getDate();
            if (null != date && ((null == this.startDate || !this.startDate.after(date)) && (null == this.endDate || !this.endDate.before(date)))) {
                results.add(cvsEntry);
            }
        }
        return (CVSEntry[]) results.toArray(new CVSEntry[results.size()]);
    }

    private void replaceAuthorIdWithName(Properties userList, CVSEntry[] entrySet) {
        for (CVSEntry entry : entrySet) {
            if (userList.containsKey(entry.getAuthor())) {
                entry.setAuthor(userList.getProperty(entry.getAuthor()));
            }
        }
    }

    private void writeChangeLog(CVSEntry[] entrySet) throws BuildException {
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(this.destFile.toPath(), new OpenOption[0]), StandardCharsets.UTF_8));
            try {
                new ChangeLogWriter().printChangeLog(writer, entrySet);
                if (writer.checkError()) {
                    throw new IOException("Encountered an error writing changelog");
                }
                writer.close();
            } catch (Throwable th) {
                try {
                    writer.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (UnsupportedEncodingException uee) {
            getProject().log(uee.toString(), 0);
        } catch (IOException ioe) {
            throw new BuildException(ioe.toString(), ioe);
        }
    }
}
