package soot.jimple.infoflow.android.callbacks.xml;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.callbacks.AndroidCallbackDefinition;
import soot.util.HashMultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/callbacks/xml/CollectedCallbacksSerializer.class */
public class CollectedCallbacksSerializer {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/callbacks/xml/CollectedCallbacksSerializer$SootMethodSerializer.class */
    public static class SootMethodSerializer extends Serializer<SootMethod> {
        /* renamed from: read  reason: collision with other method in class */
        public /* bridge */ /* synthetic */ Object m2690read(Kryo kryo, Input input, Class cls) {
            return read(kryo, input, (Class<? extends SootMethod>) cls);
        }

        private SootMethodSerializer() {
        }

        /* synthetic */ SootMethodSerializer(SootMethodSerializer sootMethodSerializer) {
            this();
        }

        public void write(Kryo kryo, Output output, SootMethod object) {
            output.writeString(object.getSignature());
        }

        public SootMethod read(Kryo kryo, Input input, Class<? extends SootMethod> type) {
            String sig = input.readString();
            return Scene.v().grabMethod(sig);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/callbacks/xml/CollectedCallbacksSerializer$SootClassSerializer.class */
    public static class SootClassSerializer extends Serializer<SootClass> {
        /* renamed from: read  reason: collision with other method in class */
        public /* bridge */ /* synthetic */ Object m2689read(Kryo kryo, Input input, Class cls) {
            return read(kryo, input, (Class<? extends SootClass>) cls);
        }

        private SootClassSerializer() {
        }

        /* synthetic */ SootClassSerializer(SootClassSerializer sootClassSerializer) {
            this();
        }

        public void write(Kryo kryo, Output output, SootClass object) {
            output.writeString(object.getName());
        }

        public SootClass read(Kryo kryo, Input input, Class<? extends SootClass> type) {
            String className = input.readString();
            return Scene.v().getSootClassUnsafe(className);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/callbacks/xml/CollectedCallbacksSerializer$AndroidCallbackDefinitionSerializer.class */
    public static class AndroidCallbackDefinitionSerializer extends Serializer<AndroidCallbackDefinition> {
        /* renamed from: read  reason: collision with other method in class */
        public /* bridge */ /* synthetic */ Object m2688read(Kryo kryo, Input input, Class cls) {
            return read(kryo, input, (Class<? extends AndroidCallbackDefinition>) cls);
        }

        private AndroidCallbackDefinitionSerializer() {
        }

        /* synthetic */ AndroidCallbackDefinitionSerializer(AndroidCallbackDefinitionSerializer androidCallbackDefinitionSerializer) {
            this();
        }

        public void write(Kryo kryo, Output output, AndroidCallbackDefinition object) {
            kryo.writeClassAndObject(output, object.getTargetMethod());
            kryo.writeClassAndObject(output, object.getParentMethod());
            kryo.writeClassAndObject(output, object.getCallbackType());
        }

        public AndroidCallbackDefinition read(Kryo kryo, Input input, Class<? extends AndroidCallbackDefinition> type) {
            SootMethod targetMethod = (SootMethod) kryo.readClassAndObject(input);
            SootMethod parentMethod = (SootMethod) kryo.readClassAndObject(input);
            AndroidCallbackDefinition.CallbackType callbackType = (AndroidCallbackDefinition.CallbackType) kryo.readClassAndObject(input);
            return new AndroidCallbackDefinition(targetMethod, parentMethod, callbackType);
        }
    }

    public static void serialize(CollectedCallbacks callbacks, InfoflowAndroidConfiguration.CallbackConfiguration config) throws IOException {
        Kryo kryo = initializeKryo();
        Throwable th = null;
        try {
            Output output = new Output(new FileOutputStream(config.getCallbacksFile()));
            kryo.writeClassAndObject(output, callbacks);
            if (output != null) {
                output.close();
            }
        } catch (Throwable th2) {
            if (0 == 0) {
                th = th2;
            } else if (null != th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static CollectedCallbacks deserialize(File callbackFile) throws KryoException, FileNotFoundException {
        Kryo kryo = initializeKryo();
        Throwable th = null;
        try {
            Input input = new Input(new FileInputStream(callbackFile));
            CollectedCallbacks collectedCallbacks = (CollectedCallbacks) kryo.readClassAndObject(input);
            if (input != null) {
                input.close();
            }
            return collectedCallbacks;
        } catch (Throwable th2) {
            if (0 == 0) {
                th = th2;
            } else if (null != th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static CollectedCallbacks deserialize(InfoflowAndroidConfiguration.CallbackConfiguration config) throws KryoException, FileNotFoundException {
        return deserialize(new File(config.getCallbacksFile()));
    }

    protected static Kryo initializeKryo() {
        Kryo kryo = new Kryo();
        kryo.register(CollectedCallbacks.class);
        kryo.register(AndroidCallbackDefinition.class, new AndroidCallbackDefinitionSerializer(null));
        kryo.register(AndroidCallbackDefinition.CallbackType.class);
        kryo.register(HashMultiMap.class);
        kryo.register(HashMap.class);
        kryo.register(HashSet.class);
        kryo.register(SootMethod.class, new SootMethodSerializer(null));
        kryo.register(SootClass.class, new SootClassSerializer(null));
        return kryo;
    }
}
