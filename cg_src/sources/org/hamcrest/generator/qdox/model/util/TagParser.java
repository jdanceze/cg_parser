package org.hamcrest.generator.qdox.model.util;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/util/TagParser.class */
public class TagParser {
    static StreamTokenizer makeTokenizer(String tagValue) {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(tagValue));
        tokenizer.resetSyntax();
        tokenizer.wordChars(65, 90);
        tokenizer.wordChars(97, 122);
        tokenizer.wordChars(48, 57);
        tokenizer.wordChars(45, 45);
        tokenizer.wordChars(95, 95);
        tokenizer.wordChars(46, 46);
        tokenizer.wordChars(60, 60);
        tokenizer.wordChars(62, 62);
        tokenizer.quoteChar(39);
        tokenizer.quoteChar(34);
        tokenizer.whitespaceChars(32, 32);
        tokenizer.whitespaceChars(9, 9);
        tokenizer.whitespaceChars(10, 10);
        tokenizer.whitespaceChars(13, 13);
        tokenizer.eolIsSignificant(false);
        return tokenizer;
    }

    public static Map parseNamedParameters(String tagValue) {
        String key;
        Map paramMap = new OrderedMap();
        StreamTokenizer tokenizer = makeTokenizer(tagValue);
        while (tokenizer.nextToken() == -3) {
            try {
                key = tokenizer.sval;
            } catch (IOException e) {
            }
            if (tokenizer.nextToken() == 61) {
                switch (tokenizer.nextToken()) {
                    case -3:
                    case 34:
                    case 39:
                        paramMap.put(key, tokenizer.sval);
                        break;
                }
            } else {
                return paramMap;
            }
        }
        return paramMap;
    }

    public static String[] parseWords(String tagValue) {
        StreamTokenizer tokenizer = makeTokenizer(tagValue);
        ArrayList wordList = new ArrayList();
        while (tokenizer.nextToken() != -1) {
            try {
                if (tokenizer.sval == null) {
                    wordList.add(Character.toString((char) tokenizer.ttype));
                } else {
                    wordList.add(tokenizer.sval);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("error tokenizing tag");
            }
        }
        String[] wordArray = new String[wordList.size()];
        wordList.toArray(wordArray);
        return wordArray;
    }

    public static String[] parseParameters(String tagValue) {
        StreamTokenizer tokenizer = makeTokenizer(tagValue);
        ArrayList wordList = new ArrayList();
        while (tokenizer.nextToken() != -1) {
            try {
                StringBuilder param = new StringBuilder();
                if (tokenizer.sval != null) {
                    param.append(tokenizer.sval);
                }
                while (tokenizer.nextToken() != -1) {
                    if (tokenizer.sval == null && ('=' == ((char) tokenizer.ttype) || ',' == ((char) tokenizer.ttype))) {
                        param.append(Character.toString((char) tokenizer.ttype));
                        tokenizer.nextToken();
                        param.append(tokenizer.sval);
                    } else {
                        tokenizer.pushBack();
                        break;
                    }
                }
                wordList.add(param.toString());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("error tokenizing tag");
            }
        }
        String[] wordArray = new String[wordList.size()];
        wordList.toArray(wordArray);
        return wordArray;
    }
}
