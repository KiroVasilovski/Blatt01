package de.dis.data.model.contract;

import de.dis.data.DbColumn;
import de.dis.data.model.estate.Apartment;
import de.dis.data.store.DbRow;
import de.dis.data.store.DbRowFactory;

import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TenancyContract extends Contract {
    enum Column implements DbColumn {
        ID(Types.INTEGER, "contract_id"),
        START_DATE(Types.DATE, "date"),
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

    private static Map<Integer, TenancyContract> cache = new HashMap<>();

    public static TenancyContract get(int id) {
        if (cache.containsKey(id)) return cache.get(id);

        DbRow<Column> store = dbRowFactory.load(id);
        DbRow<Contract.Column> estateStore = Contract.dbRowFactory.load(id);

        return new TenancyContract(store, estateStore);
    }

    public static Set<TenancyContract> getAll() {
        Set<DbRow<Contract.Column>> rows = Contract.dbRowFactory.loadAll();
        Set<TenancyContract> result = new HashSet<>();
        if (rows == null || rows.isEmpty()) return result;
        for (DbRow<Contract.Column> row : rows) {
            TenancyContract contract = get((int) row.getId());
            if (contract != null) result.add(contract);
        }
        return result;
    }

    public static Set<TenancyContract> getRentedBy(Person renter) {
        Set<DbRow<Column>> rows = dbRowFactory.loadAllWhere(Column.RENTER, renter);
        Set<TenancyContract> result = new HashSet<>();
        if (rows == null) return result;
        for (DbRow<Column> row : rows) {
            result.add(get((int) row.getId()));
        }
        return result;
    }

    public static TenancyContract getForApartment(Apartment apartment) {
        DbRow<Column> row = dbRowFactory.loadWhere(Column.APARTMENT, apartment);
        if (row == null) return null;
        return get((int) row.getId());
    }

    public static TenancyContract create(String place, LocalDate startDate, int duration, Person renter, Apartment apartment) {
        DbRow<Contract.Column> contractStore =
                Contract.dbRowFactory.create(Date.valueOf(LocalDate.now()), place);
        if (contractStore == null) return null;
        Object id = contractStore.getId();
        DbRow<Column> store = dbRowFactory.createWithId(id, Date.valueOf(startDate), duration, renter.getId(), apartment.getId());
        if (store == null) return null;

        return new TenancyContract(store, contractStore);
    }

    private final DbRow<Column> store;

    private TenancyContract(DbRow<Column> store, DbRow<Contract.Column> contractStore) {
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
}
