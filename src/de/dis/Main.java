package de.dis;

import de.dis.console.FormUtil;
import de.dis.console.Menu;
import de.dis.console.MenuOption;
import de.dis.data.model.Makler;
import de.dis.data.model.estate.Apartment;
import de.dis.data.model.estate.House;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Hauptklasse
 */
public class Main {
    /**
     * Startet die Anwendung
     */
    public static void main(String[] args) {
        showMainMenu();
    }

    /**
     * Zeigt das Hauptmenü
     */
    public static void showMainMenu() {
        //Menüoptionen
        final int MENU_MAKLER = 0;
        final int MENU_ESTATE = 1;
        final int MENU_CONTRACT = 2;
        final int QUIT = 3;

        //Erzeuge Menü
        Menu<Integer> mainMenu = new Menu("Hauptmenü");
        mainMenu.addEntry("Makler-Verwaltung", MENU_MAKLER);
        mainMenu.addEntry("Immobilien-Verwaltung", MENU_ESTATE);
        mainMenu.addEntry("Vertragsverwaltung", MENU_CONTRACT);
        mainMenu.addEntry("Beenden", QUIT);

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        //Verarbeite Eingabe
        while (true) {
            int response = mainMenu.show();

            switch (response) {
                case MENU_MAKLER:
                    showMaklerMenu();
                    break;
                case MENU_ESTATE:
                    System.out.println("Bitte Makler ID eingeben:");

                    try {
                        String id = stdin.readLine();
                        int maklerID = Integer.parseInt(id);
                        Makler m = Makler.get(maklerID);
                        if (m == null) {
                            System.out.println("Datensatz nicht gefunden! " + maklerID + " existiert nicht!");
                            break;
                        }
                        System.out.println("Datensatz gefunden! Bitte Passwort für den Makler mit der ID " + maklerID + " eingeben:");
                        String password = m.getPassword();
                        String input = stdin.readLine();
                        if (input.equals(password)) {
                            showEstateMenu();
                            break;
                        } else {
                            System.out.println("Falsches Makler-Passwort! Kehre zum Hauptmenü zurück.");
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                case QUIT:
                    return;
            }
        }
    }

    /**
     * Zeigt die Maklerverwaltung
     */
    public static void showMaklerMenu() {
        //Menüoptionen
        final int NEW_MAKLER = 0;
        final int BACK = 1;

        //Maklerverwaltungsmenü
        Menu<Integer> maklerMenu = new Menu("Makler-Verwaltung");
        maklerMenu.addEntry("Neuer Makler", NEW_MAKLER);
        maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);

        //Verarbeite Eingabe
        while (true) {
            int response = maklerMenu.show();

            switch (response) {
                case NEW_MAKLER:
                    newMakler();
                    break;
                case BACK:
                    return;
            }
        }
    }

    /**
     * Legt einen neuen Makler an, nachdem der Benutzer
     * die entprechenden Daten eingegeben hat.
     */
    public static void newMakler() {
        Makler m = Makler.create(
                FormUtil.readString("Name"),
                FormUtil.readString("Addressse"),
                FormUtil.readString("Login"),
                FormUtil.readString("Passwort")
        );

        System.out.println("Makler mit der ID " + m.getId() + " wurde erzeugt.");
    }

    /**
     * Zeigt die Immobilienverwaltung an.
     */
    public static void showEstateMenu() {
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

    private static Apartment selectApartment() {
        Set<Apartment> apartments = Apartment.getAll();
        List<MenuOption<Apartment>> list = new ArrayList<>();
        for (Apartment apartment : apartments) {
            MenuOption<Apartment> apartmentMenuOption = new MenuOption<>(apartment.toString(), apartment);
            list.add(apartmentMenuOption);
        }
        MenuOption<Apartment>[] options = list.toArray(new MenuOption[0]);
        return FormUtil.readSelection("Bitte Apartment auswählen:", options);
    }

    private static House selectHouse() {
        Set<House> houses = House.getAll();
        List<MenuOption<House>> list = new ArrayList<>();
        for (House house : houses) {
            MenuOption<House> houseMenuOption = new MenuOption<>(house.toString(), house);
            list.add(houseMenuOption);
        }
        MenuOption<House>[] options = list.toArray(new MenuOption[0]);
        return FormUtil.readSelection("Bitte Haus auswählen:", options);
    }

    private static void updateEstate() {
        String estateType = FormUtil.readString("Bitte Art der Immobilie eingeben (Haus, Apartment)");

        final int CITY = 1;
        final int STREET = 2;

        if (estateType.equals("Haus")) {
            House house = selectHouse();



            int selection = FormUtil.readSelection("Bitte das zu bearbeitende Attribut auswählen",
                    new MenuOption<>(String.format("Stadt (%s)", house.getCity()), CITY),
                    new MenuOption<>(String.format("Straße (%s)", house.getStreet()), STREET));

        } else if (estateType.equals("Apartment")) {
            Apartment apartment = selectApartment();

            int selection = FormUtil.readSelection("Bitte das zu bearbeitende Attribut auswählen",
                    new MenuOption<>(String.format("Stadt (%s)", apartment.getCity()), CITY),
                    new MenuOption<>(String.format("Straße (%s)", apartment.getStreet()), STREET));

        } else {
            System.out.println("Immobilientyp " + estateType + " existiert nicht! Kehre zum Immobilienmenü zurück...");
        }
    }


    /**
     * Legt ein neues Estate an, nachdem der Benutzer
     * die entprechenden Daten eingegeben hat.
     */
    public static void newEstate() {
        String estateType = FormUtil.readString("Bitte Art der Immobilie eingeben (Haus, Apartment)");

        if (estateType.equals("Haus")) {
            House house = House.create(
                    FormUtil.readString("Stadt"),
                    FormUtil.readString("Postleitzahl"),
                    FormUtil.readString("Straße"),
                    FormUtil.readString("Hausnummer"),
                    FormUtil.readInt("Fläche"),
                    null,
                    FormUtil.readInt("Stockwerke"),
                    FormUtil.readInt("Kaufpreis"),
                    FormUtil.readBoolean("Garten?")
            );
            System.out.println("Haus mit ID " + house.getId() + " wurde erstellt.");
        } else if (estateType.equals("Apartment")) {
            Apartment apartment = Apartment.create(
                    FormUtil.readString("Stadt"),
                    FormUtil.readString("Postleitzahl"),
                    FormUtil.readString("Straße"),
                    FormUtil.readString("Hausnummer"),
                    FormUtil.readInt("Fläche"),
                    null,
                    FormUtil.readInt("Stockwerk"),
                    FormUtil.readInt("Miete"),
                    FormUtil.readInt("Zimmer"),
                    FormUtil.readBoolean("Balkon?"),
                    FormUtil.readBoolean("Küche?")
            );
            System.out.println("Apartment mit ID " + apartment.getId() + " wurde erstellt.");
        } else {
            System.out.println("Immobilientyp " + estateType + " existiert nicht! Kehre zum Immobilienmenü zurück...");
        }
    }
}

