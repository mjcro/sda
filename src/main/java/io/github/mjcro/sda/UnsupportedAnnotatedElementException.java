package io.github.mjcro.sda;

import java.lang.reflect.AnnotatedElement;
import java.util.Locale;

public class UnsupportedAnnotatedElementException extends IllegalStateException {
    public UnsupportedAnnotatedElementException(
            Class<? extends TypeHandler> typeHandlerClass,
            AnnotatedElement element
    ) {
        super(String.format(
                Locale.ROOT,
                "Type handler %s does not support %s",
                typeHandlerClass == null ? null : typeHandlerClass.getSimpleName(),
                element
        ));
    }
}
