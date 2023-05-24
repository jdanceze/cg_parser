package com.oreilly.servlet;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.FileRenamePolicy;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpUtils;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/MultipartRequest.class */
public class MultipartRequest {
    private static final int DEFAULT_MAX_POST_SIZE = 1048576;
    protected Hashtable parameters;
    protected Hashtable files;

    public MultipartRequest(HttpServletRequest request, String saveDirectory) throws IOException {
        this(request, saveDirectory, 1048576);
    }

    public MultipartRequest(HttpServletRequest request, String saveDirectory, int maxPostSize) throws IOException {
        this(request, saveDirectory, maxPostSize, null, null);
    }

    public MultipartRequest(HttpServletRequest request, String saveDirectory, String encoding) throws IOException {
        this(request, saveDirectory, 1048576, encoding, null);
    }

    public MultipartRequest(HttpServletRequest request, String saveDirectory, int maxPostSize, FileRenamePolicy policy) throws IOException {
        this(request, saveDirectory, maxPostSize, null, policy);
    }

    public MultipartRequest(HttpServletRequest request, String saveDirectory, int maxPostSize, String encoding) throws IOException {
        this(request, saveDirectory, maxPostSize, encoding, null);
    }

    public MultipartRequest(HttpServletRequest request, String saveDirectory, int maxPostSize, String encoding, FileRenamePolicy policy) throws IOException {
        this.parameters = new Hashtable();
        this.files = new Hashtable();
        if (request == null) {
            throw new IllegalArgumentException("request cannot be null");
        }
        if (saveDirectory == null) {
            throw new IllegalArgumentException("saveDirectory cannot be null");
        }
        if (maxPostSize <= 0) {
            throw new IllegalArgumentException("maxPostSize must be positive");
        }
        File dir = new File(saveDirectory);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(new StringBuffer().append("Not a directory: ").append(saveDirectory).toString());
        }
        if (!dir.canWrite()) {
            throw new IllegalArgumentException(new StringBuffer().append("Not writable: ").append(saveDirectory).toString());
        }
        MultipartParser parser = new MultipartParser(request, maxPostSize, true, true, encoding);
        if (request.getQueryString() != null) {
            Hashtable queryParameters = HttpUtils.parseQueryString(request.getQueryString());
            Enumeration queryParameterNames = queryParameters.keys();
            while (queryParameterNames.hasMoreElements()) {
                Object paramName = queryParameterNames.nextElement();
                String[] values = (String[]) queryParameters.get(paramName);
                Vector newValues = new Vector();
                for (String str : values) {
                    newValues.add(str);
                }
                this.parameters.put(paramName, newValues);
            }
        }
        while (true) {
            Part readNextPart = parser.readNextPart();
            if (readNextPart != null) {
                String name = readNextPart.getName();
                if (readNextPart.isParam()) {
                    ParamPart paramPart = (ParamPart) readNextPart;
                    String value = paramPart.getStringValue();
                    Vector existingValues = (Vector) this.parameters.get(name);
                    if (existingValues == null) {
                        existingValues = new Vector();
                        this.parameters.put(name, existingValues);
                    }
                    existingValues.addElement(value);
                } else if (readNextPart.isFile()) {
                    FilePart filePart = (FilePart) readNextPart;
                    String fileName = filePart.getFileName();
                    if (fileName != null) {
                        filePart.setRenamePolicy(policy);
                        filePart.writeTo(dir);
                        this.files.put(name, new UploadedFile(dir.toString(), filePart.getFileName(), fileName, filePart.getContentType()));
                    } else {
                        this.files.put(name, new UploadedFile(null, null, null, null));
                    }
                }
            } else {
                return;
            }
        }
    }

    public MultipartRequest(ServletRequest request, String saveDirectory) throws IOException {
        this((HttpServletRequest) request, saveDirectory);
    }

    public MultipartRequest(ServletRequest request, String saveDirectory, int maxPostSize) throws IOException {
        this((HttpServletRequest) request, saveDirectory, maxPostSize);
    }

    public Enumeration getParameterNames() {
        return this.parameters.keys();
    }

    public Enumeration getFileNames() {
        return this.files.keys();
    }

    public String getParameter(String name) {
        try {
            Vector values = (Vector) this.parameters.get(name);
            if (values == null || values.size() == 0) {
                return null;
            }
            String value = (String) values.elementAt(values.size() - 1);
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    public String[] getParameterValues(String name) {
        try {
            Vector values = (Vector) this.parameters.get(name);
            if (values == null || values.size() == 0) {
                return null;
            }
            String[] valuesArray = new String[values.size()];
            values.copyInto(valuesArray);
            return valuesArray;
        } catch (Exception e) {
            return null;
        }
    }

    public String getFilesystemName(String name) {
        try {
            UploadedFile file = (UploadedFile) this.files.get(name);
            return file.getFilesystemName();
        } catch (Exception e) {
            return null;
        }
    }

    public String getOriginalFileName(String name) {
        try {
            UploadedFile file = (UploadedFile) this.files.get(name);
            return file.getOriginalFileName();
        } catch (Exception e) {
            return null;
        }
    }

    public String getContentType(String name) {
        try {
            UploadedFile file = (UploadedFile) this.files.get(name);
            return file.getContentType();
        } catch (Exception e) {
            return null;
        }
    }

    public File getFile(String name) {
        try {
            UploadedFile file = (UploadedFile) this.files.get(name);
            return file.getFile();
        } catch (Exception e) {
            return null;
        }
    }
}
