package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/MakeUrl.class */
public class MakeUrl extends Task {
    public static final String ERROR_MISSING_FILE = "A source file is missing: ";
    public static final String ERROR_NO_PROPERTY = "No property defined";
    public static final String ERROR_NO_FILES = "No files defined";
    private String property;
    private File file;
    private String separator = Instruction.argsep;
    private List<FileSet> filesets = new LinkedList();
    private List<Path> paths = new LinkedList();
    private boolean validate = true;

    public void setProperty(String property) {
        this.property = property;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void addFileSet(FileSet fileset) {
        this.filesets.add(fileset);
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public void addPath(Path path) {
        this.paths.add(path);
    }

    private String filesetsToURL() {
        String[] includedFiles;
        if (this.filesets.isEmpty()) {
            return "";
        }
        int count = 0;
        StringBuilder urls = new StringBuilder();
        for (FileSet fs : this.filesets) {
            DirectoryScanner scanner = fs.getDirectoryScanner(getProject());
            for (String file : scanner.getIncludedFiles()) {
                File f = new File(scanner.getBasedir(), file);
                validateFile(f);
                String asUrl = toURL(f);
                urls.append(asUrl);
                log(asUrl, 4);
                urls.append(this.separator);
                count++;
            }
        }
        return stripTrailingSeparator(urls, count);
    }

    private String stripTrailingSeparator(StringBuilder urls, int count) {
        if (count > 0) {
            urls.delete(urls.length() - this.separator.length(), urls.length());
            return new String(urls);
        }
        return "";
    }

    private String pathsToURL() {
        String[] list;
        if (this.paths.isEmpty()) {
            return "";
        }
        int count = 0;
        StringBuilder urls = new StringBuilder();
        for (Path path : this.paths) {
            for (String element : path.list()) {
                File f = new File(element);
                validateFile(f);
                String asUrl = toURL(f);
                urls.append(asUrl);
                log(asUrl, 4);
                urls.append(this.separator);
                count++;
            }
        }
        return stripTrailingSeparator(urls, count);
    }

    private void validateFile(File fileToCheck) {
        if (this.validate && !fileToCheck.exists()) {
            throw new BuildException(ERROR_MISSING_FILE + fileToCheck);
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        String url;
        validate();
        if (getProject().getProperty(this.property) != null) {
            return;
        }
        String filesetURL = filesetsToURL();
        if (this.file == null) {
            url = filesetURL;
        } else {
            validateFile(this.file);
            url = toURL(this.file);
            if (!filesetURL.isEmpty()) {
                url = url + this.separator + filesetURL;
            }
        }
        String pathURL = pathsToURL();
        if (!pathURL.isEmpty()) {
            if (url.isEmpty()) {
                url = pathURL;
            } else {
                url = url + this.separator + pathURL;
            }
        }
        log("Setting " + this.property + " to URL " + url, 3);
        getProject().setNewProperty(this.property, url);
    }

    private void validate() {
        if (this.property == null) {
            throw new BuildException(ERROR_NO_PROPERTY);
        }
        if (this.file == null && this.filesets.isEmpty() && this.paths.isEmpty()) {
            throw new BuildException(ERROR_NO_FILES);
        }
    }

    private String toURL(File fileToConvert) {
        return FileUtils.getFileUtils().toURI(fileToConvert.getAbsolutePath());
    }
}
