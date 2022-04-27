package de.dis.data.model.contract;

import de.dis.data.DbColumn;
import de.dis.data.factory.PartitionedModelObjectFactory;
import de.dis.data.model.estate.House;
import de.dis.data.store.DbRow;
import de.dis.data.store.DbRowFactory;

import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
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

    private static PartitionedModelObjectFactory<Column, Contract.Column, PurchaseContract> factory = new PartitionedModelObjectFactory(dbRowFactory, Contract.dbRowFactory, PurchaseContract::new);

    public static PurchaseContract get(int id) {
        return factory.get(id);
    }

    public static Set<PurchaseContract> getAll() {
        return factory.getAll();
    }

    public static Set<PurchaseContract> getPurchasedBy(Person buyer) {
        return factory.getAllWhereChild(Column.BUYER, buyer.getId());
    }

    public static PurchaseContract getForHouse(House house) {
        return factory.getWhereChild(Column.HOUSE, house.getId());
    }

    public static PurchaseContract create(String place, int noInstallments, float interestRate, Person buyer, House house) {
        return factory.create(
                new Object[]{Date.valueOf(LocalDate.now()), place},
                new Object[]{noInstallments, interestRate, buyer.getId(), house.getId()});
    }

    private final DbRow<Column> store;

    private PurchaseContract(DbRow<Contract.Column> contractStore, DbRow<Column> store) {
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

    @Override
    public String toString() {
        return String.format("%s | %s | %s", super.toString(), getHouse(), getBuyer());
    }
}