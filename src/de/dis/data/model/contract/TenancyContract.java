package de.dis.data.model.contract;

import de.dis.data.DbColumn;
import de.dis.data.factory.PartitionedModelObjectFactory;
import de.dis.data.model.estate.Apartment;
import de.dis.data.store.DbRow;
import de.dis.data.store.DbRowFactory;

import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Set;

public class TenancyContract extends Contract {
    enum Column implements DbColumn {
        ID(Types.INTEGER, "contract_id"),
        START_DATE(Types.DATE, "start_date"),
        DURATION(Types.INTEGER, "duration"),
        RENTER(Types.INTEGER, "renter_id"),
        APARTMENT(Types.INTEGER, "apartment_id");

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
            new DbRowFactory<>("tenancy_contract", Column.values());

    private static PartitionedModelObjectFactory<Column, Contract.Column, TenancyContract> factory =
            new PartitionedModelObjectFactory<>(dbRowFactory, Contract.dbRowFactory, TenancyContract::new);

    public static TenancyContract get(int id) {
        return factory.get(id);
    }

    public static Set<TenancyContract> getAll() {
        return factory.getAll();
    }

    public static Set<TenancyContract> getRentedBy(Person renter) {
        return factory.getAllWhereChild(Column.RENTER, renter.getId());
    }

    public static TenancyContract getForApartment(Apartment apartment) {
        return factory.getWhereChild(Column.APARTMENT, apartment.getId());
    }

    public static TenancyContract create(String place, LocalDate startDate, int duration, Person renter, Apartment apartment) {
        return factory.create(
                new Object[]{Date.valueOf(LocalDate.now()), place},
                new Object[]{Date.valueOf(startDate), duration, renter.getId(), apartment.getId()}
        );
    }

    private final DbRow<Column> store;

    private TenancyContract(DbRow<Contract.Column> contractStore, DbRow<Column> store) {
        super(contractStore);

        this.store = store;
    }

    public LocalDate getStartDate() {
        return ((java.sql.Date) store.get(Column.START_DATE)).toLocalDate();
    }

    public int getDuration() {
        return (int) store.get(Column.DURATION);
    }

    public Person getRenter() {
        return Person.get((int) store.get(Column.RENTER));
    }

    public Apartment getApartment() {
        return Apartment.get((int) store.get(Column.APARTMENT));
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s", super.toString(), getApartment(), getRenter());
    }
}
