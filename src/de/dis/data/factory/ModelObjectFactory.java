package de.dis.data.factory;

import de.dis.data.DbColumn;
import de.dis.data.store.DbRow;
import de.dis.data.store.DbRowFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModelObjectFactory<ColumnType extends Enum<ColumnType> & DbColumn, InstanceType extends ModelObject> {
    private final ModelObjectFactoryFunction<ColumnType, InstanceType> factoryFunction;
    private final DbRowFactory<ColumnType> dbRowFactory;
    private final Map<Object, InstanceType> cache = new HashMap<>();

    public ModelObjectFactory(DbRowFactory<ColumnType> dbRowFactory, ModelObjectFactoryFunction<ColumnType, InstanceType> factoryFunction) {
        this.dbRowFactory = dbRowFactory;
        this.factoryFunction = factoryFunction;
    }

    public InstanceType get(Object id) {
        if (cache.containsKey(id)) return cache.get(id);

        DbRow<ColumnType> store = dbRowFactory.load(id);

        if (store == null) return null;

        return factoryFunction.instantiate(store);
    }

    public InstanceType create(Object... values) {
        DbRow<ColumnType> store =
                dbRowFactory.create(values);
        if (store == null) return null;
        return factoryFunction.instantiate(store);
    }

    public void delete(InstanceType instance) {
        int id = instance.getId();
        dbRowFactory.delete(id);
        cache.remove(id);
    }

    public Set<InstanceType> getAll() {
        Set<DbRow<ColumnType>> rows = dbRowFactory.loadAll();
        return bulkInstantiate(rows);
    }

    public Set<InstanceType> getAllWhere(ColumnType column, Object value) {
        Set<DbRow<ColumnType>> rows = dbRowFactory.loadAllWhere(column, value);
        return bulkInstantiate(rows);
    }

    public InstanceType getWhere(ColumnType column, Object value) {
        DbRow<ColumnType> row = dbRowFactory.loadWhere(column, value);
        if (row == null) return null;
        Object id = row.getId();
        if (cache.containsKey(id)) return cache.get(id);
        return factoryFunction.instantiate(row);
    }

    private Set<InstanceType> bulkInstantiate(Set<DbRow<ColumnType>> rows) {
        Set<InstanceType> result = new HashSet<>();
        if (rows == null || rows.isEmpty()) return result;
        for (DbRow<ColumnType> row : rows) {
            InstanceType instance = factoryFunction.instantiate(row);
            if (instance != null) result.add(instance);
        }
        return result;
    }
}
