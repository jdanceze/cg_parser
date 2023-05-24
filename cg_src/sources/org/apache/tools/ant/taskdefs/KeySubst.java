package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Hashtable;
import java.util.StringTokenizer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.StringUtils;
@Deprecated
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/KeySubst.class */
public class KeySubst extends Task {
    private File source = null;
    private File dest = null;
    private String sep = "*";
    private Hashtable<String, String> replacements = new Hashtable<>();

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        log("!! KeySubst is deprecated. Use Filter + Copy instead. !!");
        log("Performing Substitutions");
        if (this.source == null || this.dest == null) {
            log("Source and destinations must not be null");
            return;
        }
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            try {
                br = new BufferedReader(new FileReader(this.source));
                this.dest.delete();
                bw = new BufferedWriter(new FileWriter(this.dest));
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    if (!line.isEmpty()) {
                        String newline = replace(line, this.replacements);
                        bw.write(newline);
                    }
                    bw.newLine();
                }
                bw.flush();
                FileUtils.close((Writer) bw);
                FileUtils.close((Reader) br);
            } catch (IOException ioe) {
                log(StringUtils.getStackTrace(ioe), 0);
                FileUtils.close((Writer) bw);
                FileUtils.close((Reader) br);
            }
        } catch (Throwable th) {
            FileUtils.close((Writer) bw);
            FileUtils.close((Reader) br);
            throw th;
        }
    }

    public void setSrc(File s) {
        this.source = s;
    }

    public void setDest(File dest) {
        this.dest = dest;
    }

    public void setSep(String sep) {
        this.sep = sep;
    }

    public void setKeys(String keys) {
        if (keys != null && !keys.isEmpty()) {
            StringTokenizer tok = new StringTokenizer(keys, this.sep, false);
            while (tok.hasMoreTokens()) {
                String token = tok.nextToken().trim();
                StringTokenizer itok = new StringTokenizer(token, "=", false);
                String name = itok.nextToken();
                String value = itok.nextToken();
                this.replacements.put(name, value);
            }
        }
    }

    public static void main(String[] args) {
        try {
            Hashtable<String, String> hash = new Hashtable<>();
            hash.put("VERSION", "1.0.3");
            hash.put("b", "ffff");
            System.out.println(replace("$f ${VERSION} f ${b} jj $", hash));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String replace(String origString, Hashtable<String, String> keys) throws BuildException {
        StringBuffer finalString = new StringBuffer();
        int i = 0;
        while (true) {
            int index = origString.indexOf("${", i);
            if (index > -1) {
                String key = origString.substring(index + 2, origString.indexOf("}", index + 3));
                finalString.append((CharSequence) origString, i, index);
                if (keys.containsKey(key)) {
                    finalString.append(keys.get(key));
                } else {
                    finalString.append("${");
                    finalString.append(key);
                    finalString.append("}");
                }
                i = index + 3 + key.length();
            } else {
                finalString.append(origString.substring(i));
                return finalString.toString();
            }
        }
    }
}
