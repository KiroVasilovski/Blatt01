package de.dis.menu;

import de.dis.console.FormUtil;
import de.dis.console.Menu;
import de.dis.console.MenuOption;
import de.dis.data.model.Makler;
import de.dis.data.model.contract.Person;
import de.dis.data.model.contract.PurchaseContract;
import de.dis.data.model.contract.TenancyContract;
import de.dis.data.model.estate.Apartment;
import de.dis.data.model.estate.House;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ContractMenu {

    enum ContractType {PURCHASECONTRACT, TENANCYCONTRACT}

    private final Makler makler;

    public ContractMenu(Makler makler) {
        this.makler = makler;
    }

    public void showContractMenu() {
        //Menüoptionen
        final int NEW_PERSON = 0;
        final int SIGN_CONTRACT = 1;
        final int SHOW_CONTRACTS = 2;
        final int BACK = 3;

        //Verwaltungsmenü Verträge
        Menu<Integer> contractMenu = new Menu("Contract management");
        contractMenu.addEntry("Create new person", NEW_PERSON);
        contractMenu.addEntry("Create contract", SIGN_CONTRACT);
        contractMenu.addEntry("Show contracts", SHOW_CONTRACTS);
        contractMenu.addEntry("Back to the main menu", BACK);

        //Verarbeite Eingabe
        boolean remain = true;
        do {
            int response = contractMenu.show();

            switch (response) {
                case NEW_PERSON -> newPerson();
                case SIGN_CONTRACT -> createContract();
                case SHOW_CONTRACTS -> showContracts();
                case BACK -> remain = false;
            }
        } while (remain);
    }

    private static ContractMenu.ContractType selectContractType() {
        return FormUtil.readSelection("Please select the type of contract:",
                new MenuOption<>("Purchase Contract", ContractMenu.ContractType.PURCHASECONTRACT),
                new MenuOption<>("Tenancy Contract", ContractMenu.ContractType.TENANCYCONTRACT));
    }

    private House selectHouse() {
        Set<House> houses = House.getManagedBy(makler);
        List<MenuOption<House>> list = new ArrayList<>();
        for (House house : houses) {
            MenuOption<House> houseMenuOption = new MenuOption<>(house.toString(), house);
            list.add(houseMenuOption);
        }
        MenuOption<House>[] options = list.toArray(new MenuOption[0]);
        return FormUtil.readSelection("Please select house:", options);
    }

    private Apartment selectApartment() {
        Set<Apartment> apartments = Apartment.getManagedBy(makler);
        List<MenuOption<Apartment>> list = new ArrayList<>();
        for (Apartment apartment : apartments) {
            MenuOption<Apartment> apartmentMenuOption = new MenuOption<>(apartment.toString(), apartment);
            list.add(apartmentMenuOption);
        }
        MenuOption<Apartment>[] options = list.toArray(new MenuOption[0]);
        return FormUtil.readSelection("Please select apartment:", options);
    }

    private static Person selectPerson() {
        Set<Person> personSet = Person.getAll();
        List<MenuOption<Person>> list = new ArrayList<>();
        for (Person person : personSet) {
            MenuOption<Person> personMenuOption = new MenuOption<>(person.toString(), person);
            list.add(personMenuOption);
        }
        MenuOption<Person>[] options = list.toArray(new MenuOption[0]);
        return FormUtil.readSelection("Please select person:", options);
    }

    private static void showContracts() {
        ContractType type = selectContractType();

        if (type == ContractType.PURCHASECONTRACT) {
            System.out.println(PurchaseContract.getAll());
        } else if (type == ContractType.TENANCYCONTRACT) {
            System.out.println(TenancyContract.getAll());
        } else {
            System.out.println("Contract type " + type + " does not exist! Return to the contract menu...");
        }
    }

    private void createContract() {
        ContractType type = selectContractType();

        if (type == ContractType.PURCHASECONTRACT) {

            PurchaseContract purchaseContract = PurchaseContract.create(
                    FormUtil.readString("Place"),
                    FormUtil.readInt("Number of installments"),
                    FormUtil.readInt("Interest rate"),
                    selectPerson(),
                    selectHouse()
            );
            System.out.println("Purchase contract with the ID " + purchaseContract.getId() + " was created.");
        } else if (type == ContractType.TENANCYCONTRACT) {
            TenancyContract tenancyContract = TenancyContract.create(
                    FormUtil.readString("Place"),
                    FormUtil.readDate("Start date"),
                    FormUtil.readInt("Duration"),
                    selectPerson(),
                    selectApartment()
            );
            System.out.println("Tenancy contract with the ID " + tenancyContract.getId() + " was created.");
        } else {
            System.out.println("Contract type " + type + " does not exist! Return to the contract menu...");
        }
    }

    /**
     * Creates a new person.
     */
    private static void newPerson() {
        Person p = Person.create(
                FormUtil.readString("Name"),
                FormUtil.readString("Surname"),
                FormUtil.readString("Address"));

        System.out.println("The person with the ID " + p.getId() + " was created.");
    }
}
