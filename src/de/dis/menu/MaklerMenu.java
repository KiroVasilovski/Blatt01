package de.dis.menu;

import de.dis.console.FormUtil;
import de.dis.console.Menu;
import de.dis.console.MenuOption;
import de.dis.data.model.Makler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        boolean remain = true;
        do {
            int response = maklerMenu.show();

            switch (response) {
                case NEW_MAKLER -> newMakler();
                case EDIT_MAKLER -> showEditMakler();
                case BACK -> remain = false;
            }
        } while (remain);
    }

    private static Makler selectMakler() {
        Set<Makler> agents = Makler.getAll();
        List<MenuOption<Makler>> list = new ArrayList<>();
        for (Makler agent : agents) {
            MenuOption<Makler> agentOption = new MenuOption<>(agent.toString(), agent);
            list.add(agentOption);
        }
        MenuOption<Makler>[] options = list.toArray(new MenuOption[0]);
        return FormUtil.readSelection("Bitte Makler auswählen:", options);
    }

    private static void showEditMakler() {
        System.out.println("Bitte Makler-ID des zu ändernden Maklers eingeben:");
        Makler m = selectMakler();
        if (m == null) return;

        final int BACK = -1;
        final int DELETE = -2;

        //Makler Felder:
        final int NAME = 1;
        final int ADDRESS = 2;
        final int LOGIN = 3;
        final int PASSWORD = 4;

        boolean remain = true;
        do {
            int selection = FormUtil.readSelection("Bitte das zu bearbeitende Attribut auswählen",
                    new MenuOption<>(String.format("Name (%s)", m.getName()), NAME),
                    new MenuOption<>(String.format("Addresse (%s)", m.getAddress()), ADDRESS),
                    new MenuOption<>(String.format("Login (%s)", m.getLogin()), LOGIN),
                    new MenuOption<>(String.format("Password (%s)", "****"), PASSWORD),
                    new MenuOption<>("Makler löschen", DELETE),
                    new MenuOption<>("Zurück", BACK));

            switch (selection) {
                case NAME -> m.setName(FormUtil.readString("Neuer Name:"));
                case ADDRESS -> m.setAddress(FormUtil.readString("Neue Addresse:"));
                case LOGIN -> m.setLogin(FormUtil.readString("Login: "));
                case PASSWORD -> m.setPassword(FormUtil.readString("Neues Passwort: "));
                case DELETE -> {
                    if (FormUtil.readBoolean("Bitte bestätigen, dass der Makler endgültig gelöscht werden soll")) {
                        Makler.delete(m);
                        remain = false;
                    }
                }
                case BACK -> remain = false;
            }
        } while (remain);
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
