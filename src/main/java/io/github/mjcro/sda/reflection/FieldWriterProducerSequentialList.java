package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Optional;

public class FieldWriterProducerSequentialList implements FieldWriterProducer {
    private final ArrayList<FieldWriterProducer> producerList;

    public FieldWriterProducerSequentialList(@Nullable Iterable<? extends FieldWriterProducer> producerList) {
        this.producerList = new ArrayList<>();
        if (producerList != null) {
            for (FieldWriterProducer producer : producerList) {
                this.add(producer);
            }
        }
    }

    public void add(@Nullable FieldWriterProducer producer) {
        if (producer instanceof FieldWriterProducerSequentialList) {
            this.producerList.addAll(((FieldWriterProducerSequentialList) producer).producerList);
        } else if (producer != null) {
            this.producerList.add(producer);
        }
    }

    @Override
    public @NonNull Optional<FieldWriter<?>> apply(@NonNull Field field, @NonNull String columnName) {
        for (FieldWriterProducer producer : producerList) {
            Optional<FieldWriter<?>> fieldWriter = producer.apply(field, columnName);
            if (fieldWriter.isPresent()) {
                return fieldWriter;
            }
        }
        return Optional.empty();
    }
}
