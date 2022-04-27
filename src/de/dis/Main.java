package de.dis;

import de.dis.console.FormUtil;
import de.dis.console.Menu;
import de.dis.data.model.Makler;
import de.dis.menu.ContractMenu;
import de.dis.menu.EstateMenu;
import de.dis.menu.MaklerMenu;

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
        boolean remain = true;
        do {
            int response = mainMenu.show();

            switch (response) {
                case MENU_MAKLER -> {
                    System.out.println("Bitte Master-Passwort zur Makler-Verwaltung eingeben:");
                    try {
                        for (int i = 3; i > 0; i--) {
                            String input = stdin.readLine();
                            if (input.equals("passwort")) {
                                MaklerMenu.showMaklerMenu();
                                break;
                            } else {
                                System.out.println("Falsches Passwort! Sie haben noch " + (i - 1) + " Versuche");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case MENU_ESTATE -> {
                    Makler m = maklerLogin();
                    if (m == null) return;

                    EstateMenu estateMenu = new EstateMenu(m);
                    estateMenu.showEstateMenu();
                }
                case MENU_CONTRACT -> {
                    Makler m = maklerLogin();
                    if (m == null) return;

                    ContractMenu contractMenu = new ContractMenu(m);
                    contractMenu.showContractMenu();
                }
                case QUIT -> remain = false;
            }
        } while (remain);
    }

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

}

