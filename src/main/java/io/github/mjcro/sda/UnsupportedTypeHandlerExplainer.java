package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;

import java.lang.reflect.AnnotatedElement;
import java.util.List;

public interface UnsupportedTypeHandlerExplainer {
    @NonNull List<@NonNull Reason> getReasons(@NonNull AnnotatedElement element);

    class Reason {
        private final TypeHandler typeHandler;
        private final AnnotatedElement element;
        private final String message;

        public Reason(@NonNull TypeHandler typeHandler, @NonNull AnnotatedElement element, @NonNull String message) {
            this.typeHandler = typeHandler;
            this.element = element;
            this.message = message;
        }

        public @NonNull TypeHandler getTypeHandler() {
            return typeHandler;
        }

        public @NonNull AnnotatedElement getElement() {
            return element;
        }

        public @NonNull String getMessage() {
            return message;
        }
    }
}
