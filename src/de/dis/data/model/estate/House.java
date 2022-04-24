package de.dis.data.model.estate;

import de.dis.data.DbColumn;
import de.dis.data.model.Makler;
import de.dis.data.store.DbRow;
import de.dis.data.store.DbRowFactory;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class House extends Estate {
    enum Column implements DbColumn {
        ID(Types.INTEGER, "estate_id"),
        FLOORS(Types.INTEGER, "floors"),
        PRICE(Types.INTEGER, "price"),
        GARDEN(Types.BOOLEAN, "garden");

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
            new DbRowFactory<>("house", Column.values());

    private static Map<Integer, House> cache = new HashMap<>();

    public static House get(int id) {
        if (cache.containsKey(id)) return cache.get(id);

        DbRow<Column> store = dbRowFactory.load(id);
        DbRow<Estate.Column> estateStore = Estate.dbRowFactory.load(id);

        return new House(store, estateStore);
    }

    public static House create(String city, String postalCode, String street, String streetNumber, int squareArea, Makler estateAgent, int floors, int price, boolean garden) {
        DbRow<Estate.Column> estateStore =
                Estate.dbRowFactory.create(city, postalCode, street, streetNumber, squareArea, estateAgent.getId());
        if (estateStore == null) return null;
        Object id = estateStore.getId();
        DbRow<Column> store = dbRowFactory.createWithId(id, floors, price, garden);
        if (store == null) return null;

        return new House(store, estateStore);
    }

    private final DbRow<Column> store;

    private House(DbRow<Column> store, DbRow<Estate.Column> estateStore) {
        super(estateStore);

        this.store = store;
    }

    public int getFloors() {
        return (int) store.get(Column.FLOORS);
    }

    public int getPrice() {
        return (int) store.get(Column.PRICE);
    }

    public boolean hasGarden() {
        return (boolean) store.get(Column.GARDEN);
    }

    public void setFloors(int floors) {
        store.set(Column.FLOORS, floors);
    }

    public void setPrice(int price) {
        store.set(Column.PRICE, price);
    }

    public void setGarden(boolean hasGarden) {
        store.set(Column.GARDEN, hasGarden);
    }
}
