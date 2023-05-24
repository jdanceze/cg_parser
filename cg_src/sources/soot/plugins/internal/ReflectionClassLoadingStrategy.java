package soot.plugins.internal;
/* loaded from: gencallgraphv3.jar:soot/plugins/internal/ReflectionClassLoadingStrategy.class */
public class ReflectionClassLoadingStrategy implements ClassLoadingStrategy {
    @Override // soot.plugins.internal.ClassLoadingStrategy
    public Object create(String className) throws ClassNotFoundException, InstantiationException {
        Class<?> clazz = Class.forName(className);
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException e) {
            throw new InstantiationException("Failed to create instance of " + className + " due to access restrictions.");
        }
    }
}
