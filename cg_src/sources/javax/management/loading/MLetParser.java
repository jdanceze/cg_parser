package javax.management.loading;

import com.sun.jmx.trace.Trace;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.Vector;
import polyglot.main.Report;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/loading/MLetParser.class */
class MLetParser {
    private int c;
    private static String tag = "mlet";
    private String dbgTag = "MLetParser";

    public void skipSpace(Reader reader) throws IOException {
        while (this.c >= 0) {
            if (this.c == 32 || this.c == 9 || this.c == 10 || this.c == 13) {
                this.c = reader.read();
            } else {
                return;
            }
        }
    }

    public String scanIdentifier(Reader reader) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            if ((this.c >= 97 && this.c <= 122) || ((this.c >= 65 && this.c <= 90) || ((this.c >= 48 && this.c <= 57) || this.c == 95))) {
                stringBuffer.append((char) this.c);
                this.c = reader.read();
            } else {
                return stringBuffer.toString();
            }
        }
    }

    public Hashtable scanTag(Reader reader) throws IOException {
        Hashtable hashtable = new Hashtable();
        skipSpace(reader);
        while (this.c >= 0 && this.c != 62) {
            String scanIdentifier = scanIdentifier(reader);
            String str = "";
            skipSpace(reader);
            if (this.c == 61) {
                int i = -1;
                this.c = reader.read();
                skipSpace(reader);
                if (this.c == 39 || this.c == 34) {
                    i = this.c;
                    this.c = reader.read();
                }
                StringBuffer stringBuffer = new StringBuffer();
                while (this.c > 0 && ((i < 0 && this.c != 32 && this.c != 9 && this.c != 10 && this.c != 13 && this.c != 62) || (i >= 0 && this.c != i))) {
                    stringBuffer.append((char) this.c);
                    this.c = reader.read();
                }
                if (this.c == i) {
                    this.c = reader.read();
                }
                skipSpace(reader);
                str = stringBuffer.toString();
            }
            hashtable.put(scanIdentifier.toLowerCase(), str);
            skipSpace(reader);
        }
        return hashtable;
    }

    public Vector parse(URL url) throws IOException {
        URLConnection openConnection = url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));
        URL url2 = openConnection.getURL();
        Vector vector = new Vector();
        Hashtable hashtable = null;
        Vector vector2 = new Vector();
        Vector vector3 = new Vector();
        while (true) {
            this.c = bufferedReader.read();
            if (this.c != -1) {
                if (this.c == 60) {
                    this.c = bufferedReader.read();
                    if (this.c == 47) {
                        this.c = bufferedReader.read();
                        if (scanIdentifier(bufferedReader).equalsIgnoreCase(tag)) {
                            if (hashtable != null) {
                                if (vector2.size() == vector3.size() && !vector2.isEmpty() && !vector3.isEmpty()) {
                                    hashtable.put(Report.types, vector2.clone());
                                    hashtable.put("values", vector3.clone());
                                }
                                vector.addElement(new MLetContent(url2, hashtable));
                            }
                            hashtable = null;
                            vector2.removeAllElements();
                            vector3.removeAllElements();
                        }
                    } else {
                        String scanIdentifier = scanIdentifier(bufferedReader);
                        if (scanIdentifier.equalsIgnoreCase("arg")) {
                            Hashtable scanTag = scanTag(bufferedReader);
                            String str = (String) scanTag.get("type");
                            if (str == null) {
                                if (isTraceOn()) {
                                    trace("parse", "<param name=... value=...> tag requires name parameter.");
                                    return null;
                                }
                                return null;
                            } else if (hashtable != null) {
                                vector2.addElement(str);
                                String str2 = (String) scanTag.get("value");
                                if (str2 == null) {
                                    if (isTraceOn()) {
                                        trace("parse", "<param name=... value=...> tag requires name parameter.");
                                        return null;
                                    }
                                    return null;
                                } else if (hashtable != null) {
                                    vector3.addElement(str2);
                                } else if (isTraceOn()) {
                                    trace("parse", "<param> tag outside <mlet> ... </mlet>.");
                                    return null;
                                } else {
                                    return null;
                                }
                            } else if (isTraceOn()) {
                                trace("parse", "<param> tag outside <mlet> ... </mlet>.");
                                return null;
                            } else {
                                return null;
                            }
                        } else if (scanIdentifier.equalsIgnoreCase(tag)) {
                            hashtable = scanTag(bufferedReader);
                            if (hashtable.get("code") == null && hashtable.get("object") == null) {
                                if (isTraceOn()) {
                                    trace("parse", "<mlet> tag requires either code or object parameter.");
                                }
                                return null;
                            } else if (hashtable.get("archive") == null) {
                                if (isTraceOn()) {
                                    trace("parse", "<mlet> tag requires archive parameter.");
                                }
                                return null;
                            }
                        } else {
                            continue;
                        }
                    }
                }
            } else {
                bufferedReader.close();
                return vector;
            }
        }
    }

    public Vector parseURL(String str) {
        URL url;
        String str2;
        try {
            if (str.indexOf(58) <= 1) {
                String property = System.getProperty("user.dir");
                if (property.charAt(0) == '/' || property.charAt(0) == File.separatorChar) {
                    str2 = "file:";
                } else {
                    str2 = "file:/";
                }
                url = new URL(new URL(new StringBuffer().append(str2).append(property.replace(File.separatorChar, '/')).append("/").toString()), str);
            } else {
                url = new URL(str);
            }
            return parse(url);
        } catch (MalformedURLException e) {
            if (isTraceOn()) {
                trace("parseURL", new StringBuffer().append("Bad URL: ").append(str).append(" ( ").append(e.getMessage()).append(" )").toString());
                return null;
            }
            return null;
        } catch (IOException e2) {
            if (isTraceOn()) {
                trace("parseURL", new StringBuffer().append("I/O exception while reading: ").append(str).append(" ( ").append(e2.getMessage()).append(" )").toString());
                return null;
            }
            return null;
        }
    }

    private boolean isTraceOn() {
        return Trace.isSelected(1, 2);
    }

    private void trace(String str, String str2, String str3) {
        Trace.send(1, 2, str, str2, str3);
    }

    private void trace(String str, String str2) {
        trace(this.dbgTag, str, str2);
    }

    private boolean isDebugOn() {
        return Trace.isSelected(2, 2);
    }

    private void debug(String str, String str2, String str3) {
        Trace.send(2, 2, str, str2, str3);
    }

    private void debug(String str, String str2) {
        debug(this.dbgTag, str, str2);
    }
}
