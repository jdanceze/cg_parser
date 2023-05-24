package org.mockito.internal.stubbing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/DoAnswerStyleStubbing.class */
class DoAnswerStyleStubbing implements Serializable {
    private final List<Answer<?>> answers = new ArrayList();
    private Strictness stubbingStrictness;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAnswers(List<Answer<?>> answers, Strictness stubbingStrictness) {
        this.stubbingStrictness = stubbingStrictness;
        this.answers.addAll(answers);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isSet() {
        return this.answers.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clear() {
        this.answers.clear();
        this.stubbingStrictness = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Answer<?>> getAnswers() {
        return this.answers;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Strictness getStubbingStrictness() {
        return this.stubbingStrictness;
    }
}
