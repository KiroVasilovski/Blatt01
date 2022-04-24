package de.dis.data.model.estate;

import de.dis.data.DbColumn;
import de.dis.data.model.Makler;
import de.dis.data.store.DbRow;
import de.dis.data.store.DbRowFactory;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class Apartment extends Estate {
    enum Column implements DbColumn {
        ID(Types.INTEGER, "estate_id"),
        FLOOR(Types.INTEGER, "floor"),
        RENT(Types.INTEGER, "rent"),
        ROOMS(Types.INTEGER, "rooms"),
        BALCONY(Types.BOOLEAN, "balcony"),
        KITCHEN(Types.BOOLEAN, "built_in_kitchen");

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
            new DbRowFactory<>("apartment", Column.values());

    private static Map<Integer, Apartment> cache = new HashMap<>();

    public static Apartment get(int id) {
        if (cache.containsKey(id)) return cache.get(id);

        DbRow<Column> store = dbRowFactory.load(id);
        DbRow<Estate.Column> estateStore = Estate.dbRowFactory.load(id);

        return new Apartment(store, estateStore);
    }

    public static Apartment create(String city, String postalCode, String street, String streetNumber, int squareArea, Makler estateAgent, int floor, int rent, int rooms, boolean balcony, boolean kitchen) {
        DbRow<Estate.Column> estateStore =
                Estate.dbRowFactory.create(city, postalCode, street, streetNumber, squareArea, estateAgent.getId());
        if (estateStore == null) return null;
        Object id = estateStore.getId();
        DbRow<Column> store = dbRowFactory.createWithId(id, floor, rent, rooms, balcony, kitchen);
        if (store == null) return null;

        return new Apartment(store, estateStore);
    }

    private final DbRow<Column> store;

    private Apartment(DbRow<Column> store, DbRow<Estate.Column> estateStore) {
        super(estateStore);

        this.store = store;
    }

    public int getFloor() {
        return (int) store.get(Column.FLOOR);
    }

    public int getRooms() {
        return (int) store.get(Column.ROOMS);
    }

    public int getRent() {
        return (int) store.get(Column.RENT);
    }

    public boolean hasBalcony() {
        return (boolean) store.get(Column.BALCONY);
    }

    public boolean hasKitchen() {
        return (boolean) store.get(Column.KITCHEN);
    }

    public void setRooms(int rooms) {
        store.set(Column.ROOMS, rooms);
    }

    public void setRent(int rent) {
        store.set(Column.RENT, rent);
    }

    public void setBalcony(boolean hasBalcony) {
        store.set(Column.BALCONY, hasBalcony);
    }

    public void setKitchen(boolean hasKitchen) {
        store.set(Column.KITCHEN, hasKitchen);
    }
}
