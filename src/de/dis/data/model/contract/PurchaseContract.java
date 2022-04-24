package de.dis.data.model.contract;

import de.dis.data.DbColumn;
import de.dis.data.model.estate.House;
import de.dis.data.store.DbRow;
import de.dis.data.store.DbRowFactory;

import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PurchaseContract extends Contract {
    enum Column implements DbColumn {
        ID(Types.INTEGER, "contract_id"),
        INSTALLMENTS(Types.INTEGER, "no_installments"),
        INTEREST_RATE(Types.FLOAT, "interest_rate"),
        BUYER(Types.INTEGER, "buyer_id"),
        HOUSE(Types.INTEGER, "house_id");

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
            new DbRowFactory<>("purchase_contract", Column.values());

    private static Map<Integer, PurchaseContract> cache = new HashMap<>();

    public static PurchaseContract get(int id) {
        if (cache.containsKey(id)) return cache.get(id);

        DbRow<Column> store = dbRowFactory.load(id);
        DbRow<Contract.Column> estateStore = Contract.dbRowFactory.load(id);

        return new PurchaseContract(store, estateStore);
    }

    public static Set<PurchaseContract> getPurchasedBy(Person buyer) {
        Set<DbRow<Column>> rows = dbRowFactory.loadAllWhere(Column.BUYER, buyer);
        Set<PurchaseContract> result = new HashSet<>();
        if (rows == null) return result;
        for (DbRow<Column> row : rows) {
            result.add(get((int) row.getId()));
        }
        return result;
    }

    public static PurchaseContract getForHouse(House house) {
        DbRow<Column> row = dbRowFactory.loadWhere(Column.HOUSE, house);
        if (row == null) return null;
        return get((int) row.getId());
    }

    public static PurchaseContract create(String place, int noInstallments, float interestRate, Person buyer, House house) {
        DbRow<Contract.Column> contractStore =
                Contract.dbRowFactory.create(Date.valueOf(LocalDate.now()), place);
        if (contractStore == null) return null;
        Object id = contractStore.getId();
        DbRow<Column> store = dbRowFactory.createWithId(id, noInstallments, interestRate, buyer.getId(), house.getId());
        if (store == null) return null;

        return new PurchaseContract(store, contractStore);
    }

    private final DbRow<Column> store;

    private PurchaseContract(DbRow<Column> store, DbRow<Contract.Column> contractStore) {
        super(contractStore);

        this.store = store;
    }

    public int getNoInstallments() {
        return (int) store.get(Column.INSTALLMENTS);
    }

    public float getInterestRate() {
        return (float) store.get(Column.INTEREST_RATE);
    }

    public Person getBuyer() {
        return Person.get((int) store.get(Column.BUYER));
    }

    public House getHouse() {
        return House.get((int) store.get(Column.HOUSE));
    }
}