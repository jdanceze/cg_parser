package javax.security.jacc;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.SecurityPermission;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/security/jacc/PolicyConfigurationFactory.class */
public abstract class PolicyConfigurationFactory {
    private static String FACTORY_NAME = "javax.security.jacc.PolicyConfigurationFactory.provider";
    private static PolicyConfigurationFactory pcFactory;

    public abstract PolicyConfiguration getPolicyConfiguration(String str, boolean z) throws PolicyContextException;

    public abstract boolean inService(String str) throws PolicyContextException;

    public static PolicyConfigurationFactory getPolicyConfigurationFactory() throws ClassNotFoundException, PolicyContextException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new SecurityPermission("setPolicy"));
        }
        if (pcFactory != null) {
            return pcFactory;
        }
        String[] classname = {null};
        Class clazz = null;
        try {
            if (sm != null) {
                try {
                    clazz = (Class) AccessController.doPrivileged(new PrivilegedExceptionAction(classname) { // from class: javax.security.jacc.PolicyConfigurationFactory.1
                        private final String[] val$classname;

                        {
                            this.val$classname = classname;
                        }

                        @Override // java.security.PrivilegedExceptionAction
                        public Object run() throws Exception {
                            this.val$classname[0] = System.getProperty(PolicyConfigurationFactory.FACTORY_NAME);
                            if (this.val$classname[0] == null) {
                                String msg = new String(new StringBuffer().append("JACC:Error PolicyConfigurationFactory : property not set : ").append(PolicyConfigurationFactory.FACTORY_NAME).toString());
                                throw new ClassNotFoundException(msg);
                            }
                            return Class.forName(this.val$classname[0], true, ClassLoader.getSystemClassLoader());
                        }
                    });
                } catch (PrivilegedActionException ex) {
                    Exception e = ex.getException();
                    if (e instanceof ClassNotFoundException) {
                        throw ((ClassNotFoundException) e);
                    }
                    if (e instanceof InstantiationException) {
                        throw ((InstantiationException) e);
                    }
                    if (e instanceof IllegalAccessException) {
                        throw ((IllegalAccessException) e);
                    }
                }
            } else {
                classname[0] = System.getProperty(FACTORY_NAME);
                if (classname[0] == null) {
                    String msg = new String(new StringBuffer().append("JACC:Error PolicyConfigurationFactory : property not set : ").append(FACTORY_NAME).toString());
                    throw new ClassNotFoundException(msg);
                }
                clazz = Class.forName(classname[0], true, ClassLoader.getSystemClassLoader());
            }
            Object factory = clazz.newInstance();
            pcFactory = (PolicyConfigurationFactory) factory;
            return pcFactory;
        } catch (ClassCastException e2) {
            String msg2 = new String(new StringBuffer().append("JACC:Error PolicyConfigurationFactory : class not PolicyConfigurationFactory : ").append(classname[0]).toString());
            throw new ClassCastException(msg2);
        } catch (ClassNotFoundException cnfe) {
            String msg3 = new String(new StringBuffer().append("JACC:Error PolicyConfigurationFactory : cannot find class : ").append(classname[0]).toString());
            throw new ClassNotFoundException(msg3, cnfe);
        } catch (IllegalAccessException iae) {
            String msg4 = new String(new StringBuffer().append("JACC:Error PolicyConfigurationFactory : cannot access class : ").append(classname[0]).toString());
            throw new PolicyContextException(msg4, iae);
        } catch (InstantiationException ie) {
            String msg5 = new String(new StringBuffer().append("JACC:Error PolicyConfigurationFactory : cannot instantiate : ").append(classname[0]).toString());
            throw new PolicyContextException(msg5, ie);
        }
    }
}
