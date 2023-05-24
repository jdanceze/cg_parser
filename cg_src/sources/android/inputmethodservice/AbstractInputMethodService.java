package android.inputmethodservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodSession;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/inputmethodservice/AbstractInputMethodService.class */
public abstract class AbstractInputMethodService extends Service implements KeyEvent.Callback {
    public abstract AbstractInputMethodImpl onCreateInputMethodInterface();

    public abstract AbstractInputMethodSessionImpl onCreateInputMethodSessionInterface();

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/inputmethodservice/AbstractInputMethodService$AbstractInputMethodImpl.class */
    public abstract class AbstractInputMethodImpl implements InputMethod {
        public AbstractInputMethodImpl() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethod
        public void createSession(InputMethod.SessionCallback callback) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethod
        public void setSessionEnabled(InputMethodSession session, boolean enabled) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethod
        public void revokeSession(InputMethodSession session) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/inputmethodservice/AbstractInputMethodService$AbstractInputMethodSessionImpl.class */
    public abstract class AbstractInputMethodSessionImpl implements InputMethodSession {
        public AbstractInputMethodSessionImpl() {
            throw new RuntimeException("Stub!");
        }

        public boolean isEnabled() {
            throw new RuntimeException("Stub!");
        }

        public boolean isRevoked() {
            throw new RuntimeException("Stub!");
        }

        public void setEnabled(boolean enabled) {
            throw new RuntimeException("Stub!");
        }

        public void revokeSelf() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void dispatchKeyEvent(int seq, KeyEvent event, InputMethodSession.EventCallback callback) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void dispatchTrackballEvent(int seq, MotionEvent event, InputMethodSession.EventCallback callback) {
            throw new RuntimeException("Stub!");
        }
    }

    public AbstractInputMethodService() {
        throw new RuntimeException("Stub!");
    }

    public KeyEvent.DispatcherState getKeyDispatcherState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    protected void dump(FileDescriptor fd, PrintWriter fout, String[] args) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    public boolean onTrackballEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }
}
