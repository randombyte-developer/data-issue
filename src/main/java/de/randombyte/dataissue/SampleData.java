package de.randombyte.dataissue;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

import static de.randombyte.dataissue.DataIssuePlugin.*;

public class SampleData extends AbstractData<SampleData, SampleData.Immutable> {

    private static final int DATA_VERSION = 1;

    private boolean bool = false;

    SampleData() {
        registerGettersAndSetters();
    }

    SampleData(boolean bool) {
        this.bool = bool;
        registerGettersAndSetters();
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(BOOL, this::isBool);
        registerFieldSetter(BOOL, this::setBool);
        registerKeyValue(BOOL, this::bool);
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public Value<Boolean> bool() {
        return Sponge.getRegistry().getValueFactory().createValue(BOOL, bool);
    }

    @Override
    public Optional<SampleData> fill(DataHolder dataHolder, MergeFunction overlap) {
        dataHolder.get(SampleData.class).ifPresent(that -> {
            SampleData data = overlap.merge(this, that);
            this.bool = data.bool;
        });
        return Optional.of(this);
    }

    @Override
    public Optional<SampleData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<SampleData> from(DataView container) {
        container.getBoolean(BOOL.getQuery()).ifPresent(v -> bool = v);
        return Optional.of(this);
    }

    @Override
    public SampleData copy() {
        return new SampleData(bool);
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(bool);
    }

    @Override
    public int getContentVersion() {
        return DATA_VERSION;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer()
                .set(BOOL.getQuery(), bool);
    }

    public static class Immutable extends AbstractImmutableData<Immutable, SampleData> {

        private boolean bool = false;

        Immutable() {
            registerGetters();
        }

        Immutable(boolean bool) {
            this.bool = bool;
            registerGetters();
        }

        @Override
        protected void registerGetters() {
            registerFieldGetter(BOOL, this::isBool);
            registerKeyValue(BOOL, this::bool);
        }

        public boolean isBool() {
            return bool;
        }

        public ImmutableValue<Boolean> bool() {
            return Sponge.getRegistry().getValueFactory().createValue(BOOL, bool).asImmutable();
        }

        @Override
        public SampleData asMutable() {
            return new SampleData(bool);
        }

        @Override
        public int getContentVersion() {
            return DATA_VERSION;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer()
                    .set(BOOL.getQuery(), bool);
        }

    }

    public static class Builder extends AbstractDataBuilder<SampleData> implements DataManipulatorBuilder<SampleData, Immutable> {

        protected Builder() {
            super(SampleData.class, 1);
        }

        @Override
        public SampleData create() {
            return new SampleData();
        }

        @Override
        public Optional<SampleData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<SampleData> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
        }

    }
}