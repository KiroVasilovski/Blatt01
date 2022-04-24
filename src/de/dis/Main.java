package de.dis;

import de.dis.data.model.Makler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        Menu mainMenu = new Menu("Hauptmenü");
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
        Menu maklerMenu = new Menu("Makler-Verwaltung");
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
        Menu estateMenu = new Menu("Immobilien-Verwaltung");
        estateMenu.addEntry("Neue Immobilie", NEW_ESTATE);
        estateMenu.addEntry("Immobilie entfernen", DEL_ESTATE);
        estateMenu.addEntry("Immobilie bearbeiten", UPDATE_ESTATE);
        estateMenu.addEntry("Zurück zum Hauptmenü", BACK);

        //Verarbeite Eingabe
        while (true) {
            int response = estateMenu.show();

            switch (response) {
                case NEW_ESTATE:
                    //TODO-newEstate();
                    break;
                case DEL_ESTATE:
                    //TODO-delEstate();
                    break;
                case UPDATE_ESTATE:
                    //TODO-updateEstate();
                    break;
                case BACK:
                    return;
            }
        }
    }
}

