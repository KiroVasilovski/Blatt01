package de.dis.data.model.contract;

import de.dis.data.DbColumn;
import de.dis.data.store.DbRow;
import de.dis.data.store.DbRowFactory;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class Person {
    enum Column implements DbColumn {
        ID(Types.INTEGER, "id"),
        NAME(Types.VARCHAR, "name"),
        FIRSTNAME(Types.VARCHAR, "first_name"),
        ADDRESS(Types.VARCHAR, "address");

        private final int type;
        private final String title;

        Column(int type, String title) {
            this.type = type;
            this.title = title;
        }

        @Override
        public int type() {
            return type;
        }

        @Override
        public String title() {
            return title;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    private static DbRowFactory<Column> dbRowFactory =
            new DbRowFactory<>("person", Column.values());

    private static Map<Object, Person> cache = new HashMap<>();

    public static Person get(int id) {
        if (cache.containsKey(id)) return cache.get(id);

        DbRow<Column> store =
                dbRowFactory.load(id);
        if (store == null) return null;
        return new Person(store);
    }

    public static Person create(String firstName, String name, String address) {
        DbRow<Column> store =
                dbRowFactory.create(firstName, name, address);
        if (store == null) return null;
        return new Person(store);
    }

    private final DbRow<Column> store;

    private Person(DbRow<Column> store) {
        this.store = store;

        cache.put(store.getId(), this);
    }

    public int getId() {
        return (int) store.getId();
    }

    public String getName() {
        return (String) store.get(Column.NAME);
    }

    public String getFirstName() {
        return (String) store.get(Column.FIRSTNAME);
    }

    public String getFullName() {
        return String.format("%s, %s", getName(), getFirstName());
    }

    public String getAddress() {
        return (String) store.get(Column.ADDRESS);
    }

    public void setName(String name) {
        store.set(Column.NAME, name);
    }

    public void setFirstName(String firstName) {
        store.set(Column.FIRSTNAME, firstName);
    }

    public void setAddress(String address) {
        store.set(Column.ADDRESS, address);
    }
}
