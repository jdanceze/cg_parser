package com.oreilly.servlet;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/MultipartResponse.class */
public class MultipartResponse {
    HttpServletResponse res;
    ServletOutputStream out;
    boolean endedLastResponse = true;

    public MultipartResponse(HttpServletResponse response) throws IOException {
        this.res = response;
        this.out = this.res.getOutputStream();
        this.res.setContentType("multipart/x-mixed-replace;boundary=End");
        this.out.println();
        this.out.println("--End");
    }

    public void startResponse(String contentType) throws IOException {
        if (!this.endedLastResponse) {
            endResponse();
        }
        this.out.println(new StringBuffer().append("Content-type: ").append(contentType).toString());
        this.out.println();
        this.endedLastResponse = false;
    }

    public void endResponse() throws IOException {
        this.out.println();
        this.out.println("--End");
        this.out.flush();
        this.endedLastResponse = true;
    }

    public void finish() throws IOException {
        this.out.println("--End--");
        this.out.flush();
    }
}
