package de.dis.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Small helper class for reading in form data
 */
public class FormUtil {
	/**
	 * Reads a string from the standard input
	 * @param label Line shown before the input
	 * @return read line
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
	 * Reads a Boolean from the standard input
	 * @param label Line shown before the input
	 * @return read Boolean
	 */
	public static boolean readBoolean(String label) {
		boolean ret = false;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.print(label+" (+ or -): ");
			ret = stdin.readLine().trim().equals("+");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	/**
	 * Reads an Integer from the standard input
	 * @param label Line shown before the input
	 * @return read Integer
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
				System.err.println("Invalid entry: Please enter a number!");
			}
		}
		
		return ret;
	}

	/**
	 * Displays a menu from which the user selects one of several options.
	 *
	 * @param label the description of the menu
	 * @param options the individual elements as MenuOptions (tuples of type (String label, T option))
	 * @param <T> the type of objects the user can choose between
	 * @return the object that the user has selected
	 */
	public static <T> T readSelection(String label, MenuOption<T>... options) {
		if (options == null || options.length == 0) return null;

		Menu<T> menu = new Menu<T>(label);
		for (MenuOption<T> option : options) {
			menu.addEntry(option);
		}

		return menu.show();
	}

	/**
	 * Reads out a date as a sequence of day, month, year.
	 * @param label Line that is displayed before the input
	 * @return a LocalDate of the date read in
	 */
	public static LocalDate readDate(String label) {
		while (true) {
			System.out.println(label + " - Enter date");
			int day = readInt("Day");
			int month = readInt("Month");
			int year = readInt("Year");

			try {
				return LocalDate.of(year, month, day);
			} catch (DateTimeException e) {
				System.out.println("Invalid date entered - please try again!");
			}
		}
	}
}
