package org.apache.tools.ant;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/BuildException.class */
public class BuildException extends RuntimeException {
    private static final long serialVersionUID = -5419014565354664240L;
    private Location location;

    public BuildException() {
        this.location = Location.UNKNOWN_LOCATION;
    }

    public BuildException(String message) {
        super(message);
        this.location = Location.UNKNOWN_LOCATION;
    }

    public BuildException(String pattern, Object... formatArguments) {
        super(String.format(pattern, formatArguments));
        this.location = Location.UNKNOWN_LOCATION;
    }

    public BuildException(String message, Throwable cause) {
        super(message, cause);
        this.location = Location.UNKNOWN_LOCATION;
    }

    public BuildException(String message, Throwable cause, Location location) {
        this(message, cause);
        this.location = location;
    }

    public BuildException(Throwable cause) {
        super(cause);
        this.location = Location.UNKNOWN_LOCATION;
    }

    public BuildException(String message, Location location) {
        super(message);
        this.location = Location.UNKNOWN_LOCATION;
        this.location = location;
    }

    public BuildException(Throwable cause, Location location) {
        this(cause);
        this.location = location;
    }

    @Deprecated
    public Throwable getException() {
        return getCause();
    }

    @Override // java.lang.Throwable
    public String toString() {
        return this.location.toString() + getMessage();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }
}
