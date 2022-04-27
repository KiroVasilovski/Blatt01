package de.dis.data.model.estate;

import de.dis.data.DbColumn;
import de.dis.data.factory.PartitionedModelObjectFactory;
import de.dis.data.model.EstateAgent;
import de.dis.data.store.DbRow;
import de.dis.data.store.DbRowFactory;

import java.sql.Types;
import java.util.Set;

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

    private static PartitionedModelObjectFactory<Apartment.Column, Estate.Column, House> factory =
            new PartitionedModelObjectFactory(dbRowFactory, Estate.dbRowFactory, House::new);

    public static House get(int id) {
        return factory.get(id);
    }

    public static Set<House> getManagedBy(EstateAgent estateAgent) {
        return factory.getAllWhereParent(Estate.Column.ESTATE_AGENT, estateAgent.getId());
    }

    public static Set<House> getAll() {
        return factory.getAll();
    }

    public static House create(String city, String postalCode, String street, String streetNumber, int squareArea, EstateAgent estateAgent, int floors, int price, boolean garden) {
        return factory.create(
                new Object[]{city, postalCode, street, streetNumber, squareArea, estateAgent.getId()},
                new Object[]{floors, price, garden}
        );
    }

    public static void delete(House house) {
        factory.delete(house);
    }

    private final DbRow<Column> store;

    private House(DbRow<Estate.Column> estateStore, DbRow<Column> store) {
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

    @Override
    public String toString() {
        return super.toString() + " (House)";
    }
}
