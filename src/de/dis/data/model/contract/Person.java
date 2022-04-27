package de.dis.data.model.contract;

import de.dis.data.DbColumn;
import de.dis.data.factory.ModelObject;
import de.dis.data.factory.ModelObjectFactory;
import de.dis.data.store.DbRow;
import de.dis.data.store.DbRowFactory;

import java.sql.Types;
import java.util.Set;

public class Person implements ModelObject {
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

    private static ModelObjectFactory<Column, Person> factory = new ModelObjectFactory<Column, Person>(dbRowFactory, Person::new);

    public static Person get(int id) {
        return factory.get(id);
    }

    // TODO all these getAll and create methods are rather redundant
    public static Set<Person> getAll() {
        return factory.getAll();
    }

    public static Person create(String firstName, String name, String address) {
        return factory.create(firstName, name, address);
    }

    private final DbRow<Column> store;

    private Person(DbRow<Column> store) {
        this.store = store;
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

    @Override
    public String toString() {
        return getFullName();
    }
}
