package front;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The Scanner Class, creates a REGEX list of entities in the input stream
 * Named Scan as Scanner already exists in java
 */
public class Scan {
	private List<Token> tokens;
	
	/**
	 * Main and only constructor for the scan class
	 * @param file filename to be passed in
	 */
	Scan(List<String> file) {
		tokens = tokenize(file);
	}
	
	/**
	 * Getter for the token of a specific
	 * @return the list of tokens
	 */
	public List<Token> GetTokens() {
		return tokens;
	}
	
	public void PrintTokens(){
		for (Token tok : this.tokens) {
			System.out.println(tok.token + "\t" + tok.type.toString());
		}
	}
	
	/**
	 * The Pattern List for this Scanner
	 */
	public static Pattern REGEX = Pattern.compile(buildRegularExpression());

	/**
	 * Takes a string of text and splits it into tokens. Each possible token is defined using the global variable REGEX.
	 * @param text a string of text to scan
	 * @return A List of strings containing each found token
	 */
	private static List<Token> tokenize(List<String> text) {
		//The basic logic of this function is to slowly build up tokens character by character.
		//If the current token plus an additional character is still valid according to regular expressions, then the token is still valid.
		//If the current token plus the new character is not valid, then the current token should be added to the list of valid tokens.
		List<Token> tokens = new ArrayList<Token>(); //List to return
		String currentToken = "";
		String currentWithNewChar;
		int lineNumber = 0;
		//Loop through every character in the string
		for(String line : text) {
			lineNumber++;
			for (char character : line.toCharArray()) {
				if(!isValidCharacter(character)) {
					System.out.println("Error: '" + character + "' not recognized");
					
				}
				currentWithNewChar = currentToken + character; //Testing current token with appended new character
				if (stringMatchesToken(currentWithNewChar)) { //If any regular expression matches, then the current token has the new character appended
					currentToken = currentWithNewChar;
				} else {
					//If no regular expression matches, then the current token is finished.
					//If the current token is valid, add it to our list of tokens
					if (stringMatchesToken(currentToken) && !isInvalidWord(currentToken)) {
						tokens.add(new Token(currentToken, lineNumber));
					} else if(isInvalidWord(currentToken)) {
						System.out.println("Error: '" + currentToken + "' not recognized");
					}
					currentToken = String.valueOf(character);
				}
				//Reset current token to be the newest character
				//currentToken = String.valueOf(character);
			}
		}
		//At end of loop check if the current token is valid. If so, add it to our list of tokens.
		if (stringMatchesToken(currentToken) && !isInvalidWord(currentToken)) {
			tokens.add(new Token(currentToken, lineNumber));
		} else if(isInvalidWord(currentToken)) {
			System.out.println("Error: '" + currentToken + "' not recognized");
		}
		
		return tokens;
	}
	
	/**
	 * Checks that all identifiers and digits are of the correct form.
	 * @param value. string that is being checked
	 * @return true or false depending on validity
	 */
	private static boolean isInvalidWord(String value) {
		Pattern antiREGEX = Pattern.compile("//d//w*//[a-zA-Z]//w*");
		Matcher test = antiREGEX.matcher(value);
		return test.matches();
	}
	
	/**
	 * Checks if any unsupported characters are in the string
	 * @param character character in code
	 * @return true if character is supported. False if not
	 */
	private static boolean isValidCharacter(char character) {
		switch(character) {
		case '@':
		case '#':
		case '$':
		case '[':
		case ']':
		case '\"':
		case '\'':
		case '.':
		case '\\':
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if a string matches any token in the global variable REGEX
	 * @param value The string to check
	 * @return A boolean. True if there is a match. False otherwise
	 */
	private static boolean stringMatchesToken(String value) {
		Matcher test = REGEX.matcher(value); //Checks for matches using the global REGEX variable.
		return test.matches();
	}
	
	
	/**
	 * Builds a regular expression to separate out tokens
	 * @return string usable by Pattern.compile()
	 */
	private static String buildRegularExpression() {
		String string = "\\{|\\}|"; 				// { and }
		string = string + "\\(|\\)|"; 				// ( and )
		string = string + "\\w+|"; 					// accepts any expressions that only use letters and digits that start with a letter
		string = string + "/|\\*|"; 				// / and *
		string = string + "\\+|\\-|"; 				// + and -
		string = string + "\\^|\\|"; 				// ^ and |
		string = string + "&|%|"; 					// & and %
		string = string + "\\!|"; 					// !
		string = string + ";|\\=|";      			// ; and =
		string = string + "0x[a-f0-9]+"; 			// Accepts hex input
		string = string + "\\+\\+|"; 				// ++
		string = string + "\\-\\-|"; 				// --
		string = string + "\\-\\=";  				// -=
		string = string + "\\+\\=";  				// +=
		string = string + "\\*\\="; 				// *=
		string = string + "\\/\\=";  				// /=
		string = string + "\\!\\=";					// !=
		string = string + "\\=\\=";					// ==
		string = string + "\\&\\&";					// &&
		string = string + "\\|\\|";					// ||
		return string;
	}
}
