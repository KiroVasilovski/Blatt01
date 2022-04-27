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
     * Shows the broker management
     */
    public static void showMaklerMenu() {
        //Menu options
        final int NEW_MAKLER = 0;
        final int EDIT_MAKLER = 1;
        final int BACK = 2;

        //Broker management menu
        Menu<Integer> maklerMenu = new Menu("Broker management");
        maklerMenu.addEntry("New broker", NEW_MAKLER);
        maklerMenu.addEntry("Edit broker", EDIT_MAKLER);
        maklerMenu.addEntry("Back to main menu", BACK);

        //Processed input
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
        return FormUtil.readSelection("Please select broker", options);
    }

    private static void showEditMakler() {
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
            int selection = FormUtil.readSelection("Please select the attribute to be edited",
                    new MenuOption<>(String.format("Name (%s)", m.getName()), NAME),
                    new MenuOption<>(String.format("Address (%s)", m.getAddress()), ADDRESS),
                    new MenuOption<>(String.format("Login (%s)", m.getLogin()), LOGIN),
                    new MenuOption<>(String.format("Password (%s)", "****"), PASSWORD),
                    new MenuOption<>("Delete broker", DELETE),
                    new MenuOption<>("Back", BACK));

            switch (selection) {
                case NAME -> m.setName(FormUtil.readString("New name:"));
                case ADDRESS -> m.setAddress(FormUtil.readString("New address:"));
                case LOGIN -> m.setLogin(FormUtil.readString("Login: "));
                case PASSWORD -> m.setPassword(FormUtil.readString("New password: "));
                case DELETE -> {
                    if (FormUtil.readBoolean("Please confirm that the broker is to be permanently deleted")) {
                        Makler.delete(m);
                        remain = false;
                    }
                }
                case BACK -> remain = false;
            }
        } while (remain);
    }

    /**
     * Creates a new broker after the user has
     * entered the corresponding data.
     */
    public static void newMakler() {
        Makler m = Makler.create(
                FormUtil.readString("Name"),
                FormUtil.readString("Address"),
                FormUtil.readString("Login"),
                FormUtil.readString("Password")
        );

        System.out.println("Broker with the ID " + m.getId() + " was created.");
    }
}
