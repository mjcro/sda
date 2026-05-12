package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;

import java.lang.reflect.AnnotatedElement;
import java.util.List;

/**
 * Optional mix-in for {@link TypeHandler}s that can explain why a given element is unsupported.
 * Produces human-readable diagnostics for {@link UnsupportedAnnotatedElementException}.
 */
public interface UnsupportedTypeHandlerExplainer {
    /**
     * Returns the list of reasons why the given element cannot be handled.
     *
     * @param element Inspected element, not null.
     * @return List of reasons, not null. Empty if no specific explanation is available.
     */
    @NonNull List<@NonNull Reason> getReasons(@NonNull AnnotatedElement element);

    /**
     * Single diagnostic entry describing why a handler rejected an element.
     */
    class Reason {
        private final TypeHandler typeHandler;
        private final AnnotatedElement element;
        private final String message;

        /**
         * @param typeHandler Handler that produced the reason. Not null.
         * @param element     Element being inspected. Not null.
         * @param message     Human-readable explanation. Not null.
         */
        public Reason(@NonNull TypeHandler typeHandler, @NonNull AnnotatedElement element, @NonNull String message) {
            this.typeHandler = typeHandler;
            this.element = element;
            this.message = message;
        }

        /**
         * @return Handler that produced this reason, not null.
         */
        public @NonNull TypeHandler getTypeHandler() {
            return typeHandler;
        }

        /**
         * @return Inspected element, not null.
         */
        public @NonNull AnnotatedElement getElement() {
            return element;
        }

        /**
         * @return Human-readable explanation, not null.
         */
        public @NonNull String getMessage() {
            return message;
        }
    }
}
