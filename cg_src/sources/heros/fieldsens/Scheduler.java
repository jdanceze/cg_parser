package heros.fieldsens;

import com.google.common.collect.Lists;
import java.util.LinkedList;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/Scheduler.class */
public class Scheduler {
    private LinkedList<Runnable> worklist = Lists.newLinkedList();

    public void schedule(Runnable job) {
        this.worklist.add(job);
    }

    public void runAndAwaitCompletion() {
        while (!this.worklist.isEmpty()) {
            this.worklist.removeLast().run();
        }
    }
}
