package org.jvnet.fastinfoset;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:org/jvnet/fastinfoset/ExternalVocabulary.class */
public class ExternalVocabulary {
    public final String URI;
    public final Vocabulary vocabulary;

    public ExternalVocabulary(String URI, Vocabulary vocabulary) {
        if (URI == null || vocabulary == null) {
            throw new IllegalArgumentException();
        }
        this.URI = URI;
        this.vocabulary = vocabulary;
    }
}
