package unal.edu.co.dao.utils;

import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Tokenizer {
	
	public static boolean isImportORPackageLine = false;
	public static List<String> packagesImported;
	public static List<String> constantes;
	
	public static String[] palabrasClave = {
		"EXCEPTION","ID"
	};

	public static boolean isUpperCaseString(String word) {
		return word.toUpperCase().equals(word);
	}
	
	public static boolean isImportORPackageWord(String word) {
		String analizedWord = word.trim();
		boolean response = false;
		if(analizedWord.equals("import") || analizedWord.equals("package") && !isUpperCaseString(analizedWord)) {
			response = true;
		} 
		return response;
	}
	
	public static boolean isImportPackage(String word) {
		int countDots = StringUtils.countMatches(word, ".");
		boolean importPackage = false;
		if(countDots > 0 && isImportORPackageLine) {
			importPackage = true;
		} 
		return importPackage;
	}

	@SuppressWarnings("unused")
	public static String  getTokens(StringReader reader) {
		packagesImported = new ArrayList<String>();
		constantes = new ArrayList<String>();
		String tokens = "";
		try {
			StreamTokenizer st = new StreamTokenizer(reader);

			// Prepare the tokenizer for Java-style tokenizing rules
			st.parseNumbers();
			st.wordChars('_', '_');
			st.eolIsSignificant(true);

			// If whitespace is not to be discarded, make this call
			st.ordinaryChars(0, ' ');

			// These calls caused comments to be discarded
			st.slashSlashComments(true);
			st.slashStarComments(true);
			

			// Parse the file
			int token = st.nextToken();
			while (token != StreamTokenizer.TT_EOF) {
				token = st.nextToken();
				//System.out.println("----" + st);
				switch (token) {
				case StreamTokenizer.TT_NUMBER:
					// A number was found; the value is in nval
					double num = st.nval;
					break;
				case StreamTokenizer.TT_WORD:
					// A word was found; the value is in sval
					String word = st.sval;
					tokens += " ";
					if(word.endsWith(".*")) {
						System.out.println("PALABRA QUE ES ------"+word);
					}
					if(word.toLowerCase().contains("exception") || isUpperCaseString(word) || isImportPackage(word) || word.equals("import") || word.equals("package")) {
						if(isUpperCaseString(word) && word.contains("_") || !isImportPackage(word) && isUpperCaseString(word)) {
							word = word.replaceAll("_", " ").toLowerCase();
							constantes.add(word);
							tokens += word.toString().toLowerCase();
						} else {
							packagesImported.add(word);
						}
						if(isImportORPackageWord(word)) {
							isImportORPackageLine = true;
						} else {
							isImportORPackageLine = false;
						}
						
					} else {
						//tokens += word.replaceAll("(?=\\p{Upper})", " ") + " ";
						isImportORPackageLine = false;
						if(word.contains("_")) {
							word = word.replaceAll("_", " ").toLowerCase();
						}
						word =  word.replaceAll(String.format("%s|%s|%s","(?<=[A-Z])(?=[A-Z][a-z])","(?<=[^A-Z])(?=[A-Z])","(?<=[A-Za-z])(?=[^A-Za-z])")," ").toLowerCase();
						if(word.contains(".")) {
							tokens +=  word.replaceAll(".","").toLowerCase();
						} else if(word.contains("_")) {
							//tokens +=  word.replaceAll("_"," ");
							packagesImported.add(word);
						} else {
							tokens +=  word.toLowerCase();
						}
						
					}
					break;
				case '"':
					// A double-quoted string was found; sval contains the
					// contents
					String dquoteVal = st.sval;
					break;
				case '\'':
					// A single-quoted string was found; sval contains the
					// contents
					
					String squoteVal = st.sval;
					//System.out.println("Palabra: " + squoteVal);
					break;
				case StreamTokenizer.TT_EOL:
					// End of line character found
					break;
				case StreamTokenizer.TT_EOF:
					// End of file has been reached
					break;
				default:
					// A regular character was found; the value is the token
					// itself
					char ch = (char) st.ttype;
					break;
				}
			}
			//System.out.println("terminó de procesar el diff" + tokens);
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
		return tokens;
	}

}
