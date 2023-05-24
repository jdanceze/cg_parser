package org.apache.tools.ant.input;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Vector;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/input/MultipleChoiceInputRequest.class */
public class MultipleChoiceInputRequest extends InputRequest {
    private final LinkedHashSet<String> choices;

    @Deprecated
    public MultipleChoiceInputRequest(String prompt, Vector<String> choices) {
        this(prompt, (Collection<String>) choices);
    }

    public MultipleChoiceInputRequest(String prompt, Collection<String> choices) {
        super(prompt);
        if (choices == null) {
            throw new IllegalArgumentException("choices must not be null");
        }
        this.choices = new LinkedHashSet<>(choices);
    }

    public Vector<String> getChoices() {
        return new Vector<>(this.choices);
    }

    @Override // org.apache.tools.ant.input.InputRequest
    public boolean isInputValid() {
        return this.choices.contains(getInput()) || (getInput().isEmpty() && getDefaultValue() != null);
    }
}
