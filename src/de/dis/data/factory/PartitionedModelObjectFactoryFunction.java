package de.dis.data.factory;

import de.dis.data.DbColumn;
import de.dis.data.store.DbRow;

public interface PartitionedModelObjectFactoryFunction<ChildColumnType extends Enum<ChildColumnType> & DbColumn, ParentColumnType extends Enum<ParentColumnType> & DbColumn, ChildInstanceType extends ModelObject> {
    ChildInstanceType instantiate(DbRow<ParentColumnType> parentStore, DbRow<ChildColumnType> store);
}
