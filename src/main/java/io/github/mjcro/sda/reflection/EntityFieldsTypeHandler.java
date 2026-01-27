package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.TypeHandler;
import io.github.mjcro.sda.UnsupportedAnnotatedElementException;
import io.github.mjcro.sda.UnsupportedTypeHandlerExplainer;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Type handler building entity by setting fields values.
 * Requires empty constructor to work.
 */
public class EntityFieldsTypeHandler implements TypeHandler, UnsupportedTypeHandlerExplainer {
    private final TypeHandler fieldTypeHandler;

    /**
     * Constructor.
     *
     * @param fieldTypeHandler Type handler to resolve type handlers for fields.
     */
    public EntityFieldsTypeHandler(@NonNull TypeHandler fieldTypeHandler) {
        this.fieldTypeHandler = Objects.requireNonNull(fieldTypeHandler, "fieldTypeHandler");
    }

    @Override
    public boolean supports(@NonNull AnnotatedElement element) {
        if (!(element instanceof Class<?>)) {
            return false;
        }

        Class<?> clazz = (Class<?>) element;
        Constructor<?>[] ctors = clazz.getDeclaredConstructors();
        if (ctors.length != 1) {
            return false; // Many constructors
        }
        if (ctors[0].getParameterCount() != 0) {
            return false; // No no-arg constructor
        }

        List<Field> fields = ReflectionUtil.getAllFields(clazz);
        if (fields.isEmpty()) {
            return false;
        }
        for (Field field : fields) {
            if (!(fieldTypeHandler.supports(field))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NonNull List<@NonNull Reason> getReasons(@NonNull AnnotatedElement element) {
        ArrayList<Reason> reasons = new ArrayList<>();
        if (!(element instanceof Class<?>)) {
            reasons.add(new Reason(this, element, "Not a Class<?>"));
        } else {
            Class<?> clazz = (Class<?>) element;
            Constructor<?>[] ctors = clazz.getDeclaredConstructors();
            if (ctors.length != 1) {
                reasons.add(new Reason(this, element, "Too many constructors"));
            } else if (ctors[0].getParameterCount() != 0) {
                reasons.add(new Reason(this, element, "No no-arg constructor"));
            } else {
                List<Field> fields = ReflectionUtil.getAllFields(clazz);
                if (fields.isEmpty()) {
                    reasons.add(new Reason(this, element, "No fields"));
                } else {
                    for (Field field : fields) {
                        if (!(fieldTypeHandler.supports(field))) {
                            reasons.add(new Reason(this, field, "Is not supported"));
                            if (fieldTypeHandler instanceof UnsupportedTypeHandlerExplainer) {
                                reasons.addAll(((UnsupportedTypeHandlerExplainer) fieldTypeHandler).getReasons(field));
                            }
                        }
                    }
                }
            }
        }
        return reasons;
    }

    @Override
    public ValueReader getValueReader(@NonNull AnnotatedElement element) {
        if (!supports(element)) {
            throw new UnsupportedAnnotatedElementException(this.getClass(), element);
        }

        Class<?> clazz = (Class<?>) element;
        Constructor<?> ctor;
        try {
            ctor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new EnvelopedReflectiveOperationException(e);
        }
        ctor.setAccessible(true);
        List<Field> fields = ReflectionUtil.getAllFields(clazz);
        FieldSetterApplicator[] applicators = new FieldSetterApplicator[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setAccessible(true);
            applicators[i] = new FieldSetterApplicator(fields.get(i), fieldTypeHandler.getValueReader(fields.get(i)));
        }

        return rs -> {
            Object entity = ctor.newInstance();
            for (FieldSetterApplicator applicator : applicators) {
                applicator.apply(rs, entity);
            }
            return entity;
        };
    }

    private static class FieldSetterApplicator {
        private final Field field;
        private final ValueReader valueReader;

        private FieldSetterApplicator(@NonNull Field field, @NonNull ValueReader reader) {
            this.field = field;
            this.valueReader = reader;
        }

        void apply(ResultSet rs, Object target) throws Exception {
            field.set(target, valueReader.getValue(rs));
        }
    }
}
