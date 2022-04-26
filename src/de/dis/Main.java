package de.dis;

import de.dis.console.FormUtil;
import de.dis.console.Menu;
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
                    System.out.println("Bitte Master-Passwort zur Makler-Verwaltung eingeben:");
                    try {
                        for ( int i = 3; i > 0; i--){
                            String input = stdin.readLine();

                            if (input.equals("passwort")) {
                                showMaklerMenu();
                                break;
                            } else {
                                System.out.println("Falsches Passwort! Versuchen Sie nochmal. Sie haben noch " + (i - 1) +" Versuche.");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                case MENU_ESTATE:
                    /* TODO ERROR
                    Makler m = maklerLogin();
                    if (m == null) return;

                    EstateMenu estateMenu = new EstateMenu(m);
                    estateMenu.showEstateMenu();

                     */
                case QUIT:
                    return;
            }
        }
    }

    /* TODO ERROR
    private static Makler maklerLogin() {
        Makler m = null;
        while (m == null) {
            String login = FormUtil.readString("Benutzernamen eingeben:");
            m = Makler.getByLogin(login);
        }
        for (int i = 0; i < 3; i++) {
            String password = FormUtil.readString("Passwort eingeben:");
            if (m.comparePassword(password)) return m;
        }
        return null;
    }

     */


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

}

