package de.dis.data.model.estate;

import de.dis.data.DbColumn;
import de.dis.data.model.Makler;
import de.dis.data.store.DbRow;
import de.dis.data.store.DbRowFactory;

import java.sql.Types;

abstract class Estate {
    enum Column implements DbColumn {
        ID(Types.INTEGER, "estate_id"),
        CITY(Types.VARCHAR, "city"),
        POSTAL_CODE(Types.VARCHAR, "postal_code"),
        STREET(Types.VARCHAR, "street"),
        STREET_NUMBER(Types.VARCHAR, "street_number"),
        SQUARE_AREA(Types.INTEGER, "square_area"),
        ESTATE_AGENT(Types.INTEGER, "estate_agent");

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

    protected static DbRowFactory<Column> dbRowFactory =
            new DbRowFactory<>("estate", Column.values());

    private final DbRow<Column> store;

    protected Estate(DbRow<Column> store) {
        this.store = store;
    }

    public int getId() {
        return (int) store.getId();
    }

    public String getCity() {
        return (String) store.get(Column.CITY);
    }

    public String getPostalCode() {
        return (String) store.get(Column.POSTAL_CODE);
    }

    public String getStreet() {
        return (String) store.get(Column.STREET);
    }

    public String getStreetNumber() {
        return (String) store.get(Column.STREET_NUMBER);
    }

    public int getSquareArea() {
        return (int) store.get(Column.SQUARE_AREA);
    }

    public Makler getEstateAgent() {
        return Makler.get((int) store.get(Column.ESTATE_AGENT));
    }

    public void setCity(String city) {
        store.set(Column.CITY, city);
    }

    public void setPostalCode(String postalCode) {
        store.set(Column.POSTAL_CODE, postalCode);
    }

    public void setStreet(String street) {
        store.set(Column.STREET, street);
    }

    public void setStreetNumber(String streetNumber) {
        store.set(Column.STREET_NUMBER, streetNumber);
    }

    public void setSqareArea(int squareArea) {
        store.set(Column.SQUARE_AREA, squareArea);
    }

    public void setEstateAgent(Makler estateAgent) {
        store.set(Column.ESTATE_AGENT, estateAgent.getId());
    }
}
