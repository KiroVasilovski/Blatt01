package de.dis.data.model.contract;

import de.dis.data.DbColumn;
import de.dis.data.store.DbRow;
import de.dis.data.store.DbRowFactory;

import java.sql.Types;
import java.time.LocalDate;

abstract class Contract {
    enum Column implements DbColumn {
        ID(Types.INTEGER, "id"),
        DATE(Types.DATE, "date"),
        PLACE(Types.VARCHAR, "place");

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
            new DbRowFactory<>("contract", Column.values());

    private final DbRow<Column> store;

    protected Contract(DbRow<Column> store) {
        this.store = store;
    }

    public int getId() {
        return (int) store.getId();
    }

    public LocalDate getDate() {
        return ((java.sql.Date) store.get(Column.DATE)).toLocalDate();
    }

    public String getPlace() {
        return (String) store.get(Column.PLACE);
    }
}
