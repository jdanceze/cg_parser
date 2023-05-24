package org.apache.tools.ant.types.resources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/JavaConstantResource.class */
public class JavaConstantResource extends AbstractClasspathResource {
    @Override // org.apache.tools.ant.types.resources.AbstractClasspathResource
    protected InputStream openInputStream(ClassLoader cl) throws IOException {
        Class<?> cls;
        String constant = getName();
        if (constant == null) {
            throw new IOException("Attribute 'name' must be set.");
        }
        int index = constant.lastIndexOf(46);
        if (index < 0) {
            throw new IOException("No class name in " + constant);
        }
        String classname = constant.substring(0, index);
        String fieldname = constant.substring(index + 1);
        try {
            if (cl != null) {
                cls = Class.forName(classname, true, cl);
            } else {
                cls = Class.forName(classname);
            }
            Class<?> clazz = cls;
            Field field = clazz.getField(fieldname);
            String value = field.get(null).toString();
            return new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8));
        } catch (ClassNotFoundException e) {
            throw new IOException("Class not found:" + classname);
        } catch (IllegalAccessException e2) {
            throw new IOException("Illegal access to :" + fieldname + " in " + classname);
        } catch (NoSuchFieldException e3) {
            throw new IOException("Field not found:" + fieldname + " in " + classname);
        } catch (NullPointerException e4) {
            throw new IOException("Not a static field: " + fieldname + " in " + classname);
        }
    }
}
