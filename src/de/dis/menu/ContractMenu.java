package de.dis.menu;

import de.dis.console.Menu;

public class ContractMenu {

    public static void showContractMenu() {
        //Menüoptionen
        final int NEW_PERSON = 0;
        final int SIGN_CONTRACT = 1;
        final int SHOW_CONTRACTS = 2;
        final int BACK = 3;

        //Verwaltungsmenü Verträge
        Menu<Integer> contractMenu = new Menu("Vertragsverwaltung");
        contractMenu.addEntry("Neue Person anlegen", NEW_PERSON);
        contractMenu.addEntry("Vertrag erstellen", SIGN_CONTRACT);
        contractMenu.addEntry("Verträge anzeigen", SHOW_CONTRACTS);
        contractMenu.addEntry("Zurück zum Hauptmenü", BACK);

        //Verarbeite Eingabe
        while(true) {
            int response = contractMenu.show();

            switch(response) {
                case NEW_PERSON:
                    //newPerson();
                    break;
                case SIGN_CONTRACT:
                    //signContract();
                    break;
                case SHOW_CONTRACTS:
                    //showContracts();
                    break;
                case BACK:
                    return;
            }
        }
    }

}
