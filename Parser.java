import java.util.Scanner;
import java.util.ArrayList;

/**
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 * 
 * @author  Michael Kölling, David J. Barnes, and Syraj Alkhalil
 * @version 28.11.2021
 */
public class Parser 
{
    private final CommandWords commands;  // holds all valid command words
    private final Scanner reader;         // source of command input
    private static final String[] connectives = {"the", "to", "in", "please", "i"};

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser() {
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }
    
    /**
     * @return The next command from the user.
     */
    public Command getCommand() {
        System.out.print("> ");
        String inputLine = reader.nextLine();
        String[] words = inputLine.split("[ ,.;?!]+");  // split the input of the user
        ArrayList<String> finalWords = new ArrayList<>();     // holds what will be passed to the constructor of Command
        
        StringBuilder temp = new StringBuilder();
        boolean flag = false;       // if we encounter a " then we set this to true.

        for(int i = 0; i < words.length; i ++){
            words[i] = words[i].toLowerCase();  // set everything to lowe case before we operate to avoid errors
            if(!flag){
                // the first if is an edge case where the word starts and ends with ". for example "east"
                if(words[i].startsWith("\"") && words[i].endsWith("\"") && words[i].length() != 1){
                    temp = new StringBuilder(words[i].substring(1, words[i].length() - 1));
                    finalWords.add(temp.toString());
                    temp = new StringBuilder();
                }
                // check if the word starts with " if so set the flag to true.
                if(words[i].startsWith("\"")){
                    temp = new StringBuilder(words[i].substring(1));
                    flag = true;
                } else{
                    // just append the word
                    if(!isConnective(words[i])){
                        finalWords.add(words[i]);
                    }
                }
            }else{ // we only check if the word ends with a " here else we know we didn't reach the end of the sentence
                if(!words[i].endsWith("\"")){
                    temp.append(" ").append(words[i]);
                }else{
                    temp.append(" ").append(words[i], 0, words[i].length() - 1);
                    flag = false;
                    finalWords.add(temp.toString().strip());
                    temp = new StringBuilder();
                }
            }
        }

        // set final words to the words from the user for easier access
        for(int i = 0; i < finalWords.size(); i++){
            words[i] = finalWords.get(i);
        }

        // check how many words we have and return a command accordingly.
        if(commands.isCommand(words[0])){
            if(finalWords.size() == 1){
                return new Command(words[0], null);
            }else if (finalWords.size() <= 2){
                return new Command(words[0], words[1]);
            }
            return new Command(words[0], words[1], words[2]);
        }
        return new Command(null, null);
    }
    
    /**
     * Checks if a certain word is a connective.
     */
    private boolean isConnective(String word) {
        for(String connective : connectives){
            if(word.equals(connective)){
                return true;
            }
        }
        return false;
    }

    /**
     * Print out a list of valid command words.
     */
    public void showCommands() {
        commands.showAll();
    }
}