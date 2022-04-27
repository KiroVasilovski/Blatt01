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
 * Main class
 */
public class Main {
    /**
     * Starts the application
     */
    public static void main(String[] args) {
        showMainMenu();
    }

    /**
     * Shows the main menu
     */
    public static void showMainMenu() {
        //Menüoptionen
        final int MENU_MAKLER = 0;
        final int MENU_ESTATE = 1;
        final int MENU_CONTRACT = 2;
        final int QUIT = 3;

        //Erzeuge Menü
        Menu<Integer> mainMenu = new Menu("Main menu");
        mainMenu.addEntry("Broker management", MENU_MAKLER);
        mainMenu.addEntry("Estate management", MENU_ESTATE);
        mainMenu.addEntry("Contract management", MENU_CONTRACT);
        mainMenu.addEntry("End", QUIT);

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        //Verarbeite Eingabe
        boolean remain = true;
        do {
            int response = mainMenu.show();

            switch (response) {
                case MENU_MAKLER -> {
                    System.out.println("Please enter the master password for broker administration:");
                    try {
                        for (int i = 3; i > 0; i--) {
                            String input = stdin.readLine();
                            if (input.equals("password")) {
                                MaklerMenu.showMaklerMenu();
                                break;
                            } else {
                                System.out.println("Wrong password! You still have " + (i - 1) + " tries");
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
            String login = FormUtil.readString("Enter user name:");
            m = Makler.getByLogin(login);
        }
        for (int i = 0; i < 3; i++) {
            String password = FormUtil.readString("Enter password:");
            if (m.comparePassword(password)) return m;
        }
        return null;
    }

}

