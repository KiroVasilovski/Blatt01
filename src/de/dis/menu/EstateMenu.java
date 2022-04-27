package de.dis.menu;

import de.dis.console.FormUtil;
import de.dis.console.Menu;
import de.dis.console.MenuOption;
import de.dis.data.model.EstateAgent;
import de.dis.data.model.estate.Apartment;
import de.dis.data.model.estate.House;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EstateMenu {
    enum EstateType {APARTMENT, HOUSE}

    private final EstateAgent estateAgent;

    public EstateMenu(EstateAgent estateAgent) {
        this.estateAgent = estateAgent;
    }

    /**
     * Estate menu.
     */
    public void showEstateMenu() {
        //Menu options
        final int NEW_ESTATE = 0;
        final int UPDATE_ESTATE = 1;
        final int BACK = 2;

        //Property management menu
        Menu<Integer> estateMenu = new Menu("Estate management");
        estateMenu.addEntry("New estate", NEW_ESTATE);
        estateMenu.addEntry("Edit estate", UPDATE_ESTATE);
        estateMenu.addEntry("Back to the main menu", BACK);

        //Processed input
        boolean remain = true;

        do {
            int response = estateMenu.show();

            switch (response) {
                case NEW_ESTATE -> newEstate();
                case UPDATE_ESTATE -> showUpdateEstateMenu();
                case BACK -> remain = false;
            }
        } while (remain);
    }

    private static EstateType selectEstateType() {
        return FormUtil.readSelection("Please select the type of estate:",
                new MenuOption<>("House", EstateType.HOUSE),
                new MenuOption<>("Apartment", EstateType.APARTMENT));
    }

    /**
     * Gives the possibility to select an existing apartment in the database.
     *
     * @return Apartment.
     */
    private Apartment selectApartment() {
        Set<Apartment> apartments = Apartment.getManagedBy(estateAgent);
        List<MenuOption<Apartment>> list = new ArrayList<>();
        for (Apartment apartment : apartments) {
            MenuOption<Apartment> apartmentMenuOption = new MenuOption<>(apartment.toString(), apartment);
            list.add(apartmentMenuOption);
        }
        MenuOption<Apartment>[] options = list.toArray(new MenuOption[0]);
        return FormUtil.readSelection("Please select apartment", options);
    }

    /**
     * Gives the possibility to select an existing house in the database.
     *
     * @return House.
     */
    private House selectHouse() {
        Set<House> houses = House.getManagedBy(estateAgent);
        List<MenuOption<House>> list = new ArrayList<>();
        for (House house : houses) {
            MenuOption<House> houseMenuOption = new MenuOption<>(house.toString(), house);
            list.add(houseMenuOption);
        }
        MenuOption<House>[] options = list.toArray(new MenuOption[0]);
        return FormUtil.readSelection("Please select house", options);
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
                int selection = FormUtil.readSelection("Please select the attribute to be edited",
                        new MenuOption<>(String.format("City (%s)", house.getCity()), CITY),
                        new MenuOption<>(String.format("Street (%s)", house.getStreet()), STREET),
                        new MenuOption<>(String.format("Postal code (%s)", house.getPostalCode()), POSTALCODE),
                        new MenuOption<>(String.format("Street number (%s)", house.getStreetNumber()), STREET_NUMBER),
                        new MenuOption<>(String.format("Square area (%s)", house.getSquareArea()), SQUARE_AREA),
                        new MenuOption<>(String.format("Floors (%s)", house.getFloors()), FLOORS),
                        new MenuOption<>(String.format("Price (%s)", house.getPrice()), PRICE),
                        new MenuOption<>(String.format("Garden (%s)", house.hasGarden()), GARDEN),
                        new MenuOption<>("Remove house", DELETE),
                        new MenuOption<>("Back", BACK));

                switch (selection) {
                    case CITY -> house.setCity(FormUtil.readString("New city"));
                    case STREET -> house.setStreet(FormUtil.readString("New street"));
                    case POSTALCODE -> house.setPostalCode(FormUtil.readString("New postal code"));
                    case STREET_NUMBER -> house.setStreetNumber(FormUtil.readString("New street number"));
                    case SQUARE_AREA -> house.setSqareArea(FormUtil.readInt("New square area"));
                    case FLOORS -> house.setFloors(FormUtil.readInt("New floors number"));
                    case PRICE -> house.setPrice(FormUtil.readInt("New price"));
                    case GARDEN -> house.setGarden(FormUtil.readBoolean("Garden"));
                    case DELETE -> {
                        if (FormUtil.readBoolean("Please confirm that the house is to be permanently deleted")) {
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
                int selection = FormUtil.readSelection("Please select the attribute to be edited",
                        new MenuOption<>(String.format("City (%s)", apartment.getCity()), CITY),
                        new MenuOption<>(String.format("Street (%s)", apartment.getStreet()), STREET),
                        new MenuOption<>(String.format("Postal code (%s)", apartment.getPostalCode()), POSTALCODE),
                        new MenuOption<>(String.format("Street number (%s)", apartment.getStreetNumber()), STREET_NUMBER),
                        new MenuOption<>(String.format("Square area (%s)", apartment.getSquareArea()), SQUARE_AREA),
                        new MenuOption<>(String.format("Floor (%s)", apartment.getFloor()), FLOOR),
                        new MenuOption<>(String.format("Rent (%s)", apartment.getRent()), RENT),
                        new MenuOption<>(String.format("Rooms (%s)", apartment.getRooms()), ROOMS),
                        new MenuOption<>(String.format("Balcony (%s)", apartment.hasBalcony()), BALCONY),
                        new MenuOption<>(String.format("Kitchen (%s)", apartment.hasKitchen()), BUILT_IN_KITCHEN),
                        new MenuOption<>("Delete house", DELETE),
                        new MenuOption<>("Back", BACK));

                switch (selection) {
                    case CITY -> apartment.setCity(FormUtil.readString("New city"));
                    case STREET -> apartment.setStreet(FormUtil.readString("New street"));
                    case POSTALCODE -> apartment.setPostalCode(FormUtil.readString("New Postal code"));
                    case STREET_NUMBER -> apartment.setStreetNumber(FormUtil.readString("New street number"));
                    case SQUARE_AREA -> apartment.setSqareArea(FormUtil.readInt("New square area"));
                    case FLOOR -> apartment.setFloor(FormUtil.readInt("New floor"));
                    case RENT -> apartment.setRent(FormUtil.readInt("New Rent"));
                    case ROOMS -> apartment.setRooms(FormUtil.readInt("New rooms number"));
                    case BALCONY -> apartment.setBalcony(FormUtil.readBoolean("Balcony"));
                    case BUILT_IN_KITCHEN -> apartment.setKitchen(FormUtil.readBoolean("Kitchen"));
                    case DELETE -> {
                        if (FormUtil.readBoolean("Please confirm that the apartment is to be permanently deleted")) {
                            Apartment.delete(apartment);
                            remain = false;
                        }
                    }
                    case BACK -> remain = false;
                }
            } while (remain);

        } else {
            System.out.println("Estate type " + type + " does not exist! Return to the Estate menu...");
        }
    }


    /**
     * Creates a new estate after the user has
     * entered the corresponding data.
     */
    public void newEstate() {
        EstateType type = selectEstateType();

        if (type == EstateType.HOUSE) {
            House house = House.create(
                    FormUtil.readString("City"),
                    FormUtil.readString("Postal code"),
                    FormUtil.readString("Street"),
                    FormUtil.readString("Street number"),
                    FormUtil.readInt("Square area"),
                    estateAgent,
                    FormUtil.readInt("Floors"),
                    FormUtil.readInt("Price"),
                    FormUtil.readBoolean("Garden?")
            );
            System.out.println("House with ID " + house.getId() + " was created.");
        } else if (type == EstateType.APARTMENT) {
            Apartment apartment = Apartment.create(
                    FormUtil.readString("City"),
                    FormUtil.readString("Postal code"),
                    FormUtil.readString("Street"),
                    FormUtil.readString("Street number"),
                    FormUtil.readInt("Square area"),
                    estateAgent,
                    FormUtil.readInt("Floor"),
                    FormUtil.readInt("Rent"),
                    FormUtil.readInt("Rooms"),
                    FormUtil.readBoolean("Balcony?"),
                    FormUtil.readBoolean("Kitchen?")
            );
            System.out.println("Apartment with ID " + apartment.getId() + " was created.");
        } else {
            System.out.println("Estate type " + type + " does not exist! Return to the Estate menu...");
        }
    }
}
