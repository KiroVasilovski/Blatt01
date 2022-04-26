package de.dis.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Kleine Helferklasse zum Einlesen von Formulardaten
 */
public class FormUtil {
	/**
	 * Liest einen String vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesene Zeile
	 */
	public static String readString(String label) {
		String ret = null;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.print(label+": ");
			ret = stdin.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	/**
	 * Liest einen Boolean vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesener Boolean
	 */
	public static boolean readBoolean(String label) {
		boolean ret = false;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.print(label+" (+ oder -): ");
			ret = stdin.readLine().trim().equals("+");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	/**
	 * Liest einen Integer vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesener Integer
	 */
	public static int readInt(String label) {
		int ret = 0;
		boolean finished = false;

		while(!finished) {
			String line = readString(label + ": ");
			
			try {
				ret = Integer.parseInt(line);
				finished = true;
			} catch (NumberFormatException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie eine Zahl an!");
			}
		}
		
		return ret;
	}

	/**
	 * Zeigt ein Menü an, über das der Nutzer eine von mehreren Auswahlmöglichkeiten
	 * selektiert.
	 *
	 * @param label die Beschreibung des Menüs
	 * @param options die einzelnen Elemente als MenuOptions (Tupel vom Typ (String label, T option))
	 * @param <T> die Art der Objekte, zwischen denen der Nutzer wählen kann
	 * @return das Objekt, das der Nutzer ausgewählt hat
	 */
	public static <T> T readSelection(String label, MenuOption<T>... options) {
		Menu<T> menu = new Menu<T>(label);
		for (MenuOption<T> option : options) {
			menu.addEntry(option);
		}
		return menu.show();
	}

	/**
	 * Liest ein Datum als Folge von Tag, Monat, Jahr aus.
	 * @param label Zeile, die vor der Eingabe angezeigt wird
	 * @return ein LocalDate des eingelesenen Datums
	 */
	public static LocalDate readDate(String label) {
		while (true) {
			System.out.println(label + " - Datum eingeben");
			int day = readInt("Tag");
			int month = readInt("Monat");
			int year = readInt("Jahr");

			try {
				return LocalDate.of(year, month, day);
			} catch (DateTimeException e) {
				System.out.println("Ungültiges Datum eingegeben - bitte erneut versuchen!");
			}
		}
	}
}
