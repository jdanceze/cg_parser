package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.taskdefs.optional.ejb.IPlanetEjbc;
import org.apache.tools.ant.types.Path;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/IPlanetEjbcTask.class */
public class IPlanetEjbcTask extends Task {
    private File ejbdescriptor;
    private File iasdescriptor;
    private File dest;
    private Path classpath;
    private boolean keepgenerated = false;
    private boolean debug = false;
    private File iashome;

    public void setEjbdescriptor(File ejbdescriptor) {
        this.ejbdescriptor = ejbdescriptor;
    }

    public void setIasdescriptor(File iasdescriptor) {
        this.iasdescriptor = iasdescriptor;
    }

    public void setDest(File dest) {
        this.dest = dest;
    }

    public void setClasspath(Path classpath) {
        if (this.classpath == null) {
            this.classpath = classpath;
        } else {
            this.classpath.append(classpath);
        }
    }

    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(getProject());
        }
        return this.classpath.createPath();
    }

    public void setKeepgenerated(boolean keepgenerated) {
        this.keepgenerated = keepgenerated;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setIashome(File iashome) {
        this.iashome = iashome;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        checkConfiguration();
        executeEjbc(getParser());
    }

    private void checkConfiguration() throws BuildException {
        if (this.ejbdescriptor == null) {
            throw new BuildException("The standard EJB descriptor must be specified using the \"ejbdescriptor\" attribute.", getLocation());
        }
        if (!this.ejbdescriptor.exists() || !this.ejbdescriptor.isFile()) {
            String msg = "The standard EJB descriptor (" + this.ejbdescriptor + ") was not found or isn't a file.";
            throw new BuildException(msg, getLocation());
        } else if (this.iasdescriptor == null) {
            throw new BuildException("The iAS-speific XML descriptor must be specified using the \"iasdescriptor\" attribute.", getLocation());
        } else {
            if (!this.iasdescriptor.exists() || !this.iasdescriptor.isFile()) {
                String msg2 = "The iAS-specific XML descriptor (" + this.iasdescriptor + ") was not found or isn't a file.";
                throw new BuildException(msg2, getLocation());
            } else if (this.dest == null) {
                throw new BuildException("The destination directory must be specified using the \"dest\" attribute.", getLocation());
            } else {
                if (!this.dest.exists() || !this.dest.isDirectory()) {
                    String msg3 = "The destination directory (" + this.dest + ") was not found or isn't a directory.";
                    throw new BuildException(msg3, getLocation());
                } else if (this.iashome != null && !this.iashome.isDirectory()) {
                    String msg4 = "If \"iashome\" is specified, it must be a valid directory (it was set to " + this.iashome + ").";
                    throw new BuildException(msg4, getLocation());
                }
            }
        }
    }

    private SAXParser getParser() throws BuildException {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setValidating(true);
            return saxParserFactory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new BuildException("Unable to create a SAXParser: " + e.getMessage(), e, getLocation());
        }
    }

    private void executeEjbc(SAXParser saxParser) throws BuildException {
        IPlanetEjbc ejbc = new IPlanetEjbc(this.ejbdescriptor, this.iasdescriptor, this.dest, getClasspath().toString(), saxParser);
        ejbc.setRetainSource(this.keepgenerated);
        ejbc.setDebugOutput(this.debug);
        if (this.iashome != null) {
            ejbc.setIasHomeDir(this.iashome);
        }
        try {
            ejbc.execute();
        } catch (IOException e) {
            throw new BuildException("An IOException occurred while trying to read the XML descriptor file: " + e.getMessage(), e, getLocation());
        } catch (IPlanetEjbc.EjbcException e2) {
            throw new BuildException("An exception occurred while trying to run the ejbc utility: " + e2.getMessage(), e2, getLocation());
        } catch (SAXException e3) {
            throw new BuildException("A SAXException occurred while trying to read the XML descriptor file: " + e3.getMessage(), e3, getLocation());
        }
    }

    private Path getClasspath() {
        if (this.classpath == null) {
            return new Path(getProject()).concatSystemClasspath("last");
        }
        return this.classpath.concatSystemClasspath(Definer.OnError.POLICY_IGNORE);
    }
}
