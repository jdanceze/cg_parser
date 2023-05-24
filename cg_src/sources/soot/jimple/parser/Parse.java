package soot.jimple.parser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.HashMap;
import org.apache.commons.cli.HelpFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Scene;
import soot.SootClass;
import soot.jimple.parser.lexer.Lexer;
import soot.jimple.parser.lexer.LexerException;
import soot.jimple.parser.node.Start;
import soot.jimple.parser.parser.Parser;
import soot.jimple.parser.parser.ParserException;
import soot.util.EscapedReader;
@Deprecated
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/Parse.class */
public class Parse {
    private static final Logger logger = LoggerFactory.getLogger(Parse.class);
    private static final String EXT = ".jimple";
    private static final String USAGE = "usage: java Parse [options] jimple_file [jimple_file ...]";

    /* JADX WARN: Multi-variable type inference failed */
    public static SootClass parse(InputStream istream, SootClass sc) {
        Walker w;
        Parser p = new Parser(new Lexer(new PushbackReader(new EscapedReader(new BufferedReader(new InputStreamReader(istream))), 1024)));
        try {
            Start tree = p.parse();
            if (sc == null) {
                w = new Walker(null);
            } else {
                w = new BodyExtractorWalker(sc, null, new HashMap());
            }
            tree.apply(w);
            return w.getSootClass();
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred: " + e);
        } catch (LexerException e2) {
            throw new RuntimeException("Lexer exception occurred: " + e2);
        } catch (ParserException e3) {
            throw new RuntimeException("Parser exception occurred: " + e3);
        }
    }

    public static void main(String[] args) throws Exception {
        InputStream inFile;
        boolean verbose = false;
        if (args.length < 1) {
            logger.debug(USAGE);
            System.exit(0);
        }
        Scene.v().setPhantomRefs(true);
        for (String arg : args) {
            if (arg.startsWith(HelpFormatter.DEFAULT_OPT_PREFIX)) {
                String arg2 = arg.substring(1);
                if (!arg2.equals("d") && arg2.equals("v")) {
                    verbose = true;
                }
            } else {
                if (verbose) {
                    try {
                        logger.debug(" ... looking for " + arg);
                    } catch (FileNotFoundException e) {
                        if (arg.endsWith(EXT)) {
                            logger.debug(" *** can't find " + arg);
                        } else {
                            String arg3 = String.valueOf(arg) + EXT;
                            if (verbose) {
                                try {
                                    logger.debug(" ... looking for " + arg3);
                                } catch (FileNotFoundException e2) {
                                    logger.debug(" *** can't find " + arg3);
                                }
                            }
                            inFile = new BufferedInputStream(new FileInputStream(arg3));
                        }
                    }
                }
                inFile = new FileInputStream(arg);
                Parser p = new Parser(new Lexer(new PushbackReader(new InputStreamReader(inFile), 1024)));
                Start tree = p.parse();
                tree.apply(new Walker(null));
            }
        }
    }
}
