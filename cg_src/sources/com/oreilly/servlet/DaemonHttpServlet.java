package com.oreilly.servlet;

import java.net.Socket;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/DaemonHttpServlet.class */
public abstract class DaemonHttpServlet extends HttpServlet {
    protected int DEFAULT_PORT = 1313;
    private Thread daemonThread;

    public abstract void handleClient(Socket socket);

    @Override // javax.servlet.GenericServlet, javax.servlet.Servlet
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            this.daemonThread = new Daemon(this);
            this.daemonThread.start();
        } catch (Exception e) {
            log(new StringBuffer().append("Problem starting socket server daemon thread").append(e.getClass().getName()).append(": ").append(e.getMessage()).toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getSocketPort() {
        try {
            return Integer.parseInt(getInitParameter("socketPort"));
        } catch (NumberFormatException e) {
            return this.DEFAULT_PORT;
        }
    }

    @Override // javax.servlet.GenericServlet, javax.servlet.Servlet
    public void destroy() {
        try {
            this.daemonThread.stop();
            this.daemonThread = null;
        } catch (Exception e) {
            log(new StringBuffer().append("Problem stopping server socket daemon thread: ").append(e.getClass().getName()).append(": ").append(e.getMessage()).toString());
        }
    }
}
