package org.mockito.internal.stubbing.answers;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Answer1;
import org.mockito.stubbing.Answer2;
import org.mockito.stubbing.Answer3;
import org.mockito.stubbing.Answer4;
import org.mockito.stubbing.Answer5;
import org.mockito.stubbing.Answer6;
import org.mockito.stubbing.VoidAnswer1;
import org.mockito.stubbing.VoidAnswer2;
import org.mockito.stubbing.VoidAnswer3;
import org.mockito.stubbing.VoidAnswer4;
import org.mockito.stubbing.VoidAnswer5;
import org.mockito.stubbing.VoidAnswer6;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/answers/AnswerFunctionalInterfaces.class */
public class AnswerFunctionalInterfaces {
    private AnswerFunctionalInterfaces() {
    }

    public static <T, A> Answer<T> toAnswer(final Answer1<T, A> answer) {
        return new Answer<T>() { // from class: org.mockito.internal.stubbing.answers.AnswerFunctionalInterfaces.1
            /* JADX WARN: Type inference failed for: r0v2, types: [T, java.lang.Object] */
            @Override // org.mockito.stubbing.Answer
            public T answer(InvocationOnMock invocation) throws Throwable {
                return Answer1.this.answer(invocation.getArgument(0));
            }
        };
    }

    public static <A> Answer<Void> toAnswer(final VoidAnswer1<A> answer) {
        return new Answer<Void>() { // from class: org.mockito.internal.stubbing.answers.AnswerFunctionalInterfaces.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.mockito.stubbing.Answer
            public Void answer(InvocationOnMock invocation) throws Throwable {
                VoidAnswer1.this.answer(invocation.getArgument(0));
                return null;
            }
        };
    }

    public static <T, A, B> Answer<T> toAnswer(final Answer2<T, A, B> answer) {
        return new Answer<T>() { // from class: org.mockito.internal.stubbing.answers.AnswerFunctionalInterfaces.3
            /* JADX WARN: Type inference failed for: r0v2, types: [T, java.lang.Object] */
            @Override // org.mockito.stubbing.Answer
            public T answer(InvocationOnMock invocation) throws Throwable {
                return Answer2.this.answer(invocation.getArgument(0), invocation.getArgument(1));
            }
        };
    }

    public static <A, B> Answer<Void> toAnswer(final VoidAnswer2<A, B> answer) {
        return new Answer<Void>() { // from class: org.mockito.internal.stubbing.answers.AnswerFunctionalInterfaces.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.mockito.stubbing.Answer
            public Void answer(InvocationOnMock invocation) throws Throwable {
                VoidAnswer2.this.answer(invocation.getArgument(0), invocation.getArgument(1));
                return null;
            }
        };
    }

    public static <T, A, B, C> Answer<T> toAnswer(final Answer3<T, A, B, C> answer) {
        return new Answer<T>() { // from class: org.mockito.internal.stubbing.answers.AnswerFunctionalInterfaces.5
            /* JADX WARN: Type inference failed for: r0v2, types: [T, java.lang.Object] */
            @Override // org.mockito.stubbing.Answer
            public T answer(InvocationOnMock invocation) throws Throwable {
                return Answer3.this.answer(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2));
            }
        };
    }

    public static <A, B, C> Answer<Void> toAnswer(final VoidAnswer3<A, B, C> answer) {
        return new Answer<Void>() { // from class: org.mockito.internal.stubbing.answers.AnswerFunctionalInterfaces.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.mockito.stubbing.Answer
            public Void answer(InvocationOnMock invocation) throws Throwable {
                VoidAnswer3.this.answer(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2));
                return null;
            }
        };
    }

    public static <T, A, B, C, D> Answer<T> toAnswer(final Answer4<T, A, B, C, D> answer) {
        return new Answer<T>() { // from class: org.mockito.internal.stubbing.answers.AnswerFunctionalInterfaces.7
            /* JADX WARN: Type inference failed for: r0v2, types: [T, java.lang.Object] */
            @Override // org.mockito.stubbing.Answer
            public T answer(InvocationOnMock invocation) throws Throwable {
                return Answer4.this.answer(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2), invocation.getArgument(3));
            }
        };
    }

    public static <A, B, C, D> Answer<Void> toAnswer(final VoidAnswer4<A, B, C, D> answer) {
        return new Answer<Void>() { // from class: org.mockito.internal.stubbing.answers.AnswerFunctionalInterfaces.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.mockito.stubbing.Answer
            public Void answer(InvocationOnMock invocation) throws Throwable {
                VoidAnswer4.this.answer(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2), invocation.getArgument(3));
                return null;
            }
        };
    }

    public static <T, A, B, C, D, E> Answer<T> toAnswer(final Answer5<T, A, B, C, D, E> answer) {
        return new Answer<T>() { // from class: org.mockito.internal.stubbing.answers.AnswerFunctionalInterfaces.9
            /* JADX WARN: Type inference failed for: r0v2, types: [T, java.lang.Object] */
            @Override // org.mockito.stubbing.Answer
            public T answer(InvocationOnMock invocation) throws Throwable {
                return Answer5.this.answer(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2), invocation.getArgument(3), invocation.getArgument(4));
            }
        };
    }

    public static <A, B, C, D, E> Answer<Void> toAnswer(final VoidAnswer5<A, B, C, D, E> answer) {
        return new Answer<Void>() { // from class: org.mockito.internal.stubbing.answers.AnswerFunctionalInterfaces.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.mockito.stubbing.Answer
            public Void answer(InvocationOnMock invocation) throws Throwable {
                VoidAnswer5.this.answer(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2), invocation.getArgument(3), invocation.getArgument(4));
                return null;
            }
        };
    }

    public static <T, A, B, C, D, E, F> Answer<T> toAnswer(final Answer6<T, A, B, C, D, E, F> answer) {
        return new Answer<T>() { // from class: org.mockito.internal.stubbing.answers.AnswerFunctionalInterfaces.11
            /* JADX WARN: Type inference failed for: r0v2, types: [T, java.lang.Object] */
            @Override // org.mockito.stubbing.Answer
            public T answer(InvocationOnMock invocation) throws Throwable {
                return Answer6.this.answer(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2), invocation.getArgument(3), invocation.getArgument(4), invocation.getArgument(5));
            }
        };
    }

    public static <A, B, C, D, E, F> Answer<Void> toAnswer(final VoidAnswer6<A, B, C, D, E, F> answer) {
        return new Answer<Void>() { // from class: org.mockito.internal.stubbing.answers.AnswerFunctionalInterfaces.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.mockito.stubbing.Answer
            public Void answer(InvocationOnMock invocation) throws Throwable {
                VoidAnswer6.this.answer(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2), invocation.getArgument(3), invocation.getArgument(4), invocation.getArgument(5));
                return null;
            }
        };
    }
}
