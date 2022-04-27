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
     * Estate menu.
     */
    public void showEstateMenu() {
        //Menüoptionen
        final int NEW_ESTATE = 0;
        final int UPDATE_ESTATE = 1;
        final int BACK = 2;

        //Immobilienverwaltungsmenü
        Menu<Integer> estateMenu = new Menu("Immobilien-Verwaltung");
        estateMenu.addEntry("Neue Immobilie", NEW_ESTATE);
        estateMenu.addEntry("Immobilie bearbeiten", UPDATE_ESTATE);
        estateMenu.addEntry("Zurück zum Hauptmenü", BACK);

        //Verarbeite Eingabe
        while (true) {
            int response = estateMenu.show();

            switch (response) {
                case NEW_ESTATE -> newEstate();
                case UPDATE_ESTATE -> showUpdateEstateMenu();
            }
        }
    }

    private static EstateType selectEstateType() {
        return FormUtil.readSelection("Bitte Art der Immobilie auswählen:",
                new MenuOption<>("Haus", EstateType.HOUSE),
                new MenuOption<>("Wohnung", EstateType.APARTMENT));
    }

    /**
     * Gives the possibility to select an existing apartment in the database.
     *
     * @return Apartment.
     */
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

    /**
     * Gives the possibility to select an existing house in the database.
     *
     * @return House.
     */
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

    /**
     * Updates the information of an existing estate in the database.
     */
    private void showUpdateEstateMenu() {
        EstateType type = selectEstateType();

        final int BACK = -1;
        final int DELETE = -2;

        //Estate fields
        final int CITY = 1;
        final int STREET = 2;
        final int POSTALCODE = 3;
        final int STREET_NUMBER = 4;
        final int SQUARE_AREA = 5;
        //Haus Felder:
        final int FLOORS = 6;
        final int PRICE = 7;
        final int GARDEN = 8;
        //Apartment Felder:
        final int FLOOR = 9;
        final int RENT = 10;
        final int ROOMS = 11;
        final int BALCONY = 12;
        final int BUILT_IN_KITCHEN = 13;

        boolean remain = true;

        if (type == EstateType.HOUSE) {
            House house = selectHouse();
            if (house == null) return;

            do {
                int selection = FormUtil.readSelection("Bitte das zu bearbeitende Attribut auswählen",
                        new MenuOption<>(String.format("Stadt (%s)", house.getCity()), CITY),
                        new MenuOption<>(String.format("Straße (%s)", house.getStreet()), STREET),
                        new MenuOption<>(String.format("Postleizahl (%s)", house.getPostalCode()), POSTALCODE),
                        new MenuOption<>(String.format("Hausnummer (%s)", house.getStreetNumber()), STREET_NUMBER),
                        new MenuOption<>(String.format("Wohnfläche (%s)", house.getSquareArea()), SQUARE_AREA),
                        new MenuOption<>(String.format("Etagen (%s)", house.getFloors()), FLOORS),
                        new MenuOption<>(String.format("Preis (%s)", house.getPrice()), PRICE),
                        new MenuOption<>(String.format("Garten (%s)", house.hasGarden()), GARDEN),
                        new MenuOption<>("Haus löschen", DELETE),
                        new MenuOption<>("zurück", BACK));

                switch (selection) {
                    case CITY -> house.setCity(FormUtil.readString("Neue Stadt"));
                    case STREET -> house.setStreet(FormUtil.readString("Neue Straße"));
                    case POSTALCODE -> house.setPostalCode(FormUtil.readString("Neue Postleizahl"));
                    case STREET_NUMBER -> house.setStreetNumber(FormUtil.readString("Neue Hausnummer"));
                    case SQUARE_AREA -> house.setSqareArea(FormUtil.readInt("Neue Wohnfläche"));
                    case FLOORS -> house.setFloors(FormUtil.readInt("Neue Anzahl Etagen"));
                    case PRICE -> house.setPrice(FormUtil.readInt("Neuer Preis"));
                    case GARDEN -> house.setGarden(FormUtil.readBoolean("Garten"));
                    case DELETE -> {
                        if (FormUtil.readBoolean("Bitte bestätigen, dass das Haus endgültig gelöscht werden soll")) {
                            House.delete(house);
                            remain = false;
                        }
                    }
                    case BACK -> remain = false;
                }
            } while (remain);

        } else if (type == EstateType.APARTMENT) {
            Apartment apartment = selectApartment();
            if (apartment == null) return;

            do {
                int selection = FormUtil.readSelection("Bitte das zu bearbeitende Attribut auswählen",
                        new MenuOption<>(String.format("Stadt (%s)", apartment.getCity()), CITY),
                        new MenuOption<>(String.format("Straße (%s)", apartment.getStreet()), STREET),
                        new MenuOption<>(String.format("Postleitzahl (%s)", apartment.getPostalCode()), POSTALCODE),
                        new MenuOption<>(String.format("Hausnummer (%s)", apartment.getStreetNumber()), STREET_NUMBER),
                        new MenuOption<>(String.format("Wohnfläche (%s)", apartment.getSquareArea()), SQUARE_AREA),
                        new MenuOption<>(String.format("Werkstock (%s)", apartment.getFloor()), FLOOR),
                        new MenuOption<>(String.format("Miete (%s)", apartment.getRent()), RENT),
                        new MenuOption<>(String.format("Anzahl Zimmer (%s)", apartment.getRooms()), ROOMS),
                        new MenuOption<>(String.format("Balkon (%s)", apartment.hasBalcony()), BALCONY),
                        new MenuOption<>(String.format("Küche (%s)", apartment.hasKitchen()), BUILT_IN_KITCHEN),
                        new MenuOption<>("Haus löschen", DELETE),
                        new MenuOption<>("zurück", BACK));

                switch (selection) {
                    case CITY -> apartment.setCity(FormUtil.readString("Neue Stadt"));
                    case STREET -> apartment.setStreet(FormUtil.readString("Neue Straße"));
                    case POSTALCODE -> apartment.setPostalCode(FormUtil.readString("Neue Postleizahl"));
                    case STREET_NUMBER -> apartment.setStreetNumber(FormUtil.readString("Neue Hausnummer"));
                    case SQUARE_AREA -> apartment.setSqareArea(FormUtil.readInt("Neue Wohnfläche"));
                    case FLOOR -> apartment.setFloor(FormUtil.readInt("Neuer Stock"));
                    case RENT -> apartment.setRent(FormUtil.readInt("Neue Miete"));
                    case ROOMS -> apartment.setRooms(FormUtil.readInt("Neue Anzahl Zimmer"));
                    case BALCONY -> apartment.setBalcony(FormUtil.readBoolean("Balkon"));
                    case BUILT_IN_KITCHEN -> apartment.setKitchen(FormUtil.readBoolean("Einbauküche"));
                    case DELETE -> {
                        if (FormUtil.readBoolean("Bitte bestätigen, dass die Wohnung endgültig gelöscht werden soll")) {
                            Apartment.delete(apartment);
                            remain = false;
                        }
                    }
                    case BACK -> remain = false;
                }
            } while (remain);

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
