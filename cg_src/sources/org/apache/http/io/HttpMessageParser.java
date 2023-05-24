package org.apache.http.io;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/io/HttpMessageParser.class */
public interface HttpMessageParser {
    HttpMessage parse() throws IOException, HttpException;
}
