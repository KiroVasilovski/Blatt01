package de.dis;

import de.dis.console.FormUtil;
import de.dis.console.Menu;
import de.dis.data.model.EstateAgent;
import de.dis.menu.ContractMenu;
import de.dis.menu.EstateMenu;
import de.dis.menu.EstateAgentMenu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Hauptklasse
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
        final int MENU_AGENT = 0;
        final int MENU_ESTATE = 1;
        final int MENU_CONTRACT = 2;
        final int QUIT = 3;

        //Erzeuge Menü
        Menu<Integer> mainMenu = new Menu("Main menu");
        mainMenu.addEntry("Broker management", MENU_AGENT);
        mainMenu.addEntry("Estate management", MENU_ESTATE);
        mainMenu.addEntry("Contract management", MENU_CONTRACT);
        mainMenu.addEntry("Quit", QUIT);

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        //Verarbeite Eingabe
        boolean remain = true;
        do {
            int response = mainMenu.show();

            switch (response) {
                case MENU_AGENT -> {
                    System.out.println("Please enter the master password for broker administration");
                    try {
                        for (int i = 3; i > 0; i--) {
                            String input = stdin.readLine();
                            if (input.equals("password")) {
                                EstateAgentMenu.showEstateAgentMenu();
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
                    EstateAgent agent = showAgentLogin();
                    if (agent == null) return;

                    EstateMenu estateMenu = new EstateMenu(agent);
                    estateMenu.showEstateMenu();
                }
                case MENU_CONTRACT -> {
                    EstateAgent agent = showAgentLogin();
                    if (agent == null) return;

                    ContractMenu contractMenu = new ContractMenu(agent);
                    contractMenu.showContractMenu();
                }
                case QUIT -> remain = false;
            }
        } while (remain);
    }

    private static EstateAgent showAgentLogin() {
        EstateAgent agent = null;
        while (agent == null) {
            String login = FormUtil.readString("Enter user name");
            agent = EstateAgent.getByLogin(login);
        }
        for (int i = 0; i < 3; i++) {
            String password = FormUtil.readString("Enter password");
            if (agent.comparePassword(password)) return agent;
        }
        return null;
    }

}

