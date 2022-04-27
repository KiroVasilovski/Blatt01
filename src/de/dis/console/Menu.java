package de.dis.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Small helper class for menus
 * Before that, menu options must be added with addEntry.
 * With the method show() the menu is displayed and the
 * constant specified with the option is returned.
 * 
 * Example:
 * Menu m = new Menu("Main menu");
 * m.addEntry("Work hard", 0);
 * m.addEntry("Rest", 1);
 * m.addEntry("Go home", 2);
 * int selection = m.show();
 * 
 * The menu is then displayed:
 * Main menu:
 * [1] Work hard
 * [2] Rest
 * [3] Go home
 * --
 */
public class Menu<T> {
	private String title;
	private ArrayList<String> labels = new ArrayList<>();
	private ArrayList<T> returnValues = new ArrayList<T>();
	
	/**
	 * Constructor.
	 * @param title Title of the menu e.g. "Main menu"
	 */
	public Menu(String title) {
		super();
		this.title = title;
	}
	
	/**
	 * Adds a menu item to the menu
	 * @param label Entry name
	 * @param returnValue Constant returned when this entry is selected
	 */
	public void addEntry(String label, T returnValue) {
		this.labels.add(label);
		this.returnValues.add(returnValue);
	}

	public void addEntry(MenuOption<T> option) {
		this.labels.add(option.label());
		this.returnValues.add(option.value());
	}
	
	/**
	 * Displays the menu
	 * @return The constant of the selected menu item
	 */
	public T show()  {
		int selection = -1;
		
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		
		while(selection == -1) {
			System.out.println(title+":");
			
			for(int i = 0; i < labels.size(); ++i) {
				System.out.println("["+(i+1)+"] "+labels.get(i));
			}
			
			System.out.print("-- ");
			try {
				selection = Integer.parseInt(stdin.readLine());
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}

			if(selection < 1 || selection > returnValues.size()) {
				System.err.println("Invalid input!");
				selection = -1;
			} 
		}
		
		return returnValues.get(selection-1);
	}
}
