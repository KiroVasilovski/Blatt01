package de.dis.menu;

import de.dis.console.FormUtil;
import de.dis.console.Menu;
import de.dis.console.MenuOption;
import de.dis.data.model.Makler;
import de.dis.data.model.estate.Apartment;
import de.dis.data.model.estate.House;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EstateMenu {
    enum EstateType {APARTMENT, HOUSE}

    private final Makler makler;

    public EstateMenu(Makler makler) {
        this.makler = makler;
    }

    /**
     * Zeigt die Immobilienverwaltung an.
     */
    public void showEstateMenu() {
        //Menüoptionen
        final int NEW_ESTATE = 0;
        final int DEL_ESTATE = 1;
        final int UPDATE_ESTATE = 2;
        final int BACK = 3;

        //Immobilienverwaltungsmenü
        Menu<Integer> estateMenu = new Menu("Immobilien-Verwaltung");
        estateMenu.addEntry("Neue Immobilie", NEW_ESTATE);
        estateMenu.addEntry("Immobilie entfernen", DEL_ESTATE);
        estateMenu.addEntry("Immobilie bearbeiten", UPDATE_ESTATE);
        estateMenu.addEntry("Zurück zum Hauptmenü", BACK);

        //Verarbeite Eingabe
        while (true) {
            int response = estateMenu.show();

            switch (response) {
                case NEW_ESTATE:
                    newEstate();
                    break;
                case DEL_ESTATE:
                    //TODO-delEstate();
                    break;
                case UPDATE_ESTATE:
                    updateEstate();
                    break;
                case BACK:
                    return;
            }
        }
    }

    private static EstateType selectEstateType() {
        return FormUtil.readSelection("Bitte Art der Immobilie auswählen:",
                new MenuOption<>("Haus", EstateType.HOUSE),
                new MenuOption<>("Wohnung", EstateType.APARTMENT));
    }

    private Apartment selectApartment() {
        Set<Apartment> apartments = Apartment.getManagedBy(makler);
        List<MenuOption<Apartment>> list = new ArrayList<>();
        for (Apartment apartment : apartments) {
            MenuOption<Apartment> apartmentMenuOption = new MenuOption<>(apartment.toString(), apartment);
            list.add(apartmentMenuOption);
        }
        MenuOption<Apartment>[] options = list.toArray(new MenuOption[0]);
        return FormUtil.readSelection("Bitte Apartment auswählen:", options);
    }

    private House selectHouse() {
        Set<House> houses = House.getManagedBy(makler);
        List<MenuOption<House>> list = new ArrayList<>();
        for (House house : houses) {
            MenuOption<House> houseMenuOption = new MenuOption<>(house.toString(), house);
            list.add(houseMenuOption);
        }
        MenuOption<House>[] options = list.toArray(new MenuOption[0]);
        return FormUtil.readSelection("Bitte Haus auswählen:", options);
    }

    private void updateEstate() {
        EstateType type = selectEstateType();

        final int CITY = 1;
        final int STREET = 2;

        if (type == EstateType.HOUSE) {
            House house = selectHouse();


            int selection = FormUtil.readSelection("Bitte das zu bearbeitende Attribut auswählen",
                    new MenuOption<>(String.format("Stadt (%s)", house.getCity()), CITY),
                    new MenuOption<>(String.format("Straße (%s)", house.getStreet()), STREET));

            switch (selection) {
                case CITY:
                    house.setCity(FormUtil.readString("Neue Stadt:"));
                    break;
                case STREET:
                    house.setStreet(FormUtil.readString("Neue Straße:"));
                    break;
            }

        } else if (type == EstateType.APARTMENT) {
            Apartment apartment = selectApartment();

            int selection = FormUtil.readSelection("Bitte das zu bearbeitende Attribut auswählen",
                    new MenuOption<>(String.format("Stadt (%s)", apartment.getCity()), CITY),
                    new MenuOption<>(String.format("Straße (%s)", apartment.getStreet()), STREET));

            switch (selection) {
                case CITY:
                    apartment.setCity(FormUtil.readString("Neue Stadt:"));
                    break;
                case STREET:
                    apartment.setStreet(FormUtil.readString("Neue Straße:"));
                    break;
            }

        } else {
            System.out.println("Immobilientyp " + type + " existiert nicht! Kehre zum Immobilienmenü zurück...");
        }
    }


    /**
     * Legt ein neues Estate an, nachdem der Benutzer
     * die entprechenden Daten eingegeben hat.
     */
    public void newEstate() {
        EstateType type = selectEstateType();

        if (type == EstateType.HOUSE) {
            House house = House.create(
                    FormUtil.readString("Stadt"),
                    FormUtil.readString("Postleitzahl"),
                    FormUtil.readString("Straße"),
                    FormUtil.readString("Hausnummer"),
                    FormUtil.readInt("Fläche"),
                    makler,
                    FormUtil.readInt("Stockwerke"),
                    FormUtil.readInt("Kaufpreis"),
                    FormUtil.readBoolean("Garten?")
            );
            System.out.println("Haus mit ID " + house.getId() + " wurde erstellt.");
        } else if (type == EstateType.APARTMENT) {
            Apartment apartment = Apartment.create(
                    FormUtil.readString("Stadt"),
                    FormUtil.readString("Postleitzahl"),
                    FormUtil.readString("Straße"),
                    FormUtil.readString("Hausnummer"),
                    FormUtil.readInt("Fläche"),
                    makler,
                    FormUtil.readInt("Stockwerk"),
                    FormUtil.readInt("Miete"),
                    FormUtil.readInt("Zimmer"),
                    FormUtil.readBoolean("Balkon?"),
                    FormUtil.readBoolean("Küche?")
            );
            System.out.println("Apartment mit ID " + apartment.getId() + " wurde erstellt.");
        } else {
            System.out.println("Immobilientyp " + type + " existiert nicht! Kehre zum Immobilienmenü zurück...");
        }
    }
}
