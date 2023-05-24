package soot.toolkits.astmetrics;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polyglot.ast.ClassDecl;
import polyglot.ast.FieldDecl;
import polyglot.ast.Formal;
import polyglot.ast.LocalDecl;
import polyglot.ast.MethodDecl;
import polyglot.ast.Node;
import polyglot.visit.NodeVisitor;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/toolkits/astmetrics/IdentifiersMetric.class */
public class IdentifiersMetric extends ASTMetric {
    private static final Logger logger = LoggerFactory.getLogger(IdentifiersMetric.class);
    double nameComplexity;
    double nameCount;
    int dictionarySize;
    ArrayList<String> dictionary;
    HashMap<String, Double> names;

    public IdentifiersMetric(Node astNode) {
        super(astNode);
        this.nameComplexity = Const.default_value_double;
        this.nameCount = Const.default_value_double;
        this.dictionarySize = 0;
        initializeDictionary();
    }

    private void initializeDictionary() {
        BufferedReader br = null;
        this.dictionary = new ArrayList<>();
        this.names = new HashMap<>();
        InputStream is = ClassLoader.getSystemResourceAsStream("mydict.txt");
        if (is != null) {
            br = new BufferedReader(new InputStreamReader(is));
            while (true) {
                try {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    addWord(line);
                } catch (IOException ioexc) {
                    logger.debug(ioexc.getMessage());
                }
            }
        }
        InputStream is2 = ClassLoader.getSystemResourceAsStream("soot/toolkits/astmetrics/dict.txt");
        if (is2 != null) {
            br = new BufferedReader(new InputStreamReader(is2));
            while (true) {
                try {
                    String line2 = br.readLine();
                    if (line2 == null) {
                        break;
                    }
                    addWord(line2.trim().toLowerCase());
                } catch (IOException ioexc2) {
                    logger.debug(ioexc2.getMessage());
                }
            }
        }
        int size = this.dictionary.size();
        this.dictionarySize = size;
        if (size == 0) {
            logger.debug("Error reading in dictionary file(s)");
        } else if (Options.v().verbose()) {
            logger.debug("Read " + this.dictionarySize + " words in from dictionary file(s)");
        }
        try {
            is2.close();
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
        if (br != null) {
            try {
                br.close();
            } catch (IOException e2) {
                logger.debug(e2.getMessage());
            }
        }
    }

    private void addWord(String word) {
        if (this.dictionarySize == 0 || word.compareTo(this.dictionary.get(this.dictionarySize - 1)) > 0) {
            this.dictionary.add(word);
        } else {
            int i = 0;
            while (i < this.dictionarySize && word.compareTo(this.dictionary.get(i)) > 0) {
                i++;
            }
            if (word.compareTo(this.dictionary.get(i)) == 0) {
                return;
            }
            this.dictionary.add(i, word);
        }
        this.dictionarySize++;
    }

    @Override // soot.toolkits.astmetrics.ASTMetric
    public void reset() {
        this.nameComplexity = Const.default_value_double;
        this.nameCount = Const.default_value_double;
    }

    @Override // soot.toolkits.astmetrics.ASTMetric
    public void addMetrics(ClassData data) {
        data.addMetric(new MetricData("NameComplexity", new Double(this.nameComplexity)));
        data.addMetric(new MetricData("NameCount", new Double(this.nameCount)));
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node parent, Node n) {
        double multiplier = 1.0d;
        String name = null;
        if (n instanceof ClassDecl) {
            name = ((ClassDecl) n).name();
            multiplier = 3.0d;
            this.nameCount += 1.0d;
        } else if (n instanceof MethodDecl) {
            name = ((MethodDecl) n).name();
            multiplier = 4.0d;
            this.nameCount += 1.0d;
        } else if (n instanceof FieldDecl) {
            name = ((FieldDecl) n).name();
            multiplier = 2.0d;
            this.nameCount += 1.0d;
        } else if (n instanceof Formal) {
            name = ((Formal) n).name();
            multiplier = 1.5d;
            this.nameCount += 1.0d;
        } else if (n instanceof LocalDecl) {
            name = ((LocalDecl) n).name();
            this.nameCount += 1.0d;
        }
        if (name != null) {
            this.nameComplexity += multiplier * computeNameComplexity(name);
        }
        return enter(n);
    }

    private double computeNameComplexity(String name) {
        if (this.names.containsKey(name)) {
            return this.names.get(name).doubleValue();
        }
        ArrayList<String> strings = new ArrayList<>();
        String tmp = "";
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if ((c > '@' && c < '[') || (c > '`' && c < '{')) {
                tmp = String.valueOf(tmp) + c;
            } else if (tmp.length() > 0) {
                strings.add(tmp);
                tmp = "";
            }
        }
        if (tmp.length() > 0) {
            strings.add(tmp);
        }
        ArrayList<String> tokens = new ArrayList<>();
        for (int i2 = 0; i2 < strings.size(); i2++) {
            String str = strings.get(i2);
            while (true) {
                String tmp2 = str;
                if (tmp2.length() > 0) {
                    int caps = countCaps(tmp2);
                    if (caps == 0) {
                        int idx = findCap(tmp2);
                        if (idx > 0) {
                            tokens.add(tmp2.substring(0, idx));
                            str = tmp2.substring(idx, tmp2.length());
                        } else {
                            tokens.add(tmp2.substring(0, tmp2.length()));
                            break;
                        }
                    } else if (caps == 1) {
                        int idx2 = findCap(tmp2.substring(1)) + 1;
                        if (idx2 > 0) {
                            tokens.add(tmp2.substring(0, idx2));
                            str = tmp2.substring(idx2, tmp2.length());
                        } else {
                            tokens.add(tmp2.substring(0, tmp2.length()));
                            break;
                        }
                    } else if (caps < tmp2.length()) {
                        tokens.add(tmp2.substring(0, caps - 1).toLowerCase());
                        str = tmp2.substring(caps);
                    } else {
                        tokens.add(tmp2.substring(0, caps).toLowerCase());
                        break;
                    }
                }
            }
        }
        double words = 0.0d;
        double complexity = 0.0d;
        for (int i3 = 0; i3 < tokens.size(); i3++) {
            if (this.dictionary.contains(tokens.get(i3))) {
                words += 1.0d;
            }
        }
        if (words > Const.default_value_double) {
            complexity = tokens.size() / words;
        }
        this.names.put(name, new Double(complexity + computeCharComplexity(name)));
        return complexity;
    }

    private double computeCharComplexity(String name) {
        int count = 0;
        int last = 0;
        int lng = name.length();
        for (int index = 0; index < lng; index++) {
            char c = name.charAt(index);
            if ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z')) {
                last++;
            } else {
                if (last > 1) {
                    count += last;
                }
                last = 0;
            }
        }
        double complexity = lng - count;
        if (complexity > Const.default_value_double) {
            return lng / complexity;
        }
        return lng;
    }

    private int countCaps(String name) {
        char c;
        int caps = 0;
        while (caps < name.length() && (c = name.charAt(caps)) > '@' && c < '[') {
            caps++;
        }
        return caps;
    }

    private int findCap(String name) {
        for (int idx = 0; idx < name.length(); idx++) {
            char c = name.charAt(idx);
            if (c > '@' && c < '[') {
                return idx;
            }
        }
        return -1;
    }
}
