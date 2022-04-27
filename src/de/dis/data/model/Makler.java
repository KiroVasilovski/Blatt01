package de.dis.data.model;

import de.dis.data.DbColumn;
import de.dis.data.factory.ModelObject;
import de.dis.data.factory.ModelObjectFactory;
import de.dis.data.store.DbRow;
import de.dis.data.store.DbRowFactory;

import java.sql.Types;
import java.util.Objects;
import java.util.Set;

public class Makler implements ModelObject {
    enum Column implements DbColumn {
        ID(Types.INTEGER, "estate_agent_id"),
        NAME(Types.VARCHAR, "name"),
        ADDRESS(Types.VARCHAR, "address"),
        LOGIN(Types.VARCHAR, "login"),
        PASSWORD(Types.VARCHAR, "password");

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
            new DbRowFactory<>("estate_agent", Column.values());

    private static ModelObjectFactory<Column, Makler> factory = new ModelObjectFactory(dbRowFactory, Makler::new);

    public static Makler get(int id) {
        return factory.get(id);
    }

    public static Set<Makler> getAll() {
        return factory.getAll();
    }

    public static Makler getByLogin(String login) {
        return factory.getWhere(Column.LOGIN, login);
    }

    public static Makler create(String name, String address, String login, String password) {
        return factory.create(name, address, login, password);
    }

    public static void delete(Makler makler){
        factory.delete(makler);
    }

    private final DbRow<Column> store;

    private Makler(DbRow<Column> store) {
        this.store = store;
    }

    public int getId() {
        return (int) store.getId();
    }

    public String getName() {
        return (String) store.get(Column.NAME);
    }

    public String getAddress() {
        return (String) store.get(Column.ADDRESS);
    }

    public String getLogin() {
        return (String) store.get(Column.LOGIN);
    }
    
    public boolean comparePassword(String password) {
        Objects.requireNonNull(password);
        return password.equals((String) store.get(Column.PASSWORD));
    }

    public void setName(String name) {
        store.set(Column.NAME, name);
    }

    public void setAddress(String address) {
        store.set(Column.ADDRESS, address);
    }

    public void setLogin(String login) {
        store.set(Column.LOGIN, login);
    }

    public void setPassword(String password) {
        store.set(Column.PASSWORD, password);
    }

    @Override
    public String toString() {
        return String.format("Agent @%s (%s)", getLogin(), getName());
    }
}
