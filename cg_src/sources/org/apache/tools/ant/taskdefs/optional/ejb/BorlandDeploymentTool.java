package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.taskdefs.optional.ejb.EjbJar;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/BorlandDeploymentTool.class */
public class BorlandDeploymentTool extends GenericDeploymentTool implements ExecuteStreamHandler {
    public static final String PUBLICID_BORLAND_EJB = "-//Inprise Corporation//DTD Enterprise JavaBeans 1.1//EN";
    protected static final String DEFAULT_BAS45_EJB11_DTD_LOCATION = "/com/inprise/j2ee/xml/dtds/ejb-jar.dtd";
    protected static final String DEFAULT_BAS_DTD_LOCATION = "/com/inprise/j2ee/xml/dtds/ejb-inprise.dtd";
    protected static final String BAS_DD = "ejb-inprise.xml";
    protected static final String BES_DD = "ejb-borland.xml";
    protected static final String JAVA2IIOP = "java2iiop";
    protected static final String VERIFY = "com.inprise.ejb.util.Verify";
    private String borlandDTD;
    static final int BES = 5;
    static final int BAS = 4;
    private String jarSuffix = "-ejb.jar";
    private boolean java2iiopdebug = false;
    private String java2iioparams = null;
    private boolean generateclient = false;
    private int version = 4;
    private boolean verify = true;
    private String verifyArgs = "";
    private Map<String, File> genfiles = new Hashtable();

    public void setDebug(boolean debug) {
        this.java2iiopdebug = debug;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }

    public void setSuffix(String inString) {
        this.jarSuffix = inString;
    }

    public void setVerifyArgs(String args) {
        this.verifyArgs = args;
    }

    public void setBASdtd(String inString) {
        this.borlandDTD = inString;
    }

    public void setGenerateclient(boolean b) {
        this.generateclient = b;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setJava2iiopParams(String params) {
        this.java2iioparams = params;
    }

    protected DescriptorHandler getBorlandDescriptorHandler(final File srcDir) {
        DescriptorHandler handler = new DescriptorHandler(getTask(), srcDir) { // from class: org.apache.tools.ant.taskdefs.optional.ejb.BorlandDeploymentTool.1
            @Override // org.apache.tools.ant.taskdefs.optional.ejb.DescriptorHandler
            protected void processElement() {
                if ("type-storage".equals(this.currentElement)) {
                    this.ejbFiles.put(this.currentText, new File(srcDir, this.currentText.substring("META-INF/".length())));
                }
            }
        };
        handler.registerDTD(PUBLICID_BORLAND_EJB, this.borlandDTD == null ? DEFAULT_BAS_DTD_LOCATION : this.borlandDTD);
        Iterator<EjbJar.DTDLocation> it = getConfig().dtdLocations.iterator();
        while (it.hasNext()) {
            EjbJar.DTDLocation dtdLocation = it.next();
            handler.registerDTD(dtdLocation.getPublicId(), dtdLocation.getLocation());
        }
        return handler;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    protected void addVendorFiles(Hashtable<String, File> ejbFiles, String ddPrefix) {
        if (this.version != 5 && this.version != 4) {
            throw new BuildException("version " + this.version + " is not supported");
        }
        String dd = this.version == 5 ? BES_DD : BAS_DD;
        log("vendor file : " + ddPrefix + dd, 4);
        File borlandDD = new File(getConfig().descriptorDir, ddPrefix + dd);
        if (borlandDD.exists()) {
            log("Borland specific file found " + borlandDD, 3);
            ejbFiles.put("META-INF/" + dd, borlandDD);
            return;
        }
        log("Unable to locate borland deployment descriptor. It was expected to be in " + borlandDD.getPath(), 1);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    File getVendorOutputJarFile(String baseName) {
        return new File(getDestDir(), baseName + this.jarSuffix);
    }

    private void verifyBorlandJar(File sourceJar) {
        if (this.version == 4) {
            verifyBorlandJarV4(sourceJar);
        } else if (this.version == 5) {
            verifyBorlandJarV5(sourceJar);
        } else {
            log("verify jar skipped because the version is invalid [" + this.version + "]", 1);
        }
    }

    private void verifyBorlandJarV5(File sourceJar) {
        log("verify BES " + sourceJar, 2);
        try {
            ExecTask execTask = new ExecTask(getTask());
            execTask.setDir(new File("."));
            execTask.setExecutable("iastool");
            if (getCombinedClasspath() != null) {
                execTask.createArg().setValue("-VBJclasspath");
                execTask.createArg().setValue(getCombinedClasspath().toString());
            }
            if (this.java2iiopdebug) {
                execTask.createArg().setValue("-debug");
            }
            execTask.createArg().setValue("-verify");
            execTask.createArg().setValue("-src");
            execTask.createArg().setValue(sourceJar.getPath());
            log("Calling iastool", 3);
            execTask.execute();
        } catch (Exception e) {
            throw new BuildException("Exception while calling generateclient Details: ", e);
        }
    }

    private void verifyBorlandJarV4(File sourceJar) {
        log("verify BAS " + sourceJar, 2);
        try {
            String args = this.verifyArgs;
            String args2 = args + Instruction.argsep + sourceJar.getPath();
            Java javaTask = new Java(getTask());
            javaTask.setTaskName("verify");
            javaTask.setClassname(VERIFY);
            Commandline.Argument arguments = javaTask.createArg();
            arguments.setLine(args2);
            Path classpath = getCombinedClasspath();
            if (classpath != null) {
                javaTask.setClasspath(classpath);
                javaTask.setFork(true);
            }
            log("Calling com.inprise.ejb.util.Verify for " + sourceJar.toString(), 3);
            javaTask.execute();
        } catch (Exception e) {
            String msg = "Exception while calling com.inprise.ejb.util.Verify Details: " + e.toString();
            throw new BuildException(msg, e);
        }
    }

    private void generateClient(File sourceJar) {
        getTask().getProject().addTaskDefinition("internal_bas_generateclient", BorlandGenerateClient.class);
        log("generate client for " + sourceJar, 2);
        try {
            Project project = getTask().getProject();
            BorlandGenerateClient gentask = (BorlandGenerateClient) project.createTask("internal_bas_generateclient");
            gentask.setEjbjar(sourceJar);
            gentask.setDebug(this.java2iiopdebug);
            Path classpath = getCombinedClasspath();
            if (classpath != null) {
                gentask.setClasspath(classpath);
            }
            gentask.setVersion(this.version);
            gentask.setTaskName("generate client");
            gentask.execute();
        } catch (Exception e) {
            throw new BuildException("Exception while calling com.inprise.ejb.util.Verify", e);
        }
    }

    private void buildBorlandStubs(Collection<String> ithomes) {
        Execute execTask = new Execute(this);
        Project project = getTask().getProject();
        execTask.setAntRun(project);
        execTask.setWorkingDirectory(project.getBaseDir());
        Commandline commandline = new Commandline();
        commandline.setExecutable(JAVA2IIOP);
        if (this.java2iiopdebug) {
            commandline.createArgument().setValue("-VBJdebug");
        }
        commandline.createArgument().setValue("-VBJclasspath");
        commandline.createArgument().setPath(getCombinedClasspath());
        commandline.createArgument().setValue("-list_files");
        commandline.createArgument().setValue("-no_tie");
        if (this.java2iioparams != null) {
            log("additional  " + this.java2iioparams + " to java2iiop ", 0);
            commandline.createArgument().setLine(this.java2iioparams);
        }
        commandline.createArgument().setValue("-root_dir");
        commandline.createArgument().setValue(getConfig().srcDir.getAbsolutePath());
        commandline.createArgument().setValue("-compile");
        ithomes.stream().map((v0) -> {
            return v0.toString();
        }).forEach(v -> {
            commandline.createArgument().setValue(v);
        });
        try {
            log("Calling java2iiop", 3);
            log(commandline.describeCommand(), 4);
            execTask.setCommandline(commandline.getCommandline());
            int result = execTask.execute();
            if (Execute.isFailure(result)) {
                throw new BuildException("Failed executing java2iiop (ret code is " + result + ")", getTask().getLocation());
            }
        } catch (IOException e) {
            log("java2iiop exception :" + e.getMessage(), 0);
            throw new BuildException(e, getTask().getLocation());
        }
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    protected void writeJar(String baseName, File jarFile, Hashtable<String, File> files, String publicId) throws BuildException {
        List<String> homes = new ArrayList<>();
        for (String clazz : files.keySet()) {
            if (clazz.endsWith("Home.class")) {
                String home = toClass(clazz);
                homes.add(home);
                log(" Home " + home, 3);
            }
        }
        buildBorlandStubs(homes);
        files.putAll(this.genfiles);
        super.writeJar(baseName, jarFile, files, publicId);
        if (this.verify) {
            verifyBorlandJar(jarFile);
        }
        if (this.generateclient) {
            generateClient(jarFile);
        }
        this.genfiles.clear();
    }

    private String toClass(String filename) {
        return filename.substring(0, filename.lastIndexOf(".class")).replace('\\', '.').replace('/', '.');
    }

    private String toClassFile(String filename) {
        return filename.replaceFirst("\\.java$", ".class");
    }

    @Override // org.apache.tools.ant.taskdefs.ExecuteStreamHandler
    public void start() throws IOException {
    }

    @Override // org.apache.tools.ant.taskdefs.ExecuteStreamHandler
    public void stop() {
    }

    @Override // org.apache.tools.ant.taskdefs.ExecuteStreamHandler
    public void setProcessInputStream(OutputStream param1) throws IOException {
    }

    @Override // org.apache.tools.ant.taskdefs.ExecuteStreamHandler
    public void setProcessOutputStream(InputStream is) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while (true) {
                String javafile = reader.readLine();
                if (javafile != null) {
                    if (javafile.endsWith(".java")) {
                        String classfile = toClassFile(javafile);
                        String key = classfile.substring(getConfig().srcDir.getAbsolutePath().length() + 1);
                        this.genfiles.put(key, new File(classfile));
                    }
                } else {
                    reader.close();
                    return;
                }
            }
        } catch (Exception e) {
            throw new BuildException("Exception while parsing java2iiop output.", e);
        }
    }

    @Override // org.apache.tools.ant.taskdefs.ExecuteStreamHandler
    public void setProcessErrorStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String s = reader.readLine();
        if (s != null) {
            log("[java2iiop] " + s, 0);
        }
    }
}
