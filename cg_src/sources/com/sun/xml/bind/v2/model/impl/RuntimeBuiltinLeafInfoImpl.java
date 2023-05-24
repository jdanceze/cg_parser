package com.sun.xml.bind.v2.model.impl;

import android.R;
import android.provider.MediaStore;
import com.sun.istack.ByteArrayDataSource;
import com.sun.xml.bind.DatatypeConverterImpl;
import com.sun.xml.bind.WhiteSpaceProcessor;
import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.TODO;
import com.sun.xml.bind.v2.model.runtime.RuntimeBuiltinLeafInfo;
import com.sun.xml.bind.v2.runtime.Name;
import com.sun.xml.bind.v2.runtime.Transducer;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import com.sun.xml.bind.v2.runtime.output.Pcdata;
import com.sun.xml.bind.v2.runtime.unmarshaller.Base64Data;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import com.sun.xml.bind.v2.util.ByteArrayOutputStreamEx;
import com.sun.xml.bind.v2.util.DataSourceSource;
import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.xml.bind.MarshalException;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/RuntimeBuiltinLeafInfoImpl.class */
public abstract class RuntimeBuiltinLeafInfoImpl<T> extends BuiltinLeafInfoImpl<Type, Class> implements RuntimeBuiltinLeafInfo, Transducer<T> {
    private static final Logger logger = com.sun.xml.bind.Util.getClassLogger();
    public static final Map<Type, RuntimeBuiltinLeafInfoImpl<?>> LEAVES = new HashMap();
    public static final RuntimeBuiltinLeafInfoImpl<String> STRING;
    private static final String DATE = "date";
    public static final List<RuntimeBuiltinLeafInfoImpl<?>> builtinBeanInfos;
    public static final String MAP_ANYURI_TO_URI = "mapAnyUriToUri";
    public static final String USE_OLD_GMONTH_MAPPING = "jaxb.ri.useOldGmonthMapping";
    private static final Map<QName, String> xmlGregorianCalendarFormatString;
    private static final Map<QName, Integer> xmlGregorianCalendarFieldRef;

    static {
        String MAP_ANYURI_TO_URI_VALUE = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public String run() {
                return System.getProperty(RuntimeBuiltinLeafInfoImpl.MAP_ANYURI_TO_URI);
            }
        });
        QName[] qnames = MAP_ANYURI_TO_URI_VALUE == null ? new QName[]{createXS("string"), createXS("anySimpleType"), createXS("normalizedString"), createXS("anyURI"), createXS("token"), createXS(MediaStore.Video.VideoColumns.LANGUAGE), createXS("Name"), createXS("NCName"), createXS("NMTOKEN"), createXS("ENTITY")} : new QName[]{createXS("string"), createXS("anySimpleType"), createXS("normalizedString"), createXS("token"), createXS(MediaStore.Video.VideoColumns.LANGUAGE), createXS("Name"), createXS("NCName"), createXS("NMTOKEN"), createXS("ENTITY")};
        STRING = new StringImplImpl(String.class, qnames);
        ArrayList<RuntimeBuiltinLeafInfoImpl<?>> secondaryList = new ArrayList<>();
        secondaryList.add(new StringImpl<Character>(Character.class, createXS("unsignedShort")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.2
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public Character parse(CharSequence text) {
                return Character.valueOf((char) DatatypeConverterImpl._parseInt(text));
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(Character v) {
                return Integer.toString(v.charValue());
            }
        });
        secondaryList.add(new StringImpl<Calendar>(Calendar.class, DatatypeConstants.DATETIME) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.3
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public Calendar parse(CharSequence text) {
                return DatatypeConverterImpl._parseDateTime(text.toString());
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(Calendar v) {
                return DatatypeConverterImpl._printDateTime(v);
            }
        });
        secondaryList.add(new StringImpl<GregorianCalendar>(GregorianCalendar.class, DatatypeConstants.DATETIME) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.4
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public GregorianCalendar parse(CharSequence text) {
                return DatatypeConverterImpl._parseDateTime(text.toString());
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(GregorianCalendar v) {
                return DatatypeConverterImpl._printDateTime(v);
            }
        });
        secondaryList.add(new StringImpl<Date>(Date.class, DatatypeConstants.DATETIME) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.5
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public Date parse(CharSequence text) {
                return DatatypeConverterImpl._parseDateTime(text.toString()).getTime();
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(Date v) {
                XMLSerializer xs = XMLSerializer.getInstance();
                QName type = xs.getSchemaType();
                GregorianCalendar cal = new GregorianCalendar(0, 0, 0);
                cal.setTime(v);
                if (type != null && "http://www.w3.org/2001/XMLSchema".equals(type.getNamespaceURI()) && "date".equals(type.getLocalPart())) {
                    return DatatypeConverterImpl._printDate(cal);
                }
                return DatatypeConverterImpl._printDateTime(cal);
            }
        });
        secondaryList.add(new StringImpl<File>(File.class, createXS("string")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.6
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public File parse(CharSequence text) {
                return new File(WhiteSpaceProcessor.trim(text).toString());
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(File v) {
                return v.getPath();
            }
        });
        secondaryList.add(new StringImpl<URL>(URL.class, createXS("anyURI")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.7
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public URL parse(CharSequence text) throws SAXException {
                TODO.checkSpec("JSR222 Issue #42");
                try {
                    return new URL(WhiteSpaceProcessor.trim(text).toString());
                } catch (MalformedURLException e) {
                    UnmarshallingContext.getInstance().handleError(e);
                    return null;
                }
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(URL v) {
                return v.toExternalForm();
            }
        });
        if (MAP_ANYURI_TO_URI_VALUE == null) {
            secondaryList.add(new StringImpl<URI>(URI.class, createXS("string")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.8
                @Override // com.sun.xml.bind.v2.runtime.Transducer
                public URI parse(CharSequence text) throws SAXException {
                    try {
                        return new URI(text.toString());
                    } catch (URISyntaxException e) {
                        UnmarshallingContext.getInstance().handleError(e);
                        return null;
                    }
                }

                @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
                public String print(URI v) {
                    return v.toString();
                }
            });
        }
        secondaryList.add(new StringImpl<Class>(Class.class, createXS("string")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.9
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public Class parse(CharSequence text) throws SAXException {
                TODO.checkSpec("JSR222 Issue #42");
                try {
                    String name = WhiteSpaceProcessor.trim(text).toString();
                    ClassLoader cl = UnmarshallingContext.getInstance().classLoader;
                    if (cl == null) {
                        cl = Thread.currentThread().getContextClassLoader();
                    }
                    if (cl != null) {
                        return cl.loadClass(name);
                    }
                    return Class.forName(name);
                } catch (ClassNotFoundException e) {
                    UnmarshallingContext.getInstance().handleError(e);
                    return null;
                }
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(Class v) {
                return v.getName();
            }
        });
        secondaryList.add(new PcdataImpl<Image>(Image.class, createXS("base64Binary")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.10
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public Image parse(CharSequence text) throws SAXException {
                InputStream is;
                try {
                    if (text instanceof Base64Data) {
                        is = ((Base64Data) text).getInputStream();
                    } else {
                        is = new ByteArrayInputStream(RuntimeBuiltinLeafInfoImpl.decodeBase64(text));
                    }
                    BufferedImage read = ImageIO.read(is);
                    is.close();
                    return read;
                } catch (IOException e) {
                    UnmarshallingContext.getInstance().handleError(e);
                    return null;
                }
            }

            private BufferedImage convertToBufferedImage(Image image) throws IOException {
                if (image instanceof BufferedImage) {
                    return (BufferedImage) image;
                }
                MediaTracker tracker = new MediaTracker(new Component() { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.10.1
                });
                tracker.addImage(image, 0);
                try {
                    tracker.waitForAll();
                    BufferedImage bufImage = new BufferedImage(image.getWidth((ImageObserver) null), image.getHeight((ImageObserver) null), 2);
                    bufImage.createGraphics().drawImage(image, 0, 0, (ImageObserver) null);
                    return bufImage;
                } catch (InterruptedException e) {
                    throw new IOException(e.getMessage());
                }
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.PcdataImpl, com.sun.xml.bind.v2.runtime.Transducer
            public Base64Data print(Image v) {
                ByteArrayOutputStreamEx imageData = new ByteArrayOutputStreamEx();
                XMLSerializer xs = XMLSerializer.getInstance();
                String mimeType = xs.getXMIMEContentType();
                mimeType = (mimeType == null || mimeType.startsWith("image/*")) ? "image/png" : "image/png";
                try {
                    Iterator<ImageWriter> itr = ImageIO.getImageWritersByMIMEType(mimeType);
                    if (itr.hasNext()) {
                        ImageWriter w = itr.next();
                        ImageOutputStream os = ImageIO.createImageOutputStream(imageData);
                        w.setOutput(os);
                        w.write(convertToBufferedImage(v));
                        os.close();
                        w.dispose();
                        Base64Data bd = new Base64Data();
                        imageData.set(bd, mimeType);
                        return bd;
                    }
                    xs.handleEvent(new ValidationEventImpl(1, Messages.NO_IMAGE_WRITER.format(mimeType), xs.getCurrentLocation(null)));
                    throw new RuntimeException("no encoder for MIME type " + mimeType);
                } catch (IOException e) {
                    xs.handleError(e);
                    throw new RuntimeException(e);
                }
            }
        });
        secondaryList.add(new PcdataImpl<DataHandler>(DataHandler.class, createXS("base64Binary")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.11
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public DataHandler parse(CharSequence text) {
                if (text instanceof Base64Data) {
                    return ((Base64Data) text).getDataHandler();
                }
                return new DataHandler(new ByteArrayDataSource(RuntimeBuiltinLeafInfoImpl.decodeBase64(text), UnmarshallingContext.getInstance().getXMIMEContentType()));
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.PcdataImpl, com.sun.xml.bind.v2.runtime.Transducer
            public Base64Data print(DataHandler v) {
                Base64Data bd = new Base64Data();
                bd.set(v);
                return bd;
            }
        });
        secondaryList.add(new PcdataImpl<Source>(Source.class, createXS("base64Binary")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.12
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public Source parse(CharSequence text) throws SAXException {
                try {
                    if (text instanceof Base64Data) {
                        return new DataSourceSource(((Base64Data) text).getDataHandler());
                    }
                    return new DataSourceSource(new ByteArrayDataSource(RuntimeBuiltinLeafInfoImpl.decodeBase64(text), UnmarshallingContext.getInstance().getXMIMEContentType()));
                } catch (MimeTypeParseException e) {
                    UnmarshallingContext.getInstance().handleError(e);
                    return null;
                }
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.PcdataImpl, com.sun.xml.bind.v2.runtime.Transducer
            public Base64Data print(Source v) {
                DataSource ds;
                String dsct;
                XMLSerializer xs = XMLSerializer.getInstance();
                Base64Data bd = new Base64Data();
                String contentType = xs.getXMIMEContentType();
                MimeType mt = null;
                if (contentType != null) {
                    try {
                        mt = new MimeType(contentType);
                    } catch (MimeTypeParseException e) {
                        xs.handleError(e);
                    }
                }
                if ((v instanceof DataSourceSource) && (dsct = (ds = ((DataSourceSource) v).getDataSource()).getContentType()) != null && (contentType == null || contentType.equals(dsct))) {
                    bd.set(new DataHandler(ds));
                    return bd;
                }
                String charset = null;
                if (mt != null) {
                    charset = mt.getParameter("charset");
                }
                if (charset == null) {
                    charset = "UTF-8";
                }
                try {
                    ByteArrayOutputStreamEx baos = new ByteArrayOutputStreamEx();
                    Transformer tr = xs.getIdentityTransformer();
                    String defaultEncoding = tr.getOutputProperty(OutputKeys.ENCODING);
                    tr.setOutputProperty(OutputKeys.ENCODING, charset);
                    tr.transform(v, new StreamResult(new OutputStreamWriter(baos, charset)));
                    tr.setOutputProperty(OutputKeys.ENCODING, defaultEncoding);
                    baos.set(bd, "application/xml; charset=" + charset);
                    return bd;
                } catch (UnsupportedEncodingException e2) {
                    xs.handleError(e2);
                    bd.set(new byte[0], "application/xml");
                    return bd;
                } catch (TransformerException e3) {
                    xs.handleError(e3);
                    bd.set(new byte[0], "application/xml");
                    return bd;
                }
            }
        });
        secondaryList.add(new StringImpl<XMLGregorianCalendar>(XMLGregorianCalendar.class, createXS("anySimpleType"), DatatypeConstants.DATE, DatatypeConstants.DATETIME, DatatypeConstants.TIME, DatatypeConstants.GMONTH, DatatypeConstants.GDAY, DatatypeConstants.GYEAR, DatatypeConstants.GYEARMONTH, DatatypeConstants.GMONTHDAY) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.13
            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(XMLGregorianCalendar cal) {
                XMLSerializer xs = XMLSerializer.getInstance();
                QName type = xs.getSchemaType();
                if (type != null) {
                    try {
                        RuntimeBuiltinLeafInfoImpl.checkXmlGregorianCalendarFieldRef(type, cal);
                        String format = (String) RuntimeBuiltinLeafInfoImpl.xmlGregorianCalendarFormatString.get(type);
                        if (format != null) {
                            return format(format, cal);
                        }
                    } catch (MarshalException e) {
                        xs.handleEvent(new ValidationEventImpl(0, e.getMessage(), xs.getCurrentLocation(null)));
                        return "";
                    }
                }
                return cal.toXMLFormat();
            }

            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public XMLGregorianCalendar parse(CharSequence lexical) throws SAXException {
                try {
                    return DatatypeConverterImpl.getDatatypeFactory().newXMLGregorianCalendar(lexical.toString().trim());
                } catch (Exception e) {
                    UnmarshallingContext.getInstance().handleError(e);
                    return null;
                }
            }

            private String format(String format, XMLGregorianCalendar value) {
                StringBuilder buf = new StringBuilder();
                int fidx = 0;
                int flen = format.length();
                while (fidx < flen) {
                    int i = fidx;
                    fidx++;
                    char fch = format.charAt(i);
                    if (fch != '%') {
                        buf.append(fch);
                    } else {
                        fidx++;
                        switch (format.charAt(fidx)) {
                            case 'D':
                                printNumber(buf, value.getDay(), 2);
                                continue;
                            case 'M':
                                printNumber(buf, value.getMonth(), 2);
                                continue;
                            case 'Y':
                                printNumber(buf, value.getEonAndYear(), 4);
                                continue;
                            case 'h':
                                printNumber(buf, value.getHour(), 2);
                                continue;
                            case 'm':
                                printNumber(buf, value.getMinute(), 2);
                                continue;
                            case 's':
                                printNumber(buf, value.getSecond(), 2);
                                if (value.getFractionalSecond() != null) {
                                    String frac = value.getFractionalSecond().toPlainString();
                                    buf.append(frac.substring(1, frac.length()));
                                    break;
                                } else {
                                    continue;
                                }
                            case 'z':
                                int offset = value.getTimezone();
                                if (offset == 0) {
                                    buf.append('Z');
                                    break;
                                } else if (offset != Integer.MIN_VALUE) {
                                    if (offset < 0) {
                                        buf.append('-');
                                        offset *= -1;
                                    } else {
                                        buf.append('+');
                                    }
                                    printNumber(buf, offset / 60, 2);
                                    buf.append(':');
                                    printNumber(buf, offset % 60, 2);
                                    break;
                                } else {
                                    continue;
                                }
                            default:
                                throw new InternalError();
                        }
                    }
                }
                return buf.toString();
            }

            private void printNumber(StringBuilder out, BigInteger number, int nDigits) {
                String s = number.toString();
                for (int i = s.length(); i < nDigits; i++) {
                    out.append('0');
                }
                out.append(s);
            }

            private void printNumber(StringBuilder out, int number, int nDigits) {
                String s = String.valueOf(number);
                for (int i = s.length(); i < nDigits; i++) {
                    out.append('0');
                }
                out.append(s);
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl, com.sun.xml.bind.v2.runtime.Transducer
            public QName getTypeName(XMLGregorianCalendar cal) {
                return cal.getXMLSchemaType();
            }
        });
        ArrayList<RuntimeBuiltinLeafInfoImpl<?>> primaryList = new ArrayList<>();
        primaryList.add(STRING);
        primaryList.add(new StringImpl<Boolean>(Boolean.class, createXS("boolean")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.14
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public Boolean parse(CharSequence text) {
                return DatatypeConverterImpl._parseBoolean(text);
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(Boolean v) {
                return v.toString();
            }
        });
        primaryList.add(new PcdataImpl<byte[]>(byte[].class, createXS("base64Binary"), createXS("hexBinary")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.15
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public byte[] parse(CharSequence text) {
                return RuntimeBuiltinLeafInfoImpl.decodeBase64(text);
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.PcdataImpl, com.sun.xml.bind.v2.runtime.Transducer
            public Base64Data print(byte[] v) {
                XMLSerializer w = XMLSerializer.getInstance();
                Base64Data bd = new Base64Data();
                String mimeType = w.getXMIMEContentType();
                bd.set(v, mimeType);
                return bd;
            }
        });
        primaryList.add(new StringImpl<Byte>(Byte.class, createXS("byte")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.16
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public Byte parse(CharSequence text) {
                return Byte.valueOf(DatatypeConverterImpl._parseByte(text));
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(Byte v) {
                return DatatypeConverterImpl._printByte(v.byteValue());
            }
        });
        primaryList.add(new StringImpl<Short>(Short.class, createXS("short"), createXS("unsignedByte")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.17
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public Short parse(CharSequence text) {
                return Short.valueOf(DatatypeConverterImpl._parseShort(text));
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(Short v) {
                return DatatypeConverterImpl._printShort(v.shortValue());
            }
        });
        primaryList.add(new StringImpl<Integer>(Integer.class, createXS("int"), createXS("unsignedShort")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.18
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public Integer parse(CharSequence text) {
                return Integer.valueOf(DatatypeConverterImpl._parseInt(text));
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(Integer v) {
                return DatatypeConverterImpl._printInt(v.intValue());
            }
        });
        primaryList.add(new StringImpl<Long>(Long.class, createXS("long"), createXS("unsignedInt")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.19
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public Long parse(CharSequence text) {
                return Long.valueOf(DatatypeConverterImpl._parseLong(text));
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(Long v) {
                return DatatypeConverterImpl._printLong(v.longValue());
            }
        });
        primaryList.add(new StringImpl<Float>(Float.class, createXS(Jimple.FLOAT)) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.20
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public Float parse(CharSequence text) {
                return Float.valueOf(DatatypeConverterImpl._parseFloat(text.toString()));
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(Float v) {
                return DatatypeConverterImpl._printFloat(v.floatValue());
            }
        });
        primaryList.add(new StringImpl<Double>(Double.class, createXS("double")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.21
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public Double parse(CharSequence text) {
                return Double.valueOf(DatatypeConverterImpl._parseDouble(text));
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(Double v) {
                return DatatypeConverterImpl._printDouble(v.doubleValue());
            }
        });
        primaryList.add(new StringImpl<BigInteger>(BigInteger.class, createXS("integer"), createXS("positiveInteger"), createXS("negativeInteger"), createXS("nonPositiveInteger"), createXS("nonNegativeInteger"), createXS("unsignedLong")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.22
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public BigInteger parse(CharSequence text) {
                return DatatypeConverterImpl._parseInteger(text);
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(BigInteger v) {
                return DatatypeConverterImpl._printInteger(v);
            }
        });
        primaryList.add(new StringImpl<BigDecimal>(BigDecimal.class, createXS("decimal")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.23
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public BigDecimal parse(CharSequence text) {
                return DatatypeConverterImpl._parseDecimal(text.toString());
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(BigDecimal v) {
                return DatatypeConverterImpl._printDecimal(v);
            }
        });
        primaryList.add(new StringImpl<QName>(QName.class, createXS("QName")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.24
            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public QName parse(CharSequence text) throws SAXException {
                try {
                    return DatatypeConverterImpl._parseQName(text.toString(), UnmarshallingContext.getInstance());
                } catch (IllegalArgumentException e) {
                    UnmarshallingContext.getInstance().handleError(e);
                    return null;
                }
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(QName v) {
                return DatatypeConverterImpl._printQName(v, XMLSerializer.getInstance().getNamespaceContext());
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl, com.sun.xml.bind.v2.runtime.Transducer
            public boolean useNamespace() {
                return true;
            }

            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl, com.sun.xml.bind.v2.runtime.Transducer
            public void declareNamespace(QName v, XMLSerializer w) {
                w.getNamespaceContext().declareNamespace(v.getNamespaceURI(), v.getPrefix(), false);
            }
        });
        if (MAP_ANYURI_TO_URI_VALUE != null) {
            primaryList.add(new StringImpl<URI>(URI.class, createXS("anyURI")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.25
                @Override // com.sun.xml.bind.v2.runtime.Transducer
                public URI parse(CharSequence text) throws SAXException {
                    try {
                        return new URI(text.toString());
                    } catch (URISyntaxException e) {
                        UnmarshallingContext.getInstance().handleError(e);
                        return null;
                    }
                }

                @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
                public String print(URI v) {
                    return v.toString();
                }
            });
        }
        primaryList.add(new StringImpl<Duration>(Duration.class, createXS("duration")) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.26
            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(Duration duration) {
                return duration.toString();
            }

            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public Duration parse(CharSequence lexical) {
                TODO.checkSpec("JSR222 Issue #42");
                return DatatypeConverterImpl.getDatatypeFactory().newDuration(lexical.toString());
            }
        });
        primaryList.add(new StringImpl<Void>(Void.class, new QName[0]) { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.27
            @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
            public String print(Void value) {
                return "";
            }

            @Override // com.sun.xml.bind.v2.runtime.Transducer
            public Void parse(CharSequence lexical) {
                return null;
            }
        });
        List<RuntimeBuiltinLeafInfoImpl<?>> l = new ArrayList<>(secondaryList.size() + primaryList.size() + 1);
        l.addAll(secondaryList);
        try {
            l.add(new UUIDImpl());
        } catch (LinkageError e) {
        }
        l.addAll(primaryList);
        builtinBeanInfos = Collections.unmodifiableList(l);
        xmlGregorianCalendarFormatString = new HashMap();
        Map<QName, String> m = xmlGregorianCalendarFormatString;
        m.put(DatatypeConstants.DATETIME, "%Y-%M-%DT%h:%m:%s%z");
        m.put(DatatypeConstants.DATE, "%Y-%M-%D%z");
        m.put(DatatypeConstants.TIME, "%h:%m:%s%z");
        String oldGmonthMappingProperty = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.28
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public String run() {
                return System.getProperty(RuntimeBuiltinLeafInfoImpl.USE_OLD_GMONTH_MAPPING);
            }
        });
        if (oldGmonthMappingProperty == null) {
            m.put(DatatypeConstants.GMONTH, "--%M%z");
        } else {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "Old GMonth mapping used.");
            }
            m.put(DatatypeConstants.GMONTH, "--%M--%z");
        }
        m.put(DatatypeConstants.GDAY, "---%D%z");
        m.put(DatatypeConstants.GYEAR, "%Y%z");
        m.put(DatatypeConstants.GYEARMONTH, "%Y-%M%z");
        m.put(DatatypeConstants.GMONTHDAY, "--%M-%D%z");
        xmlGregorianCalendarFieldRef = new HashMap();
        Map<QName, Integer> f = xmlGregorianCalendarFieldRef;
        f.put(DatatypeConstants.DATETIME, 17895697);
        f.put(DatatypeConstants.DATE, 17895424);
        f.put(DatatypeConstants.TIME, 16777489);
        f.put(DatatypeConstants.GDAY, 16781312);
        f.put(DatatypeConstants.GMONTH, Integer.valueOf((int) R.attr.theme));
        f.put(DatatypeConstants.GYEAR, 17825792);
        f.put(DatatypeConstants.GYEARMONTH, 17891328);
        f.put(DatatypeConstants.GMONTHDAY, 16846848);
    }

    private RuntimeBuiltinLeafInfoImpl(Class type, QName... typeNames) {
        super(type, typeNames);
        LEAVES.put(type, this);
    }

    @Override // com.sun.xml.bind.v2.model.runtime.RuntimeLeafInfo
    public final Class getClazz() {
        return (Class) getType();
    }

    @Override // com.sun.xml.bind.v2.model.runtime.RuntimeLeafInfo, com.sun.xml.bind.v2.model.runtime.RuntimeNonElement
    public final Transducer getTransducer() {
        return this;
    }

    @Override // com.sun.xml.bind.v2.runtime.Transducer
    public boolean useNamespace() {
        return false;
    }

    @Override // com.sun.xml.bind.v2.runtime.Transducer
    public void declareNamespace(T o, XMLSerializer w) throws AccessorException {
    }

    @Override // com.sun.xml.bind.v2.runtime.Transducer
    public QName getTypeName(T instance) {
        return null;
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/RuntimeBuiltinLeafInfoImpl$StringImpl.class */
    private static abstract class StringImpl<T> extends RuntimeBuiltinLeafInfoImpl<T> {
        public abstract String print(T t) throws AccessorException;

        /* JADX WARN: Multi-variable type inference failed */
        public /* bridge */ /* synthetic */ CharSequence print(Object obj) throws AccessorException {
            return print((StringImpl<T>) obj);
        }

        protected StringImpl(Class type, QName... typeNames) {
            super(type, typeNames);
        }

        @Override // com.sun.xml.bind.v2.runtime.Transducer
        public void writeText(XMLSerializer w, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
            w.text(print((StringImpl<T>) o), fieldName);
        }

        @Override // com.sun.xml.bind.v2.runtime.Transducer
        public void writeLeafElement(XMLSerializer w, Name tagName, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
            w.leafElement(tagName, print((StringImpl<T>) o), fieldName);
        }
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/RuntimeBuiltinLeafInfoImpl$PcdataImpl.class */
    private static abstract class PcdataImpl<T> extends RuntimeBuiltinLeafInfoImpl<T> {
        public abstract Pcdata print(T t) throws AccessorException;

        /* JADX WARN: Multi-variable type inference failed */
        public /* bridge */ /* synthetic */ CharSequence print(Object obj) throws AccessorException {
            return print((PcdataImpl<T>) obj);
        }

        protected PcdataImpl(Class type, QName... typeNames) {
            super(type, typeNames);
        }

        @Override // com.sun.xml.bind.v2.runtime.Transducer
        public final void writeText(XMLSerializer w, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
            w.text(print((PcdataImpl<T>) o), fieldName);
        }

        @Override // com.sun.xml.bind.v2.runtime.Transducer
        public final void writeLeafElement(XMLSerializer w, Name tagName, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
            w.leafElement(tagName, print((PcdataImpl<T>) o), fieldName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static QName createXS(String typeName) {
        return new QName("http://www.w3.org/2001/XMLSchema", typeName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] decodeBase64(CharSequence text) {
        if (text instanceof Base64Data) {
            Base64Data base64Data = (Base64Data) text;
            return base64Data.getExact();
        }
        return DatatypeConverterImpl._parseBase64Binary(text.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkXmlGregorianCalendarFieldRef(QName type, XMLGregorianCalendar cal) throws MarshalException {
        StringBuilder buf = new StringBuilder();
        int bitField = xmlGregorianCalendarFieldRef.get(type).intValue();
        int pos = 0;
        while (bitField != 0) {
            int bit = bitField & 1;
            bitField >>>= 4;
            pos++;
            if (bit == 1) {
                switch (pos) {
                    case 1:
                        if (cal.getSecond() == Integer.MIN_VALUE) {
                            buf.append("  ").append(Messages.XMLGREGORIANCALENDAR_SEC);
                            break;
                        } else {
                            continue;
                        }
                    case 2:
                        if (cal.getMinute() == Integer.MIN_VALUE) {
                            buf.append("  ").append(Messages.XMLGREGORIANCALENDAR_MIN);
                            break;
                        } else {
                            continue;
                        }
                    case 3:
                        if (cal.getHour() == Integer.MIN_VALUE) {
                            buf.append("  ").append(Messages.XMLGREGORIANCALENDAR_HR);
                            break;
                        } else {
                            continue;
                        }
                    case 4:
                        if (cal.getDay() == Integer.MIN_VALUE) {
                            buf.append("  ").append(Messages.XMLGREGORIANCALENDAR_DAY);
                            break;
                        } else {
                            continue;
                        }
                    case 5:
                        if (cal.getMonth() == Integer.MIN_VALUE) {
                            buf.append("  ").append(Messages.XMLGREGORIANCALENDAR_MONTH);
                            break;
                        } else {
                            continue;
                        }
                    case 6:
                        if (cal.getYear() == Integer.MIN_VALUE) {
                            buf.append("  ").append(Messages.XMLGREGORIANCALENDAR_YEAR);
                            break;
                        } else {
                            continue;
                        }
                }
            }
        }
        if (buf.length() > 0) {
            throw new MarshalException(Messages.XMLGREGORIANCALENDAR_INVALID.format(type.getLocalPart()) + buf.toString());
        }
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/RuntimeBuiltinLeafInfoImpl$UUIDImpl.class */
    private static class UUIDImpl extends StringImpl<UUID> {
        public UUIDImpl() {
            super(UUID.class, RuntimeBuiltinLeafInfoImpl.createXS("string"));
        }

        @Override // com.sun.xml.bind.v2.runtime.Transducer
        public UUID parse(CharSequence text) throws SAXException {
            TODO.checkSpec("JSR222 Issue #42");
            try {
                return UUID.fromString(WhiteSpaceProcessor.trim(text).toString());
            } catch (IllegalArgumentException e) {
                UnmarshallingContext.getInstance().handleError(e);
                return null;
            }
        }

        @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
        public String print(UUID v) {
            return v.toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/RuntimeBuiltinLeafInfoImpl$StringImplImpl.class */
    private static class StringImplImpl extends StringImpl<String> {
        public StringImplImpl(Class type, QName[] typeNames) {
            super(type, typeNames);
        }

        @Override // com.sun.xml.bind.v2.runtime.Transducer
        public String parse(CharSequence text) {
            return text.toString();
        }

        @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
        public String print(String s) {
            return s;
        }

        @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
        public final void writeText(XMLSerializer w, String o, String fieldName) throws IOException, SAXException, XMLStreamException {
            w.text(o, fieldName);
        }

        @Override // com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.bind.v2.runtime.Transducer
        public final void writeLeafElement(XMLSerializer w, Name tagName, String o, String fieldName) throws IOException, SAXException, XMLStreamException {
            w.leafElement(tagName, o, fieldName);
        }
    }
}
