package org.junit.rules;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Assert;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/TemporaryFolder.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/TemporaryFolder.class */
public class TemporaryFolder extends ExternalResource {
    private final File parentFolder;
    private final boolean assureDeletion;
    private File folder;
    private static final int TEMP_DIR_ATTEMPTS = 10000;
    private static final String TMP_PREFIX = "junit";

    public TemporaryFolder() {
        this((File) null);
    }

    public TemporaryFolder(File parentFolder) {
        this.parentFolder = parentFolder;
        this.assureDeletion = false;
    }

    protected TemporaryFolder(Builder builder) {
        this.parentFolder = builder.parentFolder;
        this.assureDeletion = builder.assureDeletion;
    }

    public static Builder builder() {
        return new Builder();
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/TemporaryFolder$Builder.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/TemporaryFolder$Builder.class */
    public static class Builder {
        private File parentFolder;
        private boolean assureDeletion;

        protected Builder() {
        }

        public Builder parentFolder(File parentFolder) {
            this.parentFolder = parentFolder;
            return this;
        }

        public Builder assureDeletion() {
            this.assureDeletion = true;
            return this;
        }

        public TemporaryFolder build() {
            return new TemporaryFolder(this);
        }
    }

    @Override // org.junit.rules.ExternalResource
    protected void before() throws Throwable {
        create();
    }

    @Override // org.junit.rules.ExternalResource
    protected void after() {
        delete();
    }

    public void create() throws IOException {
        this.folder = createTemporaryFolderIn(this.parentFolder);
    }

    public File newFile(String fileName) throws IOException {
        File file = new File(getRoot(), fileName);
        if (!file.createNewFile()) {
            throw new IOException("a file with the name '" + fileName + "' already exists in the test folder");
        }
        return file;
    }

    public File newFile() throws IOException {
        return File.createTempFile("junit", null, getRoot());
    }

    public File newFolder(String path) throws IOException {
        return newFolder(path);
    }

    public File newFolder(String... paths) throws IOException {
        if (paths.length == 0) {
            throw new IllegalArgumentException("must pass at least one path");
        }
        File root = getRoot();
        for (String path : paths) {
            if (new File(path).isAbsolute()) {
                throw new IOException("folder path '" + path + "' is not a relative path");
            }
        }
        File relativePath = null;
        File file = root;
        boolean lastMkdirsCallSuccessful = true;
        for (String path2 : paths) {
            relativePath = new File(relativePath, path2);
            file = new File(root, relativePath.getPath());
            lastMkdirsCallSuccessful = file.mkdirs();
            if (!lastMkdirsCallSuccessful && !file.isDirectory()) {
                if (file.exists()) {
                    throw new IOException("a file with the path '" + relativePath.getPath() + "' exists");
                } else {
                    throw new IOException("could not create a folder with the path '" + relativePath.getPath() + "'");
                }
            }
        }
        if (!lastMkdirsCallSuccessful) {
            throw new IOException("a folder with the path '" + relativePath.getPath() + "' already exists");
        }
        return file;
    }

    public File newFolder() throws IOException {
        return createTemporaryFolderIn(getRoot());
    }

    private static File createTemporaryFolderIn(File parentFolder) throws IOException {
        try {
            return createTemporaryFolderWithNioApi(parentFolder);
        } catch (ClassNotFoundException e) {
            return createTemporaryFolderWithFileApi(parentFolder);
        } catch (InvocationTargetException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof IOException) {
                throw ((IOException) cause);
            }
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            IOException exception = new IOException("Failed to create temporary folder in " + parentFolder);
            exception.initCause(cause);
            throw exception;
        } catch (Exception e3) {
            throw new RuntimeException("Failed to create temporary folder in " + parentFolder, e3);
        }
    }

    private static File createTemporaryFolderWithNioApi(File parentFolder) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object tempDir;
        Class<?> filesClass = Class.forName("java.nio.file.Files");
        Object fileAttributeArray = Array.newInstance(Class.forName("java.nio.file.attribute.FileAttribute"), 0);
        Class<?> pathClass = Class.forName("java.nio.file.Path");
        if (parentFolder != null) {
            Method createTempDirectoryMethod = filesClass.getDeclaredMethod("createTempDirectory", pathClass, String.class, fileAttributeArray.getClass());
            Object parentPath = File.class.getDeclaredMethod("toPath", new Class[0]).invoke(parentFolder, new Object[0]);
            tempDir = createTempDirectoryMethod.invoke(null, parentPath, "junit", fileAttributeArray);
        } else {
            Method createTempDirectoryMethod2 = filesClass.getDeclaredMethod("createTempDirectory", String.class, fileAttributeArray.getClass());
            tempDir = createTempDirectoryMethod2.invoke(null, "junit", fileAttributeArray);
        }
        return (File) pathClass.getDeclaredMethod("toFile", new Class[0]).invoke(tempDir, new Object[0]);
    }

    private static File createTemporaryFolderWithFileApi(File parentFolder) throws IOException {
        File createdFolder = null;
        for (int i = 0; i < 10000; i++) {
            File tmpFile = File.createTempFile("junit", ".tmp", parentFolder);
            String tmpName = tmpFile.toString();
            String folderName = tmpName.substring(0, tmpName.length() - ".tmp".length());
            createdFolder = new File(folderName);
            if (createdFolder.mkdir()) {
                tmpFile.delete();
                return createdFolder;
            }
            tmpFile.delete();
        }
        throw new IOException("Unable to create temporary directory in: " + parentFolder.toString() + ". Tried 10000 times. Last attempted to create: " + createdFolder.toString());
    }

    public File getRoot() {
        if (this.folder == null) {
            throw new IllegalStateException("the temporary folder has not yet been created");
        }
        return this.folder;
    }

    public void delete() {
        if (!tryDelete() && this.assureDeletion) {
            Assert.fail("Unable to clean up temporary folder " + this.folder);
        }
    }

    private boolean tryDelete() {
        if (this.folder == null) {
            return true;
        }
        return recursiveDelete(this.folder);
    }

    private boolean recursiveDelete(File file) {
        if (file.delete()) {
            return true;
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File each : files) {
                if (!recursiveDelete(each)) {
                    return false;
                }
            }
        }
        return file.delete();
    }
}
