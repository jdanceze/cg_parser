package polyglot.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import polyglot.main.Report;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/TypeEncoder.class */
public class TypeEncoder {
    protected TypeSystem ts;
    protected final boolean zip = false;
    protected final boolean base64 = true;
    protected final boolean test = false;

    public TypeEncoder(TypeSystem ts) {
        this.ts = ts;
    }

    public String encode(Type t) throws IOException {
        if (Report.should_report(Report.serialize, 1)) {
            Report.report(1, new StringBuffer().append("Encoding type ").append(t).toString());
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new TypeOutputStream(baos, this.ts, t);
        oos.writeObject(t);
        oos.flush();
        oos.close();
        byte[] b = baos.toByteArray();
        if (Report.should_report(Report.serialize, 2)) {
            Report.report(2, new StringBuffer().append("Size of serialization (without zipping) is ").append(b.length).append(" bytes").toString());
        }
        String s = new String(Base64.encode(b));
        if (Report.should_report(Report.serialize, 2)) {
            Report.report(2, new StringBuffer().append("Size of serialization after conversion to string is ").append(s.length()).append(" characters").toString());
        }
        return s;
    }

    public Type decode(String s) throws InvalidClassException {
        byte[] b = Base64.decode(s.toCharArray());
        try {
            ObjectInputStream ois = new TypeInputStream(new ByteArrayInputStream(b), this.ts);
            return (Type) ois.readObject();
        } catch (InvalidClassException e) {
            throw e;
        } catch (IOException e2) {
            throw new InternalCompilerError(new StringBuffer().append("IOException thrown while decoding serialized type info: ").append(e2.getMessage()).toString(), e2);
        } catch (ClassNotFoundException e3) {
            throw new InternalCompilerError(new StringBuffer().append("Unable to find one of the classes for the serialized type info: ").append(e3.getMessage()).toString(), e3);
        }
    }
}
