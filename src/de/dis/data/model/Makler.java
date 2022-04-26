package de.dis.data.model;

import de.dis.data.DbColumn;
import de.dis.data.store.DbRow;
import de.dis.data.store.DbRowFactory;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Makler {
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

    private static Map<Object, Makler> cache = new HashMap<>();

    public static Makler get(int id) {
        if (cache.containsKey(id)) return cache.get(id);

        DbRow<Column> store =
                dbRowFactory.load(id);
        if (store == null) return null;
        return new Makler(store);
    }

    public static Makler getByLogin(String login) {
        DbRow<Column> store = dbRowFactory.loadWhere(Column.LOGIN, login);
        if (store == null) return null;
        return new Makler(store);
    }

    public static Makler create(String name, String address, String login, String password) {
        DbRow<Column> store =
                dbRowFactory.create(name, address, login, password);
        if (store == null) return null;
        return new Makler(store);
    }

    private final DbRow<Column> store;

    private Makler(DbRow<Column> store) {
        this.store = store;

        cache.put(store.getId(), this);
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
}
