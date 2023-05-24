package soot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.xml.TagCollector;
/* loaded from: gencallgraphv3.jar:soot/XMLAttributesPrinter.class */
public class XMLAttributesPrinter {
    private static final Logger logger = LoggerFactory.getLogger(XMLAttributesPrinter.class);
    private String useFilename;
    private String outputDir;
    private FileOutputStream streamOut = null;
    private PrintWriter writerOut = null;

    private void setOutputDir(String dir) {
        this.outputDir = dir;
    }

    private String getOutputDir() {
        return this.outputDir;
    }

    public XMLAttributesPrinter(String filename, String outputDir) {
        setInFilename(filename);
        setOutputDir(outputDir);
        initAttributesDir();
        createUseFilename();
    }

    private void initFile() {
        try {
            this.streamOut = new FileOutputStream(getUseFilename());
            this.writerOut = new PrintWriter(new OutputStreamWriter(this.streamOut));
            this.writerOut.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            this.writerOut.println("<attributes>");
        } catch (IOException e1) {
            logger.debug(e1.getMessage());
        }
    }

    private void finishFile() {
        this.writerOut.println("</attributes>");
        this.writerOut.close();
    }

    public void printAttrs(SootClass c, TagCollector tc) {
        printAttrs(c, tc, false);
    }

    public void printAttrs(SootClass c) {
        printAttrs(c, new TagCollector(), true);
    }

    private void printAttrs(SootClass c, TagCollector tc, boolean includeBodyTags) {
        tc.collectKeyTags(c);
        tc.collectTags(c, includeBodyTags);
        if (tc.isEmpty()) {
            return;
        }
        initFile();
        tc.printTags(this.writerOut);
        tc.printKeys(this.writerOut);
        finishFile();
    }

    private void initAttributesDir() {
        File dir = new File(String.valueOf(getOutputDir()) + File.separatorChar + "attributes");
        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (SecurityException e) {
                logger.debug("Unable to create attributes");
            }
        }
    }

    private void createUseFilename() {
        String tmp = getInFilename();
        String tmp2 = tmp.substring(0, tmp.lastIndexOf(46));
        int slash = tmp2.lastIndexOf(File.separatorChar);
        if (slash != -1) {
            tmp2 = tmp2.substring(slash + 1, tmp2.length());
        }
        setUseFilename(getOutputDir() + File.separatorChar + "attributes" + File.separatorChar + tmp2 + ".xml");
    }

    private void setInFilename(String file) {
        this.useFilename = file;
    }

    private String getInFilename() {
        return this.useFilename;
    }

    private void setUseFilename(String file) {
        this.useFilename = file;
    }

    private String getUseFilename() {
        return this.useFilename;
    }
}
