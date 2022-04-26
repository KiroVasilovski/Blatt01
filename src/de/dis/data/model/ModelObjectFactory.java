package de.dis.data.model;

import de.dis.data.DbColumn;
import de.dis.data.store.DbRow;
import de.dis.data.store.DbRowFactory;

interface ModelObjectFactoryFunction<ColumnType extends Enum<ColumnType> & DbColumn, InstanceType> {
    InstanceType create(DbRow<ColumnType> store);
}

public class ModelObjectFactory<ColumnType extends Enum<ColumnType> & DbColumn, InstanceType> {
    private final ModelObjectFactoryFunction<ColumnType, InstanceType> factoryFunction;
    private final DbRowFactory<ColumnType> store;

    public ModelObjectFactory(DbRowFactory<ColumnType> store, ModelObjectFactoryFunction<ColumnType, InstanceType> factoryFunction) {
        this.store = store;
        this.factoryFunction = factoryFunction;
    }
}
