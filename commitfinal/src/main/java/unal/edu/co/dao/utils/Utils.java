package unal.edu.co.dao.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class Utils {

	public static void readCommitDiff(String diff) {
		String str;
		BufferedReader reader = new BufferedReader(new StringReader(diff));
		int added = 0, removed = 0;
		try {
			while ((str = reader.readLine()) != null) {
				if (str.length() > 0) {
					if(str.trim().startsWith("+") && !str.trim().startsWith("++")) {
						added++;
						//System.out.println("Es una adición en el commit: " + str.trim());
					} else if(str.trim().startsWith("-") && !str.trim().startsWith("--")) {
						removed++;
						//System.out.println("Es una eliminación en el commit");
					}
				}
			}
			System.out.println("Finalmente se adicionaron: " + added + " y se eliminaron: " + removed + " lineas");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	public static StringReader getAddedLines(String diff) {
		String str;
		BufferedReader reader = new BufferedReader(new StringReader(diff));
		int added = 0, removed = 0;
		String addedLines = "";
		String removedLines = "";
		try {
			while ((str = reader.readLine()) != null) {
				if (str.length() > 0) {
					if(str.trim().startsWith("+") && !str.trim().startsWith("++")) {
						added++;
						//System.out.println("Es una adición en el commit: " + str.trim());
						str = str.replace("+", "");
						addedLines += str;
						addedLines += "\n";
					} else if(str.trim().startsWith("-") && !str.trim().startsWith("--")) {
						removed++;
						str = str.replace("-", "");
						removedLines += str;
						removedLines += "\n";
					}
				}
			}
			//System.out.println("Finalmente se adicionaron: " + added + " y se eliminaron: " + removed + " lineas");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("Finalmente se adicionaron: " + added + addedLines);
		return new StringReader(addedLines + "\n" + removedLines);
	}
	
	@SuppressWarnings("unused")
	public static String getAddedLinesAsString(String diff) {
		String str;
		BufferedReader reader = new BufferedReader(new StringReader(diff));
		int added = 0, removed = 0;
		String addedLines = "";
		String removedLines = "";
		try {
			while ((str = reader.readLine()) != null) {
				if (str.length() > 0) {
					if(str.trim().startsWith("+") && !str.trim().startsWith("++")) {
						added++;
						//System.out.println("Es una adición en el commit: " + str.trim());
						str = str.replace("+", "");
						addedLines += str;
						addedLines += "\n";
					} else if(str.trim().startsWith("-") && !str.trim().startsWith("--")) {
						removed++;
						str = str.replace("-", "");
						removedLines += str;
						removedLines += "\n";
					}
				}
			}
			//System.out.println("Finalmente se adicionaron: " + added + " y se eliminaron: " + removed + " lineas");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("Finalmente se adicionaron: " + added + addedLines);
		return addedLines + "\n" + removedLines;
	}
	
}
