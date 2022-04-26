package de.dis.menu;

import de.dis.console.FormUtil;
import de.dis.console.Menu;
import de.dis.console.MenuOption;
import de.dis.data.model.Makler;

public class MaklerMenu {


    /**
     * Zeigt die Maklerverwaltung
     */
    public static void showMaklerMenu() {
        //Menüoptionen
        final int NEW_MAKLER = 0;
        final int EDIT_MAKLER = 1;
        final int BACK = 2;

        //Maklerverwaltungsmenü
        Menu<Integer> maklerMenu = new Menu("Makler-Verwaltung");
        maklerMenu.addEntry("Neuer Makler", NEW_MAKLER);
        maklerMenu.addEntry("Makler bearbeiten", EDIT_MAKLER);
        maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);

        //Verarbeite Eingabe
        while (true) {
            int response = maklerMenu.show();

            switch (response) {
                case NEW_MAKLER:
                    newMakler();
                    break;
                case EDIT_MAKLER:
                    showEditMakler();
                    break;
                case BACK:
                    return;
            }
        }
    }

    private static void showEditMakler() {
        //Menüoptionen
        final int UPDATE_MAKLER = 0;
        final int DELETE_MAKLER = 1;
        final int BACK = 2;

        //Immobilienverwaltungsmenü
        Menu<Integer> estateMenu = new Menu("Makler-Verwaltung");
        estateMenu.addEntry("Makler entfernen", DELETE_MAKLER);
        estateMenu.addEntry("Makler bearbeiten", UPDATE_MAKLER);
        estateMenu.addEntry("Zurück zum Hauptmenü", BACK);

        //Verarbeite Eingabe
        while (true) {
            int response = estateMenu.show();

            switch (response) {
                case DELETE_MAKLER:
                    deleteMakler();
                    break;
                case UPDATE_MAKLER:
                    updateMakler();
                    break;
                case BACK:
                    return;
            }
        }
    }

    private static void updateMakler() {
        System.out.println("Bitte Makler-ID des zu ändernden Maklers eingeben:");
        int id = FormUtil.readInt("Makler-ID");
        Makler m = Makler.get(id);

        //Makler Felder:
        final int NAME = 1;
        final int ADDRESS = 2;
        final int LOGIN = 3;
        final int PASSWORD = 4;

        if (m != null) {
            int selection = FormUtil.readSelection("Bitte das zu bearbeitende Attribut auswählen",
                    new MenuOption<>(String.format("Name (%s)", m.getName()), NAME),
                    new MenuOption<>(String.format("Addresse (%s)", m.getAddress()), ADDRESS),
                    new MenuOption<>(String.format("Login (%s)", m.getLogin()), LOGIN),
                    new MenuOption<>(String.format("Password (%s)", "****"), PASSWORD));

            switch (selection) {
                case NAME -> m.setName(FormUtil.readString("Neuer Name:"));
                case ADDRESS -> m.setAddress(FormUtil.readString("Neue Addresse:"));
                case LOGIN -> m.setLogin(FormUtil.readString("Login: "));
                case PASSWORD -> m.setPassword(FormUtil.readString("Neues Passwort: "));
            }
        }else {
            System.out.println("Makler mit der ID " + id + " existiert nicht! Kehre zum Maklermenü zurück...");
        }
    }

    private static void deleteMakler() {
        System.out.println("Bitte Makler-ID des zu löschenden Maklers eingeben:");
        int id = FormUtil.readInt("Makler-ID");
        Makler m = Makler.get(id);
        if (m != null) {
            Makler.delete(m);
            System.out.println("Erfolg! Makler mit der ID "+id+" wurde gelöscht.\n Kehre zum Makler-Menü zurück...");
        } else {
            System.out.println("Makler mit der "+id+" existiert nicht.\n Kehre zum Makler-Menü zurück...");
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
