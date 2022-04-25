package de.dis.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Kleine Helferklasse für Menüs
 * Zuvor müssen mit addEntry Menüoptionen hinzugefügt werden. Mit
 * der Methode show() wird das Menü angezeigt und die mit der Option
 * angegebene Konstante zurückgeliefert.
 * 
 * Beispiel:
 * Menu m = new Menu("Hauptmenü");
 * m.addEntry("Hart arbeiten", 0);
 * m.addEntry("Ausruhen", 1);
 * m.addEntry("Nach Hause gehen", 2);
 * int wahl = m.show();
 * 
 * Angezeigt wird dann das Menü:
 * Hauptmenü:
 * [1] Hart arbeiten
 * [2] Ausruhen
 * [3] Nach Hause gehen
 * --
 */
public class Menu<T> {
	private String title;
	private ArrayList<String> labels = new ArrayList<>();
	private ArrayList<T> returnValues = new ArrayList<T>();
	
	/**
	 * Konstruktor.
	 * @param title Titel des Menüs z.B. "Hauptmenü"
	 */
	public Menu(String title) {
		super();
		this.title = title;
	}
	
	/**
	 * Fügt einen Menüeintrag zum Menü hinzu
	 * @param label Name des Eintrags
	 * @param returnValue Konstante, die bei Wahl dieses Eintrags zurückgegeben wird
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
	 * Zeigt das Menü an
	 * @return Die Konstante des ausgewählten Menüeintrags
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
				System.err.println("Ungültige Eingabe!");
				selection = -1;
			} 
		}
		
		return returnValues.get(selection-1);
	}
}
