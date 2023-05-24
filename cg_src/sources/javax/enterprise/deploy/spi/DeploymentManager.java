package javax.enterprise.deploy.spi;

import java.io.File;
import java.io.InputStream;
import java.util.Locale;
import javax.enterprise.deploy.model.DeployableObject;
import javax.enterprise.deploy.shared.DConfigBeanVersionType;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.exceptions.DConfigBeanVersionUnsupportedException;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;
import javax.enterprise.deploy.spi.exceptions.TargetException;
import javax.enterprise.deploy.spi.status.ProgressObject;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/spi/DeploymentManager.class */
public interface DeploymentManager {
    Target[] getTargets() throws IllegalStateException;

    TargetModuleID[] getRunningModules(ModuleType moduleType, Target[] targetArr) throws TargetException, IllegalStateException;

    TargetModuleID[] getNonRunningModules(ModuleType moduleType, Target[] targetArr) throws TargetException, IllegalStateException;

    TargetModuleID[] getAvailableModules(ModuleType moduleType, Target[] targetArr) throws TargetException, IllegalStateException;

    DeploymentConfiguration createConfiguration(DeployableObject deployableObject) throws InvalidModuleException;

    ProgressObject distribute(Target[] targetArr, File file, File file2) throws IllegalStateException;

    ProgressObject distribute(Target[] targetArr, InputStream inputStream, InputStream inputStream2) throws IllegalStateException;

    ProgressObject start(TargetModuleID[] targetModuleIDArr) throws IllegalStateException;

    ProgressObject stop(TargetModuleID[] targetModuleIDArr) throws IllegalStateException;

    ProgressObject undeploy(TargetModuleID[] targetModuleIDArr) throws IllegalStateException;

    boolean isRedeploySupported();

    ProgressObject redeploy(TargetModuleID[] targetModuleIDArr, File file, File file2) throws UnsupportedOperationException, IllegalStateException;

    ProgressObject redeploy(TargetModuleID[] targetModuleIDArr, InputStream inputStream, InputStream inputStream2) throws UnsupportedOperationException, IllegalStateException;

    void release();

    Locale getDefaultLocale();

    Locale getCurrentLocale();

    void setLocale(Locale locale) throws UnsupportedOperationException;

    Locale[] getSupportedLocales();

    boolean isLocaleSupported(Locale locale);

    DConfigBeanVersionType getDConfigBeanVersion();

    boolean isDConfigBeanVersionSupported(DConfigBeanVersionType dConfigBeanVersionType);

    void setDConfigBeanVersion(DConfigBeanVersionType dConfigBeanVersionType) throws DConfigBeanVersionUnsupportedException;
}
