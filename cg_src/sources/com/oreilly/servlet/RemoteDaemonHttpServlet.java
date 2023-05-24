package com.oreilly.servlet;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/RemoteDaemonHttpServlet.class */
public abstract class RemoteDaemonHttpServlet extends DaemonHttpServlet implements Remote {
    protected Registry registry;

    @Override // com.oreilly.servlet.DaemonHttpServlet, javax.servlet.GenericServlet, javax.servlet.Servlet
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            UnicastRemoteObject.exportObject(this);
            bind();
        } catch (RemoteException e) {
            log(new StringBuffer().append("Problem binding to RMI registry: ").append(e.getMessage()).toString());
        }
    }

    @Override // com.oreilly.servlet.DaemonHttpServlet, javax.servlet.GenericServlet, javax.servlet.Servlet
    public void destroy() {
        super.destroy();
        unbind();
    }

    protected String getRegistryName() {
        String name = getInitParameter("registryName");
        return name != null ? name : getClass().getName();
    }

    protected int getRegistryPort() {
        try {
            return Integer.parseInt(getInitParameter("registryPort"));
        } catch (NumberFormatException e) {
            return 1099;
        }
    }

    protected void bind() {
        try {
            this.registry = LocateRegistry.getRegistry(getRegistryPort());
            this.registry.list();
        } catch (Exception e) {
            this.registry = null;
        }
        if (this.registry == null) {
            try {
                this.registry = LocateRegistry.createRegistry(getRegistryPort());
            } catch (Exception e2) {
                log(new StringBuffer().append("Could not get or create RMI registry on port ").append(getRegistryPort()).append(": ").append(e2.getMessage()).toString());
                return;
            }
        }
        try {
            this.registry.rebind(getRegistryName(), this);
        } catch (Exception e3) {
            log(new StringBuffer().append("humbug Could not bind to RMI registry: ").append(e3.getMessage()).toString());
        }
    }

    protected void unbind() {
        try {
            if (this.registry != null) {
                this.registry.unbind(getRegistryName());
            }
        } catch (Exception e) {
            log(new StringBuffer().append("Problem unbinding from RMI registry: ").append(e.getMessage()).toString());
        }
    }
}
