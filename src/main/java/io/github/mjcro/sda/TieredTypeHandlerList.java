package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TieredTypeHandlerList implements TypeHandler, UnsupportedTypeHandlerExplainer {
    private final LinkedList<TypeHandler>
            ecHandlers = new LinkedList<>(),
            apHandlers = new LinkedList<>(),
            ttHandlers = new LinkedList<>();

    @Override
    public boolean supports(@NonNull AnnotatedElement element) {
        return getTypeHandler(element) != null;
    }

    @Override
    public @NonNull List<@NonNull Reason> getReasons(@NonNull AnnotatedElement element) {
        ArrayList<Reason> reasons = new ArrayList<>();
        for (TypeHandler h : ecHandlers) {
            if (h instanceof UnsupportedTypeHandlerExplainer) {
                reasons.addAll(((UnsupportedTypeHandlerExplainer) h).getReasons(element));
            }
        }
        return reasons;
    }

    @Override
    public ValueReader getValueReader(@NonNull AnnotatedElement element) {
        TypeHandler handler = getTypeHandler(element);
        if (handler == null) {
            throw UnsupportedAnnotatedElementException.forExplainer(this, element);
        }
        return handler.getValueReader(element);
    }

    private @Nullable TypeHandler getTypeHandler(@NonNull AnnotatedElement element) {
        for (TypeHandler handler : ecHandlers) {
            if (handler.supports(element)) {
                return handler;
            }
        }
        for (TypeHandler handler : apHandlers) {
            if (handler.supports(element)) {
                return handler;
            }
        }
        for (TypeHandler handler : ttHandlers) {
            if (handler.supports(element)) {
                return handler;
            }
        }
        return null;
    }

    public TieredTypeHandlerList addFirst(@NonNull Tier tier, @NonNull TypeHandler... handlers) {
        LinkedList<TypeHandler> target = ttHandlers;
        switch (tier) {
            case ENTITY_CONSTRUCTORS:
                target = ecHandlers;
                break;
            case ANNOTATION_PROCESSORS:
                target = apHandlers;
                break;
        }

        if (handlers != null) {
            for (TypeHandler handler : handlers) {
                target.addFirst(handler);
            }
        }
        return this;
    }

    public TieredTypeHandlerList addLast(@NonNull Tier tier, @NonNull TypeHandler... handlers) {
        LinkedList<TypeHandler> target = ttHandlers;
        switch (tier) {
            case ENTITY_CONSTRUCTORS:
                target = ecHandlers;
                break;
            case ANNOTATION_PROCESSORS:
                target = apHandlers;
                break;
        }

        if (handlers != null) {
            for (TypeHandler handler : handlers) {
                target.addLast(handler);
            }
        }
        return this;
    }

    public enum Tier {
        ENTITY_CONSTRUCTORS,
        ANNOTATION_PROCESSORS,
        TYPES
    }
}
