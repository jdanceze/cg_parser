package org.mockito;

import java.util.Collection;
import org.mockito.internal.stubbing.answers.AnswerFunctionalInterfaces;
import org.mockito.internal.stubbing.answers.AnswersWithDelay;
import org.mockito.internal.stubbing.answers.ReturnsArgumentAt;
import org.mockito.internal.stubbing.answers.ReturnsElementsOf;
import org.mockito.internal.stubbing.defaultanswers.ForwardsInvocations;
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
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/AdditionalAnswers.class */
public class AdditionalAnswers {
    public static <T> Answer<T> returnsFirstArg() {
        return new ReturnsArgumentAt(0);
    }

    public static <T> Answer<T> returnsSecondArg() {
        return new ReturnsArgumentAt(1);
    }

    public static <T> Answer<T> returnsLastArg() {
        return new ReturnsArgumentAt(-1);
    }

    public static <T> Answer<T> returnsArgAt(int position) {
        return new ReturnsArgumentAt(position);
    }

    public static <T> Answer<T> delegatesTo(Object delegate) {
        return new ForwardsInvocations(delegate);
    }

    public static <T> Answer<T> returnsElementsOf(Collection<?> elements) {
        return new ReturnsElementsOf(elements);
    }

    @Incubating
    public static <T> Answer<T> answersWithDelay(long sleepyTime, Answer<T> answer) {
        return new AnswersWithDelay(sleepyTime, answer);
    }

    @Incubating
    public static <T, A> Answer<T> answer(Answer1<T, A> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    @Incubating
    public static <A> Answer<Void> answerVoid(VoidAnswer1<A> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    @Incubating
    public static <T, A, B> Answer<T> answer(Answer2<T, A, B> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    @Incubating
    public static <A, B> Answer<Void> answerVoid(VoidAnswer2<A, B> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    @Incubating
    public static <T, A, B, C> Answer<T> answer(Answer3<T, A, B, C> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    @Incubating
    public static <A, B, C> Answer<Void> answerVoid(VoidAnswer3<A, B, C> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    @Incubating
    public static <T, A, B, C, D> Answer<T> answer(Answer4<T, A, B, C, D> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    @Incubating
    public static <A, B, C, D> Answer<Void> answerVoid(VoidAnswer4<A, B, C, D> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    @Incubating
    public static <T, A, B, C, D, E> Answer<T> answer(Answer5<T, A, B, C, D, E> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    @Incubating
    public static <A, B, C, D, E> Answer<Void> answerVoid(VoidAnswer5<A, B, C, D, E> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    @Incubating
    public static <T, A, B, C, D, E, F> Answer<T> answer(Answer6<T, A, B, C, D, E, F> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    @Incubating
    public static <A, B, C, D, E, F> Answer<Void> answerVoid(VoidAnswer6<A, B, C, D, E, F> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }
}
