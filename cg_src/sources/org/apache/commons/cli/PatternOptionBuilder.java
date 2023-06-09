package org.apache.commons.cli;

import soot.JavaBasicTypes;
/* loaded from: gencallgraphv3.jar:commons-cli-1.2.jar:org/apache/commons/cli/PatternOptionBuilder.class */
public class PatternOptionBuilder {
    public static final Class STRING_VALUE;
    public static final Class OBJECT_VALUE;
    public static final Class NUMBER_VALUE;
    public static final Class DATE_VALUE;
    public static final Class CLASS_VALUE;
    public static final Class EXISTING_FILE_VALUE;
    public static final Class FILE_VALUE;
    public static final Class FILES_VALUE;
    public static final Class URL_VALUE;
    static Class class$java$lang$String;
    static Class class$java$lang$Object;
    static Class class$java$lang$Number;
    static Class class$java$util$Date;
    static Class class$java$lang$Class;
    static Class class$java$io$FileInputStream;
    static Class class$java$io$File;
    static Class array$Ljava$io$File;
    static Class class$java$net$URL;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    static {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class cls6;
        Class cls7;
        Class cls8;
        Class cls9;
        if (class$java$lang$String == null) {
            cls = class$("java.lang.String");
            class$java$lang$String = cls;
        } else {
            cls = class$java$lang$String;
        }
        STRING_VALUE = cls;
        if (class$java$lang$Object == null) {
            cls2 = class$(JavaBasicTypes.JAVA_LANG_OBJECT);
            class$java$lang$Object = cls2;
        } else {
            cls2 = class$java$lang$Object;
        }
        OBJECT_VALUE = cls2;
        if (class$java$lang$Number == null) {
            cls3 = class$("java.lang.Number");
            class$java$lang$Number = cls3;
        } else {
            cls3 = class$java$lang$Number;
        }
        NUMBER_VALUE = cls3;
        if (class$java$util$Date == null) {
            cls4 = class$("java.util.Date");
            class$java$util$Date = cls4;
        } else {
            cls4 = class$java$util$Date;
        }
        DATE_VALUE = cls4;
        if (class$java$lang$Class == null) {
            cls5 = class$("java.lang.Class");
            class$java$lang$Class = cls5;
        } else {
            cls5 = class$java$lang$Class;
        }
        CLASS_VALUE = cls5;
        if (class$java$io$FileInputStream == null) {
            cls6 = class$("java.io.FileInputStream");
            class$java$io$FileInputStream = cls6;
        } else {
            cls6 = class$java$io$FileInputStream;
        }
        EXISTING_FILE_VALUE = cls6;
        if (class$java$io$File == null) {
            cls7 = class$("java.io.File");
            class$java$io$File = cls7;
        } else {
            cls7 = class$java$io$File;
        }
        FILE_VALUE = cls7;
        if (array$Ljava$io$File == null) {
            cls8 = class$("[Ljava.io.File;");
            array$Ljava$io$File = cls8;
        } else {
            cls8 = array$Ljava$io$File;
        }
        FILES_VALUE = cls8;
        if (class$java$net$URL == null) {
            cls9 = class$("java.net.URL");
            class$java$net$URL = cls9;
        } else {
            cls9 = class$java$net$URL;
        }
        URL_VALUE = cls9;
    }

    public static Object getValueClass(char ch) {
        switch (ch) {
            case '#':
                return DATE_VALUE;
            case '$':
            case '&':
            case '\'':
            case '(':
            case ')':
            case ',':
            case '-':
            case '.':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case ';':
            case '=':
            case '?':
            default:
                return null;
            case '%':
                return NUMBER_VALUE;
            case '*':
                return FILES_VALUE;
            case '+':
                return CLASS_VALUE;
            case '/':
                return URL_VALUE;
            case ':':
                return STRING_VALUE;
            case '<':
                return EXISTING_FILE_VALUE;
            case '>':
                return FILE_VALUE;
            case '@':
                return OBJECT_VALUE;
        }
    }

    public static boolean isValueCode(char ch) {
        return ch == '@' || ch == ':' || ch == '%' || ch == '+' || ch == '#' || ch == '<' || ch == '>' || ch == '*' || ch == '/' || ch == '!';
    }

    public static Options parsePattern(String pattern) {
        char opt = ' ';
        boolean required = false;
        Object type = null;
        Options options = new Options();
        for (int i = 0; i < pattern.length(); i++) {
            char ch = pattern.charAt(i);
            if (!isValueCode(ch)) {
                if (opt != ' ') {
                    OptionBuilder.hasArg(type != null);
                    OptionBuilder.isRequired(required);
                    OptionBuilder.withType(type);
                    options.addOption(OptionBuilder.create(opt));
                    required = false;
                    type = null;
                }
                opt = ch;
            } else if (ch == '!') {
                required = true;
            } else {
                type = getValueClass(ch);
            }
        }
        if (opt != ' ') {
            OptionBuilder.hasArg(type != null);
            OptionBuilder.isRequired(required);
            OptionBuilder.withType(type);
            options.addOption(OptionBuilder.create(opt));
        }
        return options;
    }
}
