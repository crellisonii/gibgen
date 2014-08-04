package gibgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Gibgen{
	
	
	
	
	String outputGibberish;
	Random random;

	/**
	 * Main method for Gibgen. Reads in the arguments
	 * that are given at the command line.
	 * @param args - the arguments given via the command line
	 */
	public static void main(String[] args){
		if(args.length == 1){
			if(args[0].contains(".txt")){
				File file = new File(args[0]);
				new Gibgen(file);
			}
			else{
				String input = args[0];
				new Gibgen(input);
			}
		}
		else if(args.length > 1){
			new Gibgen(args);
		}
		else{
			System.out.println("No arguments!!");
		}
	}
	
	
	
	/**
	 * Constructor for Gibgen that has a string passed in
	 * as a command line argument.
	 * @param string - a string given at the command line
	 */
	public Gibgen(String string){
		outputGibberish = "";
		random = new Random();
		String word = "";
		int i = 0;
		while(i < string.length()){
			Character letter = string.charAt(i);
			if(string.codePointAt(i) == 32){
				scramble(word);
				word = "";
				i++;
			}
			else{
				word = word + letter;
				i++;
			}
		}
		scramble(word);
		printGibberish();
	}
	
	
	
	/**
	 * Constructor for Gibgen that has a file passed in
	 * as a command line argument.
	 * @param file - the file name given at the command line
	 */
	public Gibgen(File file){
		outputGibberish = "";
		random = new Random();
		try {
			Scanner fileReader = new Scanner(file);
			while(fileReader.hasNext()){
				String word = fileReader.next().trim();
				scramble(word);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
		printGibberish();
	}
	
	
	
	/**
	 * Constructor for Gibgen that has multiple strings passed
	 * in as a command line argument.
	 * @param stringArray - multiple arguments given at the
	 * command line
	 */
	public Gibgen(String[] stringArray){
		outputGibberish = "";
		random = new Random();
		for(String string:stringArray){
			scramble(string);
		}
		printGibberish();
	}
	
	
	
	/**
	 * Command that checks the given word to see if it has
	 * a length of 1 or 2, or is all upper case.  If either is true,
	 * it updates the output string with the words as is.  If false,
	 * it calls the wordToScramble(char[]) method to start scrambling
	 * the message.
	 * @param word - the string of the word to be scrambled.
	 */
	private void scramble(String word){
		if(word.length() == 1 || word.length() == 2){
			updateOutputGibberish(word.toString());
		}
		else if(word.equals(word.toUpperCase())){
			updateOutputGibberish(word);
		}
		else{
			wordToScramble(word.toCharArray());
		}
	}
	
	
	
	/**
	 * Command that calls firstLetter(char[]) to find the
	 * first letter of the given word (with or without a punctuation), 
	 * and calls the lastLetter(char[]) method to find the last letter
	 * of the given word (with or without a punctuation).
	 * It then stores the rest of the letters in the word in an 
	 * ArrayList<Character>. If this list contains a punctuation it calls
	 * the method scrambleWithPunctuation(ArrayList<Character>) to scramble
	 * the letters, or if the list doesn't contain a punctuation it calls the
	 * scrambleLetters(ArrayList<Character>) method to scramble the letters.
	 * @param word - the char[] of the word that is to be scrambled
	 */
	private void wordToScramble(char[] word){
		ArrayList<Character> lettersToScramble = new ArrayList<Character>();
		String gibWord = firstLetter(word);
		String lastLetters = lastLetter(word);
		int index = gibWord.length();
		while(index < (word.length - lastLetters.length())){
			lettersToScramble.add(word[index]);
			index++;
		}
		if(containsPunctuation(lettersToScramble)){
			gibWord = gibWord + scrambleWithPunctuation(lettersToScramble);
			gibWord = gibWord + lastLetters;
			updateOutputGibberish(gibWord);
		}
		else{
			gibWord = gibWord + scrambleLetters(lettersToScramble);
			gibWord = gibWord + lastLetters;
			updateOutputGibberish(gibWord);
		}
	}
	
	
	
	/**
	 * Method to check if the given Character is a punctuation.
	 * @param letter - the given Character to check if it is
	 * a punctuation
	 * @return - true if the Character is a punctuation;
	 * false otherwise
	 */
	private boolean isPunctuation(Character letter){
		boolean isPunctuation = false;
		if(!Character.isLetter(letter) && !Character.isDigit(letter)){
			isPunctuation = true;
		}
		return isPunctuation;
	}
	
	
	
	/**
	 * Method to check if the given ArrayList<Character> contains
	 * a punctuation
	 * @param word - the list of Characters that is to be checked for
	 * punctuation
	 * @return - true if the list contains a punctuation; false otherwise
	 */
	private boolean containsPunctuation(ArrayList<Character> word){
		boolean hasPunctuation = false;
		for(Character letter: word){
			if(isPunctuation(letter)){
				hasPunctuation = true;
			}
		}
		return hasPunctuation;
	}
	
	
	
	/**
	 * Method to scramble the given list of Characters that contain
	 * a punctuation. It keeps track of the index of the punctuation 
	 * and the punctuation, but removes it to scramble just the letters.
	 * Calls scrambleLetters(ArrayList<Character>) to scramble just the
	 * letters.
	 * Calls recombineWithPunctuation(String, int, Character) to put the
	 * punctuation back in its original position.
	 * @param word - the list of Characters with a punctuation that need
	 * to be scrambled
	 * @return - the scrambled word with the punctuation in its proper
	 * place
	 */
	private String scrambleWithPunctuation(ArrayList<Character> word){
		String scrambled = "";
		int punctuationIndex = 0;
		Character punctuation = ' ';
		ArrayList<Character> characterArray = new ArrayList<Character>();
		for(int i = 0; i < word.size(); i++){
			Character character = word.get(i);
			if(isPunctuation(character)){
				punctuationIndex = i;
				punctuation = character;
			}
			else{
				characterArray.add(character);
			}
		}
		String temp = scrambleLetters(characterArray);
		scrambled = recombineWithPunctuation(temp, punctuationIndex, punctuation);
		return scrambled;
	}
	
	
	
	/**
	 * Method to recombine the scrambled word with its punctuation.
	 * @param string - the string of scrambled words
	 * @param index - the index where the punctuation goes
	 * @param punctuation - the punctuation
	 * @return - the scrambled word with the punctuation in its proper
	 * position
	 */
	private String recombineWithPunctuation(String string, int index, Character punctuation){
		ArrayList<Character> characterList = new ArrayList<Character>();
		for(int i = 0; i < string.length(); i++){
			characterList.add(string.charAt(i));
		}
		characterList.add(index, punctuation);
		String word = "";
		for(int i = 0; i < characterList.size(); i++){
			word = word + characterList.get(i);
		}
		return word;
	}
	
	
	
	/**
	 * Method to get the first letter of the given char[].
	 * If the first index is just a letter then it returns
	 * just that character.
	 * If there is a punctuation at the first index it returns
	 * the punctuation and the first letter following.
	 * @param word - the given word to get the first letter
	 * @return - String with the first letter of the word
	 */
	private String firstLetter(char[] word){
		String firstLetter = "";
		int i = 0;
		while(i < word.length){
			if(!Character.isLetter(word[i]) && !Character.isDigit(word[i])){
				firstLetter = firstLetter + word[i];
				i++;
			}
			else if(Character.isDigit(word[i])){
				firstLetter = firstLetter + word[i];
				i++;
			}
			else{
				firstLetter = firstLetter + word[i];
				i = word.length;
			}
		}
		return firstLetter;
	}
	
	
	
	/**
	 * Method to get the last letter of the given char[].
	 * If the last index is a punctuation, then it returns the
	 * punctuation and the character preceeding it.
	 * If the last index is a letter, then it returns the letter
	 * @param word - the given word to get the last letter 
	 * @return - String with the last letter of the word
	 */
	private String lastLetter(char[] word){
		String lastLetter = "";
		int i = word.length -1;
		while(i >= 0){
			if(!Character.isLetter(word[i]) && !Character.isDigit(word[i])){
				lastLetter = word[i] + lastLetter;
				i--;
			}
			else{
				lastLetter = word[i] + lastLetter;
				i = -1;
			}
		}
		return lastLetter;
	}
	
	
	
	/**
	 * Method that scrambles the given ArrayList<Character>.
	 * @param letters - the given list of characters to be scrambled
	 * @return - String of the scrambled letters that were given
	 */
	private String scrambleLetters(ArrayList<Character> letters){
		ArrayList<Character> scrambledLetters = new ArrayList<Character>();
		String string = "";
		while(letters.size() != 0){
			int index = random.nextInt(letters.size());
			scrambledLetters.add(letters.get(index));
			letters.remove(index);
		}
		for(Character character: scrambledLetters){
			string = string + character;
		}
		return string;
	}
	
	
	
	/**
	 * Method to update the output String
	 * @param scrambledWord - the word to add to the output String
	 */
	private void updateOutputGibberish(String scrambledWord){
		outputGibberish = outputGibberish + scrambledWord + " ";
	}
	
	
	
	/**
	 * Method to print the output string to the console.
	 */
	private void printGibberish(){
		System.out.println(outputGibberish);
	}

}
