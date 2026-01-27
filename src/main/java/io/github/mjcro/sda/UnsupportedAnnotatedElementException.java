package io.github.mjcro.sda;

import java.lang.reflect.AnnotatedElement;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

public class UnsupportedAnnotatedElementException extends IllegalStateException {
    public UnsupportedAnnotatedElementException(
            Class<? extends TypeHandler> typeHandlerClass,
            AnnotatedElement element
    ) {
        this(String.format(
                Locale.ROOT,
                "Type handler %s does not support %s",
                typeHandlerClass == null ? null : typeHandlerClass.getSimpleName(),
                element
        ));
    }

    private UnsupportedAnnotatedElementException(String message) {
        super(message);
    }

    public static <T extends TypeHandler & UnsupportedTypeHandlerExplainer> UnsupportedAnnotatedElementException forExplainer(
            T explainer,
            AnnotatedElement element
    ) {
        List<UnsupportedTypeHandlerExplainer.Reason> reasons = explainer.getReasons(element);
        if (reasons.isEmpty()) {
            return new UnsupportedAnnotatedElementException(explainer.getClass(), element);
        }

        StringBuilder sb = new StringBuilder();
        new Formatter(sb, Locale.ROOT)
                .format("Type handler %s does not support %s:\n", explainer.getClass().getSimpleName(), element);

        for (UnsupportedTypeHandlerExplainer.Reason reason : reasons) {
            new Formatter(sb, Locale.ROOT)
                    .format(
                            "- %s %s @%s\n",
                            reason.getElement(),
                            reason.getMessage(),
                            reason.getTypeHandler().getClass().getSimpleName()
                    );
        }

        return new UnsupportedAnnotatedElementException(sb.toString());
    }
}
