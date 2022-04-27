package de.dis.data.factory;

import de.dis.data.DbColumn;
import de.dis.data.store.DbRow;

public interface ModelObjectFactoryFunction<ColumnType extends Enum<ColumnType> & DbColumn, InstanceType> {
    InstanceType instantiate(DbRow<ColumnType> store);
}
