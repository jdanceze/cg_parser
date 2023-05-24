package com.oreilly.servlet;

import java.io.IOException;
import java.net.ServerSocket;
/* compiled from: DaemonHttpServlet.java */
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/Daemon.class */
class Daemon extends Thread {
    private ServerSocket serverSocket;
    private DaemonHttpServlet servlet;

    public Daemon(DaemonHttpServlet servlet) {
        this.servlet = servlet;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.serverSocket = new ServerSocket(this.servlet.getSocketPort());
            while (true) {
                try {
                    try {
                        this.servlet.handleClient(this.serverSocket.accept());
                    } catch (IOException ioe) {
                        this.servlet.log(new StringBuffer().append("Problem accepting client's socket connection: ").append(ioe.getClass().getName()).append(": ").append(ioe.getMessage()).toString());
                    }
                } catch (ThreadDeath e) {
                    try {
                        this.serverSocket.close();
                        return;
                    } catch (IOException ioe2) {
                        this.servlet.log(new StringBuffer().append("Problem closing server socket: ").append(ioe2.getClass().getName()).append(": ").append(ioe2.getMessage()).toString());
                        return;
                    }
                }
            }
        } catch (Exception e2) {
            this.servlet.log(new StringBuffer().append("Problem establishing server socket: ").append(e2.getClass().getName()).append(": ").append(e2.getMessage()).toString());
        }
    }
}
