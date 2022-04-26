package de.dis.data.model;

import de.dis.data.DbColumn;
import de.dis.data.store.DbRow;
import de.dis.data.store.DbRowFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

interface PartitionedModelObjectFactoryFunction<ChildColumnType extends Enum<ChildColumnType> & DbColumn, ParentColumnType extends Enum<ParentColumnType> & DbColumn, ChildInstanceType extends ModelObject> {
    ChildInstanceType instantiate(DbRow<ParentColumnType> parentStore, DbRow<ChildColumnType> store);
}

public class PartitionedModelObjectFactory<ChildColumnType extends Enum<ChildColumnType> & DbColumn, ParentColumnType extends Enum<ParentColumnType> & DbColumn, ChildInstanceType extends ModelObject> {
    private final DbRowFactory<ChildColumnType> dbRowFactory;
    private final DbRowFactory<ParentColumnType> parentDbRowFactory;
    private final Map<Object, ChildInstanceType> cache = new HashMap<>();
    private final PartitionedModelObjectFactoryFunction<ChildColumnType, ParentColumnType, ChildInstanceType> factoryFunction;

    public PartitionedModelObjectFactory(DbRowFactory<ChildColumnType> dbRowFactory, DbRowFactory<ParentColumnType> parentDbRowFactory, PartitionedModelObjectFactoryFunction<ChildColumnType, ParentColumnType, ChildInstanceType> factoryFunction) {
        this.dbRowFactory = dbRowFactory;
        this.parentDbRowFactory = parentDbRowFactory;
        this.factoryFunction = factoryFunction;
    }

    public ChildInstanceType get(Object id) {
        if (cache.containsKey(id)) return cache.get(id);

        DbRow<ParentColumnType> parentStore = parentDbRowFactory.load(id);
        if (parentStore == null) return null;

        DbRow<ChildColumnType> store = dbRowFactory.load(id);
        if (store == null) return null;

        return factoryFunction.instantiate(parentStore, store);
    }

    public ChildInstanceType create(Object[] parentValues, Object[] values) {
        DbRow<ParentColumnType> parentStore =
                parentDbRowFactory.create(parentValues);
        if (parentStore == null) return null;
        Object id = parentStore.getId();
        DbRow<ChildColumnType> store = dbRowFactory.createWithId(id, values);
        if (store == null) return null;

        return factoryFunction.instantiate(parentStore, store);
    }

    public void delete(ChildInstanceType instance) {
        int id = instance.getId();
        parentDbRowFactory.delete(id);
        cache.remove(id);
    }

    public Set<ChildInstanceType> getAll() {
        Set<DbRow<ChildColumnType>> rows = dbRowFactory.loadAll();
        return bulkInstantiate(rows.stream().map(row -> row.getId()).collect(Collectors.toUnmodifiableSet()));
    }

    public Set<ChildInstanceType> getAllWhereParent(ParentColumnType column, Object value) {
        Set<DbRow<ParentColumnType>> rows = parentDbRowFactory.loadAllWhere(column, value);
        return bulkInstantiate(rows.stream().map(row -> row.getId()).collect(Collectors.toUnmodifiableSet()));
    }

    public Set<ChildInstanceType> getAllWhereChild(ChildColumnType column, Object value) {
        Set<DbRow<ChildColumnType>> rows = dbRowFactory.loadAllWhere(column, value);
        return bulkInstantiate(rows.stream().map(row -> row.getId()).collect(Collectors.toUnmodifiableSet()));
    }

    public ChildInstanceType getWhereParent(ParentColumnType column, Object value) {
        DbRow<ParentColumnType> row = parentDbRowFactory.loadWhere(column, value);
        if (row == null) return null;
        return get(row.getId());
    }

    public ChildInstanceType getWhereChild(ChildColumnType column, Object value) {
        DbRow<ChildColumnType> row = dbRowFactory.loadWhere(column, value);
        if (row == null) return null;
        return get(row.getId());
    }

    private Set<ChildInstanceType> bulkInstantiate(Set<Object> ids) {
        Set<ChildInstanceType> result = new HashSet<>();
        if (ids == null || ids.isEmpty()) return result;
        for (Object id : ids) {
            ChildInstanceType instance = get(id);
            if (instance != null) result.add(instance);
        }
        return result;
    }
}
