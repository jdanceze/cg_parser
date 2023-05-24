package org.xmlpull.v1.parser_pool;

import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/parser_pool/XmlPullParserPool.class */
public class XmlPullParserPool {
    protected List pool;
    protected XmlPullParserFactory factory;

    public XmlPullParserPool() throws XmlPullParserException {
        this(XmlPullParserFactory.newInstance());
    }

    public XmlPullParserPool(XmlPullParserFactory factory) {
        this.pool = new ArrayList();
        if (factory == null) {
            throw new IllegalArgumentException();
        }
        this.factory = factory;
    }

    protected XmlPullParser newParser() throws XmlPullParserException {
        return this.factory.newPullParser();
    }

    public XmlPullParser getPullParserFromPool() throws XmlPullParserException {
        XmlPullParser pp = null;
        if (this.pool.size() > 0) {
            synchronized (this.pool) {
                if (this.pool.size() > 0) {
                    pp = (XmlPullParser) this.pool.remove(this.pool.size() - 1);
                }
            }
        }
        if (pp == null) {
            pp = newParser();
        }
        return pp;
    }

    public void returnPullParserToPool(XmlPullParser pp) {
        if (pp == null) {
            throw new IllegalArgumentException();
        }
        synchronized (this.pool) {
            this.pool.add(pp);
        }
    }

    public static void main(String[] args) throws Exception {
        XmlPullParserPool pool = new XmlPullParserPool();
        XmlPullParser p1 = pool.getPullParserFromPool();
        pool.returnPullParserToPool(p1);
        XmlPullParser p2 = pool.getPullParserFromPool();
        if (p1 != p2) {
            throw new RuntimeException();
        }
        pool.returnPullParserToPool(p2);
        System.out.println(new StringBuffer().append(pool.getClass()).append(" OK").toString());
    }
}
