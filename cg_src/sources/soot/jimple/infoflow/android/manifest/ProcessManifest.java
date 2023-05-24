package soot.jimple.infoflow.android.manifest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParserException;
import soot.jimple.infoflow.android.axml.AXmlNode;
import soot.jimple.infoflow.android.manifest.BaseProcessManifest;
import soot.jimple.infoflow.android.manifest.binary.BinaryManifestActivity;
import soot.jimple.infoflow.android.manifest.binary.BinaryManifestBroadcastReceiver;
import soot.jimple.infoflow.android.manifest.binary.BinaryManifestContentProvider;
import soot.jimple.infoflow.android.manifest.binary.BinaryManifestService;
import soot.jimple.infoflow.android.resources.ARSCFileParser;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/manifest/ProcessManifest.class */
public class ProcessManifest extends BaseProcessManifest<BinaryManifestActivity, BinaryManifestService, BinaryManifestContentProvider, BinaryManifestBroadcastReceiver> {
    public ProcessManifest(File apkFile, ARSCFileParser arscParser) throws IOException, XmlPullParserException {
        super(apkFile, arscParser);
    }

    public ProcessManifest(File apkFile) throws IOException, XmlPullParserException {
        super(apkFile);
    }

    public ProcessManifest(InputStream manifestIS, ARSCFileParser arscParser) throws IOException, XmlPullParserException {
        super(manifestIS, arscParser);
    }

    public ProcessManifest(String apkPath) throws IOException, XmlPullParserException {
        super(apkPath);
    }

    @Override // soot.jimple.infoflow.android.manifest.BaseProcessManifest
    protected BaseProcessManifest.IComponentFactory<BinaryManifestActivity, BinaryManifestService, BinaryManifestContentProvider, BinaryManifestBroadcastReceiver> createComponentFactory() {
        return new BaseProcessManifest.IComponentFactory<BinaryManifestActivity, BinaryManifestService, BinaryManifestContentProvider, BinaryManifestBroadcastReceiver>() { // from class: soot.jimple.infoflow.android.manifest.ProcessManifest.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // soot.jimple.infoflow.android.manifest.BaseProcessManifest.IComponentFactory
            public BinaryManifestActivity createActivity(AXmlNode node) {
                return new BinaryManifestActivity(node, ProcessManifest.this);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // soot.jimple.infoflow.android.manifest.BaseProcessManifest.IComponentFactory
            public BinaryManifestBroadcastReceiver createBroadcastReceiver(AXmlNode node) {
                return new BinaryManifestBroadcastReceiver(node, ProcessManifest.this);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // soot.jimple.infoflow.android.manifest.BaseProcessManifest.IComponentFactory
            public BinaryManifestContentProvider createContentProvider(AXmlNode node) {
                return new BinaryManifestContentProvider(node, ProcessManifest.this);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // soot.jimple.infoflow.android.manifest.BaseProcessManifest.IComponentFactory
            public BinaryManifestService createService(AXmlNode node) {
                return new BinaryManifestService(node, ProcessManifest.this);
            }
        };
    }
}
