package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Optional;

public class FieldWriterProducerSequentialList implements FieldWriterProducer {
    private final ArrayList<FieldWriterProducer> producerList;

    public FieldWriterProducerSequentialList(Iterable<? extends FieldWriterProducer> producerList) {
        this.producerList = new ArrayList<>();
        if (producerList != null) {
            for (final FieldWriterProducer producer : producerList) {
                this.add(producer);
            }
        }
    }

    public void add(final FieldWriterProducer producer) {
        if (producer instanceof FieldWriterProducerSequentialList) {
            this.producerList.addAll(((FieldWriterProducerSequentialList) producer).producerList);
        } else if (producer != null) {
            this.producerList.add(producer);
        }
    }

    @Override
    public Optional<FieldWriter<?>> apply(final Field field, final String columnName) {
        for (final FieldWriterProducer producer : producerList) {
            Optional<FieldWriter<?>> fieldWriter = producer.apply(field, columnName);
            if (fieldWriter.isPresent()) {
                return fieldWriter;
            }
        }
        return Optional.empty();
    }
}
