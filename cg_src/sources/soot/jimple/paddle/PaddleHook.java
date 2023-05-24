package soot.jimple.paddle;

import java.util.Map;
import soot.G;
import soot.SceneTransformer;
import soot.Singletons;
/* loaded from: gencallgraphv3.jar:soot/jimple/paddle/PaddleHook.class */
public class PaddleHook extends SceneTransformer {
    private IPaddleTransformer paddleTransformer;
    private Object paddleG;

    public PaddleHook(Singletons.Global g) {
    }

    public static PaddleHook v() {
        return G.v().soot_jimple_paddle_PaddleHook();
    }

    public IPaddleTransformer paddleTransformer() {
        if (this.paddleTransformer == null) {
            this.paddleTransformer = (IPaddleTransformer) instantiate("soot.jimple.paddle.PaddleTransformer");
        }
        return this.paddleTransformer;
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        paddleTransformer().transform(phaseName, options);
    }

    public Object instantiate(String className) {
        try {
            Object ret = Class.forName(className).newInstance();
            return ret;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find " + className + ". Did you include Paddle on your Java classpath?");
        } catch (IllegalAccessException e2) {
            throw new RuntimeException("Could not instantiate " + className + ": " + e2);
        } catch (InstantiationException e3) {
            throw new RuntimeException("Could not instantiate " + className + ": " + e3);
        }
    }

    public Object paddleG() {
        if (this.paddleG == null) {
            this.paddleG = instantiate("soot.PaddleG");
        }
        return this.paddleG;
    }

    public void finishPhases() {
        if (this.paddleTransformer != null) {
            paddleTransformer().finishPhases();
        }
    }
}
